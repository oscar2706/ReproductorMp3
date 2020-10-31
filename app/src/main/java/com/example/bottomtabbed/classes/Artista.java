package com.example.bottomtabbed.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Artista implements Serializable {
    // Atributos
    public String nombreArtista;
    public String cantidadCanciones;
    public int idImagenArtista;
    public String pathArchivo;

    public ArrayList<Album> discografia;

    // Constructor
    public Artista(String nombreArtista, String cantidadCanciones, int idImagenArtista)  {
        this.nombreArtista = nombreArtista;
        this.cantidadCanciones = cantidadCanciones;
        this.idImagenArtista = idImagenArtista;
    }
    public Artista(){

    }

    // Metodos
    public void setNombreArtista(String nombreArtista){   this.nombreArtista = nombreArtista; }
    public void setCantidadCanciones(String cantidadCanciones){   this.cantidadCanciones = cantidadCanciones; }
    public void setIdImagenArtista(int idImagenArtista){    this.idImagenArtista = idImagenArtista; }
    public void setPathArchivo (String pathArchivo) {   this.pathArchivo = pathArchivo; }
    public void setDiscografia(ArrayList<Album> discografia) { this.discografia = discografia; }

    public String getNombreArtista () { return this.nombreArtista;}
    public String getCantidadCanciones () { return this.cantidadCanciones;}
    public int getIdImagenArtista () { return this.idImagenArtista;}
    public ArrayList<Album> getDiscografia() { return discografia; }


    // Sobre escribimos el metodo equals para saber si un artista es igual a otro solo con su
    // nombre y no con TODOS sus atributos
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Artista)){
            return false;
        }
        Artista other = (Artista) obj;
        return Objects.equals(other.nombreArtista, this.nombreArtista);
    }
}
