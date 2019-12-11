package rpl.ezy.olread

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.model.MUser
import rpl.ezy.olread.response.ResponseUsers
import rpl.ezy.olread.utils.ConstantUtils.ADMIN
import rpl.ezy.olread.utils.ConstantUtils.EMAIL
import rpl.ezy.olread.utils.ConstantUtils.USER
import rpl.ezy.olread.utils.ConstantUtils.USERNAME
import rpl.ezy.olread.utils.ConstantUtils.USER_ID
import rpl.ezy.olread.utils.SharedPreferenceUtils
import rpl.ezy.olread.view.auth.Authentification

class MainActivity : AppCompatActivity() {

    private var mUser: MUser? = null
    private var sharedPreferences: SharedPreferenceUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = SharedPreferenceUtils(this@MainActivity)
        ResponseDataUser()

        bt_logout.setOnClickListener {
            ActionLogout()
        }

    }

    fun ActionLogout() {
        sharedPreferences!!.setSharedPreferences(USER_ID, -1)
        sharedPreferences!!.setSharedPreferences(USERNAME, "")
        sharedPreferences!!.setSharedPreferences(EMAIL, "")
        startActivity(Intent(this@MainActivity, Authentification::class.java))
        finish()
    }

    fun ResponseDataUser() {
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getAllUsers()
        call.enqueue(object : Callback<ResponseUsers> {
            override fun onFailure(call: Call<ResponseUsers>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseUsers>, response: Response<ResponseUsers>) {
                if (response.body()!!.status == 200) {
//                    Toast.makeText(
//                        this@MainActivity,
//                        response.body()!!.message,
//                        Toast.LENGTH_SHORT
//                    ).show()

                    var data = response.body()!!.data

                    for (i in 0 until data.size) {
                        if (data[i].user_id == sharedPreferences!!.getIntSharedPreferences(USER_ID)) {
                            mUser = data[i]
                            break
                        }
                    }

                    tv_id.text = mUser!!.user_id.toString()
                    tv_username.text = mUser!!.username
                    tv_email.text = mUser!!.email
                    tv_password.text = mUser!!.password
                    if (mUser!!.status == ADMIN){
                        tv_status.text = "KAMU ADMIN"
                    }
                    if (mUser!!.status == USER){
                        tv_status.text = "KAMU AMPAS"
                    }


                } else {
                    Toast.makeText(
                        this@MainActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })
    }
}
