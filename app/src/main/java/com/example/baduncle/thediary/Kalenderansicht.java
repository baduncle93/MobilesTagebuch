package com.example.baduncle.thediary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Kalenderansicht extends AppCompatActivity {
    final Context context=this;
    CompactCalendarView cal;
    SharedPreferences eintragsspeicher;
    SharedPreferences.Editor eintragseditor;
    List<Datensammler> alledaten;
    List<Datensammler> anwahldatum;
    SimpleDateFormat formatmonat = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    //Navigation mittels Navigationsleiste unten
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_liste:

                    Intent intent1 = new Intent(context,Listenansicht.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    return true;
                case R.id.navigation_kalender:

                    return true;

                case R.id.navigation_search:

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
        eintragsspeicher=getSharedPreferences("Eintragsspeicher",MODE_PRIVATE);
        eintragseditor = eintragsspeicher.edit();
        alledaten = new ArrayList<Datensammler>();
        alledaten = Datensammler.parseEntries(eintragsspeicher.getString("Einträge","0§Beispieltitel§Beispieltext§Beispieluri§01.01.2000§0§0%"));
        final ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(false);
        actionbar.setTitle(formatmonat.format(new Date()));
        setContentView(R.layout.activity_kalenderansicht);
        FloatingActionButton button=  findViewById(R.id.addbutton_kal);
        cal= findViewById(R.id.kalender);
        cal.setUseThreeLetterAbbreviation(true);
        cal.setLocale(TimeZone.getDefault(),Locale.GERMANY);

        //Shows Logo
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //Events in Kalender eintragen
        for(Datensammler d:alledaten) {
            Date datum = new Date();
            try {
                datum = format.parse(d.getDatum());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long eventtime = datum.getTime();
            Event ev= new Event(Color.BLUE,eventtime,d.getTitel());
            cal.addEvent(ev);
        }

        //Durch Auswahl eines Datums im Kalender eine Detailansicht öffnen
        cal.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                anwahldatum = new ArrayList<Datensammler>();
                String hilfsstring = format.format(dateClicked);
                Intent intent = new Intent(context,Detailansicht.class);
                for(Datensammler d:alledaten){
                    if(d.getDatum().equals(hilfsstring)) {
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

            //Monatsanzeige beim Swipen verändern
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
            actionbar.setTitle(formatmonat.format(firstDayOfNewMonth));
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
