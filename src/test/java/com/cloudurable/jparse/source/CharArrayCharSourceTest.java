package com.cloudurable.jparse.source;

import com.cloudurable.jparse.Json;
import com.cloudurable.jparse.parser.IndexOverlayParser;
import com.cloudurable.jparse.parser.JsonParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharArrayCharSourceTest {


    @Test
    void nextSkipWhiteSpaceSample() {
        //.....................01234
        final String string = "[ 1 , 3 ]";
        final var charSource = Sources.stringSource(string);


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
        final var source = Sources.stringSource(string);
        assertEquals('1', (char) source.nextSkipWhiteSpace());
        source.next();
        source.skipWhiteSpace();
        assertEquals(25, source.getIndex());
        assertEquals('[', (char) source.getCurrentChar());



    }
    @Test
    void testSimpleObjectTwoItemsWeirdSpacing() {
        final IndexOverlayParser parser = new JsonParser();
        //.................. 0123456789012345678901234567890123456789012345
        final String json = "   {'h':   'a',\n\t 'i':'b'\n\t } \n\t    \n";


        final var charSource = Sources.stringSource(Json.niceJson(json));
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
        final var string = "   12";
        final var charSource = Sources.stringSource(string);

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
