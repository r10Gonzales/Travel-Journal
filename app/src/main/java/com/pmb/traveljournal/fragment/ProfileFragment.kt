package com.pmb.traveljournal.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
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

        // Mendapatkan pengguna saat ini dari FirebaseAuth
        val user = FirebaseAuth.getInstance().currentUser

        user?.let {
            // Menampilkan informasi pengguna di elemen UI
            binding.profileName.setText(it.displayName)
            binding.profileEmail.setText(it.email)

            // Menampilkan gambar profil menggunakan Picasso (jika ada)
            it.photoUrl?.let { uri ->
                Picasso.get().load(uri).into(binding.profileImage)
            }
        }

        // Mengatur tombol logout
        binding.logoutButton.setOnClickListener {
            // Melakukan logout dari Firebase
            FirebaseAuth.getInstance().signOut()

            // Mengarahkan pengguna kembali ke halaman login
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}