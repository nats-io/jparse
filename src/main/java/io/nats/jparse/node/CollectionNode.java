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

import io.nats.jparse.token.Token;

import java.util.List;
import java.util.Optional;

public interface CollectionNode extends Node{

    @Override
    default boolean isScalar() {
        return false;
    }

    @Override
    default boolean isCollection() {
        return true;
    }

    Node getNode(Object key);

    default Optional<Node> lookupNode(Object key) {
        return Optional.ofNullable(getNode(key));
    }

    List<List<Token>> childrenTokens();

    default ArrayNode asArray() {
        return (ArrayNode ) this;
    }

    default ObjectNode asObject() {
        return (ObjectNode) this;
    }


}
