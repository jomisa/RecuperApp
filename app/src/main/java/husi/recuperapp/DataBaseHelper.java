package husi.recuperapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jmss1 on 22/09/2016.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String NOMBRE_BD = "RecuperapDP.db";
    public static final String TABLA_PACIENTE = "tabla_paciente";
    public static final String COL_1_PACIENTE = "ID";
    public static final String COL_2_PACIENTE = "USUARIO";
    public static final String COL_3_PACIENTE = "CONTRASENA";
    public static final String COL_4_PACIENTE = "EMAIL";

    public static final String TABLA_FISIOLOGICOS = "tabla_fisiologicos";
    public static final String COL_1_FISIOLOGICOS = "ID";
    public static final String COL_2_FISIOLOGICOS = "CEDULA";
    public static final String COL_3_FISIOLOGICOS = "FECHA";
    public static final String COL_4_FISIOLOGICOS = "MEDICION";
    public static final String COL_5_FISIOLOGICOS = "VALOR";
    public static final String COL_6_FISIOLOGICOS = "ENVIADO";

    public static final String TABLA_MEDICAMENTOS = "tabla_medicamentos";
    public static final String COL_1_MEDICAMENTOS = "ID";
    public static final String COL_2_MEDICAMENTOS = "MEDICAMENTO";
    public static final String COL_3_MEDICAMENTOS = "DOSIS";
    public static final String COL_4_MEDICAMENTOS = "FRECUENCIA";
    public static final String COL_5_MEDICAMENTOS = "HORA";
    public static final String COL_6_MEDICAMENTOS = "ASIGNADO";

    public static final String TABLA_CAMINATAS = "tabla_caminatas";
    public static final String COL_1_CAMINATAS = "ID";
    public static final String COL_2_CAMINATAS = "FECHA";
    public static final String COL_3_CAMINATAS = "TIEMPO";
    public static final String COL_4_CAMINATAS = "DISTANCIA";
    public static final String COL_5_CAMINATAS = "PASOS";

    public static final String TABLA_CITAS = "tabla_citas";
    public static final String COL_1_CITAS = "ID";
    public static final String COL_2_CITAS = "FECHA";
    public static final String COL_3_CITAS = "MEDICO";

    public static final String TABLA_LISTA_SINTOMAS = "tabla_lista_sintomas";
    public static final String COL_1_LISTA_SINTOMAS = "ID_SINTOMA";
    public static final String COL_2_LISTA_SINTOMAS = "SINTOMA";

    public static final String CREAR_TABLA_PACIENTE = "create table " + TABLA_PACIENTE + " " +
            "(" + COL_1_PACIENTE + " INTEGER PRIMARY KEY " + "AUTOINCREMENT," + COL_2_PACIENTE +
            " TEXT," + COL_3_PACIENTE + " TEXT," + COL_4_PACIENTE + " INTEGER)";

    public static final String CREAR_TABLA_FISIOLOGICOS = "create table " + TABLA_FISIOLOGICOS + " " +
            "("+COL_1_FISIOLOGICOS+" INTEGER PRIMARY KEY " + "AUTOINCREMENT," + COL_2_FISIOLOGICOS +
            " INTEGER," + COL_3_FISIOLOGICOS + " TEXT," + COL_4_FISIOLOGICOS + " TEXT," +
            COL_5_FISIOLOGICOS + " REAL, "+ COL_6_FISIOLOGICOS +" INTEGER )";

    public static final String CREAR_TABLA_MEDICAMENTOS = "create table " + TABLA_MEDICAMENTOS + " " +
            "("+COL_1_MEDICAMENTOS+" INTEGER PRIMARY KEY " + "AUTOINCREMENT," + COL_2_MEDICAMENTOS +
            " TEXT," + COL_3_MEDICAMENTOS + " TEXT," + COL_4_MEDICAMENTOS + " TEXT," + COL_5_MEDICAMENTOS
            + " TEXT," + COL_6_MEDICAMENTOS +" TEXT)";

    public static final String CREAR_TABLA_CAMINATAS = "create table " + TABLA_CAMINATAS + " " +
            "("+COL_1_CAMINATAS+" INTEGER PRIMARY KEY " + "AUTOINCREMENT," + COL_2_CAMINATAS +
            " TEXT," + COL_3_CAMINATAS + " TEXT," + COL_4_CAMINATAS + " TEXT," + COL_5_CAMINATAS
            + " TEXT)";

    public static final String CREAR_TABLA_CITAS = "create table " + TABLA_CITAS + " " +
            "("+COL_1_CITAS+" INTEGER PRIMARY KEY " + "AUTOINCREMENT," + COL_2_CITAS +
            " TEXT," + COL_3_CITAS + " TEXT)";

    public static final String CREAR_TABLA_LISTA_SINTOMAS = "create table " + TABLA_LISTA_SINTOMAS + " " +
            "("+COL_1_LISTA_SINTOMAS+" INTEGER PRIMARY KEY, " + COL_2_LISTA_SINTOMAS + " TEXT )";

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PACIENTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_FISIOLOGICOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_MEDICAMENTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CAMINATAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CITAS);
        db.execSQL("DROP TABLE IF EXISTS " + CREAR_TABLA_LISTA_SINTOMAS);
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

    public boolean insertarUnPaciente(String usuario, String contrasena, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_PACIENTE, usuario);
        contentValues.put(COL_3_PACIENTE, contrasena);
        contentValues.put(COL_4_PACIENTE, email);
        long result = db.insert(TABLA_PACIENTE, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public ArrayList<String> obtenerPaciente() {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<String> paciente = new ArrayList<String>();
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
        }
        return paciente;
    }

    public boolean actualizarUnPaciente(String id, String usuario, String contrasena, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_PACIENTE, id);
        contentValues.put(COL_2_PACIENTE, usuario);
        contentValues.put(COL_3_PACIENTE, contrasena);
        contentValues.put(COL_4_PACIENTE, email);
        db.update(TABLA_PACIENTE, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public boolean borrarUnPaciente(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Integer filasBorradas = db.delete(TABLA_PACIENTE, "ID = ?", new String[]{id});

        if(filasBorradas > 0)
            return true;
        return false;
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

    //Medicamentos

    public boolean insertarUnMedicamento(String medicamento, String dosis, String frecuencia,
                                         String hora, String asignado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_MEDICAMENTOS, medicamento);
        contentValues.put(COL_3_MEDICAMENTOS, dosis);
        contentValues.put(COL_4_MEDICAMENTOS, frecuencia);
        contentValues.put(COL_5_MEDICAMENTOS, hora);
        contentValues.put(COL_6_MEDICAMENTOS, asignado);
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

        List<List<String>> medicamentos = new ArrayList<List<String>>();
        ArrayList<String> medicamento;

        while (resultado.moveToNext()) {
            medicamento = new ArrayList<String>();
            medicamento.add(resultado.getString(0));
            medicamento.add(resultado.getString(1));
            medicamento.add(resultado.getString(2));
            medicamento.add(resultado.getString(3));
            medicamento.add(resultado.getString(4));
            medicamento.add(resultado.getString(5));

            medicamentos.add(medicamento);
        }
        return medicamentos;
    }

    public boolean actualizarUnMedicamento(String id, String medicamento, String dosis,
                                           String frecuencia, String hora, String asignado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_MEDICAMENTOS, id);
        contentValues.put(COL_2_MEDICAMENTOS, medicamento);
        contentValues.put(COL_3_MEDICAMENTOS, dosis);
        contentValues.put(COL_4_MEDICAMENTOS, frecuencia);
        contentValues.put(COL_5_MEDICAMENTOS, hora);
        contentValues.put(COL_6_MEDICAMENTOS, asignado);
        db.update(TABLA_MEDICAMENTOS, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public boolean borrarUnMedicamento(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Integer filasBorradas = db.delete(TABLA_MEDICAMENTOS, "ID = ?", new String[]{id});

        if(filasBorradas > 0)
            return true;
        return false;
    }

    //Caminatas

    public boolean insertarUnaCaminata(String fecha, String tiempo, String distancia, String pasos) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_CAMINATAS, fecha);
        contentValues.put(COL_3_CAMINATAS, tiempo);
        contentValues.put(COL_4_CAMINATAS, distancia);
        contentValues.put(COL_5_CAMINATAS, pasos);
        long result = db.insert(TABLA_CAMINATAS, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public List<List<String>> obtenerCaminatas() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor resultado = db.rawQuery("select * from " + TABLA_CAMINATAS, null);
        if(resultado.getCount() == 0) {
            return null;
        }

        List<List<String>> caminatas = new ArrayList<List<String>>();
        ArrayList<String> caminata;

        while (resultado.moveToNext()) {
            caminata = new ArrayList<String>();
            caminata.add(resultado.getString(0));
            caminata.add(resultado.getString(1));
            caminata.add(resultado.getString(2));
            caminata.add(resultado.getString(3));
            caminata.add(resultado.getString(4));

            caminatas.add(caminata);
        }
        return caminatas;
    }

    public boolean actualizarUnaCaminata(String id, String fecha, String tiempo, String distancia,
                                         String pasos) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_CAMINATAS, id);
        contentValues.put(COL_2_CAMINATAS, fecha);
        contentValues.put(COL_3_CAMINATAS, tiempo);
        contentValues.put(COL_4_CAMINATAS, distancia);
        contentValues.put(COL_5_CAMINATAS, pasos);
        db.update(TABLA_CAMINATAS, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public boolean borrarUnaCaminata(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Integer filasBorradas = db.delete(TABLA_CAMINATAS, "ID = ?", new String[]{id});

        if(filasBorradas > 0)
            return true;
        return false;
    }

    //Citas

    public boolean insertarUnaCita(String fecha, String medico) {
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

        List<List<String>> citas = new ArrayList<List<String>>();
        ArrayList<String> cita;

        while (resultado.moveToNext()) {
            cita = new ArrayList<String>();
            cita.add(resultado.getString(0));
            cita.add(resultado.getString(1));
            cita.add(resultado.getString(2));

            citas.add(cita);
        }

        return citas;
    }

    public boolean actualizarUnaCita(String id, String fecha, String medico) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_CITAS, id);
        contentValues.put(COL_2_CITAS, fecha);
        contentValues.put(COL_3_CITAS, medico);
        db.update(TABLA_CITAS, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public boolean borrarUnaCita(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Integer filasBorradas = db.delete(TABLA_CITAS, "ID = ?", new String[]{id});

        if(filasBorradas > 0)
            return true;
        return false;
    }

    //Lista Sintomas

    public boolean insertarUnListaSintoma(String id, String sintoma) {
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

    public List<List<String>> obtenerListaSintomas() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor resultado = db.rawQuery("select * from " + TABLA_LISTA_SINTOMAS, null);
        if(resultado.getCount() == 0) {
            return null;
        }

        List<List<String>> sintomas = new ArrayList<List<String>>();
        ArrayList<String> sintoma;

        while (resultado.moveToNext()) {
            sintoma = new ArrayList<String>();
            sintoma.add(resultado.getString(0));
            sintoma.add(resultado.getString(1));

            sintomas.add(sintoma);
        }

        return sintomas;
    }

    public boolean actualizarUnListaSintoma(String id, String sintoma) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_LISTA_SINTOMAS, id);
        contentValues.put(COL_2_LISTA_SINTOMAS, sintoma);
        db.update(TABLA_LISTA_SINTOMAS, contentValues, "ID_SINTOMA = ?", new String[]{id});
        return true;
    }

    public boolean borrarUnListaSintoma(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Integer filasBorradas = db.delete(TABLA_LISTA_SINTOMAS, "ID_SINTOMA = ?", new String[]{id});

        if(filasBorradas > 0)
            return true;
        return false;
    }


}
