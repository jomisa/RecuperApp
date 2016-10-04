package husi.recuperapp;

/**
 * Created by jmss1 on 4/10/2016.
 */

public class Medicamento {

    private String hora;
    private String nombre;
    private String dosis;

    public Medicamento (String hora, String nombre,  String dosis) {
        super();
        this.hora = hora;
        this.nombre = nombre;
        this.dosis = dosis;
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
}
