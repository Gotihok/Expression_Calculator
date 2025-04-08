package com.shpp.p2p.cs.bhnatiuk.assignment11.tree;

import java.util.HashMap;

/**
 * Represents an internal node in the tree that applies a mathematical function
 * to the result of its single child {@link TreeNode}
 *
 * <p>A {@code FunctionNode} has exactly 1 child node and executes a predefined function
 * (such as {@code sin}, {@code cos}, {@code log}, etc.) on its child's computed value</p>
 *
 * <p>Implements the {@link TreeNode} interface</p>
 */
public class FunctionNode implements TreeNode {
    /** The name of the function to be applied */
    private final String value;

    /** Child node field */
    private TreeNode next;

    /**
     * Constructs a {@code FunctionNode} with the specified function name
     *
     * @param value The name of the function to be applied
     */
    FunctionNode (String value) {
        this.value = value;
        next = null;
    }

    /**
     * Evaluates the function on the child node's computed value
     *
     * @param variables HashMap of name-value pairs for variables
     * @return The result of applying the function to the child's computed value
     * @throws NullPointerException if no child node is assigned
     */
    @Override
    public double calculate(HashMap<String, Double> variables) {
        return actions.get(value).action(next.calculate(variables));
    }

    /**
     * Checks whether this node has available slots for child node
     *
     * @return {@code true} if no child node is assigned, otherwise {@code false}
     */
    @Override
    public boolean hasNodeSpaces() {
        return next == null;
    }

    /**
     * Assigns a child node to the child node field
     *
     * @param next The {@link TreeNode} to be assigned as a child
     * @throws IllegalStateException if child node is already assigned
     */
    @Override
    public void addNext(TreeNode next) {
        if (this.next == null)
            this.next = next;
        else
            throw new IllegalStateException("FunctionNode already has a child node assigned");
    }
}
