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
package io.nats.jparse.parser.indexoverlay;

import io.nats.jparse.node.RootNode;
import io.nats.jparse.node.support.NumberParseResult;
import io.nats.jparse.node.support.TokenList;
import io.nats.jparse.parser.JsonParser;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.source.support.UnexpectedCharacterException;
import io.nats.jparse.token.Token;
import io.nats.jparse.token.TokenTypes;

import java.util.List;

public class JsonFastParser implements JsonParser {

    private final boolean objectsKeysCanBeEncoded;


    public JsonFastParser(boolean objectsKeysCanBeEncoded) {
        this.objectsKeysCanBeEncoded = objectsKeysCanBeEncoded;
    }

    @Override
    public List<Token> scan(final CharSource source) {
         return scan(source, new TokenList());
    }

    @Override
    public RootNode parse(CharSource source) {
        return new RootNode((TokenList) scan(source), source, objectsKeysCanBeEncoded);
    }

    private List<Token> scan(final CharSource source, TokenList tokens) {

        int ch = source.nextSkipWhiteSpace();

        switch (ch) {
            case OBJECT_START_TOKEN:
                parseObject(source, tokens);
                break;

            case ARRAY_START_TOKEN:
                parseArray(source, tokens);
                break;

            case TRUE_BOOLEAN_START:
                parseTrue(source, tokens);
                break;

            case FALSE_BOOLEAN_START:
                parseFalse(source, tokens);
                break;

            case NULL_START:
                parseNull(source, tokens);
                break;

            case STRING_START_TOKEN:
                parseString(source, tokens);
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
                parseNumber(source, tokens);
                break;

            default:
                throw new UnexpectedCharacterException("Scanning JSON", "Unexpected character", source, (char) ch);

        }

        return tokens;
    }

    private void parseFalse(CharSource source, TokenList tokens) {
        int start = source.getIndex();
        int end = source.findFalseEnd();
        tokens.add(new Token(start, end, TokenTypes.BOOLEAN_TOKEN));
    }

    private void parseTrue(CharSource source, TokenList tokens) {
        int start = source.getIndex();
        int end = source.findTrueEnd();
        tokens.add(new Token(start, end, TokenTypes.BOOLEAN_TOKEN));
    }

    private void parseNull(CharSource source, TokenList tokens) {
        int start = source.getIndex();
        int end = source.findNullEnd();
        tokens.add(new Token(start, end, TokenTypes.NULL_TOKEN));
    }

    private void parseArray(final CharSource source, final TokenList tokens) {
        final int startSourceIndex = source.getIndex();
        final int tokenListIndex = tokens.getIndex();
        tokens.placeHolder();

        boolean done = false;
        while (!done) {
            done = parseArrayItem(source, tokens);

        }
        final Token arrayToken = new Token(startSourceIndex, source.getIndex(), TokenTypes.ARRAY_TOKEN);
        tokens.set(tokenListIndex, arrayToken);
    }

    private boolean parseArrayItem(CharSource source, TokenList tokens) {
        char ch = (char) source.nextSkipWhiteSpace();

        forLoop:
        for (; ch != ETX; ch = (char) source.nextSkipWhiteSpace()) {

            switch (ch) {
                case OBJECT_START_TOKEN:
                    parseObject(source, tokens);
                    break forLoop;

                case ARRAY_START_TOKEN:
                    parseArray(source, tokens);
                    break forLoop;

                case TRUE_BOOLEAN_START:
                    parseTrue(source, tokens);
                    break forLoop;

                case FALSE_BOOLEAN_START:
                    parseFalse(source, tokens);
                    break forLoop;


                case NULL_START:
                    parseNull(source, tokens);
                    break forLoop;

                case STRING_START_TOKEN:
                    parseString(source, tokens);
                    break forLoop;

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
                    parseNumber(source, tokens);
                    break forLoop;

                case ARRAY_END_TOKEN:
                    source.next();
                    return true;

                case ARRAY_SEP:
                    source.next();
                    return false;

                default:
                    throw new UnexpectedCharacterException("Parsing Array Item", "Unexpected character", source, (char) ch);


            }
        }

        if (source.getCurrentChar() == ARRAY_END_TOKEN) {
            source.next();
            return true;
        }
        return false;
    }


    private void parseNumber(final CharSource source, TokenList tokens) {
        final int startIndex = source.getIndex();
        final NumberParseResult numberParse = source.findEndOfNumberFast();
        tokens.add(new Token(startIndex, numberParse.endIndex(), numberParse.wasFloat() ? TokenTypes.FLOAT_TOKEN : TokenTypes.INT_TOKEN));
    }


    private boolean parseKey(final CharSource source, final TokenList tokens) {

        int ch = source.nextSkipWhiteSpace();
        final int startIndex = source.getIndex() - 1;
        final int tokenListIndex = tokens.getIndex();
        tokens.placeHolder();
        boolean found = false;

        switch (ch) {

            case STRING_START_TOKEN:
                final int strStartIndex = startIndex + 1;
                final int strEndIndex;
                if (objectsKeysCanBeEncoded) {
                    strEndIndex = source.findEndOfEncodedString();
                } else {
                    strEndIndex = source.findEndString();
                }
                tokens.add(new Token(strStartIndex + 1, strEndIndex, TokenTypes.STRING_TOKEN));
                found = true;
                break;

            case OBJECT_END_TOKEN:
                tokens.undoPlaceholder();
                return true;

            default:
                throw new UnexpectedCharacterException("Parsing key", "Unexpected character found", source);
        }



            boolean done = source.findObjectEndOrAttributeSep();

            if (!done && found) {
                tokens.set(tokenListIndex, new Token(startIndex + 1, source.getIndex(), TokenTypes.ATTRIBUTE_KEY_TOKEN));
            } else if (found && done) {

                throw new UnexpectedCharacterException("Parsing key", "Not found", source);

            }

            return done;

    }


    private boolean parseValue(final CharSource source, TokenList tokens) {
        int ch = source.nextSkipWhiteSpace();
        final int startIndex = source.getIndex();
        final int tokenListIndex = tokens.getIndex();
        tokens.placeHolder();

        switch (ch) {
            case OBJECT_START_TOKEN:
                parseObject(source, tokens);
                break;

            case ARRAY_START_TOKEN:
                parseArray(source, tokens);
                break;

            case TRUE_BOOLEAN_START:
                parseTrue(source, tokens);
                break;

            case FALSE_BOOLEAN_START:
                parseFalse(source, tokens);
                break;

            case NULL_START:
                parseNull(source, tokens);
                break;

            case STRING_START_TOKEN:
                parseString(source, tokens);
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
                parseNumber(source, tokens);
                break;

            default:
                throw new UnexpectedCharacterException("Parsing Value", "Unexpected character", source, ch);
        }

        ch = source.skipWhiteSpace();

        switch (ch) {
            case OBJECT_END_TOKEN:
                if (source.getIndex() == tokenListIndex) {
                    throw new UnexpectedCharacterException("Parsing Value", "Key separator before value", source);
                }
                tokens.set(tokenListIndex, new Token(startIndex, source.getIndex(), TokenTypes.ATTRIBUTE_VALUE_TOKEN));
                return true;
            case OBJECT_ATTRIBUTE_SEP:
                if (source.getIndex() == tokenListIndex) {
                    throw new UnexpectedCharacterException("Parsing Value", "Key separator before value", source);
                }
                tokens.set(tokenListIndex, new Token(startIndex, source.getIndex(), TokenTypes.ATTRIBUTE_VALUE_TOKEN));
                return false;

            default:
                throw new UnexpectedCharacterException("Parsing Value", "Unexpected character", source, source.getCurrentChar());

        }

    }

    private void parseString(final CharSource source, TokenList tokens) {
        final int startIndex = source.getIndex();
        final int endIndex = source.findEndOfEncodedStringFast();
        tokens.add(new Token(startIndex + 1, endIndex, TokenTypes.STRING_TOKEN));
    }


    private void parseObject(final CharSource source, TokenList tokens) {
        final int startSourceIndex = source.getIndex();
        final int tokenListIndex = tokens.getIndex();
        tokens.placeHolder();

        boolean done = false;
        while (!done) {
            done = parseKey(source, tokens);
            if (!done)
              done = parseValue(source, tokens);
        }
        source.next();
        tokens.set(tokenListIndex, new Token(startSourceIndex, source.getIndex(), TokenTypes.OBJECT_TOKEN));
    }


}
