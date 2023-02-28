package com.cloudurable.jparse;

import com.cloudurable.jparse.node.RootNode;
import com.cloudurable.jparse.parser.IndexOverlayParser;
import com.cloudurable.jparse.parser.JsonParser;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.source.Sources;
import com.cloudurable.jparse.token.Token;
import com.cloudurable.jparse.token.TokenTypes;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.cloudurable.jparse.node.JsonTestUtils.tokens;
import static com.cloudurable.jparse.node.JsonTestUtils.validateToken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonScannerTest {

    @Test
    public void testComplexMap() {
        //................012345678901234567890123
        final var json = "{'1':2,'2':7,'abc':[1,2,3]}";
        final List<Token> tokens = tokens(json);
        //showTokens(tokens);
        validateToken(tokens.get(0), TokenTypes.OBJECT_TOKEN, 0, 27);

        validateToken(tokens.get(1), TokenTypes.ATTRIBUTE_KEY_TOKEN, 1, 4);
        validateToken(tokens.get(2), TokenTypes.STRING_TOKEN, 2, 3);
        validateToken(tokens.get(3), TokenTypes.ATTRIBUTE_VALUE_TOKEN, 5, 6);
        validateToken(tokens.get(4), TokenTypes.INT_TOKEN, 5, 6);


        validateToken(tokens.get(5), TokenTypes.ATTRIBUTE_KEY_TOKEN, 7, 10);
        validateToken(tokens.get(6), TokenTypes.STRING_TOKEN, 8, 9);
        validateToken(tokens.get(7), TokenTypes.ATTRIBUTE_VALUE_TOKEN, 11, 12);
        validateToken(tokens.get(8), TokenTypes.INT_TOKEN, 11, 12);


        validateToken(tokens.get(9), TokenTypes.ATTRIBUTE_KEY_TOKEN, 13, 18);
        validateToken(tokens.get(10), TokenTypes.STRING_TOKEN, 14, 17);


        validateToken(tokens.get(11), TokenTypes.ATTRIBUTE_VALUE_TOKEN, 19, 26);
        validateToken(tokens.get(12), TokenTypes.ARRAY_TOKEN, 19, 26);

        validateToken(tokens.get(13), TokenTypes.INT_TOKEN, 20, 21);
        validateToken(tokens.get(14), TokenTypes.INT_TOKEN, 22, 23);
        validateToken(tokens.get(15), TokenTypes.INT_TOKEN, 24, 25);

    }


    @Test
    public void testMapWithList() {
        //................012345678901234567890123
        final var json = "{'abc':[1,2,'3']}";
        final List<Token> tokens = tokens(json);
        //showTokens(tokens);
        validateToken(tokens.get(0), TokenTypes.OBJECT_TOKEN, 0, 17);

        validateToken(tokens.get(1), TokenTypes.ATTRIBUTE_KEY_TOKEN, 1, 6);
        validateToken(tokens.get(2), TokenTypes.STRING_TOKEN, 2, 5);


        validateToken(tokens.get(3), TokenTypes.ATTRIBUTE_VALUE_TOKEN, 7, 16);
        validateToken(tokens.get(4), TokenTypes.ARRAY_TOKEN, 7, 16);

        validateToken(tokens.get(5), TokenTypes.INT_TOKEN, 8, 9);
        validateToken(tokens.get(6), TokenTypes.INT_TOKEN, 10, 11);
        validateToken(tokens.get(7), TokenTypes.STRING_TOKEN, 13, 14);

    }


    @Test
    void testSimpleListWithInts() {
        final IndexOverlayParser parser = new JsonParser();
        //...................0123456789
        final String json = "[ 1 , 3 ]";

        final List<Token> tokens = parser.scan(json.replace("'", "\""));

        assertEquals(TokenTypes.ARRAY_TOKEN, tokens.get(0).type);
        assertEquals(0, tokens.get(0).startIndex);
        assertEquals(9, tokens.get(0).endIndex);


        assertEquals(TokenTypes.INT_TOKEN, tokens.get(1).type);
        assertEquals(2, tokens.get(1).startIndex);
        assertEquals(3, tokens.get(1).endIndex);


        assertEquals(TokenTypes.INT_TOKEN, tokens.get(2).type);
        assertEquals(6, tokens.get(2).startIndex);
        assertEquals(7, tokens.get(2).endIndex);

    }

    @Test
    void testSimpleListWithIntsNoSpaces() {
        final IndexOverlayParser parser = new JsonParser();
        //...................0123456
        final String json = "[1,3]";

        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));

        assertEquals(TokenTypes.ARRAY_TOKEN, tokens.get(0).type);
        assertEquals(0, tokens.get(0).startIndex);
        assertEquals(5, tokens.get(0).endIndex);


        assertEquals(TokenTypes.INT_TOKEN, tokens.get(1).type);
        assertEquals(1, tokens.get(1).startIndex);
        assertEquals(2, tokens.get(1).endIndex);


        assertEquals(TokenTypes.INT_TOKEN, tokens.get(2).type);
        assertEquals(3, tokens.get(2).startIndex);
        assertEquals(4, tokens.get(2).endIndex);

    }

    @Test
    void testSimpleList() {


        final IndexOverlayParser parser = new JsonParser();
        //0123456789
        final String json = "['h','a']";
        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));

        assertEquals(TokenTypes.ARRAY_TOKEN, tokens.get(0).type);
        assertEquals(0, tokens.get(0).startIndex);
        assertEquals(9, tokens.get(0).endIndex);


        assertEquals(TokenTypes.STRING_TOKEN, tokens.get(1).type);
        assertEquals(2, tokens.get(1).startIndex);
        assertEquals(3, tokens.get(1).endIndex);


        assertEquals(TokenTypes.STRING_TOKEN, tokens.get(2).type);
        assertEquals(6, tokens.get(2).startIndex);
        assertEquals(7, tokens.get(2).endIndex);

        tokens.get(0).toString();


    }

    @Test
    void testSingletonListWithOneObject() {

        final IndexOverlayParser parser = new JsonParser();
        //...................01234567890
        final String json = "[{'h':'a'}]";
        final List<Token> tokens = parser.scan(Json.niceJson(json));

        final var arrayToken = tokens.get(0);


        assertEquals(TokenTypes.ARRAY_TOKEN, arrayToken.type);
        assertEquals(json, tokens.get(0).asString(json));
        assertEquals(TokenTypes.OBJECT_TOKEN, tokens.get(1).type);
        assertEquals("{'h':'a'}", tokens.get(1).asString(json));


    }


    @Test
    void testListOfObjects() {
        final IndexOverlayParser parser = new JsonParser();
        //...................01234567890123456789012
        final String json = "[{'h':'a'},{'i':'b'}]";
        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(TokenTypes.ARRAY_TOKEN, tokens.get(0).type);
        assertEquals(TokenTypes.OBJECT_TOKEN, tokens.get(1).type);
        assertEquals(TokenTypes.OBJECT_TOKEN, tokens.get(6).type);

        assertEquals("{'i':'b'}", tokens.get(6).asString(json));
        assertEquals("{'h':'a'}", tokens.get(1).asString(json));
    }

    @Test
    void testMapTwoItems() {
        final IndexOverlayParser parser = new JsonParser();
        //...................01234567890123456789012
        final String json = "{'h':'a', 'i':'b'}";
        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(TokenTypes.OBJECT_TOKEN, tokens.get(0).type);
        assertEquals(18, tokens.get(0).endIndex);
        assertEquals(TokenTypes.ATTRIBUTE_KEY_TOKEN, tokens.get(1).type);
        assertEquals(TokenTypes.STRING_TOKEN, tokens.get(2).type);

    }

    @Test
    void testListOfLists() {

        final IndexOverlayParser parser = new JsonParser();
        final String json = "[['h','a'],['i','b']]";
        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));

        assertEquals(TokenTypes.ARRAY_TOKEN, tokens.get(0).type);
        assertEquals(TokenTypes.ARRAY_TOKEN, tokens.get(1).type);
        assertEquals(TokenTypes.STRING_TOKEN, tokens.get(2).type);
        assertEquals(TokenTypes.STRING_TOKEN, tokens.get(3).type);
        assertEquals(TokenTypes.ARRAY_TOKEN, tokens.get(4).type);

        assertEquals("['i','b']", tokens.get(4).asString(json));


    }


    @Test
    void testStringKey() {

        final IndexOverlayParser parser = new JsonParser();
        final String json = "{'1':1}";
        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));

        assertEquals(TokenTypes.OBJECT_TOKEN, tokens.get(0).type);
        assertEquals(TokenTypes.ATTRIBUTE_KEY_TOKEN, tokens.get(1).type);
        assertEquals(TokenTypes.STRING_TOKEN, tokens.get(2).type);
        assertEquals(TokenTypes.ATTRIBUTE_VALUE_TOKEN, tokens.get(3).type);
        assertEquals(TokenTypes.INT_TOKEN, tokens.get(4).type);

    }

    @Test
    void test2ItemIntKeyMap() {
        final IndexOverlayParser parser = new JsonParser();
        final String json = "{'1':2,'2':3}";

        final var tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));

        /**
         * Token[startIndex=0, endIndex=13, type=OBJECT] 0
         * Token[startIndex=1, endIndex=4, type=ATTRIBUTE_KEY] 1
         * Token[startIndex=2, endIndex=3, type=STRING] 2
         * Token[startIndex=5, endIndex=6, type=ATTRIBUTE_VALUE] 3
         * Token[startIndex=5, endIndex=6, type=INT] 4
         * Token[startIndex=7, endIndex=10, type=ATTRIBUTE_KEY] 5
         * Token[startIndex=8, endIndex=9, type=STRING] 6
         * Token[startIndex=11, endIndex=12, type=ATTRIBUTE_VALUE] 7
         * Token[startIndex=11, endIndex=12, type=INT] 8
         */
        validateToken(tokens.get(0), TokenTypes.OBJECT_TOKEN, 0, 13);
        validateToken(tokens.get(1), TokenTypes.ATTRIBUTE_KEY_TOKEN, 1, 4);
        validateToken(tokens.get(2), TokenTypes.STRING_TOKEN, 2, 3);
        validateToken(tokens.get(3), TokenTypes.ATTRIBUTE_VALUE_TOKEN, 5, 6);
        validateToken(tokens.get(4), TokenTypes.INT_TOKEN, 5, 6);
        validateToken(tokens.get(5), TokenTypes.ATTRIBUTE_KEY_TOKEN, 7, 10);
        validateToken(tokens.get(6), TokenTypes.STRING_TOKEN, 8, 9);
        validateToken(tokens.get(7), TokenTypes.ATTRIBUTE_VALUE_TOKEN, 11, 12);
        validateToken(tokens.get(8), TokenTypes.INT_TOKEN, 11, 12);


    }


    @Test
    void testParseNumber() {
        final IndexOverlayParser parser = new JsonParser();
        //0123456789
        final String json = "1";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));

        assertEquals(TokenTypes.INT_TOKEN, tokens.get(0).type);
        assertEquals(0, tokens.get(0).startIndex);
        assertEquals(1, tokens.get(0).endIndex);


    }

    @Test
    void testParseNumberFloat() {
        final IndexOverlayParser parser = new JsonParser();
        //...................0123456789
        final String json = "1.1";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));

        assertEquals(TokenTypes.FLOAT_TOKEN, tokens.get(0).type);
        assertEquals(0, tokens.get(0).startIndex);
        assertEquals(3, tokens.get(0).endIndex);


    }

    @Test
    void testParseNumberFloat2() {
        final IndexOverlayParser parser = new JsonParser();
        //...................0123456789
        final String json = "1.1e-12";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));

        assertEquals(TokenTypes.FLOAT_TOKEN, tokens.get(0).type);
        assertEquals(0, tokens.get(0).startIndex);
        assertEquals(7, tokens.get(0).endIndex);


    }

    @Test
    void testSimpleListStrNum() {
        final IndexOverlayParser parser = new JsonParser();
        //0123456789
        final String json = "['h',1]";

        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(TokenTypes.ARRAY_TOKEN, tokens.get(0).type);
        assertEquals(0, tokens.get(0).startIndex);
        assertEquals(7, tokens.get(0).endIndex);


        assertEquals(TokenTypes.STRING_TOKEN, tokens.get(1).type);
        assertEquals(2, tokens.get(1).startIndex);
        assertEquals(3, tokens.get(1).endIndex);
        assertEquals("h", tokens.get(1).asString(json));


        assertEquals(TokenTypes.INT_TOKEN, tokens.get(2).type);
        assertEquals(5, tokens.get(2).startIndex);
        assertEquals(6, tokens.get(2).endIndex);
        assertEquals("1", tokens.get(2).asString(json));

    }


    @Test
    void testSimpleListTwoNumbers() {
        final IndexOverlayParser parser = new JsonParser();
        //...................0123456789
        final String json = "[1,2]";

        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(TokenTypes.ARRAY_TOKEN, tokens.get(0).type);
        assertEquals(0, tokens.get(0).startIndex);
        assertEquals(5, tokens.get(0).endIndex);


        assertEquals(TokenTypes.INT_TOKEN, tokens.get(1).type);
        assertEquals(1, tokens.get(1).startIndex);
        assertEquals(2, tokens.get(1).endIndex);
        assertEquals("1", tokens.get(1).asString(json));


        assertEquals(TokenTypes.INT_TOKEN, tokens.get(2).type);
        assertEquals(3, tokens.get(2).startIndex);
        assertEquals(4, tokens.get(2).endIndex);
        assertEquals("2", tokens.get(2).asString(json));

    }

    @Test
    void testSimpleListStrStr() {
        final IndexOverlayParser parser = new JsonParser();
        //0123456789
        final String json = "['h','i']";

        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(TokenTypes.ARRAY_TOKEN, tokens.get(0).type);
        assertEquals(0, tokens.get(0).startIndex);
        assertEquals(9, tokens.get(0).endIndex);


        assertEquals(TokenTypes.STRING_TOKEN, tokens.get(1).type);
        assertEquals(2, tokens.get(1).startIndex);
        assertEquals(3, tokens.get(1).endIndex);


        assertEquals(TokenTypes.STRING_TOKEN, tokens.get(2).type);
        assertEquals(6, tokens.get(2).startIndex);
        assertEquals(7, tokens.get(2).endIndex);

    }

    @Test
    void testSimpleObject() {
        final IndexOverlayParser parser = new JsonParser();
        //...................0123456789
        final String json = "{'h':'a'}";
        final List<Token> tokens = parser.scan(Json.niceJson(json));
        assertEquals(TokenTypes.OBJECT_TOKEN, tokens.get(0).type);
        assertEquals(0, tokens.get(0).startIndex);
        assertEquals(9, tokens.get(0).endIndex);
        final Token key = tokens.get(1);
        final Token keyValue = tokens.get(2);
        final Token value = tokens.get(3);
        final Token valueValue = tokens.get(4);

        assertEquals(TokenTypes.ATTRIBUTE_KEY_TOKEN, key.type);
        assertEquals(1, key.startIndex);
        assertEquals(4, key.endIndex);

        assertEquals(TokenTypes.STRING_TOKEN, keyValue.type);
        assertEquals(2, keyValue.startIndex);
        assertEquals(3, keyValue.endIndex);


        assertEquals(TokenTypes.ATTRIBUTE_VALUE_TOKEN, value.type);
        assertEquals(5, value.startIndex);
        assertEquals(8, value.endIndex);


        assertEquals(TokenTypes.STRING_TOKEN, valueValue.type);
        assertEquals(6, valueValue.startIndex);
        assertEquals(7, valueValue.endIndex);
    }


    @Test
    void testSimpleObjectNumberValue() {
        final IndexOverlayParser parser = new JsonParser();
        //...................0123456789
        final String json = "{'h': 1  }";
        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(TokenTypes.OBJECT_TOKEN, tokens.get(0).type);
        assertEquals(0, tokens.get(0).startIndex);
        assertEquals(10, tokens.get(0).endIndex);
        final Token key = tokens.get(1);
        final Token keyValue = tokens.get(2);
        final Token value = tokens.get(3);
        final Token valueValue = tokens.get(4);

        assertEquals(TokenTypes.ATTRIBUTE_KEY_TOKEN, key.type);
        assertEquals(1, key.startIndex);
        assertEquals(4, key.endIndex);

        assertEquals(TokenTypes.STRING_TOKEN, keyValue.type);
        assertEquals(2, keyValue.startIndex);
        assertEquals(3, keyValue.endIndex);


        assertEquals(TokenTypes.ATTRIBUTE_VALUE_TOKEN, value.type);
        assertEquals(6, value.startIndex);
        assertEquals(9, value.endIndex);


        assertEquals(TokenTypes.INT_TOKEN, valueValue.type);
        assertEquals(6, valueValue.startIndex);
        assertEquals(7, valueValue.endIndex);
    }


    @Test
    void testSimpleBooleanTrue() {
        final IndexOverlayParser parser = new JsonParser();
        //...................01234567890123456789
        final String json = "true";
        final CharSource source = Sources.stringSource(Json.niceJson(json));
        final List<Token> tokens = parser.scan(source);
        final var trueToken = tokens.get(0);
        assertEquals(TokenTypes.BOOLEAN_TOKEN, trueToken.type);
        assertEquals(0, trueToken.startIndex);
        assertEquals(4, trueToken.endIndex);
        //assertEquals(5, source.getIndex());
    }

    @Test
    void testSimpleMapFromMap() {
        final IndexOverlayParser parser = new JsonParser();
        //...................01234567890123456789
        final String json = "{'a':{'a':1}}";
        final RootNode jsonRoot = parser.parse(Json.niceJson(json));
        jsonRoot.tokens().forEach(System.out::println);
        final var objectToken = jsonRoot.tokens().get(0);
        assertEquals(TokenTypes.OBJECT_TOKEN, objectToken.type);
        validateToken(objectToken, TokenTypes.OBJECT_TOKEN, 0, 13);

        final var keyToken = jsonRoot.tokens().get(1);
        assertEquals(TokenTypes.ATTRIBUTE_KEY_TOKEN, keyToken.type);
        final var stringToken = jsonRoot.tokens().get(2);
        assertEquals(TokenTypes.STRING_TOKEN, stringToken.type);
        final var valueToken = jsonRoot.tokens().get(3);
        assertEquals(TokenTypes.ATTRIBUTE_VALUE_TOKEN, valueToken.type);


    }
    @Test
    void testSimpleBooleanFalse() {
        final IndexOverlayParser parser = new JsonParser();
        //...................01234567890123456789
        final String json = "false";
        final CharSource source = Sources.stringSource(Json.niceJson(json));
        final List<Token> tokens = parser.scan(source);
        final var token = tokens.get(0);
        assertEquals(TokenTypes.BOOLEAN_TOKEN, token.type);
        assertEquals(0, token.startIndex);
        assertEquals(5, token.endIndex);
        assertEquals(5, source.getIndex());
    }

    @Test
    void testSimpleString2() {
        final IndexOverlayParser parser = new JsonParser();
        //...................01234567890123456789
        final String json = "'hi'";
        final CharSource source = Sources.stringSource(Json.niceJson(json));
        final List<Token> tokens = parser.scan(source);
        final var token = tokens.get(0);
        assertEquals(TokenTypes.STRING_TOKEN, token.type);
        assertEquals(1, token.startIndex);
        assertEquals(3, token.endIndex);
        assertEquals(4, source.getIndex());
    }

    @Test
    void testSimpleObjectTwoItems() {
        final IndexOverlayParser parser = new JsonParser();
        //...................01234567890123456789
        final String json = "{'h':'a', 'i':'b'}";
        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(TokenTypes.OBJECT_TOKEN, tokens.get(0).type);
        assertEquals(0, tokens.get(0).startIndex);
        assertEquals(18, tokens.get(0).endIndex);
        final Token key = tokens.get(1);
        final Token keyValue = tokens.get(2);
        final Token value = tokens.get(3);
        final Token valueValue = tokens.get(4);

        final Token key2 = tokens.get(5);
        final Token keyValue2 = tokens.get(6);
        final Token value2 = tokens.get(7);
        final Token valueValue2 = tokens.get(8);

        assertEquals(TokenTypes.ATTRIBUTE_KEY_TOKEN, key.type);
       assertEquals(1, key.startIndex);
        assertEquals(4, key.endIndex);

        assertEquals(TokenTypes.STRING_TOKEN, keyValue.type);
        assertEquals(2, keyValue.startIndex);
        assertEquals(3, keyValue.endIndex);


        assertEquals(TokenTypes.ATTRIBUTE_VALUE_TOKEN, value.type);
        assertEquals(5, value.startIndex);
        assertEquals(8, value.endIndex);


        assertEquals(TokenTypes.STRING_TOKEN, valueValue.type);
        assertEquals(6, valueValue.startIndex);
        assertEquals(7, valueValue.endIndex);


        assertEquals(TokenTypes.ATTRIBUTE_KEY_TOKEN, key2.type);
        assertEquals(10, key2.startIndex);
        assertEquals(13, key2.endIndex);

        assertEquals(TokenTypes.STRING_TOKEN, keyValue2.type);
        assertEquals(11, keyValue2.startIndex);
        assertEquals(12, keyValue2.endIndex);

        assertEquals(TokenTypes.ATTRIBUTE_VALUE_TOKEN, value2.type);
        assertEquals(14, value2.startIndex);
        assertEquals(17, value2.endIndex);

        assertEquals(TokenTypes.STRING_TOKEN, valueValue2.type);
        assertEquals(15, valueValue2.startIndex);
        assertEquals(16, valueValue2.endIndex);

    }


    @Test
    void testSimpleObjectTwoItemsWeirdSpacing() {
        final IndexOverlayParser parser = new JsonParser();
        //.................. 0123456789012345678901234567890123456789012345
        final String json = "   {'h':   'a',\n\t 'i':'b'\n\t } \n\t    \n";
        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(TokenTypes.OBJECT_TOKEN, tokens.get(0).type);
        final Token key = tokens.get(1);
        final Token keyValue = tokens.get(2);
        final Token value = tokens.get(3);
        final Token valueValue = tokens.get(4);

        final Token key2 = tokens.get(5);
        final Token keyValue2 = tokens.get(6);
        final Token value2 = tokens.get(7);
        final Token valueValue2 = tokens.get(8);

        assertEquals(TokenTypes.ATTRIBUTE_KEY_TOKEN, key.type);

        assertEquals(TokenTypes.STRING_TOKEN, keyValue.type);


        assertEquals(TokenTypes.ATTRIBUTE_VALUE_TOKEN, value.type);


        assertEquals(TokenTypes.STRING_TOKEN, valueValue.type);


        assertEquals(TokenTypes.ATTRIBUTE_KEY_TOKEN, key2.type);

        assertEquals(TokenTypes.STRING_TOKEN, keyValue2.type);

        assertEquals(TokenTypes.ATTRIBUTE_VALUE_TOKEN, value2.type);

        assertEquals(TokenTypes.STRING_TOKEN, valueValue2.type);

    }


    @Test
    void testSimpleObjectTwoItemsWeirdSpacing2() {
        final IndexOverlayParser parser = new JsonParser();
        //01234567890123456789
        final String json = "   {'h':   'a',   'i':'b'   }        ";
        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(TokenTypes.OBJECT_TOKEN, tokens.get(0).type);
        final Token key = tokens.get(1);
        final Token keyValue = tokens.get(2);
        final Token value = tokens.get(3);
        final Token valueValue = tokens.get(4);

        final Token key2 = tokens.get(5);
        final Token keyValue2 = tokens.get(6);
        final Token value2 = tokens.get(7);
        final Token valueValue2 = tokens.get(8);

        assertEquals(TokenTypes.ATTRIBUTE_KEY_TOKEN, key.type);

        assertEquals(TokenTypes.STRING_TOKEN, keyValue.type);


        assertEquals(TokenTypes.ATTRIBUTE_VALUE_TOKEN, value.type);


        assertEquals(TokenTypes.STRING_TOKEN, valueValue.type);


        assertEquals(TokenTypes.ATTRIBUTE_KEY_TOKEN, key2.type);

        assertEquals(TokenTypes.STRING_TOKEN, keyValue2.type);

        assertEquals(TokenTypes.ATTRIBUTE_VALUE_TOKEN, value2.type);

        assertEquals(TokenTypes.STRING_TOKEN, valueValue2.type);

    }

    @Test
    void testSimpleString() {
        final IndexOverlayParser parser = new JsonParser();
        final String str = "h";
        final String json = String.format("\"%s\"", str);
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(1, token.startIndex);
        assertEquals(2, token.endIndex);
        assertEquals(str, json.substring(token.startIndex, token.endIndex));
    }

    @Test
    void testSimpleStringSkipWhiteSpace() {
        final IndexOverlayParser parser = new JsonParser();
        final String str = "hi mom";
        final String json = String.format("     \"%s\"     ", str);
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(6, token.startIndex);
        assertEquals(12, token.endIndex);
        assertEquals(str, json.substring(token.startIndex, token.endIndex));
    }

    @Test
    void testStringHandleControlChars() {
        final IndexOverlayParser parser = new JsonParser();
        final String str = "hi mom \\\" \\n \\t all good";
        final String json = String.format("     \"%s\"     ", str);
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(6, token.startIndex);
        assertEquals(30, token.endIndex);
        assertEquals(str, json.substring(token.startIndex, token.endIndex));
    }

    @Test
    void testSimpleNumber() {
        final IndexOverlayParser parser = new JsonParser();
        //...................0123
        final String json = "1 ";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(0, token.startIndex);
        assertEquals(1, token.endIndex);
        assertEquals("1", json.substring(token.startIndex, token.endIndex));
    }

    @Test
    void testSimpleNumberNoSpace() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "1";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(0, token.startIndex);
        assertEquals(1, token.endIndex);
        assertEquals("1", json.substring(token.startIndex, token.endIndex));
    }


    @Test
    void testSimpleLongNumber() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "1234567890 ";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(0, token.startIndex);
        assertEquals(10, token.endIndex);
        assertEquals(TokenTypes.INT_TOKEN, token.type);


        String value = json.substring(token.startIndex, token.endIndex);
        assertEquals("1234567890", value);
    }

    @Test
    void testSimpleLongNoSpace() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "1234567890";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(0, token.startIndex);
        assertEquals(10, token.endIndex);
        assertEquals(TokenTypes.INT_TOKEN, token.type);

        String value = json.substring(token.startIndex, token.endIndex);
        assertEquals("1234567890", value);
    }


    @Test
    void testDecimal() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "1.12 ";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(0, token.startIndex);
        assertEquals(4, token.endIndex);
        assertEquals(TokenTypes.FLOAT_TOKEN, token.type);


        String value = json.substring(token.startIndex, token.endIndex);
        assertEquals("1.12", value);
    }

    @Test
    void testDecimalNoSpace() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "1.12";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(0, token.startIndex);
        assertEquals(4, token.endIndex);
        assertEquals(TokenTypes.FLOAT_TOKEN, token.type);


        String value = json.substring(token.startIndex, token.endIndex);
        assertEquals("1.12", value);
    }

    @Test
    void testDecimalWithExponent() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "1.12e5";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(0, token.startIndex);
        assertEquals(6, token.endIndex);
        assertEquals(TokenTypes.FLOAT_TOKEN, token.type);


        String value = json.substring(token.startIndex, token.endIndex);
        assertEquals("1.12e5", value);
    }

    @Test
    void testDecimalOneEFive() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "1e5";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(0, token.startIndex);
        assertEquals(3, token.endIndex);
        assertEquals(TokenTypes.FLOAT_TOKEN, token.type);


        String value = json.substring(token.startIndex, token.endIndex);
        assertEquals("1e5", value);
    }

    @Test
    void testBadChar() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "@";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }


    @Test
    void testBadCharInNumber() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "123@";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testBadCharInFloat() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "123.1@";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testTooManySignOperators() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "123.1e-+1";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedExponentChar() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "123.1e-@";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedList() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "[1,@]";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedMapValue() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "{1:@}";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedKey() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "{@:1}";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }


    @Test
    void testUnexpectedEndKeyKey() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "{:1}";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedEndOfMapValue() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "{1:}";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }


    @Test
    void stringNotClosed() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "\"hello cruel world'";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testParseFloatWithExponentMarkerInList() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "[123.1e-9]";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));

        assertEquals(TokenTypes.ARRAY_TOKEN, tokens.get(0).type);
        assertEquals(TokenTypes.FLOAT_TOKEN, tokens.get(1).type);

        assertEquals("123.1e-9", tokens.get(1).asString(json));

    }

    @Test
    void encoding() {
        final IndexOverlayParser parser = new JsonParser();

        final String json = "'`u0003'";
        final List<Token> tokens = parser.scan(Json.niceJson(json));

        assertEquals(TokenTypes.STRING_TOKEN, tokens.get(0).type);


    }
}
