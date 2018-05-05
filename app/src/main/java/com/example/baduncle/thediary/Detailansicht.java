package com.example.baduncle.thediary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.baduncle.thediary.Listenansicht;

import java.util.ArrayList;
import java.util.List;

public class Detailansicht extends AppCompatActivity {
    SharedPreferences eintragsspeicher;
    SharedPreferences.Editor eintragseditor;
    List<Datensammler> alledaten;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        eintragsspeicher=getSharedPreferences("Eintragsspeicher",MODE_PRIVATE);
        eintragseditor = eintragsspeicher.edit();
        alledaten = new ArrayList<Datensammler>();
        alledaten = Datensammler.parseEntries(eintragsspeicher.getString("Einträge","0§Beispieltitel§Beispieltext§Beispieluri§01.01.2000§0§0%"));

        super.onCreate(savedInstanceState);
        final Context context=this;
        setContentView(R.layout.activity_detailansicht);
        Intent uebergabe = getIntent();
        Bundle extras = uebergabe.getExtras();
        String eintragsid = extras.getString("eintragsid");
        final int index =extras.getInt("index");
        // Toast.makeText(this,eintragsid,Toast.LENGTH_LONG).show();
        final Datensammler eintrag = Datensammler.parseEntry(eintragsid);
        TextView titel = findViewById(R.id.detailtitel);
        TextView beschreibung = findViewById(R.id.detailbeschreibung);;
        TextView datum = findViewById(R.id.detaildatum);;
        ImageView bild = findViewById(R.id.detailbild);;
        FloatingActionButton detailbearb=  findViewById(R.id.detailbearb);
        FloatingActionButton detailloesch=  findViewById(R.id.detailloesch);

        //Eintrag bearbeiten
        detailbearb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context,NeuerEintrag.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        }) ;

        //Eintrag löschen
        detailloesch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context,Listenansicht.class);
                Toast.makeText(context,"Hallo "+index,Toast.LENGTH_SHORT).show();
                alledaten.remove(index);

                String stringalles="";

                for(int i=0;alledaten.size() > i;i++) {
                    stringalles += alledaten.get(i).toString();
                }
                eintragseditor.putString("Einträge",stringalles);
                eintragseditor.commit();

                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                Toast.makeText(context,"Ein Eintrag wurde erfolgreich gelöscht",Toast.LENGTH_SHORT).show();
            }
        }) ;


        titel.setText(eintrag.getTitel());
        beschreibung.setText(eintrag.getBeschreibung());
        datum.setText(eintrag.getDatum());
        bild.setImageURI(Uri.parse(eintrag.getBilduri()));
    }
}
