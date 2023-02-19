package com.cloudurable.jparse.token;

public interface TokenTypes {

    int OBJECT_TOKEN = 0;
    int ATTRIBUTE_KEY_TOKEN = 1;
    int ATTRIBUTE_VALUE_TOKEN = 2;
    int ARRAY_TOKEN = 3;
    int INT_TOKEN = 4;
    int FLOAT_TOKEN = 5;
    int STRING_TOKEN = 6;
    int BOOLEAN_TOKEN= 7;
    public static final int NULL_TOKEN = 8;
    int PATH_KEY_TOKEN = 9;
    int PATH_INDEX_TOKEN = 10;
}
