package io.nats.jparse.path;


import io.nats.jparse.node.NodeType;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.source.Sources;
import io.nats.jparse.token.Token;
import io.nats.jparse.token.TokenTypes;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class KeyPathNodeTest {


    Token token = new Token(1, 5, TokenTypes.PATH_KEY_TOKEN);
    CharSource charSource = Sources.stringSource("a test Char Source");

    @Test
    public void testType() {

        KeyPathNode keyPathNode = new KeyPathNode(token, charSource);
        assertEquals(NodeType.PATH_KEY, keyPathNode.type());
    }

    @Test
    public void testTokens() {
        KeyPathNode keyPathNode = new KeyPathNode(token, charSource);
        assertEquals(Collections.singletonList(token), keyPathNode.tokens());
    }

    @Test
    public void testRootElementToken() {
        KeyPathNode keyPathNode = new KeyPathNode(token, charSource);
        assertEquals(token, keyPathNode.rootElementToken());
    }

    @Test
    public void testCharSource() {
        KeyPathNode keyPathNode = new KeyPathNode(token, charSource);
        assertEquals(charSource, keyPathNode.charSource());
    }

    @Test
    public void testValue() {
        KeyPathNode keyPathNode = new KeyPathNode(token, charSource);
        assertEquals(keyPathNode.toString(), keyPathNode.value());
    }

    @Test
    public void testIsIndex() {
        KeyPathNode keyPathNode = new KeyPathNode(token, charSource);
        assertFalse(keyPathNode.isIndex());
    }

    @Test
    public void testIsKey() {
        KeyPathNode keyPathNode = new KeyPathNode(token, charSource);
        assertTrue(keyPathNode.isKey());
    }

    @Test
    public void testToString() {
        KeyPathNode keyPathNode = new KeyPathNode(token, charSource);
        assertEquals(keyPathNode.originalString(), keyPathNode.toString());
    }

    @Test
    public void testToCharSequence() {
        KeyPathNode keyPathNode = new KeyPathNode(token, charSource);
        assertEquals(keyPathNode.originalCharSequence(), keyPathNode.toCharSequence());
    }
}
