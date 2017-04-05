package com.playground.dkkovalev.customrulerview

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent

class MainActivity : AppCompatActivity() {

    lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ruler: MyScaleView = findViewById(R.id.scale) as MyScaleView

        val handler: Handler = Handler()
        runnable = Runnable { handler.post { scrollToRight(ruler) } }
        runnable.run()
    }

    private fun scrollToRight(rulerView: MyScaleView) {
        val motionEvent: MotionEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_MOVE, 20f, 0f, 0)
        rulerView.dispatchTouchEvent(motionEvent)
    }
}
