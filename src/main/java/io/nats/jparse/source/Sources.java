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

public class Sources {

    public static CharSource charSeqSource(final CharSequence source) {
        if (source instanceof String) {
            return new CharArrayCharSource((String) source);
        } else {
            return new CharArrayCharSource(source.toString());
        }
    }

    public static CharSource stringSource(final String source) {
        return charSource(source.toCharArray());
    }

    public static CharSource byteSource(final byte[] source, final Charset charset) {
        return new CharArrayCharSource(new String(source, charset));
    }

    public static CharSource byteSource(final byte[] source) {
        return byteSource(source, StandardCharsets.UTF_8);
    }

    public static CharSource charSource(final char[] source) {
        return new CharArrayCharSource(source);
    }

    public static CharSource charSource(final int offset, final char[] source) {
        return new CharArrayOffsetCharSource(offset, source.length, source);
    }

    public static CharSource charSource(final int offset, final int endIndex, final char[] source) {
        return new CharArrayOffsetCharSource(offset, endIndex, source);
    }

    public static CharSource charBufferSource(final CharBuffer source) {
        return charSeqSource(source);
    }


    public static CharSource fileSource(final String fileNameSource) {
        return fileSource(fileNameSource, StandardCharsets.UTF_8);
    }

    public static CharSource fileSource(final String fileNameSource, final Charset charset) {
        try {
            return byteSource(Files.readAllBytes(Paths.get(fileNameSource)), charset);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static CharSource fileSource(final File fileSource) {
        return fileSource(fileSource, StandardCharsets.UTF_8);
    }

    public static CharSource fileSource(final File file, final Charset charset) {
        try {
            return byteSource(Files.readAllBytes(Paths.get(file.getAbsolutePath())), charset);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static CharSource inputStreamSource(final InputStream inputStreamSource, final Charset charset) {
        return readerSource(new InputStreamReader(inputStreamSource, charset));
    }

    public static CharSource inputStreamSource(final InputStream inputStreamSource) {
        return inputStreamSource(inputStreamSource, StandardCharsets.UTF_8);
    }

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
