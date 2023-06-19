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
package io.nats.jparse;

import io.nats.jparse.node.ArrayNode;
import io.nats.jparse.node.Node;
import io.nats.jparse.node.ObjectNode;
import io.nats.jparse.node.RootNode;
import io.nats.jparse.parser.JsonParser;
import io.nats.jparse.parser.JsonParserBuilder;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.token.Token;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The `Json` class provides static utility methods for working with JSON data. It includes methods for parsing JSON
 * data into various data structures, converting data structures to JSON, and scanning JSON data for tokens.
 */
public class Json {

    private static final AtomicReference<JsonParserBuilder> builderRef =
            new AtomicReference<>(new JsonParserBuilder().setStrict(false));
    /**
     * The `JsonParser` used by the `Json` class for parsing JSON data.
     */
    private static final JsonParser PARSER = builder().build();
    public static String J_PARSE_JSON_STRICT = "J_PARSE_JSON_STRICT";
    public static String J_OBJECT_KEY_CAN_BE_ENCODED = "J_OBJECT_KEY_CAN_BE_ENCODED";

    static {
        try {
            final String strStrict = System.getenv(J_PARSE_JSON_STRICT);
            if (strStrict != null) {
                if (strStrict.equalsIgnoreCase("false")) {
                    builderRef.set(builderRef.get().cloneBuilder()
                            .setStrict(false));
                } else {
                    builderRef.set(builderRef.get().cloneBuilder()
                            .setStrict(true));
                }
            }

            final String strObjectsKeysCanBeEncoded = System.getenv(J_OBJECT_KEY_CAN_BE_ENCODED);
            if (strObjectsKeysCanBeEncoded != null) {
                if (strObjectsKeysCanBeEncoded.equalsIgnoreCase("false")) {
                    builderRef.set(builderRef.get().cloneBuilder()
                            .setObjectsKeysCanBeEncoded(false));
                } else {
                    builderRef.set(builderRef.get().cloneBuilder()
                            .setObjectsKeysCanBeEncoded(true));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns a new `JsonParserBuilder` instance, which can be used to configure and create `JsonParser` instances.
     *
     * @return A new `JsonParserBuilder` instance
     */
    public static JsonParserBuilder builder() {
        return builderRef.get().cloneBuilder();
    }

    /**
     * Returns a string containing a "nicely formatted" version of the input JSON string.
     *
     * @param json The JSON string to format
     * @return A string containing a "nicely formatted" version of the input JSON string
     */
    public static String niceJson(String json) {
        char[] chars = json.toCharArray();
        StringBuilder sb = new StringBuilder(chars.length);
        for (char c : chars) {
            if (c == '\'') {
                sb.append('"');
            } else if (c == '`') {
                sb.append('\\');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Parses the input JSON string into an `ArrayNode`.
     *
     * @param json The JSON string to parse
     * @return An `ArrayNode` representing the parsed JSON data
     */
    public static ArrayNode toArrayNode(final String json) {
        return PARSER.parse(json).getArrayNode();
    }

    /**
     * Parses the input JSON string into an `ObjectNode`.
     *
     * @param json The JSON string to parse
     * @return An `ObjectNode` representing the parsed JSON data
     */
    public static ObjectNode toObjectNode(final String json) {
        return PARSER.parse(json).getObjectNode();
    }

    /**
     * Parses the input JSON string into a `RootNode`.
     *
     * @param json The JSON string to parse
     * @return A `RootNode` representing the parsed JSON data
     */
    public static RootNode toRootNode(final String json) {
        return PARSER.parse(json);
    }

    /**
     * Parses the input JSON string into a `List` of `Object`s.
     *
     * @param json The JSON string to parse
     * @return A `List` of `Object`s representing the parsed JSON data
     */
    public static List<Object> toList(final String json) {
        return (List<Object>) (Object) toArrayNode(json);
    }

    /**
     * Parses the input JSON string into a `Map` of `String`s to `Object`s.
     *
     * @param json The JSON string to parse
     * @return A `Map` of `String`s to `Object`s representing the parsed JSON data
     */
    public static Map<String, Object> toMap(final String json) {
        return (Map<String, Object>) (Object) toObjectNode(json);
    }

    /**
     * Scans the input JSON string and returns a `List` of `Token`s.
     *
     * @param json The JSON string to scan
     * @return A `List` of `Token`s representing the scanned JSON data
     */
    public static List<Token> toTokens(final String json) {
        return PARSER.scan(json);
    }

    /**
     * Parses the input `CharSource` object into an `ArrayNode`.
     *
     * @param json The `CharSource` object to parse
     * @return An `ArrayNode` representing the parsed JSON data
     */
    public static ArrayNode toArrayNode(final CharSource json) {
        return PARSER.parse(json).getArrayNode();
    }

    /**
     * Parses the input `CharSource` object into an `ObjectNode`.
     *
     * @param json The `CharSource` object to parse
     * @return An `ObjectNode` representing the parsed JSON data
     */
    public static ObjectNode toObjectNode(final CharSource json) {
        return PARSER.parse(json).getObjectNode();
    }

    /**
     * Parses the input JSON string into a `RootNode`.
     *
     * @param json The JSON source to parse
     * @return A `RootNode` representing the parsed JSON data
     */
    public static RootNode toRootNode(final CharSource json) {
        return PARSER.parse(json);
    }

    /**
     * Parses the input JSON string into a `List` of `Object`s.
     *
     * @param json The JSON char source to parse
     * @return A `List` of `Object`s representing the parsed JSON data
     */
    public static List<Object> toList(final CharSource json) {
        return (List<Object>) (Object) PARSER.parse(json).getArrayNode();
    }

    /**
     * Parses the input JSON string into a `Map` of `String`s to `Object`s.
     *
     * @param json The JSON char source to parse
     * @return A `Map` of `String`s to `Object`s representing the parsed JSON data
     */
    public static Map<String, Object> toMap(final CharSource json) {
        return (Map<String, Object>) (Object) PARSER.parse(json).getObjectNode();
    }

    /**
     * Scans the input JSON string and returns a `List` of `Token`s.
     *
     * @param json The JSON char source to scan
     * @return A `List` of `Token`s representing the scanned JSON data
     */
    public static List<Token> toTokens(final CharSource json) {
        return PARSER.scan(json);
    }


    /**
     * Serialize the given {@link Object} to a String, the object must be a {@see Node}.
     *
     * @param object the object to serialize
     * @return the String representation of the object, or empty string if the object is null
     * @see Node
     */
    public static String serializeToString(Object object) {
        return ((Node) object).originalString();
    }

    /**
     * Serialize the given {@link Object} to a {@link CharSequence}, the object must be a {@see Node}.
     *
     * @param object the object to serialize
     * @return the CharSequence representation of the object, or empty CharSequence if the object is null
     */
    public static CharSequence serialize(Object object) {
        return ((Node) object).originalCharSequence();
    }
}
