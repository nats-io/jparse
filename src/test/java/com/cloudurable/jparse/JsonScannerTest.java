package com.cloudurable.jparse;


import com.cloudurable.jparse.source.Sources;
import com.cloudurable.jparse.token.Token;
import com.cloudurable.jparse.token.TokenType;
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
        validateToken(tokens.get(0), TokenType.OBJECT, 0, 27);

        validateToken(tokens.get(1), TokenType.ATTRIBUTE_KEY, 1, 4);
        validateToken(tokens.get(2), TokenType.STRING, 2, 3);
        validateToken(tokens.get(3), TokenType.ATTRIBUTE_VALUE, 5, 6);
        validateToken(tokens.get(4), TokenType.INT, 5, 6);


        validateToken(tokens.get(5), TokenType.ATTRIBUTE_KEY, 7, 10);
        validateToken(tokens.get(6), TokenType.STRING, 8, 9);
        validateToken(tokens.get(7), TokenType.ATTRIBUTE_VALUE, 11, 12);
        validateToken(tokens.get(8), TokenType.INT, 11, 12);


        validateToken(tokens.get(9), TokenType.ATTRIBUTE_KEY, 13, 18);
        validateToken(tokens.get(10), TokenType.STRING, 14, 17);


        validateToken(tokens.get(11), TokenType.ATTRIBUTE_VALUE, 19, 26);
        validateToken(tokens.get(12), TokenType.ARRAY, 19, 26);

        validateToken(tokens.get(13), TokenType.INT, 20, 21);
        validateToken(tokens.get(14), TokenType.INT, 22, 23);
        validateToken(tokens.get(15), TokenType.INT, 24, 25);

    }


    @Test
    void testSimpleListWithInts() {
        final Parser parser = new JsonParser();
        //...................0123456
        final String json = "[ 1 , 3 ]";

        final List<Token> tokens = parser.scan(json.replace("'", "\""));

        assertEquals(TokenType.ARRAY, tokens.get(0).type());
        assertEquals(0, tokens.get(0).startIndex());
        assertEquals(9, tokens.get(0).endIndex());


        assertEquals(TokenType.INT, tokens.get(1).type());
        assertEquals(2, tokens.get(1).startIndex());
        assertEquals(3, tokens.get(1).endIndex());


        assertEquals(TokenType.INT, tokens.get(2).type());
        assertEquals(6, tokens.get(2).startIndex());
        assertEquals(7, tokens.get(2).endIndex());

    }

    @Test
    void testSimpleListWithIntsNoSpaces() {
        final Parser parser = new JsonParser();
        //...................0123456
        final String json = "[1,3]";

        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));

        assertEquals(TokenType.ARRAY, tokens.get(0).type());
        assertEquals(0, tokens.get(0).startIndex());
        assertEquals(5, tokens.get(0).endIndex());


        assertEquals(TokenType.INT, tokens.get(1).type());
        assertEquals(1, tokens.get(1).startIndex());
        assertEquals(2, tokens.get(1).endIndex());


        assertEquals(TokenType.INT, tokens.get(2).type());
        assertEquals(3, tokens.get(2).startIndex());
        assertEquals(4, tokens.get(2).endIndex());

    }

    @Test
    void testSimpleList() {


        final Parser parser = new JsonParser();
        //0123456789
        final String json = "['h','a']";
        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));

        assertEquals(TokenType.ARRAY, tokens.get(0).type());
        assertEquals(0, tokens.get(0).startIndex());
        assertEquals(9, tokens.get(0).endIndex());


        assertEquals(TokenType.STRING, tokens.get(1).type());
        assertEquals(2, tokens.get(1).startIndex());
        assertEquals(3, tokens.get(1).endIndex());


        assertEquals(TokenType.STRING, tokens.get(2).type());
        assertEquals(6, tokens.get(2).startIndex());
        assertEquals(7, tokens.get(2).endIndex());

        tokens.get(0).toString();


    }

    @Test
    void testSingletonListWithOneObject() {

        final Parser parser = new JsonParser();
        final String json = "[{'h':'a'}]";
        final List<Token> tokens = parser.scan(Sources.charSeqSource(json.replace("'", "\"")));

        assertEquals(TokenType.ARRAY, tokens.get(0).type());
        assertEquals(json, tokens.get(0).asString(json));
        assertEquals(TokenType.OBJECT, tokens.get(1).type());
        assertEquals("{'h':'a'}", tokens.get(1).asString(json));


    }


    @Test
    void testListOfObjects() {
        final Parser parser = new JsonParser();
        //...................01234567890123456789012
        final String json = "[{'h':'a'},{'i':'b'}]";
        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(TokenType.ARRAY, tokens.get(0).type());
        assertEquals(TokenType.OBJECT, tokens.get(1).type());
        assertEquals(TokenType.OBJECT, tokens.get(6).type());

        assertEquals("{'i':'b'}", tokens.get(6).asString(json));
        assertEquals("{'h':'a'}", tokens.get(1).asString(json));
    }

    @Test
    void testMapTwoItems() {
        final Parser parser = new JsonParser();
        //...................01234567890123456789012
        final String json = "{'h':'a', 'i':'b'}";
        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(TokenType.OBJECT, tokens.get(0).type());
        assertEquals(18, tokens.get(0).endIndex());
        assertEquals(TokenType.ATTRIBUTE_KEY, tokens.get(1).type());
        assertEquals(TokenType.STRING, tokens.get(2).type());

    }

    @Test
    void testListOfLists() {

        final Parser parser = new JsonParser();
        final String json = "[['h','a'],['i','b']]";
        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));

        assertEquals(TokenType.ARRAY, tokens.get(0).type());
        assertEquals(TokenType.ARRAY, tokens.get(1).type());
        assertEquals(TokenType.STRING, tokens.get(2).type());
        assertEquals(TokenType.STRING, tokens.get(3).type());
        assertEquals(TokenType.ARRAY, tokens.get(4).type());

        assertEquals("['i','b']", tokens.get(4).asString(json));


    }


    @Test
    void testStringKey() {

        final Parser parser = new JsonParser();
        final String json = "{'1':1}";
        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));

        assertEquals(TokenType.OBJECT, tokens.get(0).type());
        assertEquals(TokenType.ATTRIBUTE_KEY, tokens.get(1).type());
        assertEquals(TokenType.STRING, tokens.get(2).type());
        assertEquals(TokenType.ATTRIBUTE_VALUE, tokens.get(3).type());
        assertEquals(TokenType.INT, tokens.get(4).type());

    }

    @Test
    void test2ItemIntKeyMap() {
        final Parser parser = new JsonParser();
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
        validateToken(tokens.get(0), TokenType.OBJECT, 0, 13);
        validateToken(tokens.get(1), TokenType.ATTRIBUTE_KEY, 1, 4);
        validateToken(tokens.get(2), TokenType.STRING, 2, 3);
        validateToken(tokens.get(3), TokenType.ATTRIBUTE_VALUE, 5, 6);
        validateToken(tokens.get(4), TokenType.INT, 5, 6);
        validateToken(tokens.get(5), TokenType.ATTRIBUTE_KEY, 7, 10);
        validateToken(tokens.get(6), TokenType.STRING, 8, 9);
        validateToken(tokens.get(7), TokenType.ATTRIBUTE_VALUE, 11, 12);
        validateToken(tokens.get(8), TokenType.INT, 11, 12);


    }


    @Test
    void testParseNumber() {
        final Parser parser = new JsonParser();
        //0123456789
        final String json = "1";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));

        assertEquals(TokenType.INT, tokens.get(0).type());
        assertEquals(0, tokens.get(0).startIndex());
        assertEquals(1, tokens.get(0).endIndex());


    }

    @Test
    void testParseNumberFloat() {
        final Parser parser = new JsonParser();
        //...................0123456789
        final String json = "1.1";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));

        assertEquals(TokenType.FLOAT, tokens.get(0).type());
        assertEquals(0, tokens.get(0).startIndex());
        assertEquals(3, tokens.get(0).endIndex());


    }

    @Test
    void testParseNumberFloat2() {
        final Parser parser = new JsonParser();
        //...................0123456789
        final String json = "1.1e-12";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));

        assertEquals(TokenType.FLOAT, tokens.get(0).type());
        assertEquals(0, tokens.get(0).startIndex());
        assertEquals(7, tokens.get(0).endIndex());


    }

    @Test
    void testSimpleListStrNum() {
        final Parser parser = new JsonParser();
        //0123456789
        final String json = "['h',1]";

        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(TokenType.ARRAY, tokens.get(0).type());
        assertEquals(0, tokens.get(0).startIndex());
        assertEquals(7, tokens.get(0).endIndex());


        assertEquals(TokenType.STRING, tokens.get(1).type());
        assertEquals(2, tokens.get(1).startIndex());
        assertEquals(3, tokens.get(1).endIndex());
        assertEquals("h", tokens.get(1).asString(json));


        assertEquals(TokenType.INT, tokens.get(2).type());
        assertEquals(5, tokens.get(2).startIndex());
        assertEquals(6, tokens.get(2).endIndex());
        assertEquals("1", tokens.get(2).asString(json));

    }

    @Test
    void testSimpleListStrStr() {
        final Parser parser = new JsonParser();
        //0123456789
        final String json = "['h','i']";

        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(TokenType.ARRAY, tokens.get(0).type());
        assertEquals(0, tokens.get(0).startIndex());
        assertEquals(9, tokens.get(0).endIndex());


        assertEquals(TokenType.STRING, tokens.get(1).type());
        assertEquals(2, tokens.get(1).startIndex());
        assertEquals(3, tokens.get(1).endIndex());


        assertEquals(TokenType.STRING, tokens.get(2).type());
        assertEquals(6, tokens.get(2).startIndex());
        assertEquals(7, tokens.get(2).endIndex());

    }

    @Test
    void testSimpleObject() {
        final Parser parser = new JsonParser();
        //0123456789
        final String json = "{'h':'a'}";
        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(TokenType.OBJECT, tokens.get(0).type());
        assertEquals(0, tokens.get(0).startIndex());
        assertEquals(9, tokens.get(0).endIndex());
        final Token key = tokens.get(1);
        final Token keyValue = tokens.get(2);
        final Token value = tokens.get(3);
        final Token valueValue = tokens.get(4);

        assertEquals(TokenType.ATTRIBUTE_KEY, key.type());
        assertEquals(1, key.startIndex());
        assertEquals(4, key.endIndex());

        assertEquals(TokenType.STRING, keyValue.type());
        assertEquals(2, keyValue.startIndex());
        assertEquals(3, keyValue.endIndex());


        assertEquals(TokenType.ATTRIBUTE_VALUE, value.type());
        assertEquals(5, value.startIndex());
        assertEquals(8, value.endIndex());


        assertEquals(TokenType.STRING, valueValue.type());
        assertEquals(6, valueValue.startIndex());
        assertEquals(7, valueValue.endIndex());
    }


    @Test
    void testSimpleObjectNumberValue() {
        final Parser parser = new JsonParser();
        //0123456789
        final String json = "{'h' : 1 }";
        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(TokenType.OBJECT, tokens.get(0).type());
        assertEquals(0, tokens.get(0).startIndex());
        assertEquals(10, tokens.get(0).endIndex());
        final Token key = tokens.get(1);
        final Token keyValue = tokens.get(2);
        final Token value = tokens.get(3);
        final Token valueValue = tokens.get(4);

        assertEquals(TokenType.ATTRIBUTE_KEY, key.type());
        assertEquals(1, key.startIndex());
        assertEquals(5, key.endIndex());

        assertEquals(TokenType.STRING, keyValue.type());
        assertEquals(2, keyValue.startIndex());
        assertEquals(3, keyValue.endIndex());


        assertEquals(TokenType.ATTRIBUTE_VALUE, value.type());
//        assertEquals(6, value.startIndex());
        assertEquals(9, value.endIndex());


        assertEquals(TokenType.INT, valueValue.type());
        assertEquals(7, valueValue.startIndex());
        assertEquals(8, valueValue.endIndex());
    }


    @Test
    void testSimpleObjectTwoItems() {
        final Parser parser = new JsonParser();
        //01234567890123456789
        final String json = "{'h':'a', 'i':'b'}";
        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(TokenType.OBJECT, tokens.get(0).type());
        assertEquals(0, tokens.get(0).startIndex());
        assertEquals(18, tokens.get(0).endIndex());
        final Token key = tokens.get(1);
        final Token keyValue = tokens.get(2);
        final Token value = tokens.get(3);
        final Token valueValue = tokens.get(4);

        final Token key2 = tokens.get(5);
        final Token keyValue2 = tokens.get(6);
        final Token value2 = tokens.get(7);
        final Token valueValue2 = tokens.get(8);

        assertEquals(TokenType.ATTRIBUTE_KEY, key.type());
        assertEquals(1, key.startIndex());
        assertEquals(4, key.endIndex());

        assertEquals(TokenType.STRING, keyValue.type());
        assertEquals(2, keyValue.startIndex());
        assertEquals(3, keyValue.endIndex());


        assertEquals(TokenType.ATTRIBUTE_VALUE, value.type());
        assertEquals(5, value.startIndex());
        assertEquals(8, value.endIndex());


        assertEquals(TokenType.STRING, valueValue.type());
        assertEquals(6, valueValue.startIndex());
        assertEquals(7, valueValue.endIndex());


        assertEquals(TokenType.ATTRIBUTE_KEY, key2.type());
//        assertEquals(9, key2.startIndex());
        assertEquals(13, key2.endIndex());

        assertEquals(TokenType.STRING, keyValue2.type());
        assertEquals(11, keyValue2.startIndex());
        assertEquals(12, keyValue2.endIndex());

        assertEquals(TokenType.ATTRIBUTE_VALUE, value2.type());
        assertEquals(14, value2.startIndex());
        assertEquals(17, value2.endIndex());

        assertEquals(TokenType.STRING, valueValue2.type());
        assertEquals(15, valueValue2.startIndex());
        assertEquals(16, valueValue2.endIndex());

    }


    @Test
    void testSimpleObjectTwoItemsWeirdSpacing() {
        final Parser parser = new JsonParser();
        //01234567890123456789
        final String json = "   {'h':   'a',\n\t 'i':'b'\n\t } \n\t    \n";
        final List<Token> tokens = parser.scan(Sources.stringSource(json.replace("'", "\"")));
        assertEquals(TokenType.OBJECT, tokens.get(0).type());
        final Token key = tokens.get(1);
        final Token keyValue = tokens.get(2);
        final Token value = tokens.get(3);
        final Token valueValue = tokens.get(4);

        final Token key2 = tokens.get(5);
        final Token keyValue2 = tokens.get(6);
        final Token value2 = tokens.get(7);
        final Token valueValue2 = tokens.get(8);

        assertEquals(TokenType.ATTRIBUTE_KEY, key.type());

        assertEquals(TokenType.STRING, keyValue.type());


        assertEquals(TokenType.ATTRIBUTE_VALUE, value.type());


        assertEquals(TokenType.STRING, valueValue.type());


        assertEquals(TokenType.ATTRIBUTE_KEY, key2.type());

        assertEquals(TokenType.STRING, keyValue2.type());

        assertEquals(TokenType.ATTRIBUTE_VALUE, value2.type());

        assertEquals(TokenType.STRING, valueValue2.type());

    }

    @Test
    void testSimpleString() {
        final Parser parser = new JsonParser();
        final String str = "h";
        final String json = String.format("\"%s\"", str);
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(1, token.startIndex());
        assertEquals(2, token.endIndex());
        assertEquals(str, json.substring(token.startIndex(), token.endIndex()));
    }

    @Test
    void testSimpleStringSkipWhiteSpace() {
        final Parser parser = new JsonParser();
        final String str = "hi mom";
        final String json = String.format("     \"%s\"     ", str);
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(6, token.startIndex());
        assertEquals(12, token.endIndex());
        assertEquals(str, json.substring(token.startIndex(), token.endIndex()));
    }

    @Test
    void testStringHandleControlChars() {
        final Parser parser = new JsonParser();
        final String str = "hi mom \\\" \\n \\t all good";
        final String json = String.format("     \"%s\"     ", str);
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(6, token.startIndex());
        assertEquals(30, token.endIndex());
        assertEquals(str, json.substring(token.startIndex(), token.endIndex()));
    }

    @Test
    void testSimpleNumber() {
        final Parser parser = new JsonParser();

        final String json = "1 ";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(0, token.startIndex());
        assertEquals(1, token.endIndex());
        assertEquals("1", json.substring(token.startIndex(), token.endIndex()));
    }

    @Test
    void testSimpleNumberNoSpace() {
        final Parser parser = new JsonParser();

        final String json = "1";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(0, token.startIndex());
        assertEquals(1, token.endIndex());
        assertEquals("1", json.substring(token.startIndex(), token.endIndex()));
    }


    @Test
    void testSimpleLongNumber() {
        final Parser parser = new JsonParser();

        final String json = "1234567890 ";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(0, token.startIndex());
        assertEquals(10, token.endIndex());
        assertEquals(TokenType.INT, token.type());


        String value = json.substring(token.startIndex(), token.endIndex());
        assertEquals("1234567890", value);
    }

    @Test
    void testSimpleLongNoSpace() {
        final Parser parser = new JsonParser();

        final String json = "1234567890";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(0, token.startIndex());
        assertEquals(10, token.endIndex());
        assertEquals(TokenType.INT, token.type());

        String value = json.substring(token.startIndex(), token.endIndex());
        assertEquals("1234567890", value);
    }


    @Test
    void testDecimal() {
        final Parser parser = new JsonParser();

        final String json = "1.12 ";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(0, token.startIndex());
        assertEquals(4, token.endIndex());
        assertEquals(TokenType.FLOAT, token.type());


        String value = json.substring(token.startIndex(), token.endIndex());
        assertEquals("1.12", value);
    }

    @Test
    void testDecimalNoSpace() {
        final Parser parser = new JsonParser();

        final String json = "1.12";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(0, token.startIndex());
        assertEquals(4, token.endIndex());
        assertEquals(TokenType.FLOAT, token.type());


        String value = json.substring(token.startIndex(), token.endIndex());
        assertEquals("1.12", value);
    }

    @Test
    void testDecimalWithExponent() {
        final Parser parser = new JsonParser();

        final String json = "1.12e5";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(0, token.startIndex());
        assertEquals(6, token.endIndex());
        assertEquals(TokenType.FLOAT, token.type());


        String value = json.substring(token.startIndex(), token.endIndex());
        assertEquals("1.12e5", value);
    }

    @Test
    void testDecimalOneEFive() {
        final Parser parser = new JsonParser();

        final String json = "1e5";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(0, token.startIndex());
        assertEquals(3, token.endIndex());
        assertEquals(TokenType.FLOAT, token.type());


        String value = json.substring(token.startIndex(), token.endIndex());
        assertEquals("1e5", value);
    }

    @Test
    void testBadChar() {
        final Parser parser = new JsonParser();

        final String json = "@";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }


    @Test
    void testBadCharInNumber() {
        final Parser parser = new JsonParser();

        final String json = "123@";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testBadCharInFloat() {
        final Parser parser = new JsonParser();

        final String json = "123.1@";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testTooManySignOperators() {
        final Parser parser = new JsonParser();

        final String json = "123.1e-+1";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedExponentChar() {
        final Parser parser = new JsonParser();

        final String json = "123.1e-@";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedList() {
        final Parser parser = new JsonParser();

        final String json = "[1,@]";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedMapValue() {
        final Parser parser = new JsonParser();

        final String json = "{1:@}";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedKey() {
        final Parser parser = new JsonParser();

        final String json = "{@:1}";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedEndKeyKey() {
        final Parser parser = new JsonParser();

        final String json = "{:1}";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testUnexpectedEndOfMapValue() {
        final Parser parser = new JsonParser();

        final String json = "{1:}";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }


    @Test
    void stringNotClosed() {
        final Parser parser = new JsonParser();

        final String json = "\"hello cruel world'";
        try {
            final List<Token> tokens = parser.scan(Sources.stringSource(json));
            assertTrue(false);
        } catch (Exception ex) {

        }

    }

    @Test
    void testParseFloatWithExponentMarkerInList() {
        final Parser parser = new JsonParser();

        final String json = "[123.1e-9]";
        final List<Token> tokens = parser.scan(Sources.stringSource(json));

        assertEquals(TokenType.ARRAY, tokens.get(0).type());
        assertEquals(TokenType.FLOAT, tokens.get(1).type());

        assertEquals("123.1e-9", tokens.get(1).asString(json));

    }
}
