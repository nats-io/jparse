package com.cloudurable.jparse.token;

public interface TokenTypes {

    int OBJECT_TOKEN = 0;
    int ATTRIBUTE_KEY_TOKEN = 1;
    int ATTRIBUTE_VALUE_TOKEN = 2;
    int ARRAY_TOKEN = 3;
    int ARRAY_ITEM_TOKEN = 4;


    int INT_TOKEN = 5;
    int FLOAT_TOKEN = 6;
    int STRING_TOKEN = 7;
    int BOOLEAN_TOKEN = 8;
    int NULL_TOKEN = 9;
    int PATH_KEY_TOKEN = 10;
    int PATH_INDEX_TOKEN = 11;

    static String getTypeName(final int tokenType) {
        return switch (tokenType) {
            case OBJECT_TOKEN -> "Object";
            case ATTRIBUTE_KEY_TOKEN -> "Key";
            case ATTRIBUTE_VALUE_TOKEN -> "Attribute Value";
            case ARRAY_TOKEN -> "Array";
            case ARRAY_ITEM_TOKEN -> "Array Item";
            case INT_TOKEN -> "Integer";
            case FLOAT_TOKEN -> "Float";
            case STRING_TOKEN -> "String";
            case BOOLEAN_TOKEN -> "Boolean";
            case NULL_TOKEN -> "Null";
            default -> "" + tokenType;

        };
    }
}
