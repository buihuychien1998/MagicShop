package com.hidero.test.ui.fragments


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.hidero.test.R
import com.hidero.test.data.valueobject.Account
import com.hidero.test.data.valueobject.User
import com.hidero.test.databinding.FragmentIndividualBinding
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.EventObserver
import com.hidero.test.ui.viewmodels.SettingViewModel
import com.hidero.test.util.*
import timber.log.Timber
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class IndividualFragment : BaseFragment<FragmentIndividualBinding>() {
    override fun getLayoutId() = R.layout.fragment_individual
    private lateinit var viewModel: SettingViewModel
    private var imageUri: Uri? = null
    private var uploadTask: StorageTask<*>? = null
    private lateinit var storageReference: StorageReference
    private lateinit var reference: DatabaseReference
    private var fuser: FirebaseUser? = null

    override fun initViews(view: View) {
        viewModel = ViewModelProvider(this)[SettingViewModel::class.java]
        binding.handlers = viewModel
        viewModel.navigateTo.observe(viewLifecycleOwner, EventObserver {
            handleEvent(it)
        })
        viewModel.refreshAccount()
        storageReference = FirebaseStorage.getInstance().getReference(UPLOADS)


    }


    private fun handleEvent(view: View) {
        when (view.id) {
            R.id.btnSetting -> {
                findNavController().navigate(IndividualFragmentDirections.actionIndividualFragmentToSettingFragment())
            }
            R.id.btnAuthentication -> {
                findNavController().navigate(IndividualFragmentDirections.actionIndividualFragmentToAuthenticationFragment())
            }
            R.id.ivProfile -> {
                openImage()
            }
            R.id.lnlFavourite -> {
                findNavController().navigate(R.id.action_individualFragment_to_favouriteFragment)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (SharedPrefs.instance[CURRENT_USER, Account::class.java] != null) {
            fuser = FirebaseAuth.getInstance().currentUser?.apply {
                reference = FirebaseDatabase.getInstance().getReference(USERS).child(uid)
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                        val user = dataSnapshot.getValue(User::class.java)
                        if (user != null && context.isAvailable()) {
                            viewModel.refreshUser(user)
                            if (user.photoUrl.equals(DEFAULT)) {
                                binding.ivProfile.setImageResource(R.drawable.ic_user_profile)
                            } else { //and this
                                Glide.with(requireContext()).load(user.photoUrl)
                                    .into(binding.ivProfile)
                            }
                            viewModel.account.value?.photoUrl = user.photoUrl
                        }

                    }

                    override fun onCancelled(@NonNull databaseError: DatabaseError) {}
                })
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null
        ) {
            imageUri = data.data
            if (uploadTask != null && uploadTask!!.isInProgress) {
                Toast.makeText(context, "Upload in progress", Toast.LENGTH_SHORT).show()
            } else {
                uploadImage()
            }

        }
    }

    private fun uploadImage() {
        val adb = requireActivity().showDialog("Loading...")
        if (imageUri != null) {

            imageUri?.let {
                val fileReference = storageReference.child(
                    System.currentTimeMillis()
                        .toString() + "." + requireActivity().getFileExtension(it)
                )
                uploadTask = fileReference.putFile(it).apply {
                    continueWithTask { task ->
                        if (!task.isSuccessful) {
                            Timber.e(task.exception)
                        }
                        fileReference.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                            val mUri = downloadUri.toString()
                            reference = FirebaseDatabase.getInstance().getReference(USERS)
                                .child(fuser!!.uid)
                            val map =
                                HashMap<String, Any>()
                            map[PHOTOURL] = "" + mUri
                            reference.updateChildren(map)
                            adb.dismiss()
                        } else {
                            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
                            adb.dismiss()
                        }
                    }.addOnFailureListener { ex ->
                        Timber.e(ex)
                        adb.dismiss()
                    }
                }
            }

        } else {
            Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

}
