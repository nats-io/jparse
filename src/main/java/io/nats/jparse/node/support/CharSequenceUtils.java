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


public class CharSequenceUtils {

    public static boolean equals(CharSequence cs1, CharSequence cs2) {

        if (cs1.length() != cs2.length()) return false;
        final var len = cs1.length();

        for (int i = 0; i < len; i++) {
            char a = cs1.charAt(i);
            char b = cs2.charAt(i);
            if (a != b) {
                return false;
            }
        }
        return true;
    }

    public static int hashCode(final CharSequence cs) {
        int h = 0;
        for (int index = 0; index < cs.length(); index++) {
            char v = cs.charAt(index);
            h = 31 * h + v;
        }
        return h;
    }
}
