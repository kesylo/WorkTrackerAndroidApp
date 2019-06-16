package com.example.worktracker

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.list_items.view.*

class RVAdapter (myCtxt : Context, val workdays : ArrayList<ModelWorker>) : RecyclerView.Adapter<RVAdapter.ViewHolder>() {

    val myCtxt = myCtxt

    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        // get all listview elements in this class
        val txtCreationDay = itemView.textViewCreationDay!!
        val txtCreationMonth = itemView.textViewCreationMonth!!
        val txtStartHour = itemView.textViewHourFrom!!
        val txtStartMinute = itemView.txtStartMinute!!
        val txtEndHour = itemView.textViewHourTo!!
        val txtEndMinute = itemView.txtEndMinute!!
        val btnDelete = itemView.buttonDelete
        val txtNomEntreprise = itemView.txtNomEntreprise

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RVAdapter.ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.list_items, p0,false)
        return  ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return workdays.size
    }

    override fun onBindViewHolder(p0: RVAdapter.ViewHolder, p1: Int) {
        // Send data to view
        val workday : ModelWorker = workdays[p1]
        val workDayOnly = workday.workDayOnly
        val workMonthOnly = workday.workMonthOnly

        // split hour Start
        val longStartDateTime = workday.startHour
        val longStartDateTimeParts = longStartDateTime.split(" ")
        val longStartHour = longStartDateTimeParts[1]
        val longStartHourParts = longStartHour.split(":")
        val startHourOnly = longStartHourParts[0]
        val startMinuteOnly = longStartHourParts[1]

        // split hour End
        val longEndDateTime = workday.endHour
        val longEndDateTimeParts = longEndDateTime.split(" ")
        val longEndHour = longEndDateTimeParts[1]
        val longEndHourParts = longEndHour.split(":")
        val endHourOnly = longEndHourParts[0]
        val endMinuteOnly = longEndHourParts[1]

        when {
            workMonthOnly.toInt() == 1 -> {
                p0.txtCreationMonth.text = "Jan"
            }
            workMonthOnly.toInt() == 2 -> {
                p0.txtCreationMonth.text = "Fev"
            }
            workMonthOnly.toInt() == 3 -> {
                p0.txtCreationMonth.text = "Mar"
            }
            workMonthOnly.toInt() == 4 -> {
                p0.txtCreationMonth.text = "Avr"
            }
            workMonthOnly.toInt() == 5 -> {
                p0.txtCreationMonth.text = "Mai"
            }
            workMonthOnly.toInt() == 6 -> {
                p0.txtCreationMonth.text = "Jun"
            }
            workMonthOnly.toInt() == 7 -> {
                p0.txtCreationMonth.text = "Jul"
            }
            workMonthOnly.toInt() == 8 -> {
                p0.txtCreationMonth.text = "Aou"
            }
            workMonthOnly.toInt() == 9 -> {
                p0.txtCreationMonth.text = "Sep"
            }
            workMonthOnly.toInt() == 10 -> {
                p0.txtCreationMonth.text = "Oct"
            }
            workMonthOnly.toInt() == 11 -> {
                p0.txtCreationMonth.text = "Nov"
            }
            workMonthOnly.toInt() == 12 -> {
                p0.txtCreationMonth.text = "Dec"
            }
        }

        p0.txtCreationDay.text = workDayOnly
        p0.txtStartHour.text = startHourOnly
        p0.txtStartMinute.text = startMinuteOnly
        p0.txtEndHour.text = endHourOnly
        p0.txtEndMinute.text = endMinuteOnly

        // delete button
        p0.btnDelete.setOnClickListener{
            val creationDate = workday.creationDate

            // dialog
            var alertDialog = AlertDialog.Builder(myCtxt)
                .setTitle("Attention")
                .setMessage("Voulez vous supprimer ce jour de travail? ")
                .setPositiveButton("Oui", DialogInterface.OnClickListener { dialog, which ->

                    if (MainActivity.dbHandler.deleteWorkday(workday.workdayID)){
                        workdays.removeAt(p1)
                        notifyItemRemoved(p1)
                        notifyItemRangeChanged(p1, workdays.size)
                        Toast.makeText(myCtxt, "Jour de travail supprimé!", Toast.LENGTH_SHORT).show()
                    }else {
                        Toast.makeText(myCtxt, "Erreur lors de la suppression de l'entrée! ", Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton("Non", DialogInterface.OnClickListener { dialog, which ->  })
                .setIcon(R.drawable.trash1)
                .show()
        }


        // Update
        p0.txtNomEntreprise.setOnClickListener {
            val inflater = LayoutInflater.from(myCtxt)
            val view = inflater.inflate(R.layout.activity_update_workday, null)

            // edit these
            val txtWorkday : TextView = view.findViewById(R.id.editUpWorkStartDay)

            // with the current values
            txtWorkday.text = workday.startHour

            val builder = AlertDialog.Builder(myCtxt)
                .setTitle("Mofifier")
                .setMessage("Voulez vous modifier ce jour de travail? ")
                .setPositiveButton("Oui", DialogInterface.OnClickListener { dialog, which ->

                    if (MainActivity.dbHandler.deleteWorkday(workday.workdayID)){
                        workdays.removeAt(p1)
                        notifyItemRemoved(p1)
                        notifyItemRangeChanged(p1, workdays.size)
                        Toast.makeText(myCtxt, "Jour de travail supprimé!", Toast.LENGTH_SHORT).show()
                    }else {
                        Toast.makeText(myCtxt, "Erreur lors de la suppression de l'entrée! ", Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton("Non", DialogInterface.OnClickListener { dialog, which ->  })
                .setIcon(R.drawable.trash1)
                .show()
        }
    }


}

