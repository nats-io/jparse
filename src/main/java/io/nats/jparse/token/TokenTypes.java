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
package io.nats.jparse.token;

/**
 * The TokenTypes interface defines constants for the different token types used during JSON parsing. It includes
 * constants for object, attribute key, attribute value, array, and array item tokens, as well as for integer, float,
 * string, boolean, and null tokens. The interface also includes a method for getting a human-readable name for a
 * token type based on its integer value.
 * This interface contains the integer codes representing different token types that might be encountered during parsing.
 */
public interface TokenTypes {

    /**
     * Token type representing a JSON object.
     */
    int OBJECT_TOKEN = 0;

    /**
     * Token type representing a key in a JSON object attribute.
     */
    int ATTRIBUTE_KEY_TOKEN = 1;

    /**
     * Token type representing a value in a JSON object attribute.
     */
    int ATTRIBUTE_VALUE_TOKEN = 2;

    /**
     * Token type representing a JSON array.
     */
    int ARRAY_TOKEN = 3;

    /**
     * Token type representing an item in a JSON array.
     */
    int ARRAY_ITEM_TOKEN = 4;

    /**
     * Token type representing an integer value.
     */
    int INT_TOKEN = 5;

    /**
     * Token type representing a floating point value.
     */
    int FLOAT_TOKEN = 6;

    /**
     * Token type representing a string value.
     */
    int STRING_TOKEN = 7;

    /**
     * Token type representing a boolean value.
     */
    int BOOLEAN_TOKEN = 8;

    /**
     * Token type representing a null value.
     */
    int NULL_TOKEN = 9;

    /**
     * Token type representing a key in a JSON path.
     */
    int PATH_KEY_TOKEN = 10;

    /**
     * Token type representing an index in a JSON path.
     */
    int PATH_INDEX_TOKEN = 11;

    /**
     * Returns a human-readable name for a token type based on its integer value.
     *
     * @param tokenType The integer token type to get the name for
     * @return A human-readable name for the token type
     */
    static String getTypeName(final int tokenType) {
        switch (tokenType) {
            case OBJECT_TOKEN:
                return "Object";
            case ATTRIBUTE_KEY_TOKEN:
                return "Key";
            case ATTRIBUTE_VALUE_TOKEN:
                return "Attribute Value";
            case ARRAY_TOKEN:
                return "Array";
            case ARRAY_ITEM_TOKEN:
                return "Array Item";
            case INT_TOKEN:
                return "Integer";
            case FLOAT_TOKEN:
                return "Float";
            case STRING_TOKEN:
                return "String";
            case BOOLEAN_TOKEN:
                return "Boolean";
            case NULL_TOKEN:
                return "Null";
            default:
                return String.valueOf(tokenType);
        }
    }
}
