package com.cloudurable.jparse.token;

import com.cloudurable.jparse.source.CharSource;

public record Token(int startIndex, int endIndex, TokenType type) {

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
