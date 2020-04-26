package com.hidero.test.ui.viewmodels

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.hidero.test.data.api.APIUtil
import com.hidero.test.data.repository.remote.UserRepository
import com.hidero.test.data.valueobject.Account
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.ui.base.BaseViewModel
import com.hidero.test.util.*
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.*

class UserViewModel : BaseViewModel() {
    private val apiService by lazy {
        APIUtil.getRxData(baseUrl)
    }

    private val compositeDisposable = CompositeDisposable()

    private val repository by lazy {
        UserRepository(
            apiService,
            compositeDisposable
        )
    }

    var result: LiveData<String>? = null

    var networkState: LiveData<NetworkState> = repository.networkState


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    val fetchUser = { username: String, password: String ->
        repository.fetchUser(username, password)
    }


    val register = { auth: FirebaseAuth, account: Account ->
        register(auth, account.username, account.email, account.password)
        repository.register(account)
    }

    private fun register(
        auth: FirebaseAuth,
        username: String?,
        email: String?,
        password: String?
    ) {
        auth.createUserWithEmailAndPassword(email.toString(), password.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.uid?.let {
                        val reference =
                            FirebaseDatabase.getInstance().getReference(USERS).child(it)
                        val hashMap = HashMap<String, String?>()
                        hashMap[ID] = it
                        hashMap[USERNAME] = username
                        hashMap[PHOTOURL] = DEFAULT
                        hashMap[STATUS] = OFFLINE
                        hashMap[SEARCH] = username?.toLowerCase(Locale.getDefault())
                        reference.setValue(hashMap).addOnCompleteListener { t ->
                            if (t.isSuccessful) {
                                Timber.e("Register OK")
                            }
                        }
                    }

                } else {
                    Timber.e(" Register Fail")
                }
            }
    }

    fun login(auth: FirebaseAuth, email: String?, password: String?) {
        auth.signInWithEmailAndPassword(email.toString(), password.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.e("Login Ok")
                } else {
                    Timber.e(task.exception)
                }
            }
    }

    fun resetPassword(auth: FirebaseAuth, email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.e("Check your email")
            } else {
                Timber.e(task.exception)
            }
        }
    }
}