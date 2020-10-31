package com.example.bottomtabbed.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class Playlist implements Serializable {

    ArrayList<Cancion> cancionesPlaylist = new ArrayList<>();
    String nombrePlaylist;
    String cantidadCanciones;


    public Playlist(ArrayList<Cancion> cancionesPlaylist, String nombrePlaylist) {

        this.cancionesPlaylist = cancionesPlaylist;
        this.nombrePlaylist = nombrePlaylist;
        if (this.cancionesPlaylist != null) {
            this.cantidadCanciones = String.valueOf( cancionesPlaylist.size() );
        } else {
            this.cantidadCanciones = "0";
        }
    }

    public Playlist() {
        this.cantidadCanciones = null;
        this.nombrePlaylist = "";
        this.cantidadCanciones = "0";
    }

    // Lista de canciones
    public ArrayList<Cancion> getCancionesPlaylist() {
        return cancionesPlaylist;
    }
    public void setCancionesPlaylist(ArrayList<Cancion> cancionesPlaylist) {
        this.cancionesPlaylist = cancionesPlaylist;
        if (this.cancionesPlaylist != null) {
            this.cantidadCanciones = String.valueOf( cancionesPlaylist.size() );
        } else {
            this.cantidadCanciones = "0";
        }
    }

    // Nombre playlist
    public void setNombrePlaylist (String nombrePlaylist){ this.nombrePlaylist = nombrePlaylist; }
    public String getNombrePlaylist (){ return nombrePlaylist;}

    // Cantidad de canciones
    public void setCantidadCanciones (String cantidadCanciones) {
        this.cantidadCanciones = cantidadCanciones; }
    public String getCantidadCanciones (){return cantidadCanciones;}

    //

    // Metodos para agregar/eliminar canciones de la playlist
    public void agregarCancion(Cancion nuevaCancion){
        this.cancionesPlaylist.add(nuevaCancion);
        String auxiliarCantidad = String.valueOf( (this.cancionesPlaylist.size())+1 );
        this.cantidadCanciones = (auxiliarCantidad);
    }

    public void eliminarCancion(Cancion eliminaCancion){
        this.cancionesPlaylist.remove(eliminaCancion);
    }
}
