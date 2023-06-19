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
import io.nats.jparse.token.TokenTypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class ObjectNode extends AbstractMap<CharSequence, Node> implements CollectionNode {


    private final TokenSubList tokens;
    private final CharSource source;
    private final Token rootToken;
    private final boolean objectsKeysCanBeEncoded;
    private List<List<Token>> childrenTokens;
    private Map<Object, Node> elementMap;
    private List<CharSequence> keys;
    private boolean hashCodeSet;
    private int hashCode;


    public ObjectNode(TokenSubList tokens, CharSource source, boolean objectsKeysCanBeEncoded) {

        this.tokens = tokens;
        this.source = source;
        this.rootToken = tokens.get(0);
        this.objectsKeysCanBeEncoded = objectsKeysCanBeEncoded;

    }

    @Override
    public List<List<Token>> childrenTokens() {
        if (childrenTokens == null) {
            childrenTokens = NodeUtils.getChildrenTokens(tokens);
        }
        return childrenTokens;
    }

    @Override
    public Node getNode(Object key) {
        return lookupElement((CharSequence) key);
    }

    public List<CharSequence> getKeys() {
        return keys();
    }

    public int length() {
        return childrenTokens().size() / 2;
    }

    @Override
    public NodeType type() {
        return NodeType.OBJECT;
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
    public Node get(Object key) {
        final Node value = getNode(key);
//        if (value != null) {
//            switch (value.getClass().getName()) {
//                case "io.nats.jparse.node.NullNode":
//                    return null;
//                case "io.nats.jparse.node.BooleanNode":
//                    BooleanNode bn = (BooleanNode) value;
//                    return bn.booleanValue();
//
//            }
//        }
        return value instanceof NullNode ? null : value;
    }

    public boolean containsKey(Object key) {
        return lookupElement((CharSequence) key) != null;
    }

    @Override
    public boolean isEmpty() {
        return keys().isEmpty();
    }

    @Override
    public int size() {
        return keys().size();
    }

    @Override
    public Set<CharSequence> keySet() {
        return keys().stream().map(CharSequence::toString).collect(Collectors.toSet());
    }

    @Override
    public Collection<Node> values() {
        return keys().stream().map(this::lookupElement).collect(Collectors.toList());
    }

    @Override
    public Set<Entry<CharSequence, Node>> entrySet() {


        return new AbstractSet<Entry<CharSequence, Node>>() {

            @Override
            public boolean contains(Object o) {
                return keys().contains(o);
            }

            @Override
            public Iterator<Entry<CharSequence, Node>> iterator() {
                final Iterator<CharSequence> iterator = keys().iterator();
                return new Iterator<Entry<CharSequence, Node>>() {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public Entry<CharSequence, Node> next() {
                        final CharSequence nextKey = iterator.next().toString();
                        return new Entry<CharSequence, Node>() {
                            @Override
                            public CharSequence getKey() {
                                return nextKey;
                            }

                            @Override
                            public Node getValue() {
                                return lookupElement(nextKey);
                            }

                            @Override
                            public Node setValue(Node value) {
                                throw new UnsupportedOperationException();
                            }
                        };
                    }
                };
            }

            @Override
            public int size() {
                return keys().size();
            }
        };

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObjectNode)) return false;

        final ObjectNode other = (ObjectNode) o;

        if (this.tokens.size() != other.tokens.size()) {
            return false;
        }


        final List<CharSequence> keys = keys();
        final List<CharSequence> otherKeys = other.keys();

        if (keys.size() != otherKeys.size()) {
            return false;
        }

        for (Object key : keys) {
            final Node otherElementValue = other.getNode(key);
            final Node thisElementValue = this.getNode(key);

            if (otherElementValue == null) {
                return false;
            }

            if (!otherElementValue.equals(thisElementValue)) {
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
        final List<CharSequence> keys = keys();
        for (Object key : keys) {
            this.getNode(key);
        }
        hashCode = this.elementMap.hashCode();
        hashCodeSet = true;
        return hashCode;
    }


    public NumberNode getNumberNode(CharSequence key) {
        return (NumberNode) getNode(key);
    }

    public NullNode getNullNode(CharSequence key) {
        return (NullNode) getNode(key);
    }

    public long getLong(CharSequence key) {
        return ((NumberNode) getNode(key)).longValue();
    }

    public double getDouble(CharSequence key) {
        return ((NumberNode) getNode(key)).doubleValue();
    }

    public int getInt(CharSequence key) {
        return ((NumberNode) getNode(key)).intValue();
    }

    public float getFloat(CharSequence key) {
        return ((NumberNode) getNode(key)).floatValue();
    }

    public BigDecimal getBigDecimal(CharSequence key) {
        return getNumberNode(key).bigDecimalValue();
    }

    public BigInteger getBigInteger(CharSequence key) {
        return getNumberNode(key).bigIntegerValue();
    }

    public StringNode getStringNode(CharSequence key) {
        return (StringNode) getNode(key);
    }

    public String getString(CharSequence key) {
        return getStringNode(key).toString();
    }

    public ObjectNode getObjectNode(CharSequence key) {
        return (ObjectNode) getNode(key);
    }

    public ArrayNode getArrayNode(CharSequence key) {
        return (ArrayNode) getNode(key);
    }

    public BooleanNode getBooleanNode(CharSequence key) {
        return (BooleanNode) getNode(key);
    }

    public boolean getBoolean(CharSequence key) {
        return getBooleanNode(key).booleanValue();
    }

    public Optional<Node> getNode(final Node key) {
        List<List<Token>> childrenTokens = childrenTokens();
        Node node = null;
        for (int index = 0; index < childrenTokens.size(); index += 2) {
            List<Token> itemKey = childrenTokens.get(index);
            if (keyMatch(itemKey, key)) {
                node = NodeUtils.createNodeForObject(childrenTokens.get(index + 1), source, objectsKeysCanBeEncoded);
                break;
            }
        }
        return Optional.ofNullable(node);
    }


    private boolean keyMatch(final List<Token> tokens, final Node key) {
        return NodeUtils.createNodeForObject(tokens, source, objectsKeysCanBeEncoded).equals(key);
    }

    private Node lookupElement(final CharSequence key) {

        if (elementMap == null) {
            elementMap = new HashMap<>();
        }
        Node node = elementMap.get(key);

        if (node == null) {

            List<List<Token>> childrenTokens = childrenTokens();
            for (int index = 0; index < childrenTokens.size(); index += 2) {
                List<Token> itemKey = childrenTokens.get(index);


                if (doesMatchKey(itemKey, key)) {
                    node = NodeUtils.createNodeForObject(childrenTokens.get(index + 1), source, objectsKeysCanBeEncoded);
                    elementMap.put(key, node);
                    break;
                }
            }
        }

        return node;
    }

    private boolean doesMatchKey(final List<Token> itemKey, final CharSequence key) {

        final Token keyToken = itemKey.get(1);

        if (keyToken.type == TokenTypes.STRING_TOKEN) {
            if (keyToken.length() != key.length()) {
                return false;
            }

            if (objectsKeysCanBeEncoded) {
                final StringNode stringNode = new StringNode(keyToken, source, objectsKeysCanBeEncoded);
                final String string = stringNode.toString();
                for (int index = 0; index < key.length(); index++) {
                    if (string.charAt(index) != key.charAt(index)) {
                        return false;
                    }
                }
                return true;
            } else {
                return source.matchChars(keyToken.startIndex, keyToken.endIndex, key);
            }
        }
        return false;
    }


    private List<CharSequence> keys() {
        if (keys == null) {
            List<List<Token>> childrenTokens = childrenTokens();
            keys = new ArrayList<>(childrenTokens.size() / 2);
            for (int index = 0; index < childrenTokens.size(); index += 2) {
                List<Token> itemKey = childrenTokens.get(index);
                Token keyToken = itemKey.get(1);
                if (keyToken.type == TokenTypes.STRING_TOKEN) {
                    final StringNode element = new StringNode(keyToken, source, objectsKeysCanBeEncoded);
                    keys.add(element);
                } else {
                    throw new IllegalStateException("Only String are allowed for keys " + TokenTypes.getTypeName(keyToken.type));
                }

            }
        }
        return keys;
    }

    @Override
    public String toString() {
        return this.originalString();
    }
}
