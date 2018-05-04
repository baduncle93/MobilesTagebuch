package com.example.baduncle.thediary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Listenansicht extends AppCompatActivity {
    final Context context=this;
    private TextView mTextMessage;
    SharedPreferences eintragsspeicher;
    Intent neu;
    Bundle extras;

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
        eintragsspeicher = getSharedPreferences("Eintragsspeicher",MODE_PRIVATE);
        SharedPreferences.Editor eintragseditor = eintragsspeicher.edit();
        neu =getIntent();
        extras=neu.getExtras();

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
        int eintragsid= extras.getInt("eintragsid");
        TableLayout tl=(TableLayout) findViewById(R.id.eintragstable);
        TableRow tr = new TableRow(context);
        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView tittext=new TextView(context);
        tittext.setWidth(100);// give how much you need
        tittext.setText(eintragsspeicher.getString("titel "+eintragsid,"fail"));
        TextView beschtext=new TextView(context);
        beschtext.setWidth(100);// give how much you need
        beschtext.setText(eintragsspeicher.getString("beschreibung "+eintragsid,"fail"));
        ImageView einbild=new ImageView(context);
       // einbild.setImageBitmap();
//set your text
        tr.addView(tittext);
        tr.addView(beschtext);

        ImageView iv=new ImageView(this);
// set your image
        tr.addView(iv);

        tl.addView(tr);

    }

}
