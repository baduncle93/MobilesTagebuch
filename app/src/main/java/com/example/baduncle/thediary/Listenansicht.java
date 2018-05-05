package com.example.baduncle.thediary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Listenansicht extends AppCompatActivity {
    final Context context=this;
    private TextView mTextMessage;
    SharedPreferences eintragsspeicher;
    SharedPreferences.Editor eintragseditor;
    Intent neu;
    Bundle extras;
    TableLayout tl;
    CustomAdapter adapter;
    ListView eintragsliste;
    private List<Datensammler> alledaten;

    //Navigation mittels Navigationsleiste unten
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_liste:
                    mTextMessage.setText("");
                    return true;
                case R.id.navigation_kalender:
                    mTextMessage.setText("");
                    Intent intent2 = new Intent(context,Kalenderansicht.class);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listenansicht);
        FloatingActionButton button=  findViewById(R.id.addbutton_list);
        mTextMessage = (TextView) findViewById(R.id.message);
        eintragsliste = findViewById(R.id.eintragsliste);
        neu =getIntent();
        extras=neu.getExtras();
        adapter = new CustomAdapter(context,R.layout.custom_row);
        eintragsliste.setAdapter(adapter);
        eintragsspeicher = getSharedPreferences("Eintragsspeicher",MODE_PRIVATE);
        eintragseditor = eintragsspeicher.edit();

        //Zum Löschen aller Elemente auskommentieren
       // eintragseditor.remove("Einträge");
       // eintragseditor.commit();

        alledaten = new ArrayList<Datensammler>();
        alledaten = parseEntries(eintragsspeicher.getString("Einträge","0§Beispieltitel§Beispieltext§Beispieluri§01.01.2000§0§0%"));
        for(int i=0;alledaten.size()>i;i++){
            adapter.add(alledaten.get(i));
        }

        //Navigationsleiste initialisieren
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem menuitem= menu.getItem(0);
        menuitem.setChecked(true);

        //Neuer Eintrag Activity mit Add-Button öffnen
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context,NeuerEintrag.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        }) ;
        if (extras != null) {
            if (extras.containsKey("neuereintrag")) {
                neuereihe();
            }
        }

    }

    public void neuereihe() {
        neu =getIntent();
        extras=neu.getExtras();
        int eintragsid= extras.getInt("eintragsid");
        String neuereintrag = eintragsspeicher.getString("stringneu","fail");
        alledaten.add(parseEntry(neuereintrag));
        String stringalles="";

        for(int i=0;alledaten.size() > i;i++) {
           // adapter.add(alledaten.get(i));
            stringalles += alledaten.get(i).toString();
        }
        eintragseditor.putString("Einträge",stringalles);
        eintragseditor.commit();
        Intent intent = new Intent(context,Listenansicht.class);
        startActivity(intent);
    }

   public List<Datensammler> parseEntries(String str) {
        List<Datensammler> list = new ArrayList<Datensammler>();
       String[] grob = str.split("%");
       String[] fein={};
       for(int i=0;i<grob.length;i++) {
           fein=grob[i].split("§");
           Datensammler hilf = new Datensammler();
           hilf.setId(Integer.parseInt(fein[0]));
           hilf.setTitel(fein[1]);
           hilf.setBeschreibung(fein[2]);
           hilf.setBilduri(fein[3]);
           hilf.setDatum(fein[4]);
           hilf.setSterne(Integer.parseInt(fein[5]));
           hilf.setSterne(Integer.parseInt(fein[6]));
           list.add(hilf);
       };
        return list;
    }
    public Datensammler parseEntry(String str) {
        String[] fein = str.split("§");
            Datensammler hilf = new Datensammler();
            hilf.setId(Integer.parseInt(fein[0]));
            hilf.setTitel(fein[1]);
            hilf.setBeschreibung(fein[2]);
            hilf.setBilduri(fein[3]);
            hilf.setDatum(fein[4]);
            hilf.setSterne(Integer.parseInt(fein[5]));
            hilf.setSterne(Integer.parseInt(fein[6].substring(0,fein[6].length()-1)));
        return hilf;
    }
}
