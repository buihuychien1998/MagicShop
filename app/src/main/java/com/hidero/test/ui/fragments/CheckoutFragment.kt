package com.hidero.test.ui.fragments


import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hidero.test.R
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.util.SPLASH_TIME_OUT
import kotlinx.android.synthetic.main.fragment_checkout.*


/**
 * A simple [Fragment] subclass.
 */
class CheckoutFragment : BaseFragment() {
    private var currentStep = 0
    private val handler by lazy {
        Handler()
    }

    override fun getLayoutId() = R.layout.fragment_checkout


    private val runnable: Runnable = run {
        Runnable {
            stepView.also {
                if (currentStep <= it.stepCount - 1) {
                    currentStep++
                    it.go(currentStep, true)
                    if (it.currentStep == it.stepCount - 1) {
                        it.done(true)
                    }
                } else {
                    currentStep = 0
                    it.done(false)
                    it.go(currentStep, true)
                }
            }
            handler.postDelayed(runnable, SPLASH_TIME_OUT)
        }

    }


    override fun initViews(view: View) {
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        handler.postDelayed(runnable, SPLASH_TIME_OUT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }
}
