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
package io.nats.jparse.source;

import io.nats.jparse.Json;
import io.nats.jparse.node.support.NumberParseResult;
import io.nats.jparse.node.support.ParseConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharArrayCharSourceTest {

    @Test
    void next() {

        //...................01234567890123456789
        final String json = "01";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        char next = (char) source.next();
        assertEquals('0', next);
        assertEquals(0, source.getIndex());

        next = (char) source.next();
        assertEquals('1', next);
        assertEquals(1, source.getIndex());

        next = (char) source.next();
        Assertions.assertEquals(ParseConstants.ETX, next);
        assertEquals(json.length(), source.getIndex());

    }

    /* Basic test to test nextSkipWhiteSpace and getIndex */
    @Test
    void nextSkipWhiteSpace2() {

        //...................01234567890123456789
        final String json = "\t\n01";

        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        char next = (char) source.nextSkipWhiteSpace();
        assertEquals('0', next);
        assertEquals(2, source.getIndex());

        next = (char) source.nextSkipWhiteSpace();
        assertEquals('1', next);
        assertEquals(3, source.getIndex());

        next = (char) source.nextSkipWhiteSpace();
        assertEquals(ParseConstants.ETX, next);
        assertEquals(4, source.getIndex());

    }


    /* Basic test to test next and getIndex */
    @Test
    void skipWhiteSpace2() {

        //...................01234567890123456789
        final String json = "\t\n01";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        source.next();

        source.skipWhiteSpace();

        char next = (char) source.getCurrentChar();
        assertEquals('0', next);
        assertEquals(2, source.getIndex());

        next = (char) source.nextSkipWhiteSpace();
        assertEquals('1', next);
        assertEquals(3, source.getIndex());
        assertEquals('1', source.getCurrentChar());
        assertEquals('1', source.getCurrentCharSafe());

        next = (char) source.nextSkipWhiteSpace();
        assertEquals(ParseConstants.ETX, next);
        assertEquals(4, source.getIndex());

        // Does bound check so returns end of stream.
        assertEquals(ParseConstants.ETX, source.getCurrentCharSafe());

        try {
            //Does no bounds check so gets the next char which is a space.
            assertEquals(' ', source.getCurrentChar());
            fail();
        } catch (Exception ex) {

        }

    }


    @Test
    void getCharAt() {

        //...................01234567890123456789
        final String json = "01";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        assertEquals('0', source.getChartAt(0));
        assertEquals('1', source.getChartAt(1));

        try {
            /* No bounds check */
            assertEquals(' ', source.getChartAt(2));
            fail();
        } catch (Exception ex) {

        }

    }

    @Test
    void toStringTest() {

        //...................01234567890123456789
        final String json = "01";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        assertEquals("01", source.toString());

    }


    @Test
    void findEndOfNumberFast() {

        //...................01234567890123456789
        final String json = "01";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        final NumberParseResult result  = source.findEndOfNumberFast();
        assertEquals(2, result.endIndex());
        assertFalse(result.wasFloat());

    }

    @Test
    void findEndOfNumberFastFloat() {

        //...................01234567890123456789
        final String json = "0.2";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        final NumberParseResult result  = source.findEndOfNumberFast();
        assertEquals(3, result.endIndex());
        assertTrue(result.wasFloat());

    }

    @Test
    void findEndOfNumberFastFloatExponentInArray() {

        //...................01234567890123456789
        final String json = "0.2e12] ";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        final NumberParseResult result  = source.findEndOfNumberFast();
        assertEquals(6, result.endIndex());
        assertTrue(result.wasFloat());

    }



    @Test
    void findEndOfNumberFastInArray() {

        //...................01234567890123456789
        final String json = "01]";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        final NumberParseResult result  = source.findEndOfNumberFast();
        assertEquals(2, result.endIndex());
        assertFalse(result.wasFloat());

    }

    @Test
    void findEndOfNumberFastFloatInArray() {

        //...................01234567890123456789
        final String json = "0.2]";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        final NumberParseResult result  = source.findEndOfNumberFast();
        assertEquals(3, result.endIndex());
        assertTrue(result.wasFloat());

    }

    @Test
    void findEndOfNumberFastFloatExponentInArray2() {

        //...................01234567890123456789
        final String json = "0.2e12] ";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        final NumberParseResult result  = source.findEndOfNumberFast();
        assertEquals(6, result.endIndex());
        assertTrue(result.wasFloat());

    }



    @Test
    void findEndOfNumber() {

        //...................01234567890123456789
        final String json = "12";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));
        source.next();

        final NumberParseResult result  = source.findEndOfNumber();
        assertEquals(2, result.endIndex());
        assertFalse(result.wasFloat());

    }

    @Test
    void findEndOfNumberFloat() {

        //...................01234567890123456789
        final String json = "0.2";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        source.next();

        final NumberParseResult result  = source.findEndOfNumber();
        assertEquals(3, result.endIndex());
        assertTrue(result.wasFloat());

    }

    @Test
    void findEndOfNumberFloatExponentInArray() {

        //...................01234567890123456789
        final String json = "0.2e12] ";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        source.next();
        final NumberParseResult result  = source.findEndOfNumber();
        assertEquals(6, result.endIndex());
        assertTrue(result.wasFloat());

    }



    @Test
    void findEndOfNumberInArray() {

        //...................01234567890123456789
        final String json = "12]";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        source.next();

        final NumberParseResult result  = source.findEndOfNumber();
        assertEquals(2, result.endIndex());
        assertFalse(result.wasFloat());

    }

    @Test
    void findEndOfNumberFloatInArray() {

        //...................01234567890123456789
        final String json = "0.2]";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        source.next();
        final NumberParseResult result  = source.findEndOfNumber();
        assertEquals(3, result.endIndex());
        assertTrue(result.wasFloat());

    }

    @Test
    void findEndOfNumberFloatExponentInArray2() {

        //...................01234567890123456789
        final String json = "0.2e12] ";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        source.next();
        final NumberParseResult result  = source.findEndOfNumber();
        assertEquals(6, result.endIndex());
        assertTrue(result.wasFloat());

    }

    @Test
    void findTrueEnd() {

        //...................01234567890123456789
        final String json = "true";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        source.next();
        assertEquals(4, source.findTrueEnd());

    }

    @Test
    void findFalseEnd() {

        //...................01234567890123456789
        final String json = "false";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        source.next();
        assertEquals(5, source.findFalseEnd());

    }

    @Test
    void findNullEnd() {

        //...................01234567890123456789
        final String json = "null";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        source.next();
        assertEquals(4, source.findNullEnd());

    }


    @Test
    void matchChars() {

        //...................01234567890123456789
        final String json = "abcd";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        source.next();
        assertTrue(source.matchChars(1, 3, new String("bc")));
        assertFalse(source.matchChars(1, 3, new String("ab")));

    }


    @Test
    void parseDoubleSimple() {

        //...................01234567890123456789
        final String json = " 1.2 ";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        assertEquals(1.2, source.getDouble(1, 4));

    }

    @Test
    void parseDoubleExp() {

        //...................01234567890123456789
        final String json = " 1.2e12 ";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        assertEquals(1.2e12, source.getDouble(1, 7));

    }

    @Test
    void findCommaOrEnd() {
        // .......................012345
        //...................01234567890123456789
        final String json = "  ,2] ";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));

        source.next();

        assertFalse(source.findCommaOrEndForArray());
        assertEquals(2, source.getIndex());

    }

    @Test
    void findCommaOrEndEndCase() {
        // .......................012345
        //...................01234567890123456789
        final String json = "  ] ";
        final CharSource source =
                Sources.stringSource(Json.niceJson(json));
        source.next();

        assertTrue(source.findCommaOrEndForArray());
        assertEquals(3, source.getIndex());
    }


    @Test
    void readNumberWithNothingBeforeDecimal() {
        //.................0123456789
        final String json = "-.123";
        final CharSource source = Sources.stringSource(Json.niceJson(json));
        source.next();
        try {
            final NumberParseResult result = source.findEndOfNumber();
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void readNumberNormalNegativeFloat() {
        //.................0123456789
        final String json = "-0.123";
        final CharSource source = Sources.stringSource(Json.niceJson(json));
        source.next();
        final NumberParseResult result = source.findEndOfNumber();
        assertTrue(result.wasFloat());
        assertEquals(6, result.endIndex());
    }


    @Test
    void encoding() {

        //.................0123456789
        final String json = "'`u0003'";
        final CharSource source = Sources.stringSource(Json.niceJson(json));
        source.next();
        int end = source.findEndOfEncodedString();
        assertEquals(7, end);
        assertEquals(8, source.getIndex());
    }

    @Test
    void encodingFast() {

        //.................0123456789
        final String json = "'`u0003'";
        final CharSource source = Sources.stringSource(Json.niceJson(json));
        source.next();
        int end = source.findEndOfEncodedStringFast();
        assertEquals(7, end);
        assertEquals(8, source.getIndex());
    }

    @Test
    void encoding2() {

        //.................0123456789012
        final String json = "'abc`n`u0003'";
        final CharSource source = Sources.stringSource(Json.niceJson(json));
        source.next();
        int end = source.findEndOfEncodedString();
        assertEquals(12, end);
        assertEquals(13, source.getIndex());
    }

    @Test
    void encoding2Fast() {

        //.................0123456789012
        final String json = "'abc`n`u0003'";
        final CharSource source = Sources.stringSource(Json.niceJson(json));
        source.next();
        int end = source.findEndOfEncodedStringFast();
        assertEquals(12, end);
        assertEquals(13, source.getIndex());
    }

    @Test
    void encoding3() {

        //.................0123456789
        final String json =  "'abc`n`b`r`u1234'";
        final CharSource source = Sources.stringSource(Json.niceJson(json));
        source.next();
        int end = source.findEndOfEncodedString();
        assertEquals(16, end);

    }


    // final String json =;




    @Test
    void nextSkipWhiteSpaceSample() {
        //.....................01234
        final String string = "[ 1 , 3 ]";
        final CharSource charSource = Sources.stringSource(string);


        assertEquals('[', (char) charSource.nextSkipWhiteSpace());
        assertEquals('1', (char) charSource.nextSkipWhiteSpace());
        assertEquals(',', (char) charSource.nextSkipWhiteSpace());
        assertEquals('3', (char) charSource.nextSkipWhiteSpace());
        assertEquals(']', (char) charSource.nextSkipWhiteSpace());


    }

    @Test
    void skipWhiteSpace() {
        //.....................012345678901234567890123456789012345678
        final String string = "         1               [ 1 , 3 ]";
        final CharSource source = Sources.stringSource(string);
        assertEquals('1', (char) source.nextSkipWhiteSpace());
        source.next();
        source.skipWhiteSpace();
        assertEquals(25, source.getIndex());
        assertEquals('[', (char) source.getCurrentChar());



    }
    @Test
    void testSimpleObjectTwoItemsWeirdSpacing() {

        //.................. 0123456789012345678901234567890123456789012345
        final String json = "   {'h':   'a',\n\t 'i':'b'\n\t } \n\t    \n";


        final CharSource charSource = Sources.stringSource(Json.niceJson(json));
        assertEquals('{', (char) charSource.nextSkipWhiteSpace());
        assertEquals('"', (char) charSource.nextSkipWhiteSpace());
        assertEquals('h', (char) charSource.nextSkipWhiteSpace());
        assertEquals('"', (char) charSource.nextSkipWhiteSpace());
        assertEquals(':', (char) charSource.nextSkipWhiteSpace());
        assertEquals('"', (char) charSource.nextSkipWhiteSpace());
        assertEquals('a', (char) charSource.nextSkipWhiteSpace());
        assertEquals('"', (char) charSource.nextSkipWhiteSpace());
        assertEquals(',', (char) charSource.nextSkipWhiteSpace());
        assertEquals('"', (char) charSource.nextSkipWhiteSpace());
        assertEquals('i', (char) charSource.nextSkipWhiteSpace());
        assertEquals('"', (char) charSource.nextSkipWhiteSpace());
        assertEquals(':', (char) charSource.nextSkipWhiteSpace());
        assertEquals('"', (char) charSource.nextSkipWhiteSpace());
        assertEquals('b', (char) charSource.nextSkipWhiteSpace());
        assertEquals('"', (char) charSource.nextSkipWhiteSpace());
        assertEquals('}', (char) charSource.nextSkipWhiteSpace());
    }

    @Test
    void nextSkipWhiteSpace() {
        //..................01234
        final String string = "   12";
        final CharSource charSource = Sources.stringSource(string);

        char ch = (char) charSource.nextSkipWhiteSpace();


        assertEquals('1', ch);
        assertEquals('1', charSource.getCurrentChar());
        assertEquals(3, charSource.getIndex());
        assertEquals('2', charSource.next());
        assertEquals(4, charSource.getIndex());


    }

    @Test
    void parseDouble() {


        String s = "1.0e9";
        CharSource charSource = Sources.stringSource(s);
        assertEquals(1000000000.0, charSource.getDouble(0, s.length()));


        s = "1e9";
        charSource = Sources.stringSource(s);
        assertEquals(1000000000.0, charSource.getDouble(0, s.length()));


        s = "1.1";
        charSource = Sources.stringSource(s);
        assertEquals(1.1, charSource.getDouble(0, s.length()));


        s = "1.0e12";
        charSource = Sources.stringSource(s);
        assertEquals(1e12, charSource.getDouble(0, s.length()));

    }

    @Test
    void isInteger() {
        String s = "-1";
        CharSource charSource = Sources.stringSource(s);
        assertTrue(charSource.isInteger(0, s.length()));
        assertEquals(-1, charSource.getInt(0, s.length()));
    }

    @Test
    void isIntegerFalse() {

        //...................01234567890123456789
        final String json = "" + Long.MAX_VALUE;
        CharSource source = Sources.stringSource(json);

        source.next();
        assertFalse(source.isInteger(1, json.length()));

    }

    @Test
    void isIntegerButLong() {
        String s = "" + Long.MAX_VALUE;
        CharSource charSource = Sources.stringSource(s);
        assertFalse(charSource.isInteger(0, s.length()));

        long value = ((long) Integer.MAX_VALUE) + 1L;
        s = "" + value;
        charSource = Sources.stringSource(s);
        assertFalse(charSource.isInteger(0, s.length()));


        value = Integer.MAX_VALUE;
        s = "" + value;
        charSource = Sources.stringSource(s);
        assertTrue(charSource.isInteger(0, s.length()));


        value = Integer.MIN_VALUE;
        s = "" + value;
        charSource = Sources.stringSource(s);
        assertTrue(charSource.isInteger(0, s.length()));


        value = ((long) Integer.MIN_VALUE) - 1L;
        s = "" + value;
        charSource = Sources.stringSource(s);
        assertFalse(charSource.isInteger(0, s.length()));

    }
}
