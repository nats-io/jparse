package io.nats.jparse.parser.functable;

import io.nats.jparse.node.support.TokenList;
import io.nats.jparse.source.CharSource;

public interface ParseFunction {

   void parse(CharSource source, TokenList tokens);
}
