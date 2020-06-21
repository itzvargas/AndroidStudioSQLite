package com.example.tareasqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ConsultaG extends AppCompatActivity {

    TextView tv;
    AyudanteBD aBD;
    SQLiteDatabase db=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_g);
        setTitle("Consulta General");

        tv = (TextView)findViewById(R.id.textView2);
        tv.setMovementMethod(new ScrollingMovementMethod());
        try{
            aBD=new AyudanteBD(this,"PostresBD",null,1);
            db = aBD.getReadableDatabase();
            if (db!=null) {
                Cursor cursor = db.rawQuery("SELECT * FROM postres",null);
                int numcol=cursor.getColumnCount();
                int numren=cursor.getCount();
                tv.setText("Cursor con "+numren+" registros\n"+numcol+" columnas\n\n");
                while (cursor.moveToNext()){
                    tv.append("\n"+cursor.getInt(0)+"   " +cursor.getString(1)+"   "
                            +cursor.getDouble(2));
                }//while
                cursor.close();
                db.close();
            }//if
            else
                tv.setText("db fue null");
        }//try
        catch (Exception e) {
            tv.setText("ERROR "+e.getMessage());
        }//catch
    }
}
