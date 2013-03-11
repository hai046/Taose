package com.buy.games.dice;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MySensorManager {

    private final SensorManager mSensorManager;
    private static MySensorManager mMySensorManager;

    public static synchronized MySensorManager getInstance(Context mContext) {
        if (mMySensorManager == null)
            mMySensorManager = new MySensorManager( mContext );
        return mMySensorManager;
    }

    private float alpha = 0.8f;
    private float gravity[] = new float[3];
    private float linear_acceleration[] = new float[3];
    private SensorEventListener mEventListener = new SensorEventListener( ) {

        public void onSensorChanged(SensorEvent event) {
            if (mAccelerometerListener != null) {

                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

                linear_acceleration[0] = event.values[0] - gravity[0];
                linear_acceleration[1] = event.values[1] - gravity[1];
                linear_acceleration[2] = event.values[2] - gravity[2];

                mAccelerometerListener.callBack( linear_acceleration[0], linear_acceleration[1], linear_acceleration[2] );
            }

        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private AccelerometerListener mAccelerometerListener;

    public void setAccelerometerListener(AccelerometerListener mAccelerometerListener) {
        this.mAccelerometerListener = mAccelerometerListener;
        mSensorManager.registerListener( mEventListener, mSensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER ), SensorManager.SENSOR_DELAY_FASTEST );
    }

    public interface AccelerometerListener {
        void callBack(float x, float y, float z);
    }

    private MySensorManager(Context mContext) {
        mSensorManager = (SensorManager) mContext.getSystemService( Context.SENSOR_SERVICE );
        
    }

    public void removeListener() {
        mSensorManager.unregisterListener( mEventListener );
    }
}
