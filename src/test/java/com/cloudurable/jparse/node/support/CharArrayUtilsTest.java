package com.cloudurable.jparse.node.support;

import org.junit.jupiter.api.Test;

import static com.cloudurable.jparse.Json.niceJson;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CharArrayUtilsTest {

    @Test
    void decodeJsonString() {
        final String encodedString = niceJson("hello `b `n `b `u1234 ");
        final var result = CharArrayUtils.decodeJsonString(encodedString.toCharArray(), 0, encodedString.length());

        final int expectedCount = encodedString.length() - 3 - 5;
        assertEquals(expectedCount, result.length());
        assertEquals("hello \b \n \b \u1234 ", result);
    }
}
