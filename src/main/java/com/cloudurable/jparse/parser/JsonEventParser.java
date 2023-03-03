package com.cloudurable.jparse.parser;


import com.cloudurable.jparse.node.support.ParseConstants;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.source.Sources;
import com.cloudurable.jparse.token.TokenEventListener;

public interface JsonEventParser extends ParseConstants {
    void parse(final CharSource source, final TokenEventListener tokenEvent);
    default void parse(final String source, final TokenEventListener tokenEvent) {
        parse(Sources.stringSource(source), tokenEvent);
    }
}
