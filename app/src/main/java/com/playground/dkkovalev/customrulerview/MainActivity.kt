package com.playground.dkkovalev.customrulerview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var firstItemPosition = 0
    var screenWidth = 0
    var cellWidth = 0
    var headerWidth = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvInfo = findViewById(R.id.tv_info) as TextView

        val mockItems: ArrayList<MockItem> = ArrayList()
        for (i in 0..90) {
            val mockItem: MockItem = MockItem()
            mockItem.startDate = i
            mockItem.endDate = i + 1
            mockItems.add(mockItem)
        }

        val displayMetrics: DisplayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels

        cellWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60F, resources.displayMetrics).toInt()
        val offset = (screenWidth / 2) - (cellWidth / 2)
        headerWidth = offset + cellWidth

        val adapter:RulerAdapter = RulerAdapter(mockItems)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val recyclerView = findViewById(R.id.recycler) as RecyclerView
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val firstVisibleItem = layoutManager.findViewByPosition(layoutManager.findFirstCompletelyVisibleItemPosition())
                var calculatedX = 0
                if (firstItemPosition == 0) {
                    calculatedX = Math.abs(firstVisibleItem.left)
                } else {
                    calculatedX = 0
                }

                tvInfo.text = "calculated x: $calculatedX"

                Log.i("TAG", calculatedX.toString())
            }
        })
    }
}
