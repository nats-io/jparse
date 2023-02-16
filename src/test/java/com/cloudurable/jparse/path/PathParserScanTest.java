package com.cloudurable.jparse.path;

import com.cloudurable.jparse.token.Token;
import com.cloudurable.jparse.token.TokenType;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PathParserScanTest {

    @Test
    public void testScanPath() {

        final var pathParser = new PathParser();

        //....................0123456
        final var testPath = "abc.def";

        final var tokens = pathParser.scan(testPath);

        final var token1 = tokens.get(0);
        final var token2 = tokens.get(1);

        assertEquals(TokenType.PATH_KEY, token1.type());
        assertEquals(TokenType.PATH_KEY, token2.type());


        assertEquals("abc", token1.asString(testPath));
        assertEquals("def", token2.asString(testPath));

    }


    @Test
    public void testScanIndex() {

        final var pathParser = new PathParser();

        //....................0123456
        final var testPath = "abc[1]";

        final var tokens = pathParser.scan(testPath);

        final var token1 = tokens.get(0);
        final var token2 = tokens.get(1);

        assertEquals(TokenType.PATH_KEY, token1.type());
        assertEquals(TokenType.PATH_INDEX, token2.type());


        assertEquals("abc", token1.asString(testPath));
        assertEquals("1", token2.asString(testPath));

    }

    @Test
    public void testScanBracketHasKey() {

        final var pathParser = new PathParser();

        //....................0123456
        final var testPath = "abc['def']";

        final var tokens = pathParser.scan(testPath);

        final var token1 = tokens.get(0);
        final var token2 = tokens.get(1);

        assertEquals(TokenType.PATH_KEY, token1.type());
        assertEquals(TokenType.PATH_KEY, token2.type());


        assertEquals("abc", token1.asString(testPath));
        assertEquals("def", token2.asString(testPath));

    }


    @Test
    public void testScanIndexRoot() {

        final var pathParser = new PathParser();
        //....................0123456
        final var testPath = "[2]";
        final var tokens = pathParser.scan(testPath);
        final var token1 = tokens.get(0);
        assertEquals(TokenType.PATH_INDEX, token1.type());
        assertEquals("2", token1.asString(testPath));
    }

    @Test
    public void testScanBracketHasKeyRoot() {

        final var pathParser = new PathParser();
        //....................0123456
        final var testPath = "['abc']";
        final var tokens = pathParser.scan(testPath);
        final var token1 = tokens.get(0);

        assertEquals(TokenType.PATH_KEY, token1.type());
        assertEquals("abc", token1.asString(testPath));


    }


    @Test
    void webXMLBug() {

        final var pathParser = new PathParser();

        final String testPath = "['web-app'].servlet[0]['init-param'].useJSP";
        final var tokens = pathParser.scan(testPath);
        tokens.forEach(new Consumer<Token>() {
            @Override
            public void accept(Token token) {
                System.out.printf("%s %s \n", token.type(), token.asString(testPath));
            }
        });

    }


}
