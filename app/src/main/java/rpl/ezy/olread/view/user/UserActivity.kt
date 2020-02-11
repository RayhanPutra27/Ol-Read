package rpl.ezy.olread.view.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.navigation_main_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.GlideApp
import rpl.ezy.olread.R
import rpl.ezy.olread.adapter.AcceptedRecipesAdapter
import rpl.ezy.olread.adapter.CategoryAdapter
import rpl.ezy.olread.adapter.RecyclerAcceptedRecipesAdapter
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.response.ResponseCategory
import rpl.ezy.olread.response.ResponseRecipes
import rpl.ezy.olread.utils.ConstantUtils
import rpl.ezy.olread.utils.ConstantUtils.USER_ID
import rpl.ezy.olread.utils.SharedPreferenceUtils
import rpl.ezy.olread.view.auth.Authentification


class UserActivity : AppCompatActivity() {

    private var sharedPreferences: SharedPreferenceUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.nav)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        sharedPreferences = SharedPreferenceUtils(this@UserActivity)
        if (sharedPreferences!!.getStringSharedPreferences(ConstantUtils.PROFIL) != "") {
            GlideApp.with(this@UserActivity)
                .load(sharedPreferences!!.getStringSharedPreferences(ConstantUtils.PROFIL))
                .into(img_profile)
            GlideApp.with(this@UserActivity)
                .load(sharedPreferences!!.getStringSharedPreferences(ConstantUtils.PROFIL))
                .into(nav_profile)
        }
        txt_user.text = sharedPreferences!!.getStringSharedPreferences(ConstantUtils.USERNAME)

        setRecyclerTrends()
        setRecyclerCategory()
        setRecyclerMenu()

        nav_profile.setOnClickListener {
            startActivity(Intent(this@UserActivity, ProfileActivity::class.java).putExtra(USER_ID, sharedPreferences!!.getIntSharedPreferences(USER_ID)))
        }

        bt_logout.setOnClickListener {
            actionLogout()
        }
        if (btn_history.isClickable) {
            btn_history.setBackgroundResource(R.drawable.custom_history)
            txt_history.setTextColor(resources.getColor(R.color.grey_1))
        } else {
            btn_history.setBackgroundResource(R.drawable.custom_shape)
            txt_history.setTextColor(resources.getColor(R.color.white))
        }
        btn_history.setOnClickListener {
            startActivity(Intent(this@UserActivity, HistoryActivity::class.java))
        }

        btn_favorite.setOnClickListener {
            startActivity(Intent(this@UserActivity, FavoriteActivity::class.java))
        }

        txt_search.setOnClickListener {
            startActivity(Intent(this@UserActivity, SearchActivity::class.java))
        }

        btn_archive.setOnClickListener {
            startActivity(Intent(this@UserActivity, ArchiveActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val itemId = item!!.itemId
        when (itemId) {
            // Android home
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
                return true
            }
        }// manage other entries if you have it ...
        return true
    }

    private fun actionLogout() {
        sharedPreferences!!.setSharedPreferences(ConstantUtils.USER_ID, -1)
        sharedPreferences!!.setSharedPreferences(ConstantUtils.USERNAME, "")
        sharedPreferences!!.setSharedPreferences(ConstantUtils.EMAIL, "")
        startActivity(Intent(this@UserActivity, Authentification::class.java))
        finish()
    }

    private fun setRecyclerMenu() {
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getAcceptedRecipe()
        call.enqueue(object : Callback<ResponseRecipes> {
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@UserActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(
                call: Call<ResponseRecipes>,
                response: Response<ResponseRecipes>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        var data = response.body()!!.data

                        var mAdapter = AcceptedRecipesAdapter(this@UserActivity, data)

                        recycler_menu.apply {
                            layoutManager = LinearLayoutManager(this@UserActivity)
                            adapter = mAdapter
                        }

                    } else {
                        Toast.makeText(
                            this@UserActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@UserActivity,
                        "Ada kesalahan server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun setRecyclerCategory() {
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getCategory()
        call.enqueue(object : Callback<ResponseCategory> {
            override fun onFailure(call: Call<ResponseCategory>, t: Throwable) {
                Toast.makeText(
                    this@UserActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(
                call: Call<ResponseCategory>,
                response: Response<ResponseCategory>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        val data = response.body()!!.data
                        val mAdapter = CategoryAdapter(this@UserActivity, data)
                        recycler_category.apply {
                            layoutManager = LinearLayoutManager(
                                applicationContext,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            adapter = mAdapter
                        }
                    } else {
                        Toast.makeText(
                            this@UserActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@UserActivity,
                        "Ada kesalahan server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })

    }

    private fun setRecyclerTrends() {
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getTrendsRecipe()
        call.enqueue(object : Callback<ResponseRecipes> {
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@UserActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(
                call: Call<ResponseRecipes>,
                response: Response<ResponseRecipes>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        var data = response.body()!!.data

                        val mAdapter = RecyclerAcceptedRecipesAdapter(this@UserActivity, data)

                        recycler_trend.apply {
                            layoutManager = LinearLayoutManager(
                                this@UserActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            adapter = mAdapter
                        }

                    } else {
                        Toast.makeText(
                            this@UserActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@UserActivity,
                        "Ada kesalahan server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        setRecyclerTrends()
        setRecyclerCategory()
        setRecyclerMenu()
    }
}
