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
package com.cloudurable.jparse.path;

import com.cloudurable.jparse.node.NodeType;
import com.cloudurable.jparse.node.ScalarNode;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.Token;

import java.util.Collections;
import java.util.List;

public class KeyPathNode implements ScalarNode, PathElement {

    private final Token rootElementToken;
    private final CharSource charSource;

    public KeyPathNode(final Token token, final CharSource charSource) {
        this.rootElementToken = token;
        this.charSource = charSource;
    }

    @Override
    public NodeType type() {
        return NodeType.PATH_KEY;
    }

    @Override
    public List<Token> tokens() {
        return Collections.singletonList(rootElementToken);
    }

    @Override
    public Token rootElementToken() {
        return rootElementToken;
    }

    @Override
    public CharSource charSource() {
        return charSource;
    }


    @Override
    public Object value() {
        return toString();
    }

    @Override
    public boolean isIndex() {
        return false;
    }

    @Override
    public boolean isKey() {
        return true;
    }

    @Override
    public String toString() {
        return this.originalString();
    }

    public CharSequence toCharSequence() {
        return this.originalCharSequence();
    }
}
