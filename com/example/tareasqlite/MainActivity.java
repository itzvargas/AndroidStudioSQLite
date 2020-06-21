package com.example.tareasqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    EditText etID,etN,etP;
    Button btnC,btnR,btnU,btnD;
    ImageView ivBorrar, ivCamara;
    AyudanteBD aBD;
    SQLiteDatabase db=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etID = (EditText)findViewById(R.id.etId);
        etN = (EditText)findViewById(R.id.etNombre);
        etP = (EditText)findViewById(R.id.etPrecio);
        btnC = (Button)findViewById(R.id.buttonC);
        btnR = (Button)findViewById(R.id.buttonR);
        btnU = (Button)findViewById(R.id.buttonU);
        btnD = (Button)findViewById(R.id.buttonD);
        ivBorrar = (ImageView) findViewById(R.id.borrar);
        ivCamara = (ImageView)findViewById(R.id.camara);

        btnC.setOnClickListener(this);
        btnR.setOnClickListener(this);
        btnR.setOnLongClickListener(this);
        btnU.setOnClickListener(this);
        btnD.setOnClickListener(this);
        ivBorrar.setOnClickListener(this);
        ivCamara.setOnClickListener(this);

        btnU.setEnabled(false);
        btnD.setEnabled(false);
        /*try{
            aBD=new AyudanteBD(this,"PostresBD",null,1);
            Toast.makeText(this,"PostresBD Lista",Toast.LENGTH_LONG).show();
        }//try
        catch (Exception e)
        {
            Toast.makeText(this,"PostresBD Lista",Toast.LENGTH_LONG).show();
        }*/
    }

    @Override
    public void onClick(View v) {
        String id = etID.getText().toString();
        String nom = etN.getText().toString();
        String pre = etP.getText().toString();
        switch (v.getId()){
            case R.id.buttonC:
                if(!etID.getText().toString().isEmpty() && !etN.getText().toString().isEmpty() && !etP.getText().toString().isEmpty()){
                    try{
                        aBD=new AyudanteBD(this,"PostresBD",null,1);
                        db = aBD.getWritableDatabase();
                        if (db!=null) {
                            db.execSQL("INSERT INTO postres values ("+id+",'"+nom+"',"+pre+")");
                            db.close();
                            Toast.makeText(this,"Registro insertado",Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(this,"No se insertaron los datos",Toast.LENGTH_LONG).show();
                    }//try
                    catch (Exception e)
                    {
                        Toast.makeText(this,"El ID ya existe.\nPrueba con otro identificador.",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(this,"Ingresa todos los datos",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.buttonR:
                if(!etID.getText().toString().isEmpty()){
                    try{
                        aBD=new AyudanteBD(this,"PostresBD",null,1);
                        db = aBD.getReadableDatabase();
                        if (db!=null) {
                            btnU.setEnabled(true);
                            btnD.setEnabled(true);
                            etID.setEnabled(false);
                            Cursor cursor = db.rawQuery("SELECT * FROM postres WHERE id="+id,null);
                            if (cursor.moveToNext()){
                                etID.setText(""+cursor.getInt(0));
                                etN.setText(""+cursor.getString(1));
                                etP.setText(""+cursor.getDouble(2));
                            }
                            else{
                                Toast.makeText(this,"No existe. Ingresa el ID corecto.",Toast.LENGTH_LONG).show();
                                btnU.setEnabled(false);
                                btnD.setEnabled(false);
                                etID.setEnabled(true);
                            }
                            cursor.close();
                            db.close();
                        }//if
                        else
                            Toast.makeText(this,"BD null",Toast.LENGTH_LONG).show();
                    }//try
                    catch (Exception e) {
                        Toast.makeText(this,"No existe. Ingresa el ID corecto.",Toast.LENGTH_LONG).show();
                        btnU.setEnabled(false);
                        btnD.setEnabled(false);
                    }//catch
                }
                else {
                    Toast.makeText(this,"Ingresa el Identificador que desea buscar.",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.buttonU:
                if(!etID.getText().toString().isEmpty() && !etN.getText().toString().isEmpty() && !etP.getText().toString().isEmpty()){
                    try{
                        etID.setEnabled(true);
                        aBD=new AyudanteBD(this,"PostresBD",null,1);
                        db = aBD.getReadableDatabase();
                        if (db!=null) {
                            db.execSQL("UPDATE postres set nombre='"+nom+"',precio="+pre+" WHERE id="+id);
                            db.close();
                            Toast.makeText(this,"Registro modificado.",Toast.LENGTH_LONG).show();
                        }//if
                        else
                            Toast.makeText(this,"BD null",Toast.LENGTH_LONG).show();
                    }//try
                    catch (Exception e) {
                        Toast.makeText(this,"No existe el registro, ingresa el ID correcto.",Toast.LENGTH_LONG).show();
                    }//catch
                }
                else {
                    Toast.makeText(this,"Ingresa todos los datos.",Toast.LENGTH_LONG).show();
                }
                btnU.setEnabled(false);
                btnD.setEnabled(false);
                break;
            case R.id.buttonD:
                if(!etID.getText().toString().isEmpty()){
                    try{
                        aBD=new AyudanteBD(this,"PostresBD",null,1);
                        db = aBD.getReadableDatabase();
                        if (db!=null) {
                            db.execSQL("DELETE FROM postres WHERE id="+id);
                            db.close();
                            Toast.makeText(this,"Registro eliminado.",Toast.LENGTH_LONG).show();
                            etID.setEnabled(true);
                        }//if
                        else
                            Toast.makeText(this,"BD null",Toast.LENGTH_LONG).show();
                    }//try
                    catch (Exception e) {
                        Toast.makeText(this,"No existe el registro, ingresa el ID correcto.",Toast.LENGTH_LONG).show();
                    }//catch
                }
                else {
                    Toast.makeText(this,"Ingresa el Identificador que desea eliminar.",Toast.LENGTH_LONG).show();
                }
                btnU.setEnabled(false);
                btnD.setEnabled(false);
                break;
            case R.id.borrar:
                etID.getText().clear();
                etN.getText().clear();
                etP.getText().clear();
                btnU.setEnabled(false);
                btnD.setEnabled(false);
                break;
            case R.id.camara:
                //Se instancia un objeto de la clase IntentIntegrator
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                //Se procede con el proceso de scaneo
                scanIntegrator.initiateScan();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(v.getId() == R.id.buttonR){
            Intent int1 = new Intent(this,ConsultaG.class);
            startActivity(int1);
        }
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //Se obtiene el resultado del proceso de scaneo y se parsea
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //Quiere decir que se obtuvo resultado pro lo tanto:
            //Desplegamos en pantalla el contenido del código de barra scaneado
            String scanContent = scanningResult.getContents();
            etID.setText("" + scanContent);
            //Desplegamos en pantalla el nombre del formato del código de barra scaneado
            String scanFormat = scanningResult.getFormatName();
            //formatTxt.setText("Formato: " + scanFormat);
        } else {
            //Quiere decir que NO se obtuvo resultado
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No se ha recibido datos del scaneo!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
