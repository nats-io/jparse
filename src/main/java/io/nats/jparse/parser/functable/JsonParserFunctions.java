package io.nats.jparse.parser.functable;

import io.nats.jparse.node.support.NumberParseResult;
import io.nats.jparse.node.support.ParseConstants;
import io.nats.jparse.node.support.TokenList;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.source.support.UnexpectedCharacterException;
import io.nats.jparse.token.Token;
import io.nats.jparse.token.TokenTypes;


/**
 * A collection of functions used by the `JsonFuncParser` to parse JSON.
 */
public class JsonParserFunctions {

    /*
     * Default Function.
     *
     */
    public static ParseFunction defaultFunc = (source, tokens) -> {
        throw new UnexpectedCharacterException("Scanning JSON", "Unexpected character", source);
    };

    /**
     * Parses the "false" token from the given character source and adds the resulting token to the given token list.
     *
     * @param source the `CharSource` to parse
     * @param tokens the `TokenList` to which to add the resulting token
     */
    public static void parseFalse(CharSource source, TokenList tokens) {
        int start = source.getIndex();
        int end = source.findFalseEnd();
        tokens.add(new Token(start, end, TokenTypes.BOOLEAN_TOKEN));
    }

    /**
     * Parses the "true" token from the given character source and adds the resulting token to the given token list.
     *
     * @param source the `CharSource` to parse
     * @param tokens the `TokenList` to which to add the resulting token
     */
    public static void parseTrue(CharSource source, TokenList tokens) {
        int start = source.getIndex();
        int end = source.findTrueEnd();
        tokens.add(new Token(start, end, TokenTypes.BOOLEAN_TOKEN));
    }

    /**
     * Parses the "null" token from the given character source and adds the resulting token to the given token list.
     *
     * @param source the `CharSource` to parse
     * @param tokens the `TokenList` to which to add the resulting token
     */
    public static void parseNull(CharSource source, TokenList tokens) {
        int start = source.getIndex();
        int end = source.findNullEnd();
        tokens.add(new Token(start, end, TokenTypes.NULL_TOKEN));
    }

    /**
     * Parses a number from the given character source and adds the resulting token to the given token list.
     *
     * @param source the `CharSource` to parse
     * @param tokens the `TokenList` to which to add the resulting token
     */
    public static void parseNumber(final CharSource source, TokenList tokens) {
        final int startIndex = source.getIndex();
        final NumberParseResult numberParse = source.findEndOfNumberFast();
        tokens.add(new Token(startIndex, numberParse.endIndex(), numberParse.wasFloat() ? TokenTypes.FLOAT_TOKEN : TokenTypes.INT_TOKEN));
    }

    /**
     * Parses an unquoted key from the given character source and adds the resulting token to the given token list.
     *
     * @param source the `CharSource` to parse
     * @param tokens the `TokenList` to which to add the resulting token
     * @return `true` if the parse was successful, `false` otherwise
     */
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
                if (Character.isAlphabetic(ch)) {
                    final int start = source.getIndex();
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

    /**
     * Parses an encoded key from the given character source and adds the resulting token to the given token list.
     *
     * @param source the `CharSource` to parse
     * @param tokens the `TokenList` to which to add the resulting token
     * @return `true` if the parse was successful, `false` otherwise
     */
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

    /**
     * Parses an unencoded key from the given character source and adds the resulting token to the given token list.
     *
     * @param source the `CharSource` to parse
     * @param tokens the `TokenList` to which to add the resulting token
     * @return `true` if the parse was successful, `false` otherwise
     */
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

    /**
     * Parses a string given character source and adds the resulting token to the given token list.
     *
     * @param source the `CharSource` to parse
     * @param tokens the `TokenList` to which to add the resulting token
     */
    public static void parseString(final CharSource source, TokenList tokens) {
        final int startIndex = source.getIndex();
        final int endIndex = source.findEndOfEncodedStringFast();
        tokens.add(new Token(startIndex + 1, endIndex, TokenTypes.STRING_TOKEN));
    }
}
