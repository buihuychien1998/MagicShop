package com.hidero.test.ui.fragments


import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import com.hidero.test.R
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.databinding.FragmentLoginBinding
import com.hidero.test.ui.activities.MainActivity
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.CartViewModel
import com.hidero.test.ui.viewmodels.UserViewModel
import com.hidero.test.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mCallBackManager: CallbackManager
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private lateinit var googleSignInClient: GoogleSignInClient
    private var mFirebaseUser: FirebaseUser? = null
    val gso by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    private val viewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel() as T
            }
        })[UserViewModel::class.java]
    }

    private val cartViewModel by lazy {
        ViewModelProvider(this)[CartViewModel::class.java]
    }

    override fun getLayoutId() = R.layout.fragment_login

    override fun initViews(view: View) {
        initializeFirebaseAuth()
        initializeFacebook()
        signIn()
        binding.btnLogin.run {
            setOnClickListener {
                if (isNotEmpty(binding.tilUsername) && isNotEmpty(binding.tilPassword)) {
                    val username = binding.edtUsername.text.toString()
                    val password = binding.edtPassword.text.toString()
                    viewModel.run {
                        fetchUser(username, password)
                            .observeOnce(viewLifecycleOwner, Observer {
                                lifecycleScope.launch {
                                    withContext(Dispatchers.Default){
                                        login(mFirebaseAuth, it.email, password)
                                        SharedPrefs.instance.put(CURRENT_USER, it)
                                        refreshAccount()
                                    }
                                    (activity as MainActivity).enableStatistic()
                                }

                            })
                        networkState.observe(viewLifecycleOwner, Observer {
                            when (it) {
                                NetworkState.ERROR -> {
                                    morphAndRevert(requireContext())
                                    requireContext().showToast("Thông tin tài khoản hoặc mật khẩu không chính xác!")
                                }
                                NetworkState.LOADED -> {
                                    morphAndRevert(requireContext())
                                    requireContext().showToast("Đăng nhập thành công.")
                                    delayFunction({findNavController().navigateUp()}, 500)

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

    private fun initializeFirebaseAuth() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                val username = user.displayName
                Toast.makeText(requireActivity(), "Hello $username", Toast.LENGTH_LONG).show()
                requireActivity()
            }
        }
    }

    private fun signIn() {
//        binding.btnFacebookSI.setOnClickListener {
//            LoginManager.getInstance().logInWithReadPermissions(
//                this,
//                listOf("public_profile", "email")
//            )
//        }
//        binding.btnGoogleSI.setOnClickListener {
//            val signInIntent = googleSignInClient.signInIntent
//            startActivityForResult(signInIntent, RC_SIGN_IN)
//        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_authenticationFragment_to_resetPasswordFragment)
        }

    }

    private fun initializeFacebook() {
//        AppEventsLogger.activateApp(application)
        mCallBackManager = CallbackManager.Factory.create()
        LoginManager.getInstance()
            .registerCallback(mCallBackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    val credential = FacebookAuthProvider
                        .getCredential(loginResult.accessToken.token)
                    signInCredential(credential)
                }

                override fun onCancel() {}

                override fun onError(error: FacebookException) {
                    Timber.e(error)
                }
            })
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mFirebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    mFirebaseUser = mFirebaseAuth.currentUser
                    updateUI(mFirebaseUser)
                    findNavController().navigateUp()
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.e(task.exception)
                    updateUI(null)
                }
            }
    }

    private fun signInCredential(credential: AuthCredential) {
        mFirebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's informatio
                    mFirebaseUser = mFirebaseAuth.currentUser
                    updateUI(mFirebaseUser)
                    findNavController().navigateUp()
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.e(task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(mFirebaseUser: FirebaseUser?) {
        cartViewModel.fetchUser(mFirebaseUser?.email)
        lifecycleScope.launch {
            cartViewModel.account.observe(viewLifecycleOwner, Observer {
                Timber.e(it?.username.toString())
            })
        }

//
//
//
//        Timber.e(cartViewModel.fetchUser.invoke(mFirebaseUser?.email).asFlow().collect().toString())

//        if (acc == null) {
//            acc = Account("", "")
//            viewModel.register(mFirebaseAuth, acc)
//            Timber.e("Not found account!")
//        }

//        SharedPrefs.instance.put(CURRENT_USER, acc)
//        viewModel.refreshAccount()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Timber.e(e)
            }
        }
        mCallBackManager.onActivityResult(requestCode, resultCode, data)
    }
}
