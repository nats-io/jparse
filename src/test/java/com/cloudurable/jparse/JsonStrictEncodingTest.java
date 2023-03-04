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

import com.cloudurable.jparse.parser.JsonIndexOverlayParser;
import com.cloudurable.jparse.token.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonStrictEncodingTest {

    public JsonIndexOverlayParser jsonParser() {
        return Json.builder().setStrict(true).build();
    }


    @Test
    void badEncoding1() {
        final JsonIndexOverlayParser parser = jsonParser();
        //...................012345678
        final String json = "['`x00']";

        try {
            final List<Token> tokens = parser.scan(Json.niceJson(json));
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void badEncoding2() {
        final JsonIndexOverlayParser parser = jsonParser();
        //...................012345678
        final String json = "['`uqqqq']";

        try {
            final List<Token> tokens = parser.scan(Json.niceJson(json));
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void badEncoding3() {
        final JsonIndexOverlayParser parser = jsonParser();
        //...................012345678
        final String json = "[`uD834`uDd']";

        try {
            final List<Token> tokens = parser.scan(Json.niceJson(json));
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void badEncoding4() {
        final JsonIndexOverlayParser parser = jsonParser();
        //...................012345678
        final String json = "['`�']";

        try {
            final List<Token> tokens = parser.scan(Json.niceJson(json));
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void badEncoding5() {
        final JsonIndexOverlayParser parser = jsonParser();
        //...................012345678
        final String json = "'`UA66D'";

        try {
            final List<Token> tokens = parser.scan(Json.niceJson(json));
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void badEncoding6() {
        final JsonIndexOverlayParser parser = jsonParser();
        //...................012345678
        final String json = "'`uD800`uD800`x'";

        try {
            final List<Token> tokens = parser.scan(Json.niceJson(json));
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void badEncoding7() {
        final JsonIndexOverlayParser parser = jsonParser();
        //...................012345678
        final String json = "'`uD800`u'";

        try {
            final List<Token> tokens = parser.scan(Json.niceJson(json));
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void badEncoding8() {
        final JsonIndexOverlayParser parser = jsonParser();
        //...................01234567890
        final String json = "'`uD800`u1'";

        try {
            final List<Token> tokens = parser.scan(Json.niceJson(json));
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void badEncoding9() {
        final JsonIndexOverlayParser parser = jsonParser();
        //...................01234567890
        final String json = "'`uD800`u1x'";

        try {
            final List<Token> tokens = parser.scan(Json.niceJson(json));
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void badEncoding10() {
        final JsonIndexOverlayParser parser = jsonParser();
        //...................01234567890
        final String json = "'`u00A'";

        try {
            final List<Token> tokens = parser.scan(Json.niceJson(json));
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void badEncoding11() {
        final JsonIndexOverlayParser parser = jsonParser();
        //...................01234567890
        final String json = "'`u\uD83C\uDF00'";

        try {
            final List<Token> tokens = parser.scan(Json.niceJson(json));
            assertTrue(false);
        } catch (Exception ex) {

        }
    }

    @Test
    void badEncoding12() {
        final JsonIndexOverlayParser parser = jsonParser();
        //...................012345678
        final String json = "['`u�']";

        try {
            final List<Token> tokens = parser.scan(Json.niceJson(json));
            assertTrue(false);
        } catch (Exception ex) {

        }
    }
}
