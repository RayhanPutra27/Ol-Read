package rpl.ezy.olread.view.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_history.*
import rpl.ezy.olread.GlideApp
import rpl.ezy.olread.R
import rpl.ezy.olread.utils.ConstantUtils
import rpl.ezy.olread.utils.ConstantUtils.USERNAME
import rpl.ezy.olread.utils.SharedPreferenceUtils

class HistoryActivity : AppCompatActivity() {

    var sharedPref: SharedPreferenceUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.green_1)
        sharedPref = SharedPreferenceUtils(this@HistoryActivity)

        img_back.setOnClickListener {
            finish()
        }

        GlideApp.with(this@HistoryActivity)
            .load(sharedPref!!.getStringSharedPreferences(ConstantUtils.PROFIL))
            .into(profile_history)

        txt_user.text = sharedPref!!.getStringSharedPreferences(USERNAME)
    }
}
