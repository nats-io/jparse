package com.cloudurable.jparse.token;

import com.cloudurable.jparse.source.CharSource;

public interface TokenEventListener {

    void start(int tokenId, int index, CharSource source);

    void end(int tokenId, int index, CharSource source);

}
