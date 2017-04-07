package husi.recuperapp.accesoDatos;

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

import java.util.List;

import husi.recuperapp.caminatas.AlarmaCaminatasReceiver;

/**
 * Created by jmss1 on 22/09/2016.
 */

public class Paciente extends Application{


    private final String URL_SERVIDOR = "http://10.0.2.2:8080/RecuperAppServer/WebServices/";
    //private final String URL_SERVIDOR = "http://192.168.0.7:8080/RecuperAppServer/WebServices/";

    private static Paciente singleton;
    static DataBaseHelper dbHelper;
    private RequestQueue colaRequest;

    public static Paciente getInstance(){
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        colaRequest = Volley.newRequestQueue(this);
        dbHelper = new DataBaseHelper(this);

        sincronizarBD();

        singleton = this;
    }

    //Info paciente

    public boolean existePaciente(){
        if (dbHelper.obtenerPaciente()!=null)
            return true;
        return false;
    }

    public boolean comprobarCedula(String cedula){
        if (dbHelper.obtenerPaciente().get(0).equalsIgnoreCase(cedula))
            return true;
        return false;
    }

    public boolean comprobarContrasena(String contrasena){
        if (dbHelper.obtenerPaciente().get(1).equalsIgnoreCase(contrasena))
            return true;
        return false;
    }

    public String getNombresApellidos(){
        return dbHelper.obtenerPaciente().get(2)+" "+dbHelper.obtenerPaciente().get(3);
    }

    public int getCedula(){
        return Integer.parseInt(dbHelper.obtenerPaciente().get(0));
    }


    //Sincroniza BD interna con BD servidor
    public void sincronizarBD(){
        if(existePaciente()==true) {
            getMisMedicamentosDelServidor();
            getSintomasDelServidor();
            postFisiologicos();
            postSintomas();
            postCaminatas();
            postAnimos();
            //TODO llenar tablas de sintomas medicamentos y sintomas caminatas
        }
    }

    //Acceso BD
    public List<List<String>> obtenerMedicamentosBD(){
        return dbHelper.obtenerMedicamentos();
    }

    public List<List<Object>> obtenerListaSintomasBD() {
        return dbHelper.obtenerListaSintomas();
    }

    public List<Object> buscarMedicamento(int idMedicamento){
        return dbHelper.obtenerUnMedicamento(idMedicamento);
    }

    public void actualizarMedicamentoBD(String id, long hora, String asignado){
        dbHelper.actualizarHoraConsumoMedicamento( id, hora, asignado);
    }

    public void insertarCitaBD(long fecha, String medico) {
        dbHelper.insertarUnaCita(fecha, medico);
    }

    public List<List<String>> obtenerCitasBD() {
        return dbHelper.obtenerCitas();
    }

    public List<String> buscarCitaBD(long fecha, String medico ){
        return dbHelper.buscarCita(fecha, medico);
    }

    public List<String> buscarCitaIdBD(int idCita) {
        return dbHelper.buscarCitaId(idCita);
    }

    public boolean eliminarCitaBD(int idCita) {
        return dbHelper.borrarUnaCita(String.valueOf(idCita));
    }

    public void insertarUnSintomaBD(int idSintoma, int cedula, String fechaString) {
        dbHelper.insertarUnSintoma(cedula, idSintoma, fechaString);
    }

    //Acceso BD + WEB SERVICES
    public void verificarYcrearPaciente(int cedula, String contrasena) {
        JsonObjectRequest getVerificarPacienteRequest = new JsonObjectRequest(Request.Method.GET,
                URL_SERVIDOR+"pacientes/findPaciente/"+cedula+"/"+contrasena,null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Volley findPaciente ", response.toString());
                        if(response != null && dbHelper.obtenerPaciente() == null){
                            try {
                                dbHelper.insertarUnPaciente(response.getInt("cedula"),
                                        response.getString("contrasena"), response.getString("nombres"),
                                        response.getString("apellidos"), response.getString("correo"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d("getVerificarPacienteRequest: ", error.getMessage());
            }
        });

        if(getVerificarPacienteRequest!=null) {
            getVerificarPacienteRequest.setShouldCache(false);
            colaRequest.add(getVerificarPacienteRequest);

            sincronizarBD();
        }

    }

    public void insertarYpostFisiologicos(String fecha,String medicion,double valor){

        dbHelper.insertarUnFisiologico(getCedula(), fecha, medicion, valor);

        postFisiologicos();
    }

    public void postFisiologicos(){

        JsonObjectRequest postRequest=null;
        List<List<Object>> fisiologicosBD;
        List<Object> fisiologico;

        fisiologicosBD = dbHelper.obtenerFisiologicos();

        if(fisiologicosBD!=null){
            for (int i = 0; i < fisiologicosBD.size(); i++){
                fisiologico=fisiologicosBD.get(i);

                //Dato del Valor (fila 6), si es 0 no se a enviado
                if(fisiologico.get(5).toString().equals("0")) {

                    final JSONObject fisologicoJson = new JSONObject();
                    try {
                        fisologicoJson.put("id", fisiologico.get(0).toString());
                        fisologicoJson.put("cedula", fisiologico.get(1).toString());
                        fisologicoJson.put("fecha", fisiologico.get(2).toString());
                        fisologicoJson.put("medicion", fisiologico.get(3).toString());
                        fisologicoJson.put("valor", fisiologico.get(4).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    postRequest = new JsonObjectRequest(Request.Method.POST, URL_SERVIDOR + "fisiologicos", fisologicoJson,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("Volley Fisiologicos: ", response.toString());
                                    try {//La fecha es un identificador unico e igual tanto en el servido como en el app
                                        dbHelper.actualizarEnviadoFisiologico(response.getString("fecha"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if(error.networkResponse != null && error.networkResponse.data != null){
                                String errorString = new String(error.networkResponse.data);
                                VolleyLog.d("Fisiologico:", errorString);
                            } else if(error.networkResponse != null) {
                                VolleyLog.d("Fisiologico Network: ", error.networkResponse.statusCode);
                            }else{
                                VolleyLog.d("Fisiologico: ", error.getMessage());
                            }
                        }
                    });
                }
                if(postRequest!=null) {
                    postRequest.setShouldCache(false);
                    colaRequest.add(postRequest);
                }
            }
        }
    }

    public void insertarYpostAnimos(String fecha, float valor){

        dbHelper.insertarUnAnimo(getCedula(), fecha, valor);

        postAnimos();
    }

    public void postAnimos(){

        JsonObjectRequest postRequest=null;
        List<List<Object>> animosBD;
        List<Object> animo;

        animosBD = dbHelper.obtenerAnimos();

        if(animosBD!=null){
            for (int i = 0; i < animosBD.size(); i++){
                animo=animosBD.get(i);

                //Dato del Valor (fila 5), si es 0 no se a enviado
                if(animo.get(4).toString().equals("0")) {

                    final JSONObject animoJson = new JSONObject();
                    try {
                        animoJson.put("id", animo.get(0).toString());
                        animoJson.put("cedula", animo.get(1).toString());
                        animoJson.put("fecha", animo.get(2).toString());
                        animoJson.put("valor", animo.get(3).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    postRequest = new JsonObjectRequest(Request.Method.POST, URL_SERVIDOR + "estadosAnimo", animoJson,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("Volley estadosAnimo: ", response.toString());
                                    try {//La fecha es un identificador unico e igual tanto en el servido como en el app
                                        dbHelper.actualizarEnviadoAnimo(response.getString("fecha"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if(error.networkResponse != null && error.networkResponse.data != null){
                                String errorString = new String(error.networkResponse.data);
                                VolleyLog.d("estadosAnimo:", errorString);
                            } else if(error.networkResponse != null) {
                                VolleyLog.d("estadosAnimo Network: ", error.networkResponse.statusCode);
                            }else{
                                VolleyLog.d("estadosAnimo: ", error.getMessage());
                            }
                        }
                    });
                }
                if(postRequest!=null) {
                    postRequest.setShouldCache(false);
                    colaRequest.add(postRequest);
                }
            }
        }
    }

    public void insertarYpostCaminatas(String fecha, int tiempo, int distancia, int pasos, int idSintomaCaminata){

        dbHelper.insertarUnaCaminata(getCedula(), fecha, tiempo, distancia, pasos,  idSintomaCaminata);

        postCaminatas();
    }

    public void postCaminatas(){
        JsonObjectRequest postRequest=null;
        List<List<Object>> caminatasBD;
        List<Object> caminata;

        caminatasBD = dbHelper.obtenerCaminatas();

        if(caminatasBD!=null){
            for (int i = 0; i < caminatasBD.size(); i++){
                caminata=caminatasBD.get(i);

                //Dato del Valor (fila 8), si es 0 no se a enviado
                if(caminata.get(7).toString().equals("0")) {

                    final JSONObject caminataJson = new JSONObject();
                    try {
                        caminataJson.put("id", caminata.get(0).toString());
                        caminataJson.put("cedula", caminata.get(1).toString());
                        caminataJson.put("fecha", caminata.get(2).toString());
                        caminataJson.put("tiempo", caminata.get(3).toString());
                        caminataJson.put("distancia", caminata.get(4).toString());
                        caminataJson.put("pasos", caminata.get(5).toString());
                        caminataJson.put("idSintoma", caminata.get(6).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    postRequest = new JsonObjectRequest(Request.Method.POST, URL_SERVIDOR + "caminatas", caminataJson,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("Volley Caminatas: ", response.toString());
                                    try {//La fecha es un identificador unico e igual tanto en el servido como en el app
                                        dbHelper.actualizarEnviadoCaminata(response.getString("fecha"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if(error.networkResponse != null && error.networkResponse.data != null){
                                String errorString = new String(error.networkResponse.data);
                                VolleyLog.d("Caminata:", errorString);
                            } else if(error.networkResponse != null) {
                                VolleyLog.d("Caminata Network: ", error.networkResponse.statusCode);
                            }else{
                                VolleyLog.d("Caminata: ", error.getMessage());
                            }
                        }
                    });
                }
                if(postRequest!=null) {
                    postRequest.setShouldCache(false);
                    colaRequest.add(postRequest);
                }
            }
        }
    }

    public void getMisMedicamentosDelServidor(){

        JsonArrayRequest getMisMedicamentosRequest = new JsonArrayRequest(Request.Method.GET,
                URL_SERVIDOR+"medicamentos/misMedicamentos/"+getCedula(),null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Volley misMedicamentos ", response.toString());

                        JSONObject jObjectResponse;
                        for(int i=0;i<response.length();i++){

                            try {
                                jObjectResponse = response.getJSONObject(i);
                                Log.i("jObjectResponse ", jObjectResponse.toString());
                                int id = jObjectResponse.getInt("idMed");
                                String medicamento = jObjectResponse.getString("nombreMed");
                                String dosis = jObjectResponse.getString("dosis");
                                int frecuencia = jObjectResponse.getInt("frecuencia");
                                //String hora = jObjectResponse.getString("hora"); //TODO: problema dato Date en servidor
                                String sintoma = jObjectResponse.getString("sintoma");
                                int asignado = jObjectResponse.getInt("asignado");

                                //Si no exite el medicamento no lo inserta, los id de los medicamentos
                                //no son autonumerados, el id es el mismo de la tabla del servidor
                                //esto para evitar agregar medicamentos existentes a la tabla (evitar repetidos)
                                if(dbHelper.buscarMedicamento(id+"")==false)
                                    dbHelper.insertarUnMedicamento(id+"", medicamento, dosis, frecuencia+"", 0, sintoma,asignado+"");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d("misMedicamentos: ", error.getMessage());
            }
        });
        if(getMisMedicamentosRequest!=null) {
            getMisMedicamentosRequest.setShouldCache(false);
            colaRequest.add(getMisMedicamentosRequest);
        }
    }

    public void getSintomasDelServidor(){

        JsonArrayRequest getSintomasRequest = new JsonArrayRequest(Request.Method.GET,
                URL_SERVIDOR+"listasintomas",null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Volley listaSintomas ", response.toString());

                        JSONObject jObjectResponse;
                        for(int i=0;i<response.length();i++){
                            try {
                                jObjectResponse = response.getJSONObject(i);
                                Log.i("jObjectResponse ", jObjectResponse.toString());
                                int id = jObjectResponse.getInt("idSintoma");
                                String sintoma = jObjectResponse.getString("sintoma");
                                if(dbHelper.buscarListaSintoma(id+"")==false)
                                    dbHelper.insertarUnListaSintoma(id, sintoma);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d("listaSintomas: ", error.getMessage());
            }
        });
        if(getSintomasRequest!=null) {
            getSintomasRequest.setShouldCache(false);
            colaRequest.add(getSintomasRequest);
        }
    }

    public void insertarYpostSintoma(int idSintoma, int cedula, String fechaString) {

        insertarUnSintomaBD(idSintoma, cedula, fechaString);

        postSintomas();
    }

    public void postSintomas(){
        JsonObjectRequest postRequest=null;
        List<List<Object>> sintomasBD;
        List<Object> sintoma;

        sintomasBD = dbHelper.obtenerSintomas();

        if(sintomasBD!=null){
            for (int i = 0; i < sintomasBD.size(); i++){
                sintoma=sintomasBD.get(i);

                Log.i("Sintoma de DB: ",sintoma.toString());

                //Dato del Valor (fila 5), si es 0 no se a enviado
                if(sintoma.get(4).toString().equals("0")) {

                    final JSONObject caminataJson = new JSONObject();
                    try {
                        caminataJson.put("id", sintoma.get(0).toString());
                        caminataJson.put("cedula", sintoma.get(1).toString());
                        caminataJson.put("idSintoma", sintoma.get(2).toString());
                        caminataJson.put("fecha", sintoma.get(3).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    postRequest = new JsonObjectRequest(Request.Method.POST, URL_SERVIDOR + "sintomas", caminataJson,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("Volley Sintoma: ", response.toString());
                                    try {//La fecha es un identificador unico e igual tanto en el servido como en el app
                                        dbHelper.actualizarEnviadoSintoma(response.getString("fecha"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if(error.networkResponse != null && error.networkResponse.data != null){
                                String errorString = new String(error.networkResponse.data);
                                VolleyLog.d("Sintoma:", errorString);
                            } else if(error.networkResponse != null) {
                                VolleyLog.d("Sintoma Network: ", error.networkResponse.statusCode);
                            }else{
                                VolleyLog.d("Sintoma: ", error.getMessage());
                            }
                        }
                    });
                }
                if(postRequest!=null) {
                    postRequest.setShouldCache(false);
                    colaRequest.add(postRequest);
                }
            }
        }
    }
}
