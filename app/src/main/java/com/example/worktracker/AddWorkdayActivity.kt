package com.example.worktracker

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_add_workday.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import java.time.Duration
import java.time.LocalDate
import java.time.Month
import java.time.temporal.ChronoUnit
import javax.xml.datatype.DatatypeConstants.DAYS
import android.R.string








class AddWorkdayActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    // variables for dateTime Picker
    var day : Int = 0
    var month : Int = 0
    var year : Int = 0
    var hour : Int = 0
    var minute : Int = 0

    var day_x : Int = 0
    var month_x : Int = 0
    var year_x : Int = 0
    var hour_x : Int = 0
    var minute_x : Int = 0

    var clickedStart = false
    var clickedEnd = false
    var arrayStart = IntArray(5)
    var arrayEnd = IntArray(5)

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        year_x = year
        month_x = month + 1
        day_x = dayOfMonth
        val calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        var timePickerDialog = TimePickerDialog(this, this,
            hour, minute, true)
        timePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        hour_x = hourOfDay
        minute_x = minute

        if (clickedStart) {
            editHeureDebut.setText(
                "${(if (day_x < 10) "0" else "") + day_x}/" +
                        "${(if (month_x < 10) "0" else "") + month_x}/$year_x " +
                        "${(if (hour_x < 10) "0" else "") + hour_x}:" +
                        "${(if (minute_x < 10) "0" else "") + minute_x}"
            )
            arrayStart[0] = year_x
            arrayStart[1] = month_x
            arrayStart[2] = day_x
            arrayStart[3] = hour_x
            arrayStart[4] = minute_x

            // print number of hours between dates
            if (!editHeureDebut.text.isEmpty() && !editHeureFin.text.isEmpty()){

                if (compareDateTime(arrayStart, arrayEnd)){
                    //println("OK")
                    txtErrorLog.visibility = View.INVISIBLE
                    showNumberOfHours()
                    txtTempsTotal.visibility = View.VISIBLE
                }else{
                    txtTempsTotal.visibility = View.INVISIBLE
                    txtErrorLog.text = "Vos dates ne sont pas cohérantes."
                }
            }else{
                //println("NOT OK")
                txtErrorLog.visibility = View.VISIBLE
                txtTempsTotal.visibility = View.INVISIBLE
                txtErrorLog.text = "Veuillez entrer toutes les données correctement."
            }
        }

        if (clickedEnd) {
            editHeureFin.setText(
                "${(if (day_x < 10) "0" else "") + day_x}/" +
                        "${(if (month_x < 10) "0" else "") + month_x}/$year_x " +
                        "${(if (hour_x < 10) "0" else "") + hour_x}:" +
                        "${(if (minute_x < 10) "0" else "") + minute_x}"
            )
            arrayEnd[0] = year_x
            arrayEnd[1] = month_x
            arrayEnd[2] = day_x
            arrayEnd[3] = hour_x
            arrayEnd[4] = minute_x

            // print number of hours between dates
            if (!editHeureDebut.text.isEmpty() && !editHeureFin.text.isEmpty()){

                if (compareDateTime(arrayStart, arrayEnd)){
                    //println("OK")
                    txtErrorLog.visibility = View.INVISIBLE
                    showNumberOfHours()
                    txtTempsTotal.visibility = View.VISIBLE
                }else{
                    txtTempsTotal.visibility = View.INVISIBLE
                    txtErrorLog.text = "Vos dates ne sont pas cohérantes."
                }
            }else{
                //println("NOT OK")
                txtErrorLog.visibility = View.VISIBLE
                txtTempsTotal.visibility = View.INVISIBLE
                txtErrorLog.text = "Veuillez entrer toutes les données correctement."
            }

        }
    }



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_workday)

        // get live date
        editDateCreation.text = getSystemDate()




        buttonSelectHeureDebut.setOnClickListener{
            clickedEnd = false
            clickedStart = true

            runDateTimeSelector()
        }

        buttonSelectHeureFin.setOnClickListener {
            clickedStart = false
            clickedEnd = true

            runDateTimeSelector()


        }

        btnEnregistrer.setOnClickListener{
            if (!editHeureDebut.text.isEmpty() && !editHeureFin.text.isEmpty()){

                txtErrorLog.visibility = View.VISIBLE
                txtErrorLog.text = "Aucune heure de travail choisie."


            }
        }

        btnAnnuler.setOnClickListener{
            //clearEditText()
            // retour a la page arriere

            getHoursBetweenDates(editHeureDebut.text.toString(), editHeureFin.text.toString())



        }
    }


    /* ------------------------------------------------------------------------------------- */

    private fun showNumberOfHours() {

        var timeLine = getHoursBetweenDates(editHeureDebut.text.toString(), editHeureFin.text.toString())
        txtTempsTotal.text = "${timeLine[1]}h:${timeLine[2]} de travail"
    }

    private fun clearEditText (){

        editHeureDebut.text.clear()
        editHeureFin.text.clear()
    }

    private fun runDateTimeSelector (){

        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
        var datePickerDialog = DatePickerDialog(this,this, year, month, day)
        datePickerDialog.show()
    }

    private fun compareDateTime (arrayDebut : IntArray, arrayFin : IntArray) : Boolean {

        var sdf = SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH)
        var stf = SimpleDateFormat("HH:mm", Locale.FRENCH)
        val date1 = sdf.parse("${arrayDebut[0]}-${arrayDebut[1]}-${arrayDebut[2]}")
        val date2 = sdf.parse("${arrayFin[0]}-${arrayFin[1]}-${arrayFin[2]}")
        val time1 = stf.parse("${arrayDebut[3]}:${arrayDebut[4]}")
        val time2 = stf.parse("${arrayFin[3]}:${arrayFin[4]}")


        return if(date1.before(date2)){
            //println( "Date1 is before Date2 OK")
            true
        }else date1 == date2 && time1.before(time2)
    }

    private fun getSystemDate () : String {

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formatted = current.format(formatter)

        return formatted
    }

    private fun getHoursBetweenDates(dateStart : String, dateStop : String ) : Array<String?> {

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

            /*
            println("$diffDays days, ")
            println("$diffHours hours, ")
            println("$diffMinutes minutes, ")
            //println("$diffSeconds seconds.")*/

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return timeBetweenTwoDates
    }
}
