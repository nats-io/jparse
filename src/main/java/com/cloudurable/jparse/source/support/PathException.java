package com.cloudurable.jparse.source.support;

import com.cloudurable.jparse.source.CharSource;

public class PathException extends RuntimeException{

    public PathException(String whileDoing, String message, CharSource source,  int index) {
       super(String.format("Unexpected character while %, Error is '%s'. \n Details \n %s", whileDoing, message, source.errorDetails(message, index, source.getChartAt(index))));
    }


}
