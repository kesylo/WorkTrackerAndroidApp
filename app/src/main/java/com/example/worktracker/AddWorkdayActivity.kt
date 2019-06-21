package com.example.worktracker

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_workday.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class AddWorkdayActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    // variables for dateTime Picker
    var day: Int = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0

    var day_x: Int = 0
    var month_x: Int = 0
    var year_x: Int = 0
    var hour_x: Int = 0
    var minute_x: Int = 0

    var clickedStart = false
    var clickedEnd = false
    var arrayStart = IntArray(5)
    var arrayEnd = IntArray(5)

    // dataSet for autoComplete features text box
    val enterprises = arrayOf(
        "Red", "Green", "Blue", "Maroon", "Magenta",
        "Gold", "GreenYellow"
    )

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        year_x = year
        month_x = month + 1
        day_x = dayOfMonth
        val calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        var timePickerDialog = TimePickerDialog(
            this, this,
            hour, minute, true
        )
        timePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        hour_x = hourOfDay
        minute_x = minute

        if (clickedStart) {
            editHeureDebut.text = "${(if (day_x < 10) "0" else "") + day_x}/" +
                    "${(if (month_x < 10) "0" else "") + month_x}/$year_x " +
                    "${(if (hour_x < 10) "0" else "") + hour_x}:" +
                    "${(if (minute_x < 10) "0" else "") + minute_x}"
            arrayStart[0] = year_x
            arrayStart[1] = month_x
            arrayStart[2] = day_x
            arrayStart[3] = hour_x
            arrayStart[4] = minute_x


            runAfterTimeSelection()

        }

        if (clickedEnd) {
            editHeureFin.text = "${(if (day_x < 10) "0" else "") + day_x}/" +
                    "${(if (month_x < 10) "0" else "") + month_x}/$year_x " +
                    "${(if (hour_x < 10) "0" else "") + hour_x}:" +
                    "${(if (minute_x < 10) "0" else "") + minute_x}"
            arrayEnd[0] = year_x
            arrayEnd[1] = month_x
            arrayEnd[2] = day_x
            arrayEnd[3] = hour_x
            arrayEnd[4] = minute_x

            runAfterTimeSelection()
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_workday)


        // get live date
        editDateCreationUP.text = getSystemDate()
        editHeureDebut.text = "${getSystemDate()} ${getSystemTime()}"
        editHeureFin.text = "${getSystemDate()} ${getSystemTime()}"

        // Set autoComplete feature
        editEntreprise.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, enterprises))
        // The minimum number of characters to type to show the drop down
        editEntreprise.threshold = 1




        buttonSelectHeureDebutUP.setOnClickListener {
            clickedEnd = false
            clickedStart = true

            runDateTimeSelector()
        }

        buttonSelectHeureFinUP.setOnClickListener {
            clickedStart = false
            clickedEnd = true

            runDateTimeSelector()
        }

        btnEnregistrerUP.setOnClickListener {
            if (txtErrorLog.visibility == View.INVISIBLE) {

                // prepare infos to be written
                val worker = ModelWorker()

                val workdayTime = editHeureDebut.text.toString()
                val workDayParts = workdayTime.split(" ")
                val workDate = workDayParts[0]
                val workDateParts = workDate.split("/")
                val workDayOnly = workDateParts[0]
                val workMonthOnly = workDateParts[1]

                worker.creationDate = editDateCreationUP.text.toString()
                worker.workDayOnly = workDayOnly
                worker.workMonthOnly = workMonthOnly
                worker.startHour = editHeureDebut.text.toString()
                worker.endHour = editHeureFin.text.toString()

                worker.salaryPerHour = editSalaire.text.toString()
                worker.enterprise = editEntreprise.text.toString()
                worker.numberOfHours = txtTempsTotal.text.toString()


                // write to DB
                MainActivity.dbHandler.addWorkDay(this, worker)

                // clear all fields
                clearEditText()


            } else {
                Toast.makeText(this, "Vérifiez tous les champs avant d'enregistrer", Toast.LENGTH_SHORT).show()
            }
        }

        btnAnnulerUP.setOnClickListener {
            clearEditText()
            // retour a la page arriere
            finish()
        }
    }


    /* ------------------------------------------------------------------------------------- */

    private fun runAfterTimeSelection() {
        if (!editHeureDebut.text.isEmpty() && !editHeureFin.text.isEmpty()) {
            // check if date1 is before date2
            if (compareDateTime(editHeureDebut.text.toString(), editHeureFin.text.toString())) {

                // Make error messsage invisible
                txtErrorLog.visibility = View.INVISIBLE

                // get hours between dates
                val timeValues = getHoursBetweenDates(editHeureDebut.text.toString(), editHeureFin.text.toString())

                // cast to int
                val finalDay = timeValues[0]!!.toInt()
                val finalHour = timeValues[1]!!.toInt()
                val finalMinute = timeValues[2]!!.toInt()

                // check if day is greater than 1
                if (finalDay < 1) {

                    // send them to textview while adding a 0 in front if less than 10
                    txtTempsTotal.text =
                        "${(if (finalHour < 10) "0" else "") + finalHour}h:${(if (finalMinute < 10) "0" else "") + finalMinute} de travail"
                    txtTempsTotal.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
                } else {
                    txtErrorLog.visibility = View.VISIBLE
                    txtErrorLog.text = "Vous ne pouvez pas avoir un shift de plus de 24h. Vérifiez vos dates"
                    // reset hour text
                    txtTempsTotal.text = "00h:00 de travail"
                    txtTempsTotal.setTextColor(ContextCompat.getColor(this, R.color.colorRed))
                }

            } else {
                txtErrorLog.visibility = View.VISIBLE
                txtErrorLog.text = "Svp entrez des dates cohérantes."
                // reset hour text
                txtTempsTotal.text = "00h:00 de travail"
                txtTempsTotal.setTextColor(ContextCompat.getColor(this, R.color.colorRed))
            }

        } else {
            txtErrorLog.visibility = View.VISIBLE
            txtErrorLog.text = "Remplissez correctement les champs d'heure."
            // reset hour text
            txtTempsTotal.text = "00h:00 de travail"
        }
    }


    private fun clearEditText() {

        //editHeureDebut.text.clear()
        //editHeureFin.text.clear()
        editEntreprise.text.clear()
        editSalaire.text.clear()
    }

    private fun runDateTimeSelector() {

        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
        var datePickerDialog = DatePickerDialog(this, this, year, month, day)
        datePickerDialog.show()
    }

    private fun compareDateTime(dateStart: String, dateStop: String): Boolean {

        /*var sdf = SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH)
        var stf = SimpleDateFormat("HH:mm", Locale.FRENCH)
        val date1 = sdf.parse("${arrayDebut[0]}-${arrayDebut[1]}-${arrayDebut[2]}")
        val date2 = sdf.parse("${arrayFin[0]}-${arrayFin[1]}-${arrayFin[2]}")
        val time1 = stf.parse("${arrayDebut[3]}:${arrayDebut[4]}")
        val time2 = stf.parse("${arrayFin[3]}:${arrayFin[4]}")


        return if(date1.before(date2)){
            //println( "Date1 is before Date2 OK")
            true
        }else date1 == date2 && time1.before(time2)*/

        //val dateStart = "12/06/2019 10:31"
        //val dateStop = "12/06/2019 10:30"

        //HH converts hour in 24 hours format (0-23), day calculation
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm")

        var d1: Date? = null
        var d2: Date? = null

        d1 = format.parse(dateStart)
        d2 = format.parse(dateStop)

        var result = d1.compareTo(d2)
        //println(result)
        when {
            result < 0 -> {
                println("Date1 is before date2. OK")
                return true
            }
            result == 0 -> {
                println("Date1 = date2. BAD")
                return false
            }
            result > 0 -> {
                println("Date1 is after date2. BAD")
                return false
            }
        }
        return false
    }

    private fun getSystemDate(): String {

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formatted = current.format(formatter)

        return formatted
    }


    private fun getSystemTime(): String {
        val date = Date()
        val strDateFormat = "HH:mm"
        val dateFormat = SimpleDateFormat(strDateFormat)
        val formattedDate = dateFormat.format(date)
        return formattedDate
    }

    private fun getHoursBetweenDates(dateStart: String, dateStop: String): Array<String?> {

        val timeBetweenTwoDates = arrayOfNulls<String>(3)

        //val dateStart = "14/06/2012 09:29"
        //val dateStop = "14/06/2012 10:31"

        //HH converts hour in 24 hours format (0-23), day calculation
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm")

        var d1: Date? = null
        var d2: Date? = null

        try {
            d1 = format.parse(dateStart)
            d2 = format.parse(dateStop)

            //in milliseconds
            val diff = d2!!.time - d1!!.time

            //val diffSeconds = diff / 1000 % 60
            val diffMinutes = diff / (60 * 1000) % 60
            val diffHours = diff / (60 * 60 * 1000) % 24
            val diffDays = diff / (24 * 60 * 60 * 1000)

            // save in array
            timeBetweenTwoDates[0] = diffDays.toString()
            timeBetweenTwoDates[1] = diffHours.toString()
            timeBetweenTwoDates[2] = diffMinutes.toString()

            /*println(timeBetweenTwoDates[0])
            println(timeBetweenTwoDates[1])
            println(timeBetweenTwoDates[2])*/


        } catch (e: Exception) {
            e.printStackTrace()
        }

        return timeBetweenTwoDates
    }
}
