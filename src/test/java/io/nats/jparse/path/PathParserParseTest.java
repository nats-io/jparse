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
package io.nats.jparse.path;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PathParserParseTest {

    @Test
    public void testScanPath() {

        final var pathParser = new PathParser();

        //....................0123456
        final var testPath = "abc.def";

        final var pathNode = pathParser.parse(testPath).getPathNode();
        final var pathElement1 = pathNode.get(0);
        final var pathElement2 = pathNode.get(1);

        assertTrue(pathElement1.isKey());
        assertTrue(pathElement2.isKey());
        assertEquals("abc", pathElement1.asKey().toString());
        assertEquals("def", pathElement2.asKey().toString());


    }


    @Test
    public void testScanIndex() {

        final var pathParser = new PathParser();

        //....................0123456
        final var testPath = "abc[1]";

        final var pathNode = pathParser.parse(testPath).getPathNode();
        final var pathElement1 = pathNode.get(0);
        final var pathElement2 = pathNode.get(1);

        assertTrue(pathElement1.isKey());
        assertTrue(pathElement2.isIndex());
        assertEquals("abc", pathElement1.asKey().toString());
        assertEquals(1, pathElement2.asIndex().intValue());


        final var iterator = pathNode.iterator();

        while (iterator.hasNext()) {
            PathElement element = iterator.next();


            System.out.println(element);

        }

    }

    @Test
    public void testScanBracketHasKey() {

        final var pathParser = new PathParser();

        //....................0123456
        final var testPath = "abc['def']";

        final var pathNode = pathParser.parse(testPath).getPathNode();
        final var pathElement1 = pathNode.get(0);
        final var pathElement2 = pathNode.get(1);

        assertTrue(pathElement1.isKey());
        assertTrue(pathElement2.isKey());
        assertEquals("abc", pathElement1.asKey().toString());
        assertEquals("def", pathElement2.asKey().toString());

        final var iterator = pathNode.iterator();

        while (iterator.hasNext()) {
            PathElement element = iterator.next();

            assertTrue(element.isKey());

            System.out.println(element);

        }

    }


    @Test
    public void testScanIndexRoot() {

        final var pathParser = new PathParser();
        //....................0123456
        final var testPath = "[2]";

        final var pathNode = pathParser.parse(testPath).getPathNode();
        final var pathElement1 = pathNode.get(0);

        assertTrue(pathElement1.isIndex());
        assertEquals(2, pathElement1.asIndex().intValue());

        final var iterator = pathNode.iterator();


        while (iterator.hasNext()) {
            PathElement element = iterator.next();

            assertTrue(element.isIndex());

            System.out.println(element);

        }

    }

    @Test
    public void testScanBracketHasKeyRoot() {

        final var pathParser = new PathParser();
        //....................0123456
        final var testPath = "['abc']";

        final var pathNode = pathParser.parse(testPath).getPathNode();
        final var pathElement1 = pathNode.get(0);

        assertTrue(pathElement1.isKey());
        assertEquals("abc", pathElement1.asKey().toString());

        final var iterator = pathNode.iterator();


        while (iterator.hasNext()) {
            PathElement element = iterator.next();

            assertTrue(element.isKey());

            System.out.println(element);

        }

    }

    @Test
    public void testScanBracketHasKeyRootWithWhiteSpace() {

        final var pathParser = new PathParser();
        //....................0123456
        final var testPath = "['ab \t\nc']";

        final var pathNode = pathParser.parse(testPath).getPathNode();
        final var pathElement1 = pathNode.get(0);

        assertTrue(pathElement1.isKey());
        assertEquals("ab \t\nc", pathElement1.asKey().toString());

        final var iterator = pathNode.iterator();


        while (iterator.hasNext()) {
            PathElement element = iterator.next();

            assertTrue(element.isKey());

            System.out.println(element);

        }

    }

    @Test
    public void testScanBracketHasKeyRootWithDash() {

        final var pathParser = new PathParser();
        //....................0123456
        final var testPath = "['abc-def']";

        final var pathNode = pathParser.parse(testPath).getPathNode();
        final var pathElement1 = pathNode.get(0);

        assertTrue(pathElement1.isKey());
        assertEquals("abc-def", pathElement1.asKey().toString());

        final var iterator = pathNode.iterator();


        while (iterator.hasNext()) {
            PathElement element = iterator.next();

            assertTrue(element.isKey());

            System.out.println(element);

        }

    }

    @Test
    public void testScanBracketHasKeyRootWithWildStuff() {

        final var pathParser = new PathParser();
        //....................0123456
        final var testPath = "['abc-def@^&#@!_)(\n\r\t']";

        final var pathNode = pathParser.parse(testPath).getPathNode();
        final var pathElement1 = pathNode.get(0);

        assertTrue(pathElement1.isKey());
        assertEquals("abc-def@^&#@!_)(\n\r\t", pathElement1.asKey().toString());

        final var iterator = pathNode.iterator();


        while (iterator.hasNext()) {
            PathElement element = iterator.next();

            assertTrue(element.isKey());

            System.out.println(element);

        }

    }

}
