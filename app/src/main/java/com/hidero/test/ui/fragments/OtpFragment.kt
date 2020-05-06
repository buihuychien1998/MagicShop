package com.hidero.test.ui.fragments

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentFilter
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.hidero.test.R
import com.hidero.test.databinding.FragmentOtpBinding
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.receivers.MySMSBroadcastReceiver
import com.hidero.test.ui.viewmodels.OtpViewModel
import com.hidero.test.util.REQ_USER_CONSENT
import com.hidero.test.util.showToast
import timber.log.Timber
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


class OtpFragment : BaseFragment<FragmentOtpBinding>(), MySMSBroadcastReceiver.OTPReceiveListener {


    private val receiver by lazy {
        MySMSBroadcastReceiver()
    }
    private var verificationCode: String? = null
    private var mResendToken: ForceResendingToken? = null

    private lateinit var viewModel: OtpViewModel
    override fun getLayoutId() = R.layout.fragment_otp
    private var mCallback = object : OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            val code = phoneAuthCredential.smsCode
            if (code != null) {
                verificationCode = code
                binding.progressBar.visibility = View.VISIBLE
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            binding.progressBar.visibility = View.GONE
        }

        override fun onCodeSent(
            verificationId: String,
            forceResendingToken: ForceResendingToken
        ) {
            super.onCodeSent(verificationId, forceResendingToken)
            mResendToken = forceResendingToken
        }

        override fun onCodeAutoRetrievalTimeOut(p0: String) {
            super.onCodeAutoRetrievalTimeOut(p0)
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun initViews(view: View) {
        requireActivity().registerReceiver(
            receiver,
            IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        )
        receiver.initOTPListener(this)
        viewModel = ViewModelProvider(this).get(OtpViewModel::class.java)
        binding.apply {
            val phoneNumber = "+84${viewModel.account.value?.phone}"
            if (!TextUtils.isEmpty(phoneNumber)) {
                numberText.text = phoneNumber
            }
            progressBar.visibility = View.GONE
            progressBar.max = 30
            progressBar.setProgressFormatter { progress, max -> "$progress / $max s" }
            fabBack.setOnClickListener {
                findNavController().navigateUp()
            }
            fabDone.setOnClickListener {
                val code = inputCode.text.toString()
                if (!TextUtils.isEmpty(code)) {
                    verificationCode?.let {
                        if (code == it) {
                            viewModel.updateBill()
                            viewModel.status.observe(viewLifecycleOwner, Observer {
                                if (it == "Success") {
                                    requireActivity().showToast("Đã đặt đơn hàng!")
                                    findNavController().navigate(R.id.action_otpFragment_to_homeFragment)
                                } else {
                                    requireActivity().showToast("Quá trình đặt đơn bị lỗi")
                                }
                            })
                        } else {
                            requireActivity().showToast(resources.getString(R.string.wrong_otp))
                        }
                    }
                } else {
                    requireActivity().showToast("Bạn chưa nhập mã otp!")
                }

            }
            btnSend.setOnClickListener {
                phoneNumber?.let { phoneNumber ->
                    if (mResendToken == null) {
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,                     // Phone number to verify
                            30,                           // Timeout duration
                            TimeUnit.SECONDS,                // Unit of timeout
                            requireActivity(),        // Activity (for callback binding)
                            mCallback
                        )
                    } else {
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,                     // Phone number to verify
                            30,                           // Timeout duration
                            TimeUnit.SECONDS,                // Unit of timeout
                            requireActivity(),        // Activity (for callback binding)
                            mCallback,
                            mResendToken
                        )
                    }

                }

                startSmsUserConsent()
                simulateProgress()
            }
        }

    }

    private fun startSmsUserConsent() {

        val client = SmsRetriever.getClient(requireActivity() /* context */)

        client.startSmsUserConsent(null).addOnSuccessListener {
            requireActivity().showToast("On Success")
        }.addOnFailureListener {
            Timber.e(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(receiver)
    }

    // Obtain the phone number from the result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_USER_CONSENT) {
            if (resultCode == RESULT_OK && data != null) {
                data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)?.apply {
                    requireActivity().showToast(this)
                    getOtpFromMessage(this)
                }

            }
        }
    }

    private fun getOtpFromMessage(message: String?) {
        // This will match any 6 digit number in the message
        val pattern = Pattern.compile("(|^)\\d{6}")
        val matcher = pattern.matcher(message.toString())
        if (matcher.find()) {
            binding.inputCode.setText(matcher.group(0))
        }
    }

    private fun simulateProgress() {
        val animator = AnimatorInflater.loadAnimator(
            requireContext(),
            R.animator.otp_animator
        ) as ValueAnimator
        animator.addUpdateListener { animation ->
            binding.progressBar.progress = animation.animatedValue as Int
            if (binding.progressBar.progress == 0) {
                verificationCode = null
                binding.progressBar.visibility = View.GONE
            }
        }
        animator.start()
    }

    override fun onOTPReceived(intent: Intent?) {
        startActivityForResult(intent, REQ_USER_CONSENT)
    }

    override fun onOTPTimeOut() {
        Timber.e("onOTPTimeOut")
    }


}
