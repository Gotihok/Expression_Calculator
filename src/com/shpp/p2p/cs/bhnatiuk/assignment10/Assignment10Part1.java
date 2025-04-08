package com.shpp.p2p.cs.bhnatiuk.assignment10;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code Assignment10Part1} class is a simple calculator program
 * that evaluates a mathematical expression with {@code numbers, variables, operators}
 *
 * <p>The program takes an array of arguments, computes the formula's value,
 * and prints the result as a pure number to the console</p>
 *
 * <p><b>Supported operators:</b> {@code +, -, *, /, ^}.
 * Parentheses are not supported and are treated as illegal characters</p>
 *
 * <p><b>Input format:</b> Arguments should be formatted as: {@code formula, var1, var2, ..., varN}.
 * The first argument is the formula, followed by variable assignments.
 * Spaces in expressions and variables are ignored:
 * {@code "a + b / - c"} is equivalent to {@code "a+b/-c"}.</p>
 *
 * <p><b>Unary minus:</b> Must be at the start of the expression or after an operator directly before a number/variable</p>
 *
 * <p><b>Error Handling:</b> The program throws exceptions for invalid input,
 * which should be handled externally to prevent crashes</p>
 *
 * <p><b>Usage examples:</b></p>
 * <blockquote><pre>
 * Assignment10Part1.main(new String[]{"2+a", "a=5"});
 * Assignment10Part1.main(new String[]{"7 * 2 + -b", "a=2", "b=9"});
 * </pre></blockquote>
 *
 * @author Bohdan Hnatiuk
 */
public class Assignment10Part1 {
    /** Pattern that matches digits without unary minus */
    private static final String DIGIT_PATTERN = "(\\d+(\\.\\d+)?)";

    /** Pattern that matches variables without unary minus */
    private static final String VARIABLE_PATTERN = "([a-zA-Z]\\w*)";

    /** Pattern that matches unary minus after operators, should be used with numbers or variables */
    private static final String UNARY_MINUS_PATTERN = "(?<=[-+/*^]|^)-?";

    /** Pattern that matches operators */
    private static final String OPERATOR_PATTERN = "([-+/*^])";

    /** Pattern that matches digits and variables with unary minus */
    private static final String DIGIT_OR_VARIABLE_PATTERN =
            "(" + UNARY_MINUS_PATTERN + "(" + DIGIT_PATTERN + "|" + VARIABLE_PATTERN + "))";

    /**
     * <p>Main method that runs the program</p>
     * <p>It takes the arguments from args as an input and calculates math expression</p>
     *
     * @param args Arguments of the method, should be given in such format:
     *             formula, var1, var2, ... , varN
     */
    public static void main(String[] args) {
        if (args.length == 0)
            throw new IllegalArgumentException("Empty input");

        for (int i = 0; i < args.length; i++)
            args[i] = args[i].replaceAll(" ", "");

        validateFormula(args[0]);

        HashMap<String, Double> variables = parseVariables(args);
        LinkedList<String> formulaTokens = splitFormula(args[0]);

        double res = calculate(formulaTokens, variables);
        System.out.println(res);
    }

    /**
     * Calculates infix split formula, that had been validated (checked for illegal arguments or sequences).
     * Should be executed with formula tokens and variables
     *
     * @param formulaTokens Formula split to the operator and value tokens (infix notation)
     * @param variables HashMap of name -> value pairs for variables (should have all variables,
     *                  used in formula to prevent an exception and work with additional variables)
     * @return Result of the calculations
     */
    public static double calculate (LinkedList<String> formulaTokens, HashMap<String, Double> variables) {
        double res = getNumberFromToken(formulaTokens.pop(), variables);

        while (true) {
            if (formulaTokens.isEmpty()) return res;

            String operation = formulaTokens.pop();
            double nextNum = getNumberFromToken(formulaTokens.pop(), variables);
            res = calculateIteration(operation, res, nextNum, formulaTokens, variables);
        }
    }

    /**
     * Performs a mathematical operation on the given operands. If the next operation has a higher priority,
     * it is evaluated recursively first, and its result is used as the second operand of the current operation.
     *
     * @param operation The mathematical operation to perform
     * @param num1 The first operand
     * @param num2 The second operand
     * @param formulaTokens Formula split to the operator and value tokens (infix notation)
     * @param variables HashMap of name -> value pairs for variables (should have all variables,
     *                  used in formula to prevent an exception and work with additional variables)
     * @return Result of the mathematical operation
     */
    private static double calculateIteration(String operation, double num1, double num2,
                                             LinkedList<String> formulaTokens, HashMap<String, Double> variables) {
        /* Map with the priorities of the operators for the operation order checks */
        final Map<String, Integer> priorities = Map.of(
                "+", 0,
                "-", 0,
                "*", 1,
                "/", 1,
                "^", 2
        );

        if (!formulaTokens.isEmpty() && priorities.get(operation) < priorities.get(formulaTokens.peek())) {
            String nextOperation = formulaTokens.pop();
            double num3 = getNumberFromToken(formulaTokens.pop(), variables);

            num2 = calculateIteration(nextOperation, num2, num3, formulaTokens, variables);
        }
        return doOperation(operation, num1, num2);
    }

    /**
     * Parses a token as a number or variable and handles the unary minus
     *
     * @param token The token to parse (a number or variable represented as a string)
     * @param variables HashMap of name -> value pairs for variables (should have all variables,
     *                  used in formula to prevent an exception and work with additional variables)
     * @return The numerical value of the token or the corresponding variable's value
     * @throws IllegalArgumentException If a required variable is not defined in the HashMap
     */
    private static double getNumberFromToken(String token, HashMap<String, Double> variables) {
        double unaryMinusApplier = 1;
        if (token.charAt(0) == '-') {
            unaryMinusApplier = -1;
            token = token.replaceFirst("-", "");
        }

        if (variables.containsKey(token))
            return variables.get(token) * unaryMinusApplier;
        else {
            try {
                return Double.parseDouble(token) * unaryMinusApplier;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Undefined variable: " + token);
            }
        }
    }

    /**
     * Does pure mathematical operation with 2 numbers
     *
     * @param operation Mathematical operation to perform
     * @param num1 The first operand
     * @param num2 The second operand
     * @return Result of the mathematical operation
     * @throws IllegalArgumentException if switch doesn't match any operator (wrong operator passed)
     */
    public static double doOperation(String operation, double num1, double num2) {
        return switch (operation) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "*" -> num1 * num2;
            case "/" -> num1 / num2;
            case "^" -> Math.pow(num1, num2);
            default -> throw new IllegalArgumentException("Unexpected operator: " + operation);
        };
    }

    /**
     * Splits a mathematical formula into value (numbers and variables) and operator tokens,
     * keeping them as strings without conversion.
     *
     * @param formula String with mathematical expression without spaces
     * @return Queue (LinkedList) of split formula tokens
     */
    public static LinkedList<String> splitFormula (String formula) {
        LinkedList<String> tokens = new LinkedList<>();

        Pattern formulaSplittingPattern = Pattern.compile(DIGIT_OR_VARIABLE_PATTERN  + "|" + OPERATOR_PATTERN);
        Matcher formulaSplittingMatcher = formulaSplittingPattern.matcher(formula);

        while (formulaSplittingMatcher.find())
            tokens.add(formulaSplittingMatcher.group());

        return tokens;
    }

    /**
     * Validates the correctness of the formula's elements and their order
     *
     * @param formula The mathematical formula to validate (String without spaces)
     * @throws IllegalArgumentException If the formula is empty or contains structural errors
     */
    private static void validateFormula(String formula) {
        if (formula.isEmpty())
            throw new IllegalArgumentException("Formula is empty");

        Pattern formulaValidationPattern = Pattern.compile(
                "\\A" + DIGIT_OR_VARIABLE_PATTERN +
                        "(" +OPERATOR_PATTERN + DIGIT_OR_VARIABLE_PATTERN + ")*\\z"
        );
        Matcher formulaValidationMatcher = formulaValidationPattern.matcher(formula);

        if (!formulaValidationMatcher.matches())
            throw new IllegalArgumentException("Illegal formula: " + formula);
    }

    /**
     * Validates and parses variables from program arguments into a HashMap
     *
     * @param args Program arguments, where variables start from index 1
     * @return A HashMap containing parsed variables as name-value pairs
     * @throws IllegalArgumentException If there are any structural errors in the one of arguments
     */
    public static HashMap<String, Double> parseVariables(String[] args) {
        HashMap<String, Double> variables = new HashMap<>();

        Pattern variableValidationPattern = Pattern.compile(
                "\\A" + VARIABLE_PATTERN + "=-?" + DIGIT_PATTERN + "\\z");

        for (int i = 1; i < args.length; i++) {
            String strVariable = args[i].replaceAll(" ", "");

            if (!variableValidationPattern.matcher(strVariable).matches())
                throw new IllegalArgumentException("Illegal variable argument: " + strVariable);

            String[] tokens = strVariable.split("=");
            variables.put(tokens[0], Double.parseDouble(tokens[1]));
        }

        return variables;
    }
}
