package husi.recuperapp.accesoDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmss1 on 22/09/2016.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String NOMBRE_BD = "RecuperapDP.db";

    public static final String TABLA_PACIENTE = "tabla_paciente";
    public static final String COL_1_PACIENTE = "CEDULA";
    public static final String COL_2_PACIENTE = "CONTRASENA";
    public static final String COL_3_PACIENTE = "NOMBRES";
    public static final String COL_4_PACIENTE = "APELLIDOS";
    public static final String COL_5_PACIENTE = "EMAIL";

    public static final String TABLA_MEDICAMENTOS = "tabla_medicamentos";
    public static final String COL_1_MEDICAMENTOS = "ID";
    public static final String COL_2_MEDICAMENTOS = "MEDICAMENTO";
    public static final String COL_3_MEDICAMENTOS = "DOSIS";
    public static final String COL_4_MEDICAMENTOS = "FRECUENCIA";
    public static final String COL_5_MEDICAMENTOS = "HORA";
    public static final String COL_6_MEDICAMENTOS = "SINTOMA";
    public static final String COL_7_MEDICAMENTOS = "ASIGNADO";

    public static final String TABLA_FISIOLOGICOS = "tabla_fisiologicos";
    public static final String COL_1_FISIOLOGICOS = "ID";
    public static final String COL_2_FISIOLOGICOS = "CEDULA";
    public static final String COL_3_FISIOLOGICOS = "FECHA";
    public static final String COL_4_FISIOLOGICOS = "MEDICION";
    public static final String COL_5_FISIOLOGICOS = "VALOR";
    public static final String COL_6_FISIOLOGICOS = "ENVIADO";

    public static final String TABLA_ANIMOS = "tabla_animos";
    public static final String COL_1_ANIMOS = "ID";
    public static final String COL_2_ANIMOS = "CEDULA";
    public static final String COL_3_ANIMOS = "FECHA";
    public static final String COL_4_ANIMOS = "VALOR";
    public static final String COL_5_ANIMOS = "ENVIADO";

    public static final String TABLA_SINTOMAS = "tabla_sintomas";
    public static final String COL_1_SINTOMAS = "ID";
    public static final String COL_2_SINTOMAS = "CEDULA";
    public static final String COL_3_SINTOMAS = "ID_SINTOMA";
    public static final String COL_4_SINTOMAS = "FECHA";
    public static final String COL_5_SINTOMAS = "ENVIADO";

    public static final String TABLA_CAMINATAS = "tabla_caminatas";
    public static final String COL_1_CAMINATAS = "ID";
    public static final String COL_2_CAMINATAS = "CEDULA";
    public static final String COL_3_CAMINATAS = "FECHA";
    public static final String COL_4_CAMINATAS = "TIEMPO";
    public static final String COL_5_CAMINATAS = "DISTANCIA";
    public static final String COL_6_CAMINATAS = "PASOS";
    public static final String COL_7_CAMINATAS = "ID_SINTOMA_CAMINATA";
    public static final String COL_8_CAMINATAS = "ENVIADO";

    public static final String TABLA_LISTA_SINTOMAS = "tabla_lista_sintomas";
    public static final String COL_1_LISTA_SINTOMAS = "ID_SINTOMA";
    public static final String COL_2_LISTA_SINTOMAS = "SINTOMA";

    public static final String TABLA_CITAS = "tabla_citas";
    public static final String COL_1_CITAS = "ID";
    public static final String COL_2_CITAS = "FECHA";
    public static final String COL_3_CITAS = "MEDICO";

    public static final String CREAR_TABLA_PACIENTE = "create table " + TABLA_PACIENTE + " " +
            "(" + COL_1_PACIENTE + " INTEGER PRIMARY KEY, " + COL_2_PACIENTE + " TEXT," +
            COL_3_PACIENTE + " TEXT," + COL_4_PACIENTE + " TEXT," + COL_5_PACIENTE + " TEXT)";

    public static final String CREAR_TABLA_FISIOLOGICOS = "create table " + TABLA_FISIOLOGICOS + " " +
            "("+COL_1_FISIOLOGICOS+" INTEGER PRIMARY KEY " + "AUTOINCREMENT," + COL_2_FISIOLOGICOS +
            " INTEGER," + COL_3_FISIOLOGICOS + " TEXT," + COL_4_FISIOLOGICOS + " TEXT," +
            COL_5_FISIOLOGICOS + " REAL, "+ COL_6_FISIOLOGICOS +" INTEGER )";

    public static final String CREAR_TABLA_ANIMOS = "create table " + TABLA_ANIMOS + " " +
            "("+COL_1_ANIMOS+" INTEGER PRIMARY KEY " + "AUTOINCREMENT," + COL_2_ANIMOS +
            " INTEGER," + COL_3_ANIMOS + " TEXT," + COL_4_ANIMOS + " REAL, "+ COL_5_ANIMOS +" INTEGER )";

    public static final String CREAR_TABLA_SINTOMAS = "create table " + TABLA_SINTOMAS + " " +
            "("+COL_1_SINTOMAS+" INTEGER PRIMARY KEY " + "AUTOINCREMENT,"
            + COL_2_SINTOMAS + " INTEGER," + COL_3_SINTOMAS + " INTEGER," + COL_4_SINTOMAS +
            " TEXT," + COL_5_SINTOMAS +" INTEGER )";

    public static final String CREAR_TABLA_CAMINATAS = "create table " + TABLA_CAMINATAS + " " +
            "("+COL_1_CAMINATAS+" INTEGER PRIMARY KEY " + "AUTOINCREMENT," + COL_2_CAMINATAS
            + " INTEGER," + COL_3_CAMINATAS + " TEXT," + COL_4_CAMINATAS + " INTEGER," + COL_5_CAMINATAS
            + " INTEGER,"+ COL_6_CAMINATAS + " INTEGER,"+ COL_7_CAMINATAS + " INTEGER, "
            + COL_8_CAMINATAS +" INTEGER )";

    public static final String CREAR_TABLA_MEDICAMENTOS = "create table " + TABLA_MEDICAMENTOS + " " +
            "("+COL_1_MEDICAMENTOS+" INTEGER PRIMARY KEY, " + COL_2_MEDICAMENTOS +
            " TEXT," + COL_3_MEDICAMENTOS + " TEXT," + COL_4_MEDICAMENTOS + " TEXT," + COL_5_MEDICAMENTOS
            + " INTEGER," + COL_6_MEDICAMENTOS + " TEXT," + COL_7_MEDICAMENTOS +" TEXT)";

    public static final String CREAR_TABLA_LISTA_SINTOMAS = "create table " + TABLA_LISTA_SINTOMAS + " " +
            "("+COL_1_LISTA_SINTOMAS+" INTEGER PRIMARY KEY, " + COL_2_LISTA_SINTOMAS + " TEXT )";

    public static final String CREAR_TABLA_CITAS = "create table " + TABLA_CITAS + " " +
            "("+COL_1_CITAS+" INTEGER PRIMARY KEY " + "AUTOINCREMENT," + COL_2_CITAS +
            " INTEGER," + COL_3_CITAS + " TEXT)";

    public DataBaseHelper(Context context) {
        super(context, NOMBRE_BD, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_PACIENTE);
        db.execSQL(CREAR_TABLA_FISIOLOGICOS);
        db.execSQL(CREAR_TABLA_MEDICAMENTOS);
        db.execSQL(CREAR_TABLA_CAMINATAS);
        db.execSQL(CREAR_TABLA_CITAS);
        db.execSQL(CREAR_TABLA_LISTA_SINTOMAS);
        db.execSQL(CREAR_TABLA_SINTOMAS);
        db.execSQL(CREAR_TABLA_ANIMOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PACIENTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_FISIOLOGICOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_MEDICAMENTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CAMINATAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CITAS);
        db.execSQL("DROP TABLE IF EXISTS " + CREAR_TABLA_LISTA_SINTOMAS);
        db.execSQL("DROP TABLE IF EXISTS " + CREAR_TABLA_SINTOMAS);
        db.execSQL("DROP TABLE IF EXISTS " + CREAR_TABLA_ANIMOS);
        onCreate(db);
    }

    public boolean existeLaTabla(String nombreTabla) {
        SQLiteDatabase db = this.getReadableDatabase();

        if(db == null || !db.isOpen()) {
            db = getReadableDatabase();
        }

        if(!db.isReadOnly()) {
            db.close();
            db = getReadableDatabase();
        }

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+
                nombreTabla+"'", null);

        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }

        return false;
    }

    //Paciente
    public boolean insertarUnPaciente(int cedula, String contrasena, String nombres,
                                      String apellidos, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_PACIENTE, cedula);
        contentValues.put(COL_2_PACIENTE, contrasena);
        contentValues.put(COL_3_PACIENTE, nombres);
        contentValues.put(COL_4_PACIENTE, apellidos);
        contentValues.put(COL_5_PACIENTE, email);
        long result = db.insert(TABLA_PACIENTE, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public ArrayList<String> obtenerPaciente() {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<String> paciente = new ArrayList<>();
        Cursor resultado = db.rawQuery("select * from " + TABLA_PACIENTE, null);
        if(resultado.getCount() == 0) {
            return null;
        }

        StringBuffer buffer = new StringBuffer();
        while (resultado.moveToNext()) {
            paciente.add(resultado.getString(0));
            paciente.add(resultado.getString(1));
            paciente.add(resultado.getString(2));
            paciente.add(resultado.getString(3));
            paciente.add(resultado.getString(4));
        }
        return paciente;
    }

    public boolean actualizarUnPaciente(int cedula, String contrasena, String nombres,
                                        String apellidos, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_PACIENTE, cedula);
        contentValues.put(COL_2_PACIENTE, contrasena);
        contentValues.put(COL_3_PACIENTE, nombres);
        contentValues.put(COL_4_PACIENTE, apellidos);
        contentValues.put(COL_5_PACIENTE, email);
        db.update(TABLA_PACIENTE, contentValues, COL_1_PACIENTE+" = ?", new String[]{cedula+""});
        return true;
    }

    //Fisiológicos

    public boolean insertarUnFisiologico(Integer cedula, String fecha, String medicion, double valor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_FISIOLOGICOS, cedula);
        contentValues.put(COL_3_FISIOLOGICOS, fecha);
        contentValues.put(COL_4_FISIOLOGICOS, medicion);
        contentValues.put(COL_5_FISIOLOGICOS, valor);
        contentValues.put(COL_6_FISIOLOGICOS, 0);
        long result = db.insert(TABLA_FISIOLOGICOS, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public List<List<Object>> obtenerFisiologicos() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor resultado = db.rawQuery("select * from " + TABLA_FISIOLOGICOS, null);
        if(resultado.getCount() == 0) {
            return null;
        }

        List<List<Object>> fisiologicos = new ArrayList<>();
        List<Object> fisiologico;

        while (resultado.moveToNext()) {
            fisiologico = new ArrayList<>();
            fisiologico.add(resultado.getInt(0));
            fisiologico.add(resultado.getInt(1));
            fisiologico.add(resultado.getString(2));
            fisiologico.add(resultado.getString(3));
            fisiologico.add(resultado.getDouble(4));
            fisiologico.add(resultado.getInt(5));

            fisiologicos.add(fisiologico);
        }
        return fisiologicos;
    }

    //TODO: cambia todos los datos, solo debe cambiar el dato con la fecha exacta
    public boolean actualizarEnviadoFisiologico(String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_6_FISIOLOGICOS, 1);
        db.update(TABLA_FISIOLOGICOS, contentValues, "FECHA = ?", new String[]{fecha});
        return true;
    }

    //Animo
    public boolean insertarUnAnimo(Integer cedula, String fecha, float valor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_ANIMOS, cedula);
        contentValues.put(COL_3_ANIMOS, fecha);
        contentValues.put(COL_4_ANIMOS, valor);
        contentValues.put(COL_5_ANIMOS, 0);
        long result = db.insert(TABLA_ANIMOS, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public List<List<Object>> obtenerAnimos() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor resultado = db.rawQuery("select * from " + TABLA_ANIMOS, null);
        if(resultado.getCount() == 0) {
            return null;
        }

        List<List<Object>> animos = new ArrayList<>();
        List<Object> animo;

        while (resultado.moveToNext()) {
            animo = new ArrayList<>();
            animo.add(resultado.getInt(0));
            animo.add(resultado.getInt(1));
            animo.add(resultado.getString(2));
            animo.add(resultado.getFloat(3));
            animo.add(resultado.getInt(4));

            animos.add(animo);
        }
        return animos;
    }

    //TODO: cambia todos los datos, solo debe cambiar el dato con la fecha exacta
    public boolean actualizarEnviadoAnimo(String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_5_ANIMOS, 1);
        db.update(TABLA_ANIMOS, contentValues, "FECHA = ?", new String[]{fecha});
        return true;
    }

    //Caminatas

    public boolean insertarUnaCaminata(int cedula, String fecha, int tiempo, int distancia,
                                       int pasos, int idSintomaCaminata) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_CAMINATAS, cedula);
        contentValues.put(COL_3_CAMINATAS, fecha);
        contentValues.put(COL_4_CAMINATAS, tiempo);
        contentValues.put(COL_5_CAMINATAS, distancia);
        contentValues.put(COL_6_CAMINATAS, pasos);
        contentValues.put(COL_7_CAMINATAS, idSintomaCaminata);
        contentValues.put(COL_8_CAMINATAS, 0);
        long result = db.insert(TABLA_CAMINATAS, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public List<List<Object>> obtenerCaminatas() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor resultado = db.rawQuery("select * from " + TABLA_CAMINATAS, null);
        if(resultado.getCount() == 0) {
            return null;
        }

        List<List<Object>> caminatas = new ArrayList<>();
        ArrayList<Object> caminata;

        while (resultado.moveToNext()) {
            caminata = new ArrayList<>();
            caminata.add(resultado.getString(0));
            caminata.add(resultado.getString(1));
            caminata.add(resultado.getString(2));
            caminata.add(resultado.getString(3));
            caminata.add(resultado.getString(4));
            caminata.add(resultado.getString(5));
            caminata.add(resultado.getString(6));
            caminata.add(resultado.getString(7));

            caminatas.add(caminata);
        }
        return caminatas;
    }

    //TODO: cambia todos los datos, solo debe cambiar el dato con la fecha exacta
    public boolean actualizarEnviadoCaminata(String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_8_CAMINATAS, 1);
        db.update(TABLA_CAMINATAS, contentValues, "FECHA = ?", new String[]{fecha});
        return true;
    }

    //Medicamentos

    public boolean insertarUnMedicamento(String id, String medicamento, String dosis,
                                         String frecuencia, long hora,String sintoma,
                                         String asignado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_MEDICAMENTOS, id);
        contentValues.put(COL_2_MEDICAMENTOS, medicamento);
        contentValues.put(COL_3_MEDICAMENTOS, dosis);
        contentValues.put(COL_4_MEDICAMENTOS, frecuencia);
        contentValues.put(COL_5_MEDICAMENTOS, hora);
        contentValues.put(COL_6_MEDICAMENTOS, sintoma);
        contentValues.put(COL_7_MEDICAMENTOS, asignado);
        long result = db.insert(TABLA_MEDICAMENTOS, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public List<List<String>> obtenerMedicamentos() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor resultado = db.rawQuery("select * from " + TABLA_MEDICAMENTOS, null);
        if(resultado.getCount() == 0) {
            return null;
        }

        List<List<String>> medicamentos = new ArrayList<>();
        ArrayList<String> medicamento;

        while (resultado.moveToNext()) {
            medicamento = new ArrayList<>();
            medicamento.add(resultado.getString(0));
            medicamento.add(resultado.getString(1));
            medicamento.add(resultado.getString(2));
            medicamento.add(resultado.getString(3));
            medicamento.add(String.valueOf(resultado.getLong(4)));
            medicamento.add(resultado.getString(5));
            medicamento.add(resultado.getString(6));

            medicamentos.add(medicamento);
        }
        return medicamentos;
    }

    public boolean buscarMedicamento(String id){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor resultado = db.rawQuery("select * from " + TABLA_MEDICAMENTOS + " where "+
                COL_1_MEDICAMENTOS+" = "+ id, null);
        if(resultado.getCount() == 0) {
            return false;
        }else
            return true;
    }

    public boolean actualizarUnMedicamento(String id, String medicamento, String dosis,
                                           String frecuencia, int hora, String sintoma,
                                           String asignado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_MEDICAMENTOS, medicamento);
        contentValues.put(COL_3_MEDICAMENTOS, dosis);
        contentValues.put(COL_4_MEDICAMENTOS, frecuencia);
        contentValues.put(COL_5_MEDICAMENTOS, hora);
        contentValues.put(COL_6_MEDICAMENTOS, sintoma);
        contentValues.put(COL_7_MEDICAMENTOS, asignado);
        db.update(TABLA_MEDICAMENTOS, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public boolean actualizarHoraConsumoMedicamento(String id, long hora, String asignado){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_5_MEDICAMENTOS, hora);
        contentValues.put(COL_7_MEDICAMENTOS, asignado);
        db.update(TABLA_MEDICAMENTOS, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public List<Object> obtenerUnMedicamento(int idMedicamento) {

        List<Object> medicamento= new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor resultado = db.rawQuery("select * from " + TABLA_MEDICAMENTOS + " where "+
                COL_1_MEDICAMENTOS+" = "+ idMedicamento, null);
        if(resultado.getCount() == 0) {
            Log.i("No existe el medic id: ",idMedicamento+"");
            return null;
        }else{
            while (resultado.moveToNext()) {
                medicamento = new ArrayList<>();
                medicamento.add(resultado.getString(0));
                medicamento.add(resultado.getString(1));
                medicamento.add(resultado.getString(2));
                medicamento.add(resultado.getString(3));
                medicamento.add(String.valueOf(resultado.getInt(4)));
                medicamento.add(resultado.getString(5));
                medicamento.add(resultado.getString(6));
            }
            return medicamento;
        }
    }

    public boolean borrarUnMedicamento(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Integer filasBorradas = db.delete(TABLA_MEDICAMENTOS, "ID = ?", new String[]{id});

        if(filasBorradas > 0)
            return true;
        return false;
    }

    //Sintomas

    public boolean insertarUnSintoma(int cedula, int idSintoma, String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_SINTOMAS, cedula);
        contentValues.put(COL_3_SINTOMAS, idSintoma);
        contentValues.put(COL_4_SINTOMAS, fecha);
        contentValues.put(COL_5_SINTOMAS, 0);
        long result = db.insert(TABLA_SINTOMAS, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public List<List<Object>> obtenerSintomas() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor resultado = db.rawQuery("select * from " + TABLA_SINTOMAS, null);
        if(resultado.getCount() == 0) {
            return null;
        }

        List<List<Object>> sintomas = new ArrayList<>();
        ArrayList<Object> sintoma;

        while (resultado.moveToNext()) {
            sintoma = new ArrayList<>();
            sintoma.add(resultado.getString(0));
            sintoma.add(resultado.getString(1));
            sintoma.add(resultado.getString(2));
            sintoma.add(resultado.getString(3));
            sintoma.add(resultado.getString(4));

            sintomas.add(sintoma);
        }

        return sintomas;
    }

    public boolean actualizarEnviadoSintoma(String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_5_SINTOMAS, 1);
        db.update(TABLA_SINTOMAS, contentValues, "FECHA = ?", new String[]{fecha});
        return true;
    }

    //Lista Sintomas
    public boolean buscarListaSintoma(String id){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor resultado = db.rawQuery("select * from " + TABLA_LISTA_SINTOMAS + " where "+
                COL_1_LISTA_SINTOMAS+" = "+ id, null);
        if(resultado.getCount() == 0) {
            return false;
        }else
            return true;
    }

    public boolean insertarUnListaSintoma(Integer id, String sintoma) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_LISTA_SINTOMAS, id);
        contentValues.put(COL_2_LISTA_SINTOMAS, sintoma);
        long result = db.insert(TABLA_LISTA_SINTOMAS, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public List<List<Object>> obtenerListaSintomas() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor resultado = db.rawQuery("select * from " + TABLA_LISTA_SINTOMAS, null);
        if(resultado.getCount() == 0) {
            return null;
        }

        List<List<Object>> sintomas = new ArrayList<>();
        ArrayList<Object> sintoma;

        while (resultado.moveToNext()) {
            sintoma = new ArrayList<>();
            sintoma.add(resultado.getString(0));
            sintoma.add(resultado.getString(1));

            sintomas.add(sintoma);
        }

        return sintomas;
    }

    //Citas

    public boolean insertarUnaCita(long fecha, String medico) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_CITAS, fecha);
        contentValues.put(COL_3_CITAS, medico);
        long result = db.insert(TABLA_CITAS, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public List<List<String>> obtenerCitas() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor resultado = db.rawQuery("select * from " + TABLA_CITAS, null);
        if(resultado.getCount() == 0) {
            return null;
        }

        List<List<String>> citas = new ArrayList<>();
        ArrayList<String> cita;

        while (resultado.moveToNext()) {
            cita = new ArrayList<>();
            cita.add(resultado.getString(0));
            cita.add(String.valueOf(resultado.getLong(1)));
            cita.add(resultado.getString(2));

            citas.add(cita);
        }

        return citas;
    }

    public List<String> buscarCita(long fecha, String medico){
        List<String> cita= new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor resultado = db.rawQuery("select * from " + TABLA_CITAS + " where "+
                COL_2_CITAS + " = " +fecha + " and "+ COL_3_CITAS + " = " +"'"+medico+"'", null);
        if(resultado.getCount() == 0) {
            Log.i("BD: ","No existe la cita ");
            return null;
        }else{
            while (resultado.moveToNext()) {
                cita = new ArrayList<>();
                cita.add(resultado.getString(0));
                cita.add(String.valueOf(resultado.getLong(1)));
                cita.add(resultado.getString(2));
            }
            return cita;
        }
    }

    public List<String> buscarCitaId(int idCita) {
        List<String> cita = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor resultado = db.rawQuery("select * from " + TABLA_CITAS + " where " +
                COL_1_CITAS + " = " + idCita, null);
        if (resultado.getCount() == 0) {
            Log.i("BD: ", "No existe la cita: "+idCita);
            return null;
        } else {
            while (resultado.moveToNext()) {
                cita = new ArrayList<>();
                cita.add(resultado.getString(0));
                cita.add(resultado.getString(1));
                cita.add(resultado.getString(2));
            }
            return cita;
        }
    }

    public boolean borrarUnaCita(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Integer filasBorradas = db.delete(TABLA_CITAS, "ID = ?", new String[]{id});

        if(filasBorradas > 0)
            return true;
        return false;
    }
}
