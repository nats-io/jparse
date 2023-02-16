package com.cloudurable.jparse.node;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface ScalarNode extends Node{

    @Override
    default boolean isScalar() {
        return true;
    }

    @Override
    default boolean isCollection() {
        return false;
    }


    Object value();

    default boolean booleanValue()  {
        throw new UnsupportedOperationException();
    }

    default int intValue()  {
        throw new UnsupportedOperationException();
    }

    default long longValue()  {
        throw new UnsupportedOperationException();
    }

    default double doubleValue()  {
        throw new UnsupportedOperationException();
    }

    default BigDecimal bigDecimalValue()  {
        throw new UnsupportedOperationException();
    }

    default BigInteger bigIntegerValue()  {
        throw new UnsupportedOperationException();
    }

    default CharSequence charSequenceValue()  {
       return this.originalCharSequence();
    }

    default String stringValue()  {
        return this.originalString();
    }

    default boolean equalsString(String str) {
        return this.equals(str);
    }
}
