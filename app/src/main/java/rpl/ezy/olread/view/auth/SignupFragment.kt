package rpl.ezy.olread.view.auth


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.R
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.response.ResponseSignup
import rpl.ezy.olread.utils.ConstantUtils.EMAIL
import rpl.ezy.olread.utils.ConstantUtils.PROFIL
import rpl.ezy.olread.utils.ConstantUtils.STATUS
import rpl.ezy.olread.utils.ConstantUtils.USER
import rpl.ezy.olread.utils.ConstantUtils.USERNAME
import rpl.ezy.olread.utils.ConstantUtils.USER_ID
import rpl.ezy.olread.utils.SharedPreferenceUtils
import rpl.ezy.olread.view.user.UserActivity

/**
 * A simple [Fragment] subclass.
 */
class SignupFragment : Fragment() {

    private var sharedPreferences : SharedPreferenceUtils?= null
    var btnSignup: Button? = null
    var etUsername: EditText? = null
    var etEmail: EditText? = null
    var etPassword: EditText? = null

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup, container, false)


        btnSignup = view.findViewById(R.id.bt_signup)
        etUsername = view.findViewById(R.id.et_username_reg)
        etEmail = view.findViewById(R.id.et_email_reg)
        etPassword = view.findViewById(R.id.et_pass_reg)

        signup()

        return view
    }

    fun signup() {

        sharedPreferences = SharedPreferenceUtils(context!!)
        btnSignup?.setOnClickListener {
            if (ValidationSignup()){
                ResponseSignup( etUsername?.text.toString(), etEmail?.text.toString(), etPassword?.text.toString() )
            }
        }
    }

    fun ValidationSignup(): Boolean {
        if (etUsername?.text?.isEmpty()!!){
            etUsername?.error = "Isi username"
            etUsername?.requestFocus()
            return false
        }
        if (etEmail?.text?.isEmpty()!!) {
            etEmail?.error = "Isi email"
            etEmail?.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail?.text).matches()) {
            etEmail?.error = "Masukan email yang benar"
            etEmail?.requestFocus()
            return false
        }
        if (etPassword?.text?.isEmpty()!!) {
            etPassword?.error = "Isi pass"
            etPassword?.requestFocus()
            return false
        }
        if (etPassword?.length()!! < 8) {
            etPassword?.error = "Password karakter minimal 8"
            etPassword?.requestFocus()
            return false
        }

        return true
    }

    private fun ResponseSignup(username: String, email: String, pass: String){
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.userSignup(username, email, pass, USER)
        call.enqueue(object : Callback<ResponseSignup> {
            override fun onFailure(call: Call<ResponseSignup>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN","${t.message}")
            }

            override fun onResponse(call: Call<ResponseSignup>,response: Response<ResponseSignup>) {
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
                    SetDataUser(PROFIL, 0, data.profil)
                    SetDataUser(STATUS, USER, "")

                    startActivity(Intent(context, UserActivity::class.java))
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

}
