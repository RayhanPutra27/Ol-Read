package rpl.ezy.olread.view.auth


import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.R
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.response.ResponseSignup
import rpl.ezy.olread.utils.ConstantUtils.EMAIL
import rpl.ezy.olread.utils.ConstantUtils.PROFIL
import rpl.ezy.olread.utils.ConstantUtils.STATUS
import rpl.ezy.olread.utils.ConstantUtils.USER
import rpl.ezy.olread.utils.ConstantUtils.USERNAME
import rpl.ezy.olread.utils.ConstantUtils.USER_ID
import rpl.ezy.olread.utils.SharedPreferenceUtils
import rpl.ezy.olread.view.user.AddRecipesActivity
import rpl.ezy.olread.view.user.UserActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 */
class SignupFragment : Fragment() {

    private var sharedPreferences : SharedPreferenceUtils?= null
    var btnSignup: Button? = null
    var etUsername: EditText? = null
    var etEmail: EditText? = null
    var etPassword: EditText? = null
    var imgProfile: CircleImageView? = null
    var loading: ProgressDialog? = null
    private lateinit var imageData: MultipartBody.Part

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup, container, false)


        btnSignup = view.findViewById(R.id.bt_signup)
        etUsername = view.findViewById(R.id.et_username_reg)
        etEmail = view.findViewById(R.id.et_email_reg)
        etPassword = view.findViewById(R.id.et_pass_reg)
        imgProfile = view.findViewById(R.id.nav_profile)
        loading = ProgressDialog(context!!)
        loading!!.setCancelable(false)

        imgProfile!!.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    //permission already granted
                    pickImageFromGallery()
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }

        signup()
        val window = activity!!.window
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.green_1)

        return view
    }

    fun signup() {

        sharedPreferences = SharedPreferenceUtils(context!!)
        btnSignup?.setOnClickListener {
            val username = RequestBody.create(MediaType.parse("multipart/form-data"), etUsername!!.getText().toString())
            val email = RequestBody.create(MediaType.parse("multipart/form-data"), etEmail!!.getText().toString())
            val password = RequestBody.create(MediaType.parse("multipart/form-data"), etPassword!!.getText().toString())
            val user = RequestBody.create(MediaType.parse("multipart/form-data"), USER.toString())
            if (ValidationSignup()){
                ResponseSignup( username, email, password, user )
            }
        }
    }

    fun ValidationSignup(): Boolean {
        if (etUsername?.text?.isEmpty()!!){
            etUsername?.error = "Isi username"
            etUsername?.requestFocus()
            return false
        }
        if (etEmail?.text?.isEmpty()!!) {
            etEmail?.error = "Isi email"
            etEmail?.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail?.text).matches()) {
            etEmail?.error = "Masukan email yang benar"
            etEmail?.requestFocus()
            return false
        }
        if (etPassword?.text?.isEmpty()!!) {
            etPassword?.error = "Isi pass"
            etPassword?.requestFocus()
            return false
        }
        if (etPassword?.length()!! < 8) {
            etPassword?.error = "Password karakter minimal 8"
            etPassword?.requestFocus()
            return false
        }

        return true
    }

    private fun ResponseSignup(username: RequestBody, email: RequestBody, pass: RequestBody, user: RequestBody){
        loading!!.setMessage("Loading ...")
        loading!!.show()
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.userSignup(username, email, pass, user, imageData)
        call.enqueue(object : Callback<ResponseSignup> {
            override fun onFailure(call: Call<ResponseSignup>, t: Throwable) {
                loading!!.cancel()
                Toast.makeText(
                    context,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN","${t.message}")
            }

            override fun onResponse(call: Call<ResponseSignup>,response: Response<ResponseSignup>) {
                loading!!.cancel()
                if (response.isSuccessful) {
                    if(response.body()!!.status == 200){
                        Toast.makeText(
                            context,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        var data = response.body()!!.data

                        SetDataUser(USER_ID, data.user_id, "")
                        SetDataUser(USERNAME, 0, data.username)
                        SetDataUser(EMAIL, 0, data.email)
                        SetDataUser(PROFIL, 0, data.profil)
                        SetDataUser(STATUS, USER, "")

                        startActivity(Intent(context, UserActivity::class.java))
                        (context as Activity).finish()
                    } else {
                        Toast.makeText(
                            context,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Ada kesalahan server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })
    }

    fun SetDataUser(key: String, int: Int, string: String){
        if (string != ""){
            sharedPreferences!!.setSharedPreferences(key, string)
        } else {
            sharedPreferences!!.setSharedPreferences(key, int)
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createTempFile(bitmap: Bitmap): File {
        val file = File(
            context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            System.currentTimeMillis().toString() + "_image.jpg"
        )
        val bos = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmapdata = bos.toByteArray()

        try {
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
//            val mphoto = data.extras.get("") as Bitmap
            val imageUri = data!!.data
            val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, imageUri)
            uploadImage(bitmap)
            imgProfile!!.setImageURI(imageUri)

            Log.d("IMAGEURI", "$imageUri")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadImage(bitmap: Bitmap) {
        val file = createTempFile(bitmap)
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val body = MultipartBody.Part.createFormData("profil", file.name, reqFile)
        imageData = body

    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSION_CODE = 1001
    }

}
