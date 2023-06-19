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
package io.nats.jparse.node;

import io.nats.jparse.Path;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.token.Token;

import java.util.List;

public interface Node extends CharSequence {

    NodeType type();

    List<Token> tokens();

    Token rootElementToken();

    CharSource charSource();

    boolean isScalar();

    boolean isCollection();

    default ScalarNode asScalar() {
        return (ScalarNode) this;
    }

    default CollectionNode asCollection() {
        return (CollectionNode) this;
    }

    @Override
    default int length() {
        final Token token = rootElementToken();
        return token.endIndex - token.startIndex;
    }

    @Override
    default char charAt(int index) {
        return charSource().getChartAt(rootElementToken().startIndex + index);
    }


    @Override
    default CharSequence subSequence(int start, int end) {
        Token token = rootElementToken();
        return charSource().getCharSequence(start + token.startIndex, end + token.startIndex);
    }

    default String originalString() {
        return charSource().getString(rootElementToken().startIndex, rootElementToken().endIndex);
    }

    default String toJsonString() {
        return originalString();
    }

    default CharSequence originalCharSequence() {
        return charSource().getCharSequence(rootElementToken().startIndex, rootElementToken().endIndex);
    }

    default CharSequence toJsonCharSequence() {
        return originalCharSequence();
    }

    default boolean equalsContent(String content) {
        return equals(content);
    }

    default Node atPath(String path) {
        return Path.atPath(path, this);
    }
}
