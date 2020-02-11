package rpl.ezy.olread.view.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.GlideApp
import rpl.ezy.olread.R
import rpl.ezy.olread.adapter.AcceptedRecipesAdapter
import rpl.ezy.olread.adapter.HistoryAdapter
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.response.ResponseRecipes
import rpl.ezy.olread.utils.ConstantUtils
import rpl.ezy.olread.utils.ConstantUtils.USERNAME
import rpl.ezy.olread.utils.ConstantUtils.USER_ID
import rpl.ezy.olread.utils.SharedPreferenceUtils

class HistoryActivity : AppCompatActivity() {

    var sharedPref: SharedPreferenceUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.green_1)
        sharedPref = SharedPreferenceUtils(this@HistoryActivity)

        GlideApp.with(this@HistoryActivity)
            .load(sharedPref!!.getStringSharedPreferences(ConstantUtils.PROFIL))
            .into(profile_history)

        txt_user.text = sharedPref!!.getStringSharedPreferences(USERNAME)
        setToolbar()
        getHistory()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        toolbar.navigationIcon = resources.getDrawable(R.drawable.back_black)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun getHistory() {
        val service = RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        service.allHistory(sharedPref!!.getIntSharedPreferences(USER_ID)).enqueue(object:
            Callback<ResponseRecipes>{
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@HistoryActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(
                call: Call<ResponseRecipes>,
                response: Response<ResponseRecipes>
            ) {
                if(response.isSuccessful) {
                    val data = response.body()!!.data

                    val mAdapter = HistoryAdapter(this@HistoryActivity, data)

                    recycler_history.apply {
                        layoutManager = LinearLayoutManager(this@HistoryActivity)
                        adapter = mAdapter
                    }
                }
            }
        })
    }
}
