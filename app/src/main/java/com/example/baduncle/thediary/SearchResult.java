package com.example.baduncle.thediary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchResult extends AppCompatActivity {

    final Context context=this;
    SharedPreferences eintragsspeicher;
    SharedPreferences.Editor eintragseditor;
    Intent neu;
    Bundle extras;
    TableLayout tl;
    CustomAdapter adapter;
    ListView suchListe;

    int sterne;
    int preis;

    private List<Datensammler> alledaten;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        suchListe = findViewById(R.id.suchListe);
        neu =getIntent();
        extras=neu.getExtras();
        adapter = new CustomAdapter(context,R.layout.custom_row);
        suchListe.setAdapter(adapter);
        eintragsspeicher = getSharedPreferences("Eintragsspeicher",MODE_PRIVATE);
        eintragseditor = eintragsspeicher.edit();

        Intent intent = getIntent();
        preis = intent.getIntExtra("preis", 0);
        sterne = intent.getIntExtra("sterne", 0);

        Toast.makeText(context,"Preis: " + preis + ", Sterne: " + sterne ,Toast.LENGTH_SHORT).show();

        suchListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0) {
                    Intent intent = new Intent(context, Detailansicht.class);
                    intent.putExtra("index", i);
                    intent.putExtra("eintragsid", suchListe.getItemAtPosition(i).toString());
                    startActivity(intent);
                }
            }
        });

        alledaten = new ArrayList<Datensammler>();
        alledaten = Datensammler.parseEntries(eintragsspeicher.getString("Einträge","0§Beispieltitel§Beispieltext§"+ Uri.parse("android.resource://com.example.baduncle.thediary/drawable/defaultpicture")+"§01.01.2000§0§0%"));
        for(int i=0;alledaten.size()>i;i++){
            if(alledaten.get(i).getPreis()>=preis && alledaten.get(i).getSterne()>=sterne)
                adapter.add(alledaten.get(i));
        }

    }


}
