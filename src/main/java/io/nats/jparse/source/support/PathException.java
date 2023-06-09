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
package io.nats.jparse.source.support;

import io.nats.jparse.source.CharSource;

/**
 * Exception class for handling errors related to JSON paths.
 * This is a runtime exception that can be thrown when an unexpected condition
 * is met while processing a path.
 */
public class PathException extends RuntimeException {

    /**
     * Constructs a new PathException with a detailed error message.
     * The error message is formatted to include specific details about the operation,
     * the error, and the location in the source where the error occurred.
     *
     * @param whileDoing The operation being performed when the error occurred.
     * @param message The specific error message.
     * @param source The CharSource where the error occurred.
     * @param index The index in the source where the error occurred.
     */
    public PathException(String whileDoing, String message, CharSource source, int index) {
        super(String.format("Unexpected character while %s, Error is '%s'. \n Details \n %s",
                whileDoing, message, source.errorDetails(message, index, source.getChartAt(index))));
    }
}
