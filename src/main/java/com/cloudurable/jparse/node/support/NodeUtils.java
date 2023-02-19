package com.cloudurable.jparse.node.support;

import com.cloudurable.jparse.node.*;
import com.cloudurable.jparse.path.IndexPathNode;
import com.cloudurable.jparse.path.KeyPathNode;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeUtils {


    public static List<List<Token>> getChildrenTokens(final TokenSubList tokens) {

        final var root = tokens.get(0);
        final List<List<Token>> childrenTokens;
        childrenTokens = new ArrayList<>(16);

        for (int index = 1; index < tokens.size(); index++) {
            Token token = tokens.get(index);

            if (token.startIndex > root.endIndex) {
                break;
            }

            if (token.type <= 3) {

                int childCount = tokens.countChildren(index, token);
                int endIndex = index + childCount;
                childrenTokens.add(tokens.subList(index, endIndex));
                index = endIndex - 1;

            } else {
                childrenTokens.add(Collections.singletonList(token));
            }

        }

        return childrenTokens;
    }

    public static Node createNode(final List<Token> tokens, final CharSource source, boolean objectsKeysCanBeEncoded) {

        final NodeType nodeType = NodeType.tokenTypeToElement(tokens.get(0).type);

        return switch (nodeType) {
            case ARRAY -> new ArrayNode((TokenSubList) tokens, source, objectsKeysCanBeEncoded);
            case INT -> new NumberNode(tokens.get(0), source, NodeType.INT);
            case FLOAT -> new NumberNode(tokens.get(0), source, NodeType.FLOAT);
            case OBJECT -> new ObjectNode((TokenSubList) tokens, source, objectsKeysCanBeEncoded);
            case STRING -> new StringNode(tokens.get(0), source);
            case BOOLEAN -> new BooleanNode(tokens.get(0), source);
            case NULL -> new NullNode(tokens.get(0), source);
            case PATH_INDEX -> new IndexPathNode(tokens.get(0), source);
            case PATH_KEY -> new KeyPathNode(tokens.get(0), source);
            default -> throw new IllegalStateException();
        };
    }


    public static Node createNodeForObject(final List<Token> theTokens, final CharSource source, boolean objectsKeysCanBeEncoded) {

        final var rootToken = theTokens.get(1);
        final var tokens = theTokens.subList(1, theTokens.size());
        final NodeType nodeType = NodeType.tokenTypeToElement(rootToken.type);

        return switch (nodeType) {
            case ARRAY -> new ArrayNode((TokenSubList) tokens, source, objectsKeysCanBeEncoded);
            case INT -> new NumberNode(tokens.get(0), source, NodeType.INT);
            case FLOAT -> new NumberNode(tokens.get(0), source, NodeType.FLOAT);
            case OBJECT -> new ObjectNode((TokenSubList) tokens, source, objectsKeysCanBeEncoded);
            case STRING -> new StringNode(tokens.get(0), source);
            case BOOLEAN -> new BooleanNode(tokens.get(0), source);
            case NULL -> new NullNode(tokens.get(0), source);
            default -> throw new IllegalStateException();
        };
    }


}
