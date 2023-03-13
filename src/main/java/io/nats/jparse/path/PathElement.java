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

public interface PathElement extends ScalarNode {

    boolean isIndex();

    boolean isKey();

    default IndexPathNode asIndex() {
        return (IndexPathNode) this;
    }

    default KeyPathNode asKey() {
        return (KeyPathNode) this;
    }
}
