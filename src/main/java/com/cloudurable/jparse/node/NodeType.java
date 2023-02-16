package com.cloudurable.jparse.node;

import com.cloudurable.jparse.token.TokenType;

public enum NodeType {
    ROOT,
    OBJECT,
    ARRAY,
    INT,
    FLOAT,
    STRING,
    BOOLEAN,
    NULL,
    PATH_KEY,
    PATH_INDEX,
    OTHER;


    public static NodeType tokenTypeToElement(TokenType tokenType) {

        switch (tokenType) {
            case OBJECT:
                return NodeType.OBJECT;
            case ARRAY:
                return NodeType.ARRAY;
            case INT:
                return NodeType.INT;
            case FLOAT:
                return NodeType.FLOAT;
            case STRING:
                return NodeType.STRING;
            case BOOLEAN:
                return NodeType.BOOLEAN;
            case NULL:
                return NodeType.NULL;
            case PATH_KEY:
                return NodeType.PATH_KEY;
            case PATH_INDEX:
                return NodeType.PATH_INDEX;
            default:
                throw new IllegalStateException("" + tokenType);
        }

    }
}
