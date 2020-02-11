package rpl.ezy.olread.view.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_edit_profile.*
import rpl.ezy.olread.GlideApp
import rpl.ezy.olread.R
import rpl.ezy.olread.utils.ConstantUtils.ADMIN
import rpl.ezy.olread.utils.ConstantUtils.PROFIL
import rpl.ezy.olread.utils.ConstantUtils.RECIPE
import rpl.ezy.olread.utils.ConstantUtils.STATUS
import rpl.ezy.olread.utils.SharedPreferenceUtils

class EditProfile : AppCompatActivity() {

    var profile: String? = null
    private var sharedPreference : SharedPreferenceUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        sharedPreference = SharedPreferenceUtils(this@EditProfile)

        if(intent != null) {
            if( intent.getStringExtra("type") == RECIPE){
                edit_image.visibility = View.GONE
            } else {
                edit_image.visibility = View.VISIBLE
            }

            profile = intent.getStringExtra(PROFIL)

            GlideApp.with(this@EditProfile)
                .load(profile)
                .into(image_profile)
        }

        if(sharedPreference!!.getIntSharedPreferences(STATUS) == ADMIN){
            edit_image.visibility = View.GONE
        } else {
            edit_image.visibility = View.VISIBLE
        }

        img_back.setOnClickListener {
            finish()
        }
    }
}
