package rpl.ezy.olread.view.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_reject.*
import rpl.ezy.olread.R

class RejectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reject)

        bt_back.setOnClickListener {
            finish()
        }
    }
}
