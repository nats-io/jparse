package com.cloudurable.jparse.node;

import com.cloudurable.jparse.node.support.NodeUtils;
import com.cloudurable.jparse.node.support.TokenSubList;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.Token;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ArrayNode extends AbstractList<Node> implements CollectionNode {

    private final TokenSubList tokens;
    private final CharSource source;
    private final Token rootToken;
    private final boolean objectsKeysCanBeEncoded;
    private int hashCode;
    private List<List<Token>> childrenTokens;
    private Node[] elements;
    private boolean hashCodeSet;

    public ArrayNode(final TokenSubList tokens, final CharSource source, boolean objectsKeysCanBeEncoded) {
        this.tokens = tokens;
        this.rootToken = tokens.get(0);
        this.source = source;
        this.objectsKeysCanBeEncoded = objectsKeysCanBeEncoded;
    }


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

    @Override
    public Node getNode(Object key) {
        return key instanceof String ?
                this.getNodeAt(Integer.valueOf((String) key)) :
                this.getNodeAt((Integer) key);
    }

    public Node getNodeAt(int index) {
        Node element = elements()[index];
        if (element == null) {
            List<Token> tokens = childrenTokens().get(index);
            elements()[index] = NodeUtils.createNode(tokens, source, objectsKeysCanBeEncoded);
        }
        return elements()[index];
    }

    public Optional<Node> lookupNodeAt(int index) {
       return Optional.ofNullable(getNodeAt(index));
    }

    public long getLong(int index) {
        return getNumberNode(index).longValue();
    }

    public double getDouble(int index) {
        return getNumberNode(index).doubleValue();
    }

    public double[] getDoubleArray() {
        int length = length();
        double[] array = new double[length];
        for (int i = 0; i < length; i++) {
            final Token token = tokens.get(i + 1);
            array[i] = source.getDouble(token.startIndex, token.endIndex);
        }
        return array;
    }

    public float[] getFloatArray() {
        int length = length();
        float[] array = new float[length];
        for (int i = 0; i < length; i++) {
            final Token token = tokens.get(i + 1);
            array[i] = source.getFloat(token.startIndex, token.endIndex);
        }
        return array;
    }

    public double[] getDoubleArrayFast() {
        int length = length();
        double[] array = new double[length];
        for (int i = 0; i < length; i++) {
            final Token token = tokens.get(i + 1);
            array[i] = source.getBigDecimal(token.startIndex, token.endIndex).doubleValue();
        }
        return array;
    }

    public float[] getFloatArrayFast() {
        int length = length();
        float[] array = new float[length];
        for (int i = 0; i < length; i++) {
            final Token token = tokens.get(i + 1);
            array[i] = (float) source.getBigDecimal(token.startIndex, token.endIndex).doubleValue();
        }
        return array;
    }

    public int[] getIntArrayFast() {
        int length = length();
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            final Token token = tokens.get(i + 1);
            array[i] = source.getBigDecimal(token.startIndex, token.endIndex).intValue();
        }
        return array;
    }

    public long[] getLongArrayFast() {
        int length = length();
        long[] array = new long[length];
        for (int i = 0; i < length; i++) {
            final Token token = tokens.get(i + 1);
            array[i] = source.getBigDecimal(token.startIndex, token.endIndex).longValue();
        }
        return array;
    }

    public BigDecimal[] getBigDecimalArray() {
        int length = length();
        BigDecimal[] array = new BigDecimal[length];
        for (int i = 0; i < length; i++) {
            final Token token = tokens.get(i + 1);
            array[i] = source.getBigDecimal(token.startIndex, token.endIndex);
        }
        return array;
    }

    public BigInteger[] getBigIntegerArray() {
        int length = length();
        BigInteger[] array = new BigInteger[length];
        for (int i = 0; i < length; i++) {
            final Token token = tokens.get(i + 1);
            array[i] = source.getBigDecimal(token.startIndex, token.endIndex).toBigInteger();
        }
        return array;
    }

    public int[] getIntArray() {
        int length = length();
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            final Token token = tokens.get(i + 1);
            array[i] = source.getInt(token.startIndex, token.endIndex);
        }
        return array;
    }

    public long[] getLongArray() {
        int length = length();
        long[] array = new long[length];
        for (int i = 0; i < length; i++) {
            final Token token = tokens.get(i + 1);
            array[i] = source.getLong(token.startIndex, token.endIndex);
        }
        return array;
    }

    public NullNode getNullNode(int index) {
        return (NullNode) getNodeAt(index);
    }

    public int getInt(int index) {
        return getNumberNode(index).intValue();
    }

    public float getFloat(int index) {
        return getNumberNode(index).floatValue();
    }

    public NumberNode getNumberNode(int index) {
        return (NumberNode) getNodeAt(index);
    }

    public BigDecimal getBigDecimal(int i) {
        return getNumberNode(i).bigDecimalValue();
    }

    public BigInteger getBigInteger(int i) {
        return getNumberNode(i).bigIntegerValue();
    }

    public StringNode getStringNode(int index) {
        return (StringNode) getNodeAt(index);
    }

    public String getString(int index) {
        return getStringNode(index).toString();
    }

    public ObjectNode getObject(int index) {
        return (ObjectNode) getNodeAt(index);
    }

    public ArrayNode getArray(int index) {
        return (ArrayNode) getNodeAt(index);
    }

    public BooleanNode getBooleanNode(int index) {
        return (BooleanNode) getNodeAt(index);
    }

    public boolean getBoolean(int index) {
        return getBooleanNode(index).booleanValue();
    }

    public int length() {
        return elements().length;
    }

    @Override
    public NodeType type() {
        return NodeType.ARRAY;
    }

    @Override
    public List<Token> tokens() {
        return tokens;
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
    public Node get(int index) {
        final var node = getNodeAt(index);
        return node.type() == NodeType.NULL ? null : node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArrayNode)) return false;

        final ArrayNode other = (ArrayNode) o;

        if (this.tokens.size() != other.tokens.size()) {
            return false;
        }

        for (int index = 0; index < this.tokens.size(); index++) {
            var thisValue = this.tokens.get(index);
            var otherValue = other.tokens.get(index);
            if (otherValue == null && thisValue == null) continue;
            var thisStr = thisValue.asString(this.source);
            var otherStr = otherValue.asString(other.source);
            if (!thisStr.equals(otherStr)) {
                return false;
            }
        }
        return true;
    }


    @Override
    public int hashCode() {
        if (hashCodeSet) {
            return hashCode;
        }
        hashCode = Objects.hash(tokens.stream().map(tok -> tok.asString(this.source)).collect(Collectors.toList()));
        hashCodeSet = true;
        return hashCode;
    }


    @Override
    public int size() {
        return childrenTokens().size();
    }

    @Override
    public String toString() {
        return this.originalString();
    }


    public <R> List<R> mapObjectNode(Function<ObjectNode, ? extends R> mapper) {
        return map(node -> mapper.apply(node.asCollection().asObject()));
    }

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

    public List<ObjectNode> filterObjects(Predicate<ObjectNode> predicate) {
        Node[] elements = elements();
        final var length = elements.length;
        final var arrayList = new ArrayList<ObjectNode>(length / 2);
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

    public List<Node> filter(Predicate<Node> predicate) {
        Node[] elements = elements();
        final var length = elements.length;
        final var arrayList = new ArrayList<Node>(length / 2);
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
