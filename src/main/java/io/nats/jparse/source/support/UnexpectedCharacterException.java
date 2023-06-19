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


/**
 * Exception thrown when an unexpected character is encountered while parsing a JSON string or CharSource.
 *
 * @see CharSource
 * @see io.nats.jparse.parser.JsonParser
 */
public class UnexpectedCharacterException extends RuntimeException {

    private final CharSource source;
    private final String whileDoing;
    private final String message;
    private final int index;
    private final int ch;

    /**
     * Constructs a new {@code UnexpectedCharacterException} with the given details.
     *
     * @param whileDoing a string describing the context in which the exception occurred
     * @param message    a message describing the error that occurred
     * @param ch         the unexpected character that caused the exception
     * @param index      the index at which the unexpected character occurred
     */
    public UnexpectedCharacterException(String whileDoing, String message, int ch, int index) {
        super(String.format("Unexpected character while %s, Error is '%s, character is %c' at index %d.", whileDoing, message, (char) ch, index));
        this.source = null;
        this.whileDoing = whileDoing;
        this.message = message;
        this.ch = ch;
        this.index = index;

    }

    /**
     * Constructs a new {@code UnexpectedCharacterException} with the given details.
     *
     * @param whileDoing a string describing the context in which the exception occurred
     * @param message    a message describing the error that occurred
     * @param source     the character source being parsed when the exception occurred
     * @param ch         the unexpected character that caused the exception
     * @param index      the index at which the unexpected character occurred
     */
    public UnexpectedCharacterException(String whileDoing, String message, CharSource source, int ch, int index) {
        super(String.format("Unexpected character while %s, Error is '%s'. \n Details \n %s", whileDoing, message, source.errorDetails(message, index, ch)));
        this.source = source;
        this.whileDoing = whileDoing;
        this.message = message;
        this.ch = ch;
        this.index = index;
    }

    /**
     * Constructs a new {@code UnexpectedCharacterException} with the given details.
     *
     * @param whileDoing a string describing the context in which the exception occurred
     * @param message    a message describing the error that occurred
     * @param source     the character source being parsed when the exception occurred
     * @param ch         the unexpected character that caused the exception
     */
    public UnexpectedCharacterException(String whileDoing, String message, CharSource source, int ch) {
        this(whileDoing, message, source, ch, source.getIndex());
    }

    /**
     * Constructs a new {@code UnexpectedCharacterException} with the given details.
     *
     * @param whileDoing a string describing the context in which the exception occurred
     * @param message    a message describing the error that occurred
     * @param source     the character source being parsed when the exception occurred
     */
    public UnexpectedCharacterException(String whileDoing, String message, CharSource source) {
        this(whileDoing, message, source, source.getCurrentCharSafe(), source.getIndex());
    }

    /**
     * Constructs a new {@code UnexpectedCharacterException} with the given details.
     *
     * @param whileDoing                   a string describing the context in which the exception occurred
     * @param unexpectedCharacterException a exception describing the error that occurred
     * @param charSource                   the character source being parsed when the exception occurred
     */
    public UnexpectedCharacterException(String whileDoing, CharSource charSource, UnexpectedCharacterException unexpectedCharacterException) {
        this(whileDoing, unexpectedCharacterException.getLocalizedMessage(), charSource);
    }

    /**
     * The source of the error.
     *
     * @return source
     */
    public Optional<CharSource> source() {
        return Optional.ofNullable(source);
    }

    /**
     * What we were doing when the error occurred.
     *
     * @return what we were doing when the error occurred.
     */
    public String getWhileDoing() {
        return whileDoing;
    }

    /**
     * The message for the error.
     *
     * @return message for the error.
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * The index in the source where the error occurred.
     *
     * @return index in the source where the error occurred.
     */
    public int getIndex() {
        return index;
    }

    /**
     * The character that caused the error.
     *
     * @return character that caused the error.
     */
    public int getCh() {
        return ch;
    }

    /**
     * Details about the error.
     *
     * @return details about the error.
     */
    public String getDetails() {
        return source.errorDetails(message, index, ch);
    }
}
