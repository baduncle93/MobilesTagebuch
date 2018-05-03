package com.example.baduncle.thediary;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class NeuerEintrag extends AppCompatActivity {


    private TextInputLayout titel;
    private TextInputLayout beschreibung;
    private TextView datum;
    private DatePickerDialog.OnDateSetListener ondatesetlistener;
    final Context context=this;
    Bitmap bitmap;
    ImageView neuesbild;
    String[] permission = {Manifest.permission.CAMERA};
    private static final int requestcode=123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neuereintrag);
        titel = findViewById(R.id.layout2);
        beschreibung = findViewById(R.id.layout1);

        datum = (TextView) findViewById(R.id.datum);
        datum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal= Calendar.getInstance();
                int jahr=cal.get(Calendar.YEAR);
                int monat=cal.get(Calendar.MONTH);
                int tag=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog= new DatePickerDialog(context,android.R.style.Theme_Holo_Light_Dialog,ondatesetlistener,jahr,monat,tag);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        ondatesetlistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int jahr, int monat, int tag) {
            monat=monat+1;
            String datumstring = tag+"."+monat+"."+jahr;
            datum.setText(datumstring);
            }
        };

        neuesbild= (ImageView) findViewById(R.id.neuesbild);
        FloatingActionButton bildbutton=  findViewById(R.id.bildbutton);


            bildbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(ContextCompat.checkSelfPermission(context,permission[0])==PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(intent, 0);
                    }
                    else {
                        ActivityCompat.requestPermissions(NeuerEintrag.this,permission,requestcode);
                    }
                }
            });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (grantResults[0]==0)
            startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestcode,int resultcode,Intent data) {
        super.onActivityResult(requestcode,resultcode,data);
        bitmap= (Bitmap) data.getExtras().get("data");
        neuesbild.setImageBitmap(bitmap);
    }
    private boolean titelnichtleer(){
        String titelstring = titel.getEditText().getText().toString().trim();
        if(titelstring.isEmpty()) {
            titel.setError("Es muss immer ein Titel vergeben werden!");
            return false;
        }
        else {
            titel.setError(null);
            return true;
        }
}
    public void eingabeok(View v) {
        if(!titelnichtleer()) {
            return;
        }
        else{
            String titelspeichern=titel.getEditText().getText().toString();
            String beschreibungspeichern=beschreibung.getEditText().getText().toString();

        }
    }

}
