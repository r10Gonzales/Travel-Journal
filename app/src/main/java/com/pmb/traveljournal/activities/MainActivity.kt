package com.pmb.traveljournal.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.pmb.traveljournal.models.TravelNote
import com.pmb.traveljournal.utils.FirebaseHelper
import com.pmb.traveljournal.adapter.TravelNoteAdapter
import com.pmb.traveljournal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val firebaseHelper = FirebaseHelper()
    private val travelNotes = mutableListOf<TravelNote>()
    private lateinit var adapter: TravelNoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menggunakan View Binding untuk mengakses layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TravelNoteAdapter(travelNotes) { note ->
            // Menambahkan click listener pada item
            val intent = Intent(this, TravelNoteDetailActivity::class.java)
            intent.putExtra("NOTE_ID", note.id)
            startActivity(intent)
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
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Navigasi ke Add Travel Note activity
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddTravelNoteActivity::class.java)
            startActivity(intent)
        }
    }
}
