package com.cloudurable.jparse.node.support;


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
