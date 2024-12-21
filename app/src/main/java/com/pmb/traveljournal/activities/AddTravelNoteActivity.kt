package com.pmb.traveljournal.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pmb.traveljournal.models.TravelNote
import com.pmb.traveljournal.utils.FirebaseHelper
import com.pmb.traveljournal.workers.ReminderWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.pmb.traveljournal.databinding.ActivityAddTravelNoteBinding
import java.util.concurrent.TimeUnit

class AddTravelNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTravelNoteBinding
    private val firebaseHelper = FirebaseHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTravelNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

                // Mendapatkan UID pengguna yang sedang login
                val currentUser = FirebaseAuth.getInstance().currentUser
                val userId = currentUser?.uid

                if (userId != null) {
                    firebaseHelper.addTravelNote(userId, newNote)
                    Toast.makeText(this, "Note added successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun scheduleReminder() {
        val reminderWork = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(1, TimeUnit.DAYS)  // Set delay (1 day)
            .build()

        WorkManager.getInstance(this).enqueue(reminderWork)
    }
}
