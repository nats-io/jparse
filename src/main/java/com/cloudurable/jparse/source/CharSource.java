/*
 * Copyright 2013-2023 Richard M. Hightower
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
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

    char skipWhiteSpace();

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


    boolean findCommaOrEndForArray();

    boolean findObjectEndOrAttributeSep();

    void checkForJunk();

    NumberParseResult findEndOfNumberFast();

    int findEndOfEncodedStringFast();

    boolean findChar(char c);

    int findAttributeEnd();
}
