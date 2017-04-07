package husi.recuperapp.fisiologicos;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import husi.recuperapp.accesoDatos.Paciente;
import husi.recuperapp.R;

public class RegistrarFisiologicos extends AppCompatActivity {

    Paciente paciente;

    private List<Fisiologico> fisiologicos;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paciente=(Paciente)getApplicationContext();

        setContentView(R.layout.activity_registrar_fisiologicos);

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.RED);
            window.setNavigationBarColor(Color.RED);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(
                    getString(R.string.app_name), bm, Color.RED);
            setTaskDescription(taskDesc);
        }

        this.listView = (ListView) findViewById(R.id.listViewFisiologicos);

        crearListaFisiologicos();

        this.listView.setAdapter(new AdaptadorListViewFisiologicos(this, fisiologicos));

    }

    private void crearListaFisiologicos() {

        fisiologicos = new ArrayList<>();

        fisiologicos.add(new Fisiologico(R.drawable.ic_scale_bathroom, "Peso",
                "Kg"));
        fisiologicos.add(new Fisiologico(R.drawable.ic_heart_pulse, "Frecuencia Cardíaca",
                "bpm"));
        fisiologicos.add(new Fisiologico(R.drawable.ic_blood_pressure, "Presión Sanguínea SYS",
                "mmHg"));
        fisiologicos.add(new Fisiologico(R.drawable.ic_blood_pressure, "Presión Sanguínea DIA",
                "mmHg"));
        fisiologicos.add(new Fisiologico(R.drawable.ic_glucose_monitor, "Niveles de Glucosa",
                "mg"));

    }

}
