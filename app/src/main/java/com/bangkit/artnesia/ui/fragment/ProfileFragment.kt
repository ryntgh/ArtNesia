package com.bangkit.artnesia.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.model.User
import com.bangkit.artnesia.databinding.FragmentProfileBinding
import com.bangkit.artnesia.ui.activity.AddressListActivity
import com.bangkit.artnesia.ui.activity.LoginActivity
import com.bangkit.artnesia.ui.activity.MyProductActivity
import com.bangkit.artnesia.ui.activity.UserProfileActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException

class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding as FragmentProfileBinding
    private val mFireStore = FirebaseFirestore.getInstance()
    private lateinit var mUserDetails: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater , container , false)

        getUserDetails()

        binding.cvMyproduct.setOnClickListener {
            activity?.let {
                it.startActivity(Intent(it, MyProductActivity::class.java))
            }
        }

        binding.cvAddress.setOnClickListener {
            activity?.let {
                it.startActivity(Intent(it, AddressListActivity::class.java))
            }
        }

        binding.cvEditProfile.setOnClickListener {
            activity?.let {
                val intent = Intent(it, UserProfileActivity::class.java)
                intent.putExtra(UserProfileActivity.EXTRA_USER_DETAILS, mUserDetails)
                it.startActivity(intent)
            }
        }

        binding.btnLogout.setOnClickListener {
            activity?.let{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent (it, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                it.startActivity(intent)
            }
        }

        return binding.root
    }

    private fun getUserDetails() {
        getUserDetails(this)
    }

    private fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    private fun userDetailsSuccess(user: User) {
        mUserDetails = user

        // Load the image using the Glide Loader class.
        loadUserPicture(user.image, binding.profileImageProfileFrag)

        binding.profileNameProfileFrag.text = user.name
        binding.profileEmailProfileFrag.text = user.email
    }

    private fun getUserDetails(fragment: ProfileFragment) {
        // Here we pass the collection name from which we wants the data.
        mFireStore.collection("users")
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(fragment.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!
                userDetailsSuccess(user)
            }
            .addOnFailureListener { e ->
                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }

    private fun loadUserPicture(image: Any, imageView: ImageView) {
        try {
            Glide
                .with(this)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.ic_profile)
                .into(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}