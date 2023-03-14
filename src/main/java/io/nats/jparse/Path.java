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
package io.nats.jparse;

import io.nats.jparse.node.ArrayNode;
import io.nats.jparse.node.Node;
import io.nats.jparse.node.ObjectNode;
import io.nats.jparse.path.PathElement;
import io.nats.jparse.path.PathNode;
import io.nats.jparse.path.PathParser;
import io.nats.jparse.source.support.PathException;

import java.util.Iterator;

public class Path {

    public static Node atPath(final String path, final String json) {
        return atPath(path, Json.toRootNode(json));
    }

    public static Node atPath(final String path, final Node rootNode) {
        return atPath(toPath(path), rootNode);
    }

    public static Node atPath(final PathNode path, final Node rootNode) {
        Iterator<PathElement> iterator = path.iterator();
        Node node = rootNode;
        PathElement pathElement = null;
        try {
            while (iterator.hasNext()) {

                pathElement = iterator.next();

                switch (node.type()) {
                    case OBJECT:
                        final ObjectNode objectNode = (ObjectNode) node;
                        final var key = pathElement.asKey().toCharSequence();
                        node = objectNode.getNode(key);
                        break;
                    case ARRAY:
                        final ArrayNode arrayNode = (ArrayNode) node;
                        node = arrayNode.getNodeAt(pathElement.asIndex().intValue());
                        break;
                    default:
                        if (node.isCollection()) {
                            node = node.asCollection().getNode(pathElement.asKey().toCharSequence());
                        } else {
                            throw new PathException("Looking up Path", "Path not found at " + path + " path element key " + pathElement.asKey().toString(),
                                    node.charSource(), node.rootElementToken().startIndex);

                        }
                }

            }
        } catch (Exception ex) {
            throw new IllegalStateException("Path not found at " + path + " path element index " + pathElement.value());
        }
        return node;

    }

    public static PathNode toPath(String path) {
        final var pathParser = new PathParser();
        return pathParser.parse(path).getPathNode();
    }
}
