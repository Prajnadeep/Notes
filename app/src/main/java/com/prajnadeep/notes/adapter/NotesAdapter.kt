package com.prajnadeep.notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prajnadeep.notes.R
import com.prajnadeep.notes.database.models.User

class NotesAdapter(val noteList: List<User>) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>(){

    var onItemClick: ((User)->Unit) ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_view_note,parent,false)
        return NotesViewHolder(view,noteList)

    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.txtTitle.text = noteList[position].noteTitle
        holder.txtDesc.text = noteList[position].noteDesc
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    inner class NotesViewHolder(itemView: View, noteList: List<User>) : RecyclerView.ViewHolder(itemView) {
        var txtTitle: TextView = itemView.findViewById<TextView>(R.id.noteTitleTextView)
        var txtDesc: TextView = itemView.findViewById<TextView>(R.id.noteDescTextView)
        
        init{
            itemView.setOnClickListener {
                onItemClick?.invoke(noteList[adapterPosition])
            }
        }
    }

}