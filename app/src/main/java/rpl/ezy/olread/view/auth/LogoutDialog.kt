package rpl.ezy.olread.view.auth

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.dialog_logout.*
import rpl.ezy.olread.R
import rpl.ezy.olread.utils.ConstantUtils
import rpl.ezy.olread.utils.SharedPreferenceUtils

class LogoutDialog (context: Context) : Dialog(context){

    private var sharedPreferences: SharedPreferenceUtils? = null

    init {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_logout)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams()
        val window = window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = lp

        sharedPreferences = SharedPreferenceUtils(context)

        bt_no.setOnClickListener {
            dismiss()
        }
        bt_yes.setOnClickListener {

            sharedPreferences!!.setSharedPreferences(ConstantUtils.USER_ID, -1)
            sharedPreferences!!.setSharedPreferences(ConstantUtils.USERNAME, "")
            sharedPreferences!!.setSharedPreferences(ConstantUtils.EMAIL, "")
            sharedPreferences!!.setSharedPreferences(ConstantUtils.STATUS, "")

            (context as Activity).startActivity(Intent(context, Authentification::class.java))
            context.finish()
        }
    }

}