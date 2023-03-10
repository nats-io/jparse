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
package com.cloudurable.jparse.token;

public interface TokenTypes {

    int OBJECT_TOKEN = 0;
    int ATTRIBUTE_KEY_TOKEN = 1;
    int ATTRIBUTE_VALUE_TOKEN = 2;
    int ARRAY_TOKEN = 3;
    int ARRAY_ITEM_TOKEN = 4;


    int INT_TOKEN = 5;
    int FLOAT_TOKEN = 6;
    int STRING_TOKEN = 7;
    int BOOLEAN_TOKEN = 8;
    int NULL_TOKEN = 9;
    int PATH_KEY_TOKEN = 10;
    int PATH_INDEX_TOKEN = 11;

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
                return "" + tokenType;
        }
    }
}
