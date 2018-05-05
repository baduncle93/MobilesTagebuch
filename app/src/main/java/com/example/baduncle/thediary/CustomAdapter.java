package com.example.baduncle.thediary;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter{
    List list =new ArrayList<>();

    public CustomAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
static class Datahandler {
        ImageView bild;
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
        handler.bild.setImageURI(Uri.parse(daten.getBilduri()));
        handler.titel.setText(daten.getTitel());
        handler.beschreibung.setText(daten.getBeschreibung());


        return customview;
    }
}

