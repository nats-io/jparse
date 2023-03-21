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
import java.util.stream.IntStream;

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

    // TODO take these out.
//    public double[] getDoubleArray() {
//        int length = length();
//        double[] array = new double[length];
//        for (int i = 0; i < length; i++) {
//            final Token token = tokens.get(i + 1);
//            array[i] = source.getDouble(token.startIndex, token.endIndex);
//        }
//        return array;
//    }
//
//    public float[] getFloatArray() {
//        int length = length();
//        float[] array = new float[length];
//        for (int i = 0; i < length; i++) {
//            final Token token = tokens.get(i + 1);
//            array[i] = source.getFloat(token.startIndex, token.endIndex);
//        }
//        return array;
//    }

    public double[] getDoubleArray() {
        //Using collections API cont by Luis Taddey or SeelePifer
        return IntStream.range(0, length())
                .mapToDouble(i -> source.getBigDecimal(tokens.get(i + 1).startIndex, tokens.get(i + 1).endIndex).doubleValue())
                .toArray();
    }

    public float[] getFloatArray() {
        double[] doubleArray = IntStream.range(0, length())
                .mapToDouble(i -> source.getBigDecimal(tokens.get(i + 1).startIndex, tokens.get(i + 1).endIndex).doubleValue())
                .toArray();
        //Need to do that because can't use .toArray(size -> new float[size]);
        float[] floatArray = new float[doubleArray.length];
        for (int i = 0; i < doubleArray.length; i++) {
            floatArray[i] = (float) doubleArray[i];
        }
        return floatArray;
    }

//    public int[] getIntArrayFast() {
//        int length = length();
//        int[] array = new int[length];
//        for (int i = 0; i < length; i++) {
//            final Token token = tokens.get(i + 1);
//            array[i] = source.getBigDecimal(token.startIndex, token.endIndex).intValue();
//        }
//        return array;
//    }
//
//    public long[] getLongArrayFast() {
//        int length = length();
//        long[] array = new long[length];
//        for (int i = 0; i < length; i++) {
//            final Token token = tokens.get(i + 1);
//            array[i] = source.getBigDecimal(token.startIndex, token.endIndex).longValue();
//        }
//        return array;
//    }

    public BigDecimal[] getBigDecimalArray() {
        return IntStream.range(0, length())
                .mapToObj(i -> {
                    final Token token = tokens.get(i + 1);
                    return source.getBigDecimal(token.startIndex, token.endIndex);
                })
                .toArray(BigDecimal[]::new);
    }

    public BigInteger[] getBigIntegerArray() {
        return IntStream.range(0, length())
                .mapToObj(i -> {
                    final Token token = tokens.get(i + 1);
                    return source.getBigDecimal(token.startIndex, token.endIndex).toBigInteger();
                })
                .toArray(BigInteger[]::new);
    }

    public int[] getIntArray() {
        return IntStream.range(0, length())
                .map(i -> source.getInt(tokens.get(i + 1).startIndex, tokens.get(i + 1).endIndex))
                .toArray();
    }

    public long[] getLongArray() {
        return IntStream.range(0, length())
                .mapToLong(i -> source.getLong(tokens.get(i + 1).startIndex, tokens.get(i + 1).endIndex))
                .toArray();
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
        final Node node = getNodeAt(index);
        return node.type() == NodeType.NULL ? null : node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArrayNode)) return false;

        final ArrayNode other = (ArrayNode) o;

        return this.tokens.size() == other.tokens.size() &&
                IntStream.range(0, this.tokens.size())
                        .allMatch(index -> {
                            Token thisValue = this.tokens.get(index);
                            Token otherValue = other.tokens.get(index);
                            if (otherValue == null && thisValue == null) return true;
                            String thisStr = thisValue.asString(this.source);
                            String otherStr = otherValue.asString(other.source);
                            return thisStr.equals(otherStr);
                        });
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
        return IntStream.range(0, size())
                .mapToObj(i -> {
                    Node element = elements()[i];
                    if (element == null) {
                        element = getNodeAt(i);
                    }
                    return mapper.apply(element);
                })
                .collect(Collectors.toList());
    }

    public Optional<ObjectNode> findObjectNode(Predicate<ObjectNode> predicate) {
        return Arrays.stream(elements())
                .filter(element -> element != null && element.type() == NodeType.OBJECT)
                .map(element -> element.asCollection().asObject())
                .filter(predicate)
                .findFirst();
    }

    public Optional<Node> find(Predicate<Node> predicate) {
        return Arrays.stream(elements())
                .filter(Objects::nonNull)
                .filter(predicate)
                .findFirst();
    }

    public List<ObjectNode> filterObjects(Predicate<ObjectNode> predicate) {
        return Arrays.stream(elements())
                .filter(element -> element != null && element.type() == NodeType.OBJECT)
                .map(element -> element.asCollection().asObject())
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public List<Node> filter(Predicate<Node> predicate) {
        return Arrays.stream(elements())
                .filter(Objects::nonNull)
                .filter(predicate)
                .collect(Collectors.toList());
    }


}
