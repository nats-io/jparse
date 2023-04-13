package io.nats.jparse.parser.functable;

import io.nats.jparse.node.support.TokenList;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.source.Sources;
import io.nats.jparse.source.support.UnexpectedCharacterException;
import io.nats.jparse.token.TokenTypes;
import org.junit.jupiter.api.Test;

import static io.nats.jparse.parser.functable.JsonParserFunctions.parseKeyWithEncode;
import static org.junit.jupiter.api.Assertions.*;

class JsonParserFunctionsTest {

    @Test
    public void parseKeyWithEncode_GivenStringStartToken_ShouldReturnTrue() {
        TokenList tokens = new TokenList();
        CharSource source = Sources.stringSource("\"key\": \"value\"}");

       parseKeyWithEncode(source, tokens);
        assertEquals(2, tokens.size());
        assertEquals(TokenTypes.STRING_TOKEN, tokens.get(1).type);
    }

    @Test
    public void parseKeyWithEncode_GivenObjectEndToken_ShouldReturnTrue() {
        TokenList tokens = new TokenList();
        CharSource source = Sources.stringSource("}");

        assertTrue(parseKeyWithEncode(source, tokens));
        assertEquals(0, tokens.size());
    }

    @Test
    public void parseKeyWithEncode_GivenUnexpectedCharacter_ShouldThrowUnexpectedCharacterException() {
        TokenList tokens = new TokenList();
        CharSource source = Sources.stringSource("#}");

        assertThrows(UnexpectedCharacterException.class, () -> parseKeyWithEncode(source, tokens));

    }

    @Test
    public void parseKeyWithEncode_GivenAttributeSeparator_ShouldReturnTrueAndAddAttributeKeyToken() {
        TokenList tokens = new TokenList();
        CharSource source = Sources.stringSource("\"key\": }");

        parseKeyWithEncode(source, tokens);
        assertEquals(2, tokens.size());
        assertEquals(TokenTypes.ATTRIBUTE_KEY_TOKEN, tokens.get(0).type);
    }

    @Test
    public void parseKeyWithEncode_GivenNoAttributeSeparator_ShouldThrowUnexpectedCharacterException() {
        TokenList tokens = new TokenList();
        CharSource source = Sources.stringSource("{\"key\"\"value\"}");
        assertThrows(UnexpectedCharacterException.class, () -> parseKeyWithEncode(source, tokens));

    }


}
