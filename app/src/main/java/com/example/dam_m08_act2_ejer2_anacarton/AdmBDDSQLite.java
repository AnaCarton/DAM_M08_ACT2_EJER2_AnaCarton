package com.example.dam_m08_act2_ejer2_anacarton;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//SQLite es una BDD relacional ligera disponible en el SO Android.
// Permite almacenar info en un único archivo y después se puede recuperar
// mediante consultas SQL. Para programar apps que utilizan SQLite se utiliza
//el patrón de diseño Modelo Vista Controlador.

//La bdd usada por SqLite se guarda en el cliente que ejecuta la APP
// no pueden ser consultadas desde otros móviles.

//Para crear una bdd creamos  una nueva clase que herede de SQLiteOpenHelper
public class AdmBDDSQLite extends SQLiteOpenHelper {
    public AdmBDDSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Creamos la tabla con las columnas "id", "currency" y "ratio"
        sqLiteDatabase.execSQL("create table conversor(id,Currency,Ratio)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
