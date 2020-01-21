package rpl.ezy.olread.view.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.GlideApp
import rpl.ezy.olread.R
import rpl.ezy.olread.adapter.AcceptedRecipesAdapter
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.response.ResponseRecipes
import rpl.ezy.olread.utils.ConstantUtils
import rpl.ezy.olread.utils.SharedPreferenceUtils

class SearchActivity : AppCompatActivity() {

    var sharedPref: SharedPreferenceUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        sharedPref = SharedPreferenceUtils(this@SearchActivity)

        setRecyclerNull()
        val window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.green_1)

        search.setOnClickListener {
            if(txt_search.text.toString() != "") {
                setRecyclerMenu(txt_search.text.toString())
            } else {
                setRecyclerNull()
            }
        }
        txt_search.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(s.toString() == ""){
                    setRecyclerNull()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        GlideApp.with(this@SearchActivity)
            .load(sharedPref!!.getStringSharedPreferences(ConstantUtils.PROFIL))
            .into(profile_search)

        img_back.setOnClickListener {
            finish()
        }

    }


    fun setRecyclerNull(){
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getAcceptedRecipe()
        call.enqueue(object : Callback<ResponseRecipes> {
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@SearchActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseRecipes>, response: Response<ResponseRecipes>) {
                if (response.body()!!.status == 200){
                    var data = response.body()!!.data

                    var mAdapter = AcceptedRecipesAdapter(this@SearchActivity, data)

                    recycler_menu.apply {
                        layoutManager = LinearLayoutManager(this@SearchActivity)
                        adapter = mAdapter
                    }

                }
            }
        })
    }

    fun setRecyclerMenu(title: String){
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getSearch(title)
        call.enqueue(object : Callback<ResponseRecipes> {
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@SearchActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseRecipes>, response: Response<ResponseRecipes>) {
                if (response.body()!!.status == 200){
                    var data = response.body()!!.data

                    var mAdapter = AcceptedRecipesAdapter(this@SearchActivity, data)

                    recycler_menu.apply {
                        layoutManager = LinearLayoutManager(this@SearchActivity)
                        adapter = mAdapter
                    }

                }
            }
        })
    }
}
