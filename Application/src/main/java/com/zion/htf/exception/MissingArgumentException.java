package com.zion.htf.exception;

public class MissingArgumentException extends Exception{
    public MissingArgumentException(String name, String type){
        super(String.format("Missing \"%s\" parameter of type %s."));
    }

    public MissingArgumentException(String message){
        super(message);
    }
}
