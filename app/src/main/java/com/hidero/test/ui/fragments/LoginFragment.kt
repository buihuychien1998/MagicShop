package com.hidero.test.ui.fragments


import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.hidero.test.R
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.databinding.FragmentLoginBinding
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.UserViewModel
import com.hidero.test.util.*


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel() as T
            }
        })[UserViewModel::class.java]
    }

    override fun getLayoutId() = R.layout.fragment_login

    override fun initViews(view: View) {
        binding.btnLogin.run {
            setOnClickListener {
                if (isNotEmpty(binding.tilUsername) && isNotEmpty(binding.tilPassword)) {
                    val username = binding.edtUsername.text.toString()
                    val password = binding.edtPassword.text.toString()
                    viewModel.run {
                        user = viewModel.fetchUser(username, password)
                        user?.observe(viewLifecycleOwner, Observer {
                            SharedPrefs.instance.put(CURRENT_USER, it)
                            refreshAccount()
                        })
                        networkState.observe(viewLifecycleOwner, Observer {
                            when (it) {
                                NetworkState.ERROR -> {
                                    morphAndRevert(requireContext())
                                    requireContext().showToast("Thông tin tài khoản hoặc mật khẩu không chính xác!")
                                }
                                NetworkState.LOADED -> {
                                    morphAndRevert(requireContext())
//                                    val returnIntent = requireActivity().intent
//                                    returnIntent.putExtra("result", user?.value?.username)
//                                    requireActivity().setResult(Activity.RESULT_OK, returnIntent)
//                                    requireActivity().finish()
                                    requireContext().showToast("Đăng nhập thành công.")
                                    findNavController().navigateUp()
                                }
                                NetworkState.LOADING -> {
                                    startAnimation()
                                }
                            }
                        })
                    }
                }
            }
        }
    }
}
