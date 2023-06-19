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
package io.nats.jparse.path;

import io.nats.jparse.node.ScalarNode;

/**
 * An interface for representing elements of a JSON Path expression.
 *
 * <p>Implementing classes must provide methods for determining whether an element is an index or a key, as well as
 * methods for converting an element to its index or key representation.</p>
 */
public interface PathElement extends ScalarNode {

    /**
     * Determines whether this element is an index.
     *
     * @return `true` if this element is an index, `false` otherwise
     */
    boolean isIndex();

    /**
     * Determines whether this element is a key.
     *
     * @return `true` if this element is a key, `false` otherwise
     */
    boolean isKey();

    /**
     * Returns this element as an index.
     *
     * @return this element as an `IndexPathNode`
     */
    default IndexPathNode asIndex() {
        return (IndexPathNode) this;
    }

    /**
     * Returns this element as a key.
     *
     * @return this element as a `KeyPathNode`
     */
    default KeyPathNode asKey() {
        return (KeyPathNode) this;
    }
}
