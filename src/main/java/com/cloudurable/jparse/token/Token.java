package com.cloudurable.jparse.token;

import com.cloudurable.jparse.source.CharSource;

public class Token {

    public final int startIndex;
    public final int endIndex;
    public final int type;

    public Token(int startIndex, int endIndex, int type) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.type = type;
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

    @Override
    public String toString() {
        return "Token{" +
                "startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                ", type=" + TokenTypes.getTypeName(type) + " " + type +
                '}';
    }
}
