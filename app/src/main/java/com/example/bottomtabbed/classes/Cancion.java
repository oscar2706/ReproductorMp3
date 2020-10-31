package com.example.bottomtabbed.classes;

// Librerías para utilización de JSON
import android.print.PrinterId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;

public class Cancion implements Serializable {
    // Atributos
    private String path;
    public String album;
    private String title;
    private String artist;
    private String duration;

    public Cancion() {
    }

    public Cancion (String path, String title, String artist, String album, String duration){
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
    }

    // Metodos
    public void setAlbum(String album){     this.album = album; }
    public void setPath(String path){   this.path = path; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTitle () { return this.title;}
    public String getArtist() { return artist; }

    public String getAlbum () { return this.album;}
    public String getPath() { return path; }
    public String getDuration() { return duration; }

    // Sobre escribimos el metodo equals para saber si una cancion es igual a otra solo con el
    // nombre y no con TODOS sus atributos
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cancion)){
            return false;
        }
        Cancion other = (Cancion) obj;
        return Objects.equals(other.title, this.title);
    }
}
