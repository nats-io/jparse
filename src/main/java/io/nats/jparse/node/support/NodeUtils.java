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
package io.nats.jparse.node.support;

import io.nats.jparse.node.*;
import io.nats.jparse.path.IndexPathNode;
import io.nats.jparse.path.KeyPathNode;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.token.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static io.nats.jparse.token.TokenTypes.ARRAY_ITEM_TOKEN;

public class NodeUtils {


    public static List<List<Token>> getChildrenTokens(final TokenSubList tokens) {

        final Token root = tokens.get(0);
        final List<List<Token>> childrenTokens = new ArrayList<>(16);

        AtomicInteger index = new AtomicInteger(1);
        IntStream.range(1, tokens.size())
                .mapToObj(i -> tokens.get(i))
                .filter(token -> token.startIndex <= root.endIndex)
                .forEach(token -> {
                    if (token.type <= ARRAY_ITEM_TOKEN) {
                        int childCount = tokens.countChildren(index.get(), token);
                        int endIndex = index.get() + childCount;
                        childrenTokens.add(tokens.subList(index.get(), endIndex));
                        index.set(endIndex);
                    } else {
                        childrenTokens.add(Collections.singletonList(token));
                        index.incrementAndGet();
                    }
                });

        return childrenTokens;
    }

    public static Node createNode(final List<Token> tokens, final CharSource source, boolean objectsKeysCanBeEncoded) {

        final NodeType nodeType = NodeType.tokenTypeToElement(tokens.get(0).type);

        switch (nodeType) {
            case ARRAY:
                return new ArrayNode((TokenSubList) tokens, source, objectsKeysCanBeEncoded);
            case INT:
                return new NumberNode(tokens.get(0), source, NodeType.INT);
            case FLOAT:
                return new NumberNode(tokens.get(0), source, NodeType.FLOAT);
            case OBJECT:
                return new ObjectNode((TokenSubList) tokens, source, objectsKeysCanBeEncoded);
            case STRING:
                return new StringNode(tokens.get(0), source);
            case BOOLEAN:
                return new BooleanNode(tokens.get(0), source);
            case NULL:
                return new NullNode(tokens.get(0), source);
            case PATH_INDEX:
                return new IndexPathNode(tokens.get(0), source);
            case PATH_KEY:
                return new KeyPathNode(tokens.get(0), source);
            default:
                throw new IllegalStateException();
        }
    }


    public static Node createNodeForObject(final List<Token> theTokens, final CharSource source, boolean objectsKeysCanBeEncoded) {

        final Token rootToken = theTokens.get(1);
        final List<Token> tokens = theTokens.subList(1, theTokens.size());
        final NodeType nodeType = NodeType.tokenTypeToElement(rootToken.type);

        switch (nodeType) {
            case ARRAY:
                return new ArrayNode((TokenSubList) tokens, source, objectsKeysCanBeEncoded);
            case INT:
                return new NumberNode(tokens.get(0), source, NodeType.INT);
            case FLOAT:
                return new NumberNode(tokens.get(0), source, NodeType.FLOAT);
            case OBJECT:
                return new ObjectNode((TokenSubList) tokens, source, objectsKeysCanBeEncoded);
            case STRING:
                return new StringNode(tokens.get(0), source);
            case BOOLEAN:
                return new BooleanNode(tokens.get(0), source);
            case NULL:
                return new NullNode(tokens.get(0), source);
            default:
                throw new IllegalStateException();
        }
    }


}
