package com.pmb.traveljournal.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.pmb.traveljournal.databinding.FragmentTravelNoteDetailBinding
import com.pmb.traveljournal.models.TravelNote
import com.pmb.traveljournal.utils.FirebaseHelper
import androidx.navigation.fragment.findNavController
import androidx.activity.addCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.pmb.traveljournal.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TravelNoteDetailFragment : Fragment() {

    // Instance objek binding yang sesuai dengan fragment
    private var _binding: FragmentTravelNoteDetailBinding? = null
    private val binding get() = _binding!!

    // Ambil argumen navigasi yang dilewatkan ke fragment ini
    private val args: TravelNoteDetailFragmentArgs by navArgs()

    // Instance FirebaseHelper untuk berinteraksi dengan Firebase
    private val firebaseHelper = FirebaseHelper()

    private lateinit var noteId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tangani klik tombol kembali
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        // Atur listener klik ikon close untuk navigasi kembali ke fragment home
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_travelNoteDetailFragment_to_homeFragment)
        }

        // Ambil ID catatan dari argumen
        noteId = args.noteId

        // Muat detail catatan jika ID catatan valid
        if (noteId.isNotEmpty()) {
            loadNoteDetails()
        } else {
            Toast.makeText(requireContext(), "Invalid note ID", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        // Atur listener klik untuk tombol update
        binding.updateButton.setOnClickListener {
            updateNote()
        }

        // Atur listener klik untuk tombol delete
        binding.deleteButton.setOnClickListener {
            confirmDeleteNote()
        }
    }

    // Fungsi untuk memuat detail catatan dari Firebase
    private fun loadNoteDetails() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            firebaseHelper.getTravelNotes(userId).child(noteId).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val note = snapshot.getValue(TravelNote::class.java)
                    if (note != null) {
                        // Isi UI dengan detail catatan
                        binding.titleEditText.setText(note.title)
                        binding.descriptionEditText.setText(note.description)
                        binding.locationEditText.setText(note.location)
                        // Asumsikan note.date sudah diformat sebagai "dd-MM-yyyy"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error loading note", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    // Fungsi untuk memperbarui detail catatan di Firebase
    private fun updateNote() {
        val updatedTitle = binding.titleEditText.text.toString()
        val updatedDescription = binding.descriptionEditText.text.toString()
        val updatedLocation = binding.locationEditText.text.toString()

        if (updatedTitle.isNotBlank() && updatedDescription.isNotBlank() && updatedLocation.isNotBlank()) {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val currentDate = dateFormat.format(Date())

            val updatedNote = TravelNote(
                id = noteId,
                title = updatedTitle,
                description = updatedDescription,
                location = updatedLocation,
                date = currentDate  // Gunakan tanggal yang sudah diformat
            )

            val currentUser = FirebaseAuth.getInstance().currentUser
            val userId = currentUser?.uid

            if (userId != null) {
                firebaseHelper.updateTravelNote(userId, noteId, updatedNote)
                Toast.makeText(requireContext(), "Note updated successfully!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi untuk mengonfirmasi penghapusan catatan dengan AlertDialog
    private fun confirmDeleteNote() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes") { _, _ ->
                deleteNote()
            }
            .setNegativeButton("No", null)
            .show()
    }

    // Fungsi untuk menghapus catatan dari Firebase
    private fun deleteNote() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            firebaseHelper.deleteTravelNote(userId, noteId)
            Toast.makeText(requireContext(), "Note deleted successfully", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    // Lepaskan binding saat view dihancurkan
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}