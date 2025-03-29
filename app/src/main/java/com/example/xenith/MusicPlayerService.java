package com.example.xenith;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicPlayerService extends Service implements
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener {

    private final IBinder musicBind = new MusicBinder();
    private MediaPlayer mediaPlayer;
    private List<Song> songs = new ArrayList<>();
    private int songPosition;
    private boolean shuffle = false;
    private Random random;

    @Override
    public void onCreate() {
        super.onCreate();
        random = new Random();
        mediaPlayer = new MediaPlayer();
        initMusicPlayer();
    }

    public void initMusicPlayer() {
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    public void setList(List<Song> songs) {
        this.songs = songs;
    }

    public class MusicBinder extends Binder {
        MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }

    public void playSong() {
        mediaPlayer.reset();
        Song playSong = songs.get(songPosition);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(playSong.getPath()));
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
    }

    public void setSong(int songIndex) {
        songPosition = songIndex;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mediaPlayer.getCurrentPosition() > 0) {
            mp.reset();
            playNext();
        }
    }

    public void playNext() {
        if (shuffle) {
            int newSong = songPosition;
            while (newSong == songPosition) {
                newSong = random.nextInt(songs.size());
            }
            songPosition = newSong;
        } else {
            songPosition = (songPosition + 1) % songs.size();
        }
        playSong();
    }

    public void playPrev() {
        if (shuffle) {
            int newSong = songPosition;
            while (newSong == songPosition) {
                newSong = random.nextInt(songs.size());
            }
            songPosition = newSong;
        } else {
            songPosition = (songPosition - 1 < 0) ? songs.size() - 1 : songPosition - 1;
        }
        playSong();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        // Send broadcast to update UI
        Intent onPreparedIntent = new Intent("MUSIC_PREPARED");
        sendBroadcast(onPreparedIntent);
    }

    public int getPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void pausePlayer() {
        mediaPlayer.pause();
    }

    public void seek(int position) {
        mediaPlayer.seekTo(position);
    }

    public void go() {
        mediaPlayer.start();
    }

    public void setShuffle() {
        shuffle = !shuffle;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}