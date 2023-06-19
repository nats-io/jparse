package io.nats.jparse.source.support;

public class ParseFloat {

    static final float[] powersOf10 = {1e0f, 1e1f, 1e2f, 1e3f, 1e4f, 1e5f, 1e6f, 1e7f,
            1e8f, 1e9f, 1e10f, 1e11f, 1e12f, 1e13f, 1e14f, 1e15f, 1e16f, 1e17f, 1e18f};


    public static float parseFloat(char[] chars, int startIndex, int endIndex) {
        boolean negative = false;
        int i = startIndex;
        float result = 0;

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
                case 'e':
                    result = parseExponent(i + 1, endIndex, chars, result);
                    break loop;
                default:
                    throw new UnexpectedCharacterException("parsing float", "Illegal character", ch, i);
            }
        }


        if (negative) {
            result = -result;
        }

        return result;
    }

    private static float parseFractionPart(int i, int endIndex, char[] chars, float result) {
        float fraction = 0.1f;
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

                case 'e':
                    return parseExponent(i + 1, endIndex, chars, result);

                default:
                    throw new UnexpectedCharacterException("float parsing fraction part", "Illegal character", ch, i);
            }

        }
        return result;
    }

    private static float parseExponent(int i, int endIndex, char[] chars, float result) {

        boolean exponentNegative = false;
        int exponent = 0;

        char sign = chars[i];

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
                    throw new UnexpectedCharacterException("float parsing exponent part", "Illegal character", ch, i);

            }
        }


        if (exponentNegative) {
            exponent = -exponent;
        }

        // Use Lookup table for powers of 10

        // Calculate the power of 10
        if (!exponentNegative) {
            while (exponent >= powersOf10.length) {
                result *= 1e18f;
                exponent -= 18;
            }
            result *= powersOf10[exponent];
        } else {
            while (-exponent >= powersOf10.length) {
                result /= 1e18f;
                exponent += 18;
            }
            result /= powersOf10[-exponent];
        }

        return result;
    }
}
