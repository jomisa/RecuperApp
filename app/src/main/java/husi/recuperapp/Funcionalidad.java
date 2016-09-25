package husi.recuperapp;

/**
 * Created by jmss1 on 24/09/2016.
 */

public class Funcionalidad {
    private int iconID;
    private String funcionalidad;
    private String descripcion;

    public Funcionalidad(int iconID, String funcionalidad,  String descripcion) {
        super();
        this.iconID = iconID;
        this.funcionalidad = funcionalidad;
        this.descripcion = descripcion;
    }

    public String getFuncionalidad() {
        return this.funcionalidad;
    }
    public int getIconID() {
        return this.iconID;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
}
