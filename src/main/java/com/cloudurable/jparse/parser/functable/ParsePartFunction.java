package com.cloudurable.jparse.parser.functable;

import com.cloudurable.jparse.node.support.TokenList;
import com.cloudurable.jparse.source.CharSource;

public interface ParsePartFunction {

    boolean parse(CharSource source, TokenList tokens);
}
