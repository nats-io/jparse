package com.cloudurable.jparse;

import com.cloudurable.jparse.node.Node;
import com.cloudurable.jparse.node.NodeType;
import com.cloudurable.jparse.node.RootNode;
import com.cloudurable.jparse.node.StringNode;
import com.cloudurable.jparse.source.Sources;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EqualityTest {


    @Test
    void testRoot() {
        final Parser parser = new JsonParser();
        final String json1 = "'abc'";
        final String json2 = "'abc'";

        final var v1 = getJsonRoot(parser, json1);
        final var v2 = getJsonRoot(parser, json2);
        doAssert(v1, v2);
    }

    @Test
    void testSimpleString() {
        final Parser parser = new JsonParser();
        final String json1 = "'abc'";
        final String json2 = "'abc'";

        final StringNode v1 = getJsonRoot(parser, json1).getStringNode();
        final StringNode v2 = getJsonRoot(parser, json2).getStringNode();
        doAssert(v1, v2);

        final String s1 = getJsonRoot(parser, json1).getString();
        final String s2 = getJsonRoot(parser, json2).getString();
        assertEquals(s1, s2);
    }

    @Test
    void testSimpleInt() {
        final Parser parser = new JsonParser();
        final String json1 = "1";
        final String json2 = "1";
        var v1 = getJsonRoot(parser, json1).getNumberNode();
        var v2 = getJsonRoot(parser, json2).getNumberNode();
        doAssert(v1, v2);
    }

    @Test
    void testSimpleArray() {
        final Parser parser = new JsonParser();
        final String json1 = "[1,2,3,true,false]";
        final String json2 = "[1,2,3,true,false]";
        var v1 = getJsonRoot(parser, json1).getArrayNode();
        var v2 = getJsonRoot(parser, json2).getArrayNode();
        doAssert(v1, v2);
    }

    @Test
    void testSimpleObject() {
        final Parser parser = new JsonParser();
        final String json1 = "{'1':2}";
        final String json2 = "{'1':2}";
        var v1 = getJsonRoot(parser, json1).getObjectNode();
        var v2 = getJsonRoot(parser, json2).getObjectNode();
        doAssert(v1, v2);
    }

    @Test
    void testSimpleObject2() {
        final Parser parser = new JsonParser();
        final String json1 = "{'1':2,'2':1}";
        final String json2 = "{'2':1,'1':2}";
        var v1 = getJsonRoot(parser, json1).getObjectNode();
        var v2 = getJsonRoot(parser, json2).getObjectNode();
        doAssert(v1, v2);
    }

    private RootNode getJsonRoot(Parser parser, String json1) {
        return parser.parse(Sources.stringSource(json1.replace("'", "\"")));
    }

    private void doAssert(Node v1, Node v2) {
        v1.charSource();
        assertEquals(v1, v2);
        assertEquals(v1.rootElementToken().type, v2.rootElementToken().type);
        assertEquals(v1.type(), v2.type());
        assertEquals(v1.hashCode(), v2.hashCode());
        assertEquals(v1.charAt(0), v2.charAt(0));
        assertEquals(v1.length(), v2.length());
        assertEquals(v1.tokens().size(), v2.tokens().size());

        if (v1.type() != NodeType.OBJECT)
            assertEquals(v1.subSequence(0, v1.length()), v2.subSequence(0, v2.length()));
    }

}
