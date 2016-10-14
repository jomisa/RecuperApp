package husi.recuperapp;

import android.app.Application;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by jmss1 on 22/09/2016.
 */

public class Paciente extends Application{
    private static Paciente singleton;

    static DataBaseHelper dbHelper;


    private final String URL_BASE = "http://10.0.2.2:8080/RecuperAppServer2/WebServices/";

    private final String servicioListaSintomas="listasintomas";
//    private final String servicioFisiologicosPut="fisiologicos/";

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

        //getRequestWebService(servicioListaSintomas);


        String idPut="/{98}";
        JSONObject obj = new JSONObject();

        JSONArray jArray = new JSONArray();
        //jArray.put();

        try {
            obj.put("idSintoma", "99");
            obj.put("sintoma", "sintoma prueba android");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //getRequestWebService(servicioListaSintomas);

        //postArrayRequestWebService(servicioListaSintomas, jArray);

        //soap();

        postRequestWebService(servicioListaSintomas, obj);

        //putStringRequestWebService(servicioListaSintomas);

        //putHttpClient(servicioListaSintomas,obj);

        //postHttp2(servicioListaSintomas);

        this.existeEnBd=false;

        dbHelper = new DataBaseHelper(this);
        crearObjetoDesdeBD(dbHelper);
        singleton = this;
    }

    private void soap() {


        try {
            SoapObject request = new SoapObject("http://calculator.me.org/", "add");
            request.addProperty("i",1);
            request.addProperty("j",2);
            //http://calculator.me.org/
            //"http://integration/"
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.setOutputSoapObject(request);

            HttpTransportSE httpTrans = new HttpTransportSE("http://10.0.2.2:8080/CalculatorApp/CalculatorWSService?wsdl");
//10.0.2.2
            httpTrans.call("add",soapEnvelope);

            Object resultado = soapEnvelope.getResponse();

            Log.i("Respuesta: ",Integer.parseInt(resultado.toString())+"");

        }catch (IOException ioex){
            Log.e("ERROR: ", ioex.getMessage());
        }catch (XmlPullParserException xmlPar){
            Log.e("ERROR: ", xmlPar.getMessage());
        }
        catch (Exception ex){
            Log.e("ERROR: ", ex.getMessage());
        }
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

    private void postHttp2(final String servicio){
        HttpURLConnection urlConnection=null;
        String json = null;
        // -----------------------

        try {
            HttpResponse response;
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("idSintoma", "99");
            jsonObject.accumulate("sintoma", "sintoma prueba android");
            json = jsonObject.toString();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL_BASE+servicio);
            httpPost.setEntity(new StringEntity(json, "UTF-8"));
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept-Encoding", "application/json");
            httpPost.setHeader("Accept-Language", "en-US");
            response = httpClient.execute(httpPost);
            String sresponse = response.getEntity().toString();
            Log.w("QueingSystem", sresponse);
            Log.w("QueingSystem", EntityUtils.toString(response.getEntity()));
        }
        catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());

        } finally {
        /* nothing to do here */
        }

    }

    private void putHttpClient(final String servicio, final JSONObject obj){
        InputStream inputStream = null;
        String result = "";

        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // 2. make POST request to the given URL
            HttpPut httpPUT = new HttpPut(URL_BASE+servicio);
            String json = "";

            // 4. convert JSONObject to JSON to String
            json = obj.toString();

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);
            // 6. set httpPost Entity
            httpPUT.setEntity(se);
            // 7. Set some headers to inform server about the type of the content
            httpPUT.setHeader("Accept", "application/json");
            httpPUT.setHeader("Content-type", "application/json");
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPUT);
            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // 10. convert inputstream to string
            //                  if(inputStream != null)
            //                      result = convertInputStreamToString(inputStream);
            //                  else
            //                      result = "Did not work!";
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage().toString());
        }

    }

    private void putStringRequestWebService(final String servicio){

        //String url = "http://httpbin.org/put";
        StringRequest putRequest = new StringRequest(Request.Method.PUT, URL_BASE+servicio,
                new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                // response
                Log.d("Response", response);
                }
            },
            new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    // error
                    Log.d("Error.Response", error.toString());
                }
            }){
            @Override
            protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<String, String> ();
                    params.put("idSintoma", "99");
                    params.put("sintoma", "sintoma prueba android");

                    return params;
                }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(putRequest);
    }

    private void postRequestWebService(final String servicio, final JSONObject obj){

        /*JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.POST, URL_BASE+servicio,obj,
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                //headers.put("Content-Type", "application/json; charset=utf-8");
                //headers.put("Content-Type", "application/x-www-form-urlencoded, application/json; charset=utf-8");
                return headers;
            }
        };*/

        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.POST, URL_BASE+servicio,obj,
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

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(putRequest);
    }

    private void postArrayRequestWebService(final String servicio, final JSONArray jArray){

        JsonArrayRequest putArrayRequest = new JsonArrayRequest(Request.Method.POST, URL_BASE+servicio, jArray,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response){
                        Log.i("Volley Array: ", response.toString());
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){

                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                        }
                        if (error instanceof TimeoutError) {
                            Log.e("Volley", "TimeoutError");
                        }else if(error instanceof NoConnectionError){
                            Log.e("Volley", "NoConnectionError");
                        } else if (error instanceof AuthFailureError) {
                            Log.e("Volley", "AuthFailureError");
                        } else if (error instanceof ServerError) {
                            Log.e("Volley", "ServerError");
                        } else if (error instanceof NetworkError) {
                            Log.e("Volley", "NetworkError");
                        } else if (error instanceof ParseError) {
                            Log.e("Volley", "ParseError");
                        }

                        VolleyLog.d("Volley Error: ",error.toString());
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };


        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(putArrayRequest);
    }

    private void getRequestWebService(final String servicio){

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
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(getArrayRequest);
    }

    private void getStringRequestWebService(final String servicio){
        StringRequest getRequest = new StringRequest(Request.Method.GET, URL_BASE+servicio,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String respuesta) {
                        manejarRequest(servicio, respuesta);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error: ", error.getMessage());
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(getRequest);
    }

    private void manejarRequest(String servicio, String respuesta){
        if(servicio.equals(servicioListaSintomas)) {
            Log.d("Respuesta: ", respuesta);
            xmlToBD(servicio, respuesta);
        }
    }

    private void xmlToBD(String servicio, String xml) {

        dbHelper = new DataBaseHelper(this);

        if (servicio.equals(servicioListaSintomas)) {

            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xml));

                Document doc = db.parse(is);
                NodeList nodes = doc.getElementsByTagName("listaSintomas");

                for (int i = 0; i < nodes.getLength(); i++) {
                    Element element = (Element) nodes.item(i);

                    NodeList idNode = element.getElementsByTagName("idSintoma");
                    String idSintoma = getStringDelElement((Element) idNode.item(0));

                    NodeList sintomaNode = element.getElementsByTagName("sintoma");
                    String sintoma = getStringDelElement((Element) sintomaNode.item(0));

                    dbHelper.insertarUnListaSintoma(idSintoma, sintoma);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }



    public static String getStringDelElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "?";
    }
}
