package com.pmb.traveljournal.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.pmb.traveljournal.models.TravelNote
import com.pmb.traveljournal.utils.FirebaseHelper
import com.pmb.traveljournal.databinding.FragmentAddTravelNoteBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.fragment.findNavController
import androidx.activity.addCallback

class AddTravelNoteFragment : Fragment() {
    private var _binding: FragmentAddTravelNoteBinding? = null
    private val binding get() = _binding!!
    private val firebaseHelper = FirebaseHelper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTravelNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle back button click
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        // Setup close icon click listener
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.saveNoteButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()
            val location = binding.locationEditText.text.toString()

            if (title.isNotBlank() && description.isNotBlank() && location.isNotBlank()) {
                val newNote = TravelNote(
                    title = title,
                    description = description,
                    location = location,
                    date = System.currentTimeMillis().toString()  // Use current time as the date
                )

                val currentUser = FirebaseAuth.getInstance().currentUser
                val userId = currentUser?.uid

                if (userId != null) {
                    firebaseHelper.addTravelNote(userId, newNote)
                    Toast.makeText(requireContext(), "Note added successfully!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}