package rpl.ezy.olread.view.user

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_recipes.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.response.ResponseAddRecipe
import rpl.ezy.olread.utils.ConstantUtils
import rpl.ezy.olread.utils.SharedPreferenceUtils
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import rpl.ezy.olread.R
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import android.text.Editable
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody


class AddRecipesActivity : AppCompatActivity() {

    private var sharedPreferences: SharedPreferenceUtils? = null
    private lateinit var imageData: MultipartBody.Part
    var mDialog: SelectKategori? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipes)

        sharedPreferences = SharedPreferenceUtils(this@AddRecipesActivity)

        mDialog = SelectKategori(this@AddRecipesActivity)
        mDialog!!.interfaceKategori(object: SelectKategori.InterfaceKategori {

            override fun void(kategori: String) {
                txt_kategori.text = kategori
            }
        })

        pilih_kategori.setOnClickListener {
            mDialog!!.show()
        }

        initView()

    }

    private fun initView() {
        add_recipe.setOnClickListener {
            val title = RequestBody.create(MediaType.parse("multipart/form-data"), txt_title.getText().toString())
            val recipe = RequestBody.create(MediaType.parse("multipart/form-data"), txt_recipe.getText().toString())
            val kategori = RequestBody.create(MediaType.parse("multipart/form-data"), txt_kategori.getText().toString())
//            Toast.makeText(this, "$title $recipe $kategori", Toast.LENGTH_SHORT).show()
            if (validate()) {
                addRecipe(title, recipe, kategori)
            }
        }
        pick_image.setOnClickListener {
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
    }



    private fun addRecipe(title: RequestBody, recipe: RequestBody, kategori: RequestBody) {
        val user_id = RequestBody.create(MediaType.parse("multipart/form-data"), sharedPreferences!!.getIntSharedPreferences(ConstantUtils.USER_ID).toString())

        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.reqRecipe(
            user_id,
            title,
            recipe,
            imageData,
            kategori
        )
        call.enqueue(object : Callback<ResponseAddRecipe> {
            override fun onFailure(call: Call<ResponseAddRecipe>, t: Throwable) {
                Toast.makeText(
                    this@AddRecipesActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(
                call: Call<ResponseAddRecipe>,
                response: Response<ResponseAddRecipe>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@AddRecipesActivity,
                        response.body()!!.message, Toast.LENGTH_SHORT
                    ).show()
                    clearList()
                    finish()
                }
            }
        })
    }

    private fun validate(): Boolean {
        if (txt_title?.text?.isEmpty()!!) {
            txt_title?.error = "Title is Empty"
            txt_title?.requestFocus()
            return false
        }
        if (txt_recipe?.text?.isEmpty()!!) {
            txt_recipe?.error = "Recipe is Empty"
            txt_recipe?.requestFocus()
            return false
        }
        if (txt_kategori?.text?.isEmpty()!!) {
            txt_kategori?.error = "Category is Empty"
            txt_kategori?.requestFocus()
            return false
        }

        return true
    }

    private fun clearList() {
        txt_title.text = Editable.Factory.getInstance().newEditable("")
        txt_kategori.text = Editable.Factory.getInstance().newEditable("")
        txt_recipe.text = Editable.Factory.getInstance().newEditable("")
        image_add.setImageResource(0)
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
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
//            val mphoto = data.extras.get("") as Bitmap
            val imageUri = data!!.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            uploadImage(bitmap)
            image_add.setImageURI(imageUri)

            Log.d("IMAGEURI", "$imageUri")
            layout_pick_image.visibility = View.GONE
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadImage(bitmap: Bitmap) {
        val file = createTempFile(bitmap)
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val body = MultipartBody.Part.createFormData("img_url", file.name, reqFile)
        imageData = body

    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSION_CODE = 1001
    }
}
