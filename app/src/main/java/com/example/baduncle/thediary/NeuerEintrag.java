package com.example.baduncle.thediary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class NeuerEintrag extends AppCompatActivity {

    ImageView neuesbild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neuereintrag);

        neuesbild= (ImageView) findViewById(R.id.neuesbild);
        Button bildbutton= (Button) findViewById(R.id.bildbutton);

        bildbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestcode,int resultcode,Intent data) {
        super.onActivityResult(requestcode,resultcode,data);
        Bitmap bitmap= (Bitmap) data.getExtras().get("data");
        neuesbild.setImageBitmap(bitmap);
    }
}
