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
import io.nats.jparse.source.Sources;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import static io.nats.jparse.node.JsonTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectSerializerTest {

    public JsonParser jsonParser() {
        return Json.builder().setStrict(true).build();
    }

    @Test
    public void testMap() {
        final JsonParser parser = jsonParser();
        final String json = "{'1':2,'2':7}";
        ObjectNode elementMap = getJsonRoot(parser, json).getObjectNode().asObject();
        assertEquals(2L, elementMap.get("1").asScalar().longValue());
        assertEquals(7L, elementMap.get("2").asScalar().longValue());
    }


    @Test
    public void testMapBig() {
        final JsonParser parser = jsonParser();
        final String json = "{'1':2,'2':7}";
        ObjectNode elementMap = getJsonRoot(parser, json).getObjectNode().asObject();
        assertEquals(new BigInteger("2"), elementMap.get("1").asScalar().bigIntegerValue());
    }

    @Test
    public void testJsonObjectIsAMap() {
        final JsonParser parser = jsonParser();
        final String json = "{'1':2.2,'2':7}";
        Map<String, Object> elementMap = getJsonRoot(parser, json).getMap();
        assertEquals(2.2f, asFloat(elementMap, "1"));
        assertEquals(2.2d, asDouble(elementMap, "1"));
        assertEquals(new BigDecimal("2.2"), asBigDecimal(elementMap, "1"));

    }

    @Test
    public void testList() {
        final JsonParser parser = jsonParser();
        final String json = "['1',2,2.2]";
        ArrayNode elementList = getJsonRoot(parser, json).getArrayNode().asArray();
        ArrayNode list = getJsonRoot(parser, json).getArrayNode();
        assertEquals("1", elementList.get(0).asScalar().toString());
        assertEquals(2L, list.get(1).asScalar().longValue());
        assertEquals(2.2d, list.get(2).asScalar().doubleValue());
    }

    @Test
    public void testComplexList() {
        final JsonParser parser = jsonParser();
        final String json = "['1',2,2.2,[1,2,3]]";
        ArrayNode elementList = getJsonRoot(parser, json).getArrayNode().asArray();
        ArrayNode list = getJsonRoot(parser, json).getArrayNode();
        assertEquals("1", elementList.get(0).asScalar().value());
        assertEquals(2L, list.get(1).asScalar().longValue());
        assertEquals(2.2d, list.get(2).asScalar().doubleValue());
        ArrayNode list2 = ((ArrayNode) elementList.get(3));
        assertEquals(3L, list2.get(2).asScalar().longValue());
    }

    @Test
    public void testComplexMap() {
        final JsonParser parser = jsonParser();
        final String json = "{'1':2,'2':7,'abc':[1,2,3]}";
        ObjectNode elementMap = getJsonRoot(parser, json).getObjectNode();
        ObjectNode map = getJsonRoot(parser, json).getObjectNode();
        assertEquals(7L, map.get("2").asScalar().longValue());
        final Node abcElement = elementMap.get("abc");
        final ArrayNode list = ((ArrayNode) abcElement);
        assertEquals(3L, list.get(2).asScalar().longValue());
    }


    private RootNode getJsonRoot(JsonParser parser, String json1) {
        return parser.parse(Sources.stringSource(json1.replace("'", "\"")));
    }

}
