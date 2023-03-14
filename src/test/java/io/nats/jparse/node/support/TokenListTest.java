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
import io.nats.jparse.token.TokenTypes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TokenListTest {

    @Test
    void cloneList() {
        final var tokenList = new TokenList();
        tokenList.add(new Token(1, 2, TokenTypes.ARRAY_TOKEN));
        tokenList.add(new Token(3, 4, TokenTypes.ARRAY_TOKEN));
        tokenList.add(new Token(5, 6, TokenTypes.ARRAY_TOKEN));

        final var tokenList2 = tokenList.compactClone();

        assertEquals(tokenList.size(), tokenList2.size());

        assertEquals(tokenList, tokenList2);

        tokenList.clear();

        assertNotEquals(tokenList.size(), tokenList2.size());
        assertNotEquals(tokenList, tokenList2);
    }
}
