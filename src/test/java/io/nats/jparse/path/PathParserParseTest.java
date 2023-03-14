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

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PathParserParseTest {

    @Test
    public void testScanPath() {

        final PathParser pathParser = new PathParser();

        //....................0123456
        final String testPath = "abc.def";

        final PathNode pathNode = pathParser.parse(testPath).getPathNode();
        final PathElement pathElement1 = pathNode.get(0);
        final PathElement pathElement2 = pathNode.get(1);

        assertTrue(pathElement1.isKey());
        assertTrue(pathElement2.isKey());
        assertEquals("abc", pathElement1.asKey().toString());
        assertEquals("def", pathElement2.asKey().toString());


    }


    @Test
    public void testScanIndex() {

        final PathParser pathParser = new PathParser();

        //....................0123456
        final String testPath = "abc[1]";

        final PathNode pathNode = pathParser.parse(testPath).getPathNode();
        final PathElement pathElement1 = pathNode.get(0);
        final PathElement pathElement2 = pathNode.get(1);

        assertTrue(pathElement1.isKey());
        assertTrue(pathElement2.isIndex());
        assertEquals("abc", pathElement1.asKey().toString());
        assertEquals(1, pathElement2.asIndex().intValue());


        final Iterator<PathElement> iterator = pathNode.iterator();

        while (iterator.hasNext()) {
            PathElement element = iterator.next();


        }

    }

    @Test
    public void testScanBracketHasKey() {

        final PathParser pathParser = new PathParser();

        //....................0123456
        final String testPath = "abc['def']";

        final PathNode pathNode = pathParser.parse(testPath).getPathNode();
        final PathElement pathElement1 = pathNode.get(0);
        final PathElement pathElement2 = pathNode.get(1);

        assertTrue(pathElement1.isKey());
        assertTrue(pathElement2.isKey());
        assertEquals("abc", pathElement1.asKey().toString());
        assertEquals("def", pathElement2.asKey().toString());

        final Iterator<PathElement> iterator = pathNode.iterator();

        while (iterator.hasNext()) {
            PathElement element = iterator.next();

            assertTrue(element.isKey());

            System.out.println(element);

        }

    }


    @Test
    public void testScanIndexRoot() {

        final PathParser pathParser = new PathParser();
        //....................0123456
        final String testPath = "[2]";

        final PathNode pathNode = pathParser.parse(testPath).getPathNode();
        final PathElement pathElement1 = pathNode.get(0);

        assertTrue(pathElement1.isIndex());
        assertEquals(2, pathElement1.asIndex().intValue());

        final Iterator<PathElement> iterator = pathNode.iterator();


        while (iterator.hasNext()) {
            PathElement element = iterator.next();

            assertTrue(element.isIndex());

            System.out.println(element);

        }

    }

    @Test
    public void testScanBracketHasKeyRoot() {

        final PathParser pathParser = new PathParser();
        //....................0123456
        final String testPath = "['abc']";

        final PathNode pathNode = pathParser.parse(testPath).getPathNode();
        final PathElement pathElement1 = pathNode.get(0);

        assertTrue(pathElement1.isKey());
        assertEquals("abc", pathElement1.asKey().toString());

        final Iterator<PathElement> iterator = pathNode.iterator();


        while (iterator.hasNext()) {
            PathElement element = iterator.next();

            assertTrue(element.isKey());

            System.out.println(element);

        }

    }

    @Test
    public void testScanBracketHasKeyRootWithWhiteSpace() {

        final PathParser pathParser = new PathParser();
        //....................0123456
        final String testPath = "['ab \t\nc']";

        final PathNode pathNode = pathParser.parse(testPath).getPathNode();
        final PathElement pathElement1 = pathNode.get(0);

        assertTrue(pathElement1.isKey());
        assertEquals("ab \t\nc", pathElement1.asKey().toString());

        final Iterator<PathElement> iterator = pathNode.iterator();


        while (iterator.hasNext()) {
            PathElement element = iterator.next();

            assertTrue(element.isKey());

            System.out.println(element);

        }

    }

    @Test
    public void testScanBracketHasKeyRootWithDash() {

        final PathParser pathParser = new PathParser();
        //....................0123456
        final String testPath = "['abc-def']";

        final PathNode pathNode = pathParser.parse(testPath).getPathNode();
        final PathElement pathElement1 = pathNode.get(0);

        assertTrue(pathElement1.isKey());
        assertEquals("abc-def", pathElement1.asKey().toString());

        final Iterator<PathElement> iterator = pathNode.iterator();


        while (iterator.hasNext()) {
            PathElement element = iterator.next();

            assertTrue(element.isKey());

            System.out.println(element);

        }

    }

    @Test
    public void testScanBracketHasKeyRootWithWildStuff() {

        final PathParser pathParser = new PathParser();
        //....................0123456
        final String testPath = "['abc-def@^&#@!_)(\n\r\t']";

        final PathNode pathNode = pathParser.parse(testPath).getPathNode();
        final PathElement pathElement1 = pathNode.get(0);

        assertTrue(pathElement1.isKey());
        assertEquals("abc-def@^&#@!_)(\n\r\t", pathElement1.asKey().toString());

        final Iterator<PathElement> iterator = pathNode.iterator();


        while (iterator.hasNext()) {
            PathElement element = iterator.next();

            assertTrue(element.isKey());

            System.out.println(element);

        }

    }

}
