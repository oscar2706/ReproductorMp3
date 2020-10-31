package com.example.bottomtabbed.classes;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusicPlayer extends MediaPlayer implements MediaPlayer.OnPreparedListener {
    private static MusicPlayer instance;
    private ArrayList<Cancion> playlist = new ArrayList<>();
    private ArrayList<Cancion> playlistOrderBackup = new ArrayList<>();
    private Cancion playingSong;
    private int positionInList = -1;
    private boolean repeatPlaylist = false;
    private boolean shuffleMode= false;

    private MusicPlayer() {
    }

    public synchronized static MusicPlayer getInstance() {
        if (instance == null)
            instance = new MusicPlayer();
        return instance;
    }

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, IllegalStateException, SecurityException {
        super.setDataSource(path);
    }

    public void setPlaylist(ArrayList<Cancion> playlist) {
        this.playlist = playlist;
        this.playlistOrderBackup = null;
        if(shuffleMode){
            shuffleMode = false;
            toggleShufflePlaylist();
        }
        positionInList = 0;
        Log.i("MusicPlayer", "playlist.size = " + String.valueOf(this.playlist.size()));
    }

    public Cancion getPlayingSong() {
        return playingSong;
    }

    public void play() {
        this.prepareAsync();
    }

    public boolean playSong(String name) {
        boolean foundSong = false;
        this.reset();
        int i = 0;
        for (Cancion cancion : playlist) {
            if (cancion.getTitle().equals(name)) {
                foundSong = true;
                try {
                    playingSong = cancion;
                    positionInList = i;
                    Log.i("i", String.valueOf(i));
                    this.setDataSource(cancion.getPath());
                    this.setOnPreparedListener(this);
                    this.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            i++;
        }
        return foundSong;
    }

    public void playPause() {
        if (isPlaying())
            this.pause();
        else
            this.start();
    }

    public boolean playPrevious() {
        if (positionInList == -1
                || positionInList > playlist.size() - 1
                || playlist.isEmpty()) {
            Log.i("MUSIC_PLAYER", "NO PREVIOUS i=" + String.valueOf(positionInList));
            return false;
        } else {
            if(getCurrentPosition()/1000%60 < 3){
                this.reset();
                if (repeatPlaylist && positionInList == 0)
                    positionInList = playlist.size();
                if (!repeatPlaylist && positionInList == 0)
                    positionInList = 1;
                try {
                    this.setDataSource(playlist.get(--positionInList).getPath());
                    Log.i("i", String.valueOf(positionInList));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                playingSong = playlist.get(positionInList);
                this.setOnPreparedListener(this);
                this.prepareAsync();
            } else {
                this.reset();
                try {
                    this.setDataSource(playlist.get(positionInList).getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.setOnPreparedListener(this);
                this.prepareAsync();
            }
            return true;
        }
    }

    public boolean playNext() {
        if (positionInList == -1
                || positionInList > playlist.size() - 1
                || playlist.isEmpty())
            return false;
        else {
            this.reset();
            if (repeatPlaylist && positionInList == playlist.size() - 1)
                positionInList = -1;
            if (!repeatPlaylist && positionInList == playlist.size() - 1)
                positionInList = playlist.size() - 2;
            try {
                this.setDataSource(playlist.get(++positionInList).getPath());
                Log.i("i", String.valueOf(positionInList));
            } catch (IOException e) {
                e.printStackTrace();
            }
            playingSong = playlist.get(positionInList);
            this.setOnPreparedListener(this);
            this.prepareAsync();
            return true;
        }
    }

    public void toggleRepeatPlaylist() {
        repeatPlaylist = !repeatPlaylist;
        Log.i("MUSIC_PLAYER", "repeat =" + String.valueOf(repeatPlaylist));
    }

    public void toggleShufflePlaylist() {
        if(!shuffleMode){
            List<Cancion> shufflePlaylist = (List<Cancion>) playlist.clone();
            Collections.shuffle(shufflePlaylist);
            playlistOrderBackup = playlist;
            playlist = (ArrayList<Cancion>) shufflePlaylist;
            shuffleMode = true;
        } else {

            for (int i = 0; i < playlist.size()-1; i++){
                Log.i("PLAYING SONG",playingSong.getTitle());
                if(playlistOrderBackup.get(i).getTitle().equals(playingSong.getTitle())){
                    positionInList = i;
                    Log.i("positionInList", String.valueOf(positionInList));
                    break;
                }
            }
            playlist = playlistOrderBackup;
            shuffleMode = false;
        }
    }

    public boolean isRepeatActive() {
        return repeatPlaylist;
    }

    public boolean isShuffleActive() {
        return shuffleMode;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

}
