package com.example.baduncle.thediary;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

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
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

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

    Handler listhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            alledaten = new ArrayList<Datensammler>();
            alledaten = Datensammler.parseEntries(eintragsspeicher.getString("Einträge","0§Beispieltitel§Beispieltext§android.resource://com.example.baduncle.thediary/drawable/defaultpicture§01.01.2000§0§0%"));
            for(int i=alledaten.size()-1;i>=0;i--){
                adapter.add(alledaten.get(i));
            }
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


        if (!EasyPermissions.hasPermissions(context, galleryPermissions)) {
            EasyPermissions.requestPermissions(this, "Access for storage",
                    101, galleryPermissions);
        }

        //Shows Logo
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //Durch klicken auf Listenitem zur Detailansicht gelangen
        eintragsliste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             //   if(i != 0) {
                    Intent intent = new Intent(context, Detailansicht.class);
                    intent.putExtra("index", i);
                    intent.putExtra("eintragsid", eintragsliste.getItemAtPosition(i).toString());
                    startActivity(intent);
              //  }
            }
        });
        //Zum Löschen aller Elemente auskommentieren
        //eintragseditor.remove("Einträge");
        //eintragseditor.commit();

        listhandler.sendEmptyMessage(0);

       /* for(int i=0;alledaten.size()>i;i++){
            adapter.add(alledaten.get(i));
        } */

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reset_button,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.reset_button:
                resetapp();
                return true;



        }
        return super.onOptionsItemSelected(item);
    }

    private void resetapp() {
        eintragseditor.remove("Einträge");
        eintragseditor.commit();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
