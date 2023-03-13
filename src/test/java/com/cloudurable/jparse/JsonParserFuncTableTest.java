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

import com.cloudurable.jparse.node.ArrayNode;
import com.cloudurable.jparse.node.RootNode;
import com.cloudurable.jparse.parser.functable.JsonFuncParser;
import com.cloudurable.jparse.parser.JsonIndexOverlayParser;
import org.junit.jupiter.api.Test;

import static com.cloudurable.jparse.node.JsonTestUtils.asInt;
import static com.cloudurable.jparse.node.JsonTestUtils.showTokens;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonParserFuncTableTest extends JsonParserTest {

    @Override
    public  JsonIndexOverlayParser jsonParser() {
        final var parser = Json.builder().setAllowComments(true).build();

        System.out.println("             " + parser.getClass().getName());
        return parser;
    }



    @Test
    public void testDoubleArrayWithComment() {
        //................012345678901234567890123
        final var json = "[1, 1.1, 1.2, 1.3, " +
                "\n#hi noah \n1e+9, 1e9, 1e-9]";
        final double[] array = {1, 1.1, 1.2, 1.3, 1e+9, 1e9, 1e-9};
        double[] readDoubles = toArrayNode(niceJson(json)).getDoubleArray();
        assertArrayEquals(array, readDoubles);
    }

    @Test
    public void testDoubleArrayWithSlashSlash() {
        //................012345678901234567890123
        final var json = "[1, 1.1, 1.2, 1.3, " +
                "\n//hi noah \n1e+9, 1e9, 1e-9]";
        final double[] array = {1, 1.1, 1.2, 1.3, 1e+9, 1e9, 1e-9};
        double[] readDoubles = toArrayNode(niceJson(json)).getDoubleArray();
        assertArrayEquals(array, readDoubles);
    }

    @Test
    public void testDoubleArrayWithSlashStar() {
        //................012345678901234567890123
        final var json = "[1, 1.1, 1.2, 1.3, " +
                "\n/*hi noah \n\n\n*/1e+9, 1e9, 1e-9]";
        final double[] array = {1, 1.1, 1.2, 1.3, 1e+9, 1e9, 1e-9};
        double[] readDoubles = toArrayNode(niceJson(json)).getDoubleArray();
        assertArrayEquals(array, readDoubles);
    }

    @Test
    public void testComplexMap2() {
        //................012345678901234567890123
        final var json = "{'1':2,'2':7,'3':[1,2,3]}";
        final RootNode root = jsonParser().parse(Json.niceJson(json));

        showTokens(root.tokens());

        final var jsonObject = root.getMap();
        assertEquals(2, asInt(jsonObject, "1"));
        assertEquals(7, asInt(jsonObject, "2"));

        ArrayNode arrayNode = root.asObject().getArrayNode("3");
        showTokens(arrayNode.tokens());

        assertEquals(1L,arrayNode.getNodeAt(0).asScalar().longValue());

        //assertEquals(List.of(1L, 2L, 3L), asArray(jsonObject, "3").stream().map(n->n.asScalar().longValue()).collect(Collectors.toList()));

    }
}
