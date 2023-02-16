package com.cloudurable.jparse.node.support;

import com.cloudurable.jparse.JsonParser;
import com.cloudurable.jparse.Parser;
import com.cloudurable.jparse.node.NodeType;
import com.cloudurable.jparse.node.RootNode;
import com.cloudurable.jparse.source.Sources;
import com.cloudurable.jparse.token.Token;
import com.cloudurable.jparse.token.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.cloudurable.jparse.Json.niceJson;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ElementUtilTest {


    @Test
    void getChildrenTokensFromArraySimple() {

        TokenList list = new TokenList();

        list.add(new Token(0, 20, TokenType.ARRAY));
        list.add(new Token(2, 6, TokenType.STRING));
        list.add(new Token(8, 20, TokenType.STRING));


        List<List<Token>> childrenTokens = NodeUtils.getChildrenTokens((TokenSubList) list.subList(0, list.size()));

        List<Token> array1 = childrenTokens.get(0);
        assertEquals(TokenType.STRING, array1.get(0).type());
        assertEquals(2, array1.get(0).startIndex());
        assertEquals(6, array1.get(0).endIndex());

        List<Token> array2 = childrenTokens.get(1);
        assertEquals(TokenType.STRING, array2.get(0).type());
        assertEquals(8, array2.get(0).startIndex());
        assertEquals(20, array2.get(0).endIndex());


    }


    @Test
    void getChildrenTokensFromArraySimplePart2() {

        TokenList list = new TokenList();

        list.add(new Token(0, 20, TokenType.ARRAY));
        list.add(new Token(2, 6, TokenType.STRING));
        list.add(new Token(8, 20, TokenType.ARRAY));
        list.add(new Token(9, 13, TokenType.STRING));
        list.add(new Token(15, 17, TokenType.STRING));


        List<List<Token>> childrenTokens = NodeUtils.getChildrenTokens((TokenSubList) list.subList(0, list.size()));

        assertEquals(2, childrenTokens.size());
        List<Token> array1 = childrenTokens.get(0);
        assertEquals(TokenType.STRING, array1.get(0).type());
        assertEquals(2, array1.get(0).startIndex());
        assertEquals(6, array1.get(0).endIndex());

        List<Token> array2 = childrenTokens.get(1);
        assertEquals(TokenType.ARRAY, array2.get(0).type());
        assertEquals(8, array2.get(0).startIndex());
        assertEquals(20, array2.get(0).endIndex());

        assertEquals(TokenType.STRING, array2.get(1).type());
        assertEquals(9, array2.get(1).startIndex());
        assertEquals(13, array2.get(1).endIndex());

        assertEquals(TokenType.STRING, array2.get(2).type());
        assertEquals(15, array2.get(2).startIndex());
        assertEquals(17, array2.get(2).endIndex());


    }

    @Test
    void getChildrenTokensFromArray() {

        TokenList list = new TokenList();

        list.add(new Token(0, 20, TokenType.ARRAY));

        list.add(new Token(1, 10, TokenType.ARRAY));
        list.add(new Token(2, 6, TokenType.STRING));

        list.add(new Token(11, 21, TokenType.ARRAY));
        list.add(new Token(11, 15, TokenType.STRING));


        List<List<Token>> childrenTokens = NodeUtils.getChildrenTokens((TokenSubList) list.subList(0, list.size()));

        assertEquals(2, childrenTokens.size());

        List<Token> array1 = childrenTokens.get(0);
        assertEquals(TokenType.ARRAY, array1.get(0).type());
        assertEquals(1, array1.get(0).startIndex());
        assertEquals(10, array1.get(0).endIndex());

        assertEquals(TokenType.STRING, array1.get(1).type());
        assertEquals(2, array1.get(1).startIndex());
        assertEquals(6, array1.get(1).endIndex());


        List<Token> array2 = childrenTokens.get(1);
        assertEquals(TokenType.ARRAY, array2.get(0).type());
        assertEquals(11, array2.get(0).startIndex());
        assertEquals(21, array2.get(0).endIndex());

        assertEquals(TokenType.STRING, array2.get(1).type());
        assertEquals(11, array2.get(1).startIndex());
        assertEquals(15, array2.get(1).endIndex());
    }


    @Test
    void getChildrenTokensFromObject() {

        TokenList list = new TokenList();

        list.add(new Token(0, 20, TokenType.OBJECT));

        list.add(new Token(1, 6, TokenType.ATTRIBUTE_KEY));
        list.add(new Token(2, 6, TokenType.STRING));

        list.add(new Token(6, 10, TokenType.ATTRIBUTE_VALUE));
        list.add(new Token(7, 10, TokenType.STRING));


        List<List<Token>> childrenTokens = NodeUtils.getChildrenTokens((TokenSubList) list.subList(0, list.size()));

        List<Token> key = childrenTokens.get(0);
        assertEquals(TokenType.ATTRIBUTE_KEY, key.get(0).type());
        assertEquals(1, key.get(0).startIndex());
        assertEquals(6, key.get(0).endIndex());

        assertEquals(TokenType.STRING, key.get(1).type());
        assertEquals(2, key.get(1).startIndex());
        assertEquals(6, key.get(1).endIndex());


        List<Token> value = childrenTokens.get(1);
        assertEquals(TokenType.ATTRIBUTE_VALUE, value.get(0).type());
        assertEquals(6, value.get(0).startIndex());
        assertEquals(10, value.get(0).endIndex());

        assertEquals(TokenType.STRING, value.get(1).type());
        assertEquals(7, value.get(1).startIndex());
        assertEquals(10, value.get(1).endIndex());
    }


    @Test
    void testObject() {

        final Parser parser = new JsonParser();
        //...................0123456789
        final String json = "{'a':'b'}";
        final TokenList list = (TokenList) parser.scan(Sources.stringSource(json.replace("'", "\"")));


        List<List<Token>> childrenTokens = NodeUtils.getChildrenTokens((TokenSubList) list.subList(0, list.size()));

        validateSimpleObject(childrenTokens);

    }


    @Test
    void testObject2() {

        final Parser parser = new JsonParser();
        //...................0123456789
        final String json = "{'a':'b'}";
        RootNode parse = parser.parse(niceJson(json));

        TokenList list = (TokenList) parse.tokens();

//        List<Token> list = parser.scan(JsonTestUtils.niceJson(json));
//
        List<List<Token>> childrenTokens = NodeUtils.getChildrenTokens((TokenSubList) list.subList(0, list.size()));

        validateSimpleObject(childrenTokens);

        assertEquals(NodeType.OBJECT, parse.getNode().type());

        List<List<Token>> childrenTokens1 = NodeUtils.getChildrenTokens((TokenSubList) parse.getObjectNode().tokens());

        validateSimpleObject(childrenTokens);


        validateSimpleObject(parse.getObjectNode().childrenTokens());

    }

    public void validateSimpleObject(List<List<Token>> childrenTokens) {
        List<Token> key = childrenTokens.get(0);
        assertEquals(TokenType.ATTRIBUTE_KEY, key.get(0).type());
        assertEquals(1, key.get(0).startIndex());
        assertEquals(4, key.get(0).endIndex());

        assertEquals(TokenType.STRING, key.get(1).type());
        assertEquals(2, key.get(1).startIndex());
        assertEquals(3, key.get(1).endIndex());


        List<Token> value = childrenTokens.get(1);
        assertEquals(TokenType.ATTRIBUTE_VALUE, value.get(0).type());
        assertEquals(5, value.get(0).startIndex());
        assertEquals(8, value.get(0).endIndex());

        assertEquals(TokenType.STRING, value.get(1).type());
        assertEquals(6, value.get(1).startIndex());
        assertEquals(7, value.get(1).endIndex());

    }

}
