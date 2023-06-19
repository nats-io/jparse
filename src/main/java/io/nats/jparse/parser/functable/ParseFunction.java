package io.nats.jparse.parser.functable;

import io.nats.jparse.node.support.TokenList;
import io.nats.jparse.source.CharSource;


/**
 * A function that parses a portion of a `CharSource` and adds the resulting tokens to a `TokenList`.
 */
public interface ParseFunction {


    /**
     * Parses the given character source and adds the resulting tokens to the given list.
     *
     * @param source the `CharSource` to parse
     * @param tokens the `TokenList` to which to add the resulting tokens
     */
    void parse(CharSource source, TokenList tokens);
}
