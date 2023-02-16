package com.cloudurable.jparse;

import com.cloudurable.jparse.node.ArrayNode;
import com.cloudurable.jparse.node.RootNode;
import com.cloudurable.jparse.source.Sources;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import static com.cloudurable.jparse.node.JsonTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectSerializerTest {


    @Test
    public void testMap() {
        final Parser parser = new JsonParser();
        final String json = "{'1':2,'2':7}";
        var elementMap = getJsonRoot(parser, json).getObjectNode().asObject();
        assertEquals(2L, elementMap.get("1").asScalar().longValue());
        assertEquals(7L, elementMap.get("2").asScalar().longValue());
    }


    @Test
    public void testMapBig() {
        final Parser parser = new JsonParser();
        final String json = "{'1':2,'2':7}";
        var elementMap = getJsonRoot(parser, json).getObjectNode().asObject();
        assertEquals(new BigInteger("2"), elementMap.get("1").asScalar().bigIntegerValue());
    }

    @Test
    public void testJsonObjectIsAMap() {
        final Parser parser = new JsonParser();
        final String json = "{'1':2.2,'2':7}";
        var elementMap = getJsonRoot(parser, json).getMap();
        assertEquals(2.2f, asFloat(elementMap, "1"));
        assertEquals(2.2d, asDouble(elementMap, "1"));
        assertEquals(new BigDecimal("2.2"), asBigDecimal(elementMap, "1"));

    }

    @Test
    public void testList() {
        final Parser parser = new JsonParser();
        final String json = "['1',2,2.2]";
        var elementList = getJsonRoot(parser, json).getArrayNode().asArray();
        var list = getJsonRoot(parser, json).getArrayNode();
        assertEquals("1", elementList.get(0).asScalar().toString());
        assertEquals(2L, list.get(1).asScalar().longValue());
        assertEquals(2.2d, list.get(2).asScalar().doubleValue());
    }

    @Test
    public void testComplexList() {
        final Parser parser = new JsonParser();
        final String json = "['1',2,2.2,[1,2,3]]";
        var elementList = getJsonRoot(parser, json).getArrayNode().asArray();
        var list = getJsonRoot(parser, json).getArrayNode();
        assertEquals("1", elementList.get(0).asScalar().value());
        assertEquals(2L, list.get(1).asScalar().longValue());
        assertEquals(2.2d, list.get(2).asScalar().doubleValue());
        var list2 = ((ArrayNode) elementList.get(3));
        assertEquals(3L, list2.get(2).asScalar().longValue());
    }

    @Test
    public void testComplexMap() {
        final Parser parser = new JsonParser();
        final String json = "{'1':2,'2':7,'abc':[1,2,3]}";
        var elementMap = getJsonRoot(parser, json).getObjectNode();
        var map = getJsonRoot(parser, json).getObjectNode();
        assertEquals(7L, map.get("2").asScalar().longValue());
        final var abcElement = elementMap.get("abc");
        final var list = ((ArrayNode) abcElement);
        assertEquals(3L, list.get(2).asScalar().longValue());
    }


    private RootNode getJsonRoot(Parser parser, String json1) {
        return parser.parse(Sources.stringSource(json1.replace("'", "\"")));
    }

}
