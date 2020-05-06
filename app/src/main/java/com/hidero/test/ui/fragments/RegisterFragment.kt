package com.hidero.test.ui.fragments


import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.hidero.test.R
import com.hidero.test.data.valueobject.Account
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.databinding.FragmentRegisterBinding
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.UserViewModel
import com.hidero.test.util.*

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    private lateinit var mFirebaseAuth: FirebaseAuth
    private val viewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel() as T
            }
        })[UserViewModel::class.java]
    }

    override fun getLayoutId() = R.layout.fragment_register

    override fun initViews(view: View) {
        requireActivity().changeStatusBarColor()
        mFirebaseAuth = FirebaseAuth.getInstance()
        binding.apply {
            btnRegister.apply {
                setOnClickListener {
                    if (isNotEmpty(tilUsername) && isNotEmpty(tilName) && isNotEmpty(tilEmail)
                        && isNotEmpty(tilAddress) && isNotEmpty(tilPhone) && isNotEmpty(tilPassword)
                    ) {
                        val username = edtUsername.text.toString()
                        val password = edtPassword.text.toString()
                        val name = edtName.text.toString()
                        val address = edtAddress.text.toString()
                        val phone = edtPhone.text.toString()
                        val email = edtEmail.text.toString()
                        val account = Account(
                            username,
                            password,
                            USER_ROLE,
                            name,
                            address,
                            phone,
                            email,
                            null
                        )
                        if (account.isValidData() == -1) {
                            viewModel.apply {
                                result = viewModel.register(mFirebaseAuth, account)
                                result?.observeOnce(viewLifecycleOwner, Observer {
                                    if (it == "Success") {
                                        requireContext().showToast("Đăng ký thành công! Giờ bạn có thể làm việc với chúng tôi bằng tài khoản này.")
                                        clearEditText()
                                        login(mFirebaseAuth, email, password)
                                        SharedPrefs.instance.put(CURRENT_USER, account)
                                        refreshAccount()
                                        findNavController().navigateUp()
                                    } else {
                                        requireContext().showToast("Tài khoản không hợp lệ hoặc đã có người sử dụng!")
                                    }
                                })
                                networkState.observe(viewLifecycleOwner, Observer {
                                    when (it) {
                                        NetworkState.ERROR -> {
                                            morphAndRevert(requireContext())
                                        }
                                        NetworkState.LOADED -> {
                                            morphAndRevert(requireContext())
                                        }
                                        NetworkState.LOADING -> {
                                            startAnimation()
                                        }
                                    }
                                })
                            }
                        } else {
                            requireContext().showToast("Tài khoản không hợp lệ hoặc đã có người sử dụng!")
                        }

                    }
                }
            }
        }
    }

    fun clearEditText() {
        binding.apply {
            edtUsername.setText("")
            edtEmail.setText("")
            edtName.setText("")
            edtPassword.setText("")
            edtPhone.setText("")
            edtAddress.setText("")
        }
    }

}
