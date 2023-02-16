package com.cloudurable.jparse.node.support;

import com.cloudurable.jparse.token.Token;

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

    public int countChildren(final int from, final Token rootToken) {

        int idx = from;
        int count = 0;
        final Token[] tokens = this.tokens;
        final int length = this.size;
        final int offset = this.offset;
        final int rootTokenStart = rootToken.startIndex();
        final int rootTokenEnd = rootToken.endIndex();
        for (; idx < length; idx++) {
            Token token = tokens[idx + offset];


            if (token.startIndex() >= rootTokenStart
                    && token.endIndex() <= rootTokenEnd) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }
}
