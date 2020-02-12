package rpl.ezy.olread.view.user

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_profile.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.GlideApp
import rpl.ezy.olread.R
import rpl.ezy.olread.adapter.MyRecipeAdapter
import rpl.ezy.olread.adapter.RecyclerAcceptedRecipesAdapter
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.response.ResponseRecipes
import rpl.ezy.olread.response.ResponseSignup
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
        edit()

    }

    private fun edit() {
        val username = RequestBody.create(MediaType.parse("multipart/form-data"), username!!.text.toString())
        val email = RequestBody.create(MediaType.parse("multipart/form-data"), email!!.text.toString())
        val user_id = RequestBody.create(MediaType.parse("multipart/form-data"), sharedPreference!!.getIntSharedPreferences(USER_ID).toString())
        bt_edit.setOnClickListener {
            bt_done.visibility = View.VISIBLE
            edit.visibility = View.VISIBLE
            bt_edit.visibility = View.GONE
            bt_close.visibility = View.VISIBLE
        }
        bt_close.setOnClickListener {
            bt_done.visibility = View.GONE
            edit.visibility = View.GONE
            bt_edit.visibility = View.VISIBLE
        }
        bt_done.setOnClickListener {
            editUser(username, email, user_id)
        }
    }
    private fun editUser(username: RequestBody, email: RequestBody, user_id: RequestBody) {
        val service = RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        service.editUser(username, email, user_id).enqueue(object: Callback<ResponseSignup>{
            override fun onFailure(call: Call<ResponseSignup>, t: Throwable) {
                Toast.makeText(
                    this@ProfileActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(
                call: Call<ResponseSignup>,
                response: Response<ResponseSignup>
            ) {
                if(response.isSuccessful) {
                    Toast.makeText(this@ProfileActivity, "Berhasil Update!", Toast.LENGTH_SHORT).show()
                }
            }
        })
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
                        username.text = Editable.Factory.getInstance().newEditable(data.username)
                        email.text = Editable.Factory.getInstance().newEditable(data.email)
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

                    val mAdapter = MyRecipeAdapter(
                        this@ProfileActivity,
                        response.body()!!.data
                    )

                    mAdapter.interfaceRefresh(object: MyRecipeAdapter.InterfaceRefresh{
                        override fun void(status: Boolean) {
                            if(status){
                                setRecipe(intent.getIntExtra(USER_ID, 0))
                            }
                        }

                    })

                    recycler_post.apply {
                        layoutManager = LinearLayoutManager(
                            this@ProfileActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        adapter = mAdapter
                    }
                }
            }

        })
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_left)
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

    override fun onResume() {
        super.onResume()
        getUser(intent.getIntExtra(USER_ID, 0))
        setRecipe(intent.getIntExtra(USER_ID, 0))
        setArchive(intent.getIntExtra(USER_ID, 0))
    }
}
