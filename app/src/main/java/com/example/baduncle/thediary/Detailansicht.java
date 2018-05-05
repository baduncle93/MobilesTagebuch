package com.example.baduncle.thediary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.baduncle.thediary.Listenansicht;

public class Detailansicht extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailansicht);
        Intent uebergabe = getIntent();
        Bundle extras = uebergabe.getExtras();
        String eintragsid = extras.getString("eintragsid");
       // Toast.makeText(this,eintragsid,Toast.LENGTH_LONG).show();
        Datensammler eintrag = Datensammler.parseEntry(eintragsid);
        TextView titel = findViewById(R.id.detailtitel);
        TextView beschreibung = findViewById(R.id.detailbeschreibung);;
        TextView datum = findViewById(R.id.detaildatum);;
        ImageView bild = findViewById(R.id.detailbild);;

        titel.setText(eintrag.getTitel());
        beschreibung.setText(eintrag.getBeschreibung());
        datum.setText(eintrag.getDatum());
        bild.setImageURI(Uri.parse(eintrag.getBilduri()));
    }
}
