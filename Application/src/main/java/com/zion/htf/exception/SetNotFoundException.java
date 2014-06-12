package com.zion.htf.exception;

public class SetNotFoundException extends Exception {
    public SetNotFoundException(int id){
        super(String.format("No set found matching id %d.", id));
    }
}
