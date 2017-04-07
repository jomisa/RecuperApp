package husi.recuperapp.fisiologicos;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import husi.recuperapp.R;
import husi.recuperapp.accesoDatos.Paciente;

public class Examenes extends AppCompatActivity {

    Paciente paciente;

    private List<Fisiologico> examenes;
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
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.rojoClaro, null));
            window.setNavigationBarColor(ResourcesCompat.getColor(getResources(), R.color.rojoOscuro, null));
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(
                    getString(R.string.app_name), bm, ResourcesCompat.getColor(getResources(), R.color.rojoOscuro, null));
            setTaskDescription(taskDesc);
        }

        this.listView = (ListView) findViewById(R.id.listViewFisiologicos);

        crearListaExamenes();

        this.listView.setAdapter(new AdaptadorListViewFisiologicos(this, examenes));

    }

    private void crearListaExamenes() {

        examenes = new ArrayList<>();

        examenes.add(new Fisiologico(R.drawable.ic_google_drive_file, "Número de glóbulos rojos","."));
        examenes.add(new Fisiologico(R.drawable.ic_google_drive_file, "Número de reticulocitos","."));
        examenes.add(new Fisiologico(R.drawable.ic_google_drive_file, "Número Plaquetas","."));
        examenes.add(new Fisiologico(R.drawable.ic_google_drive_file, "Número Hemoglobina","."));
        examenes.add(new Fisiologico(R.drawable.ic_google_drive_file, "Número Hematocrito","."));

    }
}
