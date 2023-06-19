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
package io.nats.jparse.node;


import io.nats.jparse.token.TokenTypes;

/**
 * This enum represents the different types of nodes in the JParse library.
 * <p>
 * Each node type corresponds to a specific token type.
 *
 * <p>The enum values include ROOT, OBJECT, ARRAY, INT, FLOAT, STRING, BOOLEAN, NULL,
 * PATH_KEY, PATH_INDEX, PATH, and OTHER.
 *
 * <p>The tokenTypeToElement method allows converting a token type to its corresponding NodeType.
 *
 * @see io.nats.jparse.token.TokenTypes
 */
public enum NodeType implements TokenTypes {

    /**

     The root node type.
     */
    ROOT(-1),
    /**

     The object node type.
     */
    OBJECT(OBJECT_TOKEN),
    /**

     The array node type.
     */
    ARRAY(ARRAY_TOKEN),
    /**

     The integer node type.
     */
    INT(INT_TOKEN),
    /**

     The float node type.
     */
    FLOAT(FLOAT_TOKEN),
    /**

     The string node type.
     */
    STRING(STRING_TOKEN),
    /**

     The boolean node type.
     */
    BOOLEAN(BOOLEAN_TOKEN),
    /**

     The null node type.
     */
    NULL(NULL_TOKEN),
    /**

     The path key node type.
     */
    PATH_KEY(PATH_KEY_TOKEN),
    /**

     The path index node type.
     */
    PATH_INDEX(PATH_INDEX_TOKEN),
    /**

     The path node type.
     */
    PATH(-1),
    /**

     An other or unrecognized node type.
     */
    OTHER(-2);
    private final int tokenType;

    NodeType(int tokenType) {
        this.tokenType = tokenType;
    }

    /**

     Converts a token type to its corresponding NodeType.
     @param tokenType the token type to convert
     @return the corresponding NodeType
     @throws IllegalStateException if the token type is invalid
     */
    public static NodeType tokenTypeToElement(final int tokenType) {
        switch (tokenType) {
            case OBJECT_TOKEN:
                return NodeType.OBJECT;
            case ARRAY_TOKEN:
                return NodeType.ARRAY;
            case INT_TOKEN:
                return NodeType.INT;
            case FLOAT_TOKEN:
                return NodeType.FLOAT;
            case STRING_TOKEN:
                return NodeType.STRING;
            case BOOLEAN_TOKEN:
                return NodeType.BOOLEAN;
            case NULL_TOKEN:
                return NodeType.NULL;
            case PATH_KEY_TOKEN:
                return NodeType.PATH_KEY;
            case PATH_INDEX_TOKEN:
                return NodeType.PATH_INDEX;
            default:
                throw new IllegalStateException(String.valueOf(tokenType));
        }
    }
}
