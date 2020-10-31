package com.example.bottomtabbed.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bottomtabbed.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.bottomtabbed.adapters.SongsPlaylistRVAdapter;
import com.example.bottomtabbed.classes.Cancion;
import com.example.bottomtabbed.classes.FragmentListener;
import com.example.bottomtabbed.classes.MusicPlayer;
import com.example.bottomtabbed.classes.Playlist;
import com.example.bottomtabbed.classes.ServidorBD;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class CancionesArtista extends Fragment implements MainActivity.OnHeadlineSelectedListener{

    private final MusicPlayer musicPlayer = MusicPlayer.getInstance();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof FragmentListener) {
            fragmentInteractionListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + "Must implement");
        }
    }

    public FragmentListener fragmentInteractionListener;

    // Lista de canciones de la playlist
    ArrayList<Cancion> listaCancionesPlaylist;

    // Vista de cancionesXplaylist (card con su informacion)
    RecyclerView recyclerCancionesPlaylist;

    // Variables y metodo para inicializar el fragmento
    // (Playlist e inicialización del fragmento)
    // Creacion del fragmento junto con su playlist.
    private static final String ARG_CANCIONESPLAYLIST = "listaCancionesPlaylist";
    private String listaCancionesPlaylistJSON;
    public static com.example.bottomtabbed.fragments.CancionesPlaylist newInstance(String listaCPJSON){
        //Log.i("RECIBIMIENTO DE PARAMETROS! CP", " RECIBIENDO...");
        com.example.bottomtabbed.fragments.CancionesPlaylist cancionesPlaylistFragment = new com.example.bottomtabbed.fragments.CancionesPlaylist("");
        Bundle bundleObject = new Bundle();
        bundleObject.putString(ARG_CANCIONESPLAYLIST, listaCPJSON);
        cancionesPlaylistFragment.setArguments(bundleObject);
        Log.i("RECIBIMIENTO DE PARAMETROS! CP", " ESTABLECIDO!!");
        return cancionesPlaylistFragment;
    }

    // TextView para mostrar info de la playlist
    TextView miTXTVP;
    TextView miTXTVC;
    // Button para regresar en transicion a ver la lista de playlists
    ImageButton botonRegresarPlayListSplit;

    String artistaSeleccionado;
    RequestQueue requestQueue;

    public CancionesArtista(String artista) {
        // Required empty public constructor
        this.artistaSeleccionado = artista;
        //Log.i("ArtistaSeleciconada:", artistaSeleccionado);
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
        View vistaCancionesPlaylist = inflater.inflate
                (R.layout.fragment_canciones_artista, container, false);



        // Inicializamos las variables
        listaCancionesPlaylist = new ArrayList<>();
        recyclerCancionesPlaylist = (RecyclerView)
                vistaCancionesPlaylist.findViewById(R.id.recyclerViewCancionesArtist);
        //miPlaylistInfo = new Playlist();

        // Convertimos el json recibido en el recycler view con tarjetas de canciones
        Gson gson;
        gson = new Gson();


        miTXTVP = (TextView) vistaCancionesPlaylist.findViewById(R.id.infoArtistName);

        obtenerCancionesBD("", artistaSeleccionado);

        if (listaCancionesPlaylist != null ){

            /*
            //Los fragmentos no utilizan 'MainActivity.this', sino 'getContext()'
            recyclerCancionesPlaylist.setLayoutManager(new LinearLayoutManager(getContext()));

            SongsPlaylistRVAdapter artistsRecyclerViewAdapter =
                    new SongsPlaylistRVAdapter(listaCancionesPlaylist);

            // Listener para click para SELECCIONAR una cancion
            artistsRecyclerViewAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(),
                            "Seleccion: "+ listaCancionesPlaylist.
                                    get(recyclerCancionesPlaylist.
                                            getChildAdapterPosition(view)).getTitle(),
                            Toast.LENGTH_SHORT).show();
                }
            });
            recyclerCancionesPlaylist.setAdapter(artistsRecyclerViewAdapter);
            */

            // Boton regresar
            botonRegresarPlayListSplit =
                    (ImageButton) vistaCancionesPlaylist.findViewById(R.id.regresarPlaylists);
            botonRegresarPlayListSplit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }
        else {
            Log.i("Errores: ", "Playlist sin canciones!");
        }



        return vistaCancionesPlaylist;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    // Jalar de la BD
    // Query para obtener todas las canciones
    private void obtenerCancionesBD (String URL, String artista){

        URL = ServidorBD.direccionLocal+"/reproductormp3/leer_cancionesXArtista.php?nombreArtista="+artista;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            String nombreP = "";
            String cantiP = "";
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        // nombrePlaylist
                        // numCancionesPlaylist
                        // nombreCancion
                        // pathCancion
                        // duracionCancion
                        // albumCancion
                        // artistaCancion

                        String title = jsonObject.getString("nombreCancion");
                        String path = jsonObject.getString("pathCancion");
                        String duration = jsonObject.getString("duracionCancion");
                        String album = jsonObject.getString("albumCancion");
                        String artist = jsonObject.getString("artistaCancion");

                        Cancion song = new Cancion(path, title, artist, album, duration);
                        //Log.i("BD:", song.getTitle() + song.getArtist()+ song.getAlbum());
                        listaCancionesPlaylist.add(song);

                    } catch (JSONException e) {
                        Log.i("Exception!", e.getMessage());
                    }
                }
                //
                // < Parte para reproducir * OSCAR *
                // Adaptadores se ponen aquí porque no hay otra manera :'v

                //Los fragmentos no utilizan 'MainActivity.this', sino 'getContext()'
                recyclerCancionesPlaylist.setLayoutManager(new LinearLayoutManager(getContext()));

                SongsPlaylistRVAdapter artistsRecyclerViewAdapter =
                        new SongsPlaylistRVAdapter(listaCancionesPlaylist);

                musicPlayer.setPlaylist(listaCancionesPlaylist);
                // Listener para click para SELECCIONAR una cancion
                artistsRecyclerViewAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicPlayer.reset();
                        musicPlayer.playSong(listaCancionesPlaylist.get(recyclerCancionesPlaylist.getChildAdapterPosition(view)).getTitle());
                        fragmentInteractionListener.onFragmentInteraction(true);
                    }
                });
                recyclerCancionesPlaylist.setAdapter(artistsRecyclerViewAdapter);
                miTXTVP.setText(nombreP);

                // Parte para reproducir * OSCAR * />
                //

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERRRROOOR!: ", error.toString());
            }
        });

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
        //Log.i("TAMANIO FINAL", String.valueOf(listaCanciones.size()));
        //Log.i("DATA:", finalMiLista[0].toString());
    }


}

