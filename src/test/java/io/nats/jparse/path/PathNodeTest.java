package io.nats.jparse.path;

import io.nats.jparse.Path;
import io.nats.jparse.node.Node;
import io.nats.jparse.node.NodeType;
import io.nats.jparse.node.support.MockTokenSubList;
import io.nats.jparse.node.support.TokenSubList;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.source.Sources;
import io.nats.jparse.token.Token;
import io.nats.jparse.token.TokenTypes;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PathNodeTest {

    //...................0123456789
    final String path = "a.b[7]";

    // Create a root Token
    Token rootToken = new Token(0, path.length(), TokenTypes.PATH_KEY_TOKEN);

    // Create two child Tokens
    Token childToken1 = new Token(2, 3, TokenTypes.PATH_KEY_TOKEN);
    Token childToken2 = new Token(4, 5, TokenTypes.PATH_INDEX_TOKEN);

    CharSource source = Sources.stringSource(path);


    @Test
    public void childrenTokenTest() {
        TokenSubList tokens = MockTokenSubList.of(rootToken, childToken1, childToken2);
        PathNode pathNode = new PathNode(tokens, source);

        List<List<Token>> childrenTokens = pathNode.childrenTokens();

        assertEquals(3, childrenTokens.size());

        assertEquals(childToken1, childrenTokens.get(1).get(0));
        assertEquals(childToken2, childrenTokens.get(2).get(0));


    }

    @Test
    void getNodeTest() {
        // Create a mock TokenSubList
        TokenSubList tokens = MockTokenSubList.of(rootToken, childToken1, childToken2);

        // Create a new PathNode
        PathNode pathNode = new PathNode(tokens, source);

        // Get the Node at the key 0
        Node nodeAtKey0 = pathNode.getNode("0");

        // Assert that the returned Node is the expected one
        assertEquals(rootToken, nodeAtKey0.rootElementToken());
    }

    @Test
    void getNodeAtTest() {
        // Create a mock TokenSubList
        TokenSubList tokens = MockTokenSubList.of(rootToken, childToken1, childToken2);

        // Create a new PathNode
        PathNode pathNode = new PathNode(tokens, source);

        // Get the Node at the index 1
        Node nodeAtIndex1 = pathNode.getNodeAt(0);

        // Assert that the returned Node is the expected one
        assertEquals(rootToken, nodeAtIndex1.rootElementToken());
        assertEquals(childToken1, pathNode.getNodeAt(1).rootElementToken());
        assertEquals(childToken2, pathNode.getNodeAt(2).rootElementToken());
    }

    @Test
    void getPathIndexValueTest() {
        // Create a mock TokenSubList
        TokenSubList tokens = MockTokenSubList.of(rootToken, childToken1, childToken2);

// Create a new PathNode
        PathNode pathNode = new PathNode(tokens, source);

// Get the String value at index 0
        int value = pathNode.getPathIndexValue(2);

        assertEquals(7, value);

    }


    @Test
    void getPathKeyValueTest() {
        // Create a mock TokenSubList
        TokenSubList tokens = MockTokenSubList.of(rootToken, childToken1, childToken2);

        // Create a new PathNode
        PathNode pathNode = new PathNode(tokens, source);

        // Get the String value at index 0
        String value = pathNode.getPathKeyValue(1);

        assertEquals("b", value);

    }

    @Test
    void getKeyNodeTest() {
        // Create a mock TokenSubList
        TokenSubList tokens = MockTokenSubList.of(rootToken, childToken1, childToken2);

        // Create a new PathNode
        PathNode pathNode = new PathNode(tokens, source);

        // Get the KeyNode at index 0
        KeyPathNode node = pathNode.getKeyNode(1);

        assertEquals("b", node.toString());

    }

    @Test
    public void testRootElementToken() {

        final String path = "test.path";
        PathNode pathNode = Path.toPath(path);

        assertEquals(NodeType.PATH, pathNode.type());

        assertEquals(0, pathNode.rootElementToken().startIndex);
        assertEquals(4, pathNode.rootElementToken().endIndex);

        KeyPathNode keyNode = pathNode.getKeyNode(1);
        assertEquals(5, keyNode.rootElementToken().startIndex);
        assertEquals(path.length(), keyNode.rootElementToken().endIndex);

        assertEquals(path, pathNode.charSource().toString());

        assertEquals(2, pathNode.size());
        assertEquals(2, pathNode.length());

        assertEquals(pathNode, Path.toPath(path));

        assertEquals(pathNode.hashCode(), Path.toPath(path).hashCode());


        Iterator<PathElement> nodes = pathNode.iterator();


        PathElement pathElement = nodes.next();

        assertEquals(pathElement, pathNode.getNodeAt(0));






    }




}
