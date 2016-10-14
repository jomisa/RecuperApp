package husi.recuperapp.citas;

/**
 * Created by jmss1 on 9/10/2016.
 */

public class Cita {

    private String id;
    private String fecha;
    private String medico;

    public Cita(String id, String fecha, String medico) {
        super();
        this.id = id;
        this.fecha = fecha;
        this.medico = medico;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }
}
