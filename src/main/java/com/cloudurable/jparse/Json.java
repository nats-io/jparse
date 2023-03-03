package com.cloudurable.jparse;

import com.cloudurable.jparse.node.ArrayNode;
import com.cloudurable.jparse.node.Node;
import com.cloudurable.jparse.node.ObjectNode;
import com.cloudurable.jparse.node.RootNode;
import com.cloudurable.jparse.parser.JsonParserBuilder;
import com.cloudurable.jparse.parser.JsonStrictParser;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.Token;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Json {

    private static final  AtomicReference<JsonParserBuilder> builderRef = new AtomicReference<>(new JsonParserBuilder().setStrict(false));

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
