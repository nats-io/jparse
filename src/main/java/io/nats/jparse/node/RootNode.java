/*
 * Copyright 2013-2023 Richard M. Hightower
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.nats.jparse.node;

import io.nats.jparse.node.support.NodeUtils;
import io.nats.jparse.node.support.TokenList;
import io.nats.jparse.node.support.TokenSubList;
import io.nats.jparse.path.PathNode;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.token.Token;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static io.nats.jparse.token.TokenTypes.ARRAY_TOKEN;
import static io.nats.jparse.token.TokenTypes.OBJECT_TOKEN;


/**
 * The RootNode class represents the root node of a tree structure.
 * <p>
 * It serves as the entry point for accessing and manipulating the contents of the tree.
 * <p>
 * The root node can be either an object node or an array node.
 */
public class RootNode implements CollectionNode {

    private final TokenList tokens;
    private final CharSource source;
    private final Token rootToken;
    private final boolean objectsKeysCanBeEncoded;

    private Node root;

    /**
     * Constructs a RootNode with the specified tokens, character source, and objectsKeysCanBeEncoded flag.
     *
     * @param tokens                  the list of tokens representing the tree structure
     * @param source                  the character source from which the tokens were parsed
     * @param objectsKeysCanBeEncoded a flag indicating whether object keys can be encoded
     */
    public RootNode(TokenList tokens, CharSource source, boolean objectsKeysCanBeEncoded) {
        this.tokens = tokens;
        this.source = source;
        this.rootToken = tokens.get(0);
        this.objectsKeysCanBeEncoded = objectsKeysCanBeEncoded;
    }

    /**
     * Returns the type of the root node.
     *
     * @return the type of the root node
     */
    public NodeType getType() {
        return NodeType.tokenTypeToElement(rootToken.type);
    }

    /**
     * Returns the node associated with the specified key.
     * This method delegates the call to the appropriate method of the underlying node.
     *
     * @param key the key to retrieve the associated node
     * @return the node associated with the specified key
     */
    @Override
    public Node getNode(Object key) {
        switch (rootToken.type) {
            case OBJECT_TOKEN:
                return getObjectNode().getNode(key);
            case ARRAY_TOKEN:
                return getArrayNode().getNode(key);
            default:
                return doGetNode(key);
        }
    }

    /**
     * Returns the list of tokens representing the children of the root node.
     * This method delegates the call to the appropriate method of the underlying node.
     *
     * @return the list of tokens representing the children of the root node
     */
    @Override
    public List<List<Token>> childrenTokens() {
        switch (rootToken.type) {
            case OBJECT_TOKEN:
                return getObjectNode().childrenTokens();
            case ARRAY_TOKEN:
                return getArrayNode().childrenTokens();
            default:
                return doGetChildrenTokens();
        }
    }

    private List<List<Token>> doGetChildrenTokens() {
        return ((CollectionNode) getNode()).childrenTokens();
    }

    private Node doGetNode(Object key) {
        return ((CollectionNode) getNode()).getNode(key);
    }

    /**
     * Returns the root node of the tree.
     * If the root node has not been created yet, it will be lazily created.
     *
     * @return the root node of the tree
     */
    public Node getNode() {
        if (root == null) {
            root = NodeUtils.createNode(new TokenSubList(tokens.getTokens(), 0, tokens.size()), source, objectsKeysCanBeEncoded);
        }
        return root;
    }

    /**
     * Returns the path node associated with the root node.
     * If the root node has not been created yet, it will be lazily created.
     *
     * @return the path node associated with the root node
     */
    public PathNode getPathNode() {
        if (root == null) {
            root = new PathNode((TokenSubList) tokens.subList(0, tokens.size()), charSource());
        }
        return (PathNode) root;
    }

    /**
     * Returns the object node associated with the root node.
     *
     * @return the object node associated with the root node
     */
    public ObjectNode getObjectNode() {
        return (ObjectNode) getNode();
    }

    @Override
    public ArrayNode asArray() {
        return getArrayNode();
    }

    @Override
    public ObjectNode asObject() {
        return getObjectNode();
    }

    /**
     * Returns the map representation of the root node.
     * This method assumes that the root node is an object node.
     *
     * @return the map representation of the root node
     */
    public Map<String, Object> getMap() {
        return (Map<String, Object>) (Object) getObjectNode();
    }

    /**
     * Returns the string node associated with the root node.
     * This method assumes that the root node is a string node.
     *
     * @return the string node associated with the root node
     */
    public StringNode getStringNode() {
        return (StringNode) this.getNode();
    }

    /**
     * Returns the string representation of the root node.
     * This method assumes that the root node is a string node.
     *
     * @return the string representation of the root node
     */
    public String getString() {
        return getStringNode().toString();
    }

    /**
     * Returns the integer value of the root node.
     * This method assumes that the root node is a number node.
     *
     * @return the integer value of the root node
     */
    public int getInt() {
        return getNumberNode().intValue();
    }

    /**
     * Returns the float value of the root node.
     * This method assumes that the root node is a number node.
     *
     * @return the float value of the root node
     */
    public float getFloat() {
        return getNumberNode().floatValue();
    }

    /**
     * Returns the long value of the root node.
     * This method assumes that the root node is a number node.
     *
     * @return the long value of the root node
     */
    public long getLong() {
        return getNumberNode().longValue();
    }

    /**
     * Returns the double value of the root node.
     * This method assumes that the root node is a number node.
     *
     * @return the double value of the root node
     */
    public double getDouble() {
        return getNumberNode().doubleValue();
    }

    /**
     * Returns the BigDecimal value of the root node.
     * This method assumes that the root node is a number node.
     *
     * @return the BigDecimal value of the root node
     */
    public BigDecimal getBigDecimal() {
        return getNumberNode().bigDecimalValue();
    }

    /**
     * Returns the BigInteger value of the root node.
     * This method assumes that the root node is a number node.
     *
     * @return the BigInteger value of the root node
     */
    public BigInteger getBigIntegerValue() {
        return getNumberNode().bigIntegerValue();
    }

    /**
     * Returns the number node associated with the root node.
     * This method assumes that the root node is a number node.
     *
     * @return the number node associated with the root node
     */
    public NumberNode getNumberNode() {
        return (NumberNode) getNode();
    }

    /**
     * Returns the boolean node associated with the root node.
     * This method assumes that the root node is a boolean node.
     *
     * @return the boolean node associated with the root node
     */
    public BooleanNode getBooleanNode() {
        return (BooleanNode) getNode();
    }

    /**
     * Returns the null node associated with the root node.
     * This method assumes that the root node is a null node.
     *
     * @return the null node associated with the root node
     */
    public NullNode getNullNode() {
        return (NullNode) getNode();
    }

    /**
     * Returns the boolean value of the root node.
     * This method assumes that the root node is a boolean node.
     *
     * @return the boolean value of the root node
     */
    public boolean getBoolean() {
        return getBooleanNode().booleanValue();
    }

    /**
     * Get this as an array node.
     * @return array node
     */
    public ArrayNode getArrayNode() {
        return (ArrayNode) getNode();
    }

    /**
     * Returns the type of the root node, which is ROOT.
     *
     * @return the type of the root node
     */
    @Override
    public NodeType type() {
        return NodeType.ROOT;
    }

    /**
     * Returns the list of tokens associated with the root node.
     *
     * @return the list of tokens associated with the root node
     */
    @Override
    public List<Token> tokens() {
        return this.tokens;
    }

    /**
     * Returns the root element token of the root node.
     *
     * @return the root element token of the root node
     */
    @Override
    public Token rootElementToken() {
        return rootToken;
    }

    /**
     * Returns the character source from which the tokens were parsed.
     *
     * @return the character source from which the tokens were parsed
     */
    @Override
    public CharSource charSource() {
        return source;
    }

    /**
     * Checks if this root node is equal to the specified object.
     * Two root nodes are considered equal if their underlying nodes are equal.
     *
     * @param o the object to compare with
     * @return {@code true} if this root node is equal to the specified object, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof RootNode) {
            RootNode other = (RootNode) o;
            return getNode().equals(other.getNode());
        }
        return false;
    }

    /**
     * Returns the hash code value for this root node.
     *
     * @return the hash code value for this root node
     */
    @Override
    public int hashCode() {
        return getNode().hashCode();
    }
}
