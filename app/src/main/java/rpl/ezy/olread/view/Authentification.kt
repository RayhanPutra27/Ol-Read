package rpl.ezy.olread.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import rpl.ezy.olread.MainActivity
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import android.util.Log
import android.util.Patterns
import kotlinx.android.synthetic.main.activity_authentification.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.R
import rpl.ezy.olread.response.ResponseLogin
import rpl.ezy.olread.response.ResponseSignup
import rpl.ezy.olread.utils.ConstantUtils.EMAIL
import rpl.ezy.olread.utils.ConstantUtils.USER
import rpl.ezy.olread.utils.ConstantUtils.USERNAME
import rpl.ezy.olread.utils.ConstantUtils.USER_ID
import rpl.ezy.olread.utils.SharedPreferenceUtils


class Authentification : AppCompatActivity() {

    private var sharedPreferences : SharedPreferenceUtils ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentification)

        sharedPreferences = SharedPreferenceUtils(this@Authentification)

        if (sharedPreferences!!.getIntSharedPreferences(USER_ID) != -1){
            startActivity(Intent(this@Authentification, MainActivity::class.java))
            return
        }

        bt_login.setOnClickListener {
            if (ValidationLogin()) {
                ResponseLogin( etl_email.text.toString(), etl_pass.text.toString() )
            }
        }

        bt_signup.setOnClickListener {
            if (ValidationSignup()){
                ResponseSignup( ets_username.text.toString(), ets_email.text.toString(), ets_pass.text.toString() )
            }
        }
    }

    fun ValidationLogin(): Boolean {
        if (etl_email.text.isEmpty()) {
            etl_email.error = "Isi email"
            etl_email.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(etl_email.text).matches()) {
            etl_email.error = "Masukan email yang benar"
            etl_email.requestFocus()
            return false
        }
        if (etl_pass.text.isEmpty()) {
            etl_pass.error = "Isi pass"
            etl_pass.requestFocus()
            return false
        }
        if (etl_pass.length() < 8) {
            etl_pass.error = "Password karakter minimal 8"
            etl_pass.requestFocus()
            return false
        }

        return true
    }

    fun ValidationSignup(): Boolean {
        if (ets_username.text.isEmpty()){
            ets_username.error = "Isi username"
            ets_username.requestFocus()
            return false
        }
        if (ets_email.text.isEmpty()) {
            ets_email.error = "Isi email"
            ets_email.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(ets_email.text).matches()) {
            ets_email.error = "Masukan email yang benar"
            ets_email.requestFocus()
            return false
        }
        if (ets_pass.text.isEmpty()) {
            ets_pass.error = "Isi pass"
            ets_pass.requestFocus()
            return false
        }
        if (ets_pass.length() < 8) {
            ets_pass.error = "Password karakter minimal 8"
            ets_pass.requestFocus()
            return false
        }

        return true
    }

    fun ResponseLogin(email: String, pass: String) {
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.userLogin(email, pass)
        call.enqueue(object : Callback<ResponseLogin> {
            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Toast.makeText(
                    this@Authentification,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN","${t.message}")
            }

            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if (response.body()!!.status == 200){
                    Toast.makeText(
                        this@Authentification,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    var data = response.body()!!.data

                    SetDataUser(USER_ID, data.user_id, "")
                    SetDataUser(USERNAME, 0, data.username)
                    SetDataUser(EMAIL, 0, data.email)

                    startActivity(Intent(this@Authentification, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this@Authentification,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })
    }

    private fun ResponseSignup(username: String, email: String, pass: String){
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.userSignup(username, email, pass, USER)
        call.enqueue(object : Callback<ResponseSignup> {
            override fun onFailure(call: Call<ResponseSignup>, t: Throwable) {
                Toast.makeText(
                    this@Authentification,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN","${t.message}")
            }

            override fun onResponse(call: Call<ResponseSignup>,response: Response<ResponseSignup>) {
                if (response.body()!!.status == 200){
                    Toast.makeText(
                        this@Authentification,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    var data = response.body()!!.data

                    SetDataUser(USER_ID, data.user_id, "")
                    SetDataUser(USERNAME, 0, data.username)
                    SetDataUser(EMAIL, 0, data.email)

                    startActivity(Intent(this@Authentification, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this@Authentification,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })
    }

    fun SetDataUser(key: String, int: Int, string: String){
        if (string != ""){
            sharedPreferences!!.setSharedPreferences(key, string)
        } else {
            sharedPreferences!!.setSharedPreferences(key, int)
        }
    }

}
