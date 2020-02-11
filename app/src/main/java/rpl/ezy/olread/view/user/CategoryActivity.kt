package rpl.ezy.olread.view.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_category.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.R
import rpl.ezy.olread.adapter.AcceptedRecipesAdapter
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.response.ResponseRecipes
import rpl.ezy.olread.utils.ConstantUtils.KATEGORI

class CategoryActivity : AppCompatActivity() {

    var kategori: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.green_1)

        if(intent != null) {
            kategori = intent.getStringExtra(KATEGORI)
            nama_category_toolbar.setText(kategori)
        }

        setRecyclerMenu()
        setToolbar()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_left)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    fun setRecyclerMenu(){
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getSelectCategory(kategori!!)
        call.enqueue(object : Callback<ResponseRecipes> {
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@CategoryActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseRecipes>, response: Response<ResponseRecipes>) {
                if (response.body()!!.status == 200){
                    var data = response.body()!!.data

                    var mAdapter = AcceptedRecipesAdapter(this@CategoryActivity, data)

                    recycler_kategori.apply {
                        layoutManager = LinearLayoutManager(this@CategoryActivity)
                        adapter = mAdapter
                    }

                }
            }
        })
    }
}
