package com.example.bottomtabbed.classes;

import java.util.ArrayList;
import java.util.Objects;

public class Album {

    // Atributos
    public String nombreAlbum;
    public String nombreArtista;
    public String agnoLanzamiento;
    public ArrayList<Cancion> pistasDisco;
    public String cantidadCanciones;
    public String duracionDisco;

    public Album(String nombreAlbum, String nombreArtista, String agnoLanzamiento, ArrayList<Cancion> pistasDisco, String cantidadCanciones, String duracionDisco) {
        this.nombreAlbum = nombreAlbum;
        this.nombreArtista = nombreArtista;
        this.agnoLanzamiento = agnoLanzamiento;
        this.pistasDisco = pistasDisco;
        this.cantidadCanciones = cantidadCanciones;
        this.duracionDisco = duracionDisco;
    }

    public Album() {
        this.nombreAlbum = "";
        this.nombreArtista = "";
        this.agnoLanzamiento = "";
        this.cantidadCanciones = "";
        this.duracionDisco = "";
    }

    // Metodos de get y set
    public String getNombreAlbum() { return nombreAlbum;}
    public void setNombreAlbum(String nombreAlbum) { this.nombreAlbum = nombreAlbum; }

    public String getNombreArtista() { return nombreArtista; }
    public void setNombreArtista(String nombreArtista) { this.nombreArtista = nombreArtista; }

    public String getAgnoLanzamiento() { return agnoLanzamiento; }
    public void setAgnoLanzamiento(String agnoLanzamiento){this.agnoLanzamiento = agnoLanzamiento;}

    public ArrayList<Cancion> getPistasDisco() { return pistasDisco; }
    public void setPistasDisco(ArrayList<Cancion> pistasDisco) { this.pistasDisco = pistasDisco; }

    public String getCantidadCanciones() { return cantidadCanciones; }
    public void setCantidadCanciones(String cantidadCanciones) {
        this.cantidadCanciones = cantidadCanciones; }

    public String getDuracionDisco() { return duracionDisco; }
    public void setDuracionDisco(String duracionDisco) { this.duracionDisco = duracionDisco; }

    // Sobre escribimos el metodo equals para saber si un album es igual a otro solo con su
    // nombre y no con TODOS sus atributos
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Album)){
            return false;
        }
        Album other = (Album) obj;
        return Objects.equals(other.nombreAlbum, this.nombreAlbum);
    }
}
