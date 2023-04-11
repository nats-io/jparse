package io.nats.jparse.bugs;

import io.nats.jparse.Json;
import io.nats.jparse.node.BooleanNode;
import io.nats.jparse.node.Node;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

public class ParserBugs {

    @Test
    void issue16() {
       Json.toMap(Json.niceJson("{\n" +
               "'created': '2020-03-03T13:26:23.023Z',\n" +
               "'name': 'l',\n" +
               "'subscription': true,\n" +
               "'id': 10,\n" +
               "'updated-by': 1,\n" +
               "'type': 'organization',\n" +
               "'updated': '2023-04-03T19:58:04.743Z',\n" +
               "'tags': []\n" +
               "}"));
    }

    @Test
    void issue13() {
        //See https://github.com/nats-io/jparse/issues/13

        final Map<String, Object> map = Json.toMap("{ \"key\": \"value\"," +
                " \"key1\": 123," +
                " \"key2\": [1,2,3], " +
                " \"key3\": true }"
                );

        assertTrue(map.containsKey("key"));
        assertEquals(map.size(), 4);
        assertFalse(map.isEmpty());

        // Map contains CharSequence as values not strings (which is a StringNode)
        assertTrue(map.get("key") instanceof CharSequence);
        // Map does not have int, long, float, double, etc. but instead java.lang.Number (which is a NumberNode)
        assertTrue(map.get("key1") instanceof Number);
        // JSON arrays are java.util.List and JSON objects are java.util.Map.
        assertTrue(map.get("key2") instanceof List);
        final Set<String> keys = map.keySet();
        assertEquals(new HashSet<>(Arrays.asList("key", "key1", "key2", "key3")),
                keys);
        Collection<Object> values = map.values();
        assertEquals(4, values.size());


        assertEquals("value", map.get("key").toString());
        assertEquals( map.get("key"), "value");
        assertEquals(123, ((Number)map.get("key1")).intValue());
        assertEquals(map.get("key1"), 123);
        assertTrue(((BooleanNode)map.get("key3")).booleanValue());
        assertEquals(map.get("key3"),true );



        //All read map methods should work.
        try {
            String key = map.entrySet().iterator().next().getKey();
            assertTrue(key.startsWith("key"));
        } catch (UnsupportedOperationException ex) {
           fail();
        }

        try {
            map.forEach((key, value) -> {
                assertTrue(key.startsWith("key"));
                assertTrue(value instanceof Node);
                assertTrue(value instanceof Number || value instanceof List
                            || value instanceof CharSequence
                            || value instanceof BooleanNode);
            });

        } catch (UnsupportedOperationException ex) {
            //This map does not support this.
            fail();
        }
        // Most map methods should work like getOrDefault, computeIfEmpty, computeIfPresent, etc.
    }
}
