package com.example.worktracker

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_items.view.*

class RVAdapter (myCtxt : Context, val workdays : ArrayList<ModelWorker>) : RecyclerView.Adapter<RVAdapter.ViewHolder>() {

    val myCtxt = myCtxt

    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val txtCreationDay = itemView.textViewCreationDay
        val txtCreationMonth = itemView.textViewCreationMonth
        val txtStartHour = itemView.textViewHourFrom
        val txtEndHour = itemView.textViewHourTo
        val btnDelete = itemView.buttonDelete

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RVAdapter.ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.list_items, p0,false)
        return  ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return workdays.size
    }

    override fun onBindViewHolder(p0: RVAdapter.ViewHolder, p1: Int) {

        val workday : ModelWorker = workdays[p1]

        p0.txtCreationDay.text = workday.creationDate
        p0.txtCreationMonth.text = workday.creationDate
        p0.txtStartHour.text = workday.startHour
        p0.txtEndHour.text = workday.endHour

    }


}

