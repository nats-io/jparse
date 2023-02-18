package com.cloudurable.jparse.node;

import com.cloudurable.jparse.node.support.NodeUtils;
import com.cloudurable.jparse.node.support.TokenList;
import com.cloudurable.jparse.node.support.TokenSubList;
import com.cloudurable.jparse.path.PathNode;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.Token;
import static com.cloudurable.jparse.token.TokenTypes.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;


public class RootNode implements CollectionNode {

    private final TokenList tokens;
    private final CharSource source;
    private final Token rootToken;
    private final boolean objectsKeysCanBeEncoded;

    private Node root;

    public RootNode(TokenList tokens, CharSource source, boolean objectsKeysCanBeEncoded) {
        this.tokens = tokens;
        this.source = source;
        this.rootToken = tokens.get(0);
        this.objectsKeysCanBeEncoded = objectsKeysCanBeEncoded;
    }

    public NodeType getType() {
        return NodeType.tokenTypeToElement(rootToken.type());
    }

    @Override
    public Node getNode(Object key) {
        return switch (rootToken.type()) {
            case OBJECT_TOKEN -> getObjectNode().getNode(key);
            case ARRAY_TOKEN -> getArrayNode().getNode(key);
            default -> doGetNode(key);
        };
    }

    @Override
    public List<List<Token>> childrenTokens() {
        return switch (rootToken.type()) {
            case OBJECT_TOKEN -> getObjectNode().childrenTokens();
            case ARRAY_TOKEN -> getArrayNode().childrenTokens();
            default -> doGetChildrenTokens();
        };
    }

    private List<List<Token>> doGetChildrenTokens() {
            return ((CollectionNode) getNode()).childrenTokens();
    }

    private Node doGetNode(Object key) {
            return ((CollectionNode) getNode()).getNode(key);
    }

    public Node getNode() {
        if (root == null) {
            root = NodeUtils.createNode(new TokenSubList(tokens.getTokens(), 0, tokens.size()), source, objectsKeysCanBeEncoded);
        }
        return root;
    }

    public PathNode getPathNode() {
        if (root == null) {
            root = new PathNode((TokenSubList) tokens.subList(0, tokens.size()), charSource());
        }
        return (PathNode) root;
    }

    public ObjectNode getObjectNode() {
        return (ObjectNode) getNode();
    }

    public Map<String, Object> getMap() {
        return (Map<String, Object>) (Object) getObjectNode();
    }

    public StringNode getStringNode() {
        return (StringNode) this.getNode();
    }

    public String getString() {
        return getStringNode().toString();
    }

    public int getInt() {
        return getNumberNode().intValue();
    }

    public float getFloat() {
        return getNumberNode().floatValue();
    }

    public long getLong() {
        return getNumberNode().longValue();
    }

    public double getDouble() {
        return getNumberNode().doubleValue();
    }

    public BigDecimal getBigDecimal() {
        return getNumberNode().bigDecimalValue();
    }

    public BigInteger getBigIntegerValue() {
        return getNumberNode().bigIntegerValue();
    }

    public NumberNode getNumberNode() {
        return (NumberNode) getNode();
    }

    public BooleanNode getBooleanNode() {
        return (BooleanNode) getNode();
    }

    public NullNode getNullNode() {
        return (NullNode) getNode();
    }

    public boolean getBoolean() {
        return getBooleanNode().booleanValue();
    }

    public ArrayNode getArrayNode() {
        return (ArrayNode) getNode();
    }


    @Override
    public NodeType type() {
        return NodeType.ROOT;
    }

    @Override
    public List<Token> tokens() {
        return this.tokens;
    }

    @Override
    public Token rootElementToken() {
        return rootToken;
    }

    @Override
    public CharSource charSource() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RootNode) {
            RootNode other = (RootNode) o;
            return getNode().equals(other.getNode());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getNode().hashCode();
    }


}
