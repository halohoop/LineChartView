package com.halohoop.linechartviewdemo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.halohoop.linechartview.widgets.LineChartView;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener,SensorEventListener {

    private TextView mTv;
    private LineChartView mLcv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLcv = (LineChartView) findViewById(R.id.lcv);
        mTv = (TextView) findViewById(R.id.tv);
        mTv.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor defaultSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, defaultSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    int value = 0;

    @Override
    public void onClick(View v) {
        mLcv.setStartValue(value++);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        mLcv.setStartValue(values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
