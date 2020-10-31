package com.example.bottomtabbed.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumesRVAdapter extends RecyclerView.Adapter<AlbumesRVAdapter.AlbumViewHolder> {


    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }




    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
