package com.example.xenith;

import java.util.List;

public class Playlist {
    private String id;
    private String name;
    private int songCount;
    private List<Song> songs;

    public Playlist(String name, int songCount) {
        this.name = name;
        this.songCount = songCount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSongCount() {
        return songCount;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
        this.songCount = songs.size();
    }
}