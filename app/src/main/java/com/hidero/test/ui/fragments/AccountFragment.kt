package com.hidero.test.ui.fragments


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hidero.test.R
import com.hidero.test.ui.activities.AuthenticationActivity
import com.hidero.test.ui.adapters.SliderAdapterExample
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.util.LAUNCH_AUTHENTICATION_ACTIVITY
import com.hidero.test.util.renewItems
import com.hidero.test.util.showToast
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_account.*


/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : BaseFragment() {
    private lateinit var adapter: SliderAdapterExample
    override fun getLayoutId(): Int {
        return R.layout.fragment_account
    }

    override fun initViews(view: View) {
        setUpSlider()
        btnSetting.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_settingFragment)
        }
        ivProfile.setOnClickListener {

        }

        btnAuthentication.setOnClickListener {
            startActivityForResult(
                Intent(requireActivity(), AuthenticationActivity::class.java),
                LAUNCH_AUTHENTICATION_ACTIVITY
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LAUNCH_AUTHENTICATION_ACTIVITY && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringExtra("result")
            requireContext().showToast("Đăng nhập thành công.")
            Log.e("Acc", result.toString())
        } else {
            Log.e("Acc", "cancel")
        }
    }

    private fun setUpSlider() {
        adapter =
            SliderAdapterExample(requireContext())
        sliderView.sliderAdapter = adapter
        renewItems(adapter)
        sliderView.apply {
            setIndicatorAnimation(IndicatorAnimations.FILL) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION)
            autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
            indicatorSelectedColor = Color.WHITE
            indicatorUnselectedColor = Color.GRAY
            scrollTimeInSec = 3
            isAutoCycle = true
            startAutoCycle()
        }
    }
}
