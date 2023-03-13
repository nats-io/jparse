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
package io.nats.jparse.parser.event;

import io.nats.jparse.node.RootNode;
import io.nats.jparse.node.support.TokenList;
import io.nats.jparse.parser.JsonEventParser;
import io.nats.jparse.parser.JsonIndexOverlayParser;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.source.support.UnexpectedCharacterException;
import io.nats.jparse.token.Token;
import io.nats.jparse.token.TokenEventListener;
import io.nats.jparse.token.TokenTypes;

import java.util.List;

public abstract class JsonEventAbstractParser implements JsonEventParser, JsonIndexOverlayParser {

    private final TokenEventListener tokenEventListener;
    private TokenList tokenList;

    final TokenEventListener arrayItemListener = new TokenEventListener() {
        @Override
        public void start(int tokenId, int index, CharSource source) {
        }

        @Override
        public void end(int tokenId, int index, CharSource source) {
        }
    };
    final TokenEventListener exceptionListener = new TokenEventListener() {
        @Override
        public void start(int tokenId, int index, CharSource source) {
            throw new UnexpectedCharacterException("while doing event parsing", "Unknown token id " + tokenId, source);
        }

        @Override
        public void end(int tokenId, int index, CharSource source) {

        }
    };
    protected final boolean objectsKeysCanBeEncoded;
    private final TokenEventListener stringListener = new JsonEventStrictParser.ScalarListener(TokenTypes.STRING_TOKEN);
    private final TokenEventListener floatListener = new JsonEventStrictParser.ScalarListener(TokenTypes.FLOAT_TOKEN);
    private final TokenEventListener intListener = new JsonEventStrictParser.ScalarListener(TokenTypes.INT_TOKEN);
    private final TokenEventListener booleanListener = new JsonEventStrictParser.ScalarListener(TokenTypes.BOOLEAN_TOKEN);
    private final TokenEventListener nullListener = new JsonEventStrictParser.ScalarListener(TokenTypes.NULL_TOKEN);
    final TokenEventListener base = new TokenEventListener() {

        private int stackIndex = -1;
        private TokenEventListener[] stack = new TokenEventListener[8];

        @Override
        public void start(final int tokenId, final int index, final CharSource source) {

            TokenEventListener listener;
            switch (tokenId) {
                case TokenTypes.OBJECT_TOKEN:
                    listener = new ComplexListener(TokenTypes.OBJECT_TOKEN);
                    break;
                case TokenTypes.ARRAY_TOKEN:
                    listener = new ComplexListener(TokenTypes.ARRAY_TOKEN);
                    break;
                case TokenTypes.ARRAY_ITEM_TOKEN:
                    listener = arrayItemListener;
                    break;
                case TokenTypes.ATTRIBUTE_KEY_TOKEN:
                    listener = new ComplexListener(TokenTypes.ATTRIBUTE_KEY_TOKEN);
                    break;
                case TokenTypes.ATTRIBUTE_VALUE_TOKEN:
                    listener = new ComplexListener(TokenTypes.ATTRIBUTE_VALUE_TOKEN);
                    break;
                case TokenTypes.STRING_TOKEN:
                    listener = stringListener;
                    break;
                case TokenTypes.FLOAT_TOKEN:
                    listener = floatListener;
                    break;
                case TokenTypes.INT_TOKEN:
                    listener = intListener;
                    break;
                case TokenTypes.BOOLEAN_TOKEN:
                    listener = booleanListener;
                    break;
                case TokenTypes.NULL_TOKEN:
                    listener = nullListener;
                    break;
                default:
                    listener = exceptionListener;
                    break;
            }

            listener.start(tokenId, index, source);
            stackIndex++;
            if (stackIndex >= stack.length) {
                TokenEventListener[] stackNew = new TokenEventListener[stack.length * 2];
                System.arraycopy(stack, 0, stackNew, 0, stack.length);
                stack = stackNew;
            }
            stack[stackIndex] = listener;
        }

        @Override
        public void end(int tokenId, int index, CharSource source) {
            stack[stackIndex].end(tokenId, index, source);
            stack[stackIndex] = null;
            stackIndex--;
        }
    };




    public JsonEventAbstractParser(boolean objectsKeysCanBeEncoded, TokenEventListener tokenEventListener) {
        this.objectsKeysCanBeEncoded = objectsKeysCanBeEncoded;
        this.tokenEventListener = tokenEventListener;
    }


    class ScalarListener implements TokenEventListener {
        final int tokenType;
        int startIndex;
        ScalarListener(final int tokenType) {
            this.tokenType = tokenType;
        }

        @Override
        public void start(int tokenId, int index, CharSource source) {
            startIndex = index;
        }

        @Override
        public void end(int tokenId, int index, CharSource source) {
            tokenList.add(new Token(startIndex, index, tokenType));
        }

        @Override
        public String toString() {
            return "ScalarListener{" +
                    "tokenType=" + TokenTypes.getTypeName(tokenType) +
                    ", startIndex=" + startIndex +
                    '}';
        }
    }

    class ComplexListener implements TokenEventListener {
        final int tokenType;
        int startIndex;
        int tokenListIndex;

        ComplexListener(final int tokenType) {
            this.tokenType = tokenType;
        }

        @Override
        public void start(int tokenId, int index, CharSource source) {
            startIndex = index;
            tokenListIndex = tokenList.getIndex();
            tokenList.placeHolder();
        }

        @Override
        public void end(int tokenId, int index, CharSource source) {
            tokenList.set(tokenListIndex, new Token(startIndex, index, tokenType));
        }

        @Override
        public String toString() {
            return "ComplexListener{" +
                    "tokenType=" + TokenTypes.getTypeName(tokenType) +
                    ", startIndex=" + startIndex +
                    ", tokenListIndex=" + tokenListIndex +
                    '}';
        }
    }


    @Override
    public List<Token> scan(final CharSource source) {
        tokenList = new TokenList();
        this.parseWithEvents(source, base);
        return tokenList;
    }

    @Override
    public TokenEventListener tokenEvents() {
        return this.tokenEventListener;
    }

    @Override
    public RootNode parse(CharSource source) {
        return new RootNode((TokenList) scan(source), source, objectsKeysCanBeEncoded);
    }
}
