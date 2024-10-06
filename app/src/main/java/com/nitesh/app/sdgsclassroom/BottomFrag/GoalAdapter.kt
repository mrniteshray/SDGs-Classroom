package com.nitesh.app.sdgsclassroom.BottomFrag

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.nitesh.app.sdgsclassroom.Sub_Detail
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nitesh.app.sdgsclassroom.R

class GoalAdapter(private val goalsList: List<Goal>) :
    RecyclerView.Adapter<GoalAdapter.GoalViewHolder>() {

    class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goalIcon: ImageView = itemView.findViewById(R.id.imageViewGoalIcon)
        val goalTitle: TextView = itemView.findViewById(R.id.textViewGoalTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sdgs_cardview, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goalsList[position]
        holder.goalTitle.text = goal.title
        Glide.with(holder.itemView.context)
            .load(goal.iconResId)  // Image from drawable
            .into(holder.goalIcon)
        holder.goalIcon.setOnClickListener {
            var intent = Intent(holder.itemView.context,Sub_Detail::class.java)
            intent.putExtra("goalId",goal.goalid)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return goalsList.size
    }
}
