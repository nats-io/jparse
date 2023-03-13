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
package com.cloudurable.jparse.parser;


import com.cloudurable.jparse.node.support.TokenList;
import com.cloudurable.jparse.parser.event.JsonEventFastParser;
import com.cloudurable.jparse.parser.event.JsonEventStrictParser;
import com.cloudurable.jparse.parser.functable.JsonFuncParser;
import com.cloudurable.jparse.parser.functable.JsonParserFunctions;
import com.cloudurable.jparse.parser.functable.ParseFunction;
import com.cloudurable.jparse.parser.functable.ParsePartFunction;
import com.cloudurable.jparse.parser.indexoverlay.JsonFastParser;
import com.cloudurable.jparse.parser.indexoverlay.JsonStrictParser;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.source.support.UnexpectedCharacterException;
import com.cloudurable.jparse.token.TokenEventListener;

import java.util.Arrays;

import static com.cloudurable.jparse.node.support.ParseConstants.*;

public class JsonParserBuilder {

    private TokenEventListener tokenEventListener;
    private boolean strict = false;
    private boolean objectsKeysCanBeEncoded;
    private boolean allowHashComment;
    private boolean allowSlashSlashComment;
    private boolean allowSlashStarComment;
    private ParseFunction[] funcTable;
    private ParsePartFunction parseKey;
    private  ParseFunction defaultFunc;
    private boolean supportNoQuoteKeys;


    public static JsonParserBuilder builder() {
        return new JsonParserBuilder();
    }


    public boolean isSupportNoQuoteKeys() {
        return supportNoQuoteKeys;
    }

    public JsonParserBuilder setSupportNoQuoteKeys(boolean supportNoQuoteKeys) {
        this.supportNoQuoteKeys = supportNoQuoteKeys;
        return this;
    }

    public ParseFunction getDefaultFunc() {
        if (defaultFunc == null) {
            this.defaultFunc = JsonParserFunctions.defaultFunc;
        }
        return defaultFunc;
    }

    public JsonParserBuilder setDefaultFunc(ParseFunction defaultFunc) {
        this.defaultFunc = defaultFunc;
        return this;
    }

    public ParseFunction[] getFuncTable() {
        if (funcTable == null) {
            funcTable = new ParseFunction[256];
            Arrays.fill(funcTable, defaultFunc);
        }
        return funcTable;
    }

    public JsonParserBuilder setFuncTable(ParseFunction[] funcTable) {
        this.funcTable = funcTable;
        return this;
    }

    public JsonParserBuilder setAllowComments(boolean allowComments) {
        this.allowHashComment = allowComments;
        this.allowSlashSlashComment = allowComments;
        this.allowSlashStarComment = allowComments;
        return this;
    }

    public boolean isAllowHashComment() {
        return allowHashComment;
    }

    public JsonParserBuilder setAllowHashComment(boolean allowHashComment) {
        this.allowHashComment = allowHashComment;
        return this;
    }

    public boolean isAllowSlashSlashComment() {
        return allowSlashSlashComment;
    }

    public JsonParserBuilder setAllowSlashSlashComment(boolean allowSlashSlashComment) {
        this.allowSlashSlashComment = allowSlashSlashComment;
        return this;
    }

    public boolean isAllowSlashStarComment() {
        return allowSlashStarComment;
    }

    public JsonParserBuilder setAllowSlashStarComment(boolean allowSlashStarComment) {
        this.allowSlashStarComment = allowSlashStarComment;
        return this;
    }

    public boolean objectsKeysCanBeEncoded() {
        return objectsKeysCanBeEncoded;
    }

    public JsonParserBuilder setObjectsKeysCanBeEncoded(boolean objectsKeysCanBeEncoded) {
        this.objectsKeysCanBeEncoded = objectsKeysCanBeEncoded;
        return this;
    }

    public TokenEventListener tokenEventListener() {
        return tokenEventListener;
    }

    public JsonParserBuilder setTokenEventListener(TokenEventListener tokenEventListener) {
        this.tokenEventListener = tokenEventListener;
        return this;
    }

    public boolean strict() {
        return strict;
    }

    public JsonParserBuilder setStrict(boolean strict) {
        this.strict = strict;
        return this;
    }

    public ParsePartFunction getParseKey() {
        return parseKey;
    }

    public JsonParserBuilder setParseKey(ParsePartFunction parseKey) {
        this.parseKey = parseKey;
        return this;
    }

    public JsonIndexOverlayParser build() {

        if (isSupportNoQuoteKeys() || isAllowHashComment() || isAllowSlashSlashComment() || isAllowSlashStarComment() || getParseKey() != null) {
            final var funcTable = this.getFuncTable();
            funcTable[STRING_START_TOKEN] = JsonParserFunctions::parseString;
            funcTable[NULL_START] = JsonParserFunctions::parseNull;
            funcTable[TRUE_BOOLEAN_START] = JsonParserFunctions::parseTrue;
            funcTable[FALSE_BOOLEAN_START] = JsonParserFunctions:: parseFalse;
            for (int i = NUM_0; i < NUM_9 +1; i++){
                funcTable[i] = JsonParserFunctions::parseNumber;
            }
            funcTable[MINUS] = JsonParserFunctions::parseNumber;
            funcTable[PLUS] = JsonParserFunctions::parseNumber;

            if (isAllowHashComment()) {
                funcTable['#'] = (source, tokens) -> source.findChar('\n');
            }

            if (isSupportNoQuoteKeys() && getParseKey() == null ) {
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
                                case ETX:
                                    throw new UnexpectedCharacterException("Comment parse", "End of stream", source);
                            }
                        }
                    }
                };
            }
            return new JsonFuncParser(objectsKeysCanBeEncoded(), Arrays.copyOf(funcTable, funcTable.length),
                    this.getDefaultFunc(), this.getParseKey());
        }
        else if (strict()) {
            return new JsonStrictParser(objectsKeysCanBeEncoded());
        } else {
            return new JsonFastParser(objectsKeysCanBeEncoded());
        }
    }

    public JsonEventParser buildEventParser() {
        if (strict()) {
            return new JsonEventStrictParser(objectsKeysCanBeEncoded(), tokenEventListener());
        } else {
            return new JsonEventFastParser(objectsKeysCanBeEncoded(), tokenEventListener());
        }
    }

    public JsonParserBuilder cloneBuilder() {
        return new JsonParserBuilder().setStrict(strict()).setTokenEventListener(tokenEventListener()).setObjectsKeysCanBeEncoded(objectsKeysCanBeEncoded());
    }

}
