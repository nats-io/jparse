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

import io.nats.jparse.node.support.NumberParseResult;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * CharSource is a class that represents character sources used during JSON parsing. The JsonParser interface includes
 * methods for parsing and scanning CharSource objects, and the TokenEventListener interface includes methods that
 * take a CharSource object as a parameter. The Sources class is used to create CharSource objects from various input
 * sources, such as strings and files. CharSource is used to represent character sources, such as strings, and to
 * provide a consistent interface for working with those sources during JSON parsing.
 *
 * @see Sources
 * @see io.nats.jparse.token.TokenEventListener
 * @see io.nats.jparse.parser.JsonParser
 */
public interface CharSource {
    /**
     * Returns the next character in the source or ETX if there are no more characters.
     *
     * @return The next character in the source or ETX if there are no more characters
     */
    int next();

    /**
     * Returns the current index within the source.
     *
     * @return The current index within the source
     */
    int getIndex();

    /**
     * Returns the current character in the source.
     *
     * @return The current character in the source
     */
    char getCurrentChar();

    /**
     * Returns the current character in the source or ETX if there are no more characters.
     *
     * @return The current character in the source or ETX if there are no more characters
     */
    char getCurrentCharSafe();

    /**
     * Skips over whitespace characters in the source and returns the next non-whitespace character.
     *
     * @return The next non-whitespace character in the source
     */
    char skipWhiteSpace();

    /**
     * Returns the character at the given index in the source.
     *
     * @param index The index of the character to retrieve
     * @return The character at the given index in the source
     */
    char getChartAt(int index);

    /**
     * Returns the string containing characters from the source between the given start and end indices.
     *
     * @param startIndex The index of the first character to retrieve
     * @param endIndex   The index of the last character to retrieve
     * @return The string containing characters from the source between the given start and end indices
     */
    String getString(int startIndex, int endIndex);

    /**
     * Parses a double value from the characters in the source between the given start and end indices.
     *
     * @param startIndex The index of the first character to parse
     * @param endIndex   The index of the last character to parse
     * @return The double value parsed from the characters in the source between the given start and end indices
     */
    double getDouble(int startIndex, int endIndex);

    /**
     * Parses a float value from the characters in the source between the given start and end indices.
     *
     * @param startIndex The index of the first character to parse
     * @param endIndex   The index of the last character to parse
     * @return The float value parsed from the characters in the source between the given start and end indices
     */
    float getFloat(int startIndex, int endIndex);

    /**
     * Parses an integer value from the characters in the source between the given start and end indices.
     *
     * @param startIndex The index of the first character to parse
     * @param endIndex   The index of the last character to parse
     * @return The integer value parsed from the characters in the source between the given start and end indices
     */
    int getInt(int startIndex, int endIndex);

    /**
     * Parses a long value from the characters in the source between the given start and end indices.
     *
     * @param startIndex The index of the first character to parse
     * @param endIndex   The index of the last character to parse
     * @return The long value parsed from the characters in the source between the given start and end indices
     */
    long getLong(int startIndex, int endIndex);

    /**
     * Returns a character sequence containing characters from the source between the given start and end indices.
     *
     * @param startIndex The index of the first character to retrieve
     * @param endIndex   The index of the last character to retrieve
     * @return A character sequence containing characters from the source between the given start and end indices
     */
    CharSequence getCharSequence(int startIndex, int endIndex);

    /**
     * Returns a character array containing characters from the source between the given start and end indices.
     *
     * @param startIndex The index of the first character to retrieve
     * @param endIndex   The index of the last character to retrieve
     * @return A character array containing characters from the source between the given start and end indices
     */
    char[] getArray(int startIndex, int endIndex);

    /**
     * Returns an encoded string containing characters from the source between the given start and end indices.
     *
     * @param start The index of the first character to include in the encoded string
     * @param end   The index of the last character to include in the encoded string
     * @return An encoded string containing characters from the source between the given start and end indices
     */
    String getEncodedString(int start, int end);

    /**
     * Returns an encoded string containing characters from the source between the given start and end indices,
     * or a plain string if no encoding is necessary.
     *
     * @param start The index of the first character to include in the encoded string
     * @param end   The index of the last character to include in the encoded string
     * @return An encoded or plain string containing characters from the source between the given start and end indices
     */
    String toEncodedStringIfNeeded(int start, int end);

    /**
     * Parses a BigDecimal value from the characters in the source between the given start and end indices.
     *
     * @param startIndex The index of the first character to parse
     * @param endIndex   The index of the last character to parse
     * @return The BigDecimal value parsed from the characters in the source between the given start and end indices
     */
    BigDecimal getBigDecimal(int startIndex, int endIndex);

    /**
     * Parses a BigInteger value from the characters in the source between the given start and end indices.
     *
     * @param startIndex The index of the first character to parse
     * @param endIndex   The index of the last character to parse
     * @return The BigInteger value parsed from the characters in the source between the given start and end indices
     */
    BigInteger getBigInteger(int startIndex, int endIndex);

    /**
     * Finds the end index of an encoded string in the source, starting from the current index.
     *
     * @return The index of the last character in the encoded string.
     */
    int findEndOfEncodedString();

    /**
     * Finds the end index of a string in the source, starting from the current index.
     *
     * @return The index of the last character in the string.
     */
    int findEndString();

    /**
     * Parses a number from the source, starting from the current index.
     *
     * @return A `NumberParseResult` object containing the number and its end index
     */
    NumberParseResult findEndOfNumber();

    /**
     * Finds the end index of a JSON false value in the source, starting from the current index.
     *
     * @return The index of the last character in the false value
     */
    int findFalseEnd();

    /**
     * Finds the end index of a JSON true value in the source, starting from the current index.
     *
     * @return The index of the last character in the true value
     */
    int findTrueEnd();

    /**
     * Finds the end index of a JSON null value in the source, starting from the current index.
     *
     * @return The index of the last character in the null value
     */
    int findNullEnd();

    /**
     * Checks if the characters in the source between the given start and end indices match a given character sequence.
     *
     * @param startIndex The index of the first character to compare
     * @param endIndex   The index of the last character to compare
     * @param key        The character sequence to compare the source to
     * @return `true` if the characters in the source match the character sequence, otherwise `false`
     */
    boolean matchChars(int startIndex, int endIndex, CharSequence key);

    /**
     * Checks if the characters in the source between the given start and end indices represent an integer value.
     *
     * @param startIndex The index of the first character to check
     * @param endIndex   The index of the last character to check
     * @return `true` if the characters represent an integer value, otherwise `false`
     */
    boolean isInteger(int startIndex, int endIndex);

    /**
     * Skips over whitespace characters in the source and returns the index of the next non-whitespace character.
     *
     * @return The index of the next non-whitespace character in the source
     */
    int nextSkipWhiteSpace();

    /**
     * Returns a string containing error details for the given message, index, and character.
     *
     * @param message The error message to include in the details
     * @param index   The index in the source where the error occurred
     * @param ch      The character causing the error
     * @return A string containing error details for the given message, index, and character
     */
    String errorDetails(String message, int index, int ch);

    /**
     * Finds the next comma or end of array marker in the source, starting from the current index.
     *
     * @return `true` if a comma or end of array marker is found, otherwise `false`
     */
    boolean findCommaOrEndForArray();

    /**
     * Finds the end of an object or the separator between attributes, starting from the current index.
     *
     * @return `true` if the end of an object or the separator between attributes is found, otherwise `false`
     */
    boolean findObjectEndOrAttributeSep();

    /**
     * Checks the source for invalid characters and throws an exception if any are found.
     */
    void checkForJunk();

    /**
     * Parses a number from the source, starting from the current index, using a faster algorithm than `findEndOfNumber`.
     *
     * @return A `NumberParseResult` object containing the number and its end index
     */
    NumberParseResult findEndOfNumberFast();

    /**
     * Finds the end index of an encoded string in the source, starting from the current index, using a faster
     * algorithm than `findEndOfEncodedString`.
     *
     * @return The index of the last character in the encoded string.
     */
    int findEndOfEncodedStringFast();

    /**
     * Finds the next occurrence of a given character in the source, starting from the current index.
     *
     * @param c The character to search for
     * @return `true` if the character is found, otherwise `false`
     */
    boolean findChar(char c);

    /**
     * Finds the end of an attribute in the source, starting from the current index.
     *
     * @return The index of the last character in the attribute.
     */
    int findAttributeEnd();

}
