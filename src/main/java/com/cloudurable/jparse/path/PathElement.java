package com.cloudurable.jparse.path;

import com.cloudurable.jparse.node.ScalarNode;

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
