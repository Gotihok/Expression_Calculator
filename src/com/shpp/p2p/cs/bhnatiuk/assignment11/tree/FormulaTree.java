package com.shpp.p2p.cs.bhnatiuk.assignment11.tree;

import com.shpp.p2p.cs.bhnatiuk.assignment11.PatternConstants;

import java.util.HashMap;
import java.util.Stack;

/**
 * The {@code FormulaTree} class represents a tree structure that builds and evaluates expressions
 * in prefix (Polish Notation). It recursively computes the result using a given set of variables.
 */
public class FormulaTree implements PatternConstants {
    /** The root node of the expression tree */
    private final TreeNode root;

    /**
     * Constructs a {@code FormulaTree} from a stack of tokens in prefix notation
     * <p>Parses the given prefix notation (PN) expression and builds the corresponding tree structure</p>
     *
     * @param pnFormula A stack containing tokens of the expression in prefix notation
     */
    public FormulaTree(Stack<String> pnFormula) {
        root = parseNode(pnFormula.pop());

        Stack<TreeNode> emptyFieldNodes = new Stack<>();
        emptyFieldNodes.add(root);
        while (!pnFormula.isEmpty()) {
            TreeNode current = parseNode(pnFormula.pop());
            TreeNode previous = emptyFieldNodes.peek();

            previous.addNext(current);

            if (!previous.hasNodeSpaces())
                emptyFieldNodes.pop();

            if (current.hasNodeSpaces())
                emptyFieldNodes.add(current);
        }
    }

    /**
     * Identifies the type of the given token and returns the corresponding {@link TreeNode} implementation
     *
     * @param token The token to be converted into a tree node. It will be assigned as its value
     * @return A {@link TreeNode} implementation corresponding to the token type
     * @throws IllegalArgumentException if token doesn't match any expected pattern (wrong token passed)
     */
    private TreeNode parseNode(String token) {
        if (token.matches(OPERATOR_PATTERN)) {
            return new OperatorNode(token.charAt(0));
        } else if (token.matches(FUNCTION_PATTERN)) {
            return new FunctionNode(token);
        } else if (token.matches(DIGIT_OR_VARIABLE_PATTERN)) {
            return new ValueNode(token);
        } else {
            throw new IllegalArgumentException("Wrong token: " + token);
        }
    }

    /**
     * Evaluates the expression tree by recursively computing the values of its nodes
     * <p>It directly calls the calculate() method on the root node. If the node has child nodes, their calculate()
     * methods are invoked recursively, and their computed values are used in further calculations</p>
     *
     * @param variables HashMap of name-value pairs for variables
     * @return The computed result of the expression
     */
    public double calculate(HashMap<String, Double> variables) {
        return root.calculate(variables);
    }
}
