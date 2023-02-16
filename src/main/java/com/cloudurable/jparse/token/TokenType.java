package com.cloudurable.jparse.token;

public enum TokenType {

    OBJECT(true),
    ATTRIBUTE_KEY(true),
    ATTRIBUTE_VALUE(true),
    ARRAY(true),
    INT(false),
    FLOAT(false),
    STRING(false),
    BOOLEAN(false),
    NULL(false),
    PATH_KEY(false),
    PATH_INDEX(true);
    private final boolean complex;

    TokenType(boolean complex) {
        this.complex = complex;
    }

    public boolean isComplex() {
        return complex;
    }

}
