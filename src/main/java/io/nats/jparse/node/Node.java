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
/**
 * This interface represents a Node in the JParse library.
 * <p>
 * A Node is a component of a parsed structure, such as a JSON document.
 * <p>
 * It provides methods to access the characteristics and content of the Node.
 *
 * <p>Each Node has a type, a list of tokens, and a root element token.
 * It also provides information about whether it is a scalar or a collection.
 * <p>
 * Additionally, it offers methods to retrieve the content of the Node in various formats,
 * <p>
 * such as a String, CharSequence, or JSON representation.
 *
 * <p>Implementations of this interface should provide the necessary functionality
 * to handle different types of Nodes, such as ScalarNode and CollectionNode.
 * <p>
 * Custom operations and behaviors can be defined by extending this interface.
 *
 * <p>This interface extends the CharSequence interface, allowing users to treat
 * the Node as a sequence of characters. It also provides methods to access
 * <p>
 * individual characters and substrings within the Node's content.
 *
 * <p>Nodes can be located within a structure using a path. The {@code atPath} method
 * allows retrieving a specific Node at a given path.
 *
 * @see io.nats.jparse.node.ScalarNode
 * @see io.nats.jparse.node.CollectionNode
 * @see io.nats.jparse.Path
 * @see io.nats.jparse.source.CharSource
 * @see io.nats.jparse.token.Token
 */
public interface Node extends CharSequence {

    /**

     Returns the NodeType of the Node.
     @return the NodeType of the Node
     */
    NodeType type();

    /**

     Returns the list of tokens associated with the Node.
     @return the list of tokens associated with the Node
     */
    List<Token> tokens();

    /**

     Returns the root element token of the Node.
     @return the root element token of the Node
     */
    Token rootElementToken();

    /**

     Returns the CharSource associated with the Node.
     @return the CharSource associated with the Node
     */
    CharSource charSource();

    /**

     Checks if the Node is a scalar.
     @return {@code true} if the Node is a scalar, {@code false} otherwise
     */
    boolean isScalar();

    /**

     Checks if the Node is a collection.
     @return {@code true} if the Node is a collection, {@code false} otherwise
     */
    boolean isCollection();

    /**

     Returns this Node as a ScalarNode.
     @return this Node as a ScalarNode
     */
    default ScalarNode asScalar() {
        return (ScalarNode) this;
    }

    /**

     Returns this Node as a CollectionNode.
     @return this Node as a CollectionNode
     */
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

    /**
     * Returns the original content of the Node as a String.
     *
     * @return the original content of the Node as a String
     */
    default String originalString() {
        return charSource().getString(rootElementToken().startIndex, rootElementToken().endIndex);
    }

    /**

     Returns the JSON representation of the Node as a String.
     @return the JSON representation of the Node as a String
     */
    default String toJsonString() {
        return originalString();
    }

    /**

     Returns the original content of the Node as a CharSequence.
     @return the original content of the Node as a CharSequence
     */
    default CharSequence originalCharSequence() {
        return charSource().getCharSequence(rootElementToken().startIndex, rootElementToken().endIndex);
    }

    /**

     Returns the JSON representation of the Node as a CharSequence.
     @return the JSON representation of the Node as a CharSequence
     */
    default CharSequence toJsonCharSequence() {
        return originalCharSequence();
    }

    /**

     Compares the content of the Node with the given String.
     @param content the String to compare with
     @return {@code true} if the content of the Node is equal to the given String, {@code false} otherwise
     */
    default boolean equalsContent(String content) {
        return equals(content);
    }

    /**

     Returns the Node located at the specified path within this Node.
     @param path the path to the desired Node
     @return the Node located at the specified path, or {@code null} if not found
     */
    default Node atPath(String path) {
        return Path.atPath(path, this);
    }
}
