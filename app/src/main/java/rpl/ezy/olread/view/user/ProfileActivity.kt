package rpl.ezy.olread.view.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.GlideApp
import rpl.ezy.olread.R
import rpl.ezy.olread.adapter.RecyclerAcceptedRecipesAdapter
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.response.ResponseRecipes
import rpl.ezy.olread.response.ResponseUsers
import rpl.ezy.olread.utils.ConstantUtils.ADMIN
import rpl.ezy.olread.utils.ConstantUtils.PROFIL
import rpl.ezy.olread.utils.ConstantUtils.STATUS
import rpl.ezy.olread.utils.ConstantUtils.USER_ID
import rpl.ezy.olread.utils.SharedPreferenceUtils

class ProfileActivity : AppCompatActivity() {

    private var sharedPreference: SharedPreferenceUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setToolbar()

        val window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.green_1)

        sharedPreference = SharedPreferenceUtils(this@ProfileActivity)

        if (intent != null) {
            getUser(intent.getIntExtra(USER_ID, 0))
            setRecipe(intent.getIntExtra(USER_ID, 0))
            setArchive(intent.getIntExtra(USER_ID, 0))
        }

        if(sharedPreference!!.getIntSharedPreferences(STATUS) == ADMIN){
            addRecipe.visibility = View.GONE
        } else {
            addRecipe.visibility = View.VISIBLE
        }

//        name.text = sharedPreference!!.getStringSharedPreferences(USERNAME)

        img_back.setOnClickListener {
            finish()
        }
        addRecipe.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, AddRecipesActivity::class.java))
        }

    }

    private fun getUser(user_id: Int) {
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getUser(user_id)
        call.enqueue(object : Callback<ResponseUsers> {
            override fun onFailure(call: Call<ResponseUsers>, t: Throwable) {
                Toast.makeText(
                    this@ProfileActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseUsers>, response: Response<ResponseUsers>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        val data = response.body()!!.data[0]
                        GlideApp.with(this@ProfileActivity)
                            .load(data.profil)
                            .into(img_profile)
                        name.text = data.username
                        total_like.text = data.like.toString()

                        img_profile.setOnClickListener {
                            startActivity(
                                Intent(this@ProfileActivity, EditProfile::class.java)
                                    .putExtra(PROFIL,data.profil)
                                    .putExtra("type", PROFIL)
                            )
                        }
                    }
                } else {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Ada kesalahan server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })
    }

    private fun setRecipe(user_id: Int) {
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getRecipeByUser(user_id)
        call.enqueue(object : Callback<ResponseRecipes> {
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@ProfileActivity,
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

                    total_post.text = "${response.body()!!.data.size}"

                    recycler_post.apply {
                        layoutManager = LinearLayoutManager(
                            this@ProfileActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        adapter = RecyclerAcceptedRecipesAdapter(
                            this@ProfileActivity,
                            response.body()!!.data
                        )
                    }
                }
            }

        })
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        toolbar.navigationIcon = resources.getDrawable(R.drawable.back_black)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setArchive(user_id: Int) {
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getArchivebyId(user_id)
        call.enqueue(object : Callback<ResponseRecipes> {
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@ProfileActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(
                call: Call<ResponseRecipes>,
                response: Response<ResponseRecipes>
            ) {
                total_archive.text = "${response.body()!!.data.size}"
            }
        })
    }
}
