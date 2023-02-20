package com.cloudurable.jparse;

import com.cloudurable.jparse.node.ArrayNode;
import com.cloudurable.jparse.node.Node;
import com.cloudurable.jparse.node.ObjectNode;
import com.cloudurable.jparse.node.RootNode;
import com.cloudurable.jparse.parser.JsonParser;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.Token;

import java.util.List;
import java.util.Map;

public class Json {
    public static String niceJson(String json) {
        return json.replace("'", "\"").replace('`', '\\');
    }

    public static ArrayNode toArrayNode(final String json) {
        final var parser = new JsonParser();
        return parser.parse(json).getArrayNode();
    }

    public static ObjectNode toObjectNode(final String json) {
        final var parser = new JsonParser();
        return parser.parse(json).getObjectNode();
    }

    public static RootNode toRootNode(final String json) {
        final var parser = new JsonParser();
        return parser.parse(json);
    }

    public static List<Object> toList(final String json) {
        final var parser = new JsonParser();
        return (List<Object>) (Object) parser.parse(json).getArrayNode();
    }

    public static Map<String, Object> toMap(final String json) {
        final var parser = new JsonParser();
        return (Map<String, Object>) (Object) parser.parse(json).getObjectNode();
    }

    public static List<Token> toTokens(final String json) {
        final var parser = new JsonParser();
        return parser.scan(json);
    }

    public static ArrayNode toArrayNode(final CharSource json) {
        final var parser = new JsonParser();
        return parser.parse(json).getArrayNode();
    }

    public static ObjectNode toObjectNode(final CharSource json) {
        final var parser = new JsonParser();
        return parser.parse(json).getObjectNode();
    }

    public static RootNode toRootNode(final CharSource json) {
        final var parser = new JsonParser();
        return parser.parse(json);
    }

    public static List<Object> toList(final CharSource json) {
        final var parser = new JsonParser();
        return (List<Object>) (Object) parser.parse(json).getArrayNode();
    }

    public static Map<String, Object> toMap(final CharSource json) {
        final var parser = new JsonParser();
        return (Map<String, Object>) (Object) parser.parse(json).getObjectNode();
    }

    public static List<Token> toTokens(final CharSource json) {
        final var parser = new JsonParser();
        return parser.scan(json);
    }

    public static String serializeToString(Object object) {
        return ((Node)object).originalString();
    }

    public static CharSequence serialize(Object object) {
        return ((Node) object).originalCharSequence();
    }
}
