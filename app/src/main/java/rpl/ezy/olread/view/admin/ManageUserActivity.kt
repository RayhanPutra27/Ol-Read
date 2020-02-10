package rpl.ezy.olread.view.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_manage_user.*
import rpl.ezy.olread.R

class ManageUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_user)

        bt_back.setOnClickListener {
            finish()
        }
    }
}
