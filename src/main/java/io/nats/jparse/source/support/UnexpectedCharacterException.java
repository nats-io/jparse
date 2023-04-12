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
package io.nats.jparse.source.support;

import io.nats.jparse.source.CharSource;

import java.util.Optional;

public class UnexpectedCharacterException extends RuntimeException{

    private final  CharSource source;
    private final String whileDoing;
    private final String message;
    private final int index;
    private final int ch;

    public UnexpectedCharacterException( String whileDoing, String message,  int ch, int index) {
        super(String.format("Unexpected character while %s, Error is '%s, character is %c' at index %d.", whileDoing, message, (char)ch, index ));
        this.source = null;
        this.whileDoing = whileDoing;
        this.message = message;
        this.ch = ch;
        this.index = index;

    }

    public UnexpectedCharacterException( String whileDoing, String message, CharSource source, int ch, int index) {
       super(String.format("Unexpected character while %s, Error is '%s'. \n Details \n %s", whileDoing, message, source.errorDetails(message, index, ch)));
        this.source = source;
        this.whileDoing = whileDoing;
        this.message = message;
        this.ch = ch;
        this.index = index;
    }

    public UnexpectedCharacterException(String whileDoing, String message,  CharSource source, int ch) {
        this(whileDoing, message, source, ch, source.getIndex());
    }

    public UnexpectedCharacterException(String whileDoing, String message, CharSource source) {
        this(whileDoing, message, source, source.getCurrentCharSafe(), source.getIndex());
    }

    public UnexpectedCharacterException(String whileDoing, CharSource charSource, UnexpectedCharacterException unexpectedCharacterException) {
        this(whileDoing, unexpectedCharacterException.getLocalizedMessage(), charSource);
    }

    public Optional<CharSource> source() {
        return Optional.ofNullable(source);
    }

    public String getWhileDoing() {
        return whileDoing;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getIndex() {
        return index;
    }

    public int getCh() {
        return ch;
    }

    public String getDetails() {
        return source.errorDetails(message, index, ch);
    }
}
