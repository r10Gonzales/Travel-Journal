package com.pmb.traveljournal.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.pmb.traveljournal.R
import com.pmb.traveljournal.models.TravelNote
import com.pmb.traveljournal.utils.FirebaseHelper

class TravelNoteDetailActivity : AppCompatActivity() {

    private val firebaseHelper = FirebaseHelper()

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button

    private lateinit var noteId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_note_detail)

        // Initialize views
        titleEditText = findViewById(R.id.titleEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        locationEditText = findViewById(R.id.locationEditText)
        updateButton = findViewById(R.id.updateButton)
        deleteButton = findViewById(R.id.deleteButton)

        // Get the note ID passed from the previous activity
        noteId = intent.getStringExtra("NOTE_ID") ?: ""

        if (noteId.isNotEmpty()) {
            // Load note details
            loadNoteDetails()
        } else {
            Toast.makeText(this, "Invalid note ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Set up listeners for the buttons
        updateButton.setOnClickListener {
            updateNote()
        }

        deleteButton.setOnClickListener {
            confirmDeleteNote()
        }
    }

    private fun loadNoteDetails() {
        firebaseHelper.getTravelNotes().child(noteId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val note = snapshot.getValue(TravelNote::class.java)
                if (note != null) {
                    titleEditText.setText(note.title)
                    descriptionEditText.setText(note.description)
                    locationEditText.setText(note.location)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@TravelNoteDetailActivity, "Error loading note", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateNote() {
        val updatedTitle = titleEditText.text.toString()
        val updatedDescription = descriptionEditText.text.toString()
        val updatedLocation = locationEditText.text.toString()

        if (updatedTitle.isNotBlank() && updatedDescription.isNotBlank() && updatedLocation.isNotBlank()) {
            val updatedNote = TravelNote(
                id = noteId,
                title = updatedTitle,
                description = updatedDescription,
                location = updatedLocation,
                date = System.currentTimeMillis().toString() // Or use the existing date if needed
            )
            firebaseHelper.updateTravelNote(noteId, updatedNote)
            Toast.makeText(this, "Note updated successfully!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmDeleteNote() {
        AlertDialog.Builder(this)
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes") { _, _ ->
                deleteNote()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteNote() {
        firebaseHelper.deleteTravelNote(noteId)
        Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_SHORT).show()
        finish()
    }
}
