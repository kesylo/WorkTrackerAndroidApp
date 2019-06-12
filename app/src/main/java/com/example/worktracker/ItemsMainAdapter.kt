package com.example.worktracker

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ItemsMainAdapter : RecyclerView.Adapter<CustomViewHolder>(){


    override fun getItemCount(): Int {
        return 8
    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): CustomViewHolder {
        // create the view
        val layoutInflater = LayoutInflater.from(p0?.context)
        val cellForRow = layoutInflater.inflate(R.layout.list_items, p0,false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(p0: CustomViewHolder, position: Int) {

    }
}

class CustomViewHolder (v: View): RecyclerView.ViewHolder(v){

}