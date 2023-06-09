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

import org.junit.jupiter.api.Test;

import static io.nats.jparse.Json.niceJson;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CharArrayUtilsTest {

    @Test
    void decodeJsonString() {
        final String encodedString = niceJson("hello `b `n `b `u1234 ");
        final String result = CharArrayUtils.decodeJsonString(encodedString.toCharArray(), 0, encodedString.length());

        final int expectedCount = encodedString.length() - 3 - 5;
        assertEquals(expectedCount, result.length());
        assertEquals("hello \b \n \b \u1234 ", result);
    }
}
