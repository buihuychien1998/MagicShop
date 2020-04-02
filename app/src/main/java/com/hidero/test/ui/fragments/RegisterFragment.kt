package com.hidero.test.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hidero.test.R
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.ui.viewmodels.UserViewModel
import com.hidero.test.util.changeStatusBarColor
import com.hidero.test.util.isNotEmpty
import com.hidero.test.util.morphAndRevert
import com.hidero.test.util.showToast
import kotlinx.android.synthetic.main.fragment_register.*


/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel() as T
            }
        })[UserViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().changeStatusBarColor()
        initViews(view)
    }


    fun initViews(view: View) {
        btnRegister.run{
            setOnClickListener {
                if (isNotEmpty(tilUsername) && isNotEmpty(tilName) && isNotEmpty(tilEmail)
                    && isNotEmpty(tilAddress) && isNotEmpty(tilPhone) && isNotEmpty(tilPassword) ) {
                    val username = edtUsername.text.toString()
                    val password = edtPassword.text.toString()
                    val name = edtName.text.toString()
                    val address = edtAddress.text.toString()
                    val phone = edtPhone.text.toString()
                    val email = edtEmail.text.toString()
                    viewModel.result = viewModel.register(username, password, name, address, phone, email)
                    viewModel.result?.observe(viewLifecycleOwner, Observer {
                        if(it == "Success"){
                            requireContext().showToast("Đăng ký thành công! Giờ bạn có thể làm việc với chúng tôi bằng tài khoản này.")
                        }else{
                            requireContext().showToast("Tài khoản không hợp lệ hoặc đã có người sử dụng!")
                        }
                    })
                    viewModel.networkState.observe(viewLifecycleOwner, Observer {
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
            }
        }
    }

}
