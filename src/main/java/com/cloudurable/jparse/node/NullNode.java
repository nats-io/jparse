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
package com.cloudurable.jparse.node;

import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.Token;

import java.util.Collections;
import java.util.List;

public class NullNode implements ScalarNode {


    private final Token token;
    private final CharSource source;


    public NullNode(final Token token, final CharSource source) {
        this.token = token;
        this.source = source;
    }

    @Override
    public NodeType type() {
        return NodeType.NULL;
    }

    @Override
    public List<Token> tokens() {
        return Collections.singletonList(token);
    }

    @Override
    public Token rootElementToken() {
        return token;
    }

    @Override
    public CharSource charSource() {
        return source;
    }

    @Override
    public int length() {
        return 4;
    }

    @Override
    public char charAt(int index) {
        switch (index) {
            case 0:
                return 'n';
            case 1:
                return 'u';
            case 2:
            case 3:
                return 'l';
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public Object value() {
        return null;
    }

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

}
