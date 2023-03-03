package com.cloudurable.jparse;

import com.cloudurable.jparse.parser.JsonIndexOverlayParser;

public class JsonParserFastTest extends JsonParserTest {

    @Override
    public JsonIndexOverlayParser jsonParser() {
        return Json.builder().setStrict(false).build();
    }
}
