package com.cloudurable.jparse;

import com.cloudurable.jparse.node.*;
import com.cloudurable.jparse.path.PathElement;
import com.cloudurable.jparse.path.PathNode;
import com.cloudurable.jparse.path.PathParser;

import java.util.Iterator;

public class Path {

    public static int intAtPath(final String path, final Node rootNode) {
        return numberNodeAtPath(path, rootNode).intValue();
    }

    public static float floatAtPath(final String path, final Node rootNode) {
        return numberNodeAtPath(path, rootNode).floatValue();
    }

    public static double doubleAtPath(final String path, final Node rootNode) {
        return numberNodeAtPath(path, rootNode).doubleValue();
    }

    public static long longAtPath(final String path, final Node rootNode) {
        return numberNodeAtPath(path, rootNode).longValue();
    }

    public static CharSequence charSequenceAtPath(final String path, final Node rootNode) {
        return stringNodeAtPath(path, rootNode).charSequence();
    }

    public static CharSequence stringAtPath(final String path, final Node rootNode) {
        return stringNodeAtPath(path, rootNode).toString();
    }

    private static NumberNode numberNodeAtPath(String path, Node rootNode) {
        return (NumberNode) atPath(path, rootNode);
    }

    private static StringNode stringNodeAtPath(String path, Node rootNode) {
        return (StringNode) atPath(path, rootNode);
    }

    public static Node atPath(final String path, final String json) {
        return atPath(path, Json.toRootNode(json));
    }

    public static Node atPath(final String path, final Node rootNode) {
        return atPath(toPath(path), rootNode);
    }

    public static Node atPath(final PathNode path, final Node rootNode) {
        Iterator<PathElement> iterator = path.iterator();
        Node node = rootNode;
        PathElement pathElement = null;
        try {
            while (iterator.hasNext()) {

                pathElement = iterator.next();

                switch (node.type()) {
                    case OBJECT:
                        final ObjectNode objectNode = (ObjectNode) node;
                        final var key = pathElement.asKey().toCharSequence();
                        node = objectNode.getNode(key);
                        break;
                    case ARRAY:
                        final ArrayNode arrayNode = (ArrayNode) node;
                        node = arrayNode.getNodeAt(pathElement.asIndex().intValue());
                        break;
                    default:
                        if (node.isCollection()) {
                            node = node.asCollection().getNode(pathElement.asKey().toCharSequence());
                        } else {
                            throw new IllegalStateException("Path not found at " + path + " path element key " + pathElement.asKey().toString());
                        }
                }

            }
        } catch (Exception ex) {
            throw new IllegalStateException("Path not found at " + path + " path element index " + pathElement.value());
        }
        return node;

    }

    public static PathNode toPath(String path) {
        final var pathParser = new PathParser();
        return pathParser.parse(path).getPathNode();
    }
}
