package com.playground.dkkovalev.customrulerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyScaleView extends ScrollView {
    static int screenSize;
    static private float pxmm = screenSize / 67.f;
    int width, height, midScreenPoint;
    float startingPoint = 0;
    float downpoint = 0, movablePoint = 0, downPointClone = 0;
    private float mainPoint = 0, mainPointClone = 0;
    boolean isDown = false;
    boolean isUpward = false;
    private boolean isMove;
    //private onViewUpdateListener mListener;
    private Paint gradientPaint;
    private float rulersize = 0;
    private Paint rulerPaint, textPaint, goldenPaint;
    private int endPoint;
    boolean isSizeChanged = false;
    float userStartingPoint = 0f;
    private int scaleLineSmall;
    private int scaleLineMedium;
    private int scaleLineLarge;
    private int textStartPoint;
    private int yellowLineStrokeWidth;
    boolean isFirstTime = true;

    public MyScaleView(Context context, AttributeSet foo) {
        super(context, foo);
        if (!isInEditMode()) {
            init(context);
        }
    }

    private void init(Context context) {
        yellowLineStrokeWidth = (int) getResources().getDimension(R.dimen.yellow_line_stroke_width);
        gradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rulersize = pxmm * 10;
        rulerPaint = new Paint();
        rulerPaint.setStyle(Paint.Style.STROKE);
        rulerPaint.setStrokeWidth(0);
        rulerPaint.setAntiAlias(false);
        rulerPaint.setColor(Color.WHITE);
        textPaint = new TextPaint();
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(0);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(getResources().getDimension(R.dimen.text_size));
        textPaint.setColor(Color.WHITE);
        goldenPaint = new Paint();
        goldenPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        goldenPaint.setColor(context.getResources().getColor(android.R.color.holo_orange_light));
        goldenPaint.setStrokeWidth(yellowLineStrokeWidth);
        goldenPaint.setStrokeJoin(Paint.Join.ROUND);
        goldenPaint.setStrokeCap(Paint.Cap.ROUND);
        goldenPaint.setPathEffect(new CornerPathEffect(10));
        goldenPaint.setAntiAlias(true);
        scaleLineSmall = (int) getResources().getDimension(R.dimen.scale_line_small);
        scaleLineMedium = (int) getResources().getDimension(R.dimen.scale_line_medium);
        scaleLineLarge = (int) getResources().getDimension(R.dimen.scale_line_large);
        textStartPoint = (int) getResources().getDimension(R.dimen.text_start_point);
    }

/*    public void setUpdateListener(onViewUpdateListener onViewUpdateListener) {
        mListener = onViewUpdateListener;
    }*/

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        width = w;
        height = h;
        screenSize = width;
        pxmm = screenSize / 67.f;
        midScreenPoint = height / 2;
        endPoint = height - 40;
        if (isSizeChanged) {
            isSizeChanged = false;
            mainPoint = midScreenPoint - (userStartingPoint * 10 * pxmm);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        startingPoint = mainPoint;
        for (int i = 1; ; ++i) {
            if (startingPoint > screenSize) {
                break;
            }
            startingPoint = startingPoint + pxmm;
            int size = (i % 10 == 0) ? scaleLineLarge : (i % 5 == 0) ? scaleLineMedium : scaleLineSmall;
            canvas.drawLine(startingPoint, endPoint - size, startingPoint, endPoint, rulerPaint);
            if (i % 10 == 0) {
                System.out.println("done   " + i);
                canvas.drawText((i / 10) + " cm", startingPoint + 8, endPoint - textStartPoint, textPaint);
            }
        }
        canvas.drawLine(midScreenPoint, 0f, midScreenPoint, width - 20, goldenPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("touch event fire");
        mainPointClone = mainPoint;
        if (mainPoint < 0) {
            mainPointClone = -mainPoint;
        }
        float clickPoint = ((midScreenPoint + mainPointClone) / (pxmm * 10));
        /*if (mListener != null) {
            //mListener.onViewUpdate((midScreenPoint + mainPointClone) / (pxmm * 10));
        }*/
        System.out.println("click point" + clickPoint + "");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMove = true;
                isDown = false;
                isUpward = false;
                downpoint = event.getX();
                downPointClone = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                movablePoint = event.getX();
                if (downPointClone > movablePoint) {
                    /**
                     * if user first starts moving downward and then upwards then
                     * this method makes it to move upward
                     */
                    if (isUpward) {
                        downpoint = event.getX();
                        downPointClone = downpoint;
                    }
                    isDown = true;
                    isUpward = false;
                    /**
                     * make this differnce of 1, otherwise it moves very fast and
                     * nothing shows clearly
                     */
                    if (downPointClone - movablePoint > 1) {
                        mainPoint = mainPoint + (-(downPointClone - movablePoint));
                        downPointClone = movablePoint;
                        invalidate();
                    }
                } else {
                    // downwards
                    if (isMove) {
                        /**
                         * if user first starts moving upward and then downwards,
                         * then this method makes it to move upward
                         */
                        if (isDown) {
                            downpoint = event.getX();
                            downPointClone = downpoint;
                        }
                        isDown = false;
                        isUpward = true;
                        if (movablePoint - downpoint > 1) {
                            mainPoint = mainPoint + ((movablePoint - downPointClone));
                            downPointClone = movablePoint;
                            if (mainPoint > 0) {
                                mainPoint = 0;
                                isMove = false;
                            }
                            invalidate();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("up");
            default:
                break;
        }
        return true;
    }

    public void setStartingPoint(float point) {
        userStartingPoint = point;
        isSizeChanged = true;
        if (isFirstTime) {
            isFirstTime = false;
            /*if (mListener != null) {
                //mListener.onViewUpdate(point);
            }*/
        }
    }
}
