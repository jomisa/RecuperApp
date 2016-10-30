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

import husi.recuperapp.menus.MenuPrincipal;
import husi.recuperapp.accesoDatos.Paciente;
import husi.recuperapp.R;

/**
 * Login
 */
public class Login extends Activity{

    //Intenst para activar actividades crear usuario y menu
    Intent activarCrearUsuario;
    Intent activarMenu;
    static private final int REQUEST_CODE = 1;

    // Referencias de UI.
    private EditText mRecuperappText;
    private EditText mUsuarioView;
    private EditText mContrasenaView;
    private Button mCrearUsuarioView;
    private Button mIngresarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Asigna fuente personalizada al logo de RecuperApp(está en la carpeta assets)
        TextView mRecuperappText = (TextView) findViewById(R.id.recuperapp);
        Typeface font = Typeface.createFromAsset(getAssets(), "NovaRound.ttf");
        mRecuperappText.setTypeface(font);

        //Texto Logo (RecuperApp) en 2 colores
        String text = "<font color=#424242>Recuper</font><font color=#b71c1c>App</font>";
        mRecuperappText.setText(Html.fromHtml(text));

        mUsuarioView = (EditText) findViewById(R.id.usuario_texto);
        mContrasenaView = (EditText) findViewById(R.id.password);

        mCrearUsuarioView = (Button) findViewById(R.id.crear_usuario_button);
        mCrearUsuarioView.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                presionoBotonCrearUsuario();
            }
        });

        mIngresarView = (Button) findViewById(R.id.email_sign_in_button);
        mIngresarView.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                intentarIngresar();
            }
        });
    }

    private void intentarIngresar() {

        // Resetear Errores
        mUsuarioView.setError(null);
        mContrasenaView.setError(null);

        // Obtener valores de los campos
        String cedula = mUsuarioView.getText().toString();
        String contrasena = mContrasenaView.getText().toString();

        boolean cancelar = false;
        View focusView = null;

        if(comprobarUsuarioExistente()==false) {
            Toast.makeText(this, "Por favor cree un usuario", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(cedula)) {
            mUsuarioView.setError(getString(R.string.error_campo_requirido));
            focusView = mUsuarioView;
            focusView.requestFocus();
            cancelar = true;
        }

        if (TextUtils.isEmpty(contrasena)) {
            mContrasenaView.setError(getString(R.string.error_campo_requirido));
            focusView = mContrasenaView;
            focusView.requestFocus();
            cancelar = true;
        }

        //si hubo algun error
        if (cancelar) {

        } else {

            if(validarUsuario(cedula.trim(), contrasena)==true){
                activarMenu = new Intent(this, MenuPrincipal.class);
                activarMenu.putExtra("usuario", Paciente.getInstance().getNombresApellidos());
                startActivity(activarMenu);
            }
            else{
                Toast.makeText(this, "Los datos no son correctos", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validarUsuario(String usuario, String contrasena){
        if(Paciente.getInstance().comprobarContrasena(contrasena) && Paciente.getInstance().comprobarCedula(usuario))
            return true;
        return false;
    }

    //crear usuario retorna nombre del usuario, y se autoingresa en el campo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent respuesta){
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK)
                mUsuarioView.setText(respuesta.getStringExtra("usuario"));
        }
    }

    private void presionoBotonCrearUsuario(){
        if(comprobarUsuarioExistente()) {
            Toast.makeText(this, "Ya hay un usuario creado", Toast.LENGTH_LONG).show();
        }
        else {
            activarCrearUsuario = new Intent(Login.this, CrearUsuario.class);
            startActivityForResult(activarCrearUsuario,REQUEST_CODE);
        }
    }

    private boolean comprobarUsuarioExistente(){
        return Paciente.getInstance().existePaciente();
    }
}

