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

import java.math.BigDecimal;
import java.math.BigInteger;

public interface ScalarNode extends Node {

    @Override
    default boolean isScalar() {
        return true;
    }

    @Override
    default boolean isCollection() {
        return false;
    }


    Object value();

    default boolean booleanValue() {
        throw new UnsupportedOperationException();
    }

    default int intValue() {
        throw new UnsupportedOperationException();
    }

    default long longValue() {
        throw new UnsupportedOperationException();
    }

    default double doubleValue() {
        throw new UnsupportedOperationException();
    }

    default BigDecimal bigDecimalValue() {
        throw new UnsupportedOperationException();
    }

    default BigInteger bigIntegerValue() {
        throw new UnsupportedOperationException();
    }

    default CharSequence charSequenceValue() {
        return this.originalCharSequence();
    }

    default String stringValue() {
        return this.originalString();
    }

    default boolean equalsString(String str) {
        return this.equals(str);
    }
}
