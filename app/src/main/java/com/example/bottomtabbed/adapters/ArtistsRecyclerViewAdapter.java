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
import com.example.bottomtabbed.classes.Artista;

import java.util.ArrayList;

public class ArtistsRecyclerViewAdapter extends RecyclerView.Adapter<ArtistsRecyclerViewAdapter.ArtistViewHolder>
        implements View.OnClickListener {

    // Codigo para la lista de ARTISTAS ***** ***** ***** ***** ***** ***** ***** ***** *****

    // Listener para seleccionar cada cancion
    private View.OnClickListener listenerCanciones;

    // ArtistViewHolder
    public class ArtistViewHolder extends RecyclerView.ViewHolder {

        // Atributos para armar/llenar las cards de la lista de artistas (int, float, etc son TextView)
        ImageView artistPhoto;
        TextView artistName, artistsSongsQuantity;

        public ArtistViewHolder(View itemView) {
            super(itemView);

            artistPhoto = (ImageView) itemView.findViewById(R.id.idImageArtist);
            artistName = (TextView) itemView.findViewById(R.id.artist_name);
            artistsSongsQuantity = (TextView) itemView.findViewById(R.id.artist_songsQuantity);
        }
    }

    // Creamos y le asignamos al adaptador la lista de artistas
    ArrayList<Artista> artistas;
    public ArtistsRecyclerViewAdapter(ArrayList<Artista> artistas){
        this.artistas = artistas;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemartist, viewGroup, false);
        ArtistViewHolder avh = new ArtistViewHolder(v);

        v.setOnClickListener(this);

        return avh;
    }


    @Override
    public void onBindViewHolder(ArtistViewHolder artistViewHolder, int posicion) {

        /*Log.i("Errores *****", "Llamada a onBindViewHolder F2 " + " " +
                artistas.get(posicion).getNombreArtista() + " " +
                artistas.get(posicion).getCantidadCanciones() + " " +
                artistas.get(posicion).getIdImagenArtista());*/

        //artistViewHolder.artistPhoto.setImageResource(artistas.get(posicion).getIdImagenArtista());
        artistViewHolder.artistName.setText(artistas.get(posicion).getNombreArtista());
        String aux = " cancion";
        //Log.i("NUMERO:","->"+artistas.get(posicion).getCantidadCanciones());
        if ( !(artistas.get(posicion).getCantidadCanciones().equals("1")) ){ aux += "es";}
        artistViewHolder.artistsSongsQuantity.setText
                ( (artistas.get(posicion).getCantidadCanciones()) + aux);
    }

    @Override
    public int getItemCount() {
        return artistas.size();
    }

    public void setOnClickListener(View.OnClickListener listenerCanciones){
        this.listenerCanciones = listenerCanciones;
    }

    // Listener para seleccionar cada Artista
    @Override
    public void onClick(View view) {
        if(listenerCanciones != null){
            listenerCanciones.onClick(view);
        }
    }
}

