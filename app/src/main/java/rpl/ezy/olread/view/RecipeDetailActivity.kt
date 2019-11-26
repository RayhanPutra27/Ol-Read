package rpl.ezy.olread.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.R
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.response.ResponseRecipeById
import rpl.ezy.olread.response.ResponseRecipes
import rpl.ezy.olread.utils.ConstantUtils
import rpl.ezy.olread.utils.ConstantUtils.RECIPE_ID

class RecipeDetailActivity : AppCompatActivity() {

//    var dataRecipe: MRecipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        if (intent != null) {
            setData(intent.getIntExtra(RECIPE_ID, 0))
            Toast.makeText(
                this@RecipeDetailActivity,
                "${intent.getIntExtra(RECIPE_ID,0)}",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    fun setData(recipe_id: Int) {
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
                var dataRecipe = response.body()!!.data

                Glide.with(this@RecipeDetailActivity)
                    .load(dataRecipe!!.img_url)
                    .into(img_item)

                tv_title.text = dataRecipe!!.title
                tv_recipe.text = dataRecipe!!.recipe

                if (dataRecipe!!.isAccept == ConstantUtils.ACCEPTED){
                    bt_confirm.visibility = View.GONE
                } else {
                    bt_confirm.visibility = View.VISIBLE
                }

                bt_confirm.setOnClickListener {
                    ConfirmRecipe(recipe_id)
                }
            }
        })
    }

    fun ConfirmRecipe(recipe_id: Int){
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
                Toast.makeText(
                    this@RecipeDetailActivity,
                    "${response.body()!!.message}",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }

        })
    }

}
