package com.example.worktracker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var dbHandler: DBHandler
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = DBHandler(this,null,null,1)
        viewWorkdays()


        // load the adapter
        //recyclerViewMain.layoutManager = LinearLayoutManager(this)
        // load items in list
        //recyclerViewMain.adapter = ItemsMainAdapter()


        floatingActionButton.setOnClickListener{
            val i = Intent (this, AddWorkdayActivity::class.java)
            startActivity(i)
        }

    }

    private fun viewWorkdays (){
        val workdaysList = dbHandler.getWordays(this)
        val adapter = RVAdapter(this, workdaysList)
        val recycler = recyclerViewMain
        recycler.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false) as RecyclerView.LayoutManager
        recycler.adapter = adapter
    }

    override fun onResume() {
        viewWorkdays()
        super.onResume()
    }
}
