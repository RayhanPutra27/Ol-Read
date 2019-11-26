package rpl.ezy.olread.view.auth


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.MainActivity

import rpl.ezy.olread.R
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.response.ResponseLogin
import rpl.ezy.olread.utils.ConstantUtils.ADMIN
import rpl.ezy.olread.utils.ConstantUtils.EMAIL
import rpl.ezy.olread.utils.ConstantUtils.STATUS
import rpl.ezy.olread.utils.ConstantUtils.USER
import rpl.ezy.olread.utils.ConstantUtils.USERNAME
import rpl.ezy.olread.utils.ConstantUtils.USER_ID
import rpl.ezy.olread.utils.SharedPreferenceUtils
import rpl.ezy.olread.view.AdminActivity

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private var sharedPreferences : SharedPreferenceUtils?= null
    var btnLogin: Button? = null
    var etEmail: EditText? = null
    var etPassword: EditText? = null

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        // Inflate the layout for this fragment

        btnLogin = view.findViewById(R.id.bt_login)
        etEmail = view.findViewById(R.id.et_email)
        etPassword = view.findViewById(R.id.et_pass)

        login()

        return view
    }

    fun login() {
        sharedPreferences = SharedPreferenceUtils(context!!)

        btnLogin?.setOnClickListener {
            if (ValidationLogin()) {
                ResponseLogin( etEmail?.text.toString(), etPassword?.text.toString() )
            }
        }
    }

    fun ResponseLogin(email: String, pass: String) {
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.userLogin(email, pass)
        call.enqueue(object : Callback<ResponseLogin> {
            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN","${t.message}")
            }

            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if (response.body()!!.status == 200){
                    Toast.makeText(
                        context,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    var data = response.body()!!.data

                    SetDataUser(USER_ID, data.user_id, "")
                    SetDataUser(USERNAME, 0, data.username)
                    SetDataUser(EMAIL, 0, data.email)

                    if (data.status == ADMIN){
                        SetDataUser(STATUS, ADMIN, "")
                        startActivity(Intent(context, AdminActivity::class.java))
                        (context as Activity).finish()
                        return
                    }

                    SetDataUser(STATUS, USER, "")
                    startActivity(Intent(context, MainActivity::class.java))
                    (context as Activity).finish()
                } else {
                    Toast.makeText(
                        context,
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

    fun ValidationLogin(): Boolean {
        if (etEmail?.text?.isEmpty()!!) {
            etEmail?.error = "Enter Email"
            etEmail?.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail?.text).matches()) {
            etEmail?.error = "Enter correct Email!"
            etEmail?.requestFocus()
            return false
        }
        if (etPassword?.text?.isEmpty()!!) {
            etPassword?.error = "Enter Password"
            etPassword?.requestFocus()
            return false
        }
        if (etPassword?.length()!! < 8) {
            etPassword?.error = "At least 8 characters!"
            etPassword?.requestFocus()
            return false
        }

        return true
    }


}
