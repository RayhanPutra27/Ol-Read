package rpl.ezy.olread.view.user

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_edit_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.GlideApp
import rpl.ezy.olread.R
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.response.ResponseUsers
import rpl.ezy.olread.utils.ConstantUtils.ADMIN
import rpl.ezy.olread.utils.ConstantUtils.PROFIL
import rpl.ezy.olread.utils.ConstantUtils.RECIPE
import rpl.ezy.olread.utils.ConstantUtils.STATUS
import rpl.ezy.olread.utils.ConstantUtils.USER_ID
import rpl.ezy.olread.utils.SharedPreferenceUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class EditProfile : AppCompatActivity() {

    var profile: String? = null
    private lateinit var imageData: MultipartBody.Part
    var imageEdit: Boolean? = null
    var user_id: Int? = null
    private var sharedPreference : SharedPreferenceUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        sharedPreference = SharedPreferenceUtils(this@EditProfile)
        user_id = 0
        imageEdit = false

        if(intent != null) {
            if( intent.getStringExtra("type") == RECIPE){
                edit_image.visibility = View.GONE
            } else {
                user_id = intent.getIntExtra(USER_ID, 0)
                edit_image.visibility = View.VISIBLE
            }

            profile = intent.getStringExtra(PROFIL)

            GlideApp.with(this@EditProfile)
                .load(profile)
                .into(image_profile)
        }

        if(sharedPreference!!.getIntSharedPreferences(STATUS) == ADMIN){
            edit_image.visibility = View.GONE
        } else {
            edit_image.visibility = View.VISIBLE
        }

        img_back.setOnClickListener {
            if(imageEdit!!){
                val builder = AlertDialog.Builder(this@EditProfile)
                builder.setMessage("Anda yakin ingin membatalkan mengubah profil?")

                builder.setPositiveButton(android.R.string.yes) { _, _ ->
                    finish()
                }

                builder.setNegativeButton(android.R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                builder.show()
            } else {
                finish()
            }
        }

        edit_image.setOnClickListener {
            actionPickImage()
        }

        bt_save.setOnClickListener {
            val userId = RequestBody.create(MediaType.parse("multipart/form-data"), user_id.toString())
            actionEdit(userId)
        }
    }

    private fun actionPickImage(){
        //check runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
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

    private fun actionEdit(user_id: RequestBody){
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.editImageProfil(
            user_id,
            imageData
        )
        call.enqueue(object : Callback<ResponseUsers> {
            override fun onFailure(call: Call<ResponseUsers>, t: Throwable) {
                Toast.makeText(
                    this@EditProfile,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(
                call: Call<ResponseUsers>,
                response: Response<ResponseUsers>
            ) {
                if (response.isSuccessful) {
                    if(response.body()!!.status == 200){
                        sharedPreference!!.setSharedPreferences(PROFIL, response.body()!!.data[0].profil)
                        finish()
                    }
                } else {
                    Toast.makeText(
                        this@EditProfile,
                        "Ada kesalahan server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
//            val mphoto = data.extras.get("") as Bitmap
            val imageUri = data!!.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            uploadImage(bitmap)
            image_profile.setImageURI(imageUri)

            Log.d("IMAGEURI", "$imageUri")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadImage(bitmap: Bitmap) {
        val file = createTempFile(bitmap)
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val body = MultipartBody.Part.createFormData("profil", file.name, reqFile)
        imageData = body
        imageEdit = true
        bt_save.visibility = View.VISIBLE
    }

    private fun createTempFile(bitmap: Bitmap): File {
        val file = File(
            getExternalFilesDir(DIRECTORY_PICTURES),
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

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSION_CODE = 1001
    }

}
