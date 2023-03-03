package com.cloudurable.jparse.path;

import com.cloudurable.jparse.node.RootNode;
import com.cloudurable.jparse.node.support.TokenList;
import com.cloudurable.jparse.parser.JsonIndexOverlayParser;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.Token;
import com.cloudurable.jparse.token.TokenTypes;

import java.util.List;

public class PathParser implements JsonIndexOverlayParser {


    @Override
    public List<Token> scan(final CharSource source) {

        return scan(source, new TokenList());

    }

    private List<Token> scan(CharSource source, TokenList tokens) {

        char ch = ' ';

        loop:
        while (true) {

            ch = (char) source.next();

            switch (ch) {

                case INDEX_BRACKET_START_TOKEN:
                    parseIndexOrKey(source, (char) source.next(), tokens);
                    break;

                case A: //'A';
                case B: //'B';
                case C: //'C';
                case D: //'D';
                case E: //'E';
                case F: //'F';
                case G: //'G';
                case H: //'H';
                case I: //'I';
                case J: //'J';
                case K: //'K';
                case L: //'L';
                case M: //'M';
                case N: //'N';
                case O: //'O';
                case P: //'P';
                case Q: //'Q';
                case R: //'R';
                case S: //'S';
                case T: //'T';
                case U: //'U';
                case V: //'V';
                case W: //'W';
                case X: //'X';
                case Y: //'Y';
                case Z: //'Z';
                case A_: // = 'a';
                case B_: //'b';
                case C_: //'c';
                case D_: //'d';
                case E_: //'e';
                case F_: //'f';
                case G_: //'g';
                case H_: //'h';
                case I_: //'i';
                case J_: //'j';
                case K_: //'k';
                case L_: //'l';
                case M_: //'m';
                case N_: //'n';
                case O_: //'o';
                case P_: //'p';
                case Q_: //'q';
                case R_: //'r';
                case S_: //'s';
                case T_: //'t';
                case U_: //'u';
                case V_: //'v';
                case W_: //'w';
                case X_: //'x';
                case Y_: //'y';
                case Z_: //'z';
                    parseKeyName(source, ch, tokens);
                    break;


                case DOT:
                    parseKeyName(source, (char) source.next(), tokens);
                    break;

                case ETX:
                    break loop;

                default:
                    throw new IllegalStateException("Unable to understand char " + ch + " index " + source.getIndex());


            }

        }
        return tokens;
    }


    private void parseIndexOrKey(CharSource source, char ch, TokenList tokens) {

        final var startIndex = source.getIndex();


        switch (ch) {

            case NUM_0:
            case NUM_1:
            case NUM_2:
            case NUM_3:
            case NUM_4:
            case NUM_5:
            case NUM_6:
            case NUM_7:
            case NUM_8:
            case NUM_9:
                parseIndex(source, startIndex, tokens, ch);
                break;


            case SINGLE_QUOTE:
                parseKeyWithQuotes(source, startIndex + 1, tokens, ch);
                break;

            case ETX:
                throw new IllegalStateException("reached end");


            default:
                throw new IllegalStateException("Unable to understand char " + ch + " index " + source.getIndex());
        }


    }

    private void parseKeyWithQuotes(CharSource source, int startIndex, TokenList tokens, char ch) {


        loop:
        while (true) {

            ch = (char) source.next();

            switch (ch) {


                case A: //'A';
                case B: //'B';
                case C: //'C';
                case D: //'D';
                case E: //'E';
                case F: //'F';
                case G: //'G';
                case H: //'H';
                case I: //'I';
                case J: //'J';
                case K: //'K';
                case L: //'L';
                case M: //'M';
                case N: //'N';
                case O: //'O';
                case P: //'P';
                case Q: //'Q';
                case R: //'R';
                case S: //'S';
                case T: //'T';
                case U: //'U';
                case V: //'V';
                case W: //'W';
                case X: //'X';
                case Y: //'Y';
                case Z: //'Z';
                case A_: // = 'a';
                case B_: //'b';
                case C_: //'c';
                case D_: //'d';
                case E_: //'e';
                case F_: //'f';
                case G_: //'g';
                case H_: //'h';
                case I_: //'i';
                case J_: //'j';
                case K_: //'k';
                case L_: //'l';
                case M_: //'m';
                case N_: //'n';
                case O_: //'o';
                case P_: //'p';
                case Q_: //'q';
                case R_: //'r';
                case S_: //'s';
                case T_: //'t';
                case U_: //'u';
                case V_: //'v';
                case W_: //'w';
                case X_: //'x';
                case Y_: //'y';
                case Z_: //'z';
                case NEW_LINE_WS:
                case CARRIAGE_RETURN_WS:
                case TAB_WS:
                case SPACE_WS:
                    continue;

                case SINGLE_QUOTE:
                    break loop;

                case ETX:
                    throw new IllegalStateException("reached end");


                default:
                    if (ch > 20 && ch < 127) {
                        break;
                    } else {
                        throw new IllegalStateException("Unable to understand char " + ch + " index " + source.getIndex());
                    }
            }
        }

        final var endIndex = source.getIndex();
        int i = source.nextSkipWhiteSpace();
        if (i == INDEX_BRACKET_END_TOKEN) {
            tokens.add(new Token(startIndex, endIndex, TokenTypes.PATH_KEY_TOKEN));
        } else {
            throw new IllegalStateException("Unable to understand char " + ch + " index " + source.getIndex());
        }

    }


    private void parseKeyName(CharSource source, char ch, TokenList tokens) {
        final var startIndex = source.getIndex();

        loop:
        while (true) {

            ch = (char) source.next();
            switch (ch) {

                case A: //'A';
                case B: //'B';
                case C: //'C';
                case D: //'D';
                case E: //'E';
                case F: //'F';
                case G: //'G';
                case H: //'H';
                case I: //'I';
                case J: //'J';
                case K: //'K';
                case L: //'L';
                case M: //'M';
                case N: //'N';
                case O: //'O';
                case P: //'P';
                case Q: //'Q';
                case R: //'R';
                case S: //'S';
                case T: //'T';
                case U: //'U';
                case V: //'V';
                case W: //'W';
                case X: //'X';
                case Y: //'Y';
                case Z: //'Z';
                case A_: // = 'a';
                case B_: //'b';
                case C_: //'c';
                case D_: //'d';
                case E_: //'e';
                case F_: //'f';
                case G_: //'g';
                case H_: //'h';
                case I_: //'i';
                case J_: //'j';
                case K_: //'k';
                case L_: //'l';
                case M_: //'m';
                case N_: //'n';
                case O_: //'o';
                case P_: //'p';
                case Q_: //'q';
                case R_: //'r';
                case S_: //'s';
                case T_: //'t';
                case U_: //'u';
                case V_: //'v';
                case W_: //'w';
                case X_: //'x';
                case Y_: //'y';
                case Z_: //'z';
                    continue;

                case ETX:
                    break loop;

                case DOT:
                    break loop;

                case INDEX_BRACKET_START_TOKEN:
                    final var endIndex = source.getIndex();
                    tokens.add(new Token(startIndex, endIndex, TokenTypes.PATH_KEY_TOKEN));
                    parseIndexOrKey(source, (char) source.next(), tokens);
                    return;


                default:
                    throw new IllegalStateException("Unable to understand char " + ch + " index " + source.getIndex());
            }
        }

        final var endIndex = source.getIndex();

        tokens.add(new Token(startIndex, endIndex, TokenTypes.PATH_KEY_TOKEN));


    }

    private void parseIndex(CharSource source, int startIndex, TokenList tokens, char ch) {
        loop:
        while (true) {

            ch = (char) source.next();
            switch (ch) {

                case NUM_0:
                case NUM_1:
                case NUM_2:
                case NUM_3:
                case NUM_4:
                case NUM_5:
                case NUM_6:
                case NUM_7:
                case NUM_8:
                case NUM_9:
                    break;

                case INDEX_BRACKET_END_TOKEN:
                    break loop;


                case ETX:
                    throw new IllegalStateException("reached end");


                default:
                    throw new IllegalStateException("Unable to understand char " + ch + " index " + source.getIndex());


            }
        }

        final var endIndex = source.getIndex();

        tokens.add(new Token(startIndex, endIndex, TokenTypes.PATH_INDEX_TOKEN));

    }


    @Override
    public RootNode parse(CharSource source) {
        return new RootNode((TokenList) scan(source), source, true);
    }
}
