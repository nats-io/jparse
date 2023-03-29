package io.nats.jparse.bugs;

import io.nats.jparse.Json;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ParserBugs {

    @Test
    void issue13() {
        //See https://github.com/nats-io/jparse/issues/13

        final Map<String, Object> map = Json.toMap("{ \"key\": \"value\"," +
                " \"key2\": 123," +
                " \"key3\": [1,2,3] }");
        assertTrue(map.containsKey("key"));
        try {
            map.entrySet().iterator().next();
            fail();
        } catch (UnsupportedOperationException ex) {
            //This map does not support this.
        }
        assertEquals("value", map.get("key").toString());

        assertEquals( map.get("key"), "value");

        assertTrue(map.get("key") instanceof CharSequence);
        assertTrue(map.get("key2") instanceof Number);
        assertTrue(map.get("key3") instanceof List);

        assertEquals(123, ((Number)map.get("key2")).intValue());

        assertEquals(map.get("key2"), 123);

    }
}
