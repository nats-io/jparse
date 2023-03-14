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

public interface ParseConstants {

    int NEST_LEVEL = 2_000;


    /**
     * End of text
     */
    int ETX = 3;

    int TRUE_BOOLEAN_START = 't';
    int NULL_START = 'n';
    int FALSE_BOOLEAN_START = 'f';
    int OBJECT_START_TOKEN = '{';
    int OBJECT_END_TOKEN = '}';
    int ARRAY_START_TOKEN = '[';
    int ARRAY_END_TOKEN = ']';
    int ATTRIBUTE_SEP = ':';
    int ARRAY_SEP = ',';
    int OBJECT_ATTRIBUTE_SEP = ',';

    int INDEX_BRACKET_START_TOKEN = ARRAY_START_TOKEN;
    int INDEX_BRACKET_END_TOKEN = ARRAY_END_TOKEN;

    int STRING_START_TOKEN = '"';
    int STRING_END_TOKEN = '"';

    int NEW_LINE_WS = '\n';
    int TAB_WS = '\t';
    int CARRIAGE_RETURN_WS = '\r';
    int SPACE_WS = ' ';
    int DEL = 127;
    int CONTROL_ESCAPE_TOKEN = '\\';

    int NUM_0 = '0';
    int NUM_1 = '1';
    int NUM_2 = '2';
    int NUM_3 = '3';
    int NUM_4 = '4';
    int NUM_5 = '5';
    int NUM_6 = '6';
    int NUM_7 = '7';
    int NUM_8 = '8';
    int NUM_9 = '9';


    int DECIMAL_POINT = '.';
    int MINUS = '-';
    int PLUS = '+';
    int EXPONENT_MARKER = 'e';
    int EXPONENT_MARKER2 = 'E';

    String  MIN_INT_STR = String.valueOf( Integer.MIN_VALUE );
    String  MAX_INT_STR = String.valueOf( Integer.MAX_VALUE );

    String MIN_LONG_STR = String.valueOf(Long.MIN_VALUE);
    String MAX_LONG_STR = String.valueOf(Long.MAX_VALUE);

    int MIN_INT_STR_LENGTH = MIN_INT_STR.length();
    int MAX_INT_STR_LENGTH = MAX_INT_STR.length();


    int MIN_LONG_STR_LENGTH = MIN_LONG_STR.length();
    int MAX_LONG_STR_LENGTH = MAX_LONG_STR.length();


    int DOT = '.';
    int SINGLE_QUOTE = '\'';
    int A = 'A';
    int B = 'B';
    int C = 'C';
    int D = 'D';
    int E = 'E';
    int F = 'F';
    int G = 'G';
    int H = 'H';
    int I = 'I';
    int J = 'J';
    int K = 'K';
    int L = 'L';
    int M = 'M';
    int N = 'N';
    int O = 'O';
    int P = 'P';
    int Q = 'Q';
    int R = 'R';
    int S = 'S';
    int T = 'T';
    int U = 'U';
    int V = 'V';
    int W = 'W';
    int X = 'X';
    int Y = 'Y';
    int Z = 'Z';
    int A_ = 'a';
    int B_ = 'b';
    int C_ = 'c';
    int D_ = 'd';
    int E_ = 'e';
    int F_ = 'f';
    int G_ = 'g';
    int H_ = 'h';
    int I_ = 'i';
    int J_ = 'j';
    int K_ = 'k';
    int L_ = 'l';
    int M_ = 'm';
    int N_ = 'n';
    int O_ = 'o';
    int P_ = 'p';
    int Q_ = 'q';
    int R_ = 'r';
    int S_ = 's';
    int T_ = 't';
    int U_ = 'u';
    int V_ = 'v';
    int W_ = 'w';
    int X_ = 'x';
    int Y_ = 'y';
    int Z_ = 'z';
}
