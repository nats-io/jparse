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
package io.nats.jparse.token;

import io.nats.jparse.source.CharSource;

/**
 * The TokenEventListener interface defines methods for handling events related to JSON tokens during parsing.
 * It includes methods for handling start and end events, which are called when a token is started or ended
 * during parsing. The interface includes parameters for the ID and index of the token, as well as the character
 * source being parsed.
 *
 * @see TokenTypes
 */
public interface TokenEventListener {
    /**
     * Called when a token is started during parsing.
     * The tokenId refers to its token type TokenTypes.
     * @see TokenTypes
     *
     * @param tokenId The ID of the token being started (TokenTypes)
     * @param index The index of the token within the source
     * @param source The character source being parsed
     */
    void start(int tokenId, int index, CharSource source);

    /**
     * Called when a token is ended during parsing.
     * The tokenId refers to its token type TokenTypes.
     * @see TokenTypes
     *
     * @param tokenId The ID of the token being ended (TokenTypes)
     * @param index The index of the token within the source
     * @param source The character source being parsed
     */
    void end(int tokenId, int index, CharSource source);
}
