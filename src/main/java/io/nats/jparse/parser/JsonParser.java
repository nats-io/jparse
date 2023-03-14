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

public interface JsonParser extends ParseConstants {
    List<Token> scan(final CharSource source);
    RootNode parse(final CharSource source);
    default RootNode parse(final String source) {
        return parse(Sources.stringSource(source));
    }
    default List<Token> scan(final String source) {
        return scan(Sources.stringSource(source));
    }
}
