package com.hidero.test.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.hidero.test.R
import com.hidero.test.util.showToast
import kotlinx.android.synthetic.main.fragment_reset_password.*
import timber.log.Timber


class ResetPasswordFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        btnReset.setOnClickListener {
            val email: String = etSendEmail.text.toString()
            if (TextUtils.isEmpty(email)) {
                requireActivity().showToast("All fields are required!")
            } else {
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        requireActivity().showToast("Please check you Email")
                        findNavController().navigateUp()
                    } else {
                        Timber.e(task.exception)
                    }
                }
            }
        }
    }

}
