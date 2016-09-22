package husi.recuperapp;

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

public class CrearUsuario extends Activity {

    Intent activarLogin;

    Paciente paciente;

    private DataBaseHelper dbHelper;

    //Textos en GUI
    private EditText mUsuarioView;
    private EditText mContrasena1View;
    private EditText mContrasena2View;
    private EditText mEmailView;
    private Button mCrearView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DataBaseHelper(this);

        paciente=(Paciente)getApplicationContext();

        setContentView(R.layout.activity_crear_usuario);

        //Asigna fuente personalizada al logo de RecuperApp(está en la carpeta assets)
        TextView mRecuperappText = (TextView) findViewById(R.id.recuperapp);
        Typeface font = Typeface.createFromAsset(getAssets(), "NovaRound.ttf");
        mRecuperappText.setTypeface(font);

        //Texto Logo (RecuperApp) en 2 colores
        String text = "<font color=#424242>Recuper</font><font color=#b71c1c>App</font>";
        mRecuperappText.setText(Html.fromHtml(text));

        //obtiene los textos de xml en java
        mUsuarioView = (EditText) findViewById(R.id.usuario_crear_texto);
        mContrasena1View = (EditText) findViewById(R.id.contrasena1_crear_texto);
        mContrasena2View = (EditText) findViewById(R.id.contrasena2_crear_texto);
        mEmailView = (EditText) findViewById(R.id.email_crear_texto);

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
        mEmailView.setError(null);

        // Obtener valores de los campos
        String usuario = mUsuarioView.getText().toString();
        String contrasena1 = mContrasena1View.getText().toString();
        String contrasena2 = mContrasena2View.getText().toString();
        String email = mEmailView.getText().toString();

        View focusView = null;

        if (verificarCampos(focusView, usuario, contrasena1, contrasena2, email)) {
            //focusView.requestFocus();
        } else {
            //Ingresa los datos del paciente en la BD
            if(dbHelper.insertarUnPaciente(usuario, contrasena1, email)) {
                //actualiza los datos del paciente en el objeto paciente
                if(paciente.crearObjetoDesdeBD()==true) {
                    Toast.makeText(this, "Se creó el usuario: " + paciente.getUsuario().toString(), Toast.LENGTH_LONG).show();
                    //Crea un intent
                    activarLogin = new Intent(this, Login.class);
                    //retorna al objeto paciente en el intent a la actividad LoginUI
                    activarLogin.putExtra("usuario", paciente.getUsuario());

                    //Retorna un resultado de afirmación en caso de haber un objeto paciente creado
                    setResult(Activity.RESULT_OK, activarLogin);
                    //ejecuta el metodo onDestry() de esta actividad
                    this.finish();
                }else
                    Toast.makeText(this, "Hubo un problema, vuelva a crear el usuario", Toast.LENGTH_LONG).show();
            }else
                Toast.makeText(this, "Hubo un problema, vuelva a crear el usuario", Toast.LENGTH_LONG).show();
        }
    }

    private boolean verificarCampos(View focusView, String usuario, String contrasena1,
                                    String contrasena2, String email){

        boolean cancelar = false;

        //Verifica campo usuario diligenciado
        if (TextUtils.isEmpty(usuario)){
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

        if(!TextUtils.equals(contrasena1, contrasena2)){
            mContrasena2View.setError(getString(R.string.error_contrasenas_diferentes));
            focusView = mContrasena2View;
            focusView.requestFocus();
            return true;
        }

        //Verifica campo email diligenciado
        if (TextUtils.isEmpty(email)){
            mEmailView.setError(getString(R.string.error_campo_requirido));
            focusView = mEmailView;
            focusView.requestFocus();
            return true;
        }
        else if(!Funciones.validarFormatoEmail(email)){
            mEmailView.setError(getString(R.string.error_email_invalido));
            focusView = mEmailView;
            focusView.requestFocus();
            return true;
        }

        return cancelar;
    }
}
