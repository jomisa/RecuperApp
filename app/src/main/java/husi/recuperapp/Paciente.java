package husi.recuperapp;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by jmss1 on 22/09/2016.
 */

public class Paciente extends Application{
    private static Paciente singleton;

    static DataBaseHelper dbHelper;

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
        this.existeEnBd=false;

        dbHelper = new DataBaseHelper(this);
        crearObjetoDesdeBD(dbHelper);
        singleton = this;
    }

    public boolean crearObjetoDesdeBD(DataBaseHelper dbHelper) {
        if (this.existeEnBd == false){

            ArrayList<String> paciente = new ArrayList<String>();
            paciente = dbHelper.obtenerPaciente();

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
}
