/*
 * Copyright (C) 2017, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * LineChartView.java
 *
 * LineChart View
 * 以点图的形式反应数据是否已经做了平滑处理
 *
 * Author huanghaiqi, Created at 2017-01-10
 *
 * Ver 1.0, 2017-01-10, huanghaiqi, Create file.
 */

package com.halohoop.linechartview.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

public class LineChartView extends View {

    private Paint mPaint;
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private PointF[] mPointFs;
    private int mCurrIndex = 0;
    private long mFirstCallTime;

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasuredWidth = getMeasuredWidth();
        mMeasuredHeight = getMeasuredHeight();
        //get point array
        mPointFs = new PointF[mMeasuredWidth];
        mCurrIndex = 0;
    }

    int largerValue = 10;

    @Override
    protected void onDraw(Canvas canvas) {
        if (mPointFs[0] == null) {
            return;
        }
        for (int i = 0; i < mPointFs.length; i++) {
            PointF pointF = mPointFs[i];
            if (pointF == null) {
                break;
            }
            //do draws
            float y = mMeasuredHeight / 2 + pointF.y * largerValue;
            canvas.drawPoint(pointF.x, y, mPaint);
        }
    }

    private float mFirstBaseValue = -1;
    private boolean mStartSaveData = false;
    private boolean mIsFirstCallSetStartValue = true;//为了让数据延时几秒钟才开始获取，保证数据稳定

    public void setStartValue(float value) {
        if (mStartSaveData) {
            if (mCurrIndex >= mPointFs.length) {
                mCurrIndex = 0;
            }
            if (mPointFs[0] == null) {
                mFirstBaseValue = value;
                mPointFs[mCurrIndex++] = new PointF();
                mPointFs[0].x = 0;
            } else {
                int currIndex = mCurrIndex++;
                PointF pointF = mPointFs[currIndex];
                if (pointF == null) {
                    mPointFs[currIndex] = new PointF();
                    pointF = mPointFs[currIndex];
                    pointF.x = currIndex;
                }
                pointF.y = value - mFirstBaseValue;
                postInvalidate();
            }
        } else {
            if (mIsFirstCallSetStartValue) {
                mIsFirstCallSetStartValue = false;
                mFirstCallTime = System.currentTimeMillis();
            } else {
                long l = System.currentTimeMillis();
                long timeDistance = l - mFirstCallTime;
                if (timeDistance > 2000) {//延迟两秒
                    mStartSaveData = true;
                }
            }
        }
    }
}
