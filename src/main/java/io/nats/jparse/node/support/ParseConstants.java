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
 * The ParseConstants class provides a Java interface that defines constants used for parsing JSON strings.
 * It includes a number of integer constants, such as tokens for object and array delimiters, as well as
 * string constants for the minimum and maximum values of integers and longs. The interface also includes
 * a number of character constants, such as those for whitespace and various characters used in the JSON format.
 */
public interface ParseConstants {
    /**
     * The maximum nest level for parsing operations.
     */
    int NEST_LEVEL = 2_000;

    /**
     * The ASCII control character for end of text.
     */
    int ETX = 3;

    /**
     * The ASCII value for the start of a true boolean.
     */
    int TRUE_BOOLEAN_START = 't';

    /**
     * The ASCII value for the start of a null value.
     */
    int NULL_START = 'n';

    /**
     * The ASCII value for the start of a false boolean.
     */
    int FALSE_BOOLEAN_START = 'f';
    /**
     * The ASCII value for the start of an object.
     */
    int OBJECT_START_TOKEN = '{';

    /**
     * The ASCII value for the end of an object.
     */
    int OBJECT_END_TOKEN = '}';

    /**
     * The ASCII value for the start of an array.
     */
    int ARRAY_START_TOKEN = '[';

    /**
     * The ASCII value for the end of an array.
     */
    int ARRAY_END_TOKEN = ']';

    /**
     * The ASCII value for separating attributes.
     */
    int ATTRIBUTE_SEP = ':';

    /**
     * The ASCII value for separating array elements.
     */
    int ARRAY_SEP = ',';

    /**
     * The ASCII value for separating object attributes.
     */
    int OBJECT_ATTRIBUTE_SEP = ',';

    /**
     * The ASCII value for the start of an index bracket.
     */
    int INDEX_BRACKET_START_TOKEN = ARRAY_START_TOKEN;

    /**
     * The ASCII value for the end of an index bracket.
     */
    int INDEX_BRACKET_END_TOKEN = ARRAY_END_TOKEN;

    /**
     * The ASCII value for the start of a string.
     */
    int STRING_START_TOKEN = '"';

    /**
     * The ASCII value for the end of a string.
     */
    int STRING_END_TOKEN = '"';

    /**
     * The ASCII value for a new line whitespace character.
     */
    int NEW_LINE_WS = '\n';

    /**
     * The ASCII value for a tab whitespace character.
     */
    int TAB_WS = '\t';

    /**
     * The ASCII value for a carriage return whitespace character.
     */
    int CARRIAGE_RETURN_WS = '\r';

    /**
     * The ASCII value for a space whitespace character.
     */
    int SPACE_WS = ' ';

    /**
     * The ASCII value for the Delete character.
     */
    int DEL = 127;

    /**
     * The ASCII value for the control escape token.
     */
    int CONTROL_ESCAPE_TOKEN = '\\';

    /**
     * The ASCII value for the numeral '0'.
     */
    int NUM_0 = '0';

    /**
     * The ASCII value for the numeral '1'.
     */
    int NUM_1 = '1';

    /**
     * The ASCII value for the numeral '2'.
     */
    int NUM_2 = '2';

    /**
     * The ASCII value for the numeral '3'.
     */
    int NUM_3 = '3';

    /**
     * The ASCII value for the numeral '4'.
     */
    int NUM_4 = '4';

    /**
     * The ASCII value for the numeral '5'.
     */
    int NUM_5 = '5';

    /**
     * The ASCII value for the numeral '6'.
     */
    int NUM_6 = '6';

    /**
     * The ASCII value for the numeral '7'.
     */
    int NUM_7 = '7';

    /**
     * The ASCII value for the numeral '8'.
     */
    int NUM_8 = '8';

    /**
     * The ASCII value for the numeral '9'.
     */
    int NUM_9 = '9';

    /**
     * The ASCII value for a decimal point, used in decimal numbers.
     */
    int DECIMAL_POINT = '.';

    /**
     * The ASCII value for the minus symbol, used in negative numbers and subtraction operations.
     */
    int MINUS = '-';

    /**
     * The ASCII value for the plus symbol, used in positive numbers and addition operations.
     */
    int PLUS = '+';

    /**
     * The ASCII value for a lowercase 'e', often used as an exponent marker in scientific notation.
     */
    int EXPONENT_MARKER = 'e';

    /**
     * The ASCII value for an uppercase 'E', often used as an exponent marker in scientific notation.
     */
    int EXPONENT_MARKER2 = 'E';

    /**
     * String representation of the minimum integer value.
     */
    String MIN_INT_STR = String.valueOf(Integer.MIN_VALUE);

    /**
     * String representation of the maximum integer value.
     */
    String MAX_INT_STR = String.valueOf(Integer.MAX_VALUE);

    /**
     * String representation of the minimum long value.
     */
    String MIN_LONG_STR = String.valueOf(Long.MIN_VALUE);

    /**
     * String representation of the maximum long value.
     */
    String MAX_LONG_STR = String.valueOf(Long.MAX_VALUE);

    /**
     * The length of the string representation of the minimum integer value.
     */
    int MIN_INT_STR_LENGTH = MIN_INT_STR.length();

    /**
     * The length of the string representation of the maximum integer value.
     */
    int MAX_INT_STR_LENGTH = MAX_INT_STR.length();

    /**
     * The length of the string representation of the minimum long value.
     */
    int MIN_LONG_STR_LENGTH = MIN_LONG_STR.length();

    /**
     * The length of the string representation of the maximum long value.
     */
    int MAX_LONG_STR_LENGTH = MAX_LONG_STR.length();

    /**
     * The ASCII value for a dot or period, used in decimal numbers.
     */
    int DOT = '.';
    /**
     * The ASCII value for a single quote.
     */
    int SINGLE_QUOTE = '\'';

    /**
     * The ASCII value for the capital letter 'A'.
     */
    int A = 'A';

    /**
     * The ASCII value for the capital letter 'B'.
     */
    int B = 'B';

    /**
     * The ASCII value for the capital letter 'C'.
     */
    int C = 'C';

    /**
     * The ASCII value for the capital letter 'D'.
     */
    int D = 'D';

    /**
     * The ASCII value for the capital letter 'E'.
     */
    int E = 'E';

    /**
     * The ASCII value for the capital letter 'F'.
     */
    int F = 'F';

    /**
     * The ASCII value for the capital letter 'G'.
     */
    int G = 'G';

    /**
     * The ASCII value for the capital letter 'H'.
     */
    int H = 'H';

    /**
     * The ASCII value for the capital letter 'I'.
     */
    int I = 'I';

    /**
     * The ASCII value for the capital letter 'J'.
     */
    int J = 'J';

    /**
     * The ASCII value for the capital letter 'K'.
     */
    int K = 'K';

    /**
     * The ASCII value for the capital letter 'L'.
     */
    int L = 'L';

    /**
     * The ASCII value for the capital letter 'M'.
     */
    int M = 'M';

    /**
     * The ASCII value for the capital letter 'N'.
     */
    int N = 'N';

    /**
     * The ASCII value for the capital letter 'O'.
     */
    int O = 'O';

    /**
     * The ASCII value for the capital letter 'P'.
     */
    int P = 'P';

    /**
     * The ASCII value for the capital letter 'Q'.
     */
    int Q = 'Q';

    /**
     * The ASCII value for the capital letter 'R'.
     */
    int R = 'R';

    /**
     * The ASCII value for the capital letter 'S'.
     */
    int S = 'S';

    /**
     * The ASCII value for the capital letter 'T'.
     */
    int T = 'T';

    /**
     * The ASCII value for the capital letter 'U'.
     */
    int U = 'U';

    /**
     * The ASCII value for the capital letter 'V'.
     */
    int V = 'V';

    /**
     * The ASCII value for the capital letter 'W'.
     */
    int W = 'W';

    /**
     * The ASCII value for the capital letter 'X'.
     */
    int X = 'X';

    /**
     * The ASCII value for the capital letter 'Y'.
     */
    int Y = 'Y';

    /**
     * The ASCII value for the capital letter 'Z'.
     */
    int Z = 'Z';


    /**
     * The ASCII value for the character 'a'.
     */
    int A_ = 'a';

    /**
     * The ASCII value for the character 'b'.
     */
    int B_ = 'b';

    /**
     * The ASCII value for the character 'c'.
     */
    int C_ = 'c';
    /**
     * The ASCII value for the character 'd'.
     */
    int D_ = 'd';

    /**
     * The ASCII value for the character 'e'.
     */
    int E_ = 'e';

    /**
     * The ASCII value for the character 'f'.
     */
    int F_ = 'f';

    /**
     * The ASCII value for the character 'g'.
     */
    int G_ = 'g';

    /**
     * The ASCII value for the character 'h'.
     */
    int H_ = 'h';

    /**
     * The ASCII value for the character 'i'.
     */
    int I_ = 'i';

    /**
     * The ASCII value for the character 'j'.
     */
    int J_ = 'j';

    /**
     * The ASCII value for the character 'k'.
     */
    int K_ = 'k';

    /**
     * The ASCII value for the character 'l'.
     */
    int L_ = 'l';

    /**
     * The ASCII value for the character 'm'.
     */
    int M_ = 'm';

    /**
     * The ASCII value for the character 'n'.
     */
    int N_ = 'n';

    /**
     * The ASCII value for the character 'o'.
     */
    int O_ = 'o';

    /**
     * The ASCII value for the character 'p'.
     */
    int P_ = 'p';

    /**
     * The ASCII value for the character 'q'.
     */
    int Q_ = 'q';

    /**
     * The ASCII value for the character 'r'.
     */
    int R_ = 'r';

    /**
     * The ASCII value for the character 's'.
     */
    int S_ = 's';

    /**
     * The ASCII value for the character 't'.
     */
    int T_ = 't';

    /**
     * The ASCII value for the character 'u'.
     */
    int U_ = 'u';

    /**
     * The ASCII value for the character 'v'.
     */
    int V_ = 'v';

    /**
     * The ASCII value for the character 'w'.
     */
    int W_ = 'w';

    /**
     * The ASCII value for the character 'x'.
     */
    int X_ = 'x';

    /**
     * The ASCII value for the character 'y'.
     */
    int Y_ = 'y';

    /**
     * The ASCII value for the character 'z'.
     */
    int Z_ = 'z';
}
