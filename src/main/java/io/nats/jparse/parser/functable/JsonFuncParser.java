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
package io.nats.jparse.parser.functable;

import io.nats.jparse.node.RootNode;
import io.nats.jparse.node.support.TokenList;
import io.nats.jparse.parser.JsonParser;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.source.support.UnexpectedCharacterException;
import io.nats.jparse.token.Token;
import io.nats.jparse.token.TokenTypes;

import java.util.List;

/**
 * The `JsonFuncParser` class implements the `JsonParser` interface using a function table to define the
 * behavior of the parser. The function table is an array of `ParseFunction` objects, one for each possible
 * character value. The function table is used to determine which function to call when parsing a particular
 * character. The function table can be customized by subclassing this class and overriding the `initFuncTable`
 * method. The function table can be configured via the `JsonParserBuilder` and the JsonFuncParser can be created with a
 * `JsonParserBuilder`.
 *
 * @see io.nats.jparse.parser.JsonParserBuilder
 */
public class JsonFuncParser implements JsonParser {

    private final boolean objectsKeysCanBeEncoded;

    private final ParseFunction[] funcTable;
    private final ParsePartFunction parseKey;
    private final ParseFunction defaultFunc;


    /**
     * Create a new `JsonFuncParser` with the default function table.
     * The default function table is defined by the `JsonParserFunctions` class.
     *
     * @param objectsKeysCanBeEncoded If true, the parser will encode the keys of objects when parsing
     * @param funcTable function table
     * @param defaultFunc default function if no function is found in the table.
     * @param parseKey the function to parse keys.
     * @see io.nats.jparse.parser.functable.JsonParserFunctions
     */
    public JsonFuncParser(final boolean objectsKeysCanBeEncoded, final ParseFunction[] funcTable,
                          final ParseFunction defaultFunc, final ParsePartFunction parseKey) {

        this.objectsKeysCanBeEncoded = objectsKeysCanBeEncoded;
        this.funcTable = funcTable;
        this.defaultFunc = defaultFunc == null ? JsonParserFunctions.defaultFunc : defaultFunc;

        if (parseKey == null) {
            if (objectsKeysCanBeEncoded) {
                this.parseKey = JsonParserFunctions::parseKeyWithEncode;
            } else {
                this.parseKey = JsonParserFunctions::parseKeyNoEncode;
            }
        } else {
            this.parseKey = parseKey;
        }

        funcTable[OBJECT_START_TOKEN] = this::parseObject;
        funcTable[ARRAY_START_TOKEN] = this::parseArray;

    }

    /**
     * Scan a character source and return a list of tokens representing the JSON string.
     *
     * @param source The character source to scan
     * @return A list of tokens representing the JSON
     */
    @Override
    public List<Token> scan(final CharSource source) {
        return scan(source, new TokenList());
    }

    /**
     * Parse a character source and return a root node representing the parsed JSON.
     *
     * @param source The character source to parse
     * @return A root node representing the parsed JSON
     */
    @Override
    public RootNode parse(CharSource source) {
        return new RootNode((TokenList) scan(source), source, objectsKeysCanBeEncoded);
    }

    private List<Token> scan(final CharSource source, TokenList tokens) {
        int ch = source.nextSkipWhiteSpace();
        doParse(source, tokens, ch);
        return tokens;
    }

    private void doParse(final CharSource source, final TokenList tokens, final int ch) {
        if (ch < 256) {
            funcTable[ch].parse(source, tokens);
        } else {
            defaultFunc.parse(source, tokens);
        }
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

                case ARRAY_END_TOKEN:
                    source.next();
                    return true;

                case ARRAY_SEP:
                    source.next();
                    return false;

                default:
                    doParse(source, tokens, ch);
                    break forLoop;
            }
        }

        if (source.getCurrentChar() == ARRAY_END_TOKEN) {
            source.next();
            return true;
        }
        return false;
    }


    private boolean parseValue(final CharSource source, TokenList tokens) {
        int ch = source.nextSkipWhiteSpace();
        final int startIndex = source.getIndex();
        final int tokenListIndex = tokens.getIndex();
        tokens.placeHolder();

        doParse(source, tokens, ch);

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


    private void parseObject(final CharSource source, TokenList tokens) {
        final int startSourceIndex = source.getIndex();
        final int tokenListIndex = tokens.getIndex();
        tokens.placeHolder();

        boolean done = false;
        while (!done) {
            done = parseKey.parse(source, tokens);
            if (!done)
                done = parseValue(source, tokens);
        }
        source.next();
        tokens.set(tokenListIndex, new Token(startSourceIndex, source.getIndex(), TokenTypes.OBJECT_TOKEN));
    }


}
