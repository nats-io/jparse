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

public enum NodeType implements TokenTypes {
    ROOT(-1),
    OBJECT(OBJECT_TOKEN),
    ARRAY(ARRAY_TOKEN),
    INT(INT_TOKEN),
    FLOAT(FLOAT_TOKEN),
    STRING(STRING_TOKEN),
    BOOLEAN(BOOLEAN_TOKEN),
    NULL(NULL_TOKEN),
    PATH_KEY(PATH_KEY_TOKEN),
    PATH_INDEX(PATH_INDEX_TOKEN),
    PATH(-1),
    OTHER(-2);

    private final int tokenType;

    NodeType(int tokenTYpe) {
        this.tokenType = tokenTYpe;
    }

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
