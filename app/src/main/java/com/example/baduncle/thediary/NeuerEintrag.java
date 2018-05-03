package com.example.baduncle.thediary;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class NeuerEintrag extends AppCompatActivity {

    final Context context=this;
    ImageView neuesbild;
    String[] permission = {Manifest.permission.CAMERA};
    private static final int requestcode=123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neuereintrag);

        neuesbild= (ImageView) findViewById(R.id.neuesbild);
        FloatingActionButton bildbutton=  findViewById(R.id.bildbutton);


            bildbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(ContextCompat.checkSelfPermission(context,permission[0])==PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(intent, 0);
                    }
                    else {
                        ActivityCompat.requestPermissions(NeuerEintrag.this,permission,requestcode);
                    }
                }
            });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (grantResults[0]==0)
            startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestcode,int resultcode,Intent data) {
        super.onActivityResult(requestcode,resultcode,data);
        Bitmap bitmap= (Bitmap) data.getExtras().get("data");
        neuesbild.setImageBitmap(bitmap);
    }


}
