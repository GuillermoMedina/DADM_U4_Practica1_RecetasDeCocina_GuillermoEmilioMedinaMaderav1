package mx.edu.ittepic.dadm_u4_practica1_recetasdecocina_guillermoemiliomedinamadera;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper
{
    public BaseDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Se ejecuta cuando la aplicacion se ejecuta en el cel
        //Sirve para construir en el SQLite que esta en el cel las tablas de la App requiere para funcionar
        db.execSQL("CREATE TABLE RECETAS(ID INTEGER PRIMARY KEY, NOMBRE VARCHAR(200), INGREDIENTES VARCHAR(1000), PREPARACION VARCHAR(1000), OBSERVACIONES VARCHAR(500))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //
    }
}
