package com.cloudurable.jparse.node;

import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.Token;

import java.util.List;

public interface Node extends CharSequence {

    NodeType type();

    List<Token> tokens();

    Token rootElementToken();

    CharSource charSource();

    boolean isScalar();
    boolean isCollection();

    default ScalarNode asScalar() {
        return (ScalarNode) this;
    }

    default CollectionNode asCollection() {
        return (CollectionNode) this;
    }

    @Override
    default int length() {
        var token = rootElementToken();
        return token.endIndex() - token.startIndex();
    }

    @Override
    default char charAt(int index) {
        return charSource().getChartAt(rootElementToken().startIndex() + index);
    }


    @Override
    default CharSequence subSequence(int start, int end) {
        var token = rootElementToken();
        return charSource().getCharSequence(start + token.startIndex(), end + token.startIndex());
    }

    default String originalString() {
        return charSource().getString(rootElementToken().startIndex(), rootElementToken().endIndex());
    }

    default String toJsonString() {
        return originalString();
    }

    default CharSequence originalCharSequence() {
        return charSource().getCharSequence(rootElementToken().startIndex(), rootElementToken().endIndex());
    }

    default CharSequence toJsonCharSequence() {
        return originalCharSequence();
    }

}
