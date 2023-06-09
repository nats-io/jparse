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

import io.nats.jparse.node.*;
import io.nats.jparse.parser.JsonParser;
import io.nats.jparse.source.Sources;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EqualityTest {

    public JsonParser jsonParser() {
        return Json.builder().setStrict(true).build();
    }

    @Test
    void testRoot() {
        final JsonParser parser = jsonParser();
        final String json1 = "'abc'";
        final String json2 = "'abc'";

        final RootNode v1 = getJsonRoot(parser, json1);
        final RootNode v2 = getJsonRoot(parser, json2);
        doAssert(v1, v2);
    }

    @Test
    void testSimpleString() {
        final JsonParser parser = jsonParser();
        final String json1 = "'abc'";
        final String json2 = "'abc'";

        final StringNode v1 = getJsonRoot(parser, json1).getStringNode();
        final StringNode v2 = getJsonRoot(parser, json2).getStringNode();
        doAssert(v1, v2);

        final String s1 = getJsonRoot(parser, json1).getString();
        final String s2 = getJsonRoot(parser, json2).getString();
        assertEquals(s1, s2);
    }

    @Test
    void testSimpleInt() {
        final JsonParser parser = jsonParser();
        final String json1 = "1";
        final String json2 = "1";
        NumberNode v1 = getJsonRoot(parser, json1).getNumberNode();
        NumberNode v2 = getJsonRoot(parser, json2).getNumberNode();
        doAssert(v1, v2);
    }


    @Test
    void testSimpleArray() {
        final JsonParser parser = jsonParser();
        final String json1 = "[1,2,3,true,false]";
        final String json2 = "[1,2,3,true,false]";
        ArrayNode v1 = getJsonRoot(parser, json1).getArrayNode();
        ArrayNode v2 = getJsonRoot(parser, json2).getArrayNode();
        doAssert(v1, v2);
    }

    @Test
    void testSimpleObject() {
        final JsonParser parser = jsonParser();
        final String json1 = "{'1':2}";
        final String json2 = "{'1':2}";
        ObjectNode v1 = getJsonRoot(parser, json1).getObjectNode();
        ObjectNode v2 = getJsonRoot(parser, json2).getObjectNode();
        doAssert(v1, v2);
    }

    @Test
    void testSimpleObject2() {
        final JsonParser parser = jsonParser();
        final String json1 = "{'1':2,'2':1}";
        final String json2 = "{'2':1,'1':2}";
        ObjectNode v1 = getJsonRoot(parser, json1).getObjectNode();
        ObjectNode v2 = getJsonRoot(parser, json2).getObjectNode();
        doAssert(v1, v2);
    }

    private RootNode getJsonRoot(JsonParser parser, String json1) {
        return parser.parse(Sources.stringSource(json1.replace("'", "\"")));
    }

    private void doAssert(Node v1, Node v2) {
        v1.charSource();
        assertEquals(v1, v2);
        assertEquals(v1.rootElementToken().type, v2.rootElementToken().type);
        assertEquals(v1.type(), v2.type());
        assertEquals(v1.hashCode(), v2.hashCode());
        assertEquals(v1.charAt(0), v2.charAt(0));
        assertEquals(v1.length(), v2.length());
        assertEquals(v1.tokens().size(), v2.tokens().size());

        if (v1.type() != NodeType.OBJECT)
            assertEquals(v1.subSequence(0, v1.length()), v2.subSequence(0, v2.length()));
    }

}
