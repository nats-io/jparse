package com.cloudurable.jparse.source;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CharSourceNumberParse {


    @Test
    void parseMissingDecimalMantissa() {
        //.....................01234
        final String string = "9.e+ ";
        final var charSource = Sources.stringSource(string);
        charSource.next();

        try {
            charSource.findEndOfNumber();
            assertTrue(false);
        } catch (Exception ex) {
        }
    }


    @Test
    void parseMissingNumbersAfterExponent() {
        //.....................01234
        final String string = "1.0e+ ";
        final var charSource = Sources.stringSource(string);
        charSource.next();
        try {
            charSource.findEndOfNumber();
            assertTrue(false);
        } catch (Exception ex) {
        }
    }

    @Test
    void parseMissingNumbersAfterExponent4() {
        //.....................01234
        final String string = "0.3e+ ";
        final var charSource = Sources.stringSource(string);
        charSource.next();
        try {
            charSource.findEndOfNumber();
            assertTrue(false);
        } catch (Exception ex) {
        }
    }

    @Test
    void parseMissingDecimalMantissa2() {
        //.....................01234
        final String string = "2.e3";
        final var charSource = Sources.stringSource(string);
        charSource.next();
        try {
            charSource.findEndOfNumber();
            assertTrue(false);
        } catch (Exception ex) {
        }
    }


    @Test
    void parseMissingNumbersAfterExponent2() {
        //.....................01234
        final String string = "1.0e- ";
        final var charSource = Sources.stringSource(string);
        charSource.next();
        try {
            charSource.findEndOfNumber();
            assertTrue(false);
        } catch (Exception ex) {
        }
    }

    @Test
    void parseMissingNumbersAfterExponent3() {
        //.....................01234
        final String string = "2.e-3 ";
        final var charSource = Sources.stringSource(string);
        charSource.next();
        try {
            charSource.findEndOfNumber();
            assertTrue(false);
        } catch (Exception ex) {
        }
    }


    @Test
    void startsWith0() {
        //.....................01234
        final String string = "012 ";
        final var charSource = Sources.stringSource(string);
        charSource.next();
        try {
            charSource.findEndOfNumber();
            assertTrue(false);
        } catch (Exception ex) {
        }
    }

    @Test
    void startsWithPlus() {
        //.....................01234
        final String string = "+1 ";
        final var charSource = Sources.stringSource(string);
        charSource.next();
        try {
            charSource.findEndOfNumber();
            assertTrue(false);
        } catch (Exception ex) {
        }
    }


    @Test
    void bigExponentMissingNumber() {
        //.....................01234
        final String string = "0E+ ";
        final var charSource = Sources.stringSource(string);
        charSource.next();
        try {
            charSource.findEndOfNumber();
            assertTrue(false);
        } catch (Exception ex) {
        }
    }

    /*
     * PASS! ./src/test/resources/validation/n_number_neg_int_starting_with_zero.json
     * [-012]
     *
     * PASS! ./src/test/resources/validation/n_number_minus_space_1.json
     * [- 1]
     * PASS! ./src/test/resources/validation/n_number_neg_real_without_int_part.json
     * [-.123]
     */

    @Test
    void negativeStartsWith0() {
        //.....................01234
        final String string = "-012 ";
        final var charSource = Sources.stringSource(string);
        charSource.next();
        try {
            charSource.findEndOfNumber();
            assertTrue(false);
        } catch (Exception ex) {
        }
    }

    @Test
    void cantBeJustAMinus() {
        //.....................01234
        final String string = "- 1 ";
        final var charSource = Sources.stringSource(string);
        charSource.next();
        try {
            charSource.findEndOfNumber();
            assertTrue(false);
        } catch (Exception ex) {
        }
    }

}
