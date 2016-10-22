package husi.recuperapp.utils;

import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jmss1 on 21/09/2016.
 */

public final class Funciones {
    //Este método verifica que la estructura del correo ingresado por el usuario sea correcta, utiliza REGEX
    public static boolean validarFormatoEmail(String correo) {
        Pattern EMAIL_VALIDO = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = EMAIL_VALIDO .matcher(correo);
        return matcher.find();
    }

    public static long tiempoActualEnMillisegundos() {
        Time time = new Time();
        time.setToNow();
        return time.toMillis(false);
    }

    public static String getFechaString(){
        SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fecha.format(new Date(System.currentTimeMillis())).replace(" ","T");
    }

}
