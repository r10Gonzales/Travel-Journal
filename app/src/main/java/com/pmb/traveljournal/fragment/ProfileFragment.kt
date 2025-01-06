package com.pmb.traveljournal.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.pmb.traveljournal.activities.LoginActivity
import com.pmb.traveljournal.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser

        user?.let {
            binding.profileName.setText(it.displayName)
            binding.profileEmail.setText(it.email)

            it.photoUrl?.let { uri ->
                Picasso.get().load(uri).into(binding.profileImage)
            }
        }

        // Edit profile button
        binding.editProfileButton.setOnClickListener {
            enableEditing(true)
        }

        // Save changes
        binding.saveProfileButton.setOnClickListener {
            val newName = binding.profileName.text.toString().trim()
            val newEmail = binding.profileEmail.text.toString().trim()

            if (newName.isNotEmpty() && newEmail.isNotEmpty()) {
                updateProfile(user, newName, newEmail)
            }
        }

        // Delete profile button
        binding.deleteProfileButton.setOnClickListener {
            deleteProfile(user)
        }
    }

    private fun enableEditing(enable: Boolean) {
        binding.profileName.isEnabled = enable
        binding.profileEmail.isEnabled = enable
        binding.saveProfileButton.visibility = if (enable) View.VISIBLE else View.GONE
    }

    private fun updateProfile(user: FirebaseUser?, name: String, email: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                user.updateEmail(email).addOnCompleteListener { emailTask ->
                    if (emailTask.isSuccessful) {
                        enableEditing(false)
                        Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to update email: ${emailTask.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Failed to update profile: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteProfile(user: FirebaseUser?) {
        user?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Profile deleted", Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Toast.makeText(context, "Failed to delete profile: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
