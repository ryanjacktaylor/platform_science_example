package com.duckmethodsw.platformscience.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duckmethodsw.platformscience.R
import com.duckmethodsw.platformscience.models.Assigment

class AssignmentsAdapter : RecyclerView.Adapter<AssignmentsAdapter.AssignmentViewHolder>()
{

    private var assignments : List<Assigment> = ArrayList(0)

    inner class AssignmentViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val driver: TextView = view.findViewById(R.id.txt_driver)
        val shipment: TextView = view.findViewById(R.id.txt_shipment)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun bindData(assignments: List<Assigment>) {
        this.assignments = assignments
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_assignment_list_item, parent, false)
        return AssignmentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AssignmentViewHolder, position: Int) {
        holder.driver.text = assignments[position].driver
        holder.shipment.text = assignments[position].shipment
        /*
        The onClickListener seems strange but it was part of the requirements. The requirements
        state the shipment assignment is to be shown when the driver's name is clicked. The
        assignments are all previously calculated, so we only need to make it visible.
        */
        holder.itemView.setOnClickListener {
            holder.shipment.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return assignments.size
    }
}