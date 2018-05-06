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

public class Kalenderansicht extends AppCompatActivity {
    final Context context = this;
    private TextView mTextMessage;

    //Navigation mittels Navigationsleiste unten
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_liste:
                    mTextMessage.setText("");
                    Intent intent1 = new Intent(context,Listenansicht.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    return true;
                case R.id.navigation_kalender:
                    mTextMessage.setText("");
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
        setContentView(R.layout.activity_kalenderansicht);
        FloatingActionButton button=  findViewById(R.id.addbutton_kal);
        mTextMessage = (TextView) findViewById(R.id.message);

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
