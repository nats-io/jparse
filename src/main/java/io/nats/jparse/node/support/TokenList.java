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
package io.nats.jparse.node.support;

import io.nats.jparse.token.Token;

import java.util.AbstractList;
import java.util.List;

/**
 * A list implementation for storing tokens.
 * <p>
 * The TokenList class is an implementation of a list that stores tokens. It provides methods for adding tokens,
 * accessing tokens by index, clearing the list, creating sub lists, and more. The class also includes methods
 * for managing placeholder tokens and creating compact clones of the list.
 * </p>
 */
public class TokenList extends AbstractList<Token> {

    private Token[] tokens;
    private int index = 0;

    /**
     * Constructs an empty TokenList with an initial capacity of 32.
     */
    public TokenList() {
        this.tokens = new Token[32];
    }

    /**
     * Constructs a TokenList with the given array of tokens.
     *
     * @param tokens the array of tokens to initialize the TokenList
     */
    public TokenList(Token[] tokens) {
        index = tokens.length;
        this.tokens = tokens;
    }

    /**
     * Removes all tokens from the list.
     */
    @Override
    public void clear() {
        index = 0;
    }

    /**
     * Adds a token to the list.
     *
     * @param token the token to add
     * @return true (as specified by Collection.add)
     */
    @Override
    public final boolean add(Token token) {
        final int length = tokens.length;
        if (index >= length) {
            final Token[] newTokens = new Token[length * 2];
            System.arraycopy(tokens, 0, newTokens, 0, length);
            tokens = newTokens;
        }
        tokens[index] = token;
        index++;
        return true;
    }

    /**
     * Returns a view of the portion of this list between the specified fromIndex (inclusive) and toIndex (exclusive).
     *
     * @param from the start index of the sublist (inclusive)
     * @param to   the end index of the sublist (exclusive)
     * @return the sublist view of the TokenList
     */
    @Override
    public final List<Token> subList(final int from, final int to) {
        return new TokenSubList(tokens, from, to);
    }

    /**
     * Returns the current index.
     *
     * @return the current index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Replaces the token at the specified index with the specified element.
     *
     * @param index   the index of the token to replace
     * @param element the new token to be stored at the specified position
     * @return the token previously at the specified position
     */
    @Override
    public final Token set(final int index, final Token element) {
        tokens[index] = element;
        return null;
    }

    /**
     * Returns the token at the specified index.
     *
     * @param index the index of the token to return
     * @return the token at the specified index
     */
    @Override
    public Token get(int index) {
        return tokens[index];
    }

    /**
     * Returns the number of tokens in the list.
     *
     * @return the number of tokens in the list
     */
    @Override
    public int size() {
        return index;
    }

    /**
     * Returns the array of tokens in the list.
     *
     * @return the array of tokens in the list
     */
    public Token[] getTokens() {
        return tokens;
    }

    /**
     * Adds a placeholder token to the list.
     */
    public void placeHolder() {
        final int length = tokens.length;
        if (index >= length) {
            final Token[] newTokens = new Token[length * 2];
            System.arraycopy(tokens, 0, newTokens, 0, length);
            tokens = newTokens;
        }
        index++;
    }

    /**
     * Creates a compact clone of the TokenList with only the current tokens.
     *
     * @return a compact clone of the TokenList
     */
    public TokenList compactClone() {
        final int length = index;
        final Token[] newTokens = new Token[index];
        System.arraycopy(tokens, 0, newTokens, 0, length);
        return new TokenList(newTokens);
    }

    /**
     * Undoes the placeholder by decrementing the index.
     */
    public void undoPlaceholder() {
        index--;
    }
}
