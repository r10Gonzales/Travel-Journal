package com.pmb.traveljournal.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.pmb.traveljournal.models.TravelNote

class FirebaseHelper {

    private val database = FirebaseDatabase.getInstance().getReference("TravelNotes")

    // Menyimpan catatan berdasarkan UID pengguna
    fun addTravelNote(userId: String, note: TravelNote) {
        val noteId = database.child(userId).push().key
        if (noteId != null) {
            note.id = noteId
            database.child(userId).child(noteId).setValue(note)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FirebaseHelper", "Data berhasil disimpan dengan ID: $noteId")
                    } else {
                        Log.e("FirebaseHelper", "Error menyimpan data", task.exception)
                    }
                }
        }
    }

    // Mendapatkan catatan berdasarkan UID pengguna
    fun getTravelNotes(userId: String) = database.child(userId)

    // Memperbarui catatan
    fun updateTravelNote(userId: String, id: String, updatedNote: TravelNote) {
        database.child(userId).child(id).setValue(updatedNote)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseHelper", "Data berhasil diperbarui")
                } else {
                    Log.e("FirebaseHelper", "Gagal memperbarui data", task.exception)
                }
            }
    }

    // Menghapus catatan
    fun deleteTravelNote(userId: String, id: String) {
        database.child(userId).child(id).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseHelper", "Data berhasil dihapus")
                } else {
                    Log.e("FirebaseHelper", "Gagal menghapus data", task.exception)
                }
            }
    }
}
