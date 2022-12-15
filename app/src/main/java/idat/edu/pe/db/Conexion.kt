package idat.edu.pe.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class Conexion(var context: Context) : SQLiteOpenHelper(context, "card", null ,1) {
    override fun onCreate(p0: SQLiteDatabase?) {
     var tableCard = " CREATE TABLE card (id Integer not null primary key autoincrement, id_producto INTEGER, cantidad Double)"
        p0?.execSQL(tableCard)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}