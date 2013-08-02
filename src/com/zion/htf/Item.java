package com.zion.htf;

public class Item {
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_SECTION = 1;

    protected String name;
    protected int type;

    public Item(String name){
        this.name = name;
        this.type = Item.TYPE_SECTION;
    }

    public Item(String name, int type){
        this.name = name;
        this.type = type;
    }

    public int getType(){
        return this.type;
    }

    public String toString(){
        return this.name;
    }
}
