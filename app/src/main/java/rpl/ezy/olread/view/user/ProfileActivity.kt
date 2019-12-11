package rpl.ezy.olread.view.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import rpl.ezy.olread.R

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.green_1)
    }
}
