package io.nats.jparse.source.support;

public class ParseDouble {

    static final double[] powersOf10 = {1e0, 1e1, 1e2, 1e3, 1e4, 1e5, 1e6, 1e7,
            1e8, 1e9, 1e10, 1e11, 1e12, 1e13, 1e14, 1e15, 1e16, 1e17, 1e18, 1e19, 1e20, 1e21, 1e22};


    public static double parseDouble(char[] chars, int startIndex, int endIndex) {


        boolean negative = false;
        int i = startIndex;
        double result = 0;

        // Check for a negative sign
        if (chars[i] == '-') {
            negative = true;
            i++;
        }

        loop:
        while (i < endIndex) {
            char ch = chars[i];
            switch (ch) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    result = result * 10 + (ch - '0');
                    i++;
                    break;
                case '.':
                    result = parseFractionPart(i + 1, endIndex, chars, result);
                    break loop;
                case 'E':
                case 'e':
                    result = parseExponent(i + 1, endIndex, chars, result);
                    break loop;
                default:
                    throw new IllegalStateException("index " + i + " char " + ch);
            }
        }


        if (negative) {
            result = -result;
        }

        return result;
    }

    private static double parseFractionPart(int i, int endIndex, char[] chars, double result) {
        double fraction = 0.1;
        while (i < endIndex) {
            char ch = chars[i];
            switch (ch) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    result += (ch - '0') * fraction;
                    fraction /= 10;
                    i++;
                    break;

                case 'E':
                case 'e':
                    return parseExponent(i + 1, endIndex, chars, result);

                default:
                    throw new IllegalStateException("index " + i + " char " + ch);
            }

        }
        return result;
    }


    private static double parseExponent(int i, int endIndex, char[] chars, double result) {

        boolean exponentNegative = false;
        int exponent = 0;


        char sign =  chars[i];

        switch (sign) {
            case '-':
                exponentNegative = true;
                i++;
                break;
            case '+':
                i++;
                break;
        }

        while (i < endIndex) {
            char ch = chars[i];
            switch (chars[i]) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    exponent = exponent * 10 + (ch - '0');
                    i++;
                    break;

                default:
                    throw new IllegalStateException("index " + i + " char " + ch);
            }
        }

        if (exponentNegative) {
            exponent = -exponent;
        }

        // Use Lookup table for powers of 10

        // Calculate the power of 10
        if (!exponentNegative) {
            while (exponent >= powersOf10.length) {
                result *= 1e22;
                exponent -= 22;
            }
            result *= powersOf10[exponent];
        } else {
            while (-exponent >= powersOf10.length) {
                result /= 1e22;
                exponent += 22;
            }
            result /= powersOf10[-exponent];
        }

        return result;
    }

}
