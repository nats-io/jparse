package com.cloudurable.jparse;

import com.cloudurable.jparse.node.RootNode;
import com.cloudurable.jparse.node.support.ParseConstants;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.source.Sources;
import com.cloudurable.jparse.token.Token;

import java.util.List;

public interface Parser extends ParseConstants {

    List<Token> scan(final CharSource source);

    RootNode parse(final CharSource source);

    default RootNode parse(final String source) {
        return parse(Sources.stringSource(source));
    }

    default List<Token> scan(final String source) {
        return scan(Sources.stringSource(source));
    }
}
