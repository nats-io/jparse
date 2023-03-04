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
package com.cloudurable.jparse.node;

import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.Token;

import java.util.Collections;
import java.util.List;

public class BooleanNode implements ScalarNode {

    private final Token token;
    private final CharSource source;
    private final boolean value;

    public BooleanNode(final Token token, final CharSource source) {
        this.token = token;
        this.source = source;
        this.value = source.getChartAt(token.startIndex) == 't';
    }

    @Override
    public NodeType type() {
        return NodeType.BOOLEAN;
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


    public boolean booleanValue() {
        return value;
    }


    @Override
    public int length() {
        return value ? 4 : 5;
    }

    @Override
    public Object value() {
        return booleanValue();
    }

    @Override
    public char charAt(int index) {
        if (value) {
            return switch (index) {
                case 0 -> 't';
                case 1 -> 'r';
                case 2 -> 'u';
                case 3 -> 'e';
                default -> throw new IllegalStateException();
            };
        } else {
            return switch (index) {
                case 0 -> 'f';
                case 1 -> 'a';
                case 2 -> 'l';
                case 3 -> 's';
                case 4 -> 'e';
                default -> throw new IllegalStateException();
            };
        }
    }

    @Override
    public String toString() {
        return value ? "true" : "false";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BooleanNode that = (BooleanNode) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
