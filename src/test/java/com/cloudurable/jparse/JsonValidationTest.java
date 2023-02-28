package com.cloudurable.jparse;


import com.cloudurable.jparse.parser.IndexOverlayParser;
import com.cloudurable.jparse.parser.JsonParser;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.source.Sources;
import com.cloudurable.jparse.token.Token;
import com.cloudurable.jparse.token.TokenTypes;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonValidationTest {


    @Test
    void testSimpleString2() {
        final IndexOverlayParser parser = new JsonParser();
        //...................01234567890123456789
        final String json = "'hi'";
        final CharSource source = Sources.stringSource(Json.niceJson(json));
        final List<Token> tokens = parser.scan(source);
        final var token = tokens.get(0);
        assertEquals(TokenTypes.STRING_TOKEN, token.type);
        assertEquals(1, token.startIndex);
        assertEquals(3, token.endIndex);
        assertEquals(4, source.getIndex());
    }

    @Test
    void testBadChar() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "@";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testEmptyArray() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "[]";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));

        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }

    }

    @Test
    void objectSimpleWithEmptyArrayValue() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "{'a':[]}";
        try {
            final List<Token> tokens = parser.scan(Json.niceJson(json));

        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);

        }

    }

    //


    @Test
    void testBadCharInNumber() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "123@";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }



    @Test
    void testBadCharInFloat() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "123.1@";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }


    @Test
    void testEscapeInString() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "['a\u0000a']";
        try {
            final List<Token> tokens = parser.scan(Json.niceJson(json));
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void allowedEscapes() {
        final IndexOverlayParser parser = new JsonParser();
        final String json =   "['```/`'`b`f`n`r`t``']";
        final var root = parser.parse(Json.niceJson(json));
        assertEquals("\\/\"\b\f\n\r\t\\", root.asArray().get(0).asScalar().toString());
    }

    @Test
    void testTooManySignOperators() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "123.1e-+1";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedExponentChar() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "123.1e-@";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedList() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "[1,@]";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedMapValue() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "{1:@}";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    ////TODO FIXME
    //@Test
    void n_string_invalid_utf_8_in_escape_json() {

        final File file =  new File("./src/test/resources/validation/n_string_invalid-utf-8-in-escape.json");
        final IndexOverlayParser parser = new JsonParser();


        try {
             parser.parse(Sources.fileSource(file)).getArrayNode().getStringNode(0).toEncodedString();
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedKey() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "{@:1}";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }


    @Test
    void testUnexpectedEndKeyKey() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "{:1}";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedEndOfMapValue() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "{1:}";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }


    @Test
    void stringNotClosed() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "\"hello cruel world'";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testParseFloatWithExponentMarkerInList() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "[123.1e-9]";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));

        assertEquals(TokenTypes.ARRAY_TOKEN, tokens.get(0).type);
        assertEquals(TokenTypes.FLOAT_TOKEN, tokens.get(1).type);

        assertEquals("123.1e-9", tokens.get(1).asString(json));

    }
}
