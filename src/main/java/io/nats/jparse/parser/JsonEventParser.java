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
import io.nats.jparse.source.CharSource;
import io.nats.jparse.source.Sources;
import io.nats.jparse.token.TokenEventListener;

/**
 * An interface for parsing JSON events.
 * <p>
 * Implementing classes must provide methods for parsing a JSON source with events, either from a string or from a character
 * source. They must also provide a `TokenEventListener` to handle events generated during parsing.
 */
public interface JsonEventParser extends ParseConstants {

    /**
     * Returns the `TokenEventListener` used by this parser.
     *
     * @return the `TokenEventListener` used by this parser
     */
    TokenEventListener tokenEvents();

    /**
     * Parses the given character source with events.
     *
     * @param source      the `CharSource` to parse
     * @param tokenEvents the `TokenEventListener` to handle events generated during parsing
     */
    void parseWithEvents(final CharSource source, TokenEventListener tokenEvents);

    /**
     * Parses the given string with events.
     *
     * @param source      the string to parse
     * @param tokenEvents the `TokenEventListener` to handle events generated during parsing
     */
    default void parseWithEvents(final String source, TokenEventListener tokenEvents) {
        parseWithEvents(Sources.stringSource(source), tokenEvents);
    }

    /**
     * Parses the given character source with events, using the `TokenEventListener` associated with this parser.
     *
     * @param source the `CharSource` to parse
     */
    default void parseWithEvents(final CharSource source) {
        parseWithEvents(source, tokenEvents());
    }

    /**
     * Parses the given string with events, using the `TokenEventListener` associated with this parser.
     *
     * @param source the string to parse
     */
    default void parseWithEvents(final String source) {
        parseWithEvents(Sources.stringSource(source), tokenEvents());
    }
}
