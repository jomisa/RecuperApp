package husi.recuperapp;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by jmss1 on 22/09/2016.
 */

public class Paciente extends Application{
    private static Paciente singleton;

    static DataBaseHelper dbHelper;


    private final String URL_BASE = "http://10.0.2.2:8080/RecuperAppServer/WebServices/";

    private final String servicioListaSintomas="listasintomas";

    private Integer id;
    private String nombres;
    private String apellidos;
    private String email;
    private String usuario;
    private String contrasena;
    private boolean existeEnBd;

    public static Paciente getInstance(){
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject obj = new JSONObject();

        JSONArray jArray = new JSONArray();

        try {
            obj.put("idSintoma", "99");
            obj.put("sintoma", "sintoma prueba android");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postRequestWebService(servicioListaSintomas, queue, obj);

        getRequestWebService(servicioListaSintomas, queue);

        this.existeEnBd=false;

        dbHelper = new DataBaseHelper(this);
        crearObjetoDesdeBD(dbHelper);
        singleton = this;
    }

    public boolean crearObjetoDesdeBD(DataBaseHelper dbHelper) {
        if (this.existeEnBd == false){
            ArrayList<String> paciente = new ArrayList<String>();

            if(dbHelper.existeLaTabla("tabla_paciente")==false){

            }
            else {
                paciente = dbHelper.obtenerPaciente();
            }
            //no hay datos de paciente en la tabla
            if (paciente == null) {
                return existeEnBd;
            }

            this.id = Integer.valueOf(paciente.get(0));
            this.usuario = paciente.get(1);
            this.contrasena = paciente.get(2);
            this.email = paciente.get(3);
            this.existeEnBd = true;

            return existeEnBd;
        }
        return existeEnBd;
    }

    public boolean crearPaciente(String usuario,String contrasena1,String email){
        this.existeEnBd=false;
        dbHelper = new DataBaseHelper(this);
        if(dbHelper.insertarUnPaciente(usuario, contrasena1, email)){

            //TODO: Crea medicamentos iniciales en BD, se debe borrar estas líneas al crear web service
            dbHelper.insertarUnMedicamento("dolex", "10g", "12", "Sin Asignar", "false") ;
            dbHelper.insertarUnMedicamento("dolex forte", "20g", "24", "Sin Asignar", "false") ;
            dbHelper.insertarUnMedicamento("buscapina", "30g", "6", "Sin Asignar", "false") ;
            dbHelper.insertarUnMedicamento("mareol", "15g", "8", "Sin Asignar", "false") ;
            dbHelper.insertarUnMedicamento("advil", "20g", "48", "Sin Asignar", "false") ;
            dbHelper.insertarUnMedicamento("dolex", "18g", "12", "Sin Asignar", "false") ;

            crearObjetoDesdeBD(dbHelper);
        }
        return existeEnBd;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsuario(){
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public boolean comprobarUsuario(String usuario){
        if (this.usuario.equalsIgnoreCase(usuario))
            return true;
        return false;
    }

    public boolean comprobarContrasena(String contrasena){
        if (this.contrasena.equalsIgnoreCase(contrasena))
            return true;
        return false;
    }

    public boolean existeEnBd() {
        return existeEnBd;
    }

    //Sincronizador

    private void getRequestWebService(final String servicio, RequestQueue queue){

        JsonArrayRequest getArrayRequest = new JsonArrayRequest(Request.Method.GET, URL_BASE+servicio,null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray respuesta){
                        Log.i("Volley: ", respuesta.toString());
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.d("Error: ", error.getMessage());
            }
        });

        queue.add(getArrayRequest);
    }

    private void postRequestWebService(final String servicio, RequestQueue queue, final JSONObject obj){

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URL_BASE+servicio,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley: ", response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley ", "Error: " + error.getMessage());
            }
        });

        queue.add(postRequest);
    }


}
