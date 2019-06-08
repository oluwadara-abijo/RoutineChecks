package com.example.routinechecks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RoutineListAdapter internal constructor(context: Context) : RecyclerView.Adapter<RoutineListAdapter.WordViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var routines = emptyList<Routine>() // Cached copy of routines

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val routineTitleView: TextView = itemView.findViewById(R.id.routineTitle)
        val routineDescriptionView: TextView = itemView.findViewById(R.id.routineDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.list_item_routine, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val currentRoutine = routines[position]
        holder.routineTitleView.text = currentRoutine.title
        holder.routineDescriptionView.text = currentRoutine.description

    }

    internal fun setWords(routines: List<Routine>) {
        this.routines = routines
        notifyDataSetChanged()
    }

    override fun getItemCount() = routines.size
}