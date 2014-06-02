package com.zion.htf.exception;

/**
 * Created by sbooob on 29/05/2014.
 */
public class SetNotFoundException extends Exception {
    public SetNotFoundException(int id){
        super(String.format("No set found matching id %d.", id));
    }
}
