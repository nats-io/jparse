package com.cloudurable.jparse.node;

import com.cloudurable.jparse.token.Token;

import java.util.List;
import java.util.Map;
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
