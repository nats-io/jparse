package com.cloudurable.jparse.source.support;

import com.cloudurable.jparse.source.CharSource;

public class UnexpectedCharacterException extends RuntimeException{

    public UnexpectedCharacterException( String whileDoing, String message, CharSource source, int ch, int index) {
       super(String.format("Unexpected character while %s, Error is '%s'. \n Details \n %s", whileDoing, message, source.errorDetails(message, index, ch)));
    }

    public UnexpectedCharacterException(String whileDoing, String message,  CharSource source, int ch) {
        this(whileDoing, message, source, ch, source.getIndex());

    }

    public UnexpectedCharacterException(String whileDoing, String message, CharSource source) {
        this(whileDoing, message, source, source.getCurrentCharSafe(), source.getIndex());
    }
}
