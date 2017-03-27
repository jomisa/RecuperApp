package husi.recuperapp.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import husi.recuperapp.accesoDatos.Paciente;
import husi.recuperapp.R;

public class CrearUsuario extends Activity {

    Intent activarLogin;

    //Textos en GUI
    private EditText mUsuarioView;
    private EditText mContrasena1View;
    private EditText mContrasena2View;
    private Button mCrearView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crear_usuario);

        //Asigna fuente personalizada al logo de RecuperApp(está en la carpeta assets)
        TextView mRecuperappText = (TextView) findViewById(R.id.recuperapp);
        Typeface font = Typeface.createFromAsset(getAssets(), "NovaRound.ttf");
        mRecuperappText.setTypeface(font);

        //Texto Logo (RecuperApp) en 2 colores
        String text = "<font color=#424242>Recuper</font><font color=#b71c1c>App</font>";
        mRecuperappText.setText(Html.fromHtml(text));

        mUsuarioView = (EditText) findViewById(R.id.usuario_crear_texto);
        mContrasena1View = (EditText) findViewById(R.id.contrasena1_crear_texto);
        mContrasena2View = (EditText) findViewById(R.id.contrasena2_crear_texto);

        mCrearView = (Button) findViewById(R.id.crear_usuario_button);
        mCrearView.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                presionoBotonCrear();
            }
        });

    }

    public void presionoBotonCrear() {

        // Resetear Errores
        mUsuarioView.setError(null);
        mContrasena1View.setError(null);
        mContrasena2View.setError(null);

        // Obtener valores de los campos
        String cedula = mUsuarioView.getText().toString();
        String contrasena1 = mContrasena1View.getText().toString();
        String contrasena2 = mContrasena2View.getText().toString();

        View focusView = null;

        if (!verificarCampos(focusView, cedula, contrasena1, contrasena2)){

            Paciente.getInstance().verificarYcrearPaciente(Integer.parseInt(cedula), contrasena1);

            //Crea un intent
            activarLogin = new Intent(this, Login.class);
            setResult(Activity.RESULT_OK, activarLogin);
            //ejecuta el metodo onDestry() de esta actividad
            this.finish();
        }
    }

    private boolean verificarCampos(View focusView, String cedula, String contrasena1,
                                    String contrasena2){

        boolean cancelar = false;

        //Verifica campo usuario diligenciado
        if (TextUtils.isEmpty(cedula)){
            mUsuarioView.setError(getString(R.string.error_campo_requirido));
            focusView = mUsuarioView;
            focusView.requestFocus();
            return true;
        }

        //Verifica campo contrasena1 diligenciado
        if (TextUtils.isEmpty(contrasena1)){
            mContrasena1View.setError(getString(R.string.error_campo_requirido));
            focusView = mContrasena1View;
            focusView.requestFocus();
            return true;
        }

        //Verifica campo contrasena2 diligenciado
        if (TextUtils.isEmpty(contrasena2)){
            mContrasena2View.setError(getString(R.string.error_campo_requirido));
            focusView = mContrasena2View;
            focusView.requestFocus();
            return true;
        }

        //Verifica que las contrasenas sean iguales
        if(!TextUtils.equals(contrasena1, contrasena2)){
            mContrasena2View.setError(getString(R.string.error_contrasenas_diferentes));
            focusView = mContrasena2View;
            focusView.requestFocus();
            return true;
        }

        //Verifica que el campo sea numerico
        if(!TextUtils.isDigitsOnly(cedula)){
            mUsuarioView.setError(getString(R.string.error_campo_numerico));
            focusView = mUsuarioView;
            focusView.requestFocus();
            return true;
        }

        return cancelar;
    }
}
