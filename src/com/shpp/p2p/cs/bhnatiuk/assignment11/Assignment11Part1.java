package com.shpp.p2p.cs.bhnatiuk.assignment11;

import com.shpp.p2p.cs.bhnatiuk.assignment11.tree.FormulaTree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code Assignment11Part1} class is an advanced calculator program that evaluates a mathematical expression
 * with {@code numbers, variables, operators, functions, parentheses}
 *
 * <p>The program takes an array of arguments, constructs a three, computes the tree's value,
 * and prints the result as a pure number to the console</p>
 *
 * <p><b>Supported operators:</b> {@code +, -, *, /, ^}</p>
 * <p><b>Supported functions:</b> {@code sin, cos, tan, atan, log10, log2, sqrt}</p>
 *
 * <p><b>Input format:</b> Arguments should be formatted as: {@code formula, var1, var2, ..., varN}.
 * The first argument is the formula, followed by variable assignments.
 * Spaces in expressions and variables are ignored:
 * {@code "a + b / - c"} is equivalent to {@code "a+b/-c"}.
 * If any unsupported characters will be entered, the program will throw an exception</p>
 *
 * <p><b>Unary minus:</b> Must be at the start of the expression or after an operator directly before a number/variable.
 * Does not support unary minus before parenthesis of there is no preceding operator or number (will be treated as
 * default minus): Unsupported: {@code -(4)}; Supported: {@code 2-(4)} or {@code 3+-(4)}</p>
 *
 * <p><b>Error Handling:</b> The program throws exceptions for invalid input,
 * which should be handled externally to prevent crashes</p>
 *
 * <p><b>Usage examples:</b></p>
 * <blockquote><pre>
 * Assignment11Part1.main(new String[]{"2+a", "a=5"});
 * Assignment11Part1.main(new String[]{"7 * 2 + -b", "a=2", "b=9"});
 * Assignment11Part1.main(new String[]{"sin(a1 + a2)", "a1=2", "a2=9"});
 * Assignment11Part1.main(new String[]{"sqrt(tan(3.14 * 5) ^ 3)"});
 * Assignment11Part1.main(new String[]{"1+(2+3*(4+5-sin(45*cos(a))))/7"});
 * </pre></blockquote>
 *
 * @author Bohdan Hnatiuk
 */
public class Assignment11Part1 implements PatternConstants {
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

        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].replaceAll(" ", "");
            if (!args[i].matches(ALLOWED_SYMBOLS_PATTERN))
                throw new IllegalArgumentException("Wrong symbol passed there: " + args[i]);
        }

        if (args[0].matches("="))
            throw new IllegalArgumentException("Wrong symbol passed: '=' in formula");

        HashMap<String, Double> variables = parseVariables(args);
        LinkedList<String> formulaTokens = splitFormula(args[0]);

        Stack<String> rpnFormula = parsePN(formulaTokens);

        FormulaTree formulaTree = new FormulaTree(rpnFormula);

        double res = formulaTree.calculate(variables);
        System.out.println(res);
    }

    /**
     * Splits a mathematical formula into value (numbers and variables), operator, function (sin, cos, sqrt, ... )
     * and parentheses tokens keeping them as strings without conversion.
     *
     * @param formula String with mathematical expression without spaces
     * @return Queue (LinkedList) of split formula tokens
     */
    public static LinkedList<String> splitFormula (String formula) {
        LinkedList<String> tokens = new LinkedList<>();

        Pattern formulaSplittingPattern = Pattern.compile(
                DIGIT_OR_VARIABLE_PATTERN + "|"
                        + OPERATOR_PATTERN + "|"
                        + FUNCTION_PATTERN + "|"
                        + PARENTHESIS_PATTERN
        );

        Matcher formulaSplittingMatcher = formulaSplittingPattern.matcher(formula);

        while (formulaSplittingMatcher.find())
            tokens.add(formulaSplittingMatcher.group());

        return tokens;
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
            if (!variableValidationPattern.matcher(strVariable).matches()) {
                throw new IllegalArgumentException("Illegal variable argument: " + strVariable);
            }

            String[] tokens = strVariable.split("=");
            variables.put(tokens[0], Double.parseDouble(tokens[1]));
        }

        return variables;
    }

    /**
     * Converts an infix mathematical expression into prefix notation using the Shunting Yard Algorithm.
     *
     * <p><strong>Limitations:</strong> This method does not recognize empty parentheses {@code "()"}.
     * Using them in the input may lead to exceptions during further processing.</p>
     *
     * @param formulaTokens Queue (represented as LinkedList) containing the
     *                      infix notation mathematical expression as separate tokens
     * @return Stack with parsed tokens into prefix notation
     * @throws IllegalArgumentException If the expression contains mismatched parentheses
     */
    public static Stack<String> parsePN(LinkedList<String> formulaTokens) {
        /* Map with the priorities of the operators for the operation order checks */
        final Map<String, Integer> priorities = Map.of(
                "+", 0,
                "-", 0,
                "*", 1,
                "/", 1,
                "^", 2
        );

        Stack<String> rpnFormula = new Stack<>();
        Stack<String> operatorsStack = new Stack<>();

        // Processing all formula tokens
        while (!formulaTokens.isEmpty()) {
            String token = formulaTokens.poll();

            if (token.matches(FUNCTION_PATTERN) || token.equals("(")) {
                operatorsStack.add(token);
            } else if (token.matches(DIGIT_OR_VARIABLE_PATTERN)) {
                rpnFormula.add(token);
            } else if (token.matches(OPERATOR_PATTERN)) {
                // Adding the operator to the operatorsStack if the stack empty, or it's top element is "("
                // Popping the operator from the stack to the output if it has bigger priority than current (token)
                while (!operatorsStack.isEmpty() && !operatorsStack.peek().equals("(") &&
                        priorities.get(operatorsStack.peek()) >= priorities.get(token)) {
                    rpnFormula.add(operatorsStack.pop());
                }
                operatorsStack.add(token);

            } else if (token.equals(")")) {
                // Popping the operatorsStack to the output until
                // finding the opening parenthesis "(", or the stack is empty
                while (!operatorsStack.isEmpty() && !operatorsStack.peek().equals("("))
                    rpnFormula.add(operatorsStack.pop());

                // If stack is empty at this point or there aren't an opening parenthesis then there are
                // mismatched parentheses: not found opening parenthesis "(" for closing ")"
                if (operatorsStack.isEmpty() || !operatorsStack.peek().equals("("))
                    throw new IllegalArgumentException("Invalid parentheses in the expression");

                // Popping the opening parenthesis "(" out of the stack
                operatorsStack.pop();

                // Popping the function to the output if there is one
                // (expression in parentheses as argument for function)
                if (!operatorsStack.isEmpty() && operatorsStack.peek().matches(FUNCTION_PATTERN))
                    rpnFormula.add(operatorsStack.pop());
            }
        }

        // Processing elements left in the operatorsStack
        while (!operatorsStack.isEmpty()) {
            // There should be no opening parentheses unless there is a mismatch
            // (closing can't be here, if mismatching then the exception will be thrown in code above)
            if (operatorsStack.peek().equals("("))
                throw new IllegalArgumentException("Invalid parentheses in the expression");
            rpnFormula.add(operatorsStack.pop());
        }

        return rpnFormula;
    }
}
