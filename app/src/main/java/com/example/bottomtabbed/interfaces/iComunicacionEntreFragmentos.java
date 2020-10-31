package com.example.bottomtabbed.interfaces;

import com.example.bottomtabbed.classes.Cancion;

import java.util.ArrayList;

public interface iComunicacionEntreFragmentos {

    public void enviarListaCanciones(ArrayList<Cancion> listaDeCanciones);
}
