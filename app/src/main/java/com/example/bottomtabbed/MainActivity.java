package com.example.bottomtabbed;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bottomtabbed.adapters.MenuBottomAdapter;
import com.example.bottomtabbed.classes.Album;
import com.example.bottomtabbed.classes.Artista;
import com.example.bottomtabbed.classes.Cancion;
import com.example.bottomtabbed.classes.FragmentListener;
import com.example.bottomtabbed.classes.MusicPlayer;
import com.example.bottomtabbed.classes.Playlist;
import com.example.bottomtabbed.classes.ServidorBD;
import com.example.bottomtabbed.fragments.CuatroFragment;
import com.example.bottomtabbed.fragments.DosFragment;
import com.example.bottomtabbed.fragments.TresFragment;
import com.example.bottomtabbed.fragments.UnoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Menu Bottom
// Fragmentos (canciones, artistas, albumes, playlists)
// FILES

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FragmentListener {

    // Paginador para ver los diferentes fragmentos en una sola actividad:
    private ViewPager viewPager;
    // Array de los fragmentos
    ArrayList<Fragment> listaFragmentos;
    // Adaptador del MenuInferior para controlar el comportamiento del menu:
    private PagerAdapter menuBottomAdapter;
    // Auxiliar para poder sincronizar el Paginador y el Menu Inferior
    private MenuItem previewMenuItem;
    // Escuchador para comunicarse con los fragmentos:
    private menuOpcionesGeneral escuchadorMenu;

    // Valor para la solicitud de los permisos de lectura del celular
    public static final int REQUEST_CODE = 1;
    // Lista de canciones leidas del celular
    ArrayList<Cancion> listaCancionesDescargadas;
    // Lista de artistas
    ArrayList<Artista> listaArtistasDeCD;
    // Lista de albumes
    ArrayList<Album> listaAlbumesDeCD;
    // Lista de playlists
    ArrayList<Playlist> listaPlaylistsdeCancionesDescargadas;

    // Pa la base de datos
    RequestQueue requestQueue;
    ServidorBD miServidorBD;
    String URL ="";

    //Reproductor
    private final MusicPlayer musicPlayer = MusicPlayer.getInstance();
    private TextView txtSong, txtArtist;
    private CardView miniPlayer;
    private ImageView imageViewAlbum;
    private Button btnNext;
    private Button btnPlayPause;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        Log.i("Creacion: ******", "Creamos on create main activity");
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // Colocamos el toolbar como actionBar
        //Toolbar myToolbar = findViewById( R.id.toolbar );
        //setSupportActionBar( myToolbar );

        listaCancionesDescargadas = new ArrayList<>();
        listaArtistasDeCD = new ArrayList<>();
        listaAlbumesDeCD = new ArrayList<>();

        miServidorBD = new ServidorBD();

        // Ojo aqui oscar :v **
        // Pedimos los permisos por PRIMERA VEZ, sino, descomentar linea
        //permisos();

        //if ((obtenerCancionesBD(direccionLocal+"/reproductormp3/leer_todas_canciones.php")) == null) {

        Log.i("HECHO", "llenar lista");
        listaCancionesDescargadas = obtenerTodasCanciones(MainActivity.this);
        //listaCancionesDescargadas = obtenerCancionesBDD(this);
        //obtenerCancionesBD(direccionLocal+"/reproductormp3/leer_todas_canciones.php");

        //if (listaCancionesDescargadas.size() != 0) {

        Log.i("TAMANIO", String.valueOf(listaCancionesDescargadas.size()));

        listaArtistasDeCD = obtenerTodosArtistas(listaCancionesDescargadas);
        listaAlbumesDeCD = obtenerTodosAlbumes(listaCancionesDescargadas);
        //Cargar los datos recolectados en la BD
        guardarDatosBD();

        //miServidorBD.obtenerCancionesBD(this);
        //obtenerCancionesBD(direccionLocal+"/reproductormp3/leer_todas_canciones.php");

        // Creamos un nuevo bottomMenuAdapter (clase.java)
        // Esta clase nos ayuda a controlar el comportamiento del bottomMenu y su Viewer
        menuBottomAdapter = new MenuBottomAdapter(
                initFragments(), // Inicializa los fragmentos 1, 2 y 3 (canciones, artistas, etc.)
                getSupportFragmentManager());

        // Instanciamos el viewPager (widget donde veremos los fragmentos)
        // Y le asignamos su adaptador (controlador) de vistas con el menubottomadapter ya creado
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(menuBottomAdapter);
        // Instanciamos el viewPager (widget donde veremos los fragmentos)
        // Y le asignamos su adaptador (controlador) de vistas con el menubottomadapter ya creado
        viewPager = findViewById( R.id.viewPager );
        viewPager.setAdapter(menuBottomAdapter);
        miniPlayer = findViewById(R.id.miniPlayer);
        imageViewAlbum = findViewById(R.id.imageViewAlbum_);
        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicPlayer.playNext();
                onFragmentInteraction(true);
                btnPlayPause.setBackgroundResource(R.drawable.ic_pause);
            }
        });
        btnPlayPause = findViewById(R.id.btn_play_music);
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicPlayer.playPause();
                if(musicPlayer.isPlaying()){
                    onFragmentInteraction(true);
                } else {
                    onFragmentInteraction(false);
                }
            }
        });

        // Literalmente, este es el BottomMenu (menu inferior) junto con su funcionamiento
        final BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            // Movemos el item seleccionado del menu inferior a través de este switch
                            case R.id.menuOpcion1:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.menuOpcion2:
                                viewPager.setCurrentItem(1);
                                break;
                            /*case R.id.menuOpcion3:
                                viewPager.setCurrentItem(2);
                                break;*/
                            case R.id.menuOpcion4:
                                viewPager.setCurrentItem(3);
                                break;
                        }
                        return true;
                    }
                });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

                    @Override
                    public void onPageSelected(int position) {
                        navigationView.getMenu().getItem(position).setChecked(true);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
                findViewById(R.id.nav_host_fragment).setVisibility(View.GONE);
                findViewById(R.id.miniPlayer).setOnClickListener(this);
            //}
        //}
        txtSong = findViewById(R.id.txt_song_);
        txtArtist = findViewById(R.id.txt_artist_);
        miniPlayer.setVisibility(View.GONE);
    }

    // Inicializa los fragmentos 1, 2 y 3 (canciones, artistas, etc.)
    private ArrayList<Fragment> initFragments(){

        // Creamos el arreglo
        listaFragmentos = new ArrayList<>();

        // Le pasamos al fragmento 1 el json con las canciones
        String jsonCanciones = new Gson().toJson(listaCancionesDescargadas);
        //Log.i("INIT FRAGMENTS:", listaCancionesDescargadas.get(0).getTitle());
        UnoFragment cancionesFragmento = UnoFragment.newInstance(jsonCanciones);

        // Le pasamos al fragmento 2 el json con los artistas
        String jsonArtistas = new Gson().toJson(listaArtistasDeCD);
        DosFragment artistasFragmento = DosFragment.newInstance(jsonArtistas);

        // Le agregamos las tres instancias de los fragmentos
        listaFragmentos.add(cancionesFragmento);
        listaFragmentos.add(artistasFragmento);
        //listaFragmentos.add(new TresFragment()); // Fragmento 3 pendiente*

        String jsonPlaylists = new Gson().toJson(cargaPlaylistsFicticias());
        CuatroFragment playlistsFragmento = CuatroFragment.newInstance(jsonPlaylists);

        //guardarDatosInternalStorage(jsonPlaylists);

        listaFragmentos.add(playlistsFragmento); // Fragmento 4 pendiente*

        return listaFragmentos;
    }

    // Control del menu SUPERIOR (botones: configuracion y buscar) **** **** ****
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        // Se 'infla' como menu el archivo menu_top.xml
        inflater.inflate(R.menu.menu_top, menu);
        return true;
    }

    // Le agregamos el funcionamiento a los botones buscar y configuracion
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Se agregan las opciones de qué hacer en cada caso de los dos botones
        switch (item.getItemId()){

            // Se presionó el botón BUSCAR
            case R.id.btnBuscar_MenuTop:
                // Desplegar barra de busqueda
                showMessage("Buscar");
                break;

            // Se presionó el botón CONFIGURACIÓN
            case R.id.btnMenu_MenuTop:
                // Mostrar menu con: cambiar modo claro/oscuro,
                showMessage("Configuracion");
                break;
            default:
                showMessage("Default");
        }
        return true;
    }

    // Función que facilita la impresión de mensajes con objetos Toast
    public void showMessage(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    public void onClick(View view) {
        NavController navController;
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.action_unoFragment_to_reproductorFragment);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.reproductorFragment) {
                    findViewById(R.id.nav_host_fragment).setVisibility(View.VISIBLE);
                    findViewById(R.id.navigationView).setVisibility(View.GONE);
                    findViewById(R.id.miniPlayer).setVisibility(View.GONE);
                    //findViewById(R.id.appBarLayout).setVisibility(View.GONE);
                    findViewById(R.id.viewPager).setVisibility(View.GONE);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                } else {
                    findViewById(R.id.nav_host_fragment).setVisibility(View.GONE);
                    findViewById(R.id.navigationView).setVisibility(View.VISIBLE);
                    findViewById(R.id.miniPlayer).setVisibility(View.VISIBLE);
                    //findViewById(R.id.appBarLayout).setVisibility(View.VISIBLE);
                    findViewById(R.id.viewPager).setVisibility(View.VISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                }
            }
        });
    }

    @Override
    public void onFragmentInteraction(boolean playing) {
        if(viewPager.isShown())
            miniPlayer.setVisibility(View.VISIBLE);
        txtSong.setText(musicPlayer.getPlayingSong().getTitle());
        txtArtist.setText(musicPlayer.getPlayingSong().getArtist());
        txtSong.setSelected(true);
        txtArtist.setSelected(true);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(musicPlayer.getPlayingSong().getPath());
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
        imageViewAlbum.setImageBitmap(bitmap);
        crearPalette(bitmap);
        btnPlayPause.setBackgroundResource(R.drawable.ic_pause);
        if (playing) {
            Log.i("MINIPLAYER", "IC_PAUSE");
            btnPlayPause.setBackgroundResource(R.drawable.ic_pause);
        } else {
            Log.i("MINIPLAYER", "IC_PLAY");
            btnPlayPause.setBackgroundResource(R.drawable.ic_play);
        }
        /*if(!musicPlayer.isPlaying()){
        }
        else {
        }*/
    }

    // Ejemplo de interface para comunicarse con los fragmentos (parte de la configuración)
    // En MainActivity realizamos algo, y esto se refleja en x fragmento.
    public interface menuOpcionesGeneral{
        public void ordenarCanciones();
    }

    OnHeadlineSelectedListener callback;

    public void setOnHeadlineSelectedListener(OnHeadlineSelectedListener callback) {
        this.callback = callback;
    }

    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position);
        public void ordenarListaAlfabeticamente();
    }

    public void ordenarListaAlfabeticamente(){
        callback.ordenarListaAlfabeticamente();
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        // Send the event to the host activity
        callback.onArticleSelected(position);
    }
    // Termina/> Ejemplo de interface para comunicarse con los fragmentos (parte de la configuración)

    // FUNCION PARA PEDIR LOS PERMISOS DE LECTURA DE ARCHIVOS
    private void permisos(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        } else {
            showMessage("Permisos Ya otorgados!");

            // Ojo aqui oscar :v **
            //SIN PERMISOS (correr por primera vez) = Descomentar la sig linea.
            //listaCancionesDescargadas = obtenerTodasCanciones(MainActivity.this);
            //Log.i("Chequeo:", listaCancionesDescargadas.get(0).getNombre());
            //listaCancionesDescargadas = obtenerTodasCanciones(MainActivity.this);
            //listaArtistasDeCD = obtenerTodosArtistas(listaCancionesDescargadas);
            //listaAlbumesDeCD = obtenerTodosAlbumes(listaCancionesDescargadas);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                // Hacer lo que quieras hacer relacionado con los permisos
                showMessage("Permiso otorgado!");
                //archivosCanciones = obtenerTodasCanciones(MainActivity.this);
                //listaCancionesDescargadas = obtenerTodasCanciones(MainActivity.this);
                //listaCancionesDescargadas = obtenerTodasCanciones(MainActivity.this);
                //listaArtistasDeCD = obtenerTodosArtistas(listaCancionesDescargadas);
                //listaAlbumesDeCD = obtenerTodosAlbumes(listaCancionesDescargadas);

            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE);
            }
        }
    }

    public static ArrayList<Cancion> obtenerTodasCanciones (Context context){

        //Log.i("Errores *****", "Llamada a A OBTENER CANCIONES (Creacion de canciones)" );

        ArrayList<Cancion> miLista = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,        // para el Path
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.YEAR         // agno pendiente
        };
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, null);

        // Ojo aqui oscar :v **
        //Carpeta donde estan las rolitas
        String carpeta = "/storage/emulated/0/Music/";
        //String carpeta = "/storage/emulated/0/CancionesProyecto";

        if (cursor != null){
            while (cursor.moveToNext()){

                String path = cursor.getString(3);
                // Filtración de carpeta
                if (path.contains(carpeta)){

                    String album = cursor.getString(0);
                    String title = cursor.getString(1);
                    String duration = cursor.getString(2);
                    String artist = cursor.getString(4);
                    //Log.i("Duracion:", duration);

                    Cancion cancion = new Cancion(path, title, artist, album, duration);
                    miLista.add(cancion);

                    // Para verificar:
                    //Log.i("Path: " + path, "|| Album: " + album);
                    //Log.i("Chequeo:", cancion.getTitle());
                }

            }
            cursor.close();
        }
        //Log.i("Chequeo:", miLista.get(0).getNombre());

        return miLista;
    }


    public ArrayList<Album> obtenerTodosAlbumes (ArrayList<Cancion> listaCanciones){
        ArrayList<Album> miListaAlbumes = new ArrayList<>();

        for (int i = 0; i < listaCanciones.size(); i++){
            Album album = new Album();
            String albumNombre = listaCanciones.get(i).getAlbum();
            album.setNombreAlbum(albumNombre);

            if(miListaAlbumes.contains(album)){

                // Si ya existe ese album

                for (int j = 0; j < miListaAlbumes.size(); j++){// Buscamos su ID

                    if ( (miListaAlbumes.get(j).getNombreAlbum().equals((album.getNombreAlbum()))) ){

                        album = miListaAlbumes.get(j);
                        int nuevaCantidad = Integer.parseInt(miListaAlbumes.get(j).getCantidadCanciones());
                        nuevaCantidad++;

                        /*Log.i("MENSAJE VERIFICACION EXISTENCIA", "YA EXISTE EL ALBUM: " +
                                album.getNombreAlbum() + album.getNombreArtista() + nuevaCantidad);*/
                        album.setCantidadCanciones(Integer.toString( nuevaCantidad));
                        miListaAlbumes.set(j, album);
                    }
                }
            } else{
                // Si aun no existe ese album
                album.setCantidadCanciones("1");
                album.setAgnoLanzamiento("0002");
                album.setDuracionDisco("00:00");
                album.setNombreArtista(listaCanciones.get(i).getArtist());
                miListaAlbumes.add(album);
                //Log.i("MENSAJE VERIFICACION EXISTENCIA", "NO EXISTE: " + album.getNombreAlbum() );
            }
        }

        // Insercion de artistas en la BD
        /*for (int k = 0; k < miListaAlbumes.size(); k++){
            miServidorBD.insertarSoloUnAlbum
                    (this,   miListaAlbumes.get(k).getNombreAlbum(),
                                    miListaAlbumes.get(k).getAgnoLanzamiento(),
                                    miListaAlbumes.get(k).getCantidadCanciones(),
                                    miListaAlbumes.get(k).getDuracionDisco(),
                                    miListaAlbumes.get(k).getNombreArtista());
        }*/
        return miListaAlbumes;
    }

    public void guardarDatosBD(){

        //Log.i("InsertarArtistas:", "Empieza");
        // Insertar ArtistasBD
        for (int i = 0; i < listaArtistasDeCD.size(); i++){
            miServidorBD.insertarSoloUnArtista
                    (this,   listaArtistasDeCD.get(i).getNombreArtista(),
                            listaArtistasDeCD.get(i).getCantidadCanciones(),
                            "");
        }
        //Log.i("InsertarArtistas:", "Termina");

        //Log.i("InsertarAlbumes:", "Empieza");
        // Insertar AlbumesBD
        for (int j = 0; j < listaAlbumesDeCD.size(); j++) {
            miServidorBD.insertarSoloUnAlbum(this,
                    listaAlbumesDeCD.get(j).getNombreAlbum(),
                    listaAlbumesDeCD.get(j).getAgnoLanzamiento(),
                    listaAlbumesDeCD.get(j).getCantidadCanciones(),
                    listaAlbumesDeCD.get(j).getDuracionDisco(),
                    listaAlbumesDeCD.get(j).getNombreArtista()
            );
        }
        //Log.i("InsertarAlbumes:", "Termina");

        //Log.i("InsertarCanciones:", "Empieza");
        // Insertar CancionesBD
        for (int k = 0; k < listaCancionesDescargadas.size(); k++) {
            miServidorBD.insertarSoloUnaCancion(this,
                    listaCancionesDescargadas.get(k).getTitle(),
                    listaCancionesDescargadas.get(k).getPath(),
                    listaCancionesDescargadas.get(k).getDuration(),
                    listaCancionesDescargadas.get(k).getAlbum(),
                    listaCancionesDescargadas.get(k).getArtist());
        }
        //Log.i("InsertarCanciones:", "Termina");


        // Playlist 1
        Playlist favoritos = new Playlist();
        favoritos.setNombrePlaylist("Favoritos");
        insertarSoloUnaPlaylist(this, "Favoritos", "NULL");

    }

    public ArrayList<Artista> obtenerTodosArtistas (ArrayList<Cancion> listaCanciones){

        ArrayList<Artista> miListaArt = new ArrayList<>();
        //Cancion cancion = new Cancion(path, title, artist, album, duration);

        for (int i = 0; i < listaCanciones.size(); i++){

            String artistaNombre = listaCanciones.get(i).getArtist();
            Artista artista = new Artista();
            artista.setNombreArtista(artistaNombre);

            if(miListaArt.contains(artista)){

                // Si ya existe ese artista
                //Log.i("MENSAJE VERIFICACION EXISTENCIA", "SI EXISTE EL ARTISTA: " + artista.getNombreArtista());

                for (int j = 0; j < miListaArt.size(); j++){// Buscamos su ID

                    // Ojo aqui oscar :v **
                    // Aqui fue donde cambie el "==" por el "equals"; Así deben compararse los strings.
                    // Este consejo te doy, porque tu amiga Gaby soy gg
                    //if ( (miListaArt.get(j).getNombreArtista()) == (artista.getNombreArtista())){
                    if ( (miListaArt.get(j).getNombreArtista()).equals((artista.getNombreArtista()))){
                        int nuevaCantidad = Integer.parseInt(miListaArt.get(j).getCantidadCanciones());
                        nuevaCantidad ++;
                        artista.setCantidadCanciones(Integer.toString( nuevaCantidad));
                        miListaArt.set(j, artista);
                    }
                }
            } else{
                // Si aun no existe ese artista
                artista.setCantidadCanciones("1");
                miListaArt.add(artista);
                //Log.i("MENSAJE VERIFICACION EXISTENCIA", "NO EXISTE: " + artista.getNombreArtista() );
            }
        }
        return miListaArt;
    }

    ArrayList<Playlist> cargaPlaylistsFicticias(){

        ArrayList<Playlist> misPlaylists = new ArrayList<>();

        // Playlist 1
        Playlist favoritos = new Playlist();
        favoritos.setNombrePlaylist("Favoritos");
        favoritos.agregarCancion(listaCancionesDescargadas.get(4));

        insertarSoloUnaPlaylist(this, "Favoritos",
                listaCancionesDescargadas.get(4).getTitle());
        /*
        // Playlist 2
        Playlist perreoConAmor = new Playlist();
        perreoConAmor.setNombrePlaylist("Perreo Con Amor");
        perreoConAmor.agregarCancion(listaCancionesDescargadas.get(2));
        perreoConAmor.agregarCancion(listaCancionesDescargadas.get(6));
        perreoConAmor.agregarCancion(listaCancionesDescargadas.get(7));

        // Playlist 3
        Playlist romanticas = new Playlist();
        romanticas.setNombrePlaylist("Romanticas");
        romanticas.agregarCancion(listaCancionesDescargadas.get(5));

        // Playlist 4
        Playlist rock = new Playlist();
        rock.setNombrePlaylist("Rock");
        rock.agregarCancion(listaCancionesDescargadas.get(3));

        // Agregamos las playlists a la lista de playlists
        misPlaylists.add(favoritos);
        misPlaylists.add(perreoConAmor);
        misPlaylists.add(romanticas);
        misPlaylists.add(rock);
        */
        //misPlaylists.add(favoritos);


        return misPlaylists;
    }

    //
    // I N S E R T A R   D A T O S ************************************************************
    //
    // Insertar un solo artista!
    public void insertarSoloUnaPlaylist(Context context,  final String nombrePlaylist,
                                        final String nombreCancion){

        // Mi direccion IP y mi ubicacion de los archivos PHP (xampp/htdocs/reproductormp3/X.php)!
        String URL = ServidorBD.direccionLocal+"/reproductormp3/insertar_una_playlist.php";

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








    /*boolean guardarDatosInternalStorage(String jsonPlaylists){
        //Log.i("JSON FILE SET!:", jsonPlaylists);

        boolean resultadoGuardar = false;
        String nombreArchivo = "playlistsStorage.txt";

        FileWriter fw = null;
        String str = jsonPlaylists;
        //FileOutputStream out = null;

        try {
            fw = new FileWriter(nombreArchivo, false);
            fw.write(str);
            //out = openFileOutput(nombreArchivo, Context.MODE_APPEND);
            //out.write(str.getBytes());
            resultadoGuardar = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (fw != null){
                try {
                    fw.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }

        return resultadoGuardar;
    }*/

    // INSERCION, CONSULTA Y UPDATE DE DATOS CON LA BASE DE DATOS
    // Insertar datos
    private void ejecutarServicio(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Operacion exitosa", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("parametro1", "valor1");
                parametros.put("parametro2", "valor2");
                parametros.put("parametro3", "valor3");
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void crearPalette(Bitmap bitmap) {

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                Palette.Swatch vibrantDark = palette.getDarkVibrantSwatch();
                Palette.Swatch vibrantLight = palette.getLightVibrantSwatch();
                Palette.Swatch dominant = palette.getDominantSwatch();
                Palette.Swatch muted = palette.getMutedSwatch();
                Palette.Swatch mutedDark = palette.getDarkMutedSwatch();

                if (vibrant != null) {
                    GradientDrawable gd = new GradientDrawable();
                    gd.setColors(new int[]{
                            vibrantDark != null ? vibrantDark.getRgb() : dominant.getRgb(),
                            vibrant.getRgb(),
                            vibrantDark != null ? vibrantDark.getRgb() : dominant.getRgb(),
                    });
                    gd.setOrientation(GradientDrawable.Orientation.BL_TR);
                    miniPlayer.setBackground(gd);
                }
                if (vibrant == null) {
                    GradientDrawable gd = new GradientDrawable();
                    gd.setColors(new int[]{
                            mutedDark.getRgb(),
                            vibrantDark != null ? vibrantDark.getRgb() : muted.getRgb(),
                            mutedDark.getRgb(),
                    });
                    gd.setOrientation(GradientDrawable.Orientation.BL_TR);
                    miniPlayer.setBackground(gd);
                }
            }
        });
    }


}
