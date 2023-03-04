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
package com.cloudurable.jparse;

import com.cloudurable.jparse.node.RootNode;
import com.cloudurable.jparse.parser.JsonIndexOverlayParser;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.Token;
import com.cloudurable.jparse.token.TokenEventListener;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.cloudurable.jparse.Json.niceJson;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidateGarbageJsonTest {

    public JsonIndexOverlayParser jsonParser() {
        return Json.builder().setStrict(true).build();
    }

    public JsonIndexOverlayParser jsonEventParser() {
        return Json.builder().setStrict(true).setTokenEventListener(new TokenEventListener() {
            @Override
            public void start(int tokenId, int index, CharSource source) {

            }

            @Override
            public void end(int tokenId, int index, CharSource source) {

            }
        }).setStrict(true).build();
    }

    /*
     *
     PASS! ./src/test/resources/validation/n_string_with_trailing_garbage.json
     ""x
     */
    @Test
    void letterAfterCloseString() {
        final var parser = jsonParser();

        //...................0123
        final String json = "''x";
        try {
            final List<Token> tokens = parser.scan(niceJson(json));
            System.out.println(tokens);
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void letterAfterCloseStringEvent() {
        final var parser = jsonEventParser();

        //...................0123
        final String json = "''x";
        try {
            final List<Token> tokens = parser.scan(niceJson(json));
            System.out.println(tokens);
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    /*
         PASS! ./src/test/resources/validation/n_object_with_trailing_garbage.json

     */

    @Test
    void junkAfterMapEvents() {
        final var parser = jsonEventParser();

        //...................01234567890
        final String json = "{'a':'b'}#";
        try {
            final List<Token> tokens = parser.scan(niceJson(json));
            System.out.println(tokens);
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void junkAfterMap() {
        final var parser = jsonParser();

        //...................01234567890
        final String json = "{'a':'b'}#";
        try {
            final List<Token> tokens = parser.scan(niceJson(json));
            System.out.println(tokens);
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    /*
     */

    @Test
    void extraBracketAfterArrayEvents() {
        final var parser = jsonEventParser();

        //...................01234567890
        final String json = "[1]]";
        try {
            final List<Token> tokens = parser.scan(niceJson(json));
            System.out.println(tokens);
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void extraBracketAfterArray() {
        final var parser = jsonParser();

        //...................01234567890
        final String json = "[1]]";
        try {
            final List<Token> tokens = parser.scan(niceJson(json));
            System.out.println(tokens);
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    /* 1]

     */


    @Test
    void extraBracketAfterNumberEvents() {
        final var parser = jsonEventParser();

        //...................01234567890
        final String json = "1]";
        try {
            final List<Token> tokens = parser.scan(niceJson(json));
            System.out.println(tokens);
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void extraBracketAfterNumber() {
        final var parser = jsonParser();

        //...................01234567890
        final String json = "1]";
        try {
            final List<Token> tokens = parser.scan(niceJson(json));
            System.out.println(tokens);
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void trailingCommaInObject() {
        final var parser = jsonParser();

        //...................01234567890
        final String json = "{'id':0,}";
        try {
            final List<Token> tokens = parser.scan(niceJson(json));
            System.out.println(tokens);
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void trailingCommaInObjectEvents() {
        final var parser = jsonEventParser();

        //...................01234567890
        final String json = "{'id':0, }";
        try {
            final List<Token> tokens = parser.scan(niceJson(json));
            System.out.println(tokens);
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    //

    @Test
    void arrayNoCommaEvents() {
        final var parser = jsonEventParser();

        //...................01234567890
        final String json = "[1 true]";
        try {
            final List<Token> tokens = parser.scan(niceJson(json));
            System.out.println(tokens);
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void arrayNoComma() {
        final var parser = jsonParser();

        //...................01234567890
        final String json = "[1 true]";
        try {
            final List<Token> tokens = parser.scan(niceJson(json));
            System.out.println(tokens);
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void arrayTrailingCommaEvent() {
        final var parser = jsonEventParser();

        //...................01234567890
        final String json = "[1,]";
        try {
            final List<Token> tokens = parser.scan(niceJson(json));
            System.out.println(tokens);
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void arrayTrailingComma() {
        final var parser = jsonParser();

        //...................01234567890
        final String json = "[1,]";
        try {
            final List<Token> tokens = parser.scan(niceJson(json));
            System.out.println(tokens);
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void arrayTrailingCommaAfterStringEvent() {
        final var parser = jsonEventParser();

        //...................01234567890
        final String json = "['',]";
        try {
            final List<Token> tokens = parser.scan(niceJson(json));
            System.out.println(tokens);
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void arrayTrailingCommaAfterString() {
        final var parser = jsonParser();

        //...................01234567890
        final String json = "['',]";
        try {
            final List<Token> tokens = parser.scan(niceJson(json));
            System.out.println(tokens);
            assertTrue(false);
        } catch (Exception ex) {

        }
    }


    @Test
    void test_n_structure_double_array() {
        final JsonIndexOverlayParser parser = jsonParser();
        //...................0123456789012345678901234
        final String json = "[][]";
        try {
            final RootNode jsonRoot = parser.parse(niceJson(json));
            System.out.println(jsonRoot.tokens());
            assertTrue(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //[1,]
 /*




     PASS! ./src/test/resources/validation/n_object_trailing_comma.json
     {"id":0,}

     PASS! ./src/test/resources/validation/n_array_number_and_comma.json
     [1,]

     PASS! ./src/test/resources/validation/n_array_extra_comma.json
     ["",]

     PASS! ./src/test/resources/validation/n_array_1_true_without_comma.json
     [1 true]

     PASS! ./src/test/resources/validation/n_structure_close_unopened_array.json
     1]


     PASS! ./src/test/resources/validation/n_structure_array_trailing_garbage.json
     [1]x







     PASS! ./src/test/resources/validation/n_structure_array_with_extra_array_close.json
     [1]]
     */
}
