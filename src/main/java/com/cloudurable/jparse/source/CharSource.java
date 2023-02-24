package com.cloudurable.jparse.source;

import com.cloudurable.jparse.node.support.NumberParseResult;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface CharSource {

    /**
     * Returns next character or ETX.
     */
    int next();

    int getIndex();

    char getCurrentChar();

    char getCurrentCharSafe();

    void skipWhiteSpace();

    char getChartAt(int index);

    String getString(int startIndex, int endIndex);

    double getDouble(int startIndex, int endIndex);

    float getFloat(int startIndex, int endIndex);

    int getInt(int startIndex, int endIndex);

    long getLong(int startIndex, int endIndex);

    CharSequence getCharSequence(int startIndex, int endIndex);

    char[] getArray(int startIndex, int endIndex);

    String getEncodedString(int start, int end);

    String toEncodedStringIfNeeded(int start, int end);

    BigDecimal getBigDecimal(int startIndex, int endIndex);

    BigInteger getBigInteger(int startIndex, int endIndex);

    int findEndOfEncodedString();

    int findEndString();

    NumberParseResult findEndOfNumber();

    int findFalseEnd();

    int findTrueEnd();

    int findNullEnd();


    boolean matchChars(int startIndex, int endIndex, CharSequence key);

    boolean isInteger(int startIndex, int endIndex);

    int nextSkipWhiteSpace();

    String errorDetails( String message, int index, int ch );


    boolean findCommaOrEnd();

    boolean findObjectEndOrAttributeSep();
}
