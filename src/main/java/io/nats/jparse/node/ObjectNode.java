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

/**
 * The ObjectNode class represents an object node in a tree structure.
 * <p>
 * It extends the AbstractMap class and implements the CollectionNode interface.
 * <p>
 * Object nodes are used to store key-value pairs, where the keys are CharSequences
 * <p>
 * and the values are nodes in the tree structure.
 */
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

    /**
     * Constructs an ObjectNode with the specified tokens, character source, and objectsKeysCanBeEncoded flag.
     *
     * @param tokens                  the list of tokens representing the object node
     * @param source                  the character source from which the tokens were parsed
     * @param objectsKeysCanBeEncoded a flag indicating whether object keys can be encoded
     */
    public ObjectNode(TokenSubList tokens, CharSource source, boolean objectsKeysCanBeEncoded) {
        this.tokens = tokens;
        this.source = source;
        this.rootToken = tokens.get(0);
        this.objectsKeysCanBeEncoded = objectsKeysCanBeEncoded;
    }

    /**
     * Returns the list of tokens representing the children of the object node.
     * If the list of children tokens has not been initialized yet, it will be lazily initialized.
     *
     * @return the list of tokens representing the children of the object node
     */
    @Override
    public List<List<Token>> childrenTokens() {
        if (childrenTokens == null) {
            childrenTokens = NodeUtils.getChildrenTokens(tokens);
        }
        return childrenTokens;
    }

    /**
     * Returns the node associated with the specified key.
     *
     * @param key the key to retrieve the associated node
     * @return the node associated with the specified key
     */
    @Override
    public Node getNode(Object key) {
        return lookupElement((CharSequence) key);
    }

    /**
     * Returns the keys of the object node as a list of CharSequences.
     * If the keys list has not been initialized yet, it will be lazily initialized.
     *
     * @return the keys of the object node as a list of CharSequences
     */
    public List<CharSequence> getKeys() {
        return keys();
    }

    /**
     * Returns the number of key-value pairs in the object node.
     *
     * @return the number of key-value pairs in the object node
     */
    public int length() {
        return childrenTokens().size() / 2;
    }

    /**
     * Returns the type of the object node, which is OBJECT.
     *
     * @return the type of the object node
     */
    @Override
    public NodeType type() {
        return NodeType.OBJECT;
    }

    /**
     * Returns the list of tokens associated with the object node.
     *
     * @return the list of tokens associated with the object node
     */
    @Override
    public List<Token> tokens() {
        return tokens;
    }

    /**
     * Returns the root element token of the object node.
     *
     * @return the root element token of the object node
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
     * Returns the value associated with the specified key.
     * If the value is a NullNode, null is returned instead.
     *
     * @param key the key to retrieve the associated value
     * @return the value associated with the specified key, or null if the value is a NullNode
     */
    @Override
    public Node get(Object key) {
        final Node value = getNode(key);
        return value instanceof NullNode ? null : value;
    }

    /**
     * Checks if the object node contains the specified key.
     *
     * @param key the key to check for existence
     * @return {@code true} if the object node contains the key, {@code false} otherwise
     */
    public boolean containsKey(Object key) {
        return lookupElement((CharSequence) key) != null;
    }

    /**
     * Checks if the object node is empty (contains no key-value pairs).
     *
     * @return {@code true} if the object node is empty, {@code false} otherwise
     */
    @Override
    public boolean isEmpty() {
        return keys().isEmpty();
    }

    /**
     * Returns the number of key-value pairs in the object node.
     *
     * @return the number of key-value pairs in the object node
     */
    @Override
    public int size() {
        return keys().size();
    }

    /**
     * Returns a set of the keys in the object node.
     *
     * @return a set of the keys in the object node
     */
    @Override
    public Set<CharSequence> keySet() {
        return keys().stream().map(CharSequence::toString).collect(Collectors.toSet());
    }

    /**
     * Returns a collection of the values in the object node.
     *
     * @return a collection of the values in the object node
     */
    @Override
    public Collection<Node> values() {
        return keys().stream().map(this::lookupElement).collect(Collectors.toList());
    }

    /**
     * Returns a set view of the entries in the object node.
     *
     * @return a set view of the entries in the object node
     */
    @Override
    public Set<Entry<CharSequence, Node>> entrySet() {


        return new AbstractSet<Entry<CharSequence, Node>>() {

            /**
             * Checks if the object node contains the specified entry.
             *
             * @param o the entry to check for existence
             * @return {@code true} if the object node contains the entry, {@code false} otherwise
             */
            @Override
            public boolean contains(Object o) {
                return keys().contains(o);
            }

            /**
             * Returns an iterator over the entries in the object node.
             *
             * @return an iterator over the entries in the object node
             */
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

            /**
             * Returns the number of entries in the object node.
             *
             * @return the number of entries in the object node
             */
            @Override
            public int size() {
                return keys().size();
            }
        };

    }

    /**
     * Checks if this object node is equal to the specified object.
     * <p>
     * Two object nodes are considered equal if they have the same keys and corresponding values.
     *
     * @param o the object to compare with
     * @return {@code true} if this object node is equal to the specified object, {@code false} otherwise
     */
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

    /**
     * Returns the hash code value for this object node.
     *
     * @return the hash code value for this object node
     */
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

    /**
     * Returns the number node associated with the specified key.
     *
     * @param key the key to retrieve the associated number node
     * @return the number node associated with the specified key
     */
    public NumberNode getNumberNode(CharSequence key) {
        return (NumberNode) getNode(key);
    }

    /**
     * Returns the null node associated with the specified key.
     *
     * @param key the key to retrieve the associated null node
     * @return the null node associated with the specified key
     */
    public NullNode getNullNode(CharSequence key) {
        return (NullNode) getNode(key);
    }

    /**
     * Returns the long value associated with the specified key.
     * This method assumes that the value associated with the key is a number node.
     *
     * @param key the key to retrieve the associated long value
     * @return the long value associated with the specified key
     */
    public long getLong(CharSequence key) {
        return ((NumberNode) getNode(key)).longValue();
    }

    /**
     * Returns the double value associated with the specified key.
     * This method assumes that the value associated with the key is a number node.
     *
     * @param key the key to retrieve the associated double value
     * @return the double value associated with the specified key
     */
    public double getDouble(CharSequence key) {
        return ((NumberNode) getNode(key)).doubleValue();
    }

    /**
     * Returns the integer value associated with the specified key.
     * This method assumes that the value associated with the key is a number node.
     *
     * @param key the key to retrieve the associated integer value
     * @return the integer value associated with the specified key
     */
    public int getInt(CharSequence key) {
        return ((NumberNode) getNode(key)).intValue();
    }

    /**
     * Returns the float value associated with the specified key.
     * This method assumes that the value associated with the key is a number node.
     *
     * @param key the key to retrieve the associated float value
     * @return the float value associated with the specified key
     */
    public float getFloat(CharSequence key) {
        return ((NumberNode) getNode(key)).floatValue();
    }

    /**
     * Returns the BigDecimal value associated with the specified key.
     * This method assumes that the value associated with the key is a number node.
     *
     * @param key the key to retrieve the associated BigDecimal value
     * @return the BigDecimal value associated with the specified key
     */
    public BigDecimal getBigDecimal(CharSequence key) {
        return getNumberNode(key).bigDecimalValue();
    }

    /**
     * Returns the BigInteger value associated with the specified key.
     * This method assumes that the value associated with the key is a number node.
     *
     * @param key the key to retrieve the associated BigInteger value
     * @return the BigInteger value associated with the specified key
     */
    public BigInteger getBigInteger(CharSequence key) {
        return getNumberNode(key).bigIntegerValue();
    }

    /**
     * Returns the string node associated with the specified key.
     *
     * @param key the key to retrieve the associated string node
     * @return the string node associated with the specified key
     */
    public StringNode getStringNode(CharSequence key) {
        return (StringNode) getNode(key);
    }

    /**
     * Returns the string representation of the value associated with the specified key.
     * This method assumes that the value associated with the key is a string node.
     *
     * @param key the key to retrieve the associated string value
     * @return the string representation of the value associated with the specified key
     */
    public String getString(CharSequence key) {
        return getStringNode(key).toString();
    }

    /**
     * Returns the object node associated with the specified key.
     *
     * @param key the key to retrieve the associated object node
     * @return the object node associated with the specified key
     */
    public ObjectNode getObjectNode(CharSequence key) {
        return (ObjectNode) getNode(key);
    }

    /**
     * Returns the array node associated with the specified key.
     *
     * @param key the key to retrieve the associated array node
     * @return the array node associated with the specified key
     */
    public ArrayNode getArrayNode(CharSequence key) {
        return (ArrayNode) getNode(key);
    }

    /**
     * Returns the boolean node associated with the specified key.
     *
     * @param key the key to retrieve the associated boolean node
     * @return the boolean node associated with the specified key
     */
    public BooleanNode getBooleanNode(CharSequence key) {
        return (BooleanNode) getNode(key);
    }

    /**
     * Returns the boolean value associated with the specified key.
     * This method assumes that the value associated with the key is a boolean node.
     *
     * @param key the key to retrieve the associated boolean value
     * @return the boolean value associated with the specified key
     */
    public boolean getBoolean(CharSequence key) {
        return getBooleanNode(key).booleanValue();
    }

    /**
     * Returns an Optional containing the node associated with the specified key.
     * If a node is found with a matching key, it is created and returned as an Optional.
     * If no matching key is found, an empty Optional is returned.
     *
     * @param key the node representing the key to search for
     * @return an Optional containing the node associated with the specified key, or an empty Optional if no matching key is found
     */
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

    /**
     * Checks if the given list of tokens matches the specified key node.
     *
     * @param tokens the list of tokens representing the key
     * @param key    the node representing the key to match
     * @return {@code true} if the list of tokens matches the specified key node, {@code false} otherwise
     */
    private boolean keyMatch(final List<Token> tokens, final Node key) {
        return NodeUtils.createNodeForObject(tokens, source, objectsKeysCanBeEncoded).equals(key);
    }

    /**
     * Looks up and returns the node associated with the specified key.
     * <p>
     * If the node is found in the element map, it is returned.
     * <p>
     * Otherwise, it iterates over the children tokens to find a matching key, creates the corresponding node,
     * <p>
     * adds it to the element map, and returns it.
     *
     * @param key the key to retrieve the associated node
     * @return the node associated with the specified key, or null if no matching key is found
     */
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

    /**
     * Checks if the given list of tokens matches the specified key.
     *
     * @param itemKey the list of tokens representing the key
     * @param key     the CharSequence key to match
     * @return {@code true} if the list of tokens matches the specified key, {@code false} otherwise
     */
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
                switch (keyToken.type) {
                    case TokenTypes.STRING_TOKEN:
                        final StringNode element = new StringNode(keyToken, source, objectsKeysCanBeEncoded);
                        keys.add(element);
                        break;
                    default:
                        throw new IllegalStateException("Only String are allowed for keys " + TokenTypes.getTypeName(keyToken.type));
                }
                ;

            }
        }
        return keys;
    }

    @Override
    public String toString() {
        return this.originalString();
    }
}
