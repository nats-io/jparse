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

import io.nats.jparse.node.RootNode;
import io.nats.jparse.node.support.ParseConstants;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.source.Sources;
import io.nats.jparse.token.Token;

import java.util.List;

/**
 * The `JsonParser` interface provides methods for scanning and parsing JSON strings. It defines methods for
 * scanning a character source and returning a list of tokens, as well as for parsing a character source
 * and returning a root node representing the parsed JSON. The interface also includes default methods
 * for parsing and scanning strings,
 * and it extends the `ParseConstants` interface, which defines constants used for parsing JSON strings.
 */
public interface JsonParser extends ParseConstants {

    /**
     * Scan a character source and return a list of tokens representing the JSON string.
     *
     * @param source The character source to scan
     * @return A list of tokens representing the JSON string
     */
    List<Token> scan(final CharSource source);

    /**
     * Parse a character source and return a root node representing the parsed JSON.
     *
     * @param source The character source to parse
     * @return A root node representing the parsed JSON
     */
    RootNode parse(final CharSource source);

    /**
     * Parse a string and return a root node representing the parsed JSON.
     *
     * @param source The string to parse
     * @return A root node representing the parsed JSON
     */
    default RootNode parse(final String source) {
        return parse(Sources.stringSource(source));
    }

    /**
     * Scan a string and return a list of tokens representing the JSON string.
     *
     * @param source The string to scan
     * @return A list of tokens representing the JSON string
     */
    default List<Token> scan(final String source) {
        return scan(Sources.stringSource(source));
    }
}
