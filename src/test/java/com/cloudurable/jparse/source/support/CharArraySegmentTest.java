package com.cloudurable.jparse.source.support;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharArraySegmentTest {

    @Test
    void test() {

        final var testString = "0123456789";

        final var chars = testString.toCharArray();

        final var testA = new CharArraySegment(5, chars.length - 5, chars);
        final var testAP1 = new CharArraySegment(5, chars.length - 5, testString.toCharArray());
        final var testB = new CharArraySegment(0, 5, chars);

        assertEquals("56789", testA.toString());
        assertEquals("56789".hashCode(), testA.hashCode());
        assertEquals("01234", testB.toString());
        assertEquals('0', testB.charAt(0));
        assertEquals('5', testA.charAt(0));

        assertNotEquals(testString, testB.toString());
        assertNotEquals(testA, testB);
        assertNotEquals(testA, testB.subSequence(1, testB.length()));
        assertEquals(testA, testAP1);


    }

    @Test
    void test2() {

        final var testString = "0123456789";

        final var chars = testString.toCharArray();

        final var testA = new CharArraySegment(5, chars.length - 5, chars);
        final var testB = new CharArraySegment(0, 5, chars);


        assertTrue(testA.equals("56789"));
        assertFalse(testA.equals("5678@"));
        assertFalse(testA.equals(Integer.valueOf("56789")));
        assertTrue(testB.equals("01234"));
        assertFalse(testA.equals(testString));


    }
}
