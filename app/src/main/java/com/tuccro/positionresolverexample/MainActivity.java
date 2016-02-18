package com.tuccro.positionresolverexample;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;

    private TextView orientation;
    private TextView position;

    enum ScreenOrientation {
        DEG_0, DEG_90, DEG_180, DEG_270
    }

    enum ScreenPosition {
        UP, DOWN
    }

    ScreenOrientation currentScreenOrientation;
    ScreenPosition currentScreenPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        orientation = (TextView) findViewById(R.id.orientation);
        position = (TextView) findViewById(R.id.position);
    }

    private void initSensorManager() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    private void publishResult() {
        orientation.setText(currentScreenOrientation.toString());
        position.setText(currentScreenPosition.toString());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float[] values = event.values;

            // Movement
            float x = values[0];
            float y = values[1];
            float z = values[2];

            if (Math.abs(y) > Math.abs(x)) {
                currentScreenOrientation = y > 0 ? ScreenOrientation.DEG_0 : ScreenOrientation.DEG_180;
            } else {
                currentScreenOrientation = x > 0 ? ScreenOrientation.DEG_90 : ScreenOrientation.DEG_270;
            }

            currentScreenPosition = z > 0 ? ScreenPosition.UP : ScreenPosition.DOWN;
            publishResult();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        if (sensorManager == null) initSensorManager();

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        if (sensorManager != null) sensorManager.unregisterListener(this);
    }
}
