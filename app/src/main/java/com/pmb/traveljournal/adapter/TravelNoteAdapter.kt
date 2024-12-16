package com.pmb.traveljournal.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pmb.traveljournal.databinding.ItemTravelNoteBinding
import com.pmb.traveljournal.models.TravelNote

class TravelNoteAdapter(
    private val travelNotes: List<TravelNote>,
    private val onClick: (TravelNote) -> Unit // Menambahkan parameter onClick
) : RecyclerView.Adapter<TravelNoteAdapter.TravelNoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelNoteViewHolder {
        val binding = ItemTravelNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TravelNoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TravelNoteViewHolder, position: Int) {
        val note = travelNotes[position]
        holder.bind(note)
    }

    override fun getItemCount() = travelNotes.size

    inner class TravelNoteViewHolder(private val binding: ItemTravelNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: TravelNote) {
            binding.titleTextView.text = note.title
            binding.locationTextView.text = note.location
            binding.dateTextView.text = note.date

            // Set onClickListener untuk item
            itemView.setOnClickListener {
                onClick(note) // Akan memanggil onClick dengan objek note
            }
        }
    }
}
