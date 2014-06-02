package com.zion.htf.exception;

public class ArtistNotFoundException extends Exception{
    public ArtistNotFoundException(int id){
        super(String.format("No artist found matching id %d.", id));
    }

    public ArtistNotFoundException(String message){
        super(message);
    }
}
