package com.t3h.demofragment.mediaplayer;

public class MusicOffline {
    private String path;
    private String name;
    private long duration;
    private long date;
    private long albumId;
    private String pathAlbum;

    public MusicOffline(String path, String name, long duration,
                        long date, long albumId) {
        this.path = path;
        this.name = name;
        this.duration = duration;
        this.date = date;
        this.albumId = albumId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getPathAlbum() {
        return pathAlbum;
    }

    public void setPathAlbum(String pathAlbum) {
        this.pathAlbum = pathAlbum;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
