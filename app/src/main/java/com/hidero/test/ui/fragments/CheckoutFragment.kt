package com.hidero.test.ui.fragments


import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hidero.test.R
import com.hidero.test.databinding.FragmentCheckoutBinding
import com.hidero.test.ui.adapters.CheckoutAdapter
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.CartViewModel
import com.hidero.test.ui.viewmodels.EventObserver
import com.hidero.test.util.SPLASH_TIME_OUT


/**
 * A simple [Fragment] subclass.
 */
class CheckoutFragment : BaseFragment<FragmentCheckoutBinding>() {
    private var currentStep = 0
    private val handler by lazy {
        Handler()
    }
    private lateinit var viewModel: CartViewModel
    private var checkoutAdapter: CheckoutAdapter? = null
    override fun getLayoutId() = R.layout.fragment_checkout
    private val runnable: Runnable = run {
        Runnable {
            binding.stepView.also {
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
        handler.postDelayed(runnable, SPLASH_TIME_OUT)
        viewModel = ViewModelProvider(this)[CartViewModel::class.java]
        binding.handlers = viewModel
        checkoutAdapter = CheckoutAdapter()
        binding.rvInfoOder.adapter = checkoutAdapter
        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.run {
            fetchCart(viewModel.account.value?.username)
            cart.observe(viewLifecycleOwner, Observer {
                checkoutAdapter?.submitList(it)
            })
            navigateTo.observe(viewLifecycleOwner, EventObserver{
                handleEvent(it)
            })
        }
    }

    private fun handleEvent(view: View) {
        when (view.id) {
            R.id.btnBack -> {
                findNavController().navigateUp()
            }
            R.id.btnCheckout->{
                findNavController().navigate(R.id.action_checkoutFragment_to_otpFragment)
            }


        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }
}
