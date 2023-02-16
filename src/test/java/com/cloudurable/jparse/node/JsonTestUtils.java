package com.cloudurable.jparse.node;

import com.cloudurable.jparse.Json;
import com.cloudurable.jparse.token.Token;
import com.cloudurable.jparse.token.TokenType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTestUtils {


    public static List<Token> tokens(final String niceJson) {
        return Json.toTokens(Json.niceJson(niceJson));
    }

    public static void showTokens(final List<Token> tokens) {
        tokens.forEach(token -> out.println(token));
    }

    public static void validateToken(Token token, TokenType type, int start, int end) {
        assertEquals(type, token.type());
        assertEquals(start, token.startIndex());
        assertEquals(end, token.endIndex());
    }


    public static RootNode nodeRoot(String niceJson) {
        return Json.toRootNode(Json.niceJson(niceJson));
    }

    public static ObjectNode nodeObject(String niceJson) {
        return Json.toObjectNode(Json.niceJson(niceJson));
    }

    public static ArrayNode jsonArray(String niceJson) {
        return Json.toArrayNode(Json.niceJson(niceJson));
    }


    public static NumberNode asNumberNode(Map<String, Object> map, Object key) {
        return (NumberNode) map.get(key);
    }

    public static Number asNumber(Map<String, Object> map, Object key) {
        return (Number) map.get(key);
    }

    public static boolean asBoolean(Map<String, Object> map, Object key) {
        return ((BooleanNode) map.get(key)).booleanValue();
    }

    public static StringNode asStringNode(Map<String, Object> map, Object key) {
        return ((StringNode) map.get(key));
    }

    public static String asString(Map<String, Object> map, Object key) {
        return asStringNode(map, key).toString();
    }

    public static float asFloat(Map<String, Object> map, Object key) {
        return asNumber(map, key).floatValue();
    }

    public static BigDecimal asBigDecimal(Map<String, Object> map, Object key) {
        return asNumberNode(map, key).bigDecimalValue();
    }

    public static BigInteger asBigInteger(Map<String, Object> map, Object key) {
        return asNumberNode(map, key).bigIntegerValue();
    }

    public static double asDouble(Map<String, Object> map, Object key) {
        return asNumber(map, key).doubleValue();
    }

    public static int asInt(Map<String, Object> map, Object key) {
        return asNumber(map, key).intValue();
    }

    public static List<Object> asList(Map<String, Object> map, Object key) {
        return (List<Object>) map.get(key);
    }

    public static ArrayNode asArray(Map<String, Object> map, Object key) {
        return (ArrayNode) map.get(key);
    }

    public static Map<String, Object> asMap(Map<String, Object> map, Object key) {
        return (Map<String, Object>) map.get(key);
    }

    public static long asLong(Map<String, Object> map, Object key) {
        return asNumber(map, key).longValue();
    }

    public static short asShort(Map<String, Object> map, Object key) {
        return asNumber(map, key).shortValue();
    }


    public static NumberNode asNumberNode(List<Object> list, int index) {
        return (NumberNode) list.get(index);
    }

    public static Number asNumber(List<Object> list, int index) {
        return (Number) list.get(index);
    }

    public static boolean asBoolean(List<Object> list, int index) {
        return ((BooleanNode) list.get(index)).booleanValue();
    }

    public static StringNode asStringNode(List<Object> list, int index) {
        return ((StringNode) list.get(index));
    }

    public static String asString(List<Object> list, int index) {
        return asStringNode(list, index).toString();
    }

    public static float asFloat(List<Object> list, int index) {
        return asNumber(list, index).floatValue();
    }

    public static BigDecimal asBigDecimal(List<Object> list, int index) {
        return asNumberNode(list, index).bigDecimalValue();
    }

    public static BigInteger asBigInteger(List<Object> list, int index) {
        return asNumberNode(list, index).bigIntegerValue();
    }

    public static double asDouble(List<Object> list, int index) {
        return asNumber(list, index).doubleValue();
    }

    public static int asInt(List<Object> list, int index) {
        return asNumber(list, index).intValue();
    }

    public static List<Object> asList(List<Object> list, int index) {
        return (List<Object>) list.get(index);
    }

    public static ArrayNode asArray(List<Object> list, int index) {
        return (ArrayNode) list.get(index);
    }

    public static Map<String, Object> asMap(List<Object> list, int index) {
        return (Map<String, Object>) list.get(index);
    }

    public static long asLong(List<Object> list, int index) {
        return asNumber(list, index).longValue();
    }

    public static short asShort(List<Object> list, int index) {
        return asNumber(list, index).shortValue();
    }
}
