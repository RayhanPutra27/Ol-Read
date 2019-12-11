package rpl.ezy.olread.view.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_profile.*
import rpl.ezy.olread.R
import rpl.ezy.olread.utils.ConstantUtils
import rpl.ezy.olread.utils.SharedPreferenceUtils

class ProfileActivity : AppCompatActivity() {

    private var sharedPreference : SharedPreferenceUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.green_1)

        sharedPreference = SharedPreferenceUtils(this@ProfileActivity)

//        Glide.with(this@ProfileActivity)
//            .load(sharedPreference!!.getStringSharedPreferences(ConstantUtils.PROFIL))
//            .into(img_profile)

        name.text = sharedPreference!!.getStringSharedPreferences(ConstantUtils.USERNAME)

    }
}
