package rpl.ezy.olread.view.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.img_profile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.GlideApp
import rpl.ezy.olread.R
import rpl.ezy.olread.adapter.RecyclerAcceptedRecipesAdapter
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.response.ResponseRecipes
import rpl.ezy.olread.utils.ConstantUtils.PROFIL
import rpl.ezy.olread.utils.ConstantUtils.USERNAME
import rpl.ezy.olread.utils.ConstantUtils.USER_ID
import rpl.ezy.olread.utils.SharedPreferenceUtils

class ProfileActivity : AppCompatActivity() {

    private var sharedPreference : SharedPreferenceUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setToolbar()

        val window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.green_1)

        sharedPreference = SharedPreferenceUtils(this@ProfileActivity)

        GlideApp.with(this@ProfileActivity)
            .load(sharedPreference!!.getStringSharedPreferences(PROFIL))
            .into(img_profile)

        img_profile.setOnClickListener {
            startActivity(Intent(this, EditProfile::class.java).putExtra(PROFIL, sharedPreference!!.getStringSharedPreferences(PROFIL)))
        }

        name.text = sharedPreference!!.getStringSharedPreferences(USERNAME)

        setRecipe(sharedPreference!!.getIntSharedPreferences(USER_ID))
        setArchive(sharedPreference!!.getIntSharedPreferences(USER_ID))

        img_back.setOnClickListener {
            finish()
        }
        addRecipe.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, AddRecipesActivity::class.java))
        }

    }

    private fun setRecipe(user_id: Int){
        val service = RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getRecipeByUser(user_id)
        call.enqueue(object: Callback<ResponseRecipes>{
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    this@ProfileActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseRecipes>,response: Response<ResponseRecipes>) {
                if (response.body()!!.status == 200){

                    total_post.text = "${response.body()!!.data.size}"

                    recycler_post.apply {
                        layoutManager = LinearLayoutManager(this@ProfileActivity, LinearLayoutManager.HORIZONTAL, false)
                        adapter = RecyclerAcceptedRecipesAdapter(this@ProfileActivity, response.body()!!.data)
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

            override fun onResponse( call: Call<ResponseRecipes>, response: Response<ResponseRecipes>) {
                total_archive.text = "${response.body()!!.data.size}"
            }
        })
    }
}
