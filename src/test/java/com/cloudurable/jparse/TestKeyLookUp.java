package com.cloudurable.jparse;

import com.cloudurable.jparse.node.ObjectNode;
import com.cloudurable.jparse.node.RootNode;
import com.cloudurable.jparse.parser.JsonIndexOverlayParser;
import com.cloudurable.jparse.parser.JsonStrictParser;
import com.cloudurable.jparse.source.Sources;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestKeyLookUp {


    @Test
    void testStringKey() {
        final var parser = Json.builder().build();
        final var json1 = "'1'";
        final var json2 = "{'1':{'2':1}}";
        final var key = getJsonRoot(parser, json1).getNode();
        final var map = getJsonRoot(parser, json2).getObjectNode();
        final var value = map.getNode(key);
        assertTrue(value.isPresent());
        final var innerMap = value.get();
        assertTrue(innerMap instanceof ObjectNode);
        final var innerObject = (ObjectNode) innerMap;
        final var v = innerObject.getLong("2");
        assertEquals(1, v);
    }


    private RootNode getJsonRoot(JsonIndexOverlayParser parser, String json1) {
        return parser.parse(Sources.stringSource(json1.replace("'", "\"")));
    }
}
