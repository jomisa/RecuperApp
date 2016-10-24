package husi.recuperapp.sintomas;

/**
 * Created by jmss1 on 25/09/2016.
 */

public class Sintoma {

    private int idSintoma;
    private String sintomaTitulo;

    public Sintoma(int idSintoma, String sintomaTitulo) {
        super();
        this.idSintoma = idSintoma;
        this.sintomaTitulo = sintomaTitulo;
    }

    public int getIDSintoma() {
        return idSintoma;
    }

    public String getSintomaTitulo() {
        return sintomaTitulo;
    }
}

