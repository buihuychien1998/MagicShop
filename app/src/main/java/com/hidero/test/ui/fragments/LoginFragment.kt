package com.hidero.test.ui.fragments


import android.app.Activity
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
import com.hidero.test.util.isNotEmpty
import com.hidero.test.util.morphAndRevert
import com.hidero.test.util.showToast
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    fun initViews(view: View) {
        btnLogin.run {
            setOnClickListener {
                if (isNotEmpty(tilUsername) && isNotEmpty(tilPassword)) {
                    val username = edtUsername.text.toString()
                    val password = edtPassword.text.toString()
                    viewModel.user = viewModel.fetchUser(username, password)
                    viewModel.user?.observe(viewLifecycleOwner, Observer {

                    })
                    viewModel.networkState.observe(viewLifecycleOwner, Observer {
                        when (it) {
                            NetworkState.ERROR -> {
                                morphAndRevert(requireContext())
                                requireContext().showToast("Thông tin tài khoản hoặc mật khẩu không chính xác!")
                            }
                            NetworkState.LOADED -> {
                                morphAndRevert(requireContext())
                                val returnIntent = requireActivity().intent
                                returnIntent.putExtra("result", "result")
                                requireActivity().setResult(Activity.RESULT_OK, returnIntent)
                                requireActivity().finish()
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
