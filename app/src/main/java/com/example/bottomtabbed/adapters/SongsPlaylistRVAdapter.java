package com.example.bottomtabbed.adapters;

//SongsPlaylistRVAdapter


import android.content.Context;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bottomtabbed.R;
import com.example.bottomtabbed.classes.Cancion;

import java.util.ArrayList;
import java.util.List;

public class SongsPlaylistRVAdapter extends RecyclerView.Adapter<SongsPlaylistRVAdapter.SongViewHolder>
        implements View.OnClickListener {

    // Codigo para la lista de CANCIONES ***** ***** ***** ***** ***** ***** ***** ***** *****
    // Listener para seleccionar cada cancion
    private View.OnClickListener listener;

    private Context mContext;

    // SongViewHolder
    //public static class SongViewHolder extends RecyclerView.ViewHolder{
    public class SongViewHolder extends RecyclerView.ViewHolder{

        TextView songNameArtist;
        ImageView songPhoto;

        public SongViewHolder(View itemView) {
            super(itemView);
            //cv = (CardView)itemView.findViewById(R.id.cv);
            songPhoto = (ImageView) itemView.findViewById(R.id.imagenIDCancionPlaylist);
            songNameArtist = (TextView)itemView.findViewById(R.id.nombreArtistaCancion);
        }
    }

    // Le asignamos al adaptador la lista de canciones
    ArrayList<Cancion> canciones;
    public SongsPlaylistRVAdapter(ArrayList<Cancion> canciones){
        this.canciones = canciones;
    }

    public SongsPlaylistRVAdapter(Context mContext, ArrayList<Cancion> canciones){
        this.canciones = canciones;
        this.mContext = mContext;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemcancionplaylist, viewGroup, false);
        SongViewHolder svh = new SongViewHolder(v);

        v.setOnClickListener(this);

        return svh;
    }

    @Override
    public void onBindViewHolder(SongViewHolder songViewHolder, int posicion) {

        String combinacionNA = (canciones.get(posicion).getTitle()) + " | ";
        combinacionNA +=canciones.get(posicion).getArtist();
        songViewHolder.songNameArtist.setText(combinacionNA);

        // Poner la caratula a la cancion
        /*byte[] image = image = obtenCaratulaAlbum(canciones.get(posicion).getPath());
        if ((image) != null){
            Glide.with(mContext).asBitmap()
                    .load(image)
                    .into(songpViewHolder.songPhoto);
        } else {
            songpViewHolder.songPhoto.setImageResource(R.drawable.artistas);
        }*/
        Log.i("ANUNCIO", "Creacion de las cards listo");
        songViewHolder.songPhoto.setImageResource(R.drawable.artistas);

    }


    @Override
    public int getItemCount() {
        return canciones.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    // Para poner los albumes de cada cancion
    private byte[] obtenCaratulaAlbum (String uri){

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte [] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }


    // Listener para seleccionar cada cancion
    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }


    //RecyclerView
    /*@NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }*/


    public void eliminarItem(int posicionCancion){
        canciones.remove(posicionCancion);
        notifyItemRemoved(posicionCancion);
    }

    public void restaurarItem(Cancion cancion, int posicion){
        canciones.add(cancion);
        notifyItemInserted(posicion);
    }
}
