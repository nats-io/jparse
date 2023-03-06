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
package com.cloudurable.jparse.source;

import com.cloudurable.jparse.Json;
import com.cloudurable.jparse.parser.JsonIndexOverlayParser;
import com.cloudurable.jparse.node.RootNode;
import com.cloudurable.jparse.node.support.PathUtils;
import com.cloudurable.jparse.token.Token;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

import static com.cloudurable.jparse.node.support.PathUtils.walkFull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SourcesTest {

    final static String glossaryJson;

    public JsonIndexOverlayParser jsonParser() {
        return Json.builder().setStrict(true).build();
    }


    static {
        try {
            final File file = new File("./src/test/resources/json/glossary.json");
            final CharSource charSource = Sources.fileSource(file);
            glossaryJson = charSource.toString().trim();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

    }

    @Test
    void testSimple() {
        final File file = new File("./src/test/resources/json/glossary.json");
        assertTrue(file.exists());
    }

    @Test
    void testSourceFile() {


        final File file = new File("./src/test/resources/json/glossary.json");

        final CharSource charSource = Sources.fileSource(file);

        doTest(charSource);
    }

    @Test
    void testSourceFileName() {
        final CharSource charSource = Sources.fileSource("./src/test/resources/json/glossary.json");
        doTest(charSource);
    }

    @Test
    void testInputStream() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("./src/test/resources/json/glossary.json");
        final CharSource charSource = Sources.inputStreamSource(fileInputStream);
        doTest(charSource);
    }

    @Test
    void testReader() throws Exception {
        FileReader reader = new FileReader("./src/test/resources/json/glossary.json");
        final CharSource charSource = Sources.readerSource(reader);
        doTest(charSource);
    }

    @Test
    void testOffset() throws Exception {
        final CharSource charSource = Sources.charSource(0, glossaryJson.toCharArray());
        doTest(charSource);
    }

    @Test
    void testOffsetWithEnd() throws Exception {
        final CharSource charSource = Sources.charSource(0, glossaryJson.length(), glossaryJson.toCharArray());
        doTest(charSource);
    }

    @Test
    void testBytes()  {
        final CharSource charSource = Sources.byteSource(glossaryJson.getBytes(StandardCharsets.UTF_8));
        doTest(charSource);
    }

    @Test
    void testCharBuffer()  {
        CharBuffer charBuffer = CharBuffer.allocate(glossaryJson.length());
        charBuffer.put(glossaryJson);
        charBuffer.flip();
        final CharSource charSource = Sources.charBufferSource(charBuffer);
        doTest(charSource);
    }

    @Test
    void testCharBufferSimple() {
        final var testString = "0123456789";
        CharBuffer charBuffer = CharBuffer.allocate(testString.length());
        charBuffer.put(testString);
        charBuffer.flip();
        final CharSource charSource = Sources.charBufferSource(charBuffer);
        String sub = new String(charSource.getArray(0, 1));
        assertEquals("0", sub);
        assertEquals("012", charSource.getString(0, 3));
    }

    @Test
    void testCharSeq()  {
        final CharSource charSource = Sources.charSeqSource(glossaryJson);
        doTest(charSource);
    }


    @Test
    void testAll() throws Exception {

        final File file = new File("./src/test/resources/json/");

        for (File listFile : file.listFiles()) {
            final CharSource cs = Sources.fileSource(listFile);
            doTest(cs, listFile);
        }

    }


    private void doTest(CharSource charSource, File file) {

        try {
            final var jsonParser = jsonParser();

            RootNode jsonRoot = jsonParser.parse(charSource);

            walkFull(jsonRoot.getNode());
            jsonRoot.getNode().rootElementToken();
        } catch (Exception ex) {
            throw new IllegalStateException("" + file, ex);
        }
    }

    private void doTest(CharSource charSource) {
        final var jsonParser = jsonParser();

        RootNode jsonRoot = jsonParser.parse(charSource);
        Token token = jsonRoot.getNode().rootElementToken();

        walkFull(jsonRoot.getNode());
        testJsonElementPathLookup(jsonRoot);

        String string = charSource.getString(token.startIndex, token.endIndex);
        assertEquals(glossaryJson, string);
        String string2 = new String(charSource.getArray(token.startIndex, token.endIndex));
        assertEquals(glossaryJson, string2);
        charSource.getChartAt(token.startIndex);
    }

    private void testJsonElementPathLookup(RootNode jsonRoot) {
        String id = PathUtils.getString("glossary/GlossDiv/GlossList/GlossEntry/ID", jsonRoot);
        assertEquals("SGML", id);
        String para = PathUtils.getString("glossary/GlossDiv/GlossList/GlossEntry/GlossDef/para", jsonRoot);
        assertEquals("A \"meta-markup\" language, used to create markup languages such as DocBook.\nEpstein didn't kill himself\n\u2639", para);
        boolean published = PathUtils.getBoolean("glossary/GlossDiv/GlossList/GlossEntry/GlossDef/published", jsonRoot);
        assertEquals(true, published);
        int lines = PathUtils.getInt("glossary/GlossDiv/GlossList/GlossEntry/GlossDef/lines", jsonRoot);
        assertEquals(100, lines);
    }

    private void testObjectPathLookup(Object root) {
        validateBasePath(root);
        long lines = (Long) PathUtils.getObject("glossary/GlossDiv/GlossList/GlossEntry/GlossDef/lines", root);
        assertEquals(100L, lines);
        double score = (Double) PathUtils.getObject("glossary/GlossDiv/GlossList/GlossEntry/GlossDef/score", root);
        assertEquals(0.7, score, 0.001);

        walkFull(root);

    }

    private void testObjectPathLookupBig(Object root) {
        validateBasePath(root);
        BigInteger lines = (BigInteger) PathUtils.getObject("glossary/GlossDiv/GlossList/GlossEntry/GlossDef/lines", root);
        assertEquals(new BigInteger("100"), lines);
        BigDecimal score = (BigDecimal) PathUtils.getObject("glossary/GlossDiv/GlossList/GlossEntry/GlossDef/score", root);
        assertEquals(new BigDecimal("0.7"), score);
        walkFull(root);

    }

    private void testObjectPathLookupSmall(Object root) {
        validateBasePath(root);
        int lines = (Integer) PathUtils.getObject("glossary.GlossDiv.GlossList.GlossEntry.GlossDef.lines", root);
        assertEquals(100, lines);
        float score = (Float) PathUtils.getObject("glossary.GlossDiv.GlossList.GlossEntry.GlossDef.score", root);
        assertEquals(0.7, score, 0.001);
        walkFull(root);

    }


    private void validateBasePath(Object root) {
        String id = PathUtils.getString("glossary/GlossDiv/GlossList/GlossEntry/ID", root);
        assertEquals("SGML", id);
        String para = PathUtils.getString("glossary.GlossDiv.GlossList.GlossEntry.GlossDef.para", root);
        assertEquals("A \"meta-markup\" language, used to create markup languages such as DocBook.\nEpstein didn't kill himself\n\u2639", para);
        boolean published = (Boolean) PathUtils.getObject("glossary.GlossDiv.GlossList.GlossEntry.GlossDef.published", root);
        assertTrue(published);
    }


}
