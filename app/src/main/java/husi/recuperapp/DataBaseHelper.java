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

    public static final String CREAR_TABLA_PACIENTE = "create table " + TABLA_PACIENTE + " " +
            "(" + COL_1_PACIENTE + " INTEGER PRIMARY KEY " + "AUTOINCREMENT," + COL_2_PACIENTE +
            " TEXT," + COL_3_PACIENTE + " TEXT," + COL_4_PACIENTE + " INTEGER)";
    public static final String CREAR_TABLA_FISIOLOGICOS = "create table " + TABLA_FISIOLOGICOS + " " +
            "("+COL_1_FISIOLOGICOS+" INTEGER PRIMARY KEY " + "AUTOINCREMENT," + COL_2_FISIOLOGICOS +
            " TEXT," + COL_3_FISIOLOGICOS + " TEXT," + COL_4_FISIOLOGICOS + " INTEGER)";

    public DataBaseHelper(Context context) {
        super(context, NOMBRE_BD, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_PACIENTE);
        db.execSQL(CREAR_TABLA_FISIOLOGICOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PACIENTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_FISIOLOGICOS);
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
}
