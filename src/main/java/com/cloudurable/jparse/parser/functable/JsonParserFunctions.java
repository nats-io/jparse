package com.cloudurable.jparse.parser.functable;

import com.cloudurable.jparse.node.support.ParseConstants;
import com.cloudurable.jparse.node.support.TokenList;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.source.support.UnexpectedCharacterException;
import com.cloudurable.jparse.token.Token;
import com.cloudurable.jparse.token.TokenTypes;

public class JsonParserFunctions {


    public static ParseFunction defaultFunc = (source, tokens) -> {
        throw new UnexpectedCharacterException("Scanning JSON", "Unexpected character", source);
    };

    public static void parseFalse(CharSource source, TokenList tokens) {
        int start = source.getIndex();
        int end = source.findFalseEnd();
        tokens.add(new Token(start, end, TokenTypes.BOOLEAN_TOKEN));
    }

    public static void parseTrue(CharSource source, TokenList tokens) {
        int start = source.getIndex();
        int end = source.findTrueEnd();
        tokens.add(new Token(start, end, TokenTypes.BOOLEAN_TOKEN));
    }

    public static void parseNull(CharSource source, TokenList tokens) {
        int start = source.getIndex();
        int end = source.findNullEnd();
        tokens.add(new Token(start, end, TokenTypes.NULL_TOKEN));
    }

    public static void parseNumber(final CharSource source, TokenList tokens) {
        final int startIndex = source.getIndex();
        final var numberParse = source.findEndOfNumberFast();
        tokens.add(new Token(startIndex, numberParse.endIndex(), numberParse.wasFloat() ? TokenTypes.FLOAT_TOKEN : TokenTypes.INT_TOKEN));
    }

    public static boolean parseKeyNoQuote(final CharSource source, final TokenList tokens) {

        int ch = source.nextSkipWhiteSpace();
        final int startIndex = source.getIndex() - 1;
        final int tokenListIndex = tokens.getIndex();
        tokens.placeHolder();
        boolean found = false;

        switch (ch) {

            case ParseConstants.STRING_START_TOKEN:
                final int strStartIndex = startIndex + 1;
                final int strEndIndex = source.findEndOfEncodedString();
                tokens.add(new Token(strStartIndex + 1, strEndIndex, TokenTypes.STRING_TOKEN));
                found = true;
                break;

            case ParseConstants.OBJECT_END_TOKEN:
                tokens.undoPlaceholder();
                return true;

            default:
                if(Character.isAlphabetic(ch)) {
                    final int start =  source.getIndex();
                    final int end = source.findAttributeEnd();
                    tokens.add(new Token(start, end, TokenTypes.STRING_TOKEN));
                    found = true;
                } else {
                    throw new UnexpectedCharacterException("Parsing key", "Unexpected character found", source);
                }
        }



        boolean done = source.findObjectEndOrAttributeSep();

        if (!done && found) {
            tokens.set(tokenListIndex, new Token(startIndex + 1, source.getIndex(), TokenTypes.ATTRIBUTE_KEY_TOKEN));
        } else if (found && done) {

            throw new UnexpectedCharacterException("Parsing key", "Not found", source);

        }

        return done;
    }

    public static boolean parseKeyWithEncode(final CharSource source, final TokenList tokens) {

        int ch = source.nextSkipWhiteSpace();
        final int startIndex = source.getIndex() - 1;
        final int tokenListIndex = tokens.getIndex();
        tokens.placeHolder();
        boolean found = false;

        switch (ch) {

            case ParseConstants.STRING_START_TOKEN:
                final int strStartIndex = startIndex + 1;
                final int strEndIndex = source.findEndOfEncodedString();
                tokens.add(new Token(strStartIndex + 1, strEndIndex, TokenTypes.STRING_TOKEN));
                found = true;
                break;

            case ParseConstants.OBJECT_END_TOKEN:
                tokens.undoPlaceholder();
                return true;

            default:
                throw new UnexpectedCharacterException("Parsing key", "Unexpected character found", source);
        }



            boolean done = source.findObjectEndOrAttributeSep();

            if (!done && found) {
                tokens.set(tokenListIndex, new Token(startIndex + 1, source.getIndex(), TokenTypes.ATTRIBUTE_KEY_TOKEN));
            } else if (found && done) {

                throw new UnexpectedCharacterException("Parsing key", "Not found", source);

            }

            return done;

    }

    public static boolean parseKeyNoEncode(final CharSource source, final TokenList tokens) {

        int ch = source.nextSkipWhiteSpace();
        final int startIndex = source.getIndex() - 1;
        final int tokenListIndex = tokens.getIndex();
        tokens.placeHolder();
        boolean found = false;

        switch (ch) {

            case ParseConstants.STRING_START_TOKEN:
                final int strStartIndex = startIndex + 1;
                final int strEndIndex = source.findEndString();
                tokens.add(new Token(strStartIndex + 1, strEndIndex, TokenTypes.STRING_TOKEN));
                found = true;
                break;

            case ParseConstants.OBJECT_END_TOKEN:
                tokens.undoPlaceholder();
                return true;

            default:
                throw new UnexpectedCharacterException("Parsing key", "Unexpected character found", source);
        }



        boolean done = source.findObjectEndOrAttributeSep();

        if (!done && found) {
            tokens.set(tokenListIndex, new Token(startIndex + 1, source.getIndex(), TokenTypes.ATTRIBUTE_KEY_TOKEN));
        } else if (found && done) {

            throw new UnexpectedCharacterException("Parsing key", "Not found", source);

        }

        return done;

    }

    public static void parseString(final CharSource source, TokenList tokens) {
        final int startIndex = source.getIndex();
        final int endIndex = source.findEndOfEncodedStringFast();
        tokens.add(new Token(startIndex + 1, endIndex, TokenTypes.STRING_TOKEN));
    }
}
