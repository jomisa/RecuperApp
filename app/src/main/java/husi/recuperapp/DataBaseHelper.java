package husi.recuperapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

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
    public static final String COL_2_FISIOLOGICOS = "FECHA";
    public static final String COL_3_FISIOLOGICOS = "MEDICION";
    public static final String COL_4_FISIOLOGICOS = "VALOR";

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

    public static final String CREAR_TABLA_PACIENTE = "create table " + TABLA_PACIENTE + " " +
            "(" + COL_1_PACIENTE + " INTEGER PRIMARY KEY " + "AUTOINCREMENT," + COL_2_PACIENTE +
            " TEXT," + COL_3_PACIENTE + " TEXT," + COL_4_PACIENTE + " INTEGER)";

    public static final String CREAR_TABLA_FISIOLOGICOS = "create table " + TABLA_FISIOLOGICOS + " " +
            "("+COL_1_FISIOLOGICOS+" INTEGER PRIMARY KEY " + "AUTOINCREMENT," + COL_2_FISIOLOGICOS +
            " TEXT," + COL_3_FISIOLOGICOS + " TEXT," + COL_4_FISIOLOGICOS + " INTEGER)";

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PACIENTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_FISIOLOGICOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_MEDICAMENTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CAMINATAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CITAS);
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

    public boolean insertarUnFisiologico(String fecha, String medicion, String valor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_FISIOLOGICOS, fecha);
        contentValues.put(COL_3_FISIOLOGICOS, medicion);
        contentValues.put(COL_4_FISIOLOGICOS, valor);
        long result = db.insert(TABLA_FISIOLOGICOS, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public ArrayList<String> obtenerFisiologico() {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<String> fisiologico = new ArrayList<String>();
        Cursor resultado = db.rawQuery("select * from " + TABLA_FISIOLOGICOS, null);
        if(resultado.getCount() == 0) {
            return null;
        }

        StringBuffer buffer = new StringBuffer();
        while (resultado.moveToNext()) {
            fisiologico.add(resultado.getString(0));
            fisiologico.add(resultado.getString(1));
            fisiologico.add(resultado.getString(2));
            fisiologico.add(resultado.getString(3));
        }
        return fisiologico;
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

    public ArrayList<String> obtenerMedicamentos() {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<String> medicamento = new ArrayList<String>();
        Cursor resultado = db.rawQuery("select * from " + TABLA_MEDICAMENTOS, null);
        if(resultado.getCount() == 0) {
            return null;
        }

        StringBuffer buffer = new StringBuffer();
        while (resultado.moveToNext()) {
            medicamento.add(resultado.getString(0));
            medicamento.add(resultado.getString(1));
            medicamento.add(resultado.getString(2));
            medicamento.add(resultado.getString(3));
            medicamento.add(resultado.getString(4));
            medicamento.add(resultado.getString(5));
        }
        return medicamento;
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

    public ArrayList<String> obtenerCaminatas() {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<String> caminatas = new ArrayList<String>();
        Cursor resultado = db.rawQuery("select * from " + TABLA_CAMINATAS, null);
        if(resultado.getCount() == 0) {
            return null;
        }

        StringBuffer buffer = new StringBuffer();
        while (resultado.moveToNext()) {
            caminatas.add(resultado.getString(0));
            caminatas.add(resultado.getString(1));
            caminatas.add(resultado.getString(2));
            caminatas.add(resultado.getString(3));
            caminatas.add(resultado.getString(4));
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

    public ArrayList<String> obtenerCitas() {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<String> citas = new ArrayList<String>();
        Cursor resultado = db.rawQuery("select * from " + TABLA_CITAS, null);
        if(resultado.getCount() == 0) {
            return null;
        }

        StringBuffer buffer = new StringBuffer();
        while (resultado.moveToNext()) {
            citas.add(resultado.getString(0));
            citas.add(resultado.getString(1));
            citas.add(resultado.getString(2));
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
}
