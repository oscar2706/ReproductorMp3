package com.example.bottomtabbed.fragments;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bottomtabbed.MainActivity;
import com.example.bottomtabbed.R;

import com.example.bottomtabbed.adapters.RecyclerViewAdapter;
import com.example.bottomtabbed.classes.Cancion;
import com.example.bottomtabbed.classes.FragmentListener;
import com.example.bottomtabbed.classes.MusicPlayer;
import com.example.bottomtabbed.classes.ServidorBD;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnoFragment extends Fragment
        implements MainActivity.OnHeadlineSelectedListener {

    // Vista
    RecyclerView recyclerCanciones;

    // Lista de canciones
    ArrayList<Cancion> listaCanciones;

    //ArrayList<Cancion> listaCancionesL;
    RequestQueue requestQueue;

    private final MusicPlayer musicPlayer = MusicPlayer.getInstance();

    private static final String ARG_LISTA = "listaCanciones";
    private String listaCancionesJSON;

    public static UnoFragment newInstance(String listaJSON) {

        //Log.i("RECIBIMIENTO DE PARAMETROS!", " RECIBIENDO...");
        UnoFragment unoFragment = new UnoFragment();
        Bundle bundleObject = new Bundle();
        //bundleObject.putSerializable(ARG_LISTA, listaRecibida);
        bundleObject.putString(ARG_LISTA, listaJSON);
        unoFragment.setArguments(bundleObject);
        //Log.i("RECIBIMIENTO DE PARAMETROS!", " ESTABLECIDO!!");
        return unoFragment;
    }


    // Adaptador
    RecyclerViewAdapter recyclerViewAdapter;


    //Canciones
    TextView songName;
    TextView songArtist;
    ImageButton botonOpcCancion;

    // Boton de ordenar
    Button ordenar;

    ImageButton ordenarCanciones;

    ServidorBD servidorBD;

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


    public UnoFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lo que normalmente iría en "onCreate" va en "onCreateView"
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_uno, container, false);

        recyclerCanciones = (RecyclerView) vista.findViewById(R.id.recyclerID);
        //recyclerCanciones.setHasFixedSize(true);

        //servidorBD = new ServidorBD();
        listaCanciones = new ArrayList<>();

        Gson gson;
        gson = new Gson();

        if(getArguments() != null){

            listaCancionesJSON = getArguments().getString("listaCanciones");
            Type cancionType = new TypeToken<ArrayList<Cancion>>(){}.getType();
            //listaCanciones = gson.fromJson(listaCancionesJSON, cancionType);

            obtenerCancionesBD(ServidorBD.direccionLocal+"/reproductormp3/leer_todas_canciones.php");
            Log.i("JSON CANCIONES ROLITAS DESAYUNO: ", "AAH");
        }



        // Si por alguna razon no hay canciones...
        if ((listaCanciones != null)) {
            /*
            musicPlayer.setPlaylist(listaCanciones);
            recyclerViewAdapter = new RecyclerViewAdapter(getContext(), listaCanciones);
            // Listener para click para REPRODUCIR cada canción
            recyclerViewAdapter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    musicPlayer.reset();
                    musicPlayer.playSong(listaCanciones.get(recyclerCanciones.getChildAdapterPosition(view)).getTitle());
                    fragmentInteractionListener.onFragmentInteraction(true);
                }
            });

            recyclerCanciones.setAdapter(recyclerViewAdapter);
            //Los fragmentos no utilizan 'MainActivity.this', sino 'getContext()'
            recyclerCanciones.setLayoutManager(new LinearLayoutManager(getContext()));*/


            // Esto es para mostrar el popUp menu de ordenamiento de canciones
            ButtonSongHandler bsh = new ButtonSongHandler();
            //Log.i("Errores", "Id del boton:" + R.id.btnMenuSong);

            ordenar = (Button) vista.findViewById(R.id.btnMenu_MenuTop);
            ordenarCanciones = (ImageButton) vista.findViewById(R.id.botonOrdenarCanciones);
            ordenarCanciones.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    // GetContext == MainActivity.this (para fragmentos y no actividades)
                    PopupMenu popupMenu = new PopupMenu(getContext(), view);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_ordenamiento, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {

                                case R.id.asc_nombre:
                                    ordenarListaAlfabeticamente();
                                    return true;

                                case R.id.desc_nombre:
                                    ordenarListaAlfabeticamenteDESC();
                                    return true;

                                case R.id.asc_artista:
                                    ordenarListaAlfabeticamenteArtASC();
                                    return true;

                                case R.id.desc_artista:
                                    ordenarListaAlfabeticamenteArtDESC();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();
                }
            });

        }

        // cargamos la lista de canciones de manera ficticia (sin ocupar datos reales)
        //cargarLista();

        return vista;
    }

    // Cargar lista ficticia de canciones
    private void cargarLista() {

        //Log.i("Errores *****", "Llamada a CARGAR LISTA  (Creacion de canciones)" );

        /*listaCanciones.add(new Cancion("", "Un beso", "Alkilados", R.drawable.canciones));
        listaCanciones.add(new Cancion("/storage/emulated/0/CancionesProyecto/JB-Yummy.mp3:","Yummy", "Justin Bieber", R.drawable.canciones));
        listaCanciones.add(new Cancion("","Morena Mía", "Miguel Bosé",R.drawable.canciones));
        listaCanciones.add(new Cancion("","Dale Calma", "Technicolor Fabrics", R.drawable.canciones));
        listaCanciones.add(new Cancion("","No Lie", "Sean Paul ft. Dua Lipa", R.drawable.canciones));

        listaCanciones.add(new Cancion("","Starboy", "The Weeknd", R.drawable.canciones));
        listaCanciones.add(new Cancion("","Huitzil", "Porter", R.drawable.canciones));
        listaCanciones.add(new Cancion("","El Chico", "Siddartha",R.drawable.canciones));
        listaCanciones.add(new Cancion("","Alejandro", "Lady Gaga", R.drawable.canciones));
        listaCanciones.add(new Cancion("","Se Te Nota", "Lele Pons, Guaynaa", R.drawable.canciones));

        listaCanciones.add(new Cancion("","Azul", "J Balvin", R.drawable.canciones));
        listaCanciones.add(new Cancion("","Balance Ton Quoi", "Angèle", R.drawable.canciones));
        listaCanciones.add(new Cancion("","Alors On Danse", "Stromae", R.drawable.canciones));
        listaCanciones.add(new Cancion("","Ilomillo", "Billie Eilish", R.drawable.canciones));
        listaCanciones.add(new Cancion("","Set Fire To The Rain", "Adele",R.drawable.canciones));*/
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    public void imprimeMSG() {
        Toast.makeText(getContext(),
                "Mensaje desde el boton de:",
                Toast.LENGTH_SHORT).show();
    }

    // 'Escuchador' para el boton de opciones de cada canción
    private class ButtonSongHandler implements OnClickListener {

        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(),
                    "Mensaje desde Opcion:",
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            MainActivity headlinesFragment = (MainActivity) activity;
            headlinesFragment.setOnHeadlineSelectedListener(this);

        }
    }


    public void onArticleSelected(int position) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
        //Log.i("Estado ***", "KESTAPASANDA");
        //Log.i("Articulo: ***", position);
        //("OnArticleSelected");
    }

    // FUNCIONES IMPLEMENTADAS QUE ESTÁN EN LA INTERFAZ EN MAIN ACTIVITY
    // FUNCIONES DE ORDENAMIENTO DE CANCIONES ***************************************
    // Ordenar lista Ascendentemente (NOMBRE CANCION)
    public void ordenarListaAlfabeticamente() {
        Collections.sort(listaCanciones, new Comparator<Cancion>() {
            @Override
            public int compare(Cancion cancion1, Cancion cancion2) {
                return cancion1.getTitle().compareTo(cancion2.getTitle());
            }
        });
        recyclerViewAdapter.notifyDataSetChanged();
    }

    // Ordenar lista Descendentemente (NOMBRE CANCION)
    public void ordenarListaAlfabeticamenteDESC() {

        ordenarListaAlfabeticamente();
        Collections.reverse(listaCanciones);

        recyclerViewAdapter.notifyDataSetChanged();
    }

    // Ordenar lista Ascendentemente (NOMBRE ARTISTA)
    public void ordenarListaAlfabeticamenteArtASC() {

        Collections.sort(listaCanciones, new Comparator<Cancion>() {
            @Override
            public int compare(Cancion cancion1, Cancion cancion2) {
                return cancion1.getArtist().compareTo(cancion2.getArtist());
            }
        });
        recyclerViewAdapter.notifyDataSetChanged();
    }

    // Ordenar lista Descendentemente (NOMBRE CANCION)
    public void ordenarListaAlfabeticamenteArtDESC() {

        ordenarListaAlfabeticamenteArtASC();
        Collections.reverse(listaCanciones);

        recyclerViewAdapter.notifyDataSetChanged();
    }



    // Query para obtener todas las canciones
    private void obtenerCancionesBD (String URL){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        String title = jsonObject.getString("nombreCancion");
                        String path = jsonObject.getString("pathCancion");
                        String duration = jsonObject.getString("duracionCancion");
                        String album = jsonObject.getString("albumCancion");
                        String artist = jsonObject.getString("artistaCancion");

                        Cancion song = new Cancion(path, title, artist, album, duration);
                        //Log.i("BD:", song.getTitle() + song.getArtist()+ song.getAlbum());
                        listaCanciones.add(song);
                    } catch (JSONException e) {
                        Log.i("Exception!", e.getMessage());
                    }
                }
                //
                // < Parte para reproducir * OSCAR *
                // Adaptadores se ponen aquí porque no hay otra manera :'v
                musicPlayer.setPlaylist(listaCanciones);
                recyclerViewAdapter = new RecyclerViewAdapter(getContext(), listaCanciones);
                // Listener para click para REPRODUCIR cada canción
                recyclerViewAdapter.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicPlayer.reset();
                        musicPlayer.playSong(listaCanciones.get(recyclerCanciones.getChildAdapterPosition(view)).getTitle());
                        fragmentInteractionListener.onFragmentInteraction(true);
                    }
                });
                recyclerCanciones.setAdapter(recyclerViewAdapter);
                //Los fragmentos no utilizan 'MainActivity.this', sino 'getContext()'
                recyclerCanciones.setLayoutManager(new LinearLayoutManager(getContext()));
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
