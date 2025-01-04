package com.pmb.traveljournal.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.pmb.traveljournal.R
import com.pmb.traveljournal.adapter.TravelNoteAdapter
import com.pmb.traveljournal.databinding.FragmentHomeBinding
import com.pmb.traveljournal.models.TravelNote
import com.pmb.traveljournal.utils.FirebaseHelper

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val firebaseHelper = FirebaseHelper()
    private val travelNotes = mutableListOf<TravelNote>()
    private lateinit var adapter: TravelNoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = TravelNoteAdapter(travelNotes) { note ->
            // Menggunakan NavController untuk navigasi ke TravelNoteDetailFragment
            val action = HomeFragmentDirections.actionNavHomeToNavDetail(note.id)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter

        // Mendapatkan userId dari Firebase Authentication
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            // Load travel notes dari Firebase berdasarkan userId
            firebaseHelper.getTravelNotes(userId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    travelNotes.clear()
                    snapshot.children.forEach { child ->
                        val note = child.getValue(TravelNote::class.java)
                        if (note != null) {
                            travelNotes.add(note)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        } else {
            // Jika pengguna tidak login
            // Arahkan pengguna ke halaman login
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
        }

        // Navigasi ke AddTravelNoteFragment
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_addTravelNoteFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}