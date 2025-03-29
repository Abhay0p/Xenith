package com.example.xenith;

public class Song {
    private long id;
    private String title;
    private String artist;
    private String album;
    private long albumId;
    private long duration;
    private String path;

    public Song(long id, String title, String artist, String album, long albumId, long duration, String path) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumId = albumId;
        this.duration = duration;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public long getDuration() {
        return duration;
    }

    public String getPath() {
        return path;
    }
}