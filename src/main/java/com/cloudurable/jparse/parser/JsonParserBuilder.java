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
