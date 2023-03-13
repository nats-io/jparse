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
package com.cloudurable.jparse.node.support;

import com.cloudurable.jparse.Json;
import com.cloudurable.jparse.parser.JsonIndexOverlayParser;
import com.cloudurable.jparse.node.NodeType;
import com.cloudurable.jparse.node.RootNode;
import com.cloudurable.jparse.source.Sources;
import com.cloudurable.jparse.token.Token;
import com.cloudurable.jparse.token.TokenTypes;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.cloudurable.jparse.Json.niceJson;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ElementUtilTest {

    public JsonIndexOverlayParser jsonParser() {
        return Json.builder().setStrict(true).build();
    }


    @Test
    void getChildrenTokensFromArraySimple() {

        TokenList list = new TokenList();

        list.add(new Token(0, 20, TokenTypes.ARRAY_TOKEN));
        list.add(new Token(2, 6, TokenTypes.STRING_TOKEN));
        list.add(new Token(8, 20, TokenTypes.STRING_TOKEN));


        List<List<Token>> childrenTokens = NodeUtils.getChildrenTokens((TokenSubList) list.subList(0, list.size()));

        List<Token> array1 = childrenTokens.get(0);
        assertEquals(TokenTypes.STRING_TOKEN, array1.get(0).type);
        assertEquals(2, array1.get(0).startIndex);
        assertEquals(6, array1.get(0).endIndex);

        List<Token> array2 = childrenTokens.get(1);
        assertEquals(TokenTypes.STRING_TOKEN, array2.get(0).type);
        assertEquals(8, array2.get(0).startIndex);
        assertEquals(20, array2.get(0).endIndex);


    }


    @Test
    void getChildrenTokensFromArraySimplePart2() {

        TokenList list = new TokenList();

        list.add(new Token(0, 20, TokenTypes.ARRAY_TOKEN));
        list.add(new Token(2, 6, TokenTypes.STRING_TOKEN));
        list.add(new Token(8, 20, TokenTypes.ARRAY_TOKEN));
        list.add(new Token(9, 13, TokenTypes.STRING_TOKEN));
        list.add(new Token(15, 17, TokenTypes.STRING_TOKEN));


        List<List<Token>> childrenTokens = NodeUtils.getChildrenTokens((TokenSubList) list.subList(0, list.size()));

        assertEquals(2, childrenTokens.size());
        List<Token> array1 = childrenTokens.get(0);
        assertEquals(TokenTypes.STRING_TOKEN, array1.get(0).type);
        assertEquals(2, array1.get(0).startIndex);
        assertEquals(6, array1.get(0).endIndex);

        List<Token> array2 = childrenTokens.get(1);
        assertEquals(TokenTypes.ARRAY_TOKEN, array2.get(0).type);
        assertEquals(8, array2.get(0).startIndex);
        assertEquals(20, array2.get(0).endIndex);

        assertEquals(TokenTypes.STRING_TOKEN, array2.get(1).type);
        assertEquals(9, array2.get(1).startIndex);
        assertEquals(13, array2.get(1).endIndex);

        assertEquals(TokenTypes.STRING_TOKEN, array2.get(2).type);
        assertEquals(15, array2.get(2).startIndex);
        assertEquals(17, array2.get(2).endIndex);


    }

    @Test
    void getChildrenTokensFromArray() {

        TokenList list = new TokenList();

        list.add(new Token(0, 20, TokenTypes.ARRAY_TOKEN));

        list.add(new Token(1, 10, TokenTypes.ARRAY_TOKEN));
        list.add(new Token(2, 6, TokenTypes.STRING_TOKEN));

        list.add(new Token(11, 21, TokenTypes.ARRAY_TOKEN));
        list.add(new Token(11, 15, TokenTypes.STRING_TOKEN));


        List<List<Token>> childrenTokens = NodeUtils.getChildrenTokens((TokenSubList) list.subList(0, list.size()));

        assertEquals(2, childrenTokens.size());

        List<Token> array1 = childrenTokens.get(0);
        assertEquals(TokenTypes.ARRAY_TOKEN, array1.get(0).type);
        assertEquals(1, array1.get(0).startIndex);
        assertEquals(10, array1.get(0).endIndex);

        assertEquals(TokenTypes.STRING_TOKEN, array1.get(1).type);
        assertEquals(2, array1.get(1).startIndex);
        assertEquals(6, array1.get(1).endIndex);


        List<Token> array2 = childrenTokens.get(1);
        assertEquals(TokenTypes.ARRAY_TOKEN, array2.get(0).type);
        assertEquals(11, array2.get(0).startIndex);
        assertEquals(21, array2.get(0).endIndex);

        assertEquals(TokenTypes.STRING_TOKEN, array2.get(1).type);
        assertEquals(11, array2.get(1).startIndex);
        assertEquals(15, array2.get(1).endIndex);
    }


    @Test
    void getChildrenTokensFromObject() {

        TokenList list = new TokenList();

        list.add(new Token(0, 20, TokenTypes.OBJECT_TOKEN));

        list.add(new Token(1, 6, TokenTypes.ATTRIBUTE_KEY_TOKEN));
        list.add(new Token(2, 6, TokenTypes.STRING_TOKEN));

        list.add(new Token(6, 10, TokenTypes.ATTRIBUTE_VALUE_TOKEN));
        list.add(new Token(7, 10, TokenTypes.STRING_TOKEN));


        List<List<Token>> childrenTokens = NodeUtils.getChildrenTokens((TokenSubList) list.subList(0, list.size()));

        List<Token> key = childrenTokens.get(0);
        assertEquals(TokenTypes.ATTRIBUTE_KEY_TOKEN, key.get(0).type);
        assertEquals(1, key.get(0).startIndex);
        assertEquals(6, key.get(0).endIndex);

        assertEquals(TokenTypes.STRING_TOKEN, key.get(1).type);
        assertEquals(2, key.get(1).startIndex);
        assertEquals(6, key.get(1).endIndex);


        List<Token> value = childrenTokens.get(1);
        assertEquals(TokenTypes.ATTRIBUTE_VALUE_TOKEN, value.get(0).type);
        assertEquals(6, value.get(0).startIndex);
        assertEquals(10, value.get(0).endIndex);

        assertEquals(TokenTypes.STRING_TOKEN, value.get(1).type);
        assertEquals(7, value.get(1).startIndex);
        assertEquals(10, value.get(1).endIndex);
    }


    @Test
    void testObject() {

        final JsonIndexOverlayParser parser = jsonParser();
        //...................0123456789
        final String json = "{'a':'b'}";
        final TokenList list = (TokenList) parser.scan(Sources.stringSource(json.replace("'", "\"")));


        List<List<Token>> childrenTokens = NodeUtils.getChildrenTokens((TokenSubList) list.subList(0, list.size()));

        validateSimpleObject(childrenTokens);

    }


    @Test
    void testObject2() {

        final JsonIndexOverlayParser parser = jsonParser();
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
        assertEquals(TokenTypes.ATTRIBUTE_KEY_TOKEN, key.get(0).type);
        assertEquals(1, key.get(0).startIndex);
        assertEquals(4, key.get(0).endIndex);

        assertEquals(TokenTypes.STRING_TOKEN, key.get(1).type);
        assertEquals(2, key.get(1).startIndex);
        assertEquals(3, key.get(1).endIndex);


        List<Token> value = childrenTokens.get(1);
        assertEquals(TokenTypes.ATTRIBUTE_VALUE_TOKEN, value.get(0).type);
        assertEquals(5, value.get(0).startIndex);
        assertEquals(8, value.get(0).endIndex);

        assertEquals(TokenTypes.STRING_TOKEN, value.get(1).type);
        assertEquals(6, value.get(1).startIndex);
        assertEquals(7, value.get(1).endIndex);

    }

}
