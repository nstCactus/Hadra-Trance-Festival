package com.zion.htf;

import java.util.Date;

public class Set extends Item {
    protected String name;
    protected int type = Item.TYPE_ITEM;

    /* Sets table fields */
    protected int id;
    protected String artist_name;
    protected String set_type;
    protected Date begin_date;
    protected Date end_date;
    protected String genre;
    protected String stage;
    protected String picture;
    protected int artist_id;

    public Set(String name){
        super(name, TYPE_ITEM);
    }

    public Set(String name, int type){
        super(name, type);
    }

    /* Getters & Setters */
    public String getSetType() {
        return set_type;
    }

    public Set setSetType(String setType) {
        this.set_type = setType;
        return this;
    }

    public Date getBeginDate() {
        return this.begin_date;
    }

    public Set setBeginDate(Date beginDate) {
        this.begin_date = beginDate;
        return this;
    }

    public Date getEndDate() {
        return this.end_date;
    }

    public Set setEndDate(Date endDate) {
        this.end_date = endDate;
        return this;
    }

    public String getGenre() {
        return this.genre;
    }

    public Set setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public String getStage() {
        return stage;
    }

    public Set setStage(String stage) {
        this.stage = stage;
        return this;
    }

    public String getArtistName() {
        return artist_name;
    }

    public Set setArtistName(String artistName) {
        this.artist_name = artistName;
        return this;
    }

    public int getId() {
        return id;
    }

    public Set setId(int id){
        this.id = id;
        return this;
    }

    public String getPicture(){
        return this.picture;
    }

    public Set setPicture(String picture){
        this.picture = picture;
        return this;
    }

    public int getArtistId(){
        return this.artist_id;
    }

    public Set setArtistId(int id){
        this.artist_id = id;
        return this;
    }
}
