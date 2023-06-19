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
 * A sublist implementation for storing a portion of tokens from a TokenList.
 * <p>
 * The TokenSubList class is a sublist implementation that represents a portion of tokens from a TokenList.
 * It provides methods for accessing tokens within the sublist, getting the size of the sublist, creating sub lists,
 * converting the sublist to an array, and counting the number of children tokens within a specified range relative
 * to a root token.
 * </p>
 */
public class TokenSubList extends AbstractList<Token> {

    private final int size;
    private final Token[] tokens;
    private final int offset;
    private final int endIndex;

    /**
     * Constructs a TokenSubList with the given tokens, offset, and endIndex.
     *
     * @param tokens   the array of tokens
     * @param offset   the starting index of the sublist (inclusive)
     * @param endIndex the ending index of the sublist (exclusive)
     */
    public TokenSubList(Token[] tokens, int offset, int endIndex) {
        size = endIndex - offset;
        this.tokens = tokens;
        this.offset = offset;
        this.endIndex = endIndex;
    }

    /**
     * Returns the token at the specified index in this sublist.
     *
     * @param index the index of the token to return
     * @return the token at the specified index
     */
    @Override
    public Token get(int index) {
        return tokens[offset + index];
    }

    /**
     * Returns the size of this sublist.
     *
     * @return the size of the sublist
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns a view of the portion of this sublist between the specified startIndex (inclusive) and endIndex (exclusive).
     *
     * @param startIndex the start index of the sublist view (inclusive)
     * @param endIndex   the end index of the sublist view (exclusive)
     * @return the sublist view
     */
    @Override
    public List<Token> subList(int startIndex, int endIndex) {
        return new TokenSubList(tokens, this.offset + startIndex, this.offset + endIndex);
    }

    /**
     * Returns an array containing all of the tokens in this sublist.
     *
     * @return an array of tokens
     */
    public Token[] toArray() {
        Token[] array = new Token[size];
        System.arraycopy(tokens, offset, array, 0, size);
        return array;
    }

    /**
     * Counts the number of children tokens within the specified range, relative to a root token.
     *
     * @param from      the starting index to count children from
     * @param rootToken the root token to compare against
     * @return the number of children tokens
     */
    public int countChildren(final int from, final Token rootToken) {
        int idx = from;
        int count = 0;
        final Token[] tokens = this.tokens;
        final int length = this.size;
        final int offset = this.offset;
        final int rootTokenStart = rootToken.startIndex;
        final int rootTokenEnd = rootToken.endIndex;

        for (; idx < length; idx++) {
            Token token = tokens[idx + offset];

            if (token.startIndex >= rootTokenStart && token.endIndex <= rootTokenEnd) {
                count++;
            } else {
                break;
            }
        }

        return count;
    }
}
