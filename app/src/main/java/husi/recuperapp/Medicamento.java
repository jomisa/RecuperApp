package husi.recuperapp;

/**
 * Created by jmss1 on 4/10/2016.
 */

public class Medicamento {

    private String id;
    private String hora;
    private String nombre;
    private String dosis;
    private String asignado;
    private String frecuencia;

    public Medicamento (String id, String nombre, String dosis, String frecuencia, String hora, String asignado) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.dosis = dosis;
        this.asignado = asignado;
        this.frecuencia = frecuencia;
        this.hora = hora;
    }

    public String getHora() {
        return hora;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDosis() {
        return dosis;
    }

    public String getAsignado() {
        return asignado;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setAsignado(String asignado) {
        this.asignado = asignado;
    }
}
