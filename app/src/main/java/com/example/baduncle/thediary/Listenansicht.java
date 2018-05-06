package com.example.baduncle.thediary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Listenansicht extends AppCompatActivity {
    final Context context=this;
    private TextView mTextMessage;

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

                case R.id.navigation_search:
                    mTextMessage.setText("");
                    Intent intent3 = new Intent(context,SearchActivity.class);
                    startActivity(intent3);
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
<<<<<<< HEAD
        eintragsliste = findViewById(R.id.eintragsliste);
        neu =getIntent();
        extras=neu.getExtras();
        adapter = new CustomAdapter(context,R.layout.custom_row);
        eintragsliste.setAdapter(adapter);
        eintragsspeicher = getSharedPreferences("Eintragsspeicher",MODE_PRIVATE);
        eintragseditor = eintragsspeicher.edit();

        //Durch klicken auf Listenitem zur Detailansicht gelangen
        eintragsliste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0) {
                    Intent intent = new Intent(context, Detailansicht.class);
                    intent.putExtra("index", i);
                    intent.putExtra("eintragsid", eintragsliste.getItemAtPosition(i).toString());
                    startActivity(intent);
                }
            }
        });
        //Zum Löschen aller Elemente auskommentieren
       // eintragseditor.remove("Einträge");
       // eintragseditor.commit();

        alledaten = new ArrayList<Datensammler>();
        alledaten = Datensammler.parseEntries(eintragsspeicher.getString("Einträge","0§Beispieltitel§Beispieltext§"+Uri.parse("android.resource://com.example.baduncle.thediary/drawable/instagram").toString()+"§01.01.2000§0§0%"));
        for(int i=0;alledaten.size()>i;i++){
            adapter.add(alledaten.get(i));
        }
=======
>>>>>>> parent of 321342a... Merge branch 'master' of https://github.com/baduncle93/MobilesTagebuch

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
    }

}
