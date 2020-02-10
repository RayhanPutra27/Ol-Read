package rpl.ezy.olread.view.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_dashboard.*
import rpl.ezy.olread.R
import rpl.ezy.olread.utils.ConstantUtils
import rpl.ezy.olread.utils.SharedPreferenceUtils
import rpl.ezy.olread.view.auth.Authentification
import rpl.ezy.olread.view.auth.LogoutDialog

class DashboardActivity : AppCompatActivity() {

    private var sharedPreferences: SharedPreferenceUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        sharedPreferences = SharedPreferenceUtils(this@DashboardActivity)

        card_acc.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, AcceptActivity::class.java))
        }
        card_reject.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, RejectActivity::class.java))
        }
        card_req.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, RequestActivity::class.java))
        }
        card_manage.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, ManageUserActivity::class.java))
        }
        bt_logout.setOnClickListener {
            LogoutDialog(this).show()
        }
    }

}
