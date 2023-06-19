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
import io.nats.jparse.node.support.TokenSubList;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.token.Token;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The ArrayNode class represents an array node in a tree structure.
 * <p>
 * It extends the AbstractList class and implements the CollectionNode interface.
 */
public class ArrayNode extends AbstractList<Node> implements CollectionNode {

    private final TokenSubList tokens;
    private final CharSource source;
    private final Token rootToken;
    private final boolean objectsKeysCanBeEncoded;
    private int hashCode;
    private List<List<Token>> childrenTokens;
    private Node[] elements;
    private boolean hashCodeSet;

    /**
     * Constructs an ArrayNode with the specified tokens, source, and objectsKeysCanBeEncoded flag.
     *
     * @param tokens                  the sublist of tokens representing the array node
     * @param source                  the character source containing the array node
     * @param objectsKeysCanBeEncoded flag indicating if object keys can be encoded
     */
    public ArrayNode(final TokenSubList tokens, final CharSource source, boolean objectsKeysCanBeEncoded) {
        this.tokens = tokens;
        this.rootToken = tokens.get(0);
        this.source = source;
        this.objectsKeysCanBeEncoded = objectsKeysCanBeEncoded;
    }

    /**
     * Returns a list containing the children tokens of the array node.
     *
     * @return a list containing the children tokens of the array node
     */
    @Override
    public List<List<Token>> childrenTokens() {
        if (childrenTokens == null) {
            childrenTokens = NodeUtils.getChildrenTokens(tokens);
        }
        return childrenTokens;
    }

    Node[] elements() {
        if (elements == null) {
            elements = new Node[childrenTokens().size()];
        }
        return elements;
    }

    /**
     * Returns the node associated with the specified key in the array node.
     *
     * @param key the key to retrieve the node for
     * @return the node associated with the specified key
     */
    @Override
    public Node getNode(Object key) {
        return key instanceof String ?
                this.getNodeAt(Integer.valueOf((String) key)) :
                this.getNodeAt((Integer) key);
    }

    /**
     * Returns the node at the specified index in the array node.
     *
     * @param index the index of the node to retrieve
     * @return the node at the specified index
     */
    public Node getNodeAt(int index) {
        Node element = elements()[index];
        if (element == null) {
            List<Token> tokens = childrenTokens().get(index);
            elements()[index] = NodeUtils.createNode(tokens, source, objectsKeysCanBeEncoded);
        }
        return elements()[index];
    }

    /**
     * Returns an optional containing the node at the specified index in the array node.
     *
     * @param index the index of the node to retrieve
     * @return an optional containing the node at the specified index, or an empty optional if no node exists at the index
     */
    public Optional<Node> lookupNodeAt(int index) {
        return Optional.ofNullable(getNodeAt(index));
    }

    /**
     * Returns the long value of the node at the specified index in the array node.
     *
     * @param index the index of the node to retrieve
     * @return the long value of the node at the specified index
     */
    public long getLong(int index) {
        return getNumberNode(index).longValue();
    }

    /**
     * Returns the double value of the node at the specified index in the array node.
     *
     * @param index the index of the node to retrieve
     * @return the double value of the node at the specified index
     */
    public double getDouble(int index) {
        return getNumberNode(index).doubleValue();
    }

    /**
     * Returns an array containing the double values of the nodes in the array node.
     *
     * @return an array containing the double values of the nodes in the array node
     */
    public double[] getDoubleArray() {
        int length = length();
        double[] array = new double[length];
        for (int i = 0; i < length; i++) {
            final Token token = tokens.get(i + 1);
            array[i] = source.getDouble(token.startIndex, token.endIndex);
        }
        return array;
    }

    /**
     * Returns an array containing the float values of the nodes in the array node.
     *
     * @return an array containing the float values of the nodes in the array node
     */
    public float[] getFloatArray() {
        int length = length();
        float[] array = new float[length];
        for (int i = 0; i < length; i++) {
            final Token token = tokens.get(i + 1);
            array[i] = source.getFloat(token.startIndex, token.endIndex);
        }
        return array;
    }

    /**
     * Returns an array containing the BigDecimal values of the nodes in the array node.
     *
     * @return an array containing the BigDecimal values of the nodes in the array node
     */
    public BigDecimal[] getBigDecimalArray() {
        int length = length();
        BigDecimal[] array = new BigDecimal[length];
        for (int i = 0; i < length; i++) {
            final Token token = tokens.get(i + 1);
            array[i] = source.getBigDecimal(token.startIndex, token.endIndex);
        }
        return array;
    }

    /**
     * Returns an array containing the BigInteger values of the nodes in the array node.
     *
     * @return an array containing the BigInteger values of the nodes in the array node
     */
    public BigInteger[] getBigIntegerArray() {
        int length = length();
        BigInteger[] array = new BigInteger[length];
        for (int i = 0; i < length; i++) {
            final Token token = tokens.get(i + 1);
            array[i] = source.getBigDecimal(token.startIndex, token.endIndex).toBigInteger();
        }
        return array;
    }

    /**
     * Returns an array containing the int values of the nodes in the array node.
     *
     * @return an array containing the int values of the nodes in the array node
     */
    public int[] getIntArray() {
        int length = length();
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            final Token token = tokens.get(i + 1);
            array[i] = source.getInt(token.startIndex, token.endIndex);
        }
        return array;
    }

    /**
     * Returns an array containing the long values of the nodes in the array node.
     *
     * @return an array containing the long values of the nodes in the array node
     */
    public long[] getLongArray() {
        int length = length();
        long[] array = new long[length];
        for (int i = 0; i < length; i++) {
            final Token token = tokens.get(i + 1);
            array[i] = source.getLong(token.startIndex, token.endIndex);
        }
        return array;
    }

    /**
     * Returns the null node at the specified index in the array node.
     *
     * @param index the index of the null node to retrieve
     * @return the null node at the specified index
     */
    public int getInt(int index) {
        return getNumberNode(index).intValue();
    }

    /**
     * Returns the null node at the specified index in the array node.
     *
     * @param index the index of the null node to retrieve
     * @return the null node at the specified index
     */
    public NullNode getNullNode(int index) {
        return (NullNode) getNodeAt(index);
    }

    /**
     * Returns the float value at the specified index in the array.
     *
     * @param index the index of the float value
     * @return the float value at the specified index
     */
    public float getFloat(int index) {
        return getNumberNode(index).floatValue();
    }

    /**
     * Returns the NumberNode at the specified index in the array.
     *
     * @param index the index of the NumberNode
     * @return the NumberNode at the specified index
     */
    public NumberNode getNumberNode(int index) {
        return (NumberNode) getNodeAt(index);
    }

    /**
     * Returns the BigDecimal value at the specified index in the array.
     *
     * @param index the index of the BigDecimal value
     * @return the BigDecimal value at the specified index
     */
    public BigDecimal getBigDecimal(int index) {
        return getNumberNode(index).bigDecimalValue();
    }

    /**
     * Returns the BigInteger value at the specified index in the array.
     *
     * @param index the index of the BigInteger value
     * @return the BigInteger value at the specified index
     */
    public BigInteger getBigInteger(int index) {
        return getNumberNode(index).bigIntegerValue();
    }

    /**
     * Returns the StringNode at the specified index in the array.
     *
     * @param index the index of the StringNode
     * @return the StringNode at the specified index
     */
    public StringNode getStringNode(int index) {
        return (StringNode) getNodeAt(index);
    }

    /**
     * Returns the string value at the specified index in the array.
     *
     * @param index the index of the string value
     * @return the string value at the specified index
     */
    public String getString(int index) {
        return getStringNode(index).toString();
    }

    /**
     * Returns the ObjectNode at the specified index in the array.
     *
     * @param index the index of the ObjectNode
     * @return the ObjectNode at the specified index
     */
    public ObjectNode getObjectNode(int index) {
        return (ObjectNode) getNodeAt(index);
    }

    /**
     * Returns the ArrayNode at the specified index in the array.
     *
     * @param index the index of the ArrayNode
     * @return the ArrayNode at the specified index
     */
    public ArrayNode getArray(int index) {
        return (ArrayNode) getNodeAt(index);
    }

    /**
     * Returns the BooleanNode at the specified index in the array.
     *
     * @param index the index of the BooleanNode
     * @return the BooleanNode at the specified index
     */
    public BooleanNode getBooleanNode(int index) {
        return (BooleanNode) getNodeAt(index);
    }

    /**
     * Returns the boolean value at the specified index in the array.
     *
     * @param index the index of the boolean value
     * @return the boolean value at the specified index
     */
    public boolean getBoolean(int index) {
        return getBooleanNode(index).booleanValue();
    }

    /**
     * Returns the length of the array.
     *
     * @return the length of the array
     */
    public int length() {
        return elements().length;
    }

    /**
     * Returns the type of the node, which is NodeType.ARRAY.
     *
     * @return the type of the node
     */
    @Override
    public NodeType type() {
        return NodeType.ARRAY;
    }

    /**
     * Returns the list of tokens representing the array.
     *
     * @return the list of tokens representing the array
     */
    @Override
    public List<Token> tokens() {
        return tokens;
    }

    /**
     * Returns the root token of the array.
     *
     * @return the root token of the array
     */
    @Override
    public Token rootElementToken() {
        return rootToken;
    }

    /**
     * Returns the character source of the array.
     *
     * @return the character source of the array
     */
    @Override
    public CharSource charSource() {
        return source;
    }

    /**
     * Returns the node at the specified index in the array.
     * If the node at the index is of type NodeType.NULL, returns null.
     *
     * @param index the index of the node
     * @return the node at the specified index, or null if it is of type NodeType.NULL
     */
    @Override
    public Node get(int index) {
        final Node node = getNodeAt(index);
        return node.type() == NodeType.NULL ? null : node;
    }

    /**
     * Checks if this ArrayNode is equal to the specified object.
     * <p>
     * Two ArrayNodes are considered equal if they have the same tokens.
     *
     * @param o the object to compare
     * @return true if the ArrayNodes are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArrayNode)) return false;

        final ArrayNode other = (ArrayNode) o;

        if (this.tokens.size() != other.tokens.size()) {
            return false;
        }

        for (int index = 0; index < this.tokens.size(); index++) {
            Token thisValue = this.tokens.get(index);
            Token otherValue = other.tokens.get(index);
            if (otherValue == null && thisValue == null) continue;
            String thisStr = thisValue.asString(this.source);
            String otherStr = otherValue.asString(other.source);
            if (!thisStr.equals(otherStr)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the hash code value for the ArrayNode.
     * The hash code is computed based on the tokens of the ArrayNode.
     *
     * @return the hash code value for the ArrayNode
     */
    @Override
    public int hashCode() {
        if (hashCodeSet) {
            return hashCode;
        }
        hashCode = Objects.hash(tokens.stream().map(tok -> tok.asString(this.source)).collect(Collectors.toList()));
        hashCodeSet = true;
        return hashCode;
    }

    /**
     * Returns the size of the array, which is the number of elements it contains.
     *
     * @return the size of the array
     */
    @Override
    public int size() {
        return childrenTokens().size();
    }

    /**
     * Returns a string representation of the ArrayNode.
     *
     * @return a string representation of the ArrayNode
     */
    @Override
    public String toString() {
        return this.originalString();
    }

    /**
     * Maps the ObjectNode elements in the array to a new list of type R
     * using the provided mapper function.
     *
     * @param mapper the mapper function to apply to each ObjectNode
     * @param <R>    the type of the resulting list elements
     * @return a new list of type R resulting from the mapping
     */
    public <R> List<R> mapObjectNode(Function<ObjectNode, ? extends R> mapper) {
        return map(node -> mapper.apply(node.asCollection().asObject()));
    }

    /**
     * Maps the elements in the array to a new list of type R using the provided mapper function.
     *
     * @param mapper the mapper function to apply to each element
     * @param <R>    the type of the resulting list elements
     * @return a new list of type R resulting from the mapping
     */
    public <R> List<R> map(Function<Node, ? extends R> mapper) {
        List<R> list = new ArrayList<>(this.size());
        Node[] elements = elements();
        for (int i = 0; i < elements.length; i++) {
            Node element = elements[i];
            if (element == null) {
                element = getNodeAt(i);
                elements[i] = element;
            }
            list.add(mapper.apply(element));
        }
        return list;
    }

    /**
     * Finds the first ObjectNode in the array that matches the specified predicate.
     *
     * @param predicate the predicate to apply to each ObjectNode
     * @return an Optional containing the first matching ObjectNode, or an empty Optional if no match is found
     */
    public Optional<ObjectNode> findObjectNode(Predicate<ObjectNode> predicate) {
        final Node[] elements = elements();
        ObjectNode node = null;
        for (int i = 0; i < elements.length; i++) {
            Node element = elements[i];
            if (element == null) {
                element = getNodeAt(i);
            }
            if (element.type() == NodeType.OBJECT) {
                ObjectNode objectNode = element.asCollection().asObject();
                if (predicate.test(objectNode)) {
                    node = objectNode;
                    break;
                }
            }
        }
        return Optional.ofNullable(node);
    }

    /**
     * Finds the first Node in the array that matches the specified predicate.
     *
     * @param predicate the predicate to apply to each Node
     * @return an Optional containing the first matching Node, or an empty Optional if no match is found
     */
    public Optional<Node> find(Predicate<Node> predicate) {
        Node[] elements = elements();
        Node node = null;
        for (int i = 0; i < elements.length; i++) {
            Node element = elements[i];
            if (element == null) {
                element = getNodeAt(i);
            }
            if (predicate.test(element)) {
                node = element;
                break;
            }
        }
        return Optional.ofNullable(node);
    }

    /**
     * Filters the ObjectNode elements in the array based on the specified predicate.
     *
     * @param predicate the predicate to apply to each ObjectNode
     * @return a new list containing the filtered ObjectNodes
     */
    public List<ObjectNode> filterObjects(Predicate<ObjectNode> predicate) {
        Node[] elements = elements();
        final int length = elements.length;
        final List<ObjectNode> arrayList = new ArrayList<>(length / 2);
        for (int i = 0; i < length; i++) {
            Node element = elements[i];
            if (element == null) {
                element = getNodeAt(i);
            }
            if (element.type() == NodeType.OBJECT) {
                ObjectNode objectNode = element.asCollection().asObject();
                if (predicate.test(objectNode)) {
                    arrayList.add(objectNode);
                }
            }
        }
        return arrayList;
    }

    /**
     * Filters the elements in the array based on the specified predicate.
     *
     * @param predicate the predicate to apply to each Node
     * @return a new list containing the filtered Nodes
     */
    public List<Node> filter(Predicate<Node> predicate) {
        Node[] elements = elements();
        final int length = elements.length;
        final List<Node> arrayList = new ArrayList<>(length / 2);
        for (int i = 0; i < length; i++) {
            Node element = elements[i];
            if (element == null) {
                element = getNodeAt(i);
            }
            if (predicate.test(element)) {
                arrayList.add(element);
            }
        }
        return arrayList;
    }


}
