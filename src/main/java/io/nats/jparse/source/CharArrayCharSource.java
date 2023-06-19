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
package io.nats.jparse.source;

import io.nats.jparse.node.support.CharArrayUtils;
import io.nats.jparse.node.support.NumberParseResult;
import io.nats.jparse.node.support.ParseConstants;
import io.nats.jparse.source.support.CharArraySegment;
import io.nats.jparse.source.support.ParseDouble;
import io.nats.jparse.source.support.ParseFloat;
import io.nats.jparse.source.support.UnexpectedCharacterException;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CharArrayCharSource implements CharSource, ParseConstants {

    private final static char[] MIN_INT_CHARS = MIN_INT_STR.toCharArray();
    private final static char[] MAX_INT_CHARS = MAX_INT_STR.toCharArray();
    private final char[] data;
    private int index;

    public CharArrayCharSource(final char[] chars) {
        index = -1;
        data = chars;
    }

    public CharArrayCharSource(final String str) {
        index = -1;
        data = str.toCharArray();
    }

    public static String debugCharDescription(int c) {
        String charString;
        if (c == ' ') {
            charString = "[SPACE]";
        } else if (c == '\t') {
            charString = "[TAB]";
        } else if (c == '\n') {
            charString = "[NEWLINE]";
        } else if (c == ETX) {
            charString = "ETX";
        } else {
            charString = "'" + (char) c + "'";
        }

        charString = charString + " with an int value of " + c;
        return charString;
    }

    @Override
    public int next() {
        if (index + 1 >= data.length) {
            index = data.length;
            return ETX;
        }
        return data[++index];
    }

    @Override
    public void checkForJunk() {
        int index = this.index;
        final char[] data = this.data;
        final int length = data.length;
        int ch = ETX;

        for (; index < length; index++) {
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
        final char[] data = this.data;
        final int length = data.length;
        int ch = ETX;

        loop:
        for (; index < length; index++) {
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
        this.index = index;
        return index == length ? ETX : ch;
    }

    @Override
    public char skipWhiteSpace() {
        int index = this.index;
        final char[] data = this.data;
        final int length = data.length;

        char ch;

        loop:
        for (; index < length; index++) {
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
        this.index = index;
        return data[index];
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public char getCurrentChar() {
        return data[index];
    }

    @Override
    public char getCurrentCharSafe() {
        if (index >= data.length) {
            return ETX;
        }
        return data[index];
    }

    @Override
    public char getChartAt(int index) {
        return data[index];
    }

    @Override
    public String getString(int startIndex, int endIndex) {
        return new String(data, startIndex, endIndex - startIndex);
    }

    @Override
    public CharSequence getCharSequence(final int startIndex, final int endIndex) {
        return new CharArraySegment(startIndex, endIndex - startIndex, data);
    }

    @Override
    public char[] getArray(int startIndex, int endIndex) {
        final int length = endIndex - startIndex;
        char[] array = new char[length];
        System.arraycopy(data, startIndex, array, 0, length);
        return array;
    }

    @Override
    public BigDecimal getBigDecimal(int startIndex, int endIndex) {
        return new BigDecimal(data, startIndex, endIndex - startIndex);
    }

    @Override
    public BigInteger getBigInteger(int startIndex, int endIndex) {
        final int len = endIndex - startIndex;
        if (len > MAX_LONG_STR_LENGTH) {
            return getBigDecimal(startIndex, endIndex).toBigInteger();
        } else {
            long value = getLong(startIndex, endIndex);
            return BigInteger.valueOf(value);
        }
    }

    @Override
    public String getEncodedString(int start, int end) {
        return CharArrayUtils.decodeJsonString(data, start, end);
    }

    @Override
    public String toEncodedStringIfNeeded(int start, int end) {
        if (CharArrayUtils.hasEscapeChar(data, start, end)) {
            return getEncodedString(start, end);
        } else {
            return this.getString(start, end);
        }
    }

    @Override
    public String toString() {
        return new String(data);
    }

    @Override
    public NumberParseResult findEndOfNumberFast() {


        int i = index + 1;
        char ch = 0;
        final char[] data = this.data;
        final int length = data.length;
        for (; i < length; i++) {

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
                    return new NumberParseResult(i, false);

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
        return new NumberParseResult(i, false);

    }

    private NumberParseResult findEndOfFloatFast() {


        int i = index + 1;
        char ch = 0;
        final char[] data = this.data;
        final int length = data.length;

        for (; i < length; i++) {
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
                    return new NumberParseResult(i, true);

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
                    throw new UnexpectedCharacterException("Parsing JSON Float Number", "Unexpected character", this, ch, i);

            }

        }


        index = i;
        return new NumberParseResult(i, true);

    }

    private NumberParseResult parseFloatWithExponentFast() {

        int i = index + 1;
        char ch = 0;
        int signOperator = 0;
        final char[] data = this.data;
        final int length = data.length;
        for (; i < length; i++) {
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
                    return new NumberParseResult(i, true);

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
        return new NumberParseResult(i, true);

    }

    @Override
    public int findEndOfEncodedStringFast() {
        int i = ++index;
        final char[] data = this.data;
        final int length = data.length;
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
        final char[] data = this.data;
        final int length = data.length;
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
        final char[] data = this.data;
        final int length = data.length;
        char ch = 0;
        for (; i < length; i++) {
            ch = data[i];
            switch (ch) {
                case CONTROL_ESCAPE_TOKEN:
                    i = findEndOfStringControlEncode(i + 1);
                    continue;
                case STRING_END_TOKEN:
                    index = i + 1;
                    return i;
                default:
                    if (ch >= SPACE_WS) {
                        continue;
                    }
                    throw new UnexpectedCharacterException("Parsing JSON String", "Unexpected character while finding closing for String", this, ch, i);

            }
        }

        throw new UnexpectedCharacterException("Parsing JSON Encoded String", "Unable to find closing for String", this, ch, i);
    }

    private int findEndOfHexEncoding(int index) {
        final char[] data = this.data;
        final int length = data.length;

        if (isHex(data[++index]) && isHex(data[++index]) && isHex(data[++index]) && isHex(data[++index])) {
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
    public int findAttributeEnd() {
        int index = this.index;
        final char[] data = this.data;
        final int length = this.data.length;

        loop:
        for (; index < length; index++) {
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
        final char[] data = this.data;
        final int length = this.data.length;

        for (; index < length; index++) {
            if (data[index] == c) {
                this.index = index;
                return true;
            }
        }
        return false;
    }

    @Override
    public int findEndString() {

        int i = ++index;
        final char[] data = this.data;
        final int length = data.length;
        char ch = 0;

        for (; i < length; i++) {
            ch = data[i];
            if (ch == STRING_END_TOKEN) {
                index = i;
                return i;
            }
            if (ch >= SPACE_WS) {
                continue;
            }
            throw new UnexpectedCharacterException("Parsing JSON String", "Unexpected character while finding closing for String", this, ch, i);
        }
        throw new UnexpectedCharacterException("Parsing JSON String", "Unable to find closing for String", this, ch, i);
    }

    @Override
    public NumberParseResult findEndOfNumber() {

        final char startCh = getCurrentChar();
        final int startIndex = index;
        char ch = startCh;


        int i = index + 1;

        final char[] data = this.data;
        final int length = data.length;

        loop:
        for (; i < length; i++) {

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
                            throw new UnexpectedCharacterException("Parsing JSON Number", "Unexpected character", this, ch, i);
                        }
                    }
                    index = i;
                    return findEndOfFloat();


                case EXPONENT_MARKER:
                case EXPONENT_MARKER2:
                    index = i;
                    return parseFloatWithExponent();


                default:
                    throw new UnexpectedCharacterException("Parsing JSON Number", "Unexpected character", this, ch, i);

            }

        }

        index = i;
        final int numLength = i - startIndex;

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
        return new NumberParseResult(i, false);
    }

    private NumberParseResult findEndOfFloat() {

        int i = index + 1;
        char ch = (char) next();

        if (!isNumber(ch)) {
            throw new UnexpectedCharacterException("Parsing float part of number", "After decimal point expecting number but got", this, ch, this.index);
        }
        final char[] data = this.data;
        final int length = data.length;

        for (; i < length; i++) {
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
                    return new NumberParseResult(i, true);

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
                    throw new UnexpectedCharacterException("Parsing JSON Float Number", "Unexpected character", this, ch, i);

            }

        }


        index = i;
        return new NumberParseResult(i, true);

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
        char ch = (char) next();
        if (!isNumberOrSign(ch)) {
            throw new UnexpectedCharacterException("Parsing exponent part of float", "After exponent expecting number or sign but got", this, ch, this.index);
        }

        if (isSign(ch)) {
            ch = (char) next();
            if (!isNumber(ch)) {
                throw new UnexpectedCharacterException("Parsing exponent part of float after sign", "After sign expecting number but got", this, ch, this.index);
            }
        }

        int i = index + 1;
        final char[] data = this.data;
        final int length = data.length;

        for (; i < length; i++) {
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
                    return new NumberParseResult(i, true);

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
                    throw new UnexpectedCharacterException("Parsing Float with exponent", "Unable to find closing for Number", this, ch, i);

            }
        }
        index = i;
        return new NumberParseResult(i, true);
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
            return ++index;
        } else {
            throw new UnexpectedCharacterException("Parsing JSON False Boolean", "Unexpected character", this);

        }
    }

    @Override
    public int findTrueEnd() {
        if (this.data[++index] == 'r' && this.data[++index] == 'u' && this.data[++index] == 'e') {
            return ++index;
        } else {

            throw new UnexpectedCharacterException("Parsing JSON True Boolean", "Unexpected character", this);
        }
    }

    @Override
    public boolean findObjectEndOrAttributeSep() {
        int i = index;
        char ch = 0;
        final char[] data = this.data;
        final int length = data.length;

        for (; i < length; i++) {
            ch = data[i];
            switch (ch) {
                case OBJECT_END_TOKEN:
                    this.index = i + 1;
                    return true;
                case ATTRIBUTE_SEP:
                    this.index = i;
                    return false;
            }
        }


        throw new UnexpectedCharacterException("Parsing Object Key", "Finding object end or separator", this);
    }

    @Override
    public boolean findCommaOrEndForArray() {
        int i = index;
        char ch = 0;
        final char[] data = this.data;
        final int length = data.length;

        for (; i < length; i++) {
            ch = data[i];
            switch (ch) {
                case ARRAY_END_TOKEN:
                    this.index = i + 1;
                    return true;
                case ARRAY_SEP:
                    this.index = i;
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
            return ++index;
        } else {
            throw new UnexpectedCharacterException("Parsing JSON Null", "Unexpected character", this);
        }
    }

    @Override
    public boolean matchChars(final int startIndex, final int endIndex, CharSequence key) {

        final int length = endIndex - startIndex;
        int idx = startIndex;

        switch (length) {
            case 1:
                return key.charAt(0) == data[idx];
            case 2:
                return key.charAt(0) == data[idx] &&
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
                final int start = 0;
                final int end = length - 1;
                final int middle = length / 2;

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

    public boolean isInteger(int offset, int end) {
        int len = end - offset;
        final char[] digitChars = data;
        final boolean negative = (digitChars[offset] == '-');
        final int cmpLen = negative ? MIN_INT_STR_LENGTH : MAX_INT_STR_LENGTH;
        if (len < cmpLen) return true;
        if (len > cmpLen) return false;
        final char[] cmpStr = negative ? MIN_INT_CHARS : MAX_INT_CHARS;
        for (int i = 0; i < cmpLen; ++i) {
            int diff = digitChars[offset + i] - cmpStr[i];
            if (diff != 0) {
                return (diff < 0);
            }
        }
        return true;
    }

    @Override
    public double getDouble(int from, int to) {
        return ParseDouble.parseDouble(data, from, to);
    }

    @Override
    public float getFloat(int from, int to) {
        return ParseFloat.parseFloat(data, from, to);
    }

    @Override
    public int getInt(int offset, int to) {


        final char[] digitChars = data;

        int num;
        boolean negative = false;
        char c = digitChars[offset];
        if (c == '-') {
            offset++;
            negative = true;
        } else if (c == '+') {
            offset++;
            negative = false;
        }

        c = digitChars[offset];
        num = (c - '0');
        offset++;

        int digit;

        for (; offset < to; offset++) {
            c = digitChars[offset];
            digit = (c - '0');
            num = (num * 10) + digit;
        }

        return negative ? num * -1 : num;

    }

    @Override
    public long getLong(int offset, int to) {

        final char[] digitChars = data;

        long num;
        boolean negative = false;
        char c = digitChars[offset];
        if (c == '-') {
            offset++;
            negative = true;
        }

        c = digitChars[offset];
        num = (c - '0');
        offset++;

        long digit;

        for (; offset < to; offset++) {
            c = digitChars[offset];
            digit = (c - '0');
            num = (num * 10) + digit;
        }

        return negative ? num * -1 : num;

    }

    @Override
    public String errorDetails(String message, int index, int ch) {
        StringBuilder buf = new StringBuilder(255);

        final char[] array = data;

        buf.append(message).append("\n");


        buf.append("\n");
        buf.append("The current character read is " + debugCharDescription(ch)).append('\n');


        int line = 0;
        int lastLineIndex = 0;

        for (int i = 0; i < index && i < array.length; i++) {
            if (array[i] == '\n') {
                line++;
                lastLineIndex = i + 1;
            }
        }

        int count = 0;

        for (int i = lastLineIndex; i < array.length; i++, count++) {
            if (array[i] == '\n') {
                break;
            }
        }


        buf.append("line number " + (line + 1)).append('\n');
        buf.append("index number " + index).append('\n');


        try {
            buf.append(new String(array, lastLineIndex, count)).append('\n');
        } catch (Exception ex) {

            try {
                int start = index = (index - 10 < 0) ? 0 : index - 10;

                buf.append(new String(array, start, index)).append('\n');
            } catch (Exception ex2) {
                buf.append(new String(array)).append('\n');
            }
        }
        for (int i = 0; i < (index - lastLineIndex); i++) {
            buf.append('.');
        }
        buf.append('^');

        return buf.toString();
    }
}
