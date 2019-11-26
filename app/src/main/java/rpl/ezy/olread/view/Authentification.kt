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
import android.view.View
import kotlinx.android.synthetic.main.activity_authentification.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.R
import rpl.ezy.olread.adapter.PagerAdapter
import rpl.ezy.olread.response.ResponseLogin
import rpl.ezy.olread.response.ResponseSignup
import rpl.ezy.olread.utils.ConstantUtils.ADMIN
import rpl.ezy.olread.utils.ConstantUtils.EMAIL
import rpl.ezy.olread.utils.ConstantUtils.STATUS
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
            if (sharedPreferences!!.getIntSharedPreferences(STATUS) == ADMIN){
                startActivity(Intent(this@Authentification, AdminActivity::class.java))
                return
            }
            if (sharedPreferences!!.getIntSharedPreferences(STATUS) == USER){
                startActivity(Intent(this@Authentification, MainActivity::class.java))
                return
            }
        }

        signup()
    }

    fun signup() {
        pager_auth.adapter = PagerAdapter(supportFragmentManager)
        tab_auth.setupWithViewPager(pager_auth)
        pager_auth.setPagingEnabled(false)
    }




}
