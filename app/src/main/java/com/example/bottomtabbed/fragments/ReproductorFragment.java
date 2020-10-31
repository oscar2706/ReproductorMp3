package com.example.bottomtabbed.fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import com.example.bottomtabbed.R;
import com.example.bottomtabbed.classes.FragmentListener;
import com.example.bottomtabbed.classes.MusicPlayer;
import com.example.bottomtabbed.sheets.BottomSheet;

public class ReproductorFragment extends Fragment implements View.OnClickListener, MediaPlayer.OnCompletionListener {
    private volatile MusicPlayer musicPlayer = MusicPlayer.getInstance();

    final int ANIMATION_DURATION= 300;

    private ImageView imageView_Artwork;
    private Button playButton;
    private Button shuffleButton;
    private Button repeatButton;
    private CardView cardView_Artwork;
    private SeekBar seekBar_minute;
    private ConstraintLayout constraintLayout;
    private TextView txt_songArtist;
    private TextView txt_songName;
    private TextView txt_currentMinute;
    private TextView txt_duration;

    boolean isFavorite = true;
    boolean shuffleActive = false;

    //Fragment listener para comunicar que se cambio la canción
    public FragmentListener fragmentInteractionListener;

    public ReproductorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reproductor, container, false);

        constraintLayout = view.findViewById(R.id.playerLayout);
        txt_songArtist = view.findViewById(R.id.txt_songArtist);
        txt_songName = view.findViewById(R.id.txt_songName);
        txt_songArtist.setSelected(true);
        txt_songName.setSelected(true);
        playButton = view.findViewById(R.id.btn_play);
        repeatButton = view.findViewById(R.id.btn_repeat);
        shuffleButton = view.findViewById(R.id.btn_shuffle);

        playButton.setOnClickListener(this);
        repeatButton.setOnClickListener(this);
        shuffleButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        repeatButton.setOnClickListener(this);
        shuffleButton.setOnClickListener(this);

        if(musicPlayer.isShuffleActive()){
            shuffleButton.setBackgroundResource(R.drawable.ic_shuffle_on);
        } else {
            shuffleButton.setBackgroundResource(R.drawable.ic_shuffle_off);
        }

        if(musicPlayer.isRepeatActive()){
            repeatButton.setBackgroundResource(R.drawable.ic_repeat_on);
        } else {
            repeatButton.setBackgroundResource(R.drawable.ic_repeat_off);
        }

        view.findViewById(R.id.btn_next).setOnClickListener(this);
        view.findViewById(R.id.btn_previous).setOnClickListener(this);
        view.findViewById(R.id.btn_addToPlaylist).setOnClickListener(this);
        //view.findViewById(R.id.btnFavorite).setOnClickListener(this);
        //view.findViewById(R.id.btn_list).setOnClickListener(this);
        txt_duration = view.findViewById(R.id.txt_duration);
        txt_currentMinute = view.findViewById(R.id.txt_currentMinute);
        seekBar_minute = view.findViewById(R.id.seekBar_minute);
        seekBar_minute.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txt_currentMinute.setText(createTimeLabel(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicPlayer.seekTo(seekBar.getProgress());
            }
        });

        imageView_Artwork = view.findViewById(R.id.imageView);

        cardView_Artwork = view.findViewById(R.id.album_card);

        if (musicPlayer.isPlaying()) {
            view.findViewById(R.id.btn_play).setBackgroundResource(R.drawable.ic_pause);
            txt_songName.setText(musicPlayer.getPlayingSong().getTitle());
            txt_songArtist.setText(musicPlayer.getPlayingSong().getArtist());
        } else {
            cardView_Artwork.startAnimation(AnimationUtils.loadAnimation(
                    getContext(),
                    R.anim.zoom_out
            ));
            cardView_Artwork.setCardElevation(2f);
        }

        musicPlayer.setOnCompletionListener(this);
        updateSongInfo();

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play: {
                view.findViewById(R.id.btn_play).startAnimation(AnimationUtils.loadAnimation(
                        getContext(),
                        R.anim.bounce
                ));
                if (musicPlayer.isPlaying()) {
                    musicPlayer.pause();
                    fragmentInteractionListener.onFragmentInteraction(false);
                    cardView_Artwork.startAnimation(AnimationUtils.loadAnimation(
                            getContext(),
                            R.anim.zoom_out
                    ));
                    ObjectAnimator.ofFloat(cardView_Artwork, "elevation", 20)
                            .setDuration(ANIMATION_DURATION).start();
                    view.findViewById(R.id.btn_play).setBackgroundResource(R.drawable.ic_play);
                } else {
                    musicPlayer.start();
                    fragmentInteractionListener.onFragmentInteraction(true);
                    cardView_Artwork.startAnimation(AnimationUtils.loadAnimation(
                            getContext(),
                            R.anim.zoom_in
                    ));
                    ObjectAnimator.ofFloat(cardView_Artwork, "elevation", 80)
                            .setDuration(ANIMATION_DURATION).start();
                    view.findViewById(R.id.btn_play).setBackgroundResource(R.drawable.ic_pause);
                }
                break;
            }

            case R.id.btn_previous: {
                musicPlayer.playPrevious();
                updateSongInfo();
                playButton.setBackgroundResource(R.drawable.ic_pause);
                if (!musicPlayer.isPlaying()) {
                    cardView_Artwork.startAnimation(AnimationUtils.loadAnimation(
                            getContext(),
                            R.anim.zoom_in
                    ));
                    ObjectAnimator.ofFloat(cardView_Artwork, "elevation", 80)
                            .setDuration(ANIMATION_DURATION).start();
                }
                view.findViewById(R.id.btn_previous).startAnimation(AnimationUtils.loadAnimation(
                        getContext(),
                        R.anim.bounce
                ));
                fragmentInteractionListener.onFragmentInteraction(true);
                break;
            }

            case R.id.btn_next: {
                musicPlayer.playNext();
                updateSongInfo();
                playButton.setBackgroundResource(R.drawable.ic_pause);
                if (!musicPlayer.isPlaying()) {
                    cardView_Artwork.startAnimation(AnimationUtils.loadAnimation(
                            getContext(),
                            R.anim.zoom_in
                    ));
                    ObjectAnimator.ofFloat(cardView_Artwork, "elevation", 80)
                            .setDuration(ANIMATION_DURATION).start();
                }
                view.findViewById(R.id.btn_next).startAnimation(AnimationUtils.loadAnimation(
                        getContext(),
                        R.anim.bounce
                ));

                /*a lbum_card2.setVisibility(View.VISIBLE);
                album_card2.startAnimation(AnimationUtils.loadAnimation(
                        getContext(),
                        R.anim.move_to_left
                ));
                album_card2.setVisibility(View.GONE);*/
                fragmentInteractionListener.onFragmentInteraction(true);
                break;
            }
            /*case R.id.btnFavorite:
                if (isFavorite) {
                    view.findViewById(R.id.btnFavorite).startAnimation(AnimationUtils.loadAnimation(
                            getContext(),
                            R.anim.zoom_out
                    ));
                    view.findViewById(R.id.btnFavorite).setBackgroundResource(R.drawable.ic_favorite_off);
                    Toast.makeText(getActivity(), "Quitada de favoritos", Toast.LENGTH_SHORT).show();
                    isFavorite = false;
                } else {
                    view.findViewById(R.id.btnFavorite).setBackgroundResource(R.drawable.ic_favorite_on);
                    Toast.makeText(getActivity(), "Agregada a favoritos", Toast.LENGTH_SHORT).show();
                    isFavorite = true;
                    view.findViewById(R.id.btnFavorite).startAnimation(AnimationUtils.loadAnimation(
                            getContext(),
                            R.anim.zoom_in
                    ));
                }
                break;*/
            case R.id.btn_addToPlaylist:
                BottomSheet listMenu = new BottomSheet(musicPlayer.getPlayingSong().getTitle());
                //listMenu.setStyle(R.style.BottomSheetStyle, R.style.BottomListDialogTheme);
                listMenu.show(getFragmentManager(), "List");
                break;
            /*case R.id.btn_list:
                NavController navController;
                navController = Navigation.findNavController(view);
                Navigation.setViewNavController(view, navController);

                view.findViewById(R.id.btn_list).startAnimation(AnimationUtils.loadAnimation(
                        getContext(),
                        R.anim.bounce
                ));

                Navigation.findNavController(view).navigate(R.id.action_reproductor_to_nowPlaying);
                //navController.navigate(R.id.action_reproductor_to_nowPlaying);


                break;*/
            case R.id.btn_shuffle:
                view.findViewById(R.id.btn_shuffle).startAnimation(AnimationUtils.loadAnimation(
                        getContext(),
                        R.anim.bounce
                ));
                musicPlayer.toggleShufflePlaylist();
                if(musicPlayer.isShuffleActive()){
                    shuffleButton.setBackgroundResource(R.drawable.ic_shuffle_on);
                    shuffleButton.startAnimation(AnimationUtils.loadAnimation(
                            getContext(),
                            R.anim.zoom_in
                    ));
                } else {
                    shuffleButton.setBackgroundResource(R.drawable.ic_shuffle_off);
                    shuffleButton.startAnimation(AnimationUtils.loadAnimation(
                            getContext(),
                            R.anim.bounce
                    ));
                }
                break;
            case R.id.btn_repeat:
                musicPlayer.toggleRepeatPlaylist();
                if(musicPlayer.isRepeatActive()){
                    repeatButton.setBackgroundResource(R.drawable.ic_repeat_on);
                    repeatButton.startAnimation(AnimationUtils.loadAnimation(
                            getContext(),
                            R.anim.zoom_in
                    ));
                } else {
                    repeatButton.setBackgroundResource(R.drawable.ic_repeat_off);
                    repeatButton.startAnimation(AnimationUtils.loadAnimation(
                            getContext(),
                            R.anim.bounce
                    ));
                }

                break;
        }
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
                    constraintLayout.setBackground(gd);
                }
                if (vibrant == null) {
                    GradientDrawable gd = new GradientDrawable();
                    gd.setColors(new int[]{
                            mutedDark.getRgb(),
                            vibrantDark != null ? vibrantDark.getRgb() : muted.getRgb(),
                            mutedDark.getRgb(),
                    });
                    gd.setOrientation(GradientDrawable.Orientation.BL_TR);
                    constraintLayout.setBackground(gd);
                }
            }
        });
    }

    public void updateSongInfo() {
        Log.i("REPRODUCOR_FRAGMENT", "updateSongInfo ="
                + String.valueOf(musicPlayer.getPlayingSong().getTitle()));
        txt_songName.setText(musicPlayer.getPlayingSong().getTitle());
        txt_songArtist.setText(musicPlayer.getPlayingSong().getArtist());
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(musicPlayer.getPlayingSong().getPath());
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
        if (art != null) {
            imageView_Artwork.setImageBitmap(bitmap);
        } else {
            this.imageView_Artwork.setImageResource(R.drawable.artistas);
        }
        crearPalette(bitmap);

        final Handler mHandler = new Handler();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seekBar_minute.setProgress(musicPlayer.getCurrentPosition());
                mHandler.postDelayed(this, 500);
            }
        });
        txt_duration.setText(createTimeLabel(musicPlayer.getDuration()));
        seekBar_minute.setMax(musicPlayer.getDuration());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof FragmentListener) {
            fragmentInteractionListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + "Must implement");
        }
    }

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time/1000/60;
        int sec = time/1000%60;
        timeLabel = min +":";
        if(sec<10)
            timeLabel+="0";
        timeLabel += sec;
        return timeLabel;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        musicPlayer.playNext();
        updateSongInfo();
        fragmentInteractionListener.onFragmentInteraction(true);
    }
}
