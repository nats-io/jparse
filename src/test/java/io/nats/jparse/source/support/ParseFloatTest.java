package io.nats.jparse.source.support;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParseFloatTest {


    @Test
    public void testParseFloat() {

        // Test normal case
        char[] chars1 = {'1', '.', '2', '3', 'e', '4'};
        float f1 = ParseFloat.parseFloat(chars1, 0, chars1.length);
        assertEquals(1.23e4f, f1, 0.001f);

        // Test negative number
        char[] chars2 = {'-', '5', '.', '6', '7', 'e', '-', '4'};
        float f2 = ParseFloat.parseFloat(chars2, 0, chars2.length);
        assertEquals(-5.67e-4f, f2, 0.001f);



        // Test zero
        char[] chars3 = {'0'};
        float f3 = ParseFloat.parseFloat(chars3, 0, chars3.length);
        assertEquals(0f, f3, 0.001f);

        // Test integer
        char[] chars4 = {'4', '2'};
        float f4 = ParseFloat.parseFloat(chars4, 0, chars4.length);
        assertEquals(42f, f4, 0.001f);
    }


    @Test
    public void testParseFloatWithIntegerPartOnly() {
        char[] chars = {'1', '2', '3', '4', '5'};
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(Float.parseFloat(new String(chars)), f, 0.001f);
    }

    @Test
    public void testParseFloatWithFractionalPartOnly() {
        char[] chars = {'.', '1', '2', '3', '4', '5'};
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(Float.parseFloat(new String(chars)), f, 0.001f);
    }

    @Test
    public void testParseFloatWithExponentOnly() {
        char[] chars = {'1', 'e', '2'};
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(Float.parseFloat(new String(chars)), f, 0.001f);
    }

    @Test
    public void testParseFloatWithPositiveExponent() {
        char[] chars = {'1', '.', '2', 'e', '2'};
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(Float.parseFloat(new String(chars)), f, 0.001f);
    }

    @Test
    public void testParseFloatWithNegativeExponent() {
        char[] chars = {'1', '.', '2', 'e', '-', '2'};
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(Float.parseFloat(new String(chars)), f, 0.001f);
    }

    @Test
    public void testParseFloatWithNegativeSign() {
        char[] chars = {'-', '1', '.', '2', 'e', '2'};
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(Float.parseFloat(new String(chars)), f, 0.001f);
    }

    @Test
    public void testParseFloatWithNegativeExponentAndFractionalPart() {
        char[] chars = {'-', '1', '.', '2', '3', 'e', '-', '2'};
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(Float.parseFloat(new String(chars)), f, 0.001f);
    }

    @Test
    public void testParseFloatWithPositiveExponentAndFractionalPart() {
        char[] chars = {'1', '.', '2', '3', 'e', '2'};
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(Float.parseFloat(new String(chars)), f, 0.001f);
    }

    @Test
    public void testParseFloatWithMultipleExponents() {

        assertThrows(IllegalStateException.class, () -> {
            char[] chars = {'1', 'e', '2', 'e', '3'};
            float f = ParseFloat.parseFloat(chars, 0, chars.length);
        });

    }

    @Test
    public void testParseFloatWithMultipleFractions() {

        assertThrows(IllegalStateException.class, () -> {
            char[] chars = {'1', '.', '2', '.', '3', '.', '4', '.', '5'};
            float f = ParseFloat.parseFloat(chars, 0, chars.length);
        });

    }

    @Test
    public void testParseFloatWithLeadingZeros() {
        char[] chars = {'0', '0', '1', '.', '2', '3'};
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(Float.parseFloat(new String(chars)), f, 0.001f);
    }


    @Test
    public void testParseFloatWithTrailingZeros() {
        char[] chars = {'1', '.', '2', '3', '0', '0', '0', '0'};
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(Float.parseFloat(new String(chars)), f, 0.001f);
    }

    @Test
    void testNormalCase() {
        char[] chars = {'1', '.', '2', '3', 'e', '4'};
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(1.23e4f, f, 0.001f);
    }

    @Test
    void testPositiveExponent() {
        char[] chars = {'1', '0', 'e', '3'};
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(1e4f, f, 0.001f);
    }

    @Test
    void testNegativeExponent() {
        char[] chars = {'1', 'e', '-', '2'};
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(0.01f, f, 0.001f);
    }

    @Test
    void testExponentZero() {
        char[] chars = {'1', 'e', '0'};
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(1f, f, 0.001f);
    }

    @Test
    void testLargePositiveExponent() {
        char[] chars = {'1', '0', 'e', '9'};
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(1e10f, f, 0.001f);
    }

    @Test
    void testLargeNegativeExponent() {
        char[] chars = {'1', 'e', '-', '9'};
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(0.0001f, f, 0.001f);
    }

    @Test
    void testExponent18() {
        char[] chars = {'1',  '.', '0', 'e', '1', '8'};
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(1e18f, f, 0.001f);
    }

    @Test
    void testExponentNegative18() {
        char[] chars = {'1', '.', '0', 'e', '-', '1', '8'};
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(1e-18f, f, 0.001f);
    }

    @Test
    void testExponent19() {
        char[] chars = {'1',  '.', '0', 'e', '1', '9'};
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(1e19f, f, 0.001f);
    }

    @Test
    void testExponentCrazy() {
        final String str = "-9087.123456e-40";
        char [] chars = str.toCharArray();
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(Float.parseFloat(str), f, 0.00001f);
    }

    @Test
    void testExponentCrazy2() {
        final String str = "9087.123456e40";
        char [] chars = str.toCharArray();
        float f = ParseFloat.parseFloat(chars, 0, chars.length);
        assertEquals(Float.parseFloat(str), f, 0.00001f);
    }
}
