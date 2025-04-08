package com.shpp.p2p.cs.bhnatiuk.assignment11;

/**
 * Interface containing regex pattern constants for identifying predefined elements
 */
public interface PatternConstants {
    /** Pattern that matches only all the supported symbols in expression */
    String ALLOWED_SYMBOLS_PATTERN = "\\A[\\w=()*/^.+-]*\\z";

    /** Pattern that matches digits without unary minus */
    String DIGIT_PATTERN = "(\\d+(\\.\\d+)?)";

    /** Pattern that matches variables without unary minus */
    String VARIABLE_PATTERN = "([a-zA-Z]\\w*)";

    /** Pattern that matches unary minus after operators, should be used with numbers or variables */
    String UNARY_MINUS_PATTERN = "(?<=[-+/*^(]|^)-?";

    /** Pattern that matches operators */
    String OPERATOR_PATTERN = "([-+/*^])";

    /** Pattern that matches functions */
    String FUNCTION_PATTERN = "(sin|cos|tan|atan|log10|log2|sqrt)";

    /** Pattern that matches parentheses */
    String PARENTHESIS_PATTERN = "([()])";

    /** Pattern that matches digits and variables with unary minus */
    String DIGIT_OR_VARIABLE_PATTERN =
            "(" + UNARY_MINUS_PATTERN + "(" + DIGIT_PATTERN + "|" + VARIABLE_PATTERN + "))";
}
