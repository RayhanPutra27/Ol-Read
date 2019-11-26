package rpl.ezy.olread.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.R
import rpl.ezy.olread.adapter.RecyclerAcceptedRecipesAdapter
import rpl.ezy.olread.adapter.RecyclerUnAcceptedRecipesAdapter
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.model.MRecipe
import rpl.ezy.olread.response.ResponseRecipes
import rpl.ezy.olread.utils.ConstantUtils
import rpl.ezy.olread.utils.ConstantUtils.RECIPE_ID
import rpl.ezy.olread.utils.SharedPreferenceUtils


class AdminActivity : AppCompatActivity() {

    private var sharedPreferences: SharedPreferenceUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        sharedPreferences = SharedPreferenceUtils(this@AdminActivity)

        AcceptedRecipe()
        UnAcceptedRecipes()
        bt_logout.setOnClickListener {
            ActionLogout()
        }
    }

    fun AcceptedRecipe(){
        Toast.makeText(
            this@AdminActivity,
            "Resume",
            Toast.LENGTH_SHORT
        ).show()
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getAcceptedRecipe()
        call.enqueue(object : Callback<ResponseRecipes> {
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@AdminActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseRecipes>,response: Response<ResponseRecipes>) {
                if (response.body()!!.status == 200){
                    var data = response.body()!!.data

                    var mAdapter = RecyclerAcceptedRecipesAdapter(this@AdminActivity, data)

                    rv_acceptedRecipe.apply {
                        layoutManager = LinearLayoutManager(this@AdminActivity, LinearLayoutManager.HORIZONTAL, false)
                        adapter = mAdapter
                    }

                }
            }
        })
    }


    fun UnAcceptedRecipes(){
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getUnAcceptedRecipe()
        call.enqueue(object : Callback<ResponseRecipes> {
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@AdminActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseRecipes>,response: Response<ResponseRecipes>) {
                if (response.body()!!.status == 200){
                    var data = response.body()!!.data

                    var mAdapter = RecyclerUnAcceptedRecipesAdapter(this@AdminActivity, data)

                    rv_unAcceptedRecipes.apply {
                        layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                        adapter = mAdapter
                    }

                }
            }
        })
    }

    fun ActionLogout() {
        sharedPreferences!!.setSharedPreferences(ConstantUtils.USER_ID, -1)
        sharedPreferences!!.setSharedPreferences(ConstantUtils.USERNAME, "")
        sharedPreferences!!.setSharedPreferences(ConstantUtils.EMAIL, "")
        sharedPreferences!!.setSharedPreferences(ConstantUtils.STATUS, "")
        startActivity(Intent(this@AdminActivity, Authentification::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
        AcceptedRecipe()
        UnAcceptedRecipes()
    }

}
