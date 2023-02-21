package com.cloudurable.jparse.node.support;

import com.cloudurable.jparse.token.Token;

import java.util.AbstractList;
import java.util.List;

public class TokenList extends AbstractList<Token> {


    private Token[] tokens;
    private int index = 0;


    public TokenList() {
        this.tokens = new Token[32];
    }

    public TokenList(Token[] tokens) {

        index = tokens.length;
        this.tokens = tokens;
    }

    @Override
    public void clear() {
        index = 0;
    }

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

    @Override
    public final List<Token> subList(final int from, final int to) {
        return new TokenSubList(tokens, from, to);
    }

    public int getIndex() {
        return index;
    }

    @Override
    public final Token set(final int index, final Token element) {
        tokens[index] = element;
        return null;
    }

    @Override
    public Token get(int index) {
        return tokens[index];
    }

    @Override
    public int size() {
        return index;
    }

    public Token[] getTokens() {
        return tokens;
    }

    public void placeHolder() {
        index++;
    }

    public TokenList compactClone() {
        final var length = index;
        final Token[] newTokens = new Token[index];
        System.arraycopy(tokens, 0, newTokens, 0, length);
        return new TokenList(newTokens);
    }

    public void undoPlaceholder() {
        index--;
    }
}
