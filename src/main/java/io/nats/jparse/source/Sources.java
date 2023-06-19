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
package io.nats.jparse.source;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The `Sources` class provides utility methods for creating `CharSource` objects from various input sources, such as strings,
 * byte arrays, files, input streams, and readers.
 * <p>
 * The `Sources` class provides a set of utility methods that allow you to create `CharSource` objects from various
 * input sources. These sources include strings, byte arrays, files, input streams, and readers.
 * </p>
 * <p>
 * The `charSeqSource` method creates a `CharSource` object from a `CharSequence`. If the `CharSequence` is an
 * instance of `String`, it creates a `CharArrayCharSource` object with the `String` as input. Otherwise, it creates
 * a `CharArrayCharSource` object with the `CharSequence` converted to a `String`.
 * </p>
 * <p>
 * The `stringSource` method creates a `CharSource` object from a `String`. It does this by calling the `charSource`
 * method with the `String` converted to a `char` array.
 * </p>
 * <p>
 * The `byteSource` method creates a `CharSource` object from a byte array using a specified `Charset`. If a `Charset`
 * is not specified, it defaults to the UTF-8 `Charset`.
 * </p>
 * <p>
 * The `charSource` method creates a `CharSource` object from a `char` array. The `charSource` method has three
 * overloaded versions. One version creates a `CharSource` object from a `char` array with no offset. Another version creates a `CharSource` object from a `char` array with a specified offset. The third version creates a `CharSource` object from a `char` array with a specified offset and end index.
 * </p>
 * <p>
 * The `charBufferSource` method creates a `CharSource` object from a `CharBuffer` by calling the `charSeqSource`
 * method with the `CharBuffer` as input.
 * </p>
 * <p>
 * The `fileSource` method creates a `CharSource` object from a file. It has two overloaded versions, one of
 * which allows you to specify a `Charset`. If a `Charset` is not specified, it defaults to the UTF-8 `Charset`.
 * If there is an error reading the file, an `IllegalStateException` is thrown.
 * </p>
 * <p>
 * The `inputStreamSource` method creates a `CharSource` object from an input stream. It has two overloaded versions,
 * one of which allows you to specify a `Charset`. If a `Charset` is not specified, it defaults to the UTF-8 `Charset`.
 * </p>
 * <p>
 * The `readerSource` method creates a `CharSource` object from a `Reader`. It does this by reading the `Reader`
 * line by line and appending each line to a `StringBuilder`. The resulting `StringBuilder` is then used to create
 * a `CharArrayCharSource` object. If there is an error reading the `Reader`, an `IllegalStateException` is thrown.
 * </p>
 */
public class Sources {

        /**
         * Creates a `CharSource` object from the specified `CharSequence`.
         *
         * @param source The input `CharSequence`
         * @return The resulting `CharSource` object
         */
        public static CharSource charSeqSource(final CharSequence source) {
            if (source instanceof String) {
                return new CharArrayCharSource((String) source);
            } else {
                return new CharArrayCharSource(source.toString());
            }
        }

        /**
         * Creates a `CharSource` object from the specified `String`.
         *
         * @param source The input `String`
         * @return The resulting `CharSource` object
         */
        public static CharSource stringSource(final String source) {
            return charSource(source.toCharArray());
        }

        /**
         * Creates a `CharSource` object from the specified byte array using the specified `Charset`.
         *
         * @param source The input byte array
         * @param charset The `Charset` to use
         * @return The resulting `CharSource` object
         */
        public static CharSource byteSource(final byte[] source, final Charset charset) {
            return new CharArrayCharSource(new String(source, charset));
        }

        /**
         * Creates a `CharSource` object from the specified byte array using the UTF-8 `Charset`.
         *
         * @param source The input byte array
         * @return The resulting `CharSource` object
         */
        public static CharSource byteSource(final byte[] source) {
            return byteSource(source, StandardCharsets.UTF_8);
        }

        /**
         * Creates a `CharSource` object from the specified `char` array.
         *
         * @param source The input `char` array
         * @return The resulting `CharSource` object
         */
        public static CharSource charSource(final char[] source) {
            return new CharArrayCharSource(source);
        }

        /**
         * Creates a `CharSource` object from the specified `char` array with the specified offset.
         *
         * @param offset The offset to start reading from
         * @param source The input `char` array
         * @return The resulting `CharSource` object
         */
        public static CharSource charSource(final int offset, final char[] source) {
            return new CharArrayOffsetCharSource(offset, source.length, source);
        }

        /**
         * Creates a `CharSource` object from the specified `char` array with the specified offset and end index.
         *
         * @param offset The offset to start reading from
         * @param endIndex The end index to stop reading at
         * @param source The input `char` array
         * @return The resulting `CharSource` object
         */
        public static CharSource charSource(final int offset, final int endIndex, final char[] source) {
            return new CharArrayOffsetCharSource(offset, endIndex, source);
        }

        /**
         * Creates a `CharSource` object from the specified `CharBuffer`.
         *
         * @param source The input `CharBuffer`
         * @return The resulting `CharSource` object
         */
        public static CharSource charBufferSource(final CharBuffer source) {
            return charSeqSource(source);
        }

        /**
         * Creates a `CharSource` object from the specified file name.
         *
         * @param fileNameSource The file name to read from
         * @return The resulting `CharSource` object
         */
        public static CharSource fileSource(final String fileNameSource) {
            return fileSource(fileNameSource, StandardCharsets.UTF_8);
        }

        /**
         * Creates a `CharSource` object from the specified file name using the specified `Charset`.
         *
         * @param fileNameSource The file name to read from
         * @param charset The `Charset` to use
         * @return The resulting `CharSource` object
         * @throws IllegalStateException if there is an error reading the file
         */
        public static CharSource fileSource(final String fileNameSource, final Charset charset) {
            try {
                return byteSource(Files.readAllBytes(Paths.get(fileNameSource)), charset);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        /**
         * Creates a `CharSource` object from the specified file.
         *
         * @param fileSource The file to read from
         * @return The resulting `CharSource` object
         */
        public static CharSource fileSource(final File fileSource) {
            return fileSource(fileSource, StandardCharsets.UTF_8);
        }

        /**
         * Creates a `CharSource` object from the specified file using the specified `Charset`.
         *
         * @param fileSource The file to read from
         * @param charset The `Charset` to use
         * @return The resulting `CharSource` object
         * @throws IllegalStateException if there is an error reading the file
         */
        public static CharSource fileSource(final File fileSource, final Charset charset) {
            try {
                return byteSource(Files.readAllBytes(Paths.get(fileSource.getAbsolutePath())), charset);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        /**
         * Creates a `CharSource` object from the specified input stream using the specified `Charset`.
         *
         * @param inputStreamSource The input stream to read from
         * @param charset The `Charset` to use
         * @return The resulting `CharSource` object
         */
        public static CharSource inputStreamSource(final InputStream inputStreamSource, final Charset charset) {
            return readerSource(new InputStreamReader(inputStreamSource, charset));
        }

        /**
         * Creates a `CharSource` object from the specified input stream using the UTF-8 `Charset`.
         *
         * @param inputStreamSource The input stream to read from
         * @return The resulting `CharSource` object
         */
        public static CharSource inputStreamSource(final InputStream inputStreamSource) {
            return inputStreamSource(inputStreamSource, StandardCharsets.UTF_8);
        }

        /**
         * Creates a `CharSource` object from the specified `Reader`.
         *
         * @param readerSource The `Reader` to read from
         * @return The resulting `CharSource` object
         */
        public static CharSource readerSource(final Reader readerSource) {
            final BufferedReader reader = new BufferedReader(readerSource);
            StringBuilder stringBuilder = new StringBuilder();
            try {
                String s = reader.readLine();
                while (s != null) {
                    stringBuilder.append(s).append('\n');
                    s = reader.readLine();
                }
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            return new CharArrayCharSource(stringBuilder.toString());
        }
    }
