package com.zion.htf;

public class Artist extends Item {
    protected String name;
    protected int type = Item.TYPE_ITEM;

    public Artist(){
        super();
    }

    public Artist(String name){
        super(name, TYPE_ITEM);
    }

    public Artist(String name, int type){
        super(name, type);
    }
}
