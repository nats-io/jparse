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

import io.nats.jparse.token.Token;
import io.nats.jparse.token.TokenTypes;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PathParserScanTest {

    @Test
    public void testScanPath() {

        final PathParser pathParser = new PathParser();

        //....................0123456
        final String testPath = "abc.def";

        final List<Token> tokens = pathParser.scan(testPath);

        final Token token1 = tokens.get(0);
        final Token token2 = tokens.get(1);

        assertEquals(TokenTypes.PATH_KEY_TOKEN, token1.type);
        assertEquals(TokenTypes.PATH_KEY_TOKEN, token2.type);


        assertEquals("abc", token1.asString(testPath));
        assertEquals("def", token2.asString(testPath));

    }


    @Test
    public void testScanIndex() {

        final PathParser pathParser = new PathParser();

        //....................0123456
        final String testPath = "abc[1]";

        final List<Token> tokens = pathParser.scan(testPath);

        final Token token1 = tokens.get(0);
        final Token token2 = tokens.get(1);

        assertEquals(TokenTypes.PATH_KEY_TOKEN, token1.type);
        assertEquals(TokenTypes.PATH_INDEX_TOKEN, token2.type);


        assertEquals("abc", token1.asString(testPath));
        assertEquals("1", token2.asString(testPath));

    }

    @Test
    public void testScanBracketHasKey() {

        final PathParser pathParser = new PathParser();

        //....................0123456
        final String testPath = "abc['def']";

        final List<Token> tokens = pathParser.scan(testPath);

        final Token token1 = tokens.get(0);
        final Token token2 = tokens.get(1);

        assertEquals(TokenTypes.PATH_KEY_TOKEN, token1.type);
        assertEquals(TokenTypes.PATH_KEY_TOKEN, token2.type);


        assertEquals("abc", token1.asString(testPath));
        assertEquals("def", token2.asString(testPath));

    }


    @Test
    public void testScanIndexRoot() {

        final PathParser pathParser = new PathParser();
        //....................0123456
        final String testPath = "[2]";
        final List<Token> tokens = pathParser.scan(testPath);
        final Token token1 = tokens.get(0);
        assertEquals(TokenTypes.PATH_INDEX_TOKEN, token1.type);
        assertEquals("2", token1.asString(testPath));
    }

    @Test
    public void testScanBracketHasKeyRoot() {

        final PathParser pathParser = new PathParser();
        //....................0123456
        final String testPath = "['abc']";
        final List<Token> tokens = pathParser.scan(testPath);
        final Token token1 = tokens.get(0);

        assertEquals(TokenTypes.PATH_KEY_TOKEN, token1.type);
        assertEquals("abc", token1.asString(testPath));


    }


    @Test
    void webXMLBug() {

        final PathParser pathParser = new PathParser();

        final String testPath = "['web-app'].servlet[0]['init-param'].useJSP";
        final List<Token> tokens = pathParser.scan(testPath);
        tokens.forEach(token -> System.out.printf("%s %s \n", token.type, token.asString(testPath)));

    }


}
