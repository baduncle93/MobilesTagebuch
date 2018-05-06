package com.example.baduncle.thediary;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NeuerEintrag extends AppCompatActivity {

    private TextInputLayout titel;
    private TextInputLayout beschreibung;
    private TextView datum;
    private DatePickerDialog.OnDateSetListener ondatesetlistener;
    final Context context=this;
    Bitmap bitmap;
    ImageView neuesbild;
    FloatingActionButton bildbutton;
    String[] permission = {Manifest.permission.CAMERA};
    private static final int requestcode=123;
    EditText beschreibungstext;
    EditText titeltext;
    SharedPreferences eintragsspeicher;
    SharedPreferences.Editor eintragseditor;
    Uri bilduri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neuereintrag);
        titel = findViewById(R.id.layout2);
        beschreibung = findViewById(R.id.layout1);
        datum = (TextView) findViewById(R.id.datum);
        neuesbild= (ImageView) findViewById(R.id.neuesbild);
        eintragsspeicher = getSharedPreferences("Eintragsspeicher",MODE_PRIVATE);
        eintragseditor = eintragsspeicher.edit();
        beschreibungstext = (EditText) findViewById(R.id.beschreibung);
        titeltext = (EditText) findViewById(R.id.titel);
        bilduri=Uri.parse("android.resource://com.example.baduncle.thediary/drawable/instagram");

        Calendar kalender = Calendar.getInstance();
        final SimpleDateFormat datumsformat = new SimpleDateFormat("dd.MM.yyyy");
        datum.setText(datumsformat.format(kalender.getTime()));

        //Eingabefeld für Datum anzeigen
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

        //Datumsausgabe Formatieren
        ondatesetlistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int jahr, int monat, int tag) {
            monat=monat+1;
            Date date = new Date();
            String datumstring = tag+"."+monat+"."+jahr;
                try {
                    date = datumsformat.parse(datumstring);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datum.setText(datumsformat.format(date));
            }
        };

        //Bildauswahl mit Wählen des Kamerabuttons starten
        neuesbild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bildwahl();
            }
        });

    }

    //Handling für Auswahl eines Bildes von Gallerie/Kamera
    public void bildwahl() {
        final CharSequence[] items = {"Kamera","Gallerie","Abbrechen"};
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        builder.setTitle("Bild hinzufügen");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(items[i].equals("Kamera")){

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(ContextCompat.checkSelfPermission(context,permission[0])==PackageManager.PERMISSION_GRANTED) {
                        startActivityForResult(intent, 1);
                    }
                    else {
                        ActivityCompat.requestPermissions(NeuerEintrag.this,permission,requestcode);
                    }
                }
                else if (items[i].equals("Gallerie")){

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent,"Wähle Bild"),2);
                }
                else if (items[i].equals("Abbrechen")){
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (grantResults[0]==0)
            startActivityForResult(intent, 1);
    }

    //Nach Bildauswahl durch Bediener
    @Override
    protected void onActivityResult(int requestcode,int resultcode,Intent data) {
        super.onActivityResult(requestcode,resultcode,data);
        if (resultcode == Activity.RESULT_OK){
            if(requestcode==1) {
                Bundle bundle = data.getExtras();
                bitmap = (Bitmap) bundle.get("data");
                bilduri = getImageUri(context,bitmap);
                neuesbild.setImageURI(bilduri);
            }
            else if(requestcode==2) {
                bilduri = data.getData();
                neuesbild.setImageURI(bilduri);
            }

        }
    }

    //Überprüfung ob Titelfeld leer ist
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

    //Überprüfung ob Titelfeld leer ist
    private boolean falscheszeichen(){
        String titelstring = titel.getEditText().getText().toString().trim();
        String beschrstring = beschreibung.getEditText().getText().toString().trim();
        if(titelstring.contains("§")||titelstring.contains("%")||beschrstring.contains("%")||beschrstring.contains("§")) {
            titel.setError("Ungültige Zeichen § oder % in Titel oder Beschreibung!");
            return false;
        }
        else {
            titel.setError(null);
            return true;
        }
    }

    //Speichern der Daten, wenn alle Eingaben in Ordnung sind
    public void eingabeok(View v) {
        if(!titelnichtleer()|!falscheszeichen()) {
            return;
        }
        else{
            int sterne=0;
            int preis=0;
            int eintragsid = 1;
            Datensammler eintrag = new Datensammler(eintragsid,titel.getEditText().getText().toString(),beschreibung.getEditText().getText().toString(),bilduri.toString(),datum.getText().toString(),sterne,preis);
            eintragseditor.putString("stringneu",eintrag.toString());
            eintragseditor.commit();

            //zurück zum Hauptscreen
            Intent intent = new Intent(context,Listenansicht.class);
            intent.putExtra("daten",eintrag);
            intent.putExtra("eintragsid",eintragsid);
            intent.putExtra("neuereintrag",true);
            eintragsid++;
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            Toast.makeText(context,"Ein neuer Eintrag wurde erfolgreich erstellt",Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes); // Used for compression rate of the Image : 100 means no compression
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "xyz", null);
        return Uri.parse(path);
    }
}
