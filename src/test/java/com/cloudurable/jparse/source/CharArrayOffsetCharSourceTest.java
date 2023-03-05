package com.cloudurable.jparse.source;

import com.cloudurable.jparse.node.support.ParseConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharArrayOffsetCharSourceTest {

    /* Basic test to test next and getIndex */
    @Test
    void next() {

        //...................01234567890123456789
        final String json = "     01 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length()-1, json.toCharArray());

        char next = (char) source.next();
        assertEquals('0', next);
        assertEquals(0, source.getIndex());

        next = (char) source.next();
        assertEquals('1', next);
        assertEquals(1, source.getIndex());

        next = (char) source.next();
        assertEquals(ParseConstants.ETX, next);
        assertEquals(2, source.getIndex());

    }

    /* Basic test to test nextSkipWhiteSpace and getIndex */
    @Test
    void nextSkipWhiteSpace() {

        //...................01234567890123456789
        final String json = "     \t\n01 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length()-1, json.toCharArray());

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
    void skipWhiteSpace() {

        //...................01234567890123456789
        final String json = "     \t\n01 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length()-1, json.toCharArray());

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

        //Does no bounds check so gets the next char which is a space.
        assertEquals(' ', source.getCurrentChar());

    }


    @Test
    void getCharAt() {

        //...................01234567890123456789
        final String json = "     01 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertEquals('0', source.getChartAt(0));
        assertEquals('1', source.getChartAt(1));

        /* No bounds check */
        assertEquals(' ', source.getChartAt(2));

    }

    @Test
    void toStringTest() {

        //...................01234567890123456789
        final String json = "     01 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertEquals("01", source.toString());

    }

    @Test
    void findEndOfNumberFast() {

        //...................01234567890123456789
        final String json = "     01 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        final var result  = source.findEndOfNumberFast();
        assertEquals(2, result.endIndex());
        assertFalse(result.wasFloat());

    }

    @Test
    void findEndOfNumberFastFloat() {

        //...................01234567890123456789
        final String json = "     0.2 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        final var result  = source.findEndOfNumberFast();
        assertEquals(3, result.endIndex());
        assertTrue(result.wasFloat());

    }

    @Test
    void findEndOfNumberFastFloatExponent() {

        //...................01234567890123456789
        final String json = "     0.2e12 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        final var result  = source.findEndOfNumberFast();
        assertEquals(6, result.endIndex());
        assertTrue(result.wasFloat());

    }



    @Test
    void findEndOfNumberFastInArray() {

        //...................01234567890123456789
        final String json = "     01] ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        final var result  = source.findEndOfNumberFast();
        assertEquals(2, result.endIndex());
        assertFalse(result.wasFloat());

    }

    @Test
    void findEndOfNumberFastFloatInArray() {

        //...................01234567890123456789
        final String json = "     0.2] ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        final var result  = source.findEndOfNumberFast();
        assertEquals(3, result.endIndex());
        assertTrue(result.wasFloat());

    }

    @Test
    void findEndOfNumberFastFloatExponentInArray() {

        //...................01234567890123456789
        final String json = "     0.2e12] ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        final var result  = source.findEndOfNumberFast();
        assertEquals(6, result.endIndex());
        assertTrue(result.wasFloat());

    }



    @Test
    void findEndOfNumber() {

        //...................01234567890123456789
        final String json = "     12 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        final var result  = source.findEndOfNumber();
        assertEquals(2, result.endIndex());
        assertFalse(result.wasFloat());

    }

    @Test
    void findEndOfNumberFloat() {

        //...................01234567890123456789
        final String json = "     0.2 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        final var result  = source.findEndOfNumber();
        assertEquals(3, result.endIndex());
        assertTrue(result.wasFloat());

    }

    @Test
    void findEndOfNumberFloatExponent() {

        //...................01234567890123456789
        final String json = "     0.2e12 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        final var result  = source.findEndOfNumber();
        assertEquals(6, result.endIndex());
        assertTrue(result.wasFloat());

    }



    @Test
    void findEndOfNumberInArray() {

        //...................01234567890123456789
        final String json = "     01] ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        final var result  = source.findEndOfNumber();
        assertEquals(2, result.endIndex());
        assertFalse(result.wasFloat());

    }

    @Test
    void findEndOfNumberFloatInArray() {

        //...................01234567890123456789
        final String json = "     0.2] ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        final var result  = source.findEndOfNumber();
        assertEquals(3, result.endIndex());
        assertTrue(result.wasFloat());

    }

    @Test
    void findEndOfNumberFloatExponentInArray() {

        //...................01234567890123456789
        final String json = "     0.2e12] ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        final var result  = source.findEndOfNumber();
        assertEquals(6, result.endIndex());
        assertTrue(result.wasFloat());

    }

    @Test
    void findTrueEnd() {

        //...................01234567890123456789
        final String json = "     true ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        source.next();
        assertEquals(4, source.findTrueEnd());

    }

    @Test
    void findFalseEnd() {

        //...................01234567890123456789
        final String json = "     false ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        source.next();
        assertEquals(5, source.findFalseEnd());

    }

    @Test
    void findNullEnd() {

        //...................01234567890123456789
        final String json = "     null ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        source.next();
        assertEquals(4, source.findNullEnd());

    }

}
