package com.example.bottomtabbed.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottomtabbed.R;
import com.example.bottomtabbed.classes.Playlist;

import java.util.ArrayList;

public class PlaylistsRecyclerViewAdapter extends RecyclerView.Adapter<PlaylistsRecyclerViewAdapter.PlaylistViewHolder>
        implements View.OnClickListener{


    // Codigo para la lista de PLAYLISTS ***** ***** ***** ***** ***** ***** ***** ***** *****
    // Listener para seleccionar cada cancion
    private View.OnClickListener listenerPlaylists;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.listenerPlaylists = onClickListener;
    }

    // PlaylistViewHolder (Pa llenar las cards con el holder)
    public class PlaylistViewHolder extends RecyclerView.ViewHolder {

        // Atributos para armar/llenar las cards de la lista de playlists (int, float, etc son TextView)
        ImageView playlistFoto;
        TextView playlistNombre, playlistCantidadCanciones;

        public PlaylistViewHolder(View itemView) {
            super(itemView);

            playlistFoto = (ImageView) itemView.findViewById(R.id.idImagePlaylist);
            playlistNombre = (TextView) itemView.findViewById(R.id.playlist_name);
            playlistCantidadCanciones = (TextView) itemView.findViewById(R.id.playlist_songsQuantity);
        }
    }

    // Creamos y le asignamos al adaptador la lista de artistas
    ArrayList<Playlist> playlists;
    public PlaylistsRecyclerViewAdapter(ArrayList<Playlist> playlists){
        this.playlists = playlists;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemplaylist, viewGroup, false);
        PlaylistViewHolder pvh = new PlaylistViewHolder(v);

        v.setOnClickListener(this);

        return pvh;
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder playlistViewHolder, int posicion) {

        // Atributos del playlist view holder:
        // playlistFoto = (ImageView)
        // playlistNombre = (TextView)
        // playlistCantidadCanciones = (TextView)

        // set nombre de la playlist
        playlistViewHolder.playlistNombre.setText(playlists.get(posicion).getNombrePlaylist());
        // set numero de canciones de la playlist
        String aux = " cancion";
        //Log.i("NUMERO:","->"+playlists.get(posicion).getCantidadCanciones());
        if ( !(playlists.get(posicion).getCantidadCanciones().equals("1")) ){ aux += "es";}
        playlistViewHolder.playlistCantidadCanciones.setText(
                String.valueOf( playlists.get(posicion).getCantidadCanciones() ) + aux
        );

    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    // Listener para seleccionar cada Artista
    @Override
    public void onClick(View view) {
        if(listenerPlaylists != null){
            listenerPlaylists.onClick(view);
        }
    }

}
