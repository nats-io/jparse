/*
 * Copyright 2013-2023 Richard M. Hightower
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.nats.jparse.path;

import io.nats.jparse.node.RootNode;
import io.nats.jparse.node.support.TokenList;
import io.nats.jparse.parser.JsonIndexOverlayParser;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.token.Token;
import io.nats.jparse.token.TokenTypes;
import io.nats.jparse.node.support.ParseConstants;

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

                case ParseConstants.INDEX_BRACKET_START_TOKEN:
                    parseIndexOrKey(source, (char) source.next(), tokens);
                    break;

                case ParseConstants.A: //'A';
                case ParseConstants.B: //'B';
                case ParseConstants.C: //'C';
                case ParseConstants.D: //'D';
                case ParseConstants.E: //'E';
                case ParseConstants.F: //'F';
                case ParseConstants.G: //'G';
                case ParseConstants.H: //'H';
                case ParseConstants.I: //'I';
                case ParseConstants.J: //'J';
                case ParseConstants.K: //'K';
                case ParseConstants.L: //'L';
                case ParseConstants.M: //'M';
                case ParseConstants.N: //'N';
                case ParseConstants.O: //'O';
                case ParseConstants.P: //'P';
                case ParseConstants.Q: //'Q';
                case ParseConstants.R: //'R';
                case ParseConstants.S: //'S';
                case ParseConstants.T: //'T';
                case ParseConstants.U: //'U';
                case ParseConstants.V: //'V';
                case ParseConstants.W: //'W';
                case ParseConstants.X: //'X';
                case ParseConstants.Y: //'Y';
                case ParseConstants.Z: //'Z';
                case ParseConstants.A_: // = 'a';
                case ParseConstants.B_: //'b';
                case ParseConstants.C_: //'c';
                case ParseConstants.D_: //'d';
                case ParseConstants.E_: //'e';
                case ParseConstants.F_: //'f';
                case ParseConstants.G_: //'g';
                case ParseConstants.H_: //'h';
                case ParseConstants.I_: //'i';
                case ParseConstants.J_: //'j';
                case ParseConstants.K_: //'k';
                case ParseConstants.L_: //'l';
                case ParseConstants.M_: //'m';
                case ParseConstants.N_: //'n';
                case ParseConstants.O_: //'o';
                case ParseConstants.P_: //'p';
                case ParseConstants.Q_: //'q';
                case ParseConstants.R_: //'r';
                case ParseConstants.S_: //'s';
                case ParseConstants.T_: //'t';
                case ParseConstants.U_: //'u';
                case ParseConstants.V_: //'v';
                case ParseConstants.W_: //'w';
                case ParseConstants.X_: //'x';
                case ParseConstants.Y_: //'y';
                case ParseConstants.Z_: //'z';
                    parseKeyName(source, ch, tokens);
                    break;


                case ParseConstants.DOT:
                    parseKeyName(source, (char) source.next(), tokens);
                    break;

                case ParseConstants.ETX:
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

            case ParseConstants.NUM_0:
            case ParseConstants.NUM_1:
            case ParseConstants.NUM_2:
            case ParseConstants.NUM_3:
            case ParseConstants.NUM_4:
            case ParseConstants.NUM_5:
            case ParseConstants.NUM_6:
            case ParseConstants.NUM_7:
            case ParseConstants.NUM_8:
            case ParseConstants.NUM_9:
                parseIndex(source, startIndex, tokens, ch);
                break;


            case ParseConstants.SINGLE_QUOTE:
                parseKeyWithQuotes(source, startIndex + 1, tokens, ch);
                break;

            case ParseConstants.ETX:
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


                case ParseConstants.A: //'A';
                case ParseConstants.B: //'B';
                case ParseConstants.C: //'C';
                case ParseConstants.D: //'D';
                case ParseConstants.E: //'E';
                case ParseConstants.F: //'F';
                case ParseConstants.G: //'G';
                case ParseConstants.H: //'H';
                case ParseConstants.I: //'I';
                case ParseConstants.J: //'J';
                case ParseConstants.K: //'K';
                case ParseConstants.L: //'L';
                case ParseConstants.M: //'M';
                case ParseConstants.N: //'N';
                case ParseConstants.O: //'O';
                case ParseConstants.P: //'P';
                case ParseConstants.Q: //'Q';
                case ParseConstants.R: //'R';
                case ParseConstants.S: //'S';
                case ParseConstants.T: //'T';
                case ParseConstants.U: //'U';
                case ParseConstants.V: //'V';
                case ParseConstants.W: //'W';
                case ParseConstants.X: //'X';
                case ParseConstants.Y: //'Y';
                case ParseConstants.Z: //'Z';
                case ParseConstants.A_: // = 'a';
                case ParseConstants.B_: //'b';
                case ParseConstants.C_: //'c';
                case ParseConstants.D_: //'d';
                case ParseConstants.E_: //'e';
                case ParseConstants.F_: //'f';
                case ParseConstants.G_: //'g';
                case ParseConstants.H_: //'h';
                case ParseConstants.I_: //'i';
                case ParseConstants.J_: //'j';
                case ParseConstants.K_: //'k';
                case ParseConstants.L_: //'l';
                case ParseConstants.M_: //'m';
                case ParseConstants.N_: //'n';
                case ParseConstants.O_: //'o';
                case ParseConstants.P_: //'p';
                case ParseConstants.Q_: //'q';
                case ParseConstants.R_: //'r';
                case ParseConstants.S_: //'s';
                case ParseConstants.T_: //'t';
                case ParseConstants.U_: //'u';
                case ParseConstants.V_: //'v';
                case ParseConstants.W_: //'w';
                case ParseConstants.X_: //'x';
                case ParseConstants.Y_: //'y';
                case ParseConstants.Z_: //'z';
                case ParseConstants.NEW_LINE_WS:
                case ParseConstants.CARRIAGE_RETURN_WS:
                case ParseConstants.TAB_WS:
                case ParseConstants.SPACE_WS:
                    continue;

                case ParseConstants.SINGLE_QUOTE:
                    break loop;

                case ParseConstants.ETX:
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
        if (i == ParseConstants.INDEX_BRACKET_END_TOKEN) {
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

                case ParseConstants.A: //'A';
                case ParseConstants.B: //'B';
                case ParseConstants.C: //'C';
                case ParseConstants.D: //'D';
                case ParseConstants.E: //'E';
                case ParseConstants.F: //'F';
                case ParseConstants.G: //'G';
                case ParseConstants.H: //'H';
                case ParseConstants.I: //'I';
                case ParseConstants.J: //'J';
                case ParseConstants.K: //'K';
                case ParseConstants.L: //'L';
                case ParseConstants.M: //'M';
                case ParseConstants.N: //'N';
                case ParseConstants.O: //'O';
                case ParseConstants.P: //'P';
                case ParseConstants.Q: //'Q';
                case ParseConstants.R: //'R';
                case ParseConstants.S: //'S';
                case ParseConstants.T: //'T';
                case ParseConstants.U: //'U';
                case ParseConstants.V: //'V';
                case ParseConstants.W: //'W';
                case ParseConstants.X: //'X';
                case ParseConstants.Y: //'Y';
                case ParseConstants.Z: //'Z';
                case ParseConstants.A_: // = 'a';
                case ParseConstants.B_: //'b';
                case ParseConstants.C_: //'c';
                case ParseConstants.D_: //'d';
                case ParseConstants.E_: //'e';
                case ParseConstants.F_: //'f';
                case ParseConstants.G_: //'g';
                case ParseConstants.H_: //'h';
                case ParseConstants.I_: //'i';
                case ParseConstants.J_: //'j';
                case ParseConstants.K_: //'k';
                case ParseConstants.L_: //'l';
                case ParseConstants.M_: //'m';
                case ParseConstants.N_: //'n';
                case ParseConstants.O_: //'o';
                case ParseConstants.P_: //'p';
                case ParseConstants.Q_: //'q';
                case ParseConstants.R_: //'r';
                case ParseConstants.S_: //'s';
                case ParseConstants.T_: //'t';
                case ParseConstants.U_: //'u';
                case ParseConstants.V_: //'v';
                case ParseConstants.W_: //'w';
                case ParseConstants.X_: //'x';
                case ParseConstants.Y_: //'y';
                case ParseConstants.Z_: //'z';
                    continue;

                case ParseConstants.ETX:
                    break loop;

                case ParseConstants.DOT:
                    break loop;

                case ParseConstants.INDEX_BRACKET_START_TOKEN:
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

                case ParseConstants.NUM_0:
                case ParseConstants.NUM_1:
                case ParseConstants.NUM_2:
                case ParseConstants.NUM_3:
                case ParseConstants.NUM_4:
                case ParseConstants.NUM_5:
                case ParseConstants.NUM_6:
                case ParseConstants.NUM_7:
                case ParseConstants.NUM_8:
                case ParseConstants.NUM_9:
                    break;

                case ParseConstants.INDEX_BRACKET_END_TOKEN:
                    break loop;


                case ParseConstants.ETX:
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
