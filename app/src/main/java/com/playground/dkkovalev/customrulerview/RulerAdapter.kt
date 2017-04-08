package com.playground.dkkovalev.customrulerview

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by DKKovalev on 09.04.2017.
 */
class RulerAdapter(val items:ArrayList<MockItem>) : RecyclerView.Adapter<RulerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RulerViewHolder {
        val inflater:LayoutInflater = LayoutInflater.from(parent!!.context)
        val view: View = inflater.inflate(R.layout.item, parent, false)
        return RulerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RulerViewHolder?, position: Int) {
        val color = Color.rgb((Math.random()*255).toInt(), (Math.random()*255).toInt(), (Math.random()*255).toInt())
        holder!!.container.setBackgroundColor(color)
    }
}