package husi.recuperapp.citas;

/**
 * Created by jmss1 on 9/10/2016.
 */

public class Cita {

    private String id;
    private String fecha;
    private String hora;
    private String medico;

    public Cita(String id, String fecha, String hora,String medico) {
        super();
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.medico = medico;
    }

    public String getId() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getMedico() {
        return medico;
    }
}
