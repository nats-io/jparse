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
package io.nats.jparse.token;

import io.nats.jparse.source.CharSource;

import java.util.Objects;

public class Token {

    public final int startIndex;
    public final int endIndex;
    public final int type;

    public Token(int startIndex, int endIndex, int type) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.type = type;
    }

    public String asString(String buffer) {
        return buffer.substring(startIndex, endIndex);
    }

    public String asString(CharSource source) {
        return source.getString(startIndex, endIndex);
    }

    public int length() {
        return endIndex - startIndex;
    }

    @Override
    public String toString() {
        return "Token{" +
                "startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                ", type=" + TokenTypes.getTypeName(type) + " " + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return startIndex == token.startIndex && endIndex == token.endIndex && type == token.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startIndex, endIndex, type);
    }
}
