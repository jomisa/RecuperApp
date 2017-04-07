package husi.recuperapp.fisiologicos;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import husi.recuperapp.R;

public class Liquidos extends AppCompatActivity {

    private List<Fisiologico> liquidos;
    private ListView listView;
    private TextView mTituloAlimentosRecomendadosTextView;
    private TextView mAlimentosRecomendadosTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registrar_fisiologicos);

        getSupportActionBar().hide();

        this.mTituloAlimentosRecomendadosTextView = (TextView) findViewById(R.id.recomendacionesTituloTexto);
        mTituloAlimentosRecomendadosTextView.setVisibility(View.VISIBLE);
        this.mAlimentosRecomendadosTextView = (TextView) findViewById(R.id.recomendacionesTexto);
        mAlimentosRecomendadosTextView.setVisibility(View.VISIBLE);

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

        crearListaLiquidos();

        this.listView.setAdapter(new AdaptadorListViewFisiologicos(this, liquidos));

    }

    private void crearListaLiquidos() {
        liquidos = new ArrayList<>();
        liquidos.add(new Fisiologico(R.drawable.ic_free_breakfast, "Líquidos","mL"));
    }
}
