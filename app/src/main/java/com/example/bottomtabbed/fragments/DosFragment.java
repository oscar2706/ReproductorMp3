package com.example.bottomtabbed.fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bottomtabbed.MainActivity;
import com.example.bottomtabbed.R;
import com.example.bottomtabbed.adapters.ArtistsRecyclerViewAdapter;
import com.example.bottomtabbed.adapters.RecyclerViewAdapter;
import com.example.bottomtabbed.classes.Artista;
import com.example.bottomtabbed.classes.FragmentListener;
import com.example.bottomtabbed.classes.MusicPlayer;
import com.example.bottomtabbed.classes.ServidorBD;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DosFragment extends Fragment implements MainActivity.OnHeadlineSelectedListener{



    private static final String ARG_ARTISTAS = "listaArtistas";
    private String listaArtistasJSON;
    public static DosFragment newInstance(String listaArtistasJSON){
        //Log.i("RECIBIMIENTO DE PARAMETROS 2!", " RECIBIENDO...");
        DosFragment dosFragment = new DosFragment();
        Bundle bundleObject = new Bundle();
        //bundleObject.putSerializable(ARG_LISTA, listaRecibida);
        bundleObject.putString(ARG_ARTISTAS, listaArtistasJSON);
        dosFragment.setArguments(bundleObject);
        //Log.i("RECIBIMIENTO DE PARAMETROS 2!", " ESTABLECIDO!!");
        return dosFragment;
    }

    public DosFragment() {
        // Required empty public constructor
    }

    // Vista de artistas (card con su informacion)
    RecyclerView recyclerArtistas;

    // Lista de artistas
    ArrayList<Artista> listaArtistas;

    // Artistas (atributos (? de cada card con la informacion del artista)
    TextView artistName;
    TextView artistsSongsQuantity;
    //ImageView artistPhoto;

    private TextView miTxtV;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Lo que normalmente iría en "onCreate" va en "onCreateView"
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View vistaArtistas = inflater.inflate(R.layout.fragment_dos, container, false);

        listaArtistas = new ArrayList<>();
        recyclerArtistas = (RecyclerView) vistaArtistas.findViewById(R.id.recyclerID2);


        Gson gson;
        gson = new Gson();


        if(getArguments() != null){
            listaArtistasJSON = getArguments().getString("listaArtistas");
            //Log.i("RECIBIMIENTO DE PARAMETROS JSON 2:", listaArtistasJSON);
            Type cancionType = new TypeToken<ArrayList<Artista>>(){}.getType();
            ArrayList<Artista> miLista = gson.fromJson(listaArtistasJSON, cancionType);
            listaArtistas = miLista;
        }


        //Los fragmentos no utilizan 'MainActivity.this', sino 'getContext()'
        recyclerArtistas.setLayoutManager(new LinearLayoutManager(getContext()));

        //cargarLista();

        ArtistsRecyclerViewAdapter artistsRecyclerViewAdapter= new ArtistsRecyclerViewAdapter(listaArtistas);
        // Listener para click para SELECCIONAR un artista

        artistsRecyclerViewAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //CAncionesXArtista:
                String SELECCION = listaArtistas.get(recyclerArtistas.
                        getChildAdapterPosition(view)).getNombreArtista();

                Toast.makeText(getContext(),
                        "Seleccion: "+ listaArtistas.
                                get(recyclerArtistas.getChildAdapterPosition(view)).getNombreArtista(),
                        Toast.LENGTH_SHORT).show();


                // Crear fragmento detalle playlist, pasar nom y mostrar canciones
                // Aquí se crea el fragmento con el efecto split para mostrar las canciones
                // que contiene alguna playlist
                FragmentManager manager = getActivity().getSupportFragmentManager();
                CancionesArtista fragment = new CancionesArtista (SELECCION);
                manager.beginTransaction()
                        .setCustomAnimations(R.anim.entrar_der_a_izq, R.anim.salir_der_a_izq,
                                R.anim.entrar_izq_a_der, R.anim.salir_izq_a_der)
                        .replace(R.id.viewpagerF2, fragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });
        recyclerArtistas.setAdapter(artistsRecyclerViewAdapter);


        return vistaArtistas;
    }

    private void cargarLista() {

        Log.i("Errores *****", "Llamada a CARGAR LISTA FICTICIA  (Creacion de ARTISTAS)" );

        listaArtistas.add(new Artista( "Alkilados", "2", R.drawable.artistas));
        listaArtistas.add(new Artista( "JustinBieber", "3",  R.drawable.artistas));
        listaArtistas.add(new Artista( "MiguelBosé", "2", R.drawable.artistas));
        listaArtistas.add(new Artista( "Technicolor Fabrics", "5", R.drawable.artistas));
        listaArtistas.add(new Artista( "Sean Paul", "3", R.drawable.artistas));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onArticleSelected(int position) {

    }

    @Override
    public void ordenarListaAlfabeticamente() {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);

    }
}
