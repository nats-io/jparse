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
package com.cloudurable.jparse.node;

import com.cloudurable.jparse.node.support.NodeUtils;
import com.cloudurable.jparse.node.support.TokenSubList;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.Token;
import com.cloudurable.jparse.token.TokenTypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

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

        this.tokens =  tokens;
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
        final var value = getNode(key);
        return value instanceof NullNode ? null : value;
    }

    @Override
    public Set<Entry<CharSequence, Node>> entrySet() {

        return new AbstractSet<>() {

            @Override
            public boolean contains(Object o) {
                return keys().contains(o);
            }

            @Override
            public Iterator<Entry<CharSequence, Node>> iterator() {
                final Iterator<CharSequence> iterator = keys().iterator();
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public Entry<CharSequence, Node> next() {
                        CharSequence nextKey = iterator.next();
                        return new Entry<>() {
                            @Override
                            public CharSequence getKey() {
                                return nextKey;
                            }

                            @Override
                            public Node getValue() {
                                return getObjectNode(nextKey);
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
        if (!(o instanceof final ObjectNode other)) return false;

        if (this.tokens.size() != other.tokens.size()) {
            return false;
        }


        final var keys = keys();
        final var otherKeys = other.keys();

        if (keys.size() != otherKeys.size()) {
            return false;
        }

        for (Object key : keys) {
            final var otherElementValue = other.getNode(key);
            final var thisElementValue = this.getNode(key);

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
        final var keys = keys();
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
            final var stringNode = new StringNode(keyToken, source, objectsKeysCanBeEncoded);
            if (objectsKeysCanBeEncoded) {
                final var string = stringNode.toString();
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
                CharSequence element;
                Token keyToken = itemKey.get(1);
                element = switch (keyToken.type) {
                    case TokenTypes.STRING_TOKEN -> new StringNode(keyToken, source, objectsKeysCanBeEncoded).toString();
                    default -> throw new IllegalStateException("Only String are allowed for keys " + TokenTypes.getTypeName(keyToken.type));
                };
                keys.add(element);
            }
        }
        return keys;
    }

    @Override
    public String toString() {
        return this.originalString();
    }
}
