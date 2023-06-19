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
package io.nats.jparse.node.support;

/**
 * Utility class for working with character arrays.
 */
public class CharArrayUtils {

    private CharArrayUtils(){}
    /**
     * Mapping of escape control characters.
     */
    static final char[] controlMap = new char[255];
    /**
     * Mapping of hex values.
     */
    static final int[] hexValueMap = new int[255];
    /**
     * Escape character code.
     */
    static final int ESCAPE = '\\';
    /**
     * Value of 10s place in hexadecimal.
     */
    private final static int HEX_10s = 16;
    /**
     * Value of 100s place in hexadecimal.
     */
    private final static int HEX_100s = 16 * 16;
    /**
     * Value of 1000s place in hexadecimal.
     */
    private final static int HEX_1000s = 16 * 16 * 16;

    /**

     Initializes the controlMap and hexValueMap arrays with mappings for escape control characters
     and hex values, respectively.
     */
    static {
        controlMap['n'] = '\n';
        controlMap['b'] = '\b';
        controlMap['/'] = '/';
        controlMap['f'] = '\f';
        controlMap['r'] = '\r';
        controlMap['t'] = '\t';
        controlMap['\\'] = '\\';
        controlMap['"'] = '"';
    }

    static {
        hexValueMap['0'] = 0;
        hexValueMap['1'] = 1;
        hexValueMap['2'] = 2;
        hexValueMap['3'] = 3;
        hexValueMap['4'] = 4;
        hexValueMap['5'] = 5;
        hexValueMap['6'] = 6;
        hexValueMap['7'] = 7;
        hexValueMap['8'] = 8;
        hexValueMap['9'] = 9;
        hexValueMap['a'] = 10;
        hexValueMap['b'] = 11;
        hexValueMap['c'] = 12;
        hexValueMap['d'] = 13;
        hexValueMap['e'] = 14;
        hexValueMap['f'] = 15;
        hexValueMap['A'] = 10;
        hexValueMap['B'] = 11;
        hexValueMap['C'] = 12;
        hexValueMap['D'] = 13;
        hexValueMap['E'] = 14;
        hexValueMap['F'] = 15;
    }

    /**
     * Decodes a JSON string from the specified character array within the specified range.
     *
     * @param chars      the character array containing the JSON string
     * @param startIndex the start index of the JSON string within the character array
     * @param endIndex   the end index of the JSON string within the character array (exclusive)
     * @return the decoded JSON string
     */
    public static String decodeJsonString(char[] chars, int startIndex, int endIndex) {
        int length = endIndex - startIndex;
        char[] builder = new char[calculateLengthAfterEncoding(chars, startIndex, endIndex, length)];
        char c;
        int index = startIndex;
        int idx = 0;

        while (true) {
            c = chars[index];
            if (c == '\\' && index < (endIndex - 1)) {
                index++;
                c = chars[index];
                if (c != 'u') {
                    builder[idx] = controlMap[c];
                    idx++;
                } else {

                    if (index + 4 < endIndex) {
                        char unicode = getUnicode(chars, index);
                        builder[idx] = unicode;
                        index += 4;
                        idx++;
                    }
                }

            } else {
                builder[idx] = c;
                idx++;
            }
            if (index >= (endIndex - 1)) {
                break;
            }
            index++;
        }
        return new String(builder);

    }

    /**
     * Retrieves the Unicode character from the specified character array at the given index.
     *
     * @param chars the character array
     * @param index the index of the Unicode character
     * @return the Unicode character
     */
    private static char getUnicode(char[] chars, int index) {
        int d4 = hexValueMap[chars[index + 1]];
        int d3 = hexValueMap[chars[index + 2]];
        int d2 = hexValueMap[chars[index + 3]];
        int d1 = hexValueMap[chars[index + 4]];
        return (char) (d1 + (d2 * HEX_10s) + (d3 * HEX_100s) + (d4 * HEX_1000s));
    }

    /**
     * Calculates the length of the character array after encoding, excluding escape control characters.
     *
     * @param chars      the character array
     * @param startIndex the start index of the range to calculate
     * @param endIndex   the end index of the range to calculate
     * @param length     the original length of the range
     * @return the length of the character array after encoding
     */
    private static int calculateLengthAfterEncoding(char[] chars, int startIndex, int endIndex, int length) {
        char c;
        int index = startIndex;
        int controlCharCount = length;

        while (true) {
            c = chars[index];
            if (c == '\\' && index < (endIndex - 1)) {
                index++;
                c = chars[index];
                if (c != 'u') {
                    controlCharCount -= 1;
                } else {

                    if (index + 4 < endIndex) {
                        controlCharCount -= 5;
                        index += 4;
                    }
                }

            }
            if (index >= (endIndex - 1)) {
                break;
            }
            index++;
        }
        return controlCharCount;
    }


    /**
     * Checks if the specified character array contains any escape characters.
     *
     * @param array      the character array to check
     * @param startIndex the start index of the range to check
     * @param endIndex   the end index of the range to check
     * @return true if the array contains an escape character, false otherwise
     */
    public static boolean hasEscapeChar(char[] array, int startIndex, int endIndex) {
        char currentChar;
        for (int index = startIndex; index < endIndex; index++) {
            currentChar = array[index];
            if (currentChar == ESCAPE) {
                return true;
            }

        }
        return false;
    }
}
