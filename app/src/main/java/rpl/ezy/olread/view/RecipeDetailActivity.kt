package rpl.ezy.olread.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.R
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.model.MRecipe
import rpl.ezy.olread.response.ResponseRecipes
import rpl.ezy.olread.response.ResponseRecipeById
import rpl.ezy.olread.utils.ConstantUtils
import rpl.ezy.olread.utils.ConstantUtils.ACCEPTED
import rpl.ezy.olread.utils.ConstantUtils.PROFIL
import rpl.ezy.olread.utils.ConstantUtils.RECIPE
import rpl.ezy.olread.utils.ConstantUtils.RECIPE_ID
import rpl.ezy.olread.utils.ConstantUtils.REJECTED
import rpl.ezy.olread.utils.ConstantUtils.USER_ID
import rpl.ezy.olread.utils.SharedPreferenceUtils
import rpl.ezy.olread.view.user.EditProfile

class RecipeDetailActivity : AppCompatActivity() {

    var sharedPref: SharedPreferenceUtils? = null
    var archived = false
    var liked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        sharedPref = SharedPreferenceUtils(this@RecipeDetailActivity)
        if (intent != null) {
            setData(intent.getIntExtra(RECIPE_ID, 0))

            img_archive.setOnClickListener {
                if (archived){
                    delArchive(sharedPref!!.getIntSharedPreferences(USER_ID), intent.getIntExtra(RECIPE_ID, 0))
                    return@setOnClickListener
                }
                if (!archived){
                    archivingRecipe(sharedPref!!.getIntSharedPreferences(USER_ID), intent.getIntExtra(RECIPE_ID, 0))
                    return@setOnClickListener
                }
            }

            img_favorite.setOnClickListener {
                if (liked){
                    delLike(sharedPref!!.getIntSharedPreferences(USER_ID), intent.getIntExtra(RECIPE_ID, 0))
                    return@setOnClickListener
                }
                if (!liked){
                    likeRecipe(sharedPref!!.getIntSharedPreferences(USER_ID), intent.getIntExtra(RECIPE_ID, 0))
                    return@setOnClickListener
                }
            }
        }
        val window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.green_1)

    }

    private fun archivingRecipe(user_id: Int, recipe_id: Int){
        val service = RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.archivingRecipe(user_id, recipe_id)
        call.enqueue(object: Callback<ResponseRecipes>{
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@RecipeDetailActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseRecipes>, response: Response<ResponseRecipes>) {
                img_archive.setImageResource(R.drawable.bookmark_black)
                archived = true
            }

        })
    }

    private fun delArchive(user_id: Int, recipe_id: Int){
        val service = RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.delArchive(user_id, recipe_id)
        call.enqueue(object: Callback<ResponseRecipes>{
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@RecipeDetailActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseRecipes>, response: Response<ResponseRecipes>) {
                img_archive.setImageResource(R.drawable.bookmark_white)
                archived = false
            }

        })
    }

    private fun likeRecipe(user_id: Int, recipe_id: Int){
        val service = RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.likeRecipe(user_id, recipe_id)
        call.enqueue(object: Callback<ResponseRecipes>{
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@RecipeDetailActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseRecipes>, response: Response<ResponseRecipes>) {
                img_favorite.setImageResource(R.drawable.love_fill)
                liked = true
            }

        })
    }

    private fun delLike(user_id: Int, recipe_id: Int){
        val service = RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.delLike(user_id, recipe_id)
        call.enqueue(object: Callback<ResponseRecipes>{
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@RecipeDetailActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseRecipes>, response: Response<ResponseRecipes>) {
                img_favorite.setImageResource(R.drawable.love)
                liked = false
            }

        })
    }

    private fun setData(recipe_id: Int) {
        val service = RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getRecipeById(recipe_id)
        call.enqueue(object : Callback<ResponseRecipeById> {
            override fun onFailure(call: Call<ResponseRecipeById>, t: Throwable) {
                Toast.makeText(
                    this@RecipeDetailActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseRecipeById>, response: Response<ResponseRecipeById>) {
                val dataRecipe = response.body()!!.data

                Glide.with(this@RecipeDetailActivity)
                    .load(dataRecipe.img_url)
                    .into(img_item)

                tv_title.text = dataRecipe.title
                tv_recipe.text = dataRecipe.recipe

                if (dataRecipe.isAccept == ACCEPTED){
                    view_button.visibility = View.GONE
                } else if (dataRecipe.isAccept == REJECTED){
                    view_button.visibility = View.VISIBLE
                    bt_reject.visibility = View.GONE
                    img_archive.visibility = View.GONE
                    img_favorite.visibility = View.GONE
                } else {
                    view_button.visibility = View.VISIBLE
                    img_archive.visibility = View.GONE
                    img_favorite.visibility = View.GONE
                    tv_recipe.setPadding(0,0,0,70)
                }

                bt_confirm.setOnClickListener {
                    confirmRecipe(recipe_id)
                }

                bt_reject.setOnClickListener {
                    rejectRecipe(recipe_id)
                }

                img_item.setOnClickListener {
                    startActivity(Intent(this@RecipeDetailActivity, EditProfile::class.java)
                        .putExtra(PROFIL, dataRecipe.img_url)
                        .putExtra("type", RECIPE))
                }

                getArchive(dataRecipe)
                getLike(dataRecipe)

            }
        })
    }

    private fun confirmRecipe(recipe_id: Int){
        val service = RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.confirmRecipes(recipe_id)
        call.enqueue(object : Callback<ResponseRecipes> {
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@RecipeDetailActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseRecipes>,response: Response<ResponseRecipes>) {
                if(response.isSuccessful){
                    if(response.body()!!.status == 200){
                        Toast.makeText(
                            this@RecipeDetailActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@RecipeDetailActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@RecipeDetailActivity,
                        "Ada kesalahan server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })
    }

    private fun rejectRecipe(recipe_id: Int){
        val service = RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.rejectRecipes(recipe_id)
        call.enqueue(object : Callback<ResponseRecipes> {
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@RecipeDetailActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseRecipes>,response: Response<ResponseRecipes>) {
                if(response.isSuccessful){
                    if(response.body()!!.status == 200){
                        Toast.makeText(
                            this@RecipeDetailActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@RecipeDetailActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@RecipeDetailActivity,
                        "Ada kesalahan server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })
    }

    private fun getArchive(dataRecipe: MRecipe) {
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getArchivebyId(sharedPref!!.getIntSharedPreferences(USER_ID))
        call.enqueue(object : Callback<ResponseRecipes> {
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@RecipeDetailActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(
                call: Call<ResponseRecipes>,
                response: Response<ResponseRecipes>
            ) {
                val data = response.body()!!.data
                for (i in 0 until data.size) {
                    if (data[i].recipe_id == dataRecipe.recipe_id){
                        img_archive.setImageResource(R.drawable.bookmark_black)
                        archived = true
                    }
                }
            }
        })
    }

    private fun getLike(dataRecipe: MRecipe) {
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getLikebyId(sharedPref!!.getIntSharedPreferences(USER_ID))
        call.enqueue(object : Callback<ResponseRecipes> {
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@RecipeDetailActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(
                call: Call<ResponseRecipes>,
                response: Response<ResponseRecipes>
            ) {
                val data = response.body()!!.data
                for (i in 0 until data.size) {
                    if (data[i].recipe_id == dataRecipe.recipe_id){
                        img_favorite.setImageResource(R.drawable.love_fill)
                        liked = true
                    }
                }
            }
        })
    }

}
