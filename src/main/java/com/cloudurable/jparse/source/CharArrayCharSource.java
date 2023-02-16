package com.cloudurable.jparse.source;

import com.cloudurable.jparse.node.support.CharArrayUtils;
import com.cloudurable.jparse.node.support.NumberParseResult;
import com.cloudurable.jparse.node.support.ParseConstants;
import com.cloudurable.jparse.source.support.CharArraySegment;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CharArrayCharSource implements CharSource, ParseConstants {


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

    public CharArrayCharSource(final char[] chars) {
        index = -1;
        data = chars;
    }


    public CharArrayCharSource(final String str) {
        index = -1;
        data = str.toCharArray();
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
    public int nextSkipWhiteSpace() {
        int index = this.index + 1;
        final var data = this.data;
        final var length = data.length;

        int ch = ETX;


        loop:
        for (; index < length; index++ ) {
            ch = data[index];
            switch (ch) {
                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                    continue;
                default: break loop;
            }
        }

        this.index = index ;

        return ch;
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
        return getBigDecimal(startIndex, endIndex).toBigInteger();
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
    public int findEndOfEncodedString() {
        int i = ++index;
        final var data = this.data;
        final var length = data.length;
        boolean controlChar = false;
        for (; i < length; i++) {
            char ch = data[i];
            switch (ch) {
                case CONTROL_ESCAPE_TOKEN:
                    controlChar = true;
                    continue;
                case STRING_END_TOKEN:
                    if (!controlChar) {
                        index = i;
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

    @Override
    public int findEndString() {

        int i = ++index;
        final var data = this.data;
        final var length = data.length;

        for (; i < length; i++) {
            char ch = data[i];
            switch (ch) {
                case STRING_END_TOKEN:
                    index = i;
                    return i;
            }
        }
        throw new IllegalStateException("Unable to find closing for String");
    }


    @Override
    public NumberParseResult findEndOfNumber() {


        int i = index + 1;
        char ch = 0;
        for (; i < data.length; i++) {

            ch = data[i];

            switch (ch) {

                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                case ATTRIBUTE_SEP:
                case LIST_SEP:
                case OBJECT_END_TOKEN:
                case LIST_END_TOKEN:
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
                    return findEndOfFloat();


                case EXPONENT_MARKER:
                case EXPONENT_MARKER2:
                    index = i;
                    return parseFloatWithExponent();


                default:
                    throw new IllegalStateException("Unexpected character " + ch + " at index " + index);

            }

        }

        index = i;
        return new NumberParseResult(i, false);

    }

    private NumberParseResult findEndOfFloat() {


        int i = index + 1;
        char ch = 0;
        for (; i < data.length; i++) {
            ch = data[i];
            switch (ch) {

                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                case ATTRIBUTE_SEP:
                case LIST_SEP:
                case OBJECT_END_TOKEN:
                case LIST_END_TOKEN:
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
                    throw new IllegalStateException("Unexpected character " + ch + " at index " + index);

            }

        }


        index = i;
        return new NumberParseResult(i, true);

    }

    private NumberParseResult parseFloatWithExponent() {

        int i = index + 1;
        char ch = 0;
        int signOperator = 0;
        for (; i < data.length; i++) {
            ch = data[i];

            switch (ch) {

                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                case ATTRIBUTE_SEP:
                case LIST_SEP:
                case OBJECT_END_TOKEN:
                case LIST_END_TOKEN:
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
    public int findFalseEnd() {

        if (this.data[++index] == 'a' && this.data[++index] == 'l' && this.data[++index] == 's' && this.data[++index] == 'e') {
            return index;
        } else {
            throw new IllegalStateException("Unable to parse false");
        }
    }

    @Override
    public int findTrueEnd() {
        if (this.data[++index] == 'r' && this.data[++index] == 'u' && this.data[++index] == 'e') {
            return index;
        } else {
            throw new IllegalStateException("Unable to parse true");
        }
    }

    @Override
    public int findNullEnd() {
        if (this.data[++index] == 'u' && this.data[++index] == 'l' && this.data[++index] == 'l') {
            return index;
        } else {
            throw new IllegalStateException("Unable to parse null");
        }
    }

    @Override
    public boolean matchChars(final int startIndex, final int endIndex, CharSequence key) {

        final var length = endIndex - startIndex;
        var idx = startIndex;

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

    public boolean isInteger(int offset, int end) {
        int len = end - offset;
        final char[] digitChars = data;
        final var negative = (digitChars[offset] == '-');
        final var cmpLen = negative ? MIN_INT_STR_LENGTH : MAX_INT_STR_LENGTH;
        if (len < cmpLen) return true;
        if (len > cmpLen) return false;
        final var cmpStr = negative ? MIN_INT_CHARS : MAX_INT_CHARS;
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
        try {
            char[] buffer = data;
            final int length = index - from;
            double value = Double.NaN;
            boolean simple = true;
            int digitsPastPoint = 0;
            boolean negative = false;
            int index = from;
            if (buffer[index] == MINUS) {
                index++;
                negative = true;
            }
            boolean foundDot = false;
            int indexOfExponent = -1;
            loop:
            for (; index < to; index++) {
                char ch = buffer[index];

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
                        if (foundDot) {
                            digitsPastPoint++;
                        }
                        break;

                    case DECIMAL_POINT:
                        foundDot = true;
                        break;

                    case EXPONENT_MARKER:
                    case EXPONENT_MARKER2:
                        simple = false;
                        indexOfExponent = index + 1;
                        break loop;

                }
            }

            final int powLength = powersOf10.length;

            if (length >=  powLength ) {
                return Double.parseDouble(this.getString(from, to));
            }

            if (!simple) {
                long lvalue = parseLongFromToIgnoreDot(from, index);
                int exp = getInt(indexOfExponent, to);
                double power =  powersOf10[digitsPastPoint];
                value = (lvalue / power);
                double pow = Math.pow(10, exp);
                value = value * pow;
                //return Double.parseDouble(this.getString(from, to));
            } else if (!foundDot) {
                value = getLong(from, index);
            } else {
                long lvalue = parseLongFromToIgnoreDot(from, index);
                double power = powersOf10[digitsPastPoint];
                value = lvalue / power;
            }

            if (value == 0.0 && negative) {
                return -0.0;
            } else {
                return value;
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to parse " + getString(from, to), ex);
        }
    }

    @Override
    public float getFloat(int from, int to) {
        return (float) getDouble(from, to);
    }

    @Override
    public int getInt(int offset, int to) {


        final var digitChars = data;

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
    public long getLong(int offset, int to) {

        final var digitChars = data;

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
}
