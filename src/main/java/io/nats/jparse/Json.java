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

public class Json {

    private static final AtomicReference<JsonParserBuilder> builderRef =
            new AtomicReference<>(new JsonParserBuilder().setStrict(false));
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

    private static final JsonParser PARSER = builder().build();

    public static JsonParserBuilder builder() {
        return builderRef.get().cloneBuilder();
    }

    public static String niceJson(String json) {
        return json.replace("'", "\"").replace('`', '\\');
    }

    public static ArrayNode toArrayNode(final String json) {
        return PARSER.parse(json).getArrayNode();
    }

    public static ObjectNode toObjectNode(final String json) {
        return PARSER.parse(json).getObjectNode();
    }

    public static RootNode toRootNode(final String json) {
        return PARSER.parse(json);
    }

    public static List<Object> toList(final String json) {
        return (List<Object>) (Object) PARSER.parse(json).getArrayNode();
    }

    public static Map<String, Object> toMap(final String json) {
        return (Map<String, Object>) (Object) PARSER.parse(json).getObjectNode();
    }

    public static List<Token> toTokens(final String json) {
        return PARSER.scan(json);
    }

    public static ArrayNode toArrayNode(final CharSource json) {
        return PARSER.parse(json).getArrayNode();
    }

    public static ObjectNode toObjectNode(final CharSource json) {
        return PARSER.parse(json).getObjectNode();
    }

    public static RootNode toRootNode(final CharSource json) {
        return PARSER.parse(json);
    }

    public static List<Object> toList(final CharSource json) {
        return (List<Object>) (Object) PARSER.parse(json).getArrayNode();
    }

    public static Map<String, Object> toMap(final CharSource json) {
        return (Map<String, Object>) (Object) PARSER.parse(json).getObjectNode();
    }

    public static List<Token> toTokens(final CharSource json) {
        return PARSER.scan(json);
    }

    public static String serializeToString(Object object) {
        return ((Node)object).originalString();
    }

    public static CharSequence serialize(Object object) {
        return ((Node) object).originalCharSequence();
    }
}
