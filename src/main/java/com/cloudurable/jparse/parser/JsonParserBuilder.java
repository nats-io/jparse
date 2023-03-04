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

import com.cloudurable.jparse.token.TokenEventListener;

public class JsonParserBuilder {

    private TokenEventListener tokenEventListener;
    private boolean strict = false;
    private boolean objectsKeysCanBeEncoded;

    public static JsonParserBuilder builder() {
        return new JsonParserBuilder();
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

    public JsonIndexOverlayParser build() {
        if (strict()) {
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
