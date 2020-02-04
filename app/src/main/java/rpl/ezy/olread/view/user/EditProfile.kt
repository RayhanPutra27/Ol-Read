package rpl.ezy.olread.view.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_edit_profile.*
import rpl.ezy.olread.GlideApp
import rpl.ezy.olread.R
import rpl.ezy.olread.utils.ConstantUtils.PROFIL
import rpl.ezy.olread.utils.SharedPreferenceUtils

class EditProfile : AppCompatActivity() {

    var profile: String? = null
    private var sharedPreference : SharedPreferenceUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        sharedPreference = SharedPreferenceUtils(this@EditProfile)

        if(intent != null) {
            profile = intent.getStringExtra(PROFIL)

            GlideApp.with(this@EditProfile)
                .load(profile)
                .into(image_profile)
        }

        img_back.setOnClickListener {
            finish()
        }
    }
}
