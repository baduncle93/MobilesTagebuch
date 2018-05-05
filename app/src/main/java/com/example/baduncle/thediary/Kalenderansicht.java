package com.example.baduncle.thediary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Kalenderansicht extends AppCompatActivity {
    final Context context=this;
    CalendarView cal;
    SharedPreferences eintragsspeicher;
    SharedPreferences.Editor eintragseditor;
    List<Datensammler> alledaten;
    List<Datensammler> anwahldatum;


    //Navigation mittels Navigationsleiste unten
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_liste:

                    Intent intent1 = new Intent(context,Listenansicht.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    return true;
                case R.id.navigation_kalender:

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eintragsspeicher=getSharedPreferences("Eintragsspeicher",MODE_PRIVATE);
        eintragseditor = eintragsspeicher.edit();
        alledaten = new ArrayList<Datensammler>();
        alledaten = Datensammler.parseEntries(eintragsspeicher.getString("Einträge","0§Beispieltitel§Beispieltext§Beispieluri§01.01.2000§0§0%"));

        setContentView(R.layout.activity_kalenderansicht);
        FloatingActionButton button=  findViewById(R.id.addbutton_kal);
        cal= findViewById(R.id.kalender);
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                anwahldatum = new ArrayList<Datensammler>();
                String datum = i2+"."+(i1+1)+"."+i;
                Intent intent = new Intent(context,Detailansicht.class);
                for(Datensammler d:alledaten){
                    if(d.getDatum().equals(datum)) {
                        anwahldatum.add(d);
                    }
                }
                if(anwahldatum.size()==1) {
                    Datensammler eintrag = anwahldatum.get(0);
                    intent.putExtra("index",eintrag.getId());
                    intent.putExtra("eintragsid",eintrag.toString());
                    startActivity(intent);
                }
                if(anwahldatum.size()>1){
                    Toast.makeText(context,"An diesem Tag gibt es mehr als einen Eintrag!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Navigationsleiste initialisieren
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem menuitem= menu.getItem(1);
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
