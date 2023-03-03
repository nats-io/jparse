package com.cloudurable.jparse.parser;

import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.source.support.UnexpectedCharacterException;
import com.cloudurable.jparse.token.TokenEventListener;
import com.cloudurable.jparse.token.TokenTypes;


public class JsonEventFastParser extends JsonEventAbstractParser {




    public JsonEventFastParser(boolean objectsKeysCanBeEncoded, TokenEventListener tokenEventListener) {
        super(objectsKeysCanBeEncoded, tokenEventListener);
    }

    @Override
    public void parseWithEvents(CharSource source, final TokenEventListener event) {

        int ch = source.nextSkipWhiteSpace();


            switch (ch) {
                case OBJECT_START_TOKEN:
                    parseObject(source, event);
                    break;

                case ARRAY_START_TOKEN:
                    parseArray(source, event);
                    break;

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

            if (!done) {
                done = source.findCommaOrEnd();
            }
        }
        event.end(TokenTypes.ARRAY_TOKEN, source.getIndex(), source);
    }

    private boolean parseArrayItem(CharSource source, final TokenEventListener event) {

        char startChar = source.getCurrentChar();
        int ch = source.nextSkipWhiteSpace();

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
                            source.next();
                            return true;
                        }
                    }
                    break;

            case ARRAY_END_TOKEN:
                if (startChar == LIST_SEP) {
                    throw new UnexpectedCharacterException("Parsing Array Item", "Trailing comma", source, (char) ch);
                }
                source.next();
                return true;


            default:
                throw new UnexpectedCharacterException("Parsing Array Item", "Unexpected character", source, (char) ch);

        }

        return false;
    }

    private void parseNumber(final CharSource source, final TokenEventListener event) {
        final int startIndex = source.getIndex();
        final var numberParse = source.findEndOfNumber();
        final int tokenType = numberParse.wasFloat() ? TokenTypes.FLOAT_TOKEN : TokenTypes.INT_TOKEN;
        event.start(tokenType, startIndex, source);
        event.end(tokenType, numberParse.endIndex(), source);
    }

    private boolean parseKey(final CharSource source, final TokenEventListener event) {

        int ch = source.nextSkipWhiteSpace();
        event.start(TokenTypes.ATTRIBUTE_KEY_TOKEN, source.getIndex(), source);
        boolean found = false;


        switch (ch) {


            case STRING_START_TOKEN:
                final int strStartIndex = source.getIndex();
                event.start(TokenTypes.STRING_TOKEN, strStartIndex + 1, source);
                final int strEndIndex;
                if (objectsKeysCanBeEncoded) {
                    strEndIndex = source.findEndOfEncodedString();
                } else {
                    strEndIndex = source.findEndString();
                }
                found = true;
                event.end(TokenTypes.STRING_TOKEN, strEndIndex, source);
                break;

            case OBJECT_END_TOKEN:
                return true;

            default:
                throw new UnexpectedCharacterException("Parsing key", "Unexpected character found", source);
        }


        boolean done = source.findObjectEndOrAttributeSep();

        if (!done && found) {
            event.end(TokenTypes.ATTRIBUTE_KEY_TOKEN, source.getIndex(), source);
        } else if (found && done) {
            throw new UnexpectedCharacterException("Parsing key", "Not found", source);
        }
        return done;
    }

    private boolean parseValue(final CharSource source, final TokenEventListener event) {
        int ch = source.nextSkipWhiteSpace();
        event.start(TokenTypes.ATTRIBUTE_VALUE_TOKEN, source.getIndex(), source);

        switch (ch) {
            case OBJECT_START_TOKEN:
                parseObject(source, event);
                break;

            case ARRAY_START_TOKEN:
                parseArray(source, event);
                break;

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
                throw new UnexpectedCharacterException("Parsing Value", "Unexpected character", source, ch);
        }

        source.skipWhiteSpace();

        switch (source.getCurrentChar()) {
            case OBJECT_END_TOKEN:
                event.end(TokenTypes.ATTRIBUTE_VALUE_TOKEN, source.getIndex(), source);
                return true;
            case OBJECT_ATTRIBUTE_SEP:
                event.end(TokenTypes.ATTRIBUTE_VALUE_TOKEN, source.getIndex(), source);
                return false;

            default:
                throw new UnexpectedCharacterException("Parsing Value", "Unexpected character", source, source.getCurrentChar());

        }
    }

    private void parseString(final CharSource source, final TokenEventListener event) {
        event.start(TokenTypes.STRING_TOKEN, source.getIndex() + 1, source);
        event.end(TokenTypes.STRING_TOKEN, source.findEndOfEncodedStringFast(), source);
    }

    private void parseObject(final CharSource source, final TokenEventListener event) {
        event.start(TokenTypes.OBJECT_TOKEN, source.getIndex(), source);

        boolean done = false;
        while (!done) {
            done = parseKey(source, event);
            if (!done)
                done = parseValue(source, event);
        }

        if (source.getCurrentChar() != OBJECT_END_TOKEN) {
            throw new UnexpectedCharacterException("Parsing Object", "Unexpected character", source, source.getCurrentCharSafe());
        }
        source.next();
        event.end(TokenTypes.OBJECT_TOKEN, source.getIndex(), source);
    }


}
