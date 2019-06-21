package com.example.worktracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import java.lang.Exception

class DBHandler (context : Context, name : String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper (context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private val DATABASE_NAME = "Worker2.db"
        private val DATABASE_VERSION = 3

        // define first table
        val WORKDAY_TABLE_NAME = "Workday"
        val COLUMN_WORKDAY_ID = "id"
        val COLUMN_WORKDAY_CREATION_DATE = "creation_date"
        val COLUMN_WORKDAY_WORK_DAY_ONLY = "work_day_only"
        val COLUMN_WORKDAY_WORK_MONTH_ONLY = "work_month_only"
        val COLUMN_WORKDAY_START_TIME = "start_time"
        val COLUMN_WORKDAY_END_TIME = "end_time"

        val COLUMN_WORKDAY_NBR_HOURS_TOTAL = "work_day_total_hour"
        val COLUMN_ENTERPRISE = "enterprise"
        val COLUMN_SALARY_PER_HOUR  = "salary"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        // when you create the app, you create the database and table
        val CREATE_WORKDAYS_TABLE = ("CREATE TABLE $WORKDAY_TABLE_NAME (" +
                "$COLUMN_WORKDAY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_WORKDAY_CREATION_DATE TEXT," +
                "$COLUMN_WORKDAY_WORK_DAY_ONLY TEXT," +
                "$COLUMN_WORKDAY_WORK_MONTH_ONLY TEXT," +
                "$COLUMN_WORKDAY_NBR_HOURS_TOTAL TEXT," +
                "$COLUMN_ENTERPRISE TEXT," +
                "$COLUMN_SALARY_PER_HOUR TEXT," +
                "$COLUMN_WORKDAY_START_TIME TEXT," +
                "$COLUMN_WORKDAY_END_TIME TEXT)")
        db?.execSQL(CREATE_WORKDAYS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // change db file
   /*     if (oldVersion < 2){
            db?.execSQL("Alter Table $WORKDAY_TABLE_NAME " +
            "Add $COLUMN_SALARY_PER_HOUR TEXT NULL" +
                    "Add $COLUMN_ENTERPRISE TEXT NULL" +
                    "Add $COLUMN_WORKDAY_NBR_HOURS_TOTAL TEXT NULL")
        }*/

    }

    fun getWordays (myCtx: Context) : ArrayList<ModelWorker>{
        val query = "Select * from $WORKDAY_TABLE_NAME"
        val db = this.readableDatabase
        val cursor =  db.rawQuery(query,null)
        val workdays = ArrayList<ModelWorker>()

        if (cursor.count == 0){
            Toast.makeText(myCtx, "No data", Toast.LENGTH_SHORT).show()
        }else{

            cursor.moveToFirst()
            while (!cursor.isAfterLast){
                val workday = ModelWorker()
                workday.workdayID = cursor.getInt(cursor.getColumnIndex(COLUMN_WORKDAY_ID))
                workday.creationDate = cursor.getString(cursor.getColumnIndex(COLUMN_WORKDAY_CREATION_DATE))
                workday.workDayOnly = cursor.getString(cursor.getColumnIndex(COLUMN_WORKDAY_WORK_DAY_ONLY))

                workday.numberOfHours = cursor.getString(cursor.getColumnIndex(COLUMN_WORKDAY_NBR_HOURS_TOTAL))
                workday.enterprise = cursor.getString(cursor.getColumnIndex(COLUMN_ENTERPRISE))
                workday.salaryPerHour = cursor.getString(cursor.getColumnIndex(COLUMN_SALARY_PER_HOUR))

                workday.workMonthOnly = cursor.getString(cursor.getColumnIndex(COLUMN_WORKDAY_WORK_MONTH_ONLY))
                workday.startHour = cursor.getString(cursor.getColumnIndex(COLUMN_WORKDAY_START_TIME))
                workday.endHour = cursor.getString(cursor.getColumnIndex(COLUMN_WORKDAY_END_TIME))
                workdays.add(workday)
                cursor.moveToNext()
            }

            Toast.makeText(myCtx, "${cursor.count} item found", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        db.close()
        return workdays
    }

    fun addWorkDay (myCtx: Context, worker: ModelWorker){
        val values = ContentValues()
        values.put(COLUMN_WORKDAY_CREATION_DATE, worker.creationDate)
        values.put(COLUMN_WORKDAY_WORK_DAY_ONLY, worker.workDayOnly)
        values.put(COLUMN_WORKDAY_WORK_MONTH_ONLY, worker.workMonthOnly)
        values.put(COLUMN_WORKDAY_START_TIME, worker.startHour)
        values.put(COLUMN_WORKDAY_END_TIME, worker.endHour)

        values.put(COLUMN_WORKDAY_NBR_HOURS_TOTAL, worker.numberOfHours)
        values.put(COLUMN_ENTERPRISE, worker.enterprise)
        values.put(COLUMN_SALARY_PER_HOUR, worker.salaryPerHour)

        val db = this.writableDatabase
        try {
            db.insert(WORKDAY_TABLE_NAME, null, values) // this replaces "Insert into ..."
            Toast.makeText(myCtx, "Jour de travail enregistré avec succès!", Toast.LENGTH_SHORT).show()
        }catch (e : Exception){
            Toast.makeText(myCtx, "Erreur lors de l'ajout du jour de travail.", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun deleteWorkday (workdayID : Int) : Boolean{
        val query = "Delete From $WORKDAY_TABLE_NAME where $COLUMN_WORKDAY_ID = $workdayID"
        val db = this.writableDatabase
        var result = false
        try {
            val cursor = db.execSQL(query)
            result = true
        }catch (e : Exception){
            Log.e(ContentValues.TAG, "Erreur de suppression")
        }
        db.close()
        return result
    }

    fun updateWorkday (id : String, creationDate : String, workDayOnly : String, workMonthOnly : String,
                       startHour : String, endHour : String, numberOfHour : String, enterprise : String,
                       salaryPerHour : String) : Boolean {

        val db = this.writableDatabase
        val contentValues = ContentValues()
        var result = false

        contentValues.put(COLUMN_WORKDAY_CREATION_DATE, creationDate)
        contentValues.put(COLUMN_WORKDAY_WORK_DAY_ONLY, workDayOnly)
        contentValues.put(COLUMN_WORKDAY_WORK_MONTH_ONLY, workMonthOnly)
        contentValues.put(COLUMN_WORKDAY_START_TIME, startHour)
        contentValues.put(COLUMN_WORKDAY_END_TIME, endHour)
        contentValues.put(COLUMN_WORKDAY_NBR_HOURS_TOTAL, numberOfHour)
        contentValues.put(COLUMN_ENTERPRISE, enterprise)
        contentValues.put(COLUMN_SALARY_PER_HOUR, salaryPerHour)


        try {
            db.update(WORKDAY_TABLE_NAME, contentValues, "$COLUMN_WORKDAY_ID = ?", arrayOf(id))
            result = true
        }catch (e : Exception){
            result = false
            Log.e(ContentValues.TAG, "Erreur de mise a jour")
        }
        return result
    }
}