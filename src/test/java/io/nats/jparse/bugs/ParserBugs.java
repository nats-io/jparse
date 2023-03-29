package io.nats.jparse.bugs;

import io.nats.jparse.Json;
import io.nats.jparse.node.BooleanNode;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

public class ParserBugs {

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



        //Not all map methods work.
        try {
            map.entrySet().iterator().next();
            fail();
        } catch (UnsupportedOperationException ex) {
            //This map does not support this.
        }

        try {
            map.forEach((s, o) -> {
            });
            fail();
        } catch (UnsupportedOperationException ex) {
            //This map does not support this.
        }
        // Most map methods should work like getOrDefault, computeIfEmpty, computeIfPresent, etc.
    }
}
