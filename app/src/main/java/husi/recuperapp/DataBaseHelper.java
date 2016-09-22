package husi.recuperapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by jmss1 on 22/09/2016.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String NOMBRE_BD = "RecuperapDP.db";
    public static final String TABLA_PACIENTE = "tabla_paciente";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "USUARIO";
    public static final String COL_3 = "CONTRASENA";
    public static final String COL_4 = "EMAIL";

    public DataBaseHelper(Context context) {
        super(context, NOMBRE_BD, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLA_PACIENTE + " ("+COL_1+" INTEGER PRIMARY KEY " +
                "AUTOINCREMENT," + COL_2 + " TEXT," + COL_3 + " TEXT," + COL_4 + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PACIENTE);
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

    public boolean insertarUnPaciente(String usuario, String contrasena, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, usuario);
        contentValues.put(COL_3, contrasena);
        contentValues.put(COL_4, email);
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
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, usuario);
        contentValues.put(COL_3, contrasena);
        contentValues.put(COL_4, email);
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
}
