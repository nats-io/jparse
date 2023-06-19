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
package io.nats.jparse.parser;


import io.nats.jparse.node.support.ParseConstants;
import io.nats.jparse.parser.event.JsonEventFastParser;
import io.nats.jparse.parser.event.JsonEventStrictParser;
import io.nats.jparse.parser.functable.JsonFuncParser;
import io.nats.jparse.parser.functable.JsonParserFunctions;
import io.nats.jparse.parser.functable.ParseFunction;
import io.nats.jparse.parser.functable.ParsePartFunction;
import io.nats.jparse.parser.indexoverlay.JsonFastParser;
import io.nats.jparse.parser.indexoverlay.JsonStrictParser;
import io.nats.jparse.source.support.UnexpectedCharacterException;
import io.nats.jparse.token.TokenEventListener;

import java.util.Arrays;

/**
 * Builder class for creating instances of `JsonParser`.
 *
 * @see JsonParserBuilder#build() for details on how the build logic is formed.
 */
public class JsonParserBuilder {

    /**
     * The `TokenEventListener` to use when parsing JSON.
     */
    private TokenEventListener tokenEventListener;

    /**
     * Whether to use strict parsing when parsing JSON.
     */
    private boolean strict = false;

    /**
     * Whether object keys can be encoded when parsing JSON.
     */
    private boolean objectsKeysCanBeEncoded;

    /**
     * Whether to allow hash comments when parsing JSON.
     */
    private boolean allowHashComment;

    /**
     * Whether to allow slash-slash comments when parsing JSON.
     */
    private boolean allowSlashSlashComment;

    /**
     * Whether to allow slash-star comments when parsing JSON.
     */
    private boolean allowSlashStarComment;

    /**
     * The table of parse functions to use when parsing JSON.
     */
    private ParseFunction[] funcTable;

    /**
     * The parse function to use for keys when parsing JSON.
     */
    private ParsePartFunction parseKey;

    /**
     * The default parse function to use when parsing JSON.
     */
    private ParseFunction defaultFunc;

    /**
     * Whether to support unquoted keys when parsing JSON.
     */
    private boolean supportNoQuoteKeys;

    /**
     * Returns a new instance of `JsonParserBuilder`.
     *
     * @return a new instance
     */
    public static JsonParserBuilder builder() {
        return new JsonParserBuilder();
    }

    public boolean isSupportNoQuoteKeys() {
        return supportNoQuoteKeys;
    }

    /**
     * Sets whether to support unquoted keys when parsing JSON.
     *
     * @param supportNoQuoteKeys `true` to support unquoted keys, `false` otherwise
     * @return the modified builder
     */
    public JsonParserBuilder setSupportNoQuoteKeys(boolean supportNoQuoteKeys) {
        this.supportNoQuoteKeys = supportNoQuoteKeys;
        return this;
    }

    /**
     * Gets the `TokenEventListener` to use when parsing JSON.
     *
     * @return the `TokenEventListener`
     */
    public TokenEventListener tokenEventListener() {
        return tokenEventListener;
    }

    /**
     * Sets the `TokenEventListener` to use when parsing JSON.
     *
     * @param tokenEventListener the `TokenEventListener` to set
     * @return the modified builder
     */
    public JsonParserBuilder setTokenEventListener(TokenEventListener tokenEventListener) {
        this.tokenEventListener = tokenEventListener;
        return this;
    }

    /**
     * Gets whether to use strict parsing when parsing JSON.
     *
     * @return `true` if strict parsing is used, `false` otherwise
     */
    public boolean strict() {
        return strict;
    }

    /**
     * Sets whether to use strict parsing when parsing JSON.
     *
     * @param strict `true` to use strict parsing, `false` otherwise
     * @return the modified builder
     */
    public JsonParserBuilder setStrict(boolean strict) {
        this.strict = strict;
        return this;
    }

    /**
     * Gets whether object keys can be encoded when parsing JSON.
     *
     * @return `true` if object keys can be encoded, `false` otherwise
     */
    public boolean objectsKeysCanBeEncoded() {
        return objectsKeysCanBeEncoded;
    }

    /**
     * Sets whether object keys can be encoded when parsing JSON.
     *
     * @param objectsKeysCanBeEncoded `true` to allow encoded keys, `false` otherwise
     * @return the modified builder
     */
    public JsonParserBuilder setObjectsKeysCanBeEncoded(boolean objectsKeysCanBeEncoded) {
        this.objectsKeysCanBeEncoded = objectsKeysCanBeEncoded;
        return this;
    }

    /**
     * Gets whether to allow hash comments when parsing JSON.
     *
     * @return `true` if hash comments are allowed, `false` otherwise
     */
    public boolean isAllowHashComment() {
        return allowHashComment;
    }

    /**
     * Sets whether to allow hash comments when parsing JSON.
     *
     * @param allowHashComment `true` to allow hash comments, `false` otherwise
     * @return the modified builder
     */
    public JsonParserBuilder setAllowHashComment(boolean allowHashComment) {
        this.allowHashComment = allowHashComment;
        return this;
    }

    /**
     * Gets whether to allow slash-slash comments when parsing JSON.
     *
     * @return `true` if slash-slash comments are allowed, `false` otherwise
     */
    public boolean isAllowSlashSlashComment() {
        return allowSlashSlashComment;
    }

    /**
     * Sets whether to allow slash-slash comments when parsing JSON.
     *
     * @param allowSlashSlashComment `true` to allow slash-slash comments, `false` otherwise
     * @return the modified builder
     */
    public JsonParserBuilder setAllowSlashSlashComment(boolean allowSlashSlashComment) {
        this.allowSlashSlashComment = allowSlashSlashComment;
        return this;
    }

    /**
     * Gets whether to allow slash-star comments when parsing JSON.
     *
     * @return `true` if slash-star comments are allowed, `false` otherwise
     */
    public boolean isAllowSlashStarComment() {
        return allowSlashStarComment;
    }

    /**
     * Sets whether to allow slash-star comments when parsing JSON.
     *
     * @param allowSlashStarComment `true` to allow slash-star comments, `false` otherwise
     * @return the modified builder
     */
    public JsonParserBuilder setAllowSlashStarComment(boolean allowSlashStarComment) {
        this.allowSlashStarComment = allowSlashStarComment;
        return this;
    }

    /**
     * Gets the table of parse functions to use when parsing JSON.
     *
     * @return the parse function table
     */
    public ParseFunction[] getFuncTable() {
        if (funcTable == null) {
            funcTable = new ParseFunction[256];
            Arrays.fill(funcTable, getDefaultFunc());
        }
        return funcTable;
    }

    /**
     * Sets the table of parse functions to use when parsing JSON.
     *
     * @param funcTable the parse function table to set
     * @return the modified builder
     */
    public JsonParserBuilder setFuncTable(ParseFunction[] funcTable) {
        this.funcTable = funcTable;
        return this;
    }

    /**
     * Gets the parse function to use for keys when parsing JSON.
     *
     * @return the parse function for keys
     */
    public ParsePartFunction getParseKey() {
        if (parseKey == null) {
            if (supportNoQuoteKeys()) {
                parseKey = JsonParserFunctions::parseKeyNoQuote;
            } else {
                parseKey = JsonParserFunctions::parseKeyWithEncode;
            }
        }
        return parseKey;
    }

    /**
     * Sets the parse function to use for keys when parsing JSON.
     *
     * @param parseKey the parse function for keys to set
     * @return the modified builder
     */
    public JsonParserBuilder setParseKey(ParsePartFunction parseKey) {
        this.parseKey = parseKey;
        return this;
    }

    /**
     * Gets the default parse function to use when parsing JSON.
     *
     * @return the default parse function
     */
    public ParseFunction getDefaultFunc() {
        if (defaultFunc == null) {
            this.defaultFunc = JsonParserFunctions.defaultFunc;
        }
        return defaultFunc;
    }

    /**
     * Sets the default parse function to use when parsing JSON.
     *
     * @param defaultFunc the default parse function to set
     * @return the modified builder
     */
    public JsonParserBuilder setDefaultFunc(ParseFunction defaultFunc) {
        this.defaultFunc = defaultFunc;
        return this;
    }

    /**
     * Gets whether to support unquoted keys when parsing JSON.
     *
     * @return `true` if unquoted keys are supported, `false` otherwise
     */
    public boolean supportNoQuoteKeys() {
        return supportNoQuoteKeys;
    }

    /**
     * Sets whether to allow comments when parsing JSON.
     *
     * @return `true` if comments are allowed, `false` otherwise
     */
    public JsonParserBuilder setAllowComments(boolean allowComments) {
        this.allowHashComment = allowComments;
        this.allowSlashSlashComment = allowComments;
        this.allowSlashStarComment = allowComments;
        return this;
    }

    /**
     * Returns a new instance of `JsonParser`.
     * <p>
     * The `JsonParserBuilder.build()` function returns an instance of `JsonParser`.
     * If any of the following conditions are met: `supportNoQuoteKeys` is `true`,
     * `allowHashComment` is `true`, `allowSlashSlashComment` is `true`,
     * `allowSlashStarComment` is `true`, or `parseKey` is not `null`,
     * the function sets up a parse function table for the JSON parser. It then
     * returns a new instance of `JsonParser`.
     * <p>
     * The function first checks if any of the conditions for setting up a parse
     * function table are true. If so, it sets up an array of `ParseFunction`
     * objects and populates it with parsing functions based on the type of JSON
     * element being parsed.
     * <p>
     * If `isAllowHashComment()` is true, the function sets the hash character `#`
     * to a function that skips the comment until the end of the line.
     * <p>
     * If `isSupportNoQuoteKeys()` is true and `getParseKey()` is null, the function
     * sets the parse function for keys to `JsonParserFunctions::parseKeyNoQuote`.
     * <p>
     * If `isAllowSlashStarComment()` or `isAllowSlashSlashComment()` is true, the
     * function sets the forward slash `/` character to a function that handles
     * comments. If the next character is another forward slash, the function skips
     * to the end of the line. If the next character is an asterisk, it skips
     * everything until it reaches a closing asterisk-slash sequence.
     * <p>
     * After setting up the parse function table, the function returns a new
     * instance of `JsonFuncParser` with the table, default parse function, and
     * parse function for keys.
     * <p>
     * If none of the above conditions are met but `strict()` is true, the function
     * returns a new instance of `JsonStrictParser`. If `strict()` is false, the
     * function returns a new instance of `JsonFastParser`.
     * <p>
     * The `JsonParserBuilder.build()` function checks if any of the conditions for
     * setting up a parse function table are true. If so, it sets up an array of
     * `ParseFunction` objects and populates it with parsing functions. If none of
     * those conditions are true but `strict()` is true, it returns a new instance
     * of `JsonStrictParser`. If `strict()` is false, it returns a new instance of
     * `JsonFastParser`.
     * <p>
     * The `ParseFunction` array is populated with parsing functions based on the
     * type of JSON element being parsed. For example, the string start token is
     * assigned the `JsonParserFunctions::parseString` function, while the null
     * start token is assigned the `JsonParserFunctions::parseNull` function.
     * The `JsonParserFunctions::parseNumber` function is assigned to all numbers,
     * and the minus and plus signs.
     * <p>
     * If `isAllowHashComment()` is true, the function sets the hash character `#`
     * to a function that skips the comment until the end of the line.
     * <p>
     * If `isSupportNoQuoteKeys()` is true and `getParseKey()` is null, the function
     * sets the parse function for keys to `JsonParserFunctions::parseKeyNoQuote`.
     * <p>
     * If `isAllowSlashStarComment()` or `isAllowSlashSlashComment()` is true, the
     * function sets the forward slash `/` character to a function that handles
     * comments. If the next character is another forward slash, the function skips
     * to the end of the line. If the next character is an asterisk, it skips
     * everything until it reaches a closing asterisk-slash  sequence.
     * <p>
     * After setting up the parse function table, the function returns a new
     * instance of `JsonFuncParser` with the parse function table, default parse function, and
     * parse function for keys.
     *
     * @return a new instance of `JsonParser`
     */
    public JsonParser build() {

        if (strict()) {
            return new JsonStrictParser(objectsKeysCanBeEncoded());
        } else if (isSupportNoQuoteKeys() || isAllowHashComment() || isAllowSlashSlashComment() || isAllowSlashStarComment() || parseKey != null) {
            final ParseFunction[] funcTable = this.getFuncTable();
            funcTable[ParseConstants.STRING_START_TOKEN] = JsonParserFunctions::parseString;
            funcTable[ParseConstants.NULL_START] = JsonParserFunctions::parseNull;
            funcTable[ParseConstants.TRUE_BOOLEAN_START] = JsonParserFunctions::parseTrue;
            funcTable[ParseConstants.FALSE_BOOLEAN_START] = JsonParserFunctions::parseFalse;
            for (int i = ParseConstants.NUM_0; i < ParseConstants.NUM_9 + 1; i++) {
                funcTable[i] = JsonParserFunctions::parseNumber;
            }
            funcTable[ParseConstants.MINUS] = JsonParserFunctions::parseNumber;
            funcTable[ParseConstants.PLUS] = JsonParserFunctions::parseNumber;

            if (isAllowHashComment()) {
                funcTable['#'] = (source, tokens) -> source.findChar('\n');
            }

            if (isSupportNoQuoteKeys() && getParseKey() == null) {
                setParseKey(JsonParserFunctions::parseKeyNoQuote);
            }

            if (isAllowSlashStarComment() || isAllowSlashSlashComment()) {
                funcTable['/'] = (source, tokens) -> {
                    int next = source.next();
                    if (next == '/') {
                        source.findChar('\n');
                    } else if (next == '*') {
                        loop:
                        while (source.findChar('*')) {
                            switch (source.next()) {
                                case '/':
                                    break loop;
                                case ParseConstants.ETX:
                                    throw new UnexpectedCharacterException("Comment parse", "End of stream", source);
                            }
                        }
                    }
                };
            }
            return new JsonFuncParser(objectsKeysCanBeEncoded(), Arrays.copyOf(funcTable, funcTable.length),
                    this.getDefaultFunc(), this.getParseKey());
        } else {
            return new JsonFastParser(objectsKeysCanBeEncoded());
        }
    }


    /**
     * Returns a new instance of `JsonEventParser`.
     *
     * @return a new instance of `JsonEventParser`
     */
    public JsonEventParser buildEventParser() {
        if (strict()) {
            return new JsonEventStrictParser(objectsKeysCanBeEncoded(), tokenEventListener());
        } else {
            return new JsonEventFastParser(objectsKeysCanBeEncoded(), tokenEventListener());
        }
    }

    /**
     * Returns a new instance of `JsonParserBuilder`.
     *
     * @return a new instance
     */
    public JsonParserBuilder cloneBuilder() {
        return new JsonParserBuilder().setStrict(strict()).setTokenEventListener(tokenEventListener()).setObjectsKeysCanBeEncoded(objectsKeysCanBeEncoded());
    }

}
