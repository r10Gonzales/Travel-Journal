package com.pmb.traveljournal.utils

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.pmb.traveljournal.models.TravelNote

class FirebaseHelper {

    private val database = FirebaseDatabase.getInstance().getReference("TravelNotes")

    fun addTravelNote(note: TravelNote) {
        val noteId = database.push().key
        if (noteId != null) {
            note.id = noteId
            database.child(noteId).setValue(note)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FirebaseHelper", "Data berhasil disimpan dengan ID: $noteId")
                    } else {
                        Log.e("FirebaseHelper", "Error menyimpan data", task.exception)
                    }
                }
        }
    }


    fun getTravelNotes() = database

    fun updateTravelNote(id: String, updatedNote: TravelNote) {
        database.child(id).setValue(updatedNote)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseHelper", "Data berhasil diperbarui")
                } else {
                    Log.e("FirebaseHelper", "Gagal memperbarui data", task.exception)
                }
            }
    }


    fun deleteTravelNote(id: String) {
        database.child(id).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseHelper", "Data berhasil dihapus")
                } else {
                    Log.e("FirebaseHelper", "Gagal menghapus data", task.exception)
                }
            }
    }

}