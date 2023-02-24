package com.cloudurable.jparse;


import com.cloudurable.jparse.node.*;
import com.cloudurable.jparse.parser.IndexOverlayParser;
import com.cloudurable.jparse.parser.JsonParser;
import com.cloudurable.jparse.source.Sources;
import com.cloudurable.jparse.token.TokenTypes;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cloudurable.jparse.Json.niceJson;
import static com.cloudurable.jparse.Json.toRootNode;
import static com.cloudurable.jparse.node.JsonTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class JsonParserTest {


    @Test
    public void testDoubleArray() {
        //................012345678901234567890123
        final var json = "[1, 1.1, 1.2, 1.3, 1e+9, 1e9, 1e-9]";
        final double[] array = {1, 1.1, 1.2, 1.3, 1e+9, 1e9, 1e-9};
        double[] readDoubles = Json.toArrayNode(niceJson(json)).getDoubleArray();
        assertArrayEquals(array, readDoubles);
    }

    @Test
    public void testDoubleArrayFast() {
        //................012345678901234567890123
        final var json = "[1, 1.1, 1.2, 1.3, 1e+9, 1e9, 1e-9]";
        final double[] array = {1, 1.1, 1.2, 1.3, 1e+9, 1e9, 1e-9};
        double[] readDoubles = Json.toArrayNode(niceJson(json)).getDoubleArrayFast();
        assertArrayEquals(array, readDoubles);
    }

    @Test
    public void testFloatArray() {
        //................012345678901234567890123
        final var json = "[1, 1.1, 1.2, 1.3, 1e+9, 1e9, 1e-9]";
        final float[] array = {1, 1.1f, 1.2f, 1.3f, 1e+9f, 1e9f, 1e-9f};
        float[] values = Json.toArrayNode(niceJson(json)).getFloatArray();
        assertArrayEquals(array, values);
    }

    @Test
    public void testSimpleArray() {
        //................012345678901234567890123
        final var json = "[1]";
        final float[] array = {1};
        float[] values = Json.toArrayNode(niceJson(json)).getFloatArray();
        assertArrayEquals(array, values);
    }

    @Test
    public void testSimpleArray2() {
        //................012345678901234567890123
        final var json = "[1,2]";
        final float[] array = {1,2};
        float[] values = Json.toArrayNode(niceJson(json)).getFloatArray();
        assertArrayEquals(array, values);
    }


    @Test
    public void testFloatArrayFast() {
        //................012345678901234567890123
        final var json = "[1, 1.1, 1.2, 1.3, 1e+9, 1e9, 1e-9]";
        final float[] array = {1, 1.1f, 1.2f, 1.3f, 1e+9f, 1e9f, 1e-9f};
        float[] values = Json.toArrayNode(niceJson(json)).getFloatArrayFast();
        assertArrayEquals(array, values);
    }

    private BigDecimal bg(String s) {
        return new BigDecimal(s);
    }

    @Test
    public void testBigDecimalArray() {
        //................012345678901234567890123
        final var json = "[1, 1.1, 1.2, 1.3, 1e+9, 1e9, 1e-9]";
        final var array = new BigDecimal[]{bg("1"), bg("1.1"), bg("1.2"), bg("1.3"), bg("1e+9"), bg("1e9"), bg("1e-9")};
        final var values = Json.toArrayNode(niceJson(json)).getBigDecimalArray();
        assertArrayEquals(array, values);
    }

    private BigInteger bi(int s) {
        return new BigInteger("" + s);
    }

    @Test
    public void testBigIntArray() {
        //................012345678901234567890123
        final var json = "[1, 2, 3, 4, 5, 6, 7, 8, -9]";
        final var array = new BigInteger[]{bi(1), bi(2), bi(3), bi(4), bi(5), bi(6), bi(7), bi(8), bi(-9)};
        final var values = Json.toArrayNode(niceJson(json)).getBigIntegerArray();
        assertArrayEquals(array, values);
    }

    @Test
    public void testIntArray() {
        //................012345678901234567890123
        final var json = "[1, 2, 3, 4, 5, 6, 7, 8, -9]";
        final int[] array = {1, 2, 3, 4, 5, 6, 7, 8, -9};
        int[] values = Json.toArrayNode(niceJson(json)).getIntArray();
        assertArrayEquals(array, values);
    }

    @Test
    public void testLongArray() {
        //................012345678901234567890123
        final var json = "[1, 2, 3, 4, 5, 6, 7, 8, 9]";
        final long[] array = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L};
        long [] values = Json.toArrayNode(niceJson(json)).getLongArray();
        assertArrayEquals(array, values);
    }

    @Test
    public void testIntArrayFast() {
        //................012345678901234567890123
        final var json = "[1, 2, 3, 4, 5, 6, 7, 8, -9]";
        final int[] array = {1, 2, 3, 4, 5, 6, 7, 8, -9};
        int[] values =       Json.toArrayNode(niceJson(json)).getIntArrayFast();
        assertArrayEquals(array, values);
    }

    @Test
    public void testLongArrayFast() {
        //................012345678901234567890123
        final var json = "[1, 2, 3, 4, 5, 6, 7, 8, 9]";
        final long[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        long[] values = Json.toArrayNode(niceJson(json)).getLongArrayFast();
        assertArrayEquals(array, values);
    }

    //-3.689349E18
    @Test
    public void testDoubleArrayExponent2() {

        final double[] array = {3.689349E18};
        //...................01234567890
        final String json = "[3.689349E18]";
        double[] values = Json.toArrayNode(niceJson(json)).getDoubleArray();
        assertArrayEquals(array, values);
    }

    @Test
    public void testDoubleArrayExponent() {
        //................012345678901234567890123

        double value = Double.MAX_VALUE;
        final double unitDown = Double.MAX_VALUE / 10;

        final var buf = new StringBuilder();
        final var numbers = new ArrayList<Double>();
        buf.append('[');

        for (int i = 0; i < 5; i++) {
            value = value - (i * unitDown);
            String format = String.format("%e", value);
            numbers.add(Double.parseDouble(format));
            buf.append(format).append(",");
        }

        value = Double.MIN_VALUE;
        for (int i = 0; i < 4; i++) {
            value = value + (i * unitDown);
            String format = String.format("%e", value);
            numbers.add(Double.parseDouble(format));
            buf.append(format).append(",");
        }

        buf.setCharAt(buf.length() - 1, ']');

        final var json = buf.toString();

        double[] readDoubles = Json.toArrayNode(niceJson(json)).getDoubleArray();

        for (int i = 0; i < readDoubles.length; i++) {
            //System.out.println(numbers.get(i) + "    " +  readDoubles[i]);
            assertEquals(numbers.get(i), readDoubles[i], Math.abs(numbers.get(i) / 10_000.0));
        }


        //assertArrayEquals(array, readDoubles);
    }


    @Test
    public void testDoubleArrayLarge() {
        //................012345678901234567890123

        double value = Long.MAX_VALUE;
        final double unitDown = Long.MAX_VALUE / 10;

        final var buf = new StringBuilder();
        final var numbers = new ArrayList<Double>();
        buf.append('[');

        for (int i = 0; i < 5; i++) {
            value = value - (i * unitDown);
            String format = String.format("%f", value);
            numbers.add(Double.parseDouble(format));
            buf.append(format).append(",");
        }

        value = Long.MIN_VALUE;
        for (int i = 0; i < 4; i++) {
            value = value + (i * unitDown);
            String format = String.format("%e", value);
            numbers.add(Double.parseDouble(format));
            buf.append(format).append(",");
        }

        buf.setCharAt(buf.length() - 1, ']');


        final var json = buf.toString();
        //System.out.println(json);

        double[] readDoubles = Json.toArrayNode(niceJson(json)).getDoubleArray();

        for (int i = 0; i < readDoubles.length; i++) {

            //.println(numbers.get(i) + "    " +  readDoubles[i]);

            assertEquals(numbers.get(i), readDoubles[i], Math.abs(numbers.get(i) / 10_000.0));
        }


        //assertArrayEquals(array, readDoubles);
    }

    @Test
    public void testComplexMap() {
        //................012345678901234567890123
        final var json = "{'1':2,'2':7,'3':[1,2,3]}";
        final RootNode root = nodeRoot(json);

        final var jsonObject = root.getMap();
        assertEquals(2, asInt(jsonObject, "1"));
        assertEquals(7, asInt(jsonObject, "2"));
        assertEquals(List.of(1L, 2L, 3L), asArray(jsonObject, "3").stream().map(n->n.asScalar().longValue()).collect(Collectors.toList()));

    }

    @Test
    void testParseNumberJsonElement() {
        final IndexOverlayParser parser = new JsonParser();
        final String json = "1";
        RootNode jsonRoot = parser.parse(Sources.stringSource(json));
        assertEquals(1, jsonRoot.getInt());
        assertEquals(1, jsonRoot.getLong());
        assertEquals(new BigInteger("1"), jsonRoot.getBigIntegerValue());
        assertTrue(jsonRoot.getNode().isScalar());
        assertFalse(jsonRoot.getNode().isCollection());
    }

    @Test
    void testParseFloatJsonElement() {
        final IndexOverlayParser parser = new JsonParser();
        final String json = "1.1";
        RootNode jsonRoot = parser.parse(Sources.stringSource(json));
        assertEquals(1.1, jsonRoot.getFloat(), 0.001);
        assertEquals(1.1, jsonRoot.getDouble(), 0.001);
        assertEquals(new BigDecimal("1.1"), jsonRoot.getBigDecimal());
    }


    @Test
    public void testComplexMapWithMixedKeys() {
        //................012345678901234567890123
        final var json = "{'1':2,'2':7,'abc':[1,2,3,true,'hi'],'4':true}";
        final RootNode root = nodeRoot(json);
        final var jsonObject = Json.toMap(niceJson(json));
        assertTrue(asBoolean(jsonObject, "4"));
        assertEquals(2, asInt(jsonObject, "1"));
        assertEquals(7, asInt(jsonObject, "2"));
        assertEquals(7, asShort(jsonObject, "2"));
        assertEquals(7L, asLong(jsonObject, "2"));
        assertEquals(7.0, asDouble(jsonObject, "2"));
        assertEquals(7.0f, asFloat(jsonObject, "2"));
        assertEquals(new BigInteger("7"), asBigInteger(jsonObject, "2"));
        assertEquals(new BigDecimal("7"), asBigDecimal(jsonObject, "2"));

        assertEquals(List.of(1, 2, 3, true, "hi"), asArray(jsonObject, "abc").stream().map(n->n.asScalar().value()).collect(Collectors.toList()));

        assertEquals(1, asInt(asList(jsonObject, "abc"), 0));
        assertEquals("hi", asString(asList(jsonObject, "abc"), 4));
        assertEquals("hi", asString(asList(jsonObject, "abc"), 4));
        assertEquals(1, asShort(asList(jsonObject, "abc"), 0));
        assertEquals(1.0, asDouble(asList(jsonObject, "abc"), 0));
        assertEquals(1.0f, asFloat(asList(jsonObject, "abc"), 0));
        assertEquals(1L, asLong(asList(jsonObject, "abc"), 0));
        assertEquals(new BigInteger("1"), asBigInteger(asList(jsonObject, "abc"), 0));
        assertEquals(new BigDecimal("1"), asBigDecimal(asList(jsonObject, "abc"), 0));

        assertTrue(asBoolean(asList(jsonObject, "abc"), 3));
        assertTrue(asArray(jsonObject, "abc").getBooleanNode(3).booleanValue());
        assertTrue(asArray(jsonObject, "abc").getBoolean(3));

        assertFalse(root.getNode().isScalar());
        assertTrue(root.getNode().isCollection());

    }


    @Test
    void testSimpleBooleanTrue() {
        final IndexOverlayParser parser = new JsonParser();
        final String json = "true";

        final RootNode jsonRoot = parser.parse(Sources.charSeqSource(json));
        assertTrue(jsonRoot.getBooleanNode().booleanValue());
        assertTrue(jsonRoot.getBoolean());

        assertEquals(json, jsonRoot.getBooleanNode().toString());
        assertEquals(json, jsonRoot.getBooleanNode().charSource().toString());

        assertEquals(json.charAt(0), jsonRoot.getBooleanNode().charAt(0));
        assertEquals(json.charAt(1), jsonRoot.getBooleanNode().charAt(1));
        assertEquals(json.charAt(2), jsonRoot.getBooleanNode().charAt(2));
        assertEquals(json.charAt(3), jsonRoot.getBooleanNode().charAt(3));

        assertEquals(4, jsonRoot.getBooleanNode().length());
        assertEquals(NodeType.BOOLEAN, jsonRoot.getBooleanNode().type());
        assertEquals(TokenTypes.BOOLEAN_TOKEN, jsonRoot.getBooleanNode().rootElementToken().type);
        assertEquals(TokenTypes.BOOLEAN_TOKEN, jsonRoot.getBooleanNode().tokens().get(0).type);
    }


    @Test
    void testSimpleNull() {
        final IndexOverlayParser parser = new JsonParser();
        final String json = "null";

        final RootNode jsonRoot = parser.parse(Sources.charSeqSource(json));

        assertEquals(json, jsonRoot.getNullNode().toString());
        assertEquals(json, jsonRoot.getNullNode().charSource().toString());

        assertEquals(json.charAt(0), jsonRoot.getNullNode().charAt(0));
        assertEquals(json.charAt(1), jsonRoot.getNullNode().charAt(1));
        assertEquals(json.charAt(2), jsonRoot.getNullNode().charAt(2));
        assertEquals(json.charAt(3), jsonRoot.getNullNode().charAt(3));

        assertEquals(4, jsonRoot.getNullNode().length());
        assertEquals(NodeType.NULL, jsonRoot.getNullNode().type());
        assertEquals(TokenTypes.NULL_TOKEN, jsonRoot.getNullNode().rootElementToken().type);
        assertEquals(TokenTypes.NULL_TOKEN, jsonRoot.getNullNode().tokens().get(0).type);
    }

    @Test
    void testSimpleBooleanFalse() {
        final IndexOverlayParser parser = new JsonParser();
        final String json = "false";

        final RootNode jsonRoot = parser.parse(Sources.stringSource(json));
        assertFalse(jsonRoot.getBooleanNode().booleanValue());

        assertEquals(json, jsonRoot.getBooleanNode().toString());
        assertEquals(json, jsonRoot.getBooleanNode().charSource().toString());

        assertEquals(json.charAt(0), jsonRoot.getBooleanNode().charAt(0));
        assertEquals(json.charAt(1), jsonRoot.getBooleanNode().charAt(1));
        assertEquals(json.charAt(2), jsonRoot.getBooleanNode().charAt(2));
        assertEquals(json.charAt(3), jsonRoot.getBooleanNode().charAt(3));
        assertEquals(json.charAt(4), jsonRoot.getBooleanNode().charAt(4));

        assertEquals(5, jsonRoot.getBooleanNode().length());
        assertEquals(NodeType.BOOLEAN, jsonRoot.getBooleanNode().type());
        assertEquals(TokenTypes.BOOLEAN_TOKEN, jsonRoot.getBooleanNode().rootElementToken().type);
        assertEquals(TokenTypes.BOOLEAN_TOKEN, jsonRoot.getBooleanNode().tokens().get(0).type);
    }


    @Test
    void testSimpleString() {
        final IndexOverlayParser parser = new JsonParser();
        final String json = "\"abc\"";

        final RootNode jsonRoot = parser.parse(Sources.charSeqSource(json));
        assertEquals("abc", jsonRoot.getStringNode().toString());
        assertEquals(NodeType.STRING, jsonRoot.getType());
        assertEquals("bc", jsonRoot.getStringNode().subSequence(1, 3).toString());
    }

    @Test
    void testSimpleEncodedString() {
        final IndexOverlayParser parser = new JsonParser();
        //....................012 3 4 5 6
        final String json = "'abc`n`b`r`u1234'";

        final RootNode jsonRoot = parser.parse(Sources.stringSource(json.replace("'", "\"").replace('`', '\\')));

        final var encodedString = jsonRoot.getStringNode().toString();
        final var unencodedString = jsonRoot.getStringNode().toUnencodedString();

        assertEquals("abc\\n\\b\\r\\u1234", unencodedString);
        assertEquals("abc\n\b\r\u1234", encodedString);
        assertEquals("abc\n\b\r\u1234", jsonRoot.getStringNode().toString());
        assertEquals(7, jsonRoot.getStringNode().toEncodedString().length());
        assertEquals(NodeType.STRING, jsonRoot.getType());
        assertEquals("bc", jsonRoot.getStringNode().subSequence(1, 3).toString());

        assertEquals('\n', jsonRoot.getStringNode().toEncodedString().charAt(3));
    }

    @Test
    void testGetNumFloat() {
        final IndexOverlayParser parser = new JsonParser();
        final String json = "1.1";
        final RootNode jsonRoot = parser.parse(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(1.1, jsonRoot.getNumberNode().floatValue(), 0.001);
        assertEquals(NodeType.FLOAT, jsonRoot.getType());
    }


    @Test
    void test_empty_object() {

        final IndexOverlayParser parser = new JsonParser();
        final String json = "{}";
        final RootNode jsonRoot = parser.parse(Json.niceJson(json));

        System.out.println(jsonRoot.tokens());
        assertEquals(0, jsonRoot.asObject().size());

    }

    @Test
    void test_n_object_with_single_string() {
        final IndexOverlayParser parser = new JsonParser();
        //...................0123456789012345678901234
        final String json = "{'foo' :  'bar',  'a' }";
        try {
            final RootNode jsonRoot = parser.parse(Json.niceJson(json));
            System.out.println(jsonRoot.tokens());
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void test_n_structure_double_array() {
        final IndexOverlayParser parser = new JsonParser();
        //...................0123456789012345678901234
        final String json = "[][]";
        try {
            final RootNode jsonRoot = parser.parse(Json.niceJson(json));
            System.out.println(jsonRoot.tokens());
            assertTrue(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Test
    void test_n_array_missing_value() {
        final IndexOverlayParser parser = new JsonParser();
        //...................0123456789012345678901234
        final String json = "[   , \"\"]";
        try {
            final RootNode jsonRoot = parser.parse(Json.niceJson(json));
            System.out.println(jsonRoot.tokens());
            assertTrue(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }



    @Test
    void testGetNumInt() {
        final IndexOverlayParser parser = new JsonParser();
        final String json = "1";
        final RootNode jsonRoot = parser.parse(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(1.0, jsonRoot.getNumberNode().floatValue(), 0.001);
        assertEquals(NodeType.INT, jsonRoot.getType());
    }


    @Test
    void testSimpleStringFromMap() {
        final IndexOverlayParser parser = new JsonParser();
        final String json = "{'a':'abc'}";
        final RootNode jsonRoot = parser.parse(json.replace("'", "\""));

        StringNode aStringValue = jsonRoot.getObjectNode().getStringNode("a");
        //assertEquals("abc", jsonRoot.getObject().getValue("a").toString());
        assertEquals("abc", aStringValue.toString());
        assertEquals("abc", jsonRoot.getObjectNode().getString("a"));

        assertEquals(NodeType.OBJECT, jsonRoot.getType());

        assertEquals(1, jsonRoot.getObjectNode().length());
        //assertEquals(List.of(new StringNode("a", 0, 1)), jsonRoot.getObjectNode().getKeys());

        assertEquals(niceJson("abc"), jsonRoot.getObjectNode().getString("a"));

        assertEquals(niceJson(json), jsonRoot.originalString());
    }

    @Test
    void testSimpleStringFromMap2() {
        final var json = "{'a':'abc'}";
        Map<String, Object> map = Json.toMap(niceJson(json));
        assertEquals("abc", asString(map, "a"));
    }

    @Test
    void testSimpleStringFromMap3() {
        final var json = "{'a':null}";
        Map<String, Object> map = Json.toMap(niceJson(json));
        final var root = Json.toRootNode(niceJson(json));
        assertEquals("null", "" + map.get("a"));

        final var nullNode = root.getNode("a");
        assertEquals("null", nullNode.toString());
        assertEquals(4, nullNode.length());
        assertEquals('n', nullNode.charAt(0));
        assertEquals('u', nullNode.charAt(1));
        assertEquals('l', nullNode.charAt(2));
        assertEquals('l', nullNode.charAt(3));
        assertEquals(nullNode, nullNode);
        assertEquals(nullNode, Json.toRootNode(niceJson(json)).getNode("a"));
        assertEquals(nullNode, Json.toRootNode(niceJson(json)).getObjectNode().getNullNode("a"));
    }


    @Test
    void test3ItemMap() {
        final IndexOverlayParser parser = new JsonParser();
        //...................012345678901234567890
        final String json = "{'a':'abc','b':'def', 'c': 1234 }";
        final RootNode jsonRoot = parser.parse(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(3, jsonRoot.getObjectNode().length());
        assertEquals(NodeType.OBJECT, jsonRoot.getType());
        assertEquals("abc", jsonRoot.getObjectNode().getNode("a").toString());
        assertEquals("def", jsonRoot.getObjectNode().getStringNode("b").toString());
        assertEquals(1234, jsonRoot.getObjectNode().getNumberNode("c").intValue());
        assertEquals(1234, jsonRoot.getObjectNode().getInt("c"));
    }

    @Test
    void test2ItemIntKeyMap() {
        final IndexOverlayParser parser = new JsonParser();
        //...................0123456789
        final String json = "{'1':2,'2':3}";
        final RootNode jsonRoot = parser.parse(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(2, jsonRoot.getObjectNode().length());
        assertEquals(3, jsonRoot.getObjectNode().getLong("2"));

        assertEquals(new BigInteger("3"), jsonRoot.getObjectNode().getBigInteger("2"));
        assertEquals(new BigDecimal("3"), jsonRoot.getObjectNode().getBigDecimal("2"));

        assertEquals(2, jsonRoot.getObjectNode().getLong("1"));
        assertEquals(3, jsonRoot.getObjectNode().getInt("2"));
        assertEquals(2, jsonRoot.getObjectNode().getInt("1"));
        assertEquals(NodeType.OBJECT, jsonRoot.getType());
    }

    @Test
    void testSimpleMapFromMap() {
        final IndexOverlayParser parser = new JsonParser();
        //...................01234567890123456789
        final String json = "{'a':{'a':1}}";
        final RootNode jsonRoot = parser.parse(Json.niceJson(json));
        jsonRoot.tokens().forEach(System.out::println);
        assertEquals(1, jsonRoot.getObjectNode().size());

        //.getObjectNode("a").getStringNode("a").toString());
//        assertEquals(NodeType.OBJECT, jsonRoot.getType());
//        assertEquals(NodeType.OBJECT, jsonRoot.getObjectNode().type());
//
//
//        assertEquals(niceJson("{'a':'abc'}"), jsonRoot.getObjectNode().getObjectNode("a").originalString());
//        assertEquals(niceJson("{'a':'abc'}"), jsonRoot.getObjectNode().getObjectNode("a").originalCharSequence().toString());

    }

    @Test
    void testSimpleMapFromMap2() {

        final String json = "{'a':{'b':'abc'}}";

        final var mapOuter = Json.toMap(niceJson(json));
        final var mapInner = asMap(mapOuter, "a");

        assertEquals("abc", mapInner.get("b").toString());


    }

    @Test
    void testSimpleArrayFromMap() {
        final var json = "{'a':[1,2,3]}";
        final var jsonObject = nodeObject(json);
        final var map = Json.toMap(niceJson(json));
        final var hash = jsonObject.hashCode();
        final var jsonArray = asArray(map, "a");
        assertEquals(1L, jsonArray.getLong(0));
        assertEquals(1, jsonArray.getInt(0));
        assertEquals(hash, jsonObject.hashCode());
        assertEquals(nodeObject(json), jsonObject);

        jsonObject.entrySet().forEach(objectObjectEntry -> assertTrue(jsonObject.containsKey(objectObjectEntry.getKey())));
    }

    @Test
    void testSimpleArrayFromMa2p() {
        final var json = "{'a':[1,2,3]}";
        final var jsonObject = nodeObject(json);
        final var map = Json.toMap(niceJson(json));
        final var hash = jsonObject.hashCode();
        final var list = asList(map, "a");
        assertEquals(1L, asLong(list, 0));
        assertEquals(1, asInt(list, 0));
    }

    @Test
    void testSimpleIntFromArray() {
        final var json = "[1,2,3]";
        final var jsonArray = Json.toArrayNode(json);
        assertEquals(1L, jsonArray.getLong(0));
        assertEquals(1, jsonArray.getNumberNode(0).intValue());
        assertEquals(1, jsonArray.getNumberNode(0).longValue());
    }

    @Test
    void testSimpleFloatFromArray() {
        final IndexOverlayParser parser = new JsonParser();
        final String json = "[1.1,2,3]";
        final RootNode jsonRoot = parser.parse(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(1.1, jsonRoot.getArrayNode().getDouble(0), 0.001);
        assertEquals(1.1, jsonRoot.getArrayNode().getFloat(0), 0.001);
        assertEquals(new BigDecimal("1.1"), jsonRoot.getArrayNode().getBigDecimal(0));
        assertEquals(new BigInteger("2"), jsonRoot.getArrayNode().getBigInteger(1));

        assertEquals(1.1, jsonRoot.getArrayNode().getNumberNode(0).floatValue(), 0.001);
        assertEquals(1.1, jsonRoot.getArrayNode().getNumberNode(0).doubleValue(), 0.001);
    }

    @Test
    void testSimpleNullFromArray() {
        final IndexOverlayParser parser = new JsonParser();
        final String json = "[null,2,null]";
        final RootNode jsonRoot = toRootNode(json);
        NullNode nullNode = jsonRoot.getArrayNode().getNullNode(0);
        assertEquals("null", nullNode.toString());

        assertNull(jsonRoot.getArrayNode().get(0));

    }

    @Test
    void testSimpleArrayFromArray() {
        final IndexOverlayParser parser = new JsonParser();
        final String json = "[[1,2,3],2,3]";
        final RootNode jsonRoot = parser.parse(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(1L, jsonRoot.getArrayNode().getArray(0).getLong(0));
        assertEquals(1, jsonRoot.getArrayNode().getArray(0).getNumberNode(0).intValue());
        assertEquals(3, jsonRoot.getArrayNode().getArray(0).length());
    }

    @Test
    void testSimpleArrayFromArray_easy() {
        final IndexOverlayParser parser = new JsonParser();
        final String json = "[[1],2]";
        final RootNode jsonRoot = parser.parse(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(1L, jsonRoot.getArrayNode().getArray(0).getLong(0));
        assertEquals(1, jsonRoot.getArrayNode().getArray(0).getNumberNode(0).intValue());
        assertEquals(1, jsonRoot.getArrayNode().getArray(0).length());
    }
    @Test
    void testSimpleArrayFromArray2() {

        final var json = "[[1,2,3],7,13]";
        final var listOuter = Json.toList(niceJson(json));
        final var listInner = asList(listOuter, 0);

        final var arrayInner = asArray(listOuter, 0);

        assertEquals(1, asInt(listInner, 0));
        assertEquals(2, asInt(listInner, 1));
        assertEquals(3, asInt(listInner, 2));

        assertEquals(1, arrayInner.getInt(0));
        assertEquals(2, arrayInner.getInt(1));
        assertEquals(3, arrayInner.getInt(2));
        assertEquals(7, asInt(listOuter, 1));
        assertEquals(13, asInt(listOuter, 2));

    }


    @Test
    void testGeFloatFromMap() {
        final IndexOverlayParser parser = new JsonParser();
        final String json = "{'a':1.1}";
        final RootNode jsonRoot = parser.parse(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(1.1, jsonRoot.getObjectNode().getDouble("a"), 0.001);
        assertEquals(1.1, jsonRoot.getObjectNode().getFloat("a"), 0.001);

        assertEquals(NodeType.OBJECT, jsonRoot.getType());
    }

    @Test
    void testGetIntFromMap() {
        final IndexOverlayParser parser = new JsonParser();
        //...................0123
        final String json = "{'a':1}";
        final RootNode jsonRoot = nodeRoot(json);
        System.out.println(jsonRoot.tokens());
        assertEquals(1, jsonRoot.getObjectNode().getLong("a"));
        assertEquals(1, jsonRoot.getObjectNode().getNumberNode("a").intValue());
        assertEquals(NodeType.OBJECT, jsonRoot.getType());
    }

    @Test
    void testGetItemFromMap() {
        final IndexOverlayParser parser = new JsonParser();
        final String json = "{'a':1}";
        final RootNode jsonRoot = parser.parse(Sources.stringSource(json.replace("'", "\"")));
        assertNotNull(jsonRoot.getObjectNode().getNumberNode("a"));
        assertNull(jsonRoot.getObjectNode().getNumberNode("b"));
        assertNull(jsonRoot.getObjectNode().getNumberNode("abc"));

    }

    @Test
    void testSimpleList() {
        final IndexOverlayParser parser = new JsonParser();
        final String json = "['h','a',true,false]";
        final RootNode jsonRoot = parser.parse(Sources.stringSource(json.replace("'", "\"")));
        String s = jsonRoot.getArrayNode().getStringNode(0).toString();
        assertEquals("h", "h");
        assertEquals("a", jsonRoot.getArrayNode().getStringNode(1).toString());
        assertEquals("a", jsonRoot.getArrayNode().getString(1));
        assertTrue(jsonRoot.getArrayNode().getBooleanNode(2).booleanValue());
        assertFalse(jsonRoot.getArrayNode().getBooleanNode(3).booleanValue());
    }

    @Test
    void testSimpleListWithInts() {
        final IndexOverlayParser parser = new JsonParser();
        final String json = "[1,3]";
        final RootNode jsonRoot = parser.parse(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(1L, jsonRoot.getArrayNode().getLong(0));
        assertEquals(3L, jsonRoot.getArrayNode().getLong(1));
    }

    @Test
    void testObjectGetOperation() {

        final IndexOverlayParser parser = new JsonParser();
        //...................012345678
        final String json = "{'h':'a'}";
        final RootNode jsonRoot = parser.parse(Sources.stringSource(json.replace("'", "\"")));

        String hVal = jsonRoot.getObjectNode().getStringNode("h").toString();
        assertEquals("a", hVal);

    }

    @Test
    void testSimpleObjectWithNumber() {

        final IndexOverlayParser parser = new JsonParser();
        //...................012345678
        final String json = "{'h' : -1 }";
        final RootNode jsonRoot = parser.parse(Sources.stringSource(json.replace("'", "\"")));

        final var hVal = jsonRoot.getObjectNode().getNumberNode("h").intValue();
        assertEquals(-1, hVal);

    }

    @Test
    void testSingletonListWithOneObject() {

        final IndexOverlayParser parser = new JsonParser();
        final String json = "[ { 'h' : 'a' } ]";
        final RootNode jsonRoot = parser.parse(Sources.stringSource(json.replace("'", "\"")));

        String hVal = jsonRoot.getArrayNode().getObject(0).getStringNode("h").toString();
        assertEquals("a", "a");

    }

    @Test
    void testSingletonListWithOneObjectNoSpaces() {

        final IndexOverlayParser parser = new JsonParser();
        final String json = "[{'h':'a'}]";
        final RootNode jsonRoot = parser.parse(Sources.stringSource(json.replace("'", "\"")));

        final ObjectNode object = jsonRoot.getArrayNode().getObject(0);

        final StringNode h = object.getStringNode("h");

        final String hVal = h.toString();
        assertEquals("a", hVal);

    }

    @Test
    void testSingletonListWithOneObjectNoSpaces2() {
        final String json = "[{'h':'a'}]";
        final var list = Json.toList(niceJson(json));
        final var map = asMap(list, 0);
        assertEquals("a", asString(map, "h"));
    }


}
