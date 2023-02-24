package com.cloudurable.jparse.parser;

import com.cloudurable.jparse.node.RootNode;
import com.cloudurable.jparse.node.support.TokenList;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.source.support.UnexpectedCharacterException;
import com.cloudurable.jparse.token.Token;
import com.cloudurable.jparse.token.TokenEventListener;
import com.cloudurable.jparse.token.TokenTypes;

import java.util.List;


public class JsonEventParser implements EventParser, IndexOverlayParser {
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
    private final boolean objectsKeysCanBeEncoded;
    private final TokenEventListener stringListener = new ScalarListener(TokenTypes.STRING_TOKEN);
    private final TokenEventListener floatListener = new ScalarListener(TokenTypes.FLOAT_TOKEN);
    private final TokenEventListener intListener = new ScalarListener(TokenTypes.INT_TOKEN);
    private final TokenEventListener booleanListener = new ScalarListener(TokenTypes.BOOLEAN_TOKEN);
    private final TokenEventListener nullListener = new ScalarListener(TokenTypes.NULL_TOKEN);
    final TokenEventListener base = new TokenEventListener() {

        private int stackIndex = -1;
        private TokenEventListener[] stack = new TokenEventListener[8];

        @Override
        public void start(final int tokenId, final int index, final CharSource source) {

            final var listener = switch (tokenId) {
                case TokenTypes.OBJECT_TOKEN -> new ComplexListener(TokenTypes.OBJECT_TOKEN);
                case TokenTypes.ARRAY_TOKEN -> new ComplexListener(TokenTypes.ARRAY_TOKEN);
                case TokenTypes.ARRAY_ITEM_TOKEN -> arrayItemListener;
                case TokenTypes.ATTRIBUTE_KEY_TOKEN -> new ComplexListener(TokenTypes.ATTRIBUTE_KEY_TOKEN);
                case TokenTypes.ATTRIBUTE_VALUE_TOKEN -> new ComplexListener(TokenTypes.ATTRIBUTE_VALUE_TOKEN);
                case TokenTypes.STRING_TOKEN -> stringListener;
                case TokenTypes.FLOAT_TOKEN -> floatListener;
                case TokenTypes.INT_TOKEN -> intListener;
                case TokenTypes.BOOLEAN_TOKEN -> booleanListener;
                case TokenTypes.NULL_TOKEN -> nullListener;
                default -> exceptionListener;
            };

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
    private TokenList tokenList;


    public JsonEventParser(boolean objectsKeysCanBeEncoded) {
        this.objectsKeysCanBeEncoded = objectsKeysCanBeEncoded;
    }

    public JsonEventParser() {
        this(false);
    }

    @Override
    public void parse(CharSource source, final TokenEventListener event) {

        int ch = source.nextSkipWhiteSpace();

        for (; ch != ETX; ch = source.nextSkipWhiteSpace()) {

            switch (ch) {
                case OBJECT_START_TOKEN:
                    parseObject(source, event);
                    break;

                case ARRAY_START_TOKEN:
                    parseArray(source, event);
                    break;

                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                    continue;

                case TRUE_BOOLEAN_START:
                    parseTrue(source, event);
                    break;

                case FALSE_BOOLEAN_START:
                    parseFalse(source, event);
                    break;

                case NULL_START:
                    parseNull(source, event);
                    break;

                case STRING_START_TOKEN:
                    parseString(source, event);
                    break;

                case NUM_0:
                case NUM_1:
                case NUM_2:
                case NUM_3:
                case NUM_4:
                case NUM_5:
                case NUM_6:
                case NUM_7:
                case NUM_8:
                case NUM_9:
                case MINUS:
                case PLUS:
                    parseNumber(source, event);
                    break;

                default:
                    throw new UnexpectedCharacterException("Scanning JSON", "Unexpected character", source, (char) ch);
            }

        }
    }

    private void parseFalse(final CharSource source, final TokenEventListener event) {
        event.start(TokenTypes.BOOLEAN_TOKEN, source.getIndex(), source);
        event.end(TokenTypes.BOOLEAN_TOKEN, source.findFalseEnd(), source);
    }

    private void parseTrue(CharSource source, final TokenEventListener event) {
        event.start(TokenTypes.BOOLEAN_TOKEN, source.getIndex(), source);
        event.end(TokenTypes.BOOLEAN_TOKEN, source.findTrueEnd(), source);
    }

    private void parseNull(CharSource source, final TokenEventListener event) {
        event.start(TokenTypes.NULL_TOKEN, source.getIndex(), source);
        event.end(TokenTypes.NULL_TOKEN, source.findNullEnd(), source);
    }

    private void parseArray(final CharSource source, final TokenEventListener event) {
        event.start(TokenTypes.ARRAY_TOKEN, source.getIndex(), source);
        boolean done = false;
        while (!done) {
            done = parseArrayItem(source, event);
        }
        event.end(TokenTypes.ARRAY_TOKEN, source.getIndex() + 1, source);
    }

    private boolean parseArrayItem(CharSource source, final TokenEventListener event) {
        int ch = source.nextSkipWhiteSpace();

        forLoop:
        for (; ch != ETX; ch = source.nextSkipWhiteSpace()) {

            switch (ch) {
                case OBJECT_START_TOKEN:
                    event.start(TokenTypes.ARRAY_ITEM_TOKEN, source.getIndex(), source);
                    parseObject(source, event);
                    event.end(TokenTypes.ARRAY_ITEM_TOKEN, source.getIndex(), source);
                    break;

                case ARRAY_START_TOKEN:
                    event.start(TokenTypes.ARRAY_ITEM_TOKEN, source.getIndex(), source);
                    parseArray(source, event);
                    event.end(TokenTypes.ARRAY_ITEM_TOKEN, source.getIndex(), source);
                    break;

                case TRUE_BOOLEAN_START:
                    event.start(TokenTypes.ARRAY_ITEM_TOKEN, source.getIndex(), source);
                    parseTrue(source, event);
                    event.end(TokenTypes.ARRAY_ITEM_TOKEN, source.getIndex(), source);
                    break;

                case FALSE_BOOLEAN_START:
                    event.start(TokenTypes.ARRAY_ITEM_TOKEN, source.getIndex(), source);
                    parseFalse(source, event);
                    event.end(TokenTypes.ARRAY_ITEM_TOKEN, source.getIndex(), source);
                    break;

                case NULL_START:
                    event.start(TokenTypes.ARRAY_ITEM_TOKEN, source.getIndex(), source);
                    parseNull(source, event);
                    event.end(TokenTypes.ARRAY_ITEM_TOKEN, source.getIndex(), source);
                    break;

                case STRING_START_TOKEN:
                    event.start(TokenTypes.ARRAY_ITEM_TOKEN, source.getIndex(), source);
                    parseString(source, event);
                    event.end(TokenTypes.ARRAY_ITEM_TOKEN, source.getIndex(), source);
                    break;

                case NUM_0:
                case NUM_1:
                case NUM_2:
                case NUM_3:
                case NUM_4:
                case NUM_5:
                case NUM_6:
                case NUM_7:
                case NUM_8:
                case NUM_9:
                case MINUS:
                case PLUS:
                    event.start(TokenTypes.ARRAY_ITEM_TOKEN, source.getIndex(), source);
                    parseNumber(source, event);
                    event.end(TokenTypes.ARRAY_ITEM_TOKEN, source.getIndex(), source);
                    if (source.getCurrentChar() == ARRAY_END_TOKEN || source.getCurrentChar() == LIST_SEP) {
                        if (source.getCurrentChar() == ARRAY_END_TOKEN) {
                            return true;
                        }
                    }
                    break;

                case ARRAY_END_TOKEN:
                case LIST_SEP:
                    break forLoop;

                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                    continue;

                default:
                    throw new UnexpectedCharacterException("Parsing Array Item", "Unexpected character", source, (char) ch);

            }
        }
        return ch == ARRAY_END_TOKEN;
    }

    private void parseNumber(final CharSource source, final TokenEventListener event) {
        final int startIndex = source.getIndex();
        final var numberParse = source.findEndOfNumber();
        final int token = numberParse.wasFloat() ? TokenTypes.FLOAT_TOKEN : TokenTypes.INT_TOKEN;
        event.start(token, startIndex, source);
        event.end(token, numberParse.endIndex(), source);
    }

    private void parseKey(final CharSource source, final TokenEventListener event) {
        int ch = source.nextSkipWhiteSpace();
        event.start(TokenTypes.ATTRIBUTE_KEY_TOKEN, source.getIndex(), source);
        boolean done = false;

        forLoop:
        for (; ch != ETX; ch = source.nextSkipWhiteSpace()) {
            switch (ch) {

                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                    continue;

                case STRING_START_TOKEN:
                    final int strStartIndex = source.getIndex();
                    event.start(TokenTypes.STRING_TOKEN, strStartIndex + 1, source);
                    final int strEndIndex;
                    if (objectsKeysCanBeEncoded) {
                        strEndIndex = source.findEndOfEncodedString();
                    } else {
                        strEndIndex = source.findEndString();
                    }
                    event.end(TokenTypes.STRING_TOKEN, strEndIndex, source);
                    break;

                case ATTRIBUTE_SEP:
                    event.end(TokenTypes.ATTRIBUTE_KEY_TOKEN, source.getIndex(), source);
                    done = true;
                    break forLoop;

                default:
                    throw new UnexpectedCharacterException("Parsing Object Key", "Unexpected character", source, (char) ch);
            }
        }

        if (!done) {
            throw new UnexpectedCharacterException("Parsing Object Key", "Should be done but not", source);
        }
    }

    private boolean parseValue(final CharSource source, final TokenEventListener event) {
        int ch = source.nextSkipWhiteSpace();
        event.start(TokenTypes.ATTRIBUTE_VALUE_TOKEN, source.getIndex(), source);

        forLoop:
        for (; ch != ETX; ch = source.nextSkipWhiteSpace()) {
            switch (ch) {
                case OBJECT_START_TOKEN:
                    parseObject(source, event);
                    break;

                case ARRAY_START_TOKEN:
                    parseArray(source, event);
                    break;

                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                    continue;

                case TRUE_BOOLEAN_START:
                    parseTrue(source, event);
                    break;

                case FALSE_BOOLEAN_START:
                    parseFalse(source, event);
                    break;

                case NULL_START:
                    parseNull(source, event);
                    break;

                case STRING_START_TOKEN:
                    parseString(source, event);
                    break;

                case NUM_0:
                case NUM_1:
                case NUM_2:
                case NUM_3:
                case NUM_4:
                case NUM_5:
                case NUM_6:
                case NUM_7:
                case NUM_8:
                case NUM_9:
                case MINUS:
                case PLUS:
                    parseNumber(source, event);
                    if (source.getCurrentChar() == OBJECT_ATTRIBUTE_SEP) {
                        event.end(TokenTypes.ATTRIBUTE_VALUE_TOKEN, source.getIndex(), source);
                        return false;
                    }
                    if (source.getCurrentChar() == OBJECT_END_TOKEN) {
                        event.end(TokenTypes.ATTRIBUTE_VALUE_TOKEN, source.getIndex(), source);
                        return true;
                    } else {
                        break;
                    }

                case OBJECT_END_TOKEN:
                case OBJECT_ATTRIBUTE_SEP:
                    event.end(TokenTypes.ATTRIBUTE_VALUE_TOKEN, source.getIndex(), source);
                    break forLoop;

                default:
                    throw new UnexpectedCharacterException("Parsing Value", "Unexpected character", source, ch);
            }
        }
        return ch == OBJECT_END_TOKEN;
    }

    private void parseString(final CharSource source, final TokenEventListener event) {
        event.start(TokenTypes.STRING_TOKEN, source.getIndex() + 1, source);
        event.end(TokenTypes.STRING_TOKEN, source.findEndOfEncodedString(), source);
    }

    private void parseObject(final CharSource source, final TokenEventListener event) {
        event.start(TokenTypes.OBJECT_TOKEN, source.getIndex(), source);
        boolean done = false;
        while (!done) {
            parseKey(source, event);
            done = parseValue(source, event);
        }
        event.end(TokenTypes.OBJECT_TOKEN, source.getIndex() + 1, source);
    }

    @Override
    public List<Token> scan(final CharSource source) {
        tokenList = new TokenList();
        this.parse(source, base);
        return tokenList;
    }

    @Override
    public RootNode parse(CharSource source) {
        return new RootNode((TokenList) scan(source), source, objectsKeysCanBeEncoded);
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
}
