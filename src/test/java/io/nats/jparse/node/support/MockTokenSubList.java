package io.nats.jparse.node.support;

import io.nats.jparse.token.Token;

import java.util.Arrays;
import java.util.List;

public class MockTokenSubList extends TokenSubList {

    private final List<Token> tokens;

    // Constructor
    public MockTokenSubList(final List<Token> tokens) {
        super(tokens.toArray(new Token[tokens.size()]), 0, tokens.size());
        this.tokens = tokens;
    }

    public static TokenSubList of(Token... rootToken) {
        final List<Token> tokens = Arrays.asList(rootToken);
        return new MockTokenSubList(tokens);
    }

    // Returns the size of the TokenSubList
    @Override
    public int size() {
        return tokens.size();
    }

    // Returns the Token at the given index
    @Override
    public Token get(final int index) {
        return tokens.get(index);
    }

    // Returns a sub list of Tokens
    @Override
    public TokenSubList subList(final int fromIndex, final int toIndex) {
        return new MockTokenSubList(tokens.subList(fromIndex, toIndex));
    }

    // The toString() method for debugging
    @Override
    public String toString() {
        return "MockTokenSubList{tokens=" + tokens + '}';
    }

}
