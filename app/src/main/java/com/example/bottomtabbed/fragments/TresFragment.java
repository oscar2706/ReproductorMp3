package com.example.bottomtabbed.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bottomtabbed.MainActivity;
import com.example.bottomtabbed.R;
import com.example.bottomtabbed.classes.Album;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TresFragment extends Fragment implements MainActivity.OnHeadlineSelectedListener{


    public TresFragment() {
        // Required empty public constructor
    }


    private TextView miTxtV;

    // Vista de artistas (card con su informacion)
    RecyclerView recyclerAlbumes;

    // Lista de artistas
    ArrayList<Album> listaAlbumes;

    // Albumes (atributos (? de cada card con la informacion del artista)
    TextView nombreAlbum;
    TextView artistsSongsQuantity;
    String URL = "";


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_uno);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vistaAlbumes = inflater.inflate(R.layout.fragment_tres, container, false);

        listaAlbumes = new ArrayList<>();
        recyclerAlbumes = (RecyclerView) vistaAlbumes.findViewById(R.id.recyclerID2);




        return vistaAlbumes;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //miTxtV.setText("Fragmento Tres");
    }

    @Override
    public void onArticleSelected(int position) {

    }

    @Override
    public void ordenarListaAlfabeticamente() {

    }

    public void obtenerAlbumesBD(){

    }
}
