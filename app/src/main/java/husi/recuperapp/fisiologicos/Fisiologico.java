package husi.recuperapp.fisiologicos;

/**
 * Created by jmss1 on 25/09/2016.
 */

public class Fisiologico {

    private int iconID;
    private String medicion;
    private String unidades;

    public Fisiologico(int iconID, String medicion,  String unidades) {
        super();
        this.iconID = iconID;
        this.medicion = medicion;
        this.unidades = unidades;
    }

    public int getIconID() {
        return iconID;
    }

    public String getMedicion() {
        return medicion;
    }

    public String getUnidades() {
        return unidades;
    }
}

