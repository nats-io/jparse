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
package io.nats.jparse;


import io.nats.jparse.node.RootNode;
import io.nats.jparse.parser.JsonParser;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.source.Sources;
import io.nats.jparse.token.Token;
import io.nats.jparse.token.TokenTypes;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonValidationTest {

    public JsonParser jsonParser() {
        return Json.builder().setStrict(true).build();
    }

    @Test
    void testSimpleString2() {
        final JsonParser parser = jsonParser();
        //...................01234567890123456789
        final String json = "'hi'";
        final CharSource source = Sources.stringSource(Json.niceJson(json));
        final List<Token> tokens = parser.scan(source);
        final Token token = tokens.get(0);
        assertEquals(TokenTypes.STRING_TOKEN, token.type);
        assertEquals(1, token.startIndex);
        assertEquals(3, token.endIndex);
        assertEquals(4, source.getIndex());
    }


    //



    @Test
    void testEscapeInString() {
        final JsonParser parser = jsonParser();

        final String json = "['a\u0000a']";
        try {
            final List<Token> tokens = parser.scan(Json.niceJson(json));
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void allowedEscapes() {
        final JsonParser parser = jsonParser();
        final String json =   "['```/`'`b`f`n`r`t``']";
        final  RootNode root = parser.parse(Json.niceJson(json));
        assertEquals("\\/\"\b\f\n\r\t\\", root.asArray().get(0).asScalar().toString());
    }

    @Test
    void testUnexpectedExponentChar() {
        final JsonParser parser = jsonParser();

        final String json = "123.1e-@";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedList() {
        final JsonParser parser = jsonParser();

        final String json = "[1,@]";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedMapValue() {
        final JsonParser parser = jsonParser();

        final String json = "{1:@}";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }


    @Test
    void test_n_array_missing_value() {
        final JsonParser parser = jsonParser();
        //...................0123456789012345678901234
        final String json = "[   , \"\"]";
        try {
            final RootNode jsonRoot = parser.parse(Json.niceJson(json));
            System.out.println(jsonRoot.tokens());
            assertTrue(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Test
    void n_string_invalid_utf_8_in_escape_json() {

        final File file =  new File("./src/test/resources/validation/n_string_invalid-utf-8-in-escape.json");
        final JsonParser parser = jsonParser();


        try {
             parser.parse(Sources.fileSource(file)).getArrayNode().getStringNode(0).toEncodedString();
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedKey() {
        final JsonParser parser = jsonParser();

        final String json = "{@:1}";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }


    @Test
    void testUnexpectedEndKeyKey() {
        final JsonParser parser = jsonParser();

        final String json = "{:1}";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedEndOfMapValue() {
        final JsonParser parser = jsonParser();

        final String json = "{1:}";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }


    @Test
    void stringNotClosed() {
        final JsonParser parser = jsonParser();

        final String json = "\"hello cruel world'";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testParseFloatWithExponentMarkerInList() {
        final JsonParser parser = jsonParser();

        final String json = "[123.1e-9]";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));

        assertEquals(TokenTypes.ARRAY_TOKEN, tokens.get(0).type);
        assertEquals(TokenTypes.FLOAT_TOKEN, tokens.get(1).type);

        assertEquals("123.1e-9", tokens.get(1).asString(json));

    }
}
