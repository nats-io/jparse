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

import io.nats.jparse.node.Node;
import io.nats.jparse.node.ObjectNode;
import io.nats.jparse.node.RootNode;
import io.nats.jparse.parser.JsonParser;
import io.nats.jparse.source.Sources;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestKeyLookUp {


    @Test
    void testStringKey() {
        final JsonParser parser = Json.builder().build();
        final String json1 = "'1'";
        final String json2 = "{'1':{'2':1}}";
        final Node key = getJsonRoot(parser, json1).getNode();
        final ObjectNode map = getJsonRoot(parser, json2).getObjectNode();
        final Optional<Node> value = map.getNode(key);
        assertTrue(value.isPresent());
        final Node innerMap = value.get();
        assertTrue(innerMap instanceof ObjectNode);
        final ObjectNode innerObject = (ObjectNode) innerMap;
        final long v = innerObject.getLong("2");
        assertEquals(1, v);
    }


    private RootNode getJsonRoot(JsonParser parser, String json1) {
        return parser.parse(Sources.stringSource(json1.replace("'", "\"")));
    }
}
