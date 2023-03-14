package io.nats.jparse.parser.functable;

import io.nats.jparse.node.support.TokenList;
import io.nats.jparse.source.CharSource;

public interface ParsePartFunction {

    boolean parse(CharSource source, TokenList tokens);
}
