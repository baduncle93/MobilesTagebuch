package com.example.baduncle.thediary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;

public class SearchActivity extends AppCompatActivity {
    final Context context = this;
    RatingBar sterne;
    RatingBar preis;
    int sternint;
    int preisint;
    TextInputEditText titel;

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
                    Intent intent2 = new Intent(context,Kalenderansicht.class);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    return true;

                case R.id.navigation_search:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Shows Logo
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        sterne =(RatingBar) findViewById(R.id.ratingBar_star);
        preis = (RatingBar) findViewById(R.id.ratingBar_dollar);
        titel = (TextInputEditText) findViewById(R.id.suchtitel);
        sternint=0;
        preisint=0;

        //Navigationsleiste initialisieren
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem menuitem= menu.getItem(2);
        menuitem.setChecked(true);

        sterne.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                sternint = Math.round(v);

            }
        });

        preis.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                preisint = Math.round(v);

            }
        });

    }

    public void suchen(View v) {
        Intent intent4 = new Intent(context,SearchResult.class);
        intent4.putExtra("sterne", sternint);
        intent4.putExtra("preis", preisint);
        intent4.putExtra("titel",titel.getText().toString());
        startActivity(intent4);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

}
