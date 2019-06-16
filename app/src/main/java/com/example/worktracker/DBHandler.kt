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
        private val DATABASE_NAME = "Worker.db"
        private val DATABASE_VERSION = 1

        // define first table
        val WORKDAY_TABLE_NAME = "Workday"
        val COLUMN_WORKDAY_ID = "id"
        val COLUMN_WORKDAY_CREATION_DATE = "creation_date"
        val COLUMN_WORKDAY_WORK_DAY_ONLY = "work_day_only"
        val COLUMN_WORKDAY_WORK_MONTH_ONLY = "work_month_only"
        val COLUMN_WORKDAY_START_TIME = "start_time"
        val COLUMN_WORKDAY_END_TIME = "end_time"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        // when you create the app, you create the database and table
        val CREATE_WORKDAYS_TABLE = ("CREATE TABLE $WORKDAY_TABLE_NAME (" +
                "$COLUMN_WORKDAY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_WORKDAY_CREATION_DATE TEXT," +
                "$COLUMN_WORKDAY_WORK_DAY_ONLY TEXT," +
                "$COLUMN_WORKDAY_WORK_MONTH_ONLY TEXT," +
                "$COLUMN_WORKDAY_START_TIME TEXT," +
                "$COLUMN_WORKDAY_END_TIME TEXT)")
        db?.execSQL(CREATE_WORKDAYS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

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

    fun updateWorkday (id : String, workDayOnly : String) : Boolean{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        var result = false
        contentValues.put(COLUMN_WORKDAY_WORK_DAY_ONLY,workDayOnly)
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