package com.example.bottomtabbed.interfaces;

import androidx.recyclerview.widget.RecyclerView;

public interface CallBackItemTouch {
    void itemTouchOnMode(int posicionVieja, int posicionNueva);
    void deslizar(RecyclerView.ViewHolder viewHolder, int posicion);
}
