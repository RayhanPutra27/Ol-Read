package rpl.ezy.olread.view.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_accept.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.R
import rpl.ezy.olread.adapter.AcceptedRecipesAdapter
import rpl.ezy.olread.adapter.RecyclerAcceptedRecipesAdapter
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.response.ResponseRecipes
import rpl.ezy.olread.utils.SharedPreferenceUtils

class AcceptActivity : AppCompatActivity() {

    private var sharedPreferences: SharedPreferenceUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accept)

        sharedPreferences = SharedPreferenceUtils(this@AcceptActivity)

        AcceptedRecipe()

        bt_back.setOnClickListener {
            finish()
        }

        val window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.green_1)
    }

    fun AcceptedRecipe(){
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getAcceptedRecipe()
        call.enqueue(object : Callback<ResponseRecipes> {
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@AcceptActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseRecipes>,response: Response<ResponseRecipes>) {
                if (response.body()!!.status == 200){
                    val data = response.body()!!.data

                    val mAdapter = AcceptedRecipesAdapter(this@AcceptActivity, data)

                    rv_acceptedRecipe.apply {
                        layoutManager = LinearLayoutManager(this@AcceptActivity, LinearLayoutManager.VERTICAL, false)
                        adapter = mAdapter
                    }

                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        AcceptedRecipe()
    }

}
