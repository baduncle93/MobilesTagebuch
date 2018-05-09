package com.example.baduncle.thediary;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;
import com.master.glideimageview.GlideImageView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter{
    List list =new ArrayList<>();

    public CustomAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
static class Datahandler {
        GlideImageView bild;
        TextView titel;
        TextView beschreibung;
}
    public void add(Object object){
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View customview = convertView;
        Datahandler handler;
        if(customview == null) {
            LayoutInflater inflator = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            customview = inflator.inflate(R.layout.custom_row,parent,false);
            handler = new Datahandler();
            handler.titel =customview.findViewById(R.id.titelid);
            handler.beschreibung =customview.findViewById(R.id.beschreibungid);
            handler.bild =customview.findViewById(R.id.bildid);
            customview.setTag(handler);

        }
        else {
            handler = (Datahandler) customview.getTag();
        }


        Datensammler daten ;
        daten=(Datensammler) this.getItem(position);
        //Bitmap hilf = BitmapFactory.decodeFile(getPath(Uri.parse(daten.getBilduri())));
        InputStream stream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        try {
            stream =getContext().getContentResolver().openInputStream(Uri.parse(daten.getBilduri()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap hilf = BitmapFactory.decodeStream(stream);
        BitmapFactory.Options opt= new BitmapFactory.Options();
        opt.inScaled = true;
        opt.inSampleSize = 5;
        opt.inDensity = hilf.getDensity();
        opt.inTargetDensity = 130*opt.inSampleSize;

        if(daten.getBilduri().equals("android.resource://com.example.baduncle.thediary/drawable/defaultpicture")){
            handler.bild.setImageURI(Uri.parse(daten.getBilduri()));
        }
        else {
           // Bitmap bitmap = BitmapFactory.decodeFile(daten.getBilduri(),opt);
            Bitmap bitmap = BitmapFactory.decodeFile(getPath(Uri.parse(daten.getBilduri())),opt);
            handler.bild.setImageBitmap(bitmap);
        }
        //handler.bild.setImageURI(Uri.parse(daten.getBilduri()));
        handler.titel.setText(daten.getTitel());
        handler.beschreibung.setText(daten.getBeschreibung());


        return customview;
    }

    private String getPath(Uri uri) {
        if(uri.toString().contains("NoteLookPicture")){
            return uri.toString().substring(6);
        }
        if(uri.toString().contains("drawable")){
            Log.d("NULLPOINTER","string="+uri.toString());
            return uri.toString();
        }
        else{
            String[]  data = { MediaStore.Images.Media.DATA };
            CursorLoader loader = new CursorLoader(getContext(), uri, data, null, null, null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
    }
}

