package io.nats.jparse.source.support;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParseDoubleTest {

    @Test
    public void testParseDouble() {
        char[] input1 = "123.456".toCharArray();
        double result1 = ParseDouble.parseDouble(input1, 0, input1.length);
        assertEquals(123.456, result1, 1e-6);

        char[] input2 = "-0.5".toCharArray();
        double result2 = ParseDouble.parseDouble(input2, 0, input2.length );
        assertEquals(-0.5, result2, 1e-6);

        char[] input3 = "1.23e4".toCharArray();
        double result3 = ParseDouble.parseDouble(input3, 0, input3.length );
        assertEquals(1.23e4, result3, 1e-6);

        char[] input4 = "-1.23e-4".toCharArray();
        double result4 = ParseDouble.parseDouble(input4, 0, input4.length );
        assertEquals(-1.23e-4, result4, 1e-10);
    }

    @Test
    public void testParseDoubleInvalidInput() {
        char[] input1 = "123a.456".toCharArray();
        assertThrows(UnexpectedCharacterException.class, () -> ParseDouble.parseDouble(input1, 0, input1.length));

        char[] input2 = "123..456".toCharArray();
        assertThrows(UnexpectedCharacterException.class, () -> ParseDouble.parseDouble(input2, 0, input2.length));

        char[] input3 = "1.23e4.5".toCharArray();
        assertThrows(UnexpectedCharacterException.class, () -> ParseDouble.parseDouble(input3, 0, input3.length));
    }

    @Test
    public void testParseDoubleWithIntegerPartOnly() {
        char[] chars = {'1', '2', '3', '4', '5'};
        double f = ParseDouble.parseDouble(chars, 0, chars.length);
        assertEquals(Double.parseDouble(new String(chars)), f, 0.001f);
    }

    @Test
    public void testParseDoubleWithFractionalPartOnly() {
        char[] chars = {'.', '1', '2', '3', '4', '5'};
        double f = ParseDouble.parseDouble(chars, 0, chars.length);
        assertEquals(Double.parseDouble(new String(chars)), f, 0.001f);
    }

    @Test
    public void testParseDoubleWithExponentOnly() {
        char[] chars = {'1', 'e', '2'};
        double f = ParseDouble.parseDouble(chars, 0, chars.length);
        assertEquals(Double.parseDouble(new String(chars)), f, 0.001f);
    }

    @Test
    public void testParseDoubleWithPositiveExponent() {
        char[] chars = {'1', '2', '3', 'e', '4'};
        double f = ParseDouble.parseDouble(chars, 0, chars.length);
        assertEquals(Double.parseDouble(new String(chars)), f, 0.001f);
    }

    @Test
    public void testParseDoubleWithNegativeExponent() {
        char[] chars = {'1', '2', '3', 'e', '-', '4'};
        double f = ParseDouble.parseDouble(chars, 0, chars.length);
        assertEquals(Double.parseDouble(new String(chars)), f, 0.001f);
    }
}
