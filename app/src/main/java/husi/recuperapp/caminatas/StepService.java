/*
 *  Pedometer - Android App
 *  Copyright (C) 2009 Levente Bagi
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package husi.recuperapp.caminatas;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import husi.recuperapp.R;

/*
 * This is an example of implementing an application service that runs locally
 * in the same process as the application.  The {@link StepServiceController}
 * and {@link StepServiceBinding} classes show how to interact with the
 * service.
 *
 * <p>Notice the use of the {@link NotificationManager} when interesting things
 * happen in the service.  This is generally how background services should
 * interact with the user, rather than doing something more disruptive such as
 * calling startActivity().
 */
public class StepService extends Service {
	private static final String TAG = "pedometer.StepService";
    private SharedPreferences mSettings;
    private PedometerSettings mPedometerSettings;
    private SharedPreferences mState;
    private SharedPreferences.Editor mStateEditor;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private StepDetector mStepDetector;
    private StepDisplayer mStepDisplayer;
    private DistanceNotifier mDistanceNotifier;
    
    private PowerManager.WakeLock wakeLock;
    private NotificationManager mNotificationManager;

    private int mSteps;
    private float mDistance;
    
    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class StepBinder extends Binder {
        StepService getService() {
            return StepService.this;
        }
    }
    
    @Override
    public void onCreate() {
        Log.i(TAG, "[SERVICE] onCreate");
        super.onCreate();

        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //showNotification();
        
        // Load settings
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mPedometerSettings = new PedometerSettings(mSettings);
        mState = getSharedPreferences("state", 0);

        //Hace funcionar app de acuerdo al Operational Level
        acquireWakeLock();
        
        // Empieza a detectar, utilizando el accelerometro
        mStepDetector = new StepDetector();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        registerDetector();

        //Registra un receiver para la acción ACTION_SCREEN_OFF. esto ejecutara el código
        //cuando el teléfono entre en modo standby
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);

        mStepDisplayer = new StepDisplayer(mPedometerSettings);
        mStepDisplayer.setSteps(mSteps = mState.getInt("steps", 0));
        mStepDisplayer.addListener(mStepListener);
        mStepDetector.addStepListener(mStepDisplayer);

        mDistanceNotifier = new DistanceNotifier(mDistanceListener, mPedometerSettings);
        mDistanceNotifier.setDistance(mDistance = mState.getFloat("distance", 0));
        mStepDetector.addStepListener(mDistanceNotifier);

        //restaura los ajustes
        reloadSettings();

        //Muestra al usuario un mensaje de comienzo
        Toast.makeText(this, getText(R.string.started), Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onStart(Intent intent, int startId) {
        Log.i(TAG, "[SERVICE] onStart");
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "[SERVICE] onDestroy");

        //Se desregistra el receiber
        unregisterReceiver(mReceiver);
        unregisterDetector();
        
        mStateEditor = mState.edit();
        mStateEditor.putInt("steps", mSteps);
        mStateEditor.putFloat("distance", mDistance);
        mStateEditor.commit();

        //se cancela la notificación
        mNotificationManager.cancel(R.string.app_name);

        wakeLock.release();
        
        super.onDestroy();
        
        //Se desregistra el accelerometro
        mSensorManager.unregisterListener(mStepDetector);

        //Muestra al usuario un mensaje de terminar
        Toast.makeText(this, getText(R.string.stopped), Toast.LENGTH_SHORT).show();
    }

    private void registerDetector() {
        mSensor = mSensorManager.getDefaultSensor(
            Sensor.TYPE_ACCELEROMETER );
        mSensorManager.registerListener(mStepDetector,
            mSensor,
            SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void unregisterDetector() {
        mSensorManager.unregisterListener(mStepDetector);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "[SERVICE] onBind");
        return mBinder;
    }

    /**
     * Receives messages from activity.
     */
    private final IBinder mBinder = new StepBinder();

    public interface ICallback {
        void stepsChanged(int value);
        void distanceChanged(float value);
    }
    
    private ICallback mCallback;

    public void registerCallback(ICallback cb) {
        mCallback = cb;
    }
    
    public void reloadSettings() {
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        
        if (mStepDetector != null) { 
            mStepDetector.setSensitivity(
                    Float.valueOf(mSettings.getString("sensitivity", "10"))
            );
        }
        
        if (mStepDisplayer    != null) mStepDisplayer.reloadSettings();
        if (mDistanceNotifier != null) mDistanceNotifier.reloadSettings();
    }
    
    public void resetValues() {
        mStepDisplayer.setSteps(0);
        mDistanceNotifier.setDistance(0);
    }
    
    /**
     * Forwards pace values from paceNotifier to the activity.
     */
    private StepDisplayer.Listener mStepListener = new StepDisplayer.Listener() {
        public void stepsChanged(int value) {
            mSteps = value;
            passValue();
        }
        public void passValue() {
            if (mCallback != null) {
                mCallback.stepsChanged(mSteps);
            }
        }
    };
    /**
     * Forwards distance values from DistanceNotifier to the activity. 
     */
    private DistanceNotifier.Listener mDistanceListener = new DistanceNotifier.Listener() {
        public void valueChanged(float value) {
            mDistance = value;
            passValue();
        }
        public void passValue() {
            if (mCallback != null) {
                mCallback.distanceChanged(mDistance);
            }
        }
    };

    //TODO: Poner notificación, está funcionando
    /**
     * Show a notification while this service is running.
     */

    private void showNotification() {

        Intent pedometerIntent = new Intent();
        pedometerIntent.setComponent(new ComponentName(this, Pedometer.class));
        pedometerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                pedometerIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_maps_directions_walk);
        builder.setContentTitle("Caminata en curso");
        builder.setContentIntent(contentIntent);

        Notification notification = builder.getNotification();
        mNotificationManager.notify(R.drawable.ic_action_alarm, notification);
    }


    // BroadcastReceiver for handling ACTION_SCREEN_OFF.
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Check action just to be on the safe side.
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                // Unregisters the listener and registers it again.
                StepService.this.unregisterDetector();
                StepService.this.registerDetector();
                if (mPedometerSettings.wakeAggressively()) {
                    wakeLock.release();
                    acquireWakeLock();
                }
            }
        }
    };

    private void acquireWakeLock() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        int wakeFlags;
        if (mPedometerSettings.wakeAggressively()) {
            wakeFlags = PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP;
        }
        else if (mPedometerSettings.keepScreenOn()) {
            wakeFlags = PowerManager.SCREEN_DIM_WAKE_LOCK;
        }
        else {
            wakeFlags = PowerManager.PARTIAL_WAKE_LOCK;
        }
        wakeLock = pm.newWakeLock(wakeFlags, TAG);
        wakeLock.acquire();
    }

}

