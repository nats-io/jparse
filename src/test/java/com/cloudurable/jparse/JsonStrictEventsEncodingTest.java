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
package com.cloudurable.jparse;

import com.cloudurable.jparse.parser.JsonIndexOverlayParser;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.TokenEventListener;

public class JsonStrictEventsEncodingTest extends JsonStrictEncodingTest{
    @Override
    public JsonIndexOverlayParser jsonParser() {
        return (JsonIndexOverlayParser) Json.builder().setStrict(true).setTokenEventListener(new TokenEventListener() {
            @Override
            public void start(int tokenId, int index, CharSource source) {

            }

            @Override
            public void end(int tokenId, int index, CharSource source) {

            }
        }).buildEventParser();
    }
}
