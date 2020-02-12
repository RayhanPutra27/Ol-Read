package rpl.ezy.olread.view.user

import android.app.ProgressDialog
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
import rpl.ezy.olread.model.MRecipe
import rpl.ezy.olread.response.ResponseRecipes
import rpl.ezy.olread.utils.ConstantUtils
import rpl.ezy.olread.utils.ConstantUtils.USERNAME
import rpl.ezy.olread.utils.ConstantUtils.USER_ID
import rpl.ezy.olread.utils.SharedPreferenceUtils

class HistoryActivity : AppCompatActivity() {

    var sharedPref: SharedPreferenceUtils? = null
    var loading: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.green_1)
        sharedPref = SharedPreferenceUtils(this@HistoryActivity)

        loading = ProgressDialog(this@HistoryActivity)
        loading!!.setCancelable(false)

        GlideApp.with(this@HistoryActivity)
            .load(sharedPref!!.getStringSharedPreferences(ConstantUtils.PROFIL))
            .into(profile_history)

        txt_user.text = sharedPref!!.getStringSharedPreferences(USERNAME)
        setToolbar()
        getHistory()
        deleteAll.setOnClickListener {
            deleteAllHistory()
        }
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_left)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun deleteAllHistory() {
        loading!!.setMessage("Loading . . .")
        loading!!.show()
        val service = RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        service.delHistory(sharedPref!!.getIntSharedPreferences(USER_ID)).enqueue(object: Callback<ResponseRecipes>{
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                loading!!.cancel()
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
                    loading!!.cancel()
                    Toast.makeText(this@HistoryActivity, "Berhasil Hapus Semua!", Toast.LENGTH_SHORT).show()
                    getHistory()
                }
            }
        })
    }

    private fun getHistory() {
        loading!!.setMessage("Loading . . .")
        loading!!.show()
        val service = RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        service.allHistory(sharedPref!!.getIntSharedPreferences(USER_ID)).enqueue(object:
            Callback<ResponseRecipes>{
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                loading!!.cancel()
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
                    loading!!.cancel()
                    val data = response.body()!!.data

                    val mAdapter = HistoryAdapter(this@HistoryActivity, data)

                    mAdapter.interfaceClick(object: HistoryAdapter.indeleteOne{
                        override fun delete(isSuccess: Boolean) {
                            if(isSuccess) {
                                getHistory()
                            }
                        }
                    })

                    recycler_history.apply {
                        layoutManager = LinearLayoutManager(this@HistoryActivity)
                        adapter = mAdapter
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getHistory()
    }
}
