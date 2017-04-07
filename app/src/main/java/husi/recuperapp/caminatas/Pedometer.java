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

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import husi.recuperapp.accesoDatos.Funciones;
import husi.recuperapp.accesoDatos.Paciente;
import husi.recuperapp.R;

public class Pedometer extends AppCompatActivity {
	private static final String TAG = "Pedometer";

    private SharedPreferences mSettings;
    private PedometerSettings mPedometerSettings;

    private StepService mService;

    Thread hiloTiempo;

    private TextView mValorPasoView;
    private TextView mValorDistanciaView;
    private TextView mValorTiempoView;

    private long tiempoInicio;
    private long tiempoFin;
    private double duracion;
    private int valorPaso;
    private float valorDistancia;
    private boolean quitando = false; // Set when user selected Quit from menu, can be used by onPause, onStop, onDestroy

    private Button mBotonEmpezar;
    private Button mBotonTerminar;
    private Button mBotonFinalizar;

    /**
     * True, when service is running.
     */
    private boolean mIsRunning;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "[ACTIVITY] onCreate");
        super.onCreate(savedInstanceState);

        valorPaso = 0;

        setContentView(R.layout.activity_pedometer);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.rojoClaro, null));
            window.setNavigationBarColor(ResourcesCompat.getColor(getResources(), R.color.rojoOscuro, null));
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(
                    getString(R.string.app_name), bm, ResourcesCompat.getColor(getResources(), R.color.rojoOscuro, null));
            setTaskDescription(taskDesc);
        }
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

        mValorPasoView     = (TextView) findViewById(R.id.pasos_dato_texto);
        mValorDistanciaView = (TextView) findViewById(R.id.distancia_dato_texto);
        mValorTiempoView = (TextView) findViewById(R.id.tiempo_dato_texto);

        if (mIsRunning) {//Si está caminando desde antes
            mBotonTerminar.setVisibility(View.VISIBLE);
            bindStepService();
        }else{//en caso de abrir caminata por primera vez
            mBotonTerminar.setVisibility(View.GONE);
            mBotonFinalizar.setVisibility(View.GONE);
            mValorTiempoView.setText("0");
        }

        mPedometerSettings.clearServiceRunning();

        mBotonEmpezar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!mIsRunning && mPedometerSettings.isNewStart()) {
                    startStepService();
                    bindStepService();
                }

                tiempoInicio = System.currentTimeMillis();

                //Este Hilo actualiza el mTiempoValorView cada cierto tiempo
                hiloTiempo = new Thread() {
                    @Override
                    public void run() {
                        try {
                            while (!isInterrupted()) {
                                Thread.sleep(1000);//Actualiza cada segundo
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tiempoFin = System.currentTimeMillis();
                                        duracion = (tiempoFin - tiempoInicio) / 1000.0;

                                        //TODO si está en modo prueba de los 6 minutos, debe
                                        //notificar a los 6 minutos

                                        mValorTiempoView.setText(""+ (int) duracion);
                                    }
                                });
                            }
                        } catch (InterruptedException e) {
                        }
                    }
                };
                hiloTiempo.start();

                mBotonEmpezar.setVisibility(View.GONE);
                mBotonTerminar.setVisibility(View.VISIBLE);
            }
        });

        mBotonTerminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //resetValues(false);
                unbindStepService();
                stopStepService();
                quitando = true;

                hiloTiempo.interrupt();

                Log.i("Pasos Totales: ",""+valorPaso);
                Log.i("Distancia Total: ",""+valorDistancia);
                Log.i("Tiempo Total: ",""+(int) duracion);

                Paciente.getInstance().insertarYpostCaminatas(Funciones.getFechaString(),
                        (int) duracion , (int) valorDistancia, valorPaso, 1);//TODO Poner sintoma si obtubo alguno

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

    }

    @Override
    protected void onPause() {
        Log.i(TAG, "[ACTIVITY] onPause");
        if (mIsRunning) {
            unbindStepService();
        }
        if (quitando) {
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
        super.onRestart();
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
            mValorPasoView.setText("0");
            mValorDistanciaView.setText("0");
            mValorTiempoView.setText("0");
            SharedPreferences state = getSharedPreferences("state", 0);
            SharedPreferences.Editor stateEditor = state.edit();
            if (updateDisplay) {
                stateEditor.putInt("steps", 0);
                stateEditor.putFloat("distance", 0);
                stateEditor.commit();
            }
        }
    }

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
                    valorPaso = (int)msg.arg1;
                    mValorPasoView.setText("" + valorPaso);
                    break;
                case DISTANCE_MSG:
                    valorDistancia = ((int)msg.arg1)/1000f;
                    if (valorDistancia <= 0) {
                        mValorDistanciaView.setText("0");
                    }
                    else {
                        mValorDistanciaView.setText("" + valorDistancia );
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }

    };


}