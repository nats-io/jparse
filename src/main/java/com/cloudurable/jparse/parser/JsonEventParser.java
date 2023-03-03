package com.cloudurable.jparse.parser;


import com.cloudurable.jparse.node.support.ParseConstants;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.source.Sources;
import com.cloudurable.jparse.token.TokenEventListener;

public interface JsonEventParser extends ParseConstants {

    TokenEventListener tokenEvents();

    void parseWithEvents(final CharSource source, TokenEventListener tokenEvents);
    default void parseWithEvents(final String source, TokenEventListener tokenEvents) {
        parseWithEvents(Sources.stringSource(source),tokenEvents);
    }
    default void parseWithEvents(final CharSource source) {
        parseWithEvents(source, tokenEvents());
    }
    default void parseWithEvents(final String source) {
        parseWithEvents(Sources.stringSource(source), tokenEvents());
    }
}
