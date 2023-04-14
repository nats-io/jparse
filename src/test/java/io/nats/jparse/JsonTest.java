package io.nats.jparse;

import io.nats.jparse.node.ArrayNode;
import io.nats.jparse.node.ObjectNode;
import io.nats.jparse.source.Sources;
import io.nats.jparse.source.support.UnexpectedCharacterException;
import io.nats.jparse.token.Token;
import io.nats.jparse.token.TokenTypes;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonTest {


    @Test
    public void testToArrayNodeReturnsArrayNodeForValidJson() {
        String validJson = Json.niceJson("[{ 'name': 'John Doe', 'age': 98 }, { 'name': 'Jane Doe', 'age': 89 }]");
        ArrayNode arrayNode = Json.toArrayNode(validJson);

        String string = arrayNode.getObjectNode(0).getString("name");
        assertEquals("John Doe", string);
    }

    @Test
    public void testToArrayNodeReturnsArrayNodeForValidJsonSource() {
        String validJson = Json.niceJson("[{ 'name': 'John Doe', 'age': 98 }, { 'name': 'Jane Doe', 'age': 89 }]");
        ArrayNode arrayNode = Json.toArrayNode(Sources.stringSource(validJson));

        String string = arrayNode.getObjectNode(0).getString("name");
        assertEquals("John Doe", string);
    }
    @Test
    public void testToObjectNodeReturnsObjectNodeForValidJson() {
        String validJson = Json.niceJson("{ 'name': 'John Doe', 'age': 98 }");
        ObjectNode objectNode = Json.toObjectNode(validJson);

        assertEquals(ObjectNode.class, objectNode.getClass());

        String string = objectNode.getString("name");
        assertEquals("John Doe", string);
    }

    @Test
    public void testToObjectNodeReturnsObjectNodeForValidJsonSource() {
        String validJson = Json.niceJson("{ 'name': 'John Doe', 'age': 98 }");
        ObjectNode objectNode = Json.toObjectNode(Sources.stringSource(validJson));

        assertEquals(ObjectNode.class, objectNode.getClass());

        String string = objectNode.getString("name");
        assertEquals("John Doe", string);
    }
    @Test
    public void testToArrayNodeThrowsJsonParseExceptionForInvalidJson() {
        String invalidJson = "invalid json";
        assertThrows(UnexpectedCharacterException.class, () -> Json.toArrayNode(invalidJson));
        assertThrows(UnexpectedCharacterException.class, () -> Json.toArrayNode(Sources.stringSource(invalidJson)));
    }

    @Test
    public void testToObjectNodeThrowsJsonParseExceptionForInvalidJson() {
        String invalidJson = "invalid json";
        assertThrows(UnexpectedCharacterException.class, () -> Json.toObjectNode(invalidJson));
        assertThrows(UnexpectedCharacterException.class, () -> Json.toObjectNode(Sources.stringSource(invalidJson)));

    }

    @Test
    public void testToMapReturnsMapForValidJson() {
        String validJson = "{ \"foo\": \"bar\" }";
        Map<String, Object> result = Json.toMap(validJson);
        assertEquals( "bar", result.get("foo").toString());
    }

    @Test
    public void testToMapReturnsMapForValidJsonSource() {
        String validJson = "{ \"foo\": \"bar\" }";
        Map<String, Object> result = Json.toMap(Sources.stringSource(validJson));
        assertEquals( "bar", result.get("foo").toString());
    }
    @Test
    public void testToTokensReturnsCorrectTokenSequenceForJsonString() {
        String validJson = "{ \"foo\": \"bar\" }";
        List<Token> result = Json.toTokens(validJson);
        List<Token> expected = Arrays.asList(
                new Token(0, validJson.length(), TokenTypes.OBJECT_TOKEN),
                new Token(2, 7, TokenTypes.ATTRIBUTE_KEY_TOKEN),
                new Token(3, 6, TokenTypes.STRING_TOKEN),
                new Token(9, 15, TokenTypes.ATTRIBUTE_VALUE_TOKEN),
                new Token(10, 13, TokenTypes.STRING_TOKEN)
        );

        System.out.println(result);
        assertEquals(expected, result);
    }

    @Test
    public void testToTokensReturnsCorrectTokenSequenceForJsonStringSource() {
        String validJson = "{ \"foo\": \"bar\" }";
        List<Token> result = Json.toTokens(Sources.stringSource(validJson));
        List<Token> expected = Arrays.asList(
                new Token(0, validJson.length(), TokenTypes.OBJECT_TOKEN),
                new Token(2, 7, TokenTypes.ATTRIBUTE_KEY_TOKEN),
                new Token(3, 6, TokenTypes.STRING_TOKEN),
                new Token(9, 15, TokenTypes.ATTRIBUTE_VALUE_TOKEN),
                new Token(10, 13, TokenTypes.STRING_TOKEN)
        );

        System.out.println(result);
        assertEquals(expected, result);
    }
    @Test
    public void testToListReturnsListForValidJson() {
        String validJson = "[1, 2, 3]";
        List<Object> result = Json.toList(validJson);
        assertEquals(1, ((Number)result.get(0)).intValue());
        assertEquals(2, ((Number)result.get(1)).intValue());
        assertEquals(3, ((Number)result.get(2)).intValue());
    }

    @Test
    public void testToListReturnsListForValidJsonSource() {
        String validJson = "[1, 2, 3]";
        List<Object> result = Json.toList(Sources.stringSource(validJson));
        assertEquals(1, ((Number)result.get(0)).intValue());
        assertEquals(2, ((Number)result.get(1)).intValue());
        assertEquals(3, ((Number)result.get(2)).intValue());
    }
}
