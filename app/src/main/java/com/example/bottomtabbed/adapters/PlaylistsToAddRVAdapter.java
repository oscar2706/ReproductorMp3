package com.example.bottomtabbed.adapters;

import android.content.Context;
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

public class PlaylistsToAddRVAdapter extends RecyclerView.Adapter<PlaylistsToAddRVAdapter.PlaylistToAddHolder>
        implements View.OnClickListener {


    // Lista de las playlists disponibles para agregar X cancion
    ArrayList<Playlist> listasParaAgregar;

    // Listener para seleccionar cada lista
    private View.OnClickListener listener;

    private Context mContext;

    // Constructor
    public PlaylistsToAddRVAdapter(ArrayList<Playlist> listasParaAgregar) {
        this.listasParaAgregar = listasParaAgregar;
    }
    // Constructor II
    public PlaylistsToAddRVAdapter(Context mContext, ArrayList<Playlist> listasParaAgregar){
        this.mContext = mContext;
        this.listasParaAgregar = listasParaAgregar;
    }

    //
    // Clase PlaylistToAddHolder!
    public class PlaylistToAddHolder extends RecyclerView.ViewHolder {

        TextView playlistToAddNombre, playlistToAddQuantite;
        ImageView playlistToAddPhoto;

        public PlaylistToAddHolder(@NonNull View itemView) {
            super(itemView);

            // Referencia a las cards de las playlists
            playlistToAddNombre = (TextView) itemView.findViewById(R.id.playlistToAddName);
            playlistToAddQuantite = (TextView) itemView.findViewById(R.id.playlistToAddNSongs);
            playlistToAddPhoto = (ImageView) itemView.findViewById(R.id.playlistToAddImage);
        }
    }

    @NonNull
    @Override
    public PlaylistToAddHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from
                (parent.getContext()).inflate(R.layout.item_playlistlisttoadd, parent, false);

        PlaylistToAddHolder ptah = new PlaylistToAddHolder(v);
        v.setOnClickListener(this);
        return ptah;
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    // Listener para seleccionar cada playlist
    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistToAddHolder playlistHolder, int position) {
        // Atributos de PlaylistToAddHolder
        // TextView playlistToAddNombre, playlistToAddQuantite;
        // ImageView playlistToAddPhoto;
        String nombreP = listasParaAgregar.get(position).getNombrePlaylist();
        String cancionesP = listasParaAgregar.get(position).getCantidadCanciones();

        playlistHolder.playlistToAddPhoto.setImageResource(R.drawable.playlists);
        playlistHolder.playlistToAddNombre.setText(nombreP);
        playlistHolder.playlistToAddQuantite.setText(cancionesP);
    }

    @Override
    public int getItemCount() {
        return listasParaAgregar.size();
    }
}
