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
package io.nats.jparse.source.support;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharArraySegmentTest {

    @Test
    void test() {

        final String testString = "0123456789";

        final char[] chars = testString.toCharArray();

        final CharArraySegment testA = new CharArraySegment(5, chars.length - 5, chars);
        final CharArraySegment testAP1 = new CharArraySegment(5, chars.length - 5, testString.toCharArray());
        final CharArraySegment testB = new CharArraySegment(0, 5, chars);

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

        final String testString = "0123456789";

        final char[] chars = testString.toCharArray();

        final CharArraySegment testA = new CharArraySegment(5, chars.length - 5, chars);
        final CharArraySegment testB = new CharArraySegment(0, 5, chars);


        assertTrue(testA.equals("56789"));
        assertFalse(testA.equals("5678@"));
        assertFalse(testA.equals(Integer.valueOf("56789")));
        assertTrue(testB.equals("01234"));
        assertFalse(testA.equals(testString));


    }
}
