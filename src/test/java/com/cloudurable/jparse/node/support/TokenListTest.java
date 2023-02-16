package com.cloudurable.jparse.node.support;

import com.cloudurable.jparse.token.Token;
import com.cloudurable.jparse.token.TokenType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TokenListTest {

    @Test
    void cloneList() {
        final var tokenList = new TokenList();
        tokenList.add(new Token(1, 2, TokenType.ARRAY));
        tokenList.add(new Token(3, 4, TokenType.ARRAY));
        tokenList.add(new Token(5, 6, TokenType.ARRAY));

        final var tokenList2 = tokenList.compactClone();

        assertEquals(tokenList.size(), tokenList2.size());

        assertEquals(tokenList, tokenList2);

        tokenList.clear();

        assertNotEquals(tokenList.size(), tokenList2.size());
        assertNotEquals(tokenList, tokenList2);
    }
}
