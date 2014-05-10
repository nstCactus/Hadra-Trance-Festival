package com.zion.htf.data;

public class Artist{
    /* Sets table fields */
    protected int    id;
    protected String name;
    protected String artist_name;
    protected String set_type;
    protected String genre;
    protected String picture;

    public Artist(String name){
        this.artist_name = name;
    }

    public String getArtistName(){
        return this.artist_name;
    }

    public String getSetType(){
        return this.set_type;
    }

    public Artist setSetType(String set_type) {
        this.set_type = set_type;
        return this;
    }

    public String getGenre() {
        return genre;
    }

    public Artist setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public Artist setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public int getId() {
        return id;
    }

    public Artist setId(int id) {
        this.id = id;
        return this;
    }
}