package rpl.ezy.olread.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import rpl.ezy.olread.view.auth.LoginFragment
import rpl.ezy.olread.view.auth.SignupFragment

class PagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    private val title = arrayOf("LOGIN", "SIGNUP")

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> LoginFragment()
            1 -> SignupFragment()
            else -> LoginFragment()
        }
    }

    override fun getCount(): Int {
        return title.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }
}