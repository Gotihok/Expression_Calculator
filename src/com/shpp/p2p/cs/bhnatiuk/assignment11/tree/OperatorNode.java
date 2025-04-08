package com.shpp.p2p.cs.bhnatiuk.assignment11.tree;

import java.util.HashMap;

/**
 * Represents an internal node in the tree that applies a mathematical operation
 * to the result of its 2 child nodes {@link TreeNode} in order: {@code leftRes operation rightRes}
 *
 * <p>An {@code OperationNode} has 2 child nodes and executes a predefined operation
 * (such as {@code +}, {@code /}, {@code ^}, etc.) on its child nodes computed values</p>
 *
 * <p>Implements the {@link TreeNode} interface.</p>
 */
public class OperatorNode implements TreeNode {
    /** The name of the operation to be applied */
    private final char operator;

    /** Left child node field */
    private TreeNode left;

    /** Right child node field */
    private TreeNode right;

    /**
     * Constructs a {@code OperationNode} with the specified operation name
     *
     * @param operator The name of the operation to be applied
     */
    OperatorNode (char operator) {
        this.operator = operator;
        left = null;
        right = null;
    }

    /**
     * Evaluates the operation on the child nodes computed values
     *
     * <p>Performs such evaluation: {@code leftRes operation rightRes}</p>
     * <p><b>Examples: </b></p>
     * <blockquote><pre>
     *     2 + 4
     *     a - (sqrt(4))
     *     (2 + 4) * 3
     * </pre></blockquote>
     *
     * @param variables HashMap of name-value pairs for variables
     * @return The result of applying the operation to the child nodes computed values
     * @throws NullPointerException if at least 1 of the child nodes in not assigned
     */
    @Override
    public double calculate(HashMap<String, Double> variables) {
        return operations.get(operator).operation(left.calculate(variables), right.calculate(variables));
    }

    /**
     * Checks whether this node has available slots for child nodes
     *
     * @return {@code true} if there is at least 1 not assigned child node field, otherwise {@code false}
     */
    @Override
    public boolean hasNodeSpaces() {
        return left == null || right == null;
    }

    /**
     * Assigns a child node to one of the child node fields, right first
     *
     * @param next The {@link TreeNode} to be assigned as a child
     * @throws IllegalStateException if both child nodes are already assigned
     */
    @Override
    public void addNext(TreeNode next) {
        if (right == null)
            right = next;
        else if (left == null)
            left = next;
        else
            throw new IllegalStateException("OperatorNode already has child nodes assigned");
    }
}
