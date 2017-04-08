package com.playground.dkkovalev.customrulerview

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout

/**
 * Created by DKKovalev on 09.04.2017.
 */
class RulerViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {
    val container = itemView.findViewById(R.id.container) as LinearLayout
}