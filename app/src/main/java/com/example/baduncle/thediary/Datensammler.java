package com.example.baduncle.thediary;

import android.content.SharedPreferences;
import android.net.Uri;

import java.io.Serializable;

import static android.content.Context.MODE_PRIVATE;

public class Datensammler implements Serializable{

private int id;
private String titel;
private String beschreibung;
private String datum;
private String bilduri;
private int sterne;
private int preis;

    public Datensammler() {
        this.id = 0;
        this.titel = "Vorlage";
        this.beschreibung = "Vorlage";
        this.datum = "Vorlage";
        this.bilduri = "Vorlage";
        this.sterne = 0;
        this.preis = 0;
    }

    public Datensammler(int id, String titel, String beschreibung, String bilduri, String datum, int sterne, int preis) {
        this.id = id;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.datum = datum;
        this.bilduri = bilduri;
        this.sterne = sterne;
        this.preis = preis;

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
            builder.append(this.getId());
            builder.append("§");
            builder.append(this.getTitel());
            builder.append("§");
            builder.append(this.getBeschreibung());
            builder.append("§");
            builder.append(this.getBilduri());
            builder.append("§");
            builder.append(this.getDatum());
            builder.append("§");
            builder.append(this.getSterne());
            builder.append("§");
            builder.append(this.getPreis());
            builder.append("%");

        return builder.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getBilduri() {
        return bilduri;
    }

    public void setBilduri(String bilduri) {
        this.bilduri = bilduri;
    }

    public int getSterne() {
        return sterne;
    }

    public void setSterne(int sterne) {
        this.sterne = sterne;
    }

    public int getPreis() {
        return preis;
    }

    public void setPreis(int preis) {
        this.preis = preis;
    }
}
