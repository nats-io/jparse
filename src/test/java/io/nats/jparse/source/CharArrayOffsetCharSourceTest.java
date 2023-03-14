package io.nats.jparse.source;

import io.nats.jparse.Json;
import io.nats.jparse.node.support.ParseConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

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
        Assertions.assertEquals(ParseConstants.ETX, next);
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


    @Test
    void matchChars() {

        //...................01234567890123456789
        final String json = "     abcd ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        source.next();
        assertTrue(source.matchChars(1, 3, new String("bc")));
        assertFalse(source.matchChars(1, 3, new String("ab")));

    }

    @Test
    void isInteger() {

        //...................01234567890123456789
        final String json = "      123 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        source.next();
        assertTrue(source.isInteger(1, 3));

    }

    @Test
    void isIntegerFalse() {

        //...................01234567890123456789
        final String json = "     " + Long.MAX_VALUE + " ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        source.next();
        assertFalse(source.isInteger(1, json.length()-1));

    }

    @Test
    void parseDoubleSimple() {
        // .......................012345
        //...................01234567890123456789
        final String json = "      1.2 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertEquals(1.2, source.getDouble(1, 4));

    }

    @Test
    void parseDoubleExp() {
        // .......................012345
        //...................01234567890123456789
        final String json = "      1.2e12 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertEquals(1.2e12, source.getDouble(1, 7));

    }


    @Test
    void parseFloatSimple() {
        // .......................012345
        //...................01234567890123456789
        final String json = "      1.2 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertEquals(1.2f, source.getFloat(1, 4), 0.000001);

    }

    @Test
    void parseFloatExp() {
        // .......................012345
        //...................01234567890123456789
        final String json = "      1.2e12 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertEquals(1.2e12f, source.getFloat(1, 7), 0.000001);

    }



    @Test
    void parseIntSimple() {
        // .......................012345
        //...................01234567890123456789
        final String json = "      100 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertEquals("100", source.getString(1, 4));
        assertEquals(100, source.getInt(1, 4));

    }

    @Test
    void parseLongSimple() {
        // .......................012345
        //...................01234567890123456789
        final String json = "      100 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertEquals("100", source.getString(1, 4));
        assertEquals(100, source.getLong(1, 4));

    }

    @Test
    void getBigDecimal() {
        // .......................012345
        //...................01234567890123456789
        final String json = "      100 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertEquals("100", source.getString(1, 4));
        assertEquals(new BigDecimal(100), source.getBigDecimal(1, 4));

    }

    @Test
    void getBigInt() {
        // .......................012345
        //...................01234567890123456789
        final String json = "      100 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertEquals("100", source.getString(1, 4));
        assertEquals(new BigInteger("100"), source.getBigInteger(1, 4));

    }

    @Test
    void getEncodedString() {
        // ....................................012345678901
        //................................01234567890123456789
        final String json = Json.niceJson("     `b`n`r`t ");
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertEquals(Json.niceJson("`b`n`r`t"), source.getString(0, 8));
        assertEquals("\b\n\r\t", source.getEncodedString(0, 8));

    }

    @Test
    void toEncodedStringIfNeeded() {
        // ....................................012345678901
        //................................01234567890123456789
        final String json = Json.niceJson("     `b`n`r`t ");
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertEquals(Json.niceJson("`b`n`r`t"), source.getString(0, 8));
        assertEquals("\b\n\r\t", source.toEncodedStringIfNeeded(0, 8));

    }

    @Test
    void toEncodedStringIfNeededNotNeeded() {
        // ....................................012345678901
        //.................................01234567890123456789
        final String json = Json.niceJson("     himom ");
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertEquals("himom", source.toEncodedStringIfNeeded(0, 5));

    }




    @Test
    void findCommaOrEnd() {
        // .......................012345
        //...................01234567890123456789
        final String json = "       ,2] ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertFalse(source.findCommaOrEndForArray());
        assertEquals(2, source.getIndex());
    }

    @Test
    void findCommaOrEndEndCase() {
        // .......................012345
        //...................01234567890123456789
        final String json = "      ] ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertTrue(source.findCommaOrEndForArray());
        assertEquals(2, source.getIndex());
    }


    @Test
    void findObjectEndOrAttributeSep() {
        // .......................012345
        //...................01234567890123456789
        final String json = "       : 2 } ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertFalse(source.findObjectEndOrAttributeSep());
        assertEquals(2, source.getIndex());
    }

    @Test
    void findObjectEndOrAttributeSepEndCase() {
        // .......................012345
        //...................01234567890123456789
        final String json = "      } ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertTrue(source.findObjectEndOrAttributeSep());
        assertEquals(2, source.getIndex());
    }

    @Test
    void checkForJunk() {
        // .......................012345
        //...................01234567890123456789
        final String json = "      } ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        try {
            source.checkForJunk();
            fail();
        }catch (Exception ex) {

        }
    }


    @Test
    void getString() {
        // .......................012345
        //...................01234567890123456789
        final String json = "      100 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertEquals("100", source.getString(1, 4));

    }

    @Test
    void getArray() {
        // .......................012345
        //...................01234567890123456789
        final String json = "      100 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertEquals("100", new String(source.getArray(1, 4)));

    }

    @Test
    void getCharArraySeq() {
        // .......................012345
        //...................01234567890123456789
        final String json = "      100 ";
        final CharArrayOffsetCharSource source =
                new CharArrayOffsetCharSource(5, json.length() - 1, json.toCharArray());

        assertEquals("100", source.getCharSequence(1, 4).toString());

    }

}
