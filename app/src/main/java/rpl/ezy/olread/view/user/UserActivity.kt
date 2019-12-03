package rpl.ezy.olread.view.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.navigation_main_user.*
import kotlinx.android.synthetic.main.navigation_main_user.bt_logout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.R
import rpl.ezy.olread.adapter.AcceptedRecipesAdapter
import rpl.ezy.olread.adapter.CategoryAdapter
import rpl.ezy.olread.adapter.RecyclerAcceptedRecipesAdapter
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.model.MUser
import rpl.ezy.olread.response.ResponseCategory
import rpl.ezy.olread.response.ResponseRecipes
import rpl.ezy.olread.response.ResponseUsers
import rpl.ezy.olread.utils.ConstantUtils
import rpl.ezy.olread.utils.SharedPreferenceUtils
import rpl.ezy.olread.view.auth.Authentification

class UserActivity : AppCompatActivity() {

    private var mUser: MUser? = null
    private var sharedPreferences: SharedPreferenceUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        sharedPreferences = SharedPreferenceUtils(this@UserActivity)
        responseDataUser()

        setRecyclerMenu()
        setRecyclerCategory()

        bt_logout.setOnClickListener {
            actionLogout()
        }

        txt_search.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

    }

    fun actionLogout() {
        sharedPreferences!!.setSharedPreferences(ConstantUtils.USER_ID, -1)
        sharedPreferences!!.setSharedPreferences(ConstantUtils.USERNAME, "")
        sharedPreferences!!.setSharedPreferences(ConstantUtils.EMAIL, "")
        startActivity(Intent(this@UserActivity, Authentification::class.java))
        finish()
    }

    fun setRecyclerMenu(){
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

            override fun onResponse(call: Call<ResponseRecipes>, response: Response<ResponseRecipes>) {
                if (response.body()!!.status == 200){
                    var data = response.body()!!.data

                    var mAdapter = AcceptedRecipesAdapter(this@UserActivity, data)

                    recycler_menu.apply {
                        layoutManager = LinearLayoutManager(this@UserActivity)
                        adapter = mAdapter
                    }

                }
            }
        })
    }

    fun setRecyclerCategory() {
        val service = RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getCategory()
        call.enqueue(object : Callback<ResponseRecipes>{
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@UserActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseRecipes>,response: Response<ResponseRecipes>) {
                if(response.body()!!.status == 200) {
                    var data = response.body()!!.data
                    var mAdapter = CategoryAdapter(this@UserActivity, data)
                    recycler_category.apply {
                        layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                        adapter = mAdapter
                    }
                }
            }

        })

    }

    fun responseDataUser() {
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getAllUsers()
        call.enqueue(object : Callback<ResponseUsers> {
            override fun onFailure(call: Call<ResponseUsers>, t: Throwable) {
                Toast.makeText(
                    this@UserActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseUsers>, response: Response<ResponseUsers>) {
                if (response.body()!!.status == 200) {

                    val data = response.body()!!.data

                    for (i in 0 until data.size) {
                        if (data[i].user_id == sharedPreferences!!.getIntSharedPreferences(ConstantUtils.USER_ID)) {
                            mUser = data[i]
                            break
                        }
                    }

//                    tv_id.text = mUser!!.user_id.toString()
                    txt_user.text = mUser!!.username
                    Glide.with(this@UserActivity)
                        .load(mUser!!.profil)
                        .into(img_profile)
                    Glide.with(this@UserActivity)
                        .load(mUser!!.profil)
                        .into(nav_profile)
                    Toast.makeText(this@UserActivity, mUser!!.profil, Toast.LENGTH_LONG).show()
////                    tv_email.text = mUser!!.email
//                    tv_password.text = mUser!!.password
//                    if (mUser!!.status == ConstantUtils.ADMIN){
//                        tv_status.text = "KAMU ADMIN"
//                    }
//                    if (mUser!!.status == ConstantUtils.USER){
//                        tv_status.text = "KAMU AMPAS"
//                    }


                } else {
                    Toast.makeText(
                        this@UserActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })
    }

}
