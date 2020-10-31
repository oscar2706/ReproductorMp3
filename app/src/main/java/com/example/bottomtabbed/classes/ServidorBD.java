package com.example.bottomtabbed.classes;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServidorBD {
    //public static final String direccionLocal = "http://192.168.100.7:8081";
    public static final String direccionLocal = "http://192.168.0.199:8888";

    RequestQueue requestQueue;
    String URL;

    public ArrayList<Cancion> cancionesss;

    public ServidorBD(){
        cancionesss = new ArrayList<>();
    }

    public ArrayList<Cancion> getCancionesss() {
        return cancionesss;
    }

    public void setCancionesss(ArrayList<Cancion> cancionesss) {
        this.cancionesss = cancionesss;
    }

    // INSERCION, CONSULTA Y UPDATE DE DATOS CON LA BASE DE DATOS
    // Insertar datos prueba/plantilla
    public void ejecutarServicio(String URL, Context context){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), "Operacion exitosa", Toast.LENGTH_SHORT).show();
                Log.i("RESULTADO","Operacion exitosa");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.i("RESULTADO",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("parametro1", "valor1");
                parametros.put("parametro2", "valor2");
                parametros.put("parametro3", "valor3");
                return parametros;
            }
        };

        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    //
    //
    // C O N S U L T A R   D A T O S ************************************************************
    //
    // Obtener todas las canciones
    public ArrayList<Cancion> obtenerCancionesBD (Context context){
        URL = direccionLocal+"/reproductormp3/leer_todas_canciones.php";
        final String[] jsonreponse = {""};
        final ArrayList<Cancion> songs_ = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    //Log.i("INTENTO", "i:"+i);
                    try {
                        jsonObject = response.getJSONObject(i);
                        Cancion song = new Cancion();
                        //Log.i("Nombre Cancion " + i, jsonObject.getString("nombreCancion"));
                        //Log.i("Artista Cancion " + i, jsonObject.getString("artistaCancion"));
                        //Log.i("JSON CANCIONES", response.toString());
                        song.setTitle(jsonObject.getString("nombreCancion"));
                        song.setPath(jsonObject.getString("pathCancion"));
                        song.setDuration(jsonObject.getString("duracionCancion"));
                        song.setAlbum(jsonObject.getString("albumCancion"));
                        song.setArtist(jsonObject.getString("artistaCancion"));

                        Log.i("Nombre Cancion " + i, song.getTitle());
                        Log.i("Artista Cancion " + i, song.getArtist());

                        //this.cancionesss.add(song);
                        songs_.add(song);


                    } catch (JSONException e) {
                        Log.i("Exception!", e.getMessage());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERRRROOOR!: ", error.toString());
            }
        });

        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);

        return songs_;
    }

    // Obtener todos los artistas
    public void obtenerArtistasBD (Context context){
        String URL = direccionLocal+"/reproductormp3/leer_todos_artistas.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Log.i("Nombre artista" + i, jsonObject.getString("nombreArtista"));
                        Log.i("Cantidad canciones:" + i, jsonObject.getString("cantidadCanciones"));

                    } catch (JSONException e) {
                        Log.i("Exception!", e.getMessage());
                    }}}
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERRRROOOR!: ", error.toString());
            }
        });
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    // Obtener todos los albumes
    public void obtenerAlbumesBD (Context context, String filtro){

        String URL = direccionLocal+"/reproductormp3/leer_todos_albumes.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Log.i("Nombre album" + i, jsonObject.getString("nombreAlbum"));
                        Log.i("Cantidad canciones:" + i, jsonObject.getString("cantidadCanciones"));

                    } catch (JSONException e) {
                        Log.i("Exception!", e.getMessage());
                    }}}
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERRRROOOR!: ", error.toString());
            }
        });
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    // Obtener todas las playlists
    public void obtenerPlaylistsBD (Context context, String filtro){

        String URL = direccionLocal+"/reproductormp3/leer_todas_playlists.php";

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
                        //Log.i("Cantidad canciones:" + i, jsonObject.getString("cantidadCanciones"));

                        Type plaType = new TypeToken<ArrayList<Playlist>>(){}.getType();
                        ArrayList<Artista> miLista = gson.fromJson(response.toString(), plaType);
                        Log.d("JSON PLAY", response.toString());

                    } catch (JSONException e) {
                        Log.i("Exception!", e.getMessage());
                    }}}
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERRRROOOR!: ", error.toString());
            }
        });
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    //
    //
    // I N S E R T A R   D A T O S ************************************************************
    //
    // Insertar un solo artista!
    public void insertarSoloUnArtista(Context context,  final String nombreArtista,
                                                        final String cantidadCanciones,
                                                        final String OPCpathImagen){

        Log.i("Prueba yabastaplx", "Holi");
        // Mi direccion IP y mi ubicacion de los archivos PHP (xampp/htdocs/reproductormp3/X.php)!
        String URL = direccionLocal+"/reproductormp3/insertar_un_artista.php";

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
                parametros.put("nombreArtista", nombreArtista);
                parametros.put("cantidadCanciones", cantidadCanciones);
                parametros.put("OPCpathImagen", OPCpathImagen);
                return parametros;
            }
        };

        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    // Insertar datos de un album!
    public void insertarSoloUnAlbum (Context context,   final String nombreAlbum,
                                                        final String agnoLanzamiento,
                                                        final String cantidadCanciones,
                                                        final String duracionAlbum,
                                                        final String artistaAlbum){

        // Mi direccion IP y mi ubicacion de los archivos PHP (xampp/htdocs/reproductormp3/X.php)!
        String URL = direccionLocal+"/reproductormp3/insertar_un_album.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), "Operacion exitosa", Toast.LENGTH_SHORT).show();
                //Log.i("RESULTADO INSERCION ALBUMES:","Operacion exitosa!");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                //Log.i("RESULTADO INSERCION ALBUMES:",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();

                // Key y Valor
                parametros.put("nombreAlbum", nombreAlbum);
                parametros.put("agnoLanzamiento", agnoLanzamiento);
                parametros.put("cantidadCanciones", cantidadCanciones);
                parametros.put("duracionAlbum", duracionAlbum);
                parametros.put("artistaAlbum", artistaAlbum);
                return parametros;
            }
        };

        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    // Insertar datos de una cancion!
    public void insertarSoloUnaCancion( Context context,   final String nombreCancion,
                                                            final String pathCancion,
                                                            final String duracionCancion,
                                                            final String albumCancion,
                                                            final String artistaCancion){

        // Mi direccion IP y mi ubicacion de los archivos PHP (xampp/htdocs/reproductormp3/X.php)!
        String URL = direccionLocal+"/reproductormp3/insertar_una_cancion.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), "Operacion exitosa", Toast.LENGTH_SHORT).show();
                //Log.i("RESULTADO INSERCION ALBUMES:","Operacion exitosa!");
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
                parametros.put("nombreCancion", nombreCancion);
                parametros.put("pathCancion", pathCancion);
                parametros.put("duracionCancion", duracionCancion);
                parametros.put("albumCancion", albumCancion);
                parametros.put("artistaCancion", artistaCancion);
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    // Insertar datos de una playlist!
    public void insertarSoloUnaPlaylist(Context context){

    }

    //
    //
    // A C T U A L I Z A R   D A T O S ************************************************************
    //
    // Actualizar datos de una playlist (1:agregar/ 2:eliminar una nueva playlist)
    public void actualizarPlaylist(Context context, String playlist,
                                                    String cancion,
                                                    int accion){


    }

}
