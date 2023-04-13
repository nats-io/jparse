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

public class TokenSubList extends AbstractList<Token> {

    private final int size;
    private final Token[] tokens;
    private final int offset;
    private final int endIndex;


    public TokenSubList(Token[] tokens, int offset, int endIndex) {
        size = endIndex - offset;
        this.tokens = tokens;
        this.offset = offset;
        this.endIndex = endIndex;
    }

    @Override
    public Token get(int index) {
        return tokens[offset + index];

    }

    @Override
    public int size() {
        return size;
    }


    @Override
    public List<Token> subList(int startIndex, int endIndex) {
        return new TokenSubList(tokens, this.offset + startIndex, this.offset + endIndex);
    }

    public Token[] toArray() {
        Token[] array = new Token[size];
        System.arraycopy(tokens, offset, array, 0, size);
        return array;
    }

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


            if (token.startIndex >= rootTokenStart
                    && token.endIndex <= rootTokenEnd) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }
}
