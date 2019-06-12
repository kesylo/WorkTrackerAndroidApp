package com.example.worktracker

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DBHandler (context : Context, name : String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper (context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private val DATABASE_NAME = "Worker.db"
        private val DATABASE_VERSION = 1

        // define first table
        val WORKDAY_TABLE_NAME = "Workday"
        val COLUMN_WORKDAY_ID = "id"
        val COLUMN_WORKDAY_CREATION_DATE = "creation_date"
        val COLUMN_WORKDAY_START_TIME = "start_time"
        val COLUMN_WORKDAY_END_TIME = "end_time"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        // when you create the app, you create the database and table
        val CREATE_WORKDAYS_TABLE = ("CREATE TABLE $WORKDAY_TABLE_NAME (" +
                "$COLUMN_WORKDAY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_WORKDAY_CREATION_DATE TEXT," +
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
            while (cursor.moveToNext()){

                val workday = ModelWorker()
                workday.workdayID = cursor.getInt(cursor.getColumnIndex(COLUMN_WORKDAY_ID))
                workday.creationDate = cursor.getString(cursor.getColumnIndex(COLUMN_WORKDAY_CREATION_DATE))
                workday.startHour = cursor.getString(cursor.getColumnIndex(COLUMN_WORKDAY_START_TIME))
                workday.endHour = cursor.getString(cursor.getColumnIndex(COLUMN_WORKDAY_END_TIME))
                workdays.add(workday)
            }

            Toast.makeText(myCtx, "${cursor.count.toString()} item found", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        db.close()
        return workdays
    }
}