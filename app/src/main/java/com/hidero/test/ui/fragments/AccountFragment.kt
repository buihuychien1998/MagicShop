package com.hidero.test.ui.fragments


import android.graphics.Color
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hidero.test.R
import com.hidero.test.databinding.FragmentAccountBinding
import com.hidero.test.ui.adapters.SliderAdapterExample
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.EventObserver
import com.hidero.test.ui.viewmodels.SettingViewModel
import com.hidero.test.util.renewItems
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView


/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : BaseFragment<FragmentAccountBinding>() {
    private lateinit var adapter: SliderAdapterExample
    override fun getLayoutId() = R.layout.fragment_account
    private lateinit var viewModel: SettingViewModel

    override fun initViews(view: View) {
        setUpSlider()
        viewModel = ViewModelProvider(this)[SettingViewModel::class.java]
        binding.handlers = viewModel
        viewModel.navigateTo.observe(viewLifecycleOwner, EventObserver {
            handleEvent(it)
        })
        viewModel.refreshAccount()
    }


    private fun handleEvent(view: View) {
        when (view.id) {
            R.id.btnSetting -> {
                findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToSettingFragment())
            }
            R.id.btnAuthentication -> {
//                startActivityForResult(
//                    Intent(requireActivity(), AuthenticationFragment::class.java),
//                    LAUNCH_AUTHENTICATION_ACTIVITY
//                )
                findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToAuthenticationFragment())
            }
        }

    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == LAUNCH_AUTHENTICATION_ACTIVITY && resultCode == Activity.RESULT_OK) {
////            val result = data?.getStringExtra("result")
//            requireContext().showToast("Đăng nhập thành công.")
////            Account.instance.username = result
//        } else {
//            Timber.e("cancel")
//        }
//    }

    private fun setUpSlider() {
        adapter =
            SliderAdapterExample(requireContext())
        renewItems(adapter)
        binding.sliderView.apply {
            sliderAdapter = adapter
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
