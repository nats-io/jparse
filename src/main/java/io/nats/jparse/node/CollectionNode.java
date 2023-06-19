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

/**
 * The CollectionNode interface represents a collection node in a tree structure.
 * <p>
 * It extends the Node interface.
 */
public interface CollectionNode extends Node {

    /**
     * Returns whether the collection node is a scalar node.
     * In a collection node, this method always returns false.
     *
     * @return false
     */
    @Override
    default boolean isScalar() {
        return false;
    }

    /**
     * Returns whether the collection node is a collection node.
     * In a collection node, this method always returns true.
     *
     * @return true
     */
    @Override
    default boolean isCollection() {
        return true;
    }

    /**
     * Returns the node associated with the specified key.
     *
     * @param key the key to retrieve the associated node
     * @return the node associated with the specified key
     */
    Node getNode(Object key);

    /**
     * Returns an optional that contains the node associated with the specified key,
     * or an empty optional if no node is associated with the key.
     *
     * @param key the key to retrieve the associated node
     * @return an optional that contains the node associated with the specified key,
     * or an empty optional if no node is associated with the key
     */
    default Optional<Node> lookupNode(Object key) {
        return Optional.ofNullable(getNode(key));
    }

    /**
     * Returns a list of lists of tokens representing the children of the collection node.
     *
     * @return a list of lists of tokens representing the children of the collection node
     */
    List<List<Token>> childrenTokens();

    /**
     * Returns the collection node as an array node.
     * This method should be implemented by the ArrayNode class.
     *
     * @return the collection node as an array node
     */
    default ArrayNode asArray() {
        return (ArrayNode) this;
    }

    /**
     * Returns the collection node as an object node.
     * This method should be implemented by the ObjectNode class.
     *
     * @return the collection node as an object node
     */
    default ObjectNode asObject() {
        return (ObjectNode) this;
    }
}
