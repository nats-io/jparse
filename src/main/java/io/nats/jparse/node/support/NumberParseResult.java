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

/**
 * Represents the result of a number parsing operation.
 * <p>
 * The NumberParseResult class represents the result of a number parsing operation.
 * It provides methods to access the end index of the parsed number and to check if the parsed number was a float.
 * The class also overrides the equals, hashCode, and toString methods for proper object comparison and string
 * representation.
 */
public final class NumberParseResult {
    private final int endIndex;
    private final boolean wasFloat;

    /**
     * Constructs a new NumberParseResult.
     *
     * @param endIndex the end index of the parsed number
     * @param wasFloat indicates whether the parsed number was a float
     */
    public NumberParseResult(int endIndex, boolean wasFloat) {
        this.endIndex = endIndex;
        this.wasFloat = wasFloat;
    }

    /**
     * Returns the end index of the parsed number.
     *
     * @return the end index of the parsed number
     */
    public int endIndex() {
        return endIndex;
    }

    /**
     * Indicates whether the parsed number was a float.
     *
     * @return true if the parsed number was a float, false otherwise
     */
    public boolean wasFloat() {
        return wasFloat;
    }

    /**
     * Checks if this NumberParseResult is equal to another object.
     *
     * @param obj the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        final NumberParseResult that = (NumberParseResult) obj;
        return this.endIndex == that.endIndex &&
                this.wasFloat == that.wasFloat;
    }

    /**
     * Generates a hash code for this NumberParseResult.
     *
     * @return the hash code value for this NumberParseResult
     */
    @Override
    public int hashCode() {
        return Objects.hash(endIndex, wasFloat);
    }

    /**
     * Returns a string representation of this NumberParseResult.
     *
     * @return a string representation of this NumberParseResult
     */
    @Override
    public String toString() {
        return "NumberParseResult[" +
                "endIndex=" + endIndex + ", " +
                "wasFloat=" + wasFloat + ']';
    }
}
