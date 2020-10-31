package com.example.bottomtabbed.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bottomtabbed.MainActivity;
import com.example.bottomtabbed.R;
import com.example.bottomtabbed.adapters.ArtistsRecyclerViewAdapter;
import com.example.bottomtabbed.adapters.PlaylistsRecyclerViewAdapter;
import com.example.bottomtabbed.classes.Artista;
import com.example.bottomtabbed.classes.Cancion;
import com.example.bottomtabbed.classes.Playlist;
import com.example.bottomtabbed.classes.ServidorBD;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CuatroFragment extends Fragment implements MainActivity.OnHeadlineSelectedListener{

    // Lista de playlists del usuario
    ArrayList<Playlist> playlistsUsuario;

    // Vista de playlists (card con su informacion)
    RecyclerView recyclerPlaylists;
    PlaylistsRecyclerViewAdapter playlistsRecyclerViewAdapter;

    private TextView miTxtV;

    RequestQueue requestQueue;


    // Creacion del fragmento junto con su playlist.
    private static final String ARG_PLAYLIST = "listaPlaylists";
    private String listaPlaylistsJSON;
    public static CuatroFragment newInstance(String listaJSON){
        //Log.i("RECIBIMIENTO DE PARAMETROS! 4", " RECIBIENDO...");
        CuatroFragment cuatroFragment = new CuatroFragment();
        Bundle bundleObject = new Bundle();
        //bundleObject.putSerializable(ARG_LISTA, listaRecibida);
        bundleObject.putString(ARG_PLAYLIST, listaJSON);
        cuatroFragment.setArguments(bundleObject);
        Log.i("RECIBIMIENTO DE PARAMETROS! 4", " ESTABLECIDO!!");
        return cuatroFragment;
    }

    public CuatroFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_uno);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View vistaPlaylists = inflater.inflate(R.layout.fragment_cuatro, container, false);
        playlistsUsuario = new ArrayList<>();
        recyclerPlaylists = (RecyclerView) vistaPlaylists.findViewById(R.id.recyclerIDPlaylists);

        //obtenerPlaylistsBD(getContext(), "");

        Gson gson;
        gson = new Gson();

        if(getArguments() != null){
            listaPlaylistsJSON = getArguments().getString("listaPlaylists");
            Type playlistType = new TypeToken<ArrayList<Playlist>>(){}.getType();
            ArrayList<Playlist> miLista = gson.fromJson(listaPlaylistsJSON, playlistType);

            //  H E R E
            //playlistsUsuario = miLista;
            obtenerPlaylistsBD(getContext(), "");
            //Log.i("JSON PLAYLISTS: ", listaPlaylistsJSON);
        }

        // Si tenemos al menos la playlist de favoritos
        if (playlistsUsuario != null){
            /*
            //Los fragmentos no utilizan 'MainActivity.this', sino 'getContext()'
            recyclerPlaylists.setLayoutManager(new LinearLayoutManager(getContext()));

            //cargarLista();

            playlistsRecyclerViewAdapter = new PlaylistsRecyclerViewAdapter(playlistsUsuario);
            // Listener para click para SELECCIONAR un artista

            playlistsRecyclerViewAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(),
                            "Seleccion: "+ playlistsUsuario.
                                    get(recyclerPlaylists.getChildAdapterPosition(view)).getNombrePlaylist(),
                            Toast.LENGTH_SHORT).show();

                    // Convertimos la playlist elegida en String
                    String jsonPlaylistElegida = new Gson().toJson(
                            playlistsUsuario.get(recyclerPlaylists.getChildAdapterPosition(view)));
                    //Log.i("PLAYLIST ELEGIDA: ", jsonPlaylistElegida);

                    // Crear fragmento detalle playlist, pasar nom y mostrar canciones
                    // Aquí se crea el fragmento con el efecto split para mostrar las canciones
                    // que contiene alguna playlist
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    CancionesPlaylist fragment = CancionesPlaylist.newInstance(jsonPlaylistElegida);
                    manager.beginTransaction()
                            .setCustomAnimations(R.anim.entrar_der_a_izq, R.anim.salir_der_a_izq,
                                    R.anim.entrar_izq_a_der, R.anim.salir_izq_a_der)
                            .replace(R.id.viewpagerF4, fragment)
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();


                }
            });
            recyclerPlaylists.setAdapter(playlistsRecyclerViewAdapter);*/
        }

        return vistaPlaylists;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        // Boton para abrir las cancinoes de la playlist
        /*getView().findViewById(R.id.botonSplit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                CancionesPlaylist fragment = new CancionesPlaylist();
                manager.beginTransaction()
                        .setCustomAnimations(R.anim.entrar_der_a_izq, R.anim.salir_der_a_izq,
                                R.anim.entrar_izq_a_der, R.anim.salir_izq_a_der)
                        .replace(R.id.viewPager, fragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });*/

        //miTxtV.setText("Fragmento Tres");
    }

    @Override
    public void onArticleSelected(int position) {

    }

    @Override
    public void ordenarListaAlfabeticamente() {

    }


    // Obtener todas las playlists
    public void obtenerPlaylistsBD (Context context, String filtro){

        String URL = ServidorBD.direccionLocal+"/reproductormp3/leer_todas_playlists.php";

        final Gson gson;
        gson = new Gson();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //Log.i("Nombre playlist" + i, jsonObject.getString("nombrePlaylist"));
                        //Log.i("Cantidad canciones:" + i, jsonObject.getString("numCancionesPlaylist"));

                        String nombrePlaylist = jsonObject.getString("nombrePlaylist");
                        String cantidadCanciones = jsonObject.getString("numCancionesPlaylist");

                        Playlist playlist = new Playlist();
                        playlist.setNombrePlaylist(nombrePlaylist);
                        playlist.setCantidadCanciones(cantidadCanciones);

                        Log.i("Nombre playlist" + i, nombrePlaylist);
                        Log.i("Cantidad canciones:" + i, cantidadCanciones);

                        playlistsUsuario.add(playlist);

                    } catch (JSONException e) {
                        Log.i("Exception!", e.getMessage());
                    }
                }
                // I C I

                //Los fragmentos no utilizan 'MainActivity.this', sino 'getContext()'
                recyclerPlaylists.setLayoutManager(new LinearLayoutManager(getContext()));

                //cargarLista();

                playlistsRecyclerViewAdapter = new PlaylistsRecyclerViewAdapter(playlistsUsuario);
                // Listener para click para SELECCIONAR un artista

                playlistsRecyclerViewAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String SELECCION = playlistsUsuario.
                                get(recyclerPlaylists.getChildAdapterPosition(view)).getNombrePlaylist();
                        Toast.makeText(getContext(),
                                "Seleccion: "+ SELECCION,
                                Toast.LENGTH_SHORT).show();

                        // Convertimos la playlist elegida en String
                        String jsonPlaylistElegida = new Gson().toJson(
                                playlistsUsuario.get(recyclerPlaylists.getChildAdapterPosition(view)));
                        //Log.i("PLAYLIST ELEGIDA: ", jsonPlaylistElegida);

                        // Crear fragmento detalle playlist, pasar nom y mostrar canciones
                        // Aquí se crea el fragmento con el efecto split para mostrar las canciones
                        // que contiene alguna playlist
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        CancionesPlaylist fragment = new CancionesPlaylist (SELECCION);
                        manager.beginTransaction()
                                .setCustomAnimations(R.anim.entrar_der_a_izq, R.anim.salir_der_a_izq,
                                        R.anim.entrar_izq_a_der, R.anim.salir_izq_a_der)
                                .replace(R.id.viewpagerF4, fragment)
                                .addToBackStack(null)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();


                    }
                });
                recyclerPlaylists.setAdapter(playlistsRecyclerViewAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERRRROOOR!: ", error.toString());
            }
        });
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

}

/* Plantilla fragment
package com.example.bottomtabbed.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bottomtabbed.MainActivity;
import com.example.bottomtabbed.R;


public class CuatroFragment extends Fragment implements MainActivity.OnHeadlineSelectedListener{


    public CuatroFragment() {
        // Required empty public constructor
    }


    private TextView miTxtV;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_uno);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View vistaF4 = inflater.inflate(R.layout.fragment_cuatro, container, false);
        this.miTxtV = (TextView) vistaF4.findViewById(R.id.textoB);

        return vistaF4;
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
}



*/
