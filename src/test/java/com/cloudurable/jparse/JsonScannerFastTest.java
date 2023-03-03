package com.cloudurable.jparse;

import com.cloudurable.jparse.parser.JsonIndexOverlayParser;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.TokenEventListener;

public class JsonScannerFastTest extends JsonScannerTest{
    @Override
    public JsonIndexOverlayParser jsonParser() {
        return Json.builder().setStrict(false).build();
    }
}
