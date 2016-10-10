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


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import husi.recuperapp.DataBaseHelper;
import husi.recuperapp.Funciones;
import husi.recuperapp.R;


public class Pedometer extends Activity {
	private static final String TAG = "Pedometer";
    private SharedPreferences mSettings;
    private PedometerSettings mPedometerSettings;

    private StepService mService;

    private TextView mStepValueView;
    private TextView mDistanceValueView;
    private TextView mTiempoValorView;

    private int mStepValue;
    private long tiempoInicio;
    private long tiempoFin;
    private double duracion;
    private float mDistanceValue;
    private boolean mQuitting = false; // Set when user selected Quit from menu, can be used by onPause, onStop, onDestroy

    private Button mBotonEmpezar;
    private Button mBotonTerminar;
    private Button mBotonFinalizar;
//    private Button mBotonSettings;

    private static DataBaseHelper dbHelper;

    /**
     * True, when service is running.
     */
    private boolean mIsRunning;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "[ACTIVITY] onCreate");
        super.onCreate(savedInstanceState);

        mStepValue = 0;

        setContentView(R.layout.activity_pedometer);
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "[ACTIVITY] onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "[ACTIVITY] onResume");
        super.onResume();

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mPedometerSettings = new PedometerSettings(mSettings);

        // Read from preferences if the service was running on the last onPause
        mIsRunning = mPedometerSettings.isServiceRunning();

        mBotonEmpezar = (Button) findViewById(R.id.empezar_caminata_button);
        mBotonTerminar = (Button) findViewById(R.id.terminar_caminata_button);
        mBotonFinalizar = (Button) findViewById(R.id.finalizar_caminata_button);
        //mBotonSettings = (Button) findViewById(R.id.settings_boton);

        mStepValueView     = (TextView) findViewById(R.id.pasos_dato_texto);
        mDistanceValueView = (TextView) findViewById(R.id.distancia_dato_texto);
        mTiempoValorView = (TextView) findViewById(R.id.tiempo_dato_texto);

        if (mIsRunning) {//Si estácmainando desde antes
            mBotonTerminar.setVisibility(View.VISIBLE);
            bindStepService();
        }else{//en caso de abrir caminata por primera vez
            mBotonTerminar.setVisibility(View.GONE);
            mBotonFinalizar.setVisibility(View.GONE);
 //           mBotonSettings.setVisibility(View.VISIBLE);
            mTiempoValorView.setText("");
        }

        mPedometerSettings.clearServiceRunning();

        mBotonEmpezar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!mIsRunning && mPedometerSettings.isNewStart()) {
                    startStepService();
                    bindStepService();
                }

                tiempoInicio = System.currentTimeMillis();

                mBotonEmpezar.setVisibility(View.GONE);
                mBotonTerminar.setVisibility(View.VISIBLE);
            }
        });

        mBotonTerminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //resetValues(false);
                unbindStepService();
                stopStepService();
                mQuitting = true;

                mTiempoValorView.setText(""+ (int) duracion);

                //TODO: Perisistir resultados
                Log.i("Pasos Totales: ",""+mStepValue);
                Log.i("Distancia Total: ",""+mDistanceValue);
                Log.i("Tiempo Total: ",""+(int) duracion);

                dbHelper = new DataBaseHelper(getApplicationContext());
                dbHelper.insertarUnaCaminata(Funciones.getFechaString(), String.valueOf((int) duracion)
                        , mDistanceValue+"", mStepValue+"");

                mBotonTerminar.setVisibility(View.GONE);
                mBotonFinalizar.setVisibility(View.VISIBLE);
            }
        });

        mBotonFinalizar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetValues(true);
                finish();
            }
        });
        /*
        mBotonSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent settingsActivity = new Intent(getApplicationContext(), Settings.class);
                startActivity(settingsActivity);
            }
        });
        */
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "[ACTIVITY] onPause");
        if (mIsRunning) {
            unbindStepService();
        }
        if (mQuitting) {
            mPedometerSettings.saveServiceRunningWithNullTimestamp(mIsRunning);
        }
        else {
            mPedometerSettings.saveServiceRunningWithTimestamp(mIsRunning);
        }

        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "[ACTIVITY] onStop");
        super.onStop();
    }

    protected void onDestroy() {
        Log.i(TAG, "[ACTIVITY] onDestroy");
        super.onDestroy();
    }

    protected void onRestart() {
        Log.i(TAG, "[ACTIVITY] onRestart");
        super.onDestroy();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = ((StepService.StepBinder)service).getService();

            mService.registerCallback(mCallback);
            mService.reloadSettings();

        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    private void startStepService() {
        if (! mIsRunning) {
            Log.i(TAG, "[SERVICE] Start");
            mIsRunning = true;
            startService(new Intent(Pedometer.this,
                    StepService.class));
        }
    }

    private void bindStepService() {
        Log.i(TAG, "[SERVICE] Bind");
        bindService(new Intent(Pedometer.this,
                StepService.class), mConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    private void unbindStepService() {
        Log.i(TAG, "[SERVICE] Unbind");
        unbindService(mConnection);
    }

    private void stopStepService() {
        Log.i(TAG, "[SERVICE] Stop");
        if (mService != null) {
            Log.i(TAG, "[SERVICE] stopService");
            stopService(new Intent(Pedometer.this,
                  StepService.class));
        }
        mIsRunning = false;
    }

    private void resetValues(boolean updateDisplay) {
        if (mService != null && mIsRunning) {
            mService.resetValues();
        }
        else {
            mStepValueView.setText("0");
            mDistanceValueView.setText("0");
            mTiempoValorView.setText("0");
            SharedPreferences state = getSharedPreferences("state", 0);
            SharedPreferences.Editor stateEditor = state.edit();
            if (updateDisplay) {
                stateEditor.putInt("steps", 0);
                stateEditor.putFloat("distance", 0);
                stateEditor.commit();
            }
        }
    }

    // TODO: unite all into 1 type of message
    private StepService.ICallback mCallback = new StepService.ICallback() {
        public void stepsChanged(int value) {
            mHandler.sendMessage(mHandler.obtainMessage(STEPS_MSG, value, 0));
        }
        public void distanceChanged(float value) {
            mHandler.sendMessage(mHandler.obtainMessage(DISTANCE_MSG, (int)(value*1000), 0));
        }
    };

    private static final int STEPS_MSG = 1;
    private static final int DISTANCE_MSG = 3;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STEPS_MSG:
                    mStepValue = (int)msg.arg1;
                    mStepValueView.setText("" + mStepValue);
                    tiempoFin = System.currentTimeMillis();
                    duracion = (tiempoFin - tiempoInicio) / 1000.0;
                    mTiempoValorView.setText(""+(int) duracion);
                    break;
                case DISTANCE_MSG:
                    mDistanceValue = ((int)msg.arg1)/1000f;
                    if (mDistanceValue <= 0) {
                        mDistanceValueView.setText("0");
                        mTiempoValorView.setText("0");
                    }
                    else {
                        //mDistanceValueView.setText(("" + (mDistanceValue + 0.000001f)).substring(0, 5));
                        mDistanceValueView.setText("" + mDistanceValue );
                        tiempoFin = System.currentTimeMillis();
                        duracion = (tiempoFin - tiempoInicio) / 1000.0;
                        mTiempoValorView.setText(""+(int) duracion);
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }

    };


}