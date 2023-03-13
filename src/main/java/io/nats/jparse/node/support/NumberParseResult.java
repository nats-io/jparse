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

import java.util.Objects;

public final class NumberParseResult {
    private final int endIndex;
    private final boolean wasFloat;

    public NumberParseResult(int endIndex, boolean wasFloat) {
        this.endIndex = endIndex;
        this.wasFloat = wasFloat;
    }

    public int endIndex() {
        return endIndex;
    }

    public boolean wasFloat() {
        return wasFloat;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (NumberParseResult) obj;
        return this.endIndex == that.endIndex &&
                this.wasFloat == that.wasFloat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(endIndex, wasFloat);
    }

    @Override
    public String toString() {
        return "NumberParseResult[" +
                "endIndex=" + endIndex + ", " +
                "wasFloat=" + wasFloat + ']';
    }

}
