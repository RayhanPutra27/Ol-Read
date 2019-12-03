package rpl.ezy.olread.view.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_archive.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.R
import rpl.ezy.olread.adapter.AcceptedRecipesAdapter
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.model.MRecipe
import rpl.ezy.olread.response.ResponseArchive
import rpl.ezy.olread.response.ResponseRecipes
import rpl.ezy.olread.utils.ConstantUtils.USER_ID
import rpl.ezy.olread.utils.SharedPreferenceUtils

class ArchiveActivity : AppCompatActivity() {

    var sharedPref: SharedPreferenceUtils? = null
    private var dataRecipes: ArrayList<MRecipe>? = ArrayList()
    //    var dataRecipes: ArrayList<String>? = ArrayList()
    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)
//        var arra = arrayOf("TES", "TESLAH", "TESTES")
//
//        for (i in 0 until arra.size){
//            dataRecipes!!.add(i, arra[i])
//        }
//
//        var mAdapter = AcceptedRecipesAdapter(this@ArchiveActivity, dataRecipes!!)
//
//        recycler_archive.apply {
//            layoutManager = LinearLayoutManager(this@ArchiveActivity)
//            adapter = mAdapter
//        }

        sharedPref = SharedPreferenceUtils(this@ArchiveActivity)

        getArchive()
        setRecycler()
    }

    private fun getArchive() {
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getArchive(sharedPref!!.getIntSharedPreferences(USER_ID))
        call.enqueue(object : Callback<ResponseArchive> {
            override fun onFailure(call: Call<ResponseArchive>, t: Throwable) {
                Toast.makeText(
                    this@ArchiveActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(
                call: Call<ResponseArchive>,
                response: Response<ResponseArchive>
            ) {
                var data = response.body()!!.data

                for (i in 0 until data.size) {
                    setRecipe(data[i].recipe_id)
                }

            }
        })
    }

    private fun setRecipe(recipes_id: Int) {
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getAcceptedRecipe()
        call.enqueue(object : Callback<ResponseRecipes> {
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@ArchiveActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(
                call: Call<ResponseRecipes>,
                response: Response<ResponseRecipes>
            ) {
                if (response.body()!!.status == 200) {
                    val data = response.body()!!.data
                    for (i in 0 until data.size) {
                        if (data[i].recipe_id == recipes_id) {
                            dataRecipes!!.add(data[i])
                            break
                        }
                    }

                    val mAdapter = AcceptedRecipesAdapter(this@ArchiveActivity, dataRecipes!!)

                    recycler_archive.apply {
                        layoutManager = LinearLayoutManager(this@ArchiveActivity)
                        adapter = mAdapter
                    }

                }
            }
        })
    }

    private fun setRecycler() {
        Log.d("LOGLOGAN", "${dataRecipes!!}")
    }

}
