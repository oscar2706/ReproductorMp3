package com.example.bottomtabbed.sheets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.PluralsRes;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bottomtabbed.MainActivity;
import com.example.bottomtabbed.R;
import com.example.bottomtabbed.adapters.PlaylistsRecyclerViewAdapter;
import com.example.bottomtabbed.adapters.PlaylistsToAddRVAdapter;
import com.example.bottomtabbed.classes.Playlist;
import com.example.bottomtabbed.classes.ServidorBD;
import com.example.bottomtabbed.fragments.CancionesPlaylist;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BottomSheet extends BottomSheetDialogFragment implements View.OnClickListener
        , MainActivity.OnHeadlineSelectedListener {


    String cancionAAgregar;

    public BottomSheet(String cancionAAgregar) {
        this.cancionAAgregar = cancionAAgregar;
    }

    // Vista de playlists disponibles para agregar (card con su informacion)
    RecyclerView recyclerPlaylistsDisponibles;
    // ArrayLista de playlists y su json pa llenarlo
    ArrayList<Playlist> playlistsDisponibles;
    String playlistsDisponiblesJSON = "";

    RequestQueue requestQueue;

    PlaylistsToAddRVAdapter artistsRecyclerViewAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sheet_add_to_playlist, container, false);
        view.findViewById(R.id.btn_new_playlist).setOnClickListener(this);

        /*view.findViewById(R.id.playlist_card_1).setOnClickListener(this);
        view.findViewById(R.id.playlist_card_2).setOnClickListener(this);
        view.findViewById(R.id.playlist_card_3).setOnClickListener(this);
        view.findViewById(R.id.playlist_card_4).setOnClickListener(this);*/


        // Inicializamos las variables
        playlistsDisponibles = new ArrayList<>();
        recyclerPlaylistsDisponibles = (RecyclerView)
                view.findViewById(R.id.recyclerViewPlaylistToAdd);


        // Obtenemos los datos de las playlists almacenadas
        playlistsDisponiblesJSON = cargarDatosPlaylists();
        if(playlistsDisponiblesJSON != ""){

            //Log.i("JSON FILE GET!:", playlistsDisponiblesJSON);
            Gson gson;
            gson = new Gson();

            Type playlistType = new TypeToken<ArrayList<Playlist>>(){}.getType();
            ArrayList<Playlist> miLista = gson.fromJson(playlistsDisponiblesJSON, playlistType);
            //playlistsDisponibles = miLista;

            //Log.i("Chequeo:", playlistsDisponibles.get(0).getNombrePlaylist());
        }
        obtenerPlaylistsBD(getContext(),"");
        // Generamos la lista de playlists en el bottomsheet
        if (playlistsDisponibles != null){

            /*
            //Los fragmentos no utilizan 'MainActivity.this', sino 'getContext()'
            recyclerPlaylistsDisponibles.setLayoutManager(new LinearLayoutManager(getContext()));

            artistsRecyclerViewAdapter =
                    new PlaylistsToAddRVAdapter(playlistsDisponibles);

            // Listener para click para SELECCIONAR una playlist
            artistsRecyclerViewAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(),
                            "Seleccion: "+ playlistsDisponibles.
                                    get(recyclerPlaylistsDisponibles.
                                            getChildAdapterPosition(view)).getNombrePlaylist(),
                            Toast.LENGTH_SHORT).show();
                }
            });
            recyclerPlaylistsDisponibles.setAdapter(artistsRecyclerViewAdapter);
            */

        } else {
            Log.i("ERROR!", "No hay playlists! (Esto no debería estar pasando :'v)");
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_new_playlist:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Nueva Playlist");

                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input.setHint("Nombrelista");
                builder.setView(input);
                builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        insertarSoloUnaPlaylist(getContext(), input.getText().toString(), cancionAAgregar);
                        dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                //


                break;
                /*
            case R.id.playlist_card_1:
                Toast.makeText(getActivity(), "Agregada a playlist1", Toast.LENGTH_SHORT).show();
                this.dismiss();
                break;
            case R.id.playlist_card_2:
                Toast.makeText(getActivity(), "Agregada a playlist2", Toast.LENGTH_SHORT).show();
                this.dismiss();
                break;
            case R.id.playlist_card_3:
                Toast.makeText(getActivity(), "Agregada a playlist3", Toast.LENGTH_SHORT).show();
                this.dismiss();
                break;
            case R.id.playlist_card_4:
                Toast.makeText(getActivity(), "Agregada a playlist4", Toast.LENGTH_SHORT).show();
                this.dismiss();
                break;
                */
        }
    }

    @Override
    public void onArticleSelected(int position) {

    }

    @Override
    public void ordenarListaAlfabeticamente() {

    }

    // LOAD
    public String cargarDatosPlaylists(){
        //TextView tv = (TextView) findViewById(R.id.textView);

        FileInputStream in = null;
        StringBuilder sb = new StringBuilder();
        String nombreArchivo = "playlistsStorage.txt";
        String jsonRescatado = "";

        try {
            in = getActivity().openFileInput(nombreArchivo);

            int read = 0;
            while((read = in.read()) != -1){
                sb.append((char) read);
            }
            jsonRescatado = sb.toString();
            Log.i("LOAD:", sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (in != null){
                try {
                    in.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return jsonRescatado;
    }


    // BASE DE DATOOOS!
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

                        String nombrePlaylist = jsonObject.getString("nombrePlaylist");
                        String numCancionesPlaylist = jsonObject.getString("numCancionesPlaylist");

                        Playlist playlist = new Playlist();
                        playlist.setNombrePlaylist(nombrePlaylist);
                        playlist.setCantidadCanciones(numCancionesPlaylist);

                        Log.i("Nombre playlist" + i, nombrePlaylist);
                        Log.i("Cantidad canciones:" + i, numCancionesPlaylist);

                        playlistsDisponibles.add(playlist);

                    } catch (JSONException e) {
                        Log.i("Exception!", e.getMessage());
                    }
                }
                // I C I

                // 1. recycler.setLayoutManager
                // 2. adapter = new Adapter (array de playlists)
                // 3. recycler.setAdapter(adapter)

                //Los fragmentos no utilizan 'MainActivity.this', sino 'getContext()'
                recyclerPlaylistsDisponibles.setLayoutManager(new LinearLayoutManager(getContext()));

                artistsRecyclerViewAdapter =
                        new PlaylistsToAddRVAdapter(playlistsDisponibles);

                // Listener para click para SELECCIONAR una playlist
                artistsRecyclerViewAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String nombrePlaylis = playlistsDisponibles.
                                get(recyclerPlaylistsDisponibles.
                                        getChildAdapterPosition(view)).getNombrePlaylist();

                        //Insertamos la cancion a la playlist
                        insertarCancionXPlaylist(getContext(), nombrePlaylis, cancionAAgregar);

                        Toast.makeText(getContext(),
                                "Agregada a: "+ nombrePlaylis,
                                Toast.LENGTH_SHORT).show();

                        dismiss();
                    }
                });
                recyclerPlaylistsDisponibles.setAdapter(artistsRecyclerViewAdapter);


                // I C I

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

    public void insertarCancionXPlaylist(Context context,  final String nombrePlaylist,
                                                final String nombreCancion){

        // Mi direccion IP y mi ubicacion de los archivos PHP (xampp/htdocs/reproductormp3/X.php)!
        String URL = ServidorBD.direccionLocal+"/reproductormp3/insertar_cancion_a_playlist.php";
        //String URL = "http://X.X.X.X:PUERTO/reproductormp3/insertar_un_artista.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), "Operacion exitosa", Toast.LENGTH_SHORT).show();
                //Log.i("RESULTADO INSERCION ARTISTAS:","Operacion exitosa!");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                //Log.i("RESULTADO INSERCION ARTISTAS:",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                // Key y Valor
                parametros.put("nombrePlaylist", nombrePlaylist);
                parametros.put("nombreCancion", nombreCancion);
                return parametros;
            }
        };

        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    public void insertarSoloUnaPlaylist(Context context,  final String nombrePlaylist,
                                        final String nombreCancion){

        // Mi direccion IP y mi ubicacion de los archivos PHP (xampp/htdocs/reproductormp3/X.php)!
        String URL = ServidorBD.direccionLocal+"/reproductormp3/insertar_una_playlist.php";
        //String URL = "http://X.X.X.X:PUERTO/reproductormp3/insertar_un_artista.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), "Operacion exitosa", Toast.LENGTH_SHORT).show();
                //Log.i("RESULTADO INSERCION ARTISTAS:","Operacion exitosa!");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                //Log.i("RESULTADO INSERCION ARTISTAS:",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                // Key y Valor
                parametros.put("nombrePlaylist", nombrePlaylist);
                parametros.put("nombreCancion", nombreCancion);
                return parametros;
            }
        };

        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


}
