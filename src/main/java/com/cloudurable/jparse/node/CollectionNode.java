package com.cloudurable.jparse.node;

import com.cloudurable.jparse.token.Token;

import java.util.List;
import java.util.Map;

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

    List<List<Token>> childrenTokens();

    default ArrayNode asArray() {
        return (ArrayNode ) this;
    }

    default ObjectNode asObject() {
        return (ObjectNode) this;
    }


}
