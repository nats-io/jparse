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

import com.cloudurable.jparse.node.support.CharArrayUtils;
import com.cloudurable.jparse.node.support.NumberParseResult;
import com.cloudurable.jparse.node.support.ParseConstants;
import com.cloudurable.jparse.source.support.CharArraySegment;
import com.cloudurable.jparse.source.support.UnexpectedCharacterException;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CharArrayOffsetCharSource implements CharSource, ParseConstants {

    private final static char[] MIN_INT_CHARS = MIN_INT_STR.toCharArray();
    private final static char[] MAX_INT_CHARS = MAX_INT_STR.toCharArray();
    private static final double[] powersOf10 = {
            1.0,
            10.0,
            100.0,
            1_000.0,
            10_000.0,
            100_000.0,
            1_000_000.0,
            10_000_000.0,
            100_000_000.0,
            1_000_000_000.0,
            10_000_000_000.0,
            100_000_000_000.0,
            1_000_000_000_000.0,
            10_000_000_000_000.0,
            100_000_000_000_000.0,
            1_000_000_000_000_000.0,
            10_000_000_000_000_000.0,
            100_000_000_000_000_000.0,
            1_000_000_000_000_000_000.0,
            10_000_000_000_000_000_000.0,
            100_000_000_000_000_000_000.0,
            1_000_000_000_000_000_000_000.0,
            10_000_000_000_000_000_000_000.0,
            100_000_000_000_000_000_000_000.0,
            1_000_000_000_000_000_000_000_000.0,
            10_000_000_000_000_000_000_000_000.0,
            100_000_000_000_000_000_000_000_000.0,
            1_000_000_000_000_000_000_000_000_000.0,
            10_000_000_000_000_000_000_000_000_000.0,
            100_000_000_000_000_000_000_000_000_000.0,
            1_000_000_000_000_000_000_000_000_000_000.0,
            10_000_000_000_000_000_000_000_000_000_000.0,
            100_000_000_000_000_000_000_000_000_000_000.0,
            1_000_000_000_000_000_000_000_000_000_000_000.0,
            10_000_000_000_000_000_000_000_000_000_000_000.0,
            100_000_000_000_000_000_000_000_000_000_000_000.0,
            1_000_000_000_000_000_000_000_000_000_000_000_000.0,
            10_000_000_000_000_000_000_000_000_000_000_000_000.0,
            100_000_000_000_000_000_000_000_000_000_000_000_000.0,
            1_000_000_000_000_000_000_000_000_000_000_000_000_000.0,
            10_000_000_000_000_000_000_000_000_000_000_000_000_000.0,
            100_000_000_000_000_000_000_000_000_000_000_000_000_000.0,
            1_000_000_000_000_000_000_000_000_000_000_000_000_000_000.0,
            10_000_000_000_000_000_000_000_000_000_000_000_000_000_000.0,
            100_000_000_000_000_000_000_000_000_000_000_000_000_000_000.0,
            1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000.0,
            10_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000.0,
            100_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000.0,

    };
    private final char[] data;
    private int index;
    private final int  sourceStartIndex;
    private final int  sourceEndIndex;
    private final int length;

    public CharArrayOffsetCharSource(final int startIndex, final int endIndex, final char[] chars) {
        index =  startIndex -1;
        data = chars;
        sourceStartIndex = startIndex;
        sourceEndIndex = endIndex;
        length = endIndex - startIndex;
    }

    @Override
    public int next() {
        if (index + 1 >= sourceEndIndex) {
            index = sourceEndIndex;
            return ETX;
        }
        return data[++index];
    }

    @Override
    public int findAttributeEnd() {
        int index = this.index;
        final var data = this.data;
        final var end = this.sourceEndIndex;

        loop:
        for (; index < end; index++){
            char ch = data[index];
            switch (ch) {
                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                case ATTRIBUTE_SEP:
                    this.index = index;
                    break loop;
            }
        }

        return index;
    }

    @Override
    public boolean findChar(char c) {
        int index = this.index;
        final var data = this.data;
        final var end = sourceEndIndex;

        for (; index < end; index++){
            if (data[index]==c){
                this.index = index;
                return true;
            }
        }
        return false;
    }

    @Override
    public void checkForJunk() {
        int index = this.index;
        final var data = this.data;
        final var end = this.sourceEndIndex;
        int ch = ETX;

        for (; index < end; index++) {
            ch = data[index];
            switch (ch) {
                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                    continue;
                default:
                    throw new UnexpectedCharacterException("Junk", "Unexpected extra characters", this);

            }
        }
    }

    @Override
    public int nextSkipWhiteSpace() {
        int index = this.index + 1;
        final var data = this.data;
        final var endIndex = sourceEndIndex;
        int ch = ETX;

        loop:
        for (; index < endIndex; index++) {
            ch = data[index];
            switch (ch) {
                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                    continue;
                default:
                    break loop;
            }
        }
        this.index = index ;
        return index == endIndex ? ETX : ch;
    }

    @Override
    public char skipWhiteSpace() {
        int index = this.index;
        final var data = this.data;
        final var endIndex = sourceEndIndex;

        char ch;

        loop:
        for (; index < endIndex; index++) {
            ch = data[index];
            switch (ch) {
                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                    continue;
                default:
                    break loop;
            }
        }
        this.index = index ;
        return data[index];
    }

    @Override
    public int getIndex() {
        return index - sourceStartIndex;
    }


    @Override
    public char getCurrentChar() {
        return data[index];
    }

    @Override
    public char getCurrentCharSafe() {
        if (index  >= sourceEndIndex) {
            return ETX;
        }
        return data[index];
    }


    @Override
    public char getChartAt(final int index) {
        return data[index + sourceStartIndex];
    }

    @Override
    public String getString(int startIndex, int endIndex) {
        final int from = startIndex + sourceStartIndex;
        return new String(data, from, endIndex - startIndex);
    }

    @Override
    public CharSequence getCharSequence(final int startIndex, final int endIndex) {
        return new CharArraySegment(startIndex + sourceStartIndex, endIndex - startIndex, data);
    }

    @Override
    public char[] getArray(int startIndex, int endIndex) {
        final int length = endIndex - startIndex;
        char[] array = new char[length];
        System.arraycopy(data, startIndex + sourceStartIndex, array, 0, length);
        return array;
    }

    @Override
    public BigDecimal getBigDecimal(int startIndex, int endIndex) {
        return new BigDecimal(data, startIndex + sourceStartIndex, endIndex - startIndex);
    }

    @Override
    public BigInteger getBigInteger(int startIndex, int endIndex) {
        return getBigDecimal(startIndex, endIndex).toBigInteger();
    }

    @Override
    public String getEncodedString(int start, int end) {
        return CharArrayUtils.decodeJsonString(data, start + sourceStartIndex, end + sourceStartIndex);
    }

    @Override
    public String toEncodedStringIfNeeded(int startIndex, int endIndex) {
        final int start = startIndex + sourceStartIndex;
        final int end = endIndex + sourceStartIndex;

        if (CharArrayUtils.hasEscapeChar(data, start, end)) {
            return getEncodedString(startIndex, endIndex);
        } else {
            return this.getString(startIndex, endIndex);
        }
    }

    @Override
    public String toString() {
        return new String(data, sourceStartIndex, length);
    }


    @Override
    public NumberParseResult findEndOfNumberFast() {


        int i = index + 1;
        char ch = 0;
        final var data = this.data;
        final var endIndex = this.sourceEndIndex;
        for (; i < endIndex; i++) {

            ch = data[i];

            switch (ch) {

                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                case ATTRIBUTE_SEP:
                case ARRAY_SEP:
                case OBJECT_END_TOKEN:
                case ARRAY_END_TOKEN:
                    index = i;
                    return new NumberParseResult(i - sourceStartIndex, false);

                case NUM_0:
                case NUM_1:
                case NUM_2:
                case NUM_3:
                case NUM_4:
                case NUM_5:
                case NUM_6:
                case NUM_7:
                case NUM_8:
                case NUM_9:
                    break;

                case DECIMAL_POINT:
                    index = i;
                    return findEndOfFloatFast();


                case EXPONENT_MARKER:
                case EXPONENT_MARKER2:
                    index = i;
                    return parseFloatWithExponentFast();


                default:
                    throw new IllegalStateException("Unexpected character " + ch + " at index " + index);

            }

        }

        index = i;
        return new NumberParseResult(i - sourceStartIndex, false);

    }

    private NumberParseResult findEndOfFloatFast() {


        int i = index + 1;
        char ch =  0;
        final var data = this.data;
        final var endIndex = this.sourceEndIndex;

        for (; i < endIndex; i++) {
            ch = data[i];
            switch (ch) {

                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                case ATTRIBUTE_SEP:
                case ARRAY_SEP:
                case OBJECT_END_TOKEN:
                case ARRAY_END_TOKEN:
                    index = i;
                    return new NumberParseResult(i - sourceStartIndex, true);

                case NUM_0:
                case NUM_1:
                case NUM_2:
                case NUM_3:
                case NUM_4:
                case NUM_5:
                case NUM_6:
                case NUM_7:
                case NUM_8:
                case NUM_9:
                    break;

                case EXPONENT_MARKER:
                case EXPONENT_MARKER2:
                    index = i;
                    return parseFloatWithExponentFast();


                default:
                    throw new UnexpectedCharacterException("Parsing JSON Float Number", "Unexpected character", this,  ch, i);

            }

        }


        index = i;
        return new NumberParseResult(i - sourceStartIndex, true);

    }

    private NumberParseResult parseFloatWithExponentFast() {

        int i = index + 1;
        char ch = 0;
        int signOperator = 0;
        final var data = this.data;
        final var end = sourceEndIndex;
        for (; i < end; i++) {
            ch = data[i];

            switch (ch) {

                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                case ATTRIBUTE_SEP:
                case ARRAY_SEP:
                case OBJECT_END_TOKEN:
                case ARRAY_END_TOKEN:
                    index = i;
                    return new NumberParseResult(i - sourceStartIndex, true);

                case MINUS:
                case PLUS:
                    signOperator++;
                    if (signOperator > 1) {
                        throw new IllegalStateException("Too many sign operators when parsing exponent of float");
                    }
                    break;

                case NUM_0:
                case NUM_1:
                case NUM_2:
                case NUM_3:
                case NUM_4:
                case NUM_5:
                case NUM_6:
                case NUM_7:
                case NUM_8:
                case NUM_9:
                    break;


                default:
                    throw new IllegalStateException("Unexpected character " + ch + " at index " + index);

            }

        }


        index = i;
        return new NumberParseResult(i - sourceStartIndex, true);

    }

    @Override
    public int findEndOfEncodedStringFast() {
        int i = ++index;
        final var data = this.data;
        final var length = data.length;
        boolean controlChar = false;
        for (; i < length; i++) {
            char ch = data[i];
            switch (ch) {
                case CONTROL_ESCAPE_TOKEN:
                    controlChar = !controlChar;
                    continue;
                case STRING_END_TOKEN:
                    if (!controlChar) {
                        index = i + 1;
                        return i;
                    }
                    controlChar = false;
                    break;
                default:
                    controlChar = false;
                    break;

            }
        }
        throw new IllegalStateException("Unable to find closing for String");
    }

    private int findEndOfStringControlEncode(int i) {
        final var data = this.data;
        final var length = data.length;
        char ch = 0;


            ch = data[i];
            switch (ch) {
                case CONTROL_ESCAPE_TOKEN:
                case STRING_END_TOKEN:
                case 'n':
                case 'b':
                case '/':
                case 'r':
                case 't':
                case 'f':
                    return i;

                case 'u':
                    return findEndOfHexEncoding(i);

                default:
                    throw new UnexpectedCharacterException("Parsing JSON String", "Unexpected character while finding closing for String", this, ch, i);

            }

    }

    @Override
    public int findEndOfEncodedString() {
        int i = ++index;
        final var data = this.data;
        final var length = data.length;
        char ch = 0;
        for (; i < length; i++) {
            ch = data[i];
            switch (ch) {
                case CONTROL_ESCAPE_TOKEN:
                    i = findEndOfStringControlEncode(i + 1);
                    continue;
                case STRING_END_TOKEN:
                    index = i +1;
                    return i;
                default:
                    if (ch >= SPACE_WS) {
                        continue;
                    }
                    throw new UnexpectedCharacterException("Parsing JSON String", "Unexpected character while finding closing for String", this,  ch, i);

            }
        }

        throw new UnexpectedCharacterException("Parsing JSON Encoded String", "Unable to find closing for String", this, (int) ch, i);
    }


    private int findEndOfHexEncoding(int index) {
        final var data = this.data;
        final var length = data.length;

        if (isHex(data[++index]) && isHex(data[++index])  && isHex(data[++index])  && isHex(data[++index]) ) {
            return index;
        } else {
            throw new UnexpectedCharacterException("Parsing hex encoding in a string", "Unexpected character", this);
        }

    }

    private boolean isHex(char datum) {
        switch (datum) {
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return true;
            default:
                return false;
        }
    }

    @Override
    public int findEndString() {

        int i = ++index;
        final var data = this.data;
        final var length = data.length;
        char ch = 0;

        for (; i < length; i++) {
           ch = data[i];
            switch (ch) {
                case STRING_END_TOKEN:
                    index = i;
                    return i;
                default:
                    if (ch >= SPACE_WS) {
                        continue;
                    }
                    throw new UnexpectedCharacterException("Parsing JSON String", "Unexpected character while finding closing for String", this,  ch, i);
            }
        }
        throw new UnexpectedCharacterException("Parsing JSON String", "Unable to find closing for String", this,  ch, i);
    }


    @Override
    public NumberParseResult findEndOfNumber() {

        final char startCh = getCurrentChar();
        final int startIndex = index;
        char ch = startCh;


        int i = index + 1;

        final var data = this.data;
        final var endIndex = this.sourceEndIndex;

        loop:
        for (; i < endIndex; i++) {

            ch = data[i];

            switch (ch) {

                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                case ATTRIBUTE_SEP:
                case ARRAY_SEP:
                case OBJECT_END_TOKEN:
                case ARRAY_END_TOKEN:
                   break loop;

                case NUM_0:
                case NUM_1:
                case NUM_2:
                case NUM_3:
                case NUM_4:
                case NUM_5:
                case NUM_6:
                case NUM_7:
                case NUM_8:
                case NUM_9:
                    break;

                case DECIMAL_POINT:

                    if (startCh == MINUS) {
                        final int numLenSoFar = i - startIndex;
                        if (numLenSoFar == 1) {
                            throw new UnexpectedCharacterException("Parsing JSON Number", "Unexpected character", this,  ch, i);
                        }
                    }
                    index = i;
                    return findEndOfFloat();


                case EXPONENT_MARKER:
                case EXPONENT_MARKER2:
                    index = i;
                    return parseFloatWithExponent();


                default:
                    throw new UnexpectedCharacterException("Parsing JSON Number", "Unexpected character", this,  ch, i);

            }

        }

        index = i;
        final var numLength = i - startIndex;

        switch (startCh) {
            case NUM_0:
                if (numLength != 1) {
                    throw new UnexpectedCharacterException("Parsing JSON Int Number",
                            "Int can't start with a 0 ", this, startCh, startIndex);
                }
                break;
            case PLUS:
                throw new UnexpectedCharacterException("Parsing JSON Int Number",
                        "Int can't start with a plus ", this, startCh, startIndex);

            case MINUS:
                switch (numLength) {
                    case 1:
                        throw new UnexpectedCharacterException("Parsing JSON Int Number",
                                "Int can't be only a minus, number is missing", this, startCh, startIndex);
                    case 2:
                        break;
                    default:
                        if (data[startIndex + 1] == NUM_0) {

                            throw new UnexpectedCharacterException("Parsing JSON Int Number",
                                    "0 can't be after minus sign", this, startCh, startIndex);
                        }
                }
        }
        return new NumberParseResult(i - this.sourceStartIndex, false);
    }

    private NumberParseResult findEndOfFloat() {

        int i = index + 1;
        char ch =  (char) next();

        if (!isNumber(ch)) {
            throw new UnexpectedCharacterException("Parsing float part of number", "After decimal point expecting number but got", this, (int) ch, this.index);
        }
        final var data = this.data;

        final var endIndex = this.sourceEndIndex;

        for (; i < endIndex; i++) {
            ch = data[i];
            switch (ch) {

                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                case ATTRIBUTE_SEP:
                case ARRAY_SEP:
                case OBJECT_END_TOKEN:
                case ARRAY_END_TOKEN:
                    index = i;
                    return new NumberParseResult(i - sourceStartIndex, true);

                case NUM_0:
                case NUM_1:
                case NUM_2:
                case NUM_3:
                case NUM_4:
                case NUM_5:
                case NUM_6:
                case NUM_7:
                case NUM_8:
                case NUM_9:
                    break;

                case EXPONENT_MARKER:
                case EXPONENT_MARKER2:
                    index = i;
                    return parseFloatWithExponent();


                default:
                    throw new UnexpectedCharacterException("Parsing JSON Float Number", "Unexpected character", this,  ch, i);

            }

        }


        index = i;
        return new NumberParseResult(i - sourceStartIndex, true);

    }

    private boolean isNumber(final char ch) {
        switch (ch) {
            case NUM_0:
            case NUM_1:
            case NUM_2:
            case NUM_3:
            case NUM_4:
            case NUM_5:
            case NUM_6:
            case NUM_7:
            case NUM_8:
            case NUM_9:
                return true;
            default:
                return false;
        }
    }

    private NumberParseResult parseFloatWithExponent() {


        char ch =  (char) next();
        if (!isNumberOrSign(ch)) {
            throw new UnexpectedCharacterException("Parsing exponent part of float", "After exponent expecting number or sign but got", this, (int) ch, this.index);
        }

        if (isSign(ch)) {
            ch =  (char) next();
            if (!isNumber(ch)) {
                throw new UnexpectedCharacterException("Parsing exponent part of float after sign", "After sign expecting number but got", this, (int) ch, this.index);
            }
        }

        int i = index + 1;
        final var data = this.data;

        final var endIndex = this.sourceEndIndex;

        for (; i < endIndex; i++) {
            ch = data[i];

            switch (ch) {

                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                case ATTRIBUTE_SEP:
                case ARRAY_SEP:
                case OBJECT_END_TOKEN:
                case ARRAY_END_TOKEN:
                    index = i;
                    return new NumberParseResult(i - sourceStartIndex, true);

                case NUM_0:
                case NUM_1:
                case NUM_2:
                case NUM_3:
                case NUM_4:
                case NUM_5:
                case NUM_6:
                case NUM_7:
                case NUM_8:
                case NUM_9:
                    break;

                default:
                    throw new UnexpectedCharacterException("Parsing Float with exponent", "Unable to find closing for Number", this,  ch, i);

            }
        }
        index = i;
        return new NumberParseResult(i - sourceStartIndex, true);
    }

    private boolean isNumberOrSign(char ch) {
        switch (ch) {
            case NUM_0:
            case NUM_1:
            case NUM_2:
            case NUM_3:
            case NUM_4:
            case NUM_5:
            case NUM_6:
            case NUM_7:
            case NUM_8:
            case NUM_9:
            case MINUS:
            case PLUS:
                return true;
            default:
                return false;
        }
    }

    private boolean isSign(char ch) {
        switch (ch) {
            case MINUS:
            case PLUS:
                return true;
            default:
                return false;
        }
    }

    @Override
    public int findFalseEnd() {

        if (this.data[++index] == 'a' && this.data[++index] == 'l' && this.data[++index] == 's' && this.data[++index] == 'e') {
            return ++index - sourceStartIndex;
        } else {
            throw new UnexpectedCharacterException("Parsing JSON False Boolean", "Unexpected character", this);

        }
    }

    @Override
    public int findTrueEnd() {
        if (this.data[++index] == 'r' && this.data[++index] == 'u' && this.data[++index] == 'e') {
            return ++index - sourceStartIndex;
        } else {
            throw new UnexpectedCharacterException("Parsing JSON True Boolean", "Unexpected character", this);
        }
    }

    @Override
    public boolean findObjectEndOrAttributeSep() {
        int i = index;
        char ch = 0;
        final var data = this.data;
        final var end = sourceEndIndex;

        for (; i < end; i++) {
            ch = data[i];
            switch (ch) {
                case OBJECT_END_TOKEN:
                    this.index = i + 1;
                    return true;
                case ATTRIBUTE_SEP:
                    this.index = i ;
                    return false;
            }
        }



        throw new UnexpectedCharacterException("Parsing Object Key", "Finding object end or separator", this);
    }

    @Override
    public boolean findCommaOrEndForArray() {
        int i = index;
        char ch = 0;
        final var data = this.data;
        final var end = sourceEndIndex;

        for (; i < end; i++) {
            ch = data[i];
            switch (ch) {
                case ARRAY_END_TOKEN:
                    this.index = i + 1;
                    return true;
                case ARRAY_SEP:
                    this.index = i ;
                    return false;

                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                    continue;

                default:
                    throw new UnexpectedCharacterException("Parsing Object Key", "Finding object end or separator", this, ch, i);
            }
        }


        throw new UnexpectedCharacterException("Parsing Array", "Finding list end or separator", this);
    }

    @Override
    public int findNullEnd() {
        if (this.data[++index] == 'u' && this.data[++index] == 'l' && this.data[++index] == 'l') {
            return ++index - sourceStartIndex;
        } else {
            throw new UnexpectedCharacterException("Parsing JSON Null", "Unexpected character", this);
        }
    }

    @Override
    public boolean matchChars(final int startIndex, final int endIndex, CharSequence key) {

        final var length = endIndex - startIndex;
        final var offset = this.sourceStartIndex;
        var idx = startIndex + offset;


        switch (length) {
            case 1:
                return key.charAt(0) == data[idx];
            case 2:
                return key.charAt(0) == data[idx ] &&
                        key.charAt(1) == data[idx + 1];
            case 3:
                return key.charAt(0) == data[idx] &&
                        key.charAt(1) == data[idx + 1] &&
                        key.charAt(2) == data[idx + 2];
            case 4:
                return key.charAt(0) == data[idx] &&
                        key.charAt(1) == data[idx + 1] &&
                        key.charAt(2) == data[idx + 2] &&
                        key.charAt(3) == data[idx + 3];

            case 5:
                return key.charAt(1) == data[idx + 1] &&
                        key.charAt(3) == data[idx + 3] &&
                        key.charAt(0) == data[idx] &&
                        key.charAt(2) == data[idx + 2] &&
                        key.charAt(4) == data[idx + 4];

            case 6:
                return key.charAt(0) == data[idx] &&
                        key.charAt(5) == data[idx + 5] &&
                        key.charAt(3) == data[idx + 3] &&
                        key.charAt(1) == data[idx + 1] &&
                        key.charAt(2) == data[idx + 2] &&
                        key.charAt(4) == data[idx + 4];

            case 7:
                return key.charAt(0) == data[idx] &&
                        key.charAt(6) == data[idx + 6] &&
                        key.charAt(3) == data[idx + 3] &&
                        key.charAt(1) == data[idx + 1] &&
                        key.charAt(5) == data[idx + 5] &&
                        key.charAt(2) == data[idx + 2] &&
                        key.charAt(4) == data[idx + 4];

            case 8:
                return key.charAt(0) == data[idx] &&
                        key.charAt(7) == data[idx + 7] &&
                        key.charAt(3) == data[idx + 3] &&
                        key.charAt(1) == data[idx + 1] &&
                        key.charAt(5) == data[idx + 5] &&
                        key.charAt(2) == data[idx + 2] &&
                        key.charAt(6) == data[idx + 6] &&
                        key.charAt(4) == data[idx + 4];


            case 9:
                return key.charAt(0) == data[idx] &&
                        key.charAt(8) == data[idx + 8] &&
                        key.charAt(2) == data[idx + 2] &&
                        key.charAt(6) == data[idx + 6] &&
                        key.charAt(3) == data[idx + 3] &&
                        key.charAt(7) == data[idx + 7] &&
                        key.charAt(4) == data[idx + 4] &&
                        key.charAt(5) == data[idx + 5] &&
                        key.charAt(1) == data[idx + 1];

            case 10:
                return key.charAt(0) == data[idx] &&
                        key.charAt(9) == data[idx + 9] &&
                        key.charAt(6) == data[idx + 6] &&
                        key.charAt(3) == data[idx + 3] &&
                        key.charAt(7) == data[idx + 7] &&
                        key.charAt(2) == data[idx + 2] &&
                        key.charAt(4) == data[idx + 4] &&
                        key.charAt(5) == data[idx + 5] &&
                        key.charAt(1) == data[idx + 1] &&
                        key.charAt(8) == data[idx + 8];

            case 11:
                return key.charAt(0) == data[idx] &&
                        key.charAt(10) == data[idx + 10] &&
                        key.charAt(6) == data[idx + 6] &&
                        key.charAt(3) == data[idx + 3] &&
                        key.charAt(7) == data[idx + 7] &&
                        key.charAt(2) == data[idx + 2] &&
                        key.charAt(9) == data[idx + 9] &&
                        key.charAt(4) == data[idx + 4] &&
                        key.charAt(5) == data[idx + 5] &&
                        key.charAt(1) == data[idx + 1] &&
                        key.charAt(8) == data[idx + 8];

            case 12:
                return key.charAt(0) == data[idx] &&
                        key.charAt(11) == data[idx + 11] &&
                        key.charAt(3) == data[idx + 3] &&
                        key.charAt(7) == data[idx + 7] &&
                        key.charAt(2) == data[idx + 2] &&
                        key.charAt(6) == data[idx + 6] &&
                        key.charAt(9) == data[idx + 9] &&
                        key.charAt(4) == data[idx + 4] &&
                        key.charAt(5) == data[idx + 5] &&
                        key.charAt(10) == data[idx + 10] &&
                        key.charAt(1) == data[idx + 1] &&
                        key.charAt(8) == data[idx + 8];

            default:
                final var start = 0;
                final var end = length - 1;
                final var middle = length / 2;

                if (key.charAt(start) == data[idx] &&
                        key.charAt(end) == data[idx + end] &&
                        key.charAt(middle) == data[idx + middle]) {
                    for (int i = 1; i < length; i++) {
                        if (key.charAt(i) != data[idx + i]) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    return false;
                }
        }

    }

    public boolean isInteger(int startIndex, int endIndex) {
        int len = endIndex - startIndex;
        int offset = this.sourceStartIndex;
        final char[] digitChars = data;
        final var negative = (digitChars[startIndex] == '-');
        final var cmpLen = negative ? MIN_INT_STR_LENGTH : MAX_INT_STR_LENGTH;
        if (len < cmpLen) return true;
        if (len > cmpLen) return false;
        final var cmpStr = negative ? MIN_INT_CHARS : MAX_INT_CHARS;
        for (int i = 0; i < cmpLen; ++i) {
            int diff = digitChars[startIndex + i + offset] - cmpStr[i];
            if (diff != 0) {
                return (diff < 0);
            }
        }
        return true;
    }

    @Override
    public double getDouble(final int startIndex, final int endIndex) {

        return getBigDecimal(startIndex, endIndex).doubleValue();
//        final int offset = this.sourceStartIndex;
//        int from = startIndex + offset;
//        int to = endIndex + offset;
//        int index = from;

//
//
//        try {
//            char[] buffer = data;
//            final int length = endIndex - startIndex;
//            double value = Double.NaN;
//            boolean simple = true;
//            int digitsPastPoint = 0;
//            boolean negative = false;
//            if (buffer[index] == MINUS) {
//                index++;
//                negative = true;
//            }
//            boolean foundDot = false;
//            int indexOfExponent = -1;
//            loop:
//            for (; index < to; index++) {
//                char ch = buffer[index];
//
//                switch (ch) {
//                    case NUM_0:
//                    case NUM_1:
//                    case NUM_2:
//                    case NUM_3:
//                    case NUM_4:
//                    case NUM_5:
//                    case NUM_6:
//                    case NUM_7:
//                    case NUM_8:
//                    case NUM_9:
//                        if (foundDot) {
//                            digitsPastPoint++;
//                        }
//                        break;
//
//                    case DECIMAL_POINT:
//                        foundDot = true;
//                        break;
//
//                    case EXPONENT_MARKER:
//                    case EXPONENT_MARKER2:
//                        simple = false;
//                        indexOfExponent = index + 1;
//                        break loop;
//
//                }
//            }
//
//            final int powLength = powersOf10.length;
//
//            if (length >=  powLength ) {
//                return Double.parseDouble(this.getString(from, to));
//            }
//
//            if (!simple) {
//                long lvalue = parseLongFromToIgnoreDot(from, index);
//                int exp = getInt(indexOfExponent, to);
//                double power =  powersOf10[digitsPastPoint];
//                value = (lvalue / power);
//                double pow = Math.pow(10, exp);
//                value = value * pow;
//                //return Double.parseDouble(this.getString(from, to));
//            } else if (!foundDot) {
//                value = getLong(from, index);
//            } else {
//                long lvalue = parseLongFromToIgnoreDot(from, index);
//                double power = powersOf10[digitsPastPoint];
//                value = lvalue / power;
//            }
//
//            if (value == 0.0 && negative) {
//                return -0.0;
//            } else {
//                return value;
//            }
//        } catch (Exception ex) {
//            throw new UnexpectedCharacterException("Convert JSON number to Java double",
//                    "Unable to parse " + getString(from, to), this,  this.getChartAt(from), from);
//        }
    }

    @Override
    public float getFloat(int from, int to) {
        return (float) getDouble(from, to);
    }

    @Override
    public int getInt(int startIndex, int endIndex) {

        int from = startIndex + sourceStartIndex;
        int to = endIndex + sourceStartIndex;

        final var digitChars = data;

        int num;
        boolean negative = false;
        char c = digitChars[from];
        if (c == '-') {
            from++;
            negative = true;
        } else if (c == '+') {
            from++;
            negative = false;
        }

        c = digitChars[from];
        num = (c - '0');
        from++;

        int digit;

        for (; from < to; from++) {
            c = digitChars[from];
            digit = (c - '0');
            num = (num * 10) + digit;
        }

        return negative ? num * -1 : num;

    }

    public long parseLongFromToIgnoreDot(int offset, int to) {


        final var digitChars = this.data;

        long num;
        boolean negative = false;
        char c = digitChars[offset];
        if (c == MINUS) {
            offset++;
            negative = true;
        }

        c = digitChars[offset];
        num = (c - NUM_0);
        offset++;

        loop:
        for (; offset < to; offset++) {
            c = digitChars[offset];
            switch (c) {
                case DECIMAL_POINT:
                    break;
                case NUM_0:
                case NUM_1:
                case NUM_2:
                case NUM_3:
                case NUM_4:
                case NUM_5:
                case NUM_6:
                case NUM_7:
                case NUM_8:
                case NUM_9:
                    num = (num * 10) + (c - NUM_0);
                    break;


                case EXPONENT_MARKER:
                case EXPONENT_MARKER2:
                    break loop;

            }

        }

        return negative ? num * -1 : num;
    }

    @Override
    public long getLong(final int startIndex, final int endIndex) {
        int from = startIndex + sourceStartIndex;
        int to = endIndex + sourceStartIndex;

        final var digitChars = data;

        long num;
        boolean negative = false;
        char c = digitChars[from];
        if (c == '-') {
            from++;
            negative = true;
        }

        c = digitChars[from];
        num = (c - '0');
        from++;

        long digit;

        for (; from < to; from++) {
            c = digitChars[from];
            digit = (c - '0');
            num = (num * 10) + digit;
        }

        return negative ? num * -1 : num;

    }


    @Override
    public String errorDetails( String message, int index, int ch ) {
        StringBuilder buf = new StringBuilder(255);

        final var array = data;

        buf.append( message ).append("\n");


        buf.append("\n");
        buf.append( "The current character read is " + debugCharDescription( ch ) ).append('\n');


        int line = 0;
        int lastLineIndex = 0;

        for ( int i = 0; i < index && i < array.length; i++ ) {
            if ( array[ i ] == '\n' ) {
                line++;
                lastLineIndex = i + 1;
            }
        }

        int count = 0;

        for ( int i = lastLineIndex; i < array.length; i++, count++ ) {
            if ( array[ i ] == '\n' ) {
                break;
            }
        }


        buf.append( "line number " + (line + 1) ).append('\n');
        buf.append( "index number " + index ).append('\n');
        buf.append( "offset index number " + index + sourceStartIndex).append('\n');


        try {
            buf.append( new String( array, lastLineIndex, count ) ).append('\n');
        } catch ( Exception ex ) {

            try {
                int start =  index = ( index - 10 < 0 ) ? 0 : index - 10;

                buf.append( new String( array, start, index ) ).append('\n');
            } catch ( Exception ex2 ) {
                buf.append( new String( array, 0, array.length ) ).append('\n');
            }
        }
        for ( int i = 0; i < ( index - lastLineIndex ); i++ ) {
            buf.append( '.' );
        }
        buf.append( '^' );

        return buf.toString();
    }



    public static String debugCharDescription( int c ) {
        String charString;
        if ( c == ' ' ) {
            charString = "[SPACE]";
        } else if ( c == '\t' ) {
            charString = "[TAB]";
        } else if ( c == '\n' ) {
            charString = "[NEWLINE]";
        } else if ( c == ETX) {
            charString = "ETX";
        } else{
            charString = "'" + (char)c + "'";
        }

        charString = charString + " with an int value of " + ( ( int ) c );
        return charString;
    }
}
