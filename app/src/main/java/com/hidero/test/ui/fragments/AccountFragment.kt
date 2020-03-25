package com.hidero.test.ui.fragments


import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hidero.test.R
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.activities.AccountActivity
import kotlinx.android.synthetic.main.fragment_account.*

/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_account
    }

    override fun initView(view: View) {
        btnSetting.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_settingFragment)
        }
        ivProfile.setOnClickListener {

        }

        btnAuthentication.setOnClickListener {
            val intent = Intent(activity, AccountActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        }

    }
}
