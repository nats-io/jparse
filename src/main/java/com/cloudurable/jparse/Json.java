package com.cloudurable.jparse;

import com.cloudurable.jparse.node.ArrayNode;
import com.cloudurable.jparse.node.Node;
import com.cloudurable.jparse.node.ObjectNode;
import com.cloudurable.jparse.node.RootNode;
import com.cloudurable.jparse.parser.JsonParserBuilder;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.Token;

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

    public static JsonParserBuilder builder() {
        return builderRef.get().cloneBuilder();
    }

    public static String niceJson(String json) {
        return json.replace("'", "\"").replace('`', '\\');
    }

    public static ArrayNode toArrayNode(final String json) {
        final var parser = builder().build();
        return parser.parse(json).getArrayNode();
    }

    public static ObjectNode toObjectNode(final String json) {
        final var parser = builder().build();
        return parser.parse(json).getObjectNode();
    }

    public static RootNode toRootNode(final String json) {
        final var parser = builder().build();
        return parser.parse(json);
    }

    public static List<Object> toList(final String json) {
        final var parser = builder().build();
        return (List<Object>) (Object) parser.parse(json).getArrayNode();
    }

    public static Map<String, Object> toMap(final String json) {
        final var parser = builder().build();
        return (Map<String, Object>) (Object) parser.parse(json).getObjectNode();
    }

    public static List<Token> toTokens(final String json) {
        final var parser = builder().build();
        return parser.scan(json);
    }

    public static ArrayNode toArrayNode(final CharSource json) {
        final var parser = builder().build();
        return parser.parse(json).getArrayNode();
    }

    public static ObjectNode toObjectNode(final CharSource json) {
        final var parser = builder().build();
        return parser.parse(json).getObjectNode();
    }

    public static RootNode toRootNode(final CharSource json) {
        final var parser = builder().build();
        return parser.parse(json);
    }

    public static List<Object> toList(final CharSource json) {
        final var parser = builder().build();
        return (List<Object>) (Object) parser.parse(json).getArrayNode();
    }

    public static Map<String, Object> toMap(final CharSource json) {
        final var parser = builder().build();
        return (Map<String, Object>) (Object) parser.parse(json).getObjectNode();
    }

    public static List<Token> toTokens(final CharSource json) {
        final var parser = builder().build();
        return parser.scan(json);
    }

    public static String serializeToString(Object object) {
        return ((Node)object).originalString();
    }

    public static CharSequence serialize(Object object) {
        return ((Node) object).originalCharSequence();
    }
}
