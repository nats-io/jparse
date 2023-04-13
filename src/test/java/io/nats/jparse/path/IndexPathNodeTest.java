package io.nats.jparse.path;

import io.nats.jparse.node.NodeType;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.source.Sources;
import io.nats.jparse.token.Token;
import io.nats.jparse.token.TokenTypes;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class IndexPathNodeTest {

    Token token = new Token(0, 3, TokenTypes.PATH_INDEX_TOKEN);
    CharSource charSource = Sources.stringSource("100");


    @Test
    public void testType() {
        IndexPathNode indexPathNode = new IndexPathNode(token, charSource);
        assertEquals(NodeType.PATH_INDEX, indexPathNode.type());
    }

    @Test
    public void testTokens() {
        IndexPathNode indexPathNode = new IndexPathNode(token, charSource);
        assertEquals(Collections.singletonList(token), indexPathNode.tokens());
    }

    @Test
    public void testRootElementToken() {
        IndexPathNode indexPathNode = new IndexPathNode(token, charSource);
        assertEquals(token, indexPathNode.rootElementToken());
    }

    @Test
    public void testCharSource() {
        IndexPathNode indexPathNode = new IndexPathNode(token, charSource);
        assertEquals(charSource, indexPathNode.charSource());
    }

    @Test
    public void testValue() {
        IndexPathNode indexPathNode = new IndexPathNode(token, charSource);
        assertEquals(100, indexPathNode.value());
    }

    @Test
    public void testIsIndex() {
        IndexPathNode indexPathNode = new IndexPathNode(token, charSource);
        assertTrue(indexPathNode.isIndex());
    }

    @Test
    public void testIsKey() {
        IndexPathNode indexPathNode = new IndexPathNode(token, charSource);
        assertFalse(indexPathNode.isKey());
    }

    @Test
    public void testToCharSequence() {
        IndexPathNode indexPathNode = new IndexPathNode(token, charSource);
        assertEquals(indexPathNode.originalCharSequence(), indexPathNode.charSequenceValue().toString());
    }

}
