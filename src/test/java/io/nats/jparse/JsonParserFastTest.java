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

import io.nats.jparse.node.ArrayNode;
import io.nats.jparse.node.RootNode;
import io.nats.jparse.parser.JsonParser;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.nats.jparse.node.JsonTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonParserFastTest extends JsonParserTest {

    @Override
    public JsonParser jsonParser() {
        return Json.builder().setStrict(false).build();
    }

    @Test
    public void testComplexMap2() {
        //................012345678901234567890123
        final String json = "{'1':2,'2':7,'3':[1,2,3]}";
        final RootNode root = jsonParser().parse(Json.niceJson(json));

        showTokens(root.tokens());

        final Map<String, Object> jsonObject = root.getMap();
        assertEquals(2, asInt(jsonObject, "1"));
        assertEquals(7, asInt(jsonObject, "2"));

        ArrayNode arrayNode = root.asObject().getArrayNode("3");
        showTokens(arrayNode.tokens());

        assertEquals(1L,arrayNode.getNodeAt(0).asScalar().longValue());

        //assertEquals(List.of(1L, 2L, 3L), asArray(jsonObject, "3").stream().map(n->n.asScalar().longValue()).collect(Collectors.toList()));

    }
}
