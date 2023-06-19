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

import io.nats.jparse.node.support.CharSequenceUtils;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.token.Token;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NumberNode extends Number implements ScalarNode, CharSequence {

    private final Token token;
    private final CharSource source;
    private final NodeType elementType;
    private boolean hashCodeSet;
    private int hashCode;

    public NumberNode(Token token, CharSource source, NodeType elementType) {
        this.token = token;
        this.source = source;
        this.elementType = elementType;
    }

    @Override
    public int intValue() {
        return source.getInt(token.startIndex, token.endIndex);
    }

    @Override
    public long longValue() {
        return source.getLong(token.startIndex, token.endIndex);
    }

    @Override
    public float floatValue() {
        return source.getFloat(token.startIndex, token.endIndex);
    }

    @Override
    public double doubleValue() {
        return source.getDouble(token.startIndex, token.endIndex);
    }


    @Override
    public BigDecimal bigDecimalValue() {
        return source.getBigDecimal(token.startIndex, token.endIndex);
    }

    @Override
    public BigInteger bigIntegerValue() {
        return source.getBigInteger(token.startIndex, token.endIndex);
    }


    @Override
    public Object value() {
        if (isInteger()) {
            return intValue();
        } else if (isLong()) {
            return longValue();
        } else {
            return this.doubleValue();
        }
    }

    @Override
    public NodeType type() {
        return this.elementType;
    }

    @Override
    public int length() {
        return token.endIndex - token.startIndex;
    }

    @Override
    public char charAt(int index) {
        if (index > length()) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return source.getChartAt(token.startIndex + index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        if (end > length()) {
            throw new IndexOutOfBoundsException();
        }
        return source.getCharSequence(start + token.startIndex, end + token.startIndex);
    }

    @Override
    public List<Token> tokens() {
        return Collections.singletonList(this.token);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if (o instanceof NumberNode) {
            NumberNode other = (NumberNode) o;
            return CharSequenceUtils.equals(this, other);
        } else if (o instanceof Number) {

            switch (o.getClass().getName()) {
                case "java.lang.Integer":
                    return this.intValue() == (int) o;
                case "java.lang.Long":
                    return this.longValue() == (long) o;
                case "java.lang.Float":
                    return this.floatValue() == (float) o;
                case "java.lang.Double":
                    return this.doubleValue() == (double) o;
                case "java.math.BigDecimal":
                    return this.bigDecimalValue().equals(o);
                case "java.math.BigInteger":
                    return this.bigIntegerValue().equals(o);
                default:
                    return false;
            }

        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        if (hashCodeSet) {
            return hashCode;
        }
        hashCode = CharSequenceUtils.hashCode(this);
        hashCodeSet = true;
        return hashCode;
    }

    public boolean isInteger() {
        switch (elementType) {
            case INT:
                return source.isInteger(this.token.startIndex, this.token.endIndex);
            default:
                return false;
        }
    }

    public boolean isLong() {
        switch (elementType) {
            case INT:
                return !source.isInteger(this.token.startIndex, this.token.endIndex);
            default:
                return false;
        }
    }


    @Override
    public String toString() {
        return this.originalString();
    }


}
