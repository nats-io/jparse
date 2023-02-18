package com.cloudurable.jparse.token;

import com.cloudurable.jparse.source.CharSource;

public class Token {

    private final int startIndex;
    private final int endIndex;
    private final int tokenType;

    public Token(final int startIndex, final int endIndex, final int tokenType) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.tokenType = tokenType;
    }

    public int startIndex() {
        return startIndex;
    }

    public int endIndex() {
        return endIndex;
    }



    public int type() {
        return tokenType;
    }

    public String asString(String buffer) {
        return buffer.substring(startIndex, endIndex);
    }

    public String asString(CharSource source) {
        return source.getString(startIndex, endIndex);
    }

    public int length() {
        return endIndex - startIndex;
    }
}
