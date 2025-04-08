package com.shpp.p2p.cs.bhnatiuk.assignment11.tree;

import com.shpp.p2p.cs.bhnatiuk.assignment11.PatternConstants;

import java.util.HashMap;

/**
 * Represents a terminal node in the tree that stores a numeric value or a variable name. A {@code ValueNode} does not
 * have child nodes and serves as a leaf in the expression tree. Is an implementation of the {@link TreeNode}
 */
public class ValueNode implements TreeNode, PatternConstants {
    /** Numeric value of the node or name of a variable */
    private final String value;

    /**
     * Constructs a {@code ValueNode} with the specified numeric value or variable name
     *
     * @param value The numeric value, or name of the variable
     */
    ValueNode(String value) {
        this.value = value;
    }

    /**
     * Evaluates the value of this node by parsing its numeric content or retrieving
     * the corresponding value from the provided variable HashMap.
     *
     * <p>Handles unary minus correctly and ensures that the referenced variable is defined.</p>
     *
     * @param variables HashMap of name-value pairs for variables
     * @return The numeric value of this node
     * @throws IllegalArgumentException If the variable is undefined or the node's value is invalid.
     */
    @Override
    public double calculate(HashMap<String, Double> variables) {
        if (value.matches(UNARY_MINUS_PATTERN + DIGIT_PATTERN)){
            return Double.parseDouble(value);
        } else if (value.matches(UNARY_MINUS_PATTERN + VARIABLE_PATTERN)){
            String key = value;
            int unaryMinusApplier = 1;
            if (key.charAt(0) == '-') {
                key = value.replace("-", "");
                unaryMinusApplier = -1;
            }

            if (!variables.containsKey(key))
                throw new IllegalArgumentException("Not defined variable: " + key);
            return variables.get(key) * unaryMinusApplier;
        } else {
            throw new IllegalArgumentException("Illegal value of the node: " + value);
        }
    }

    /**
     * Determines that this node is full (no empty child node fields)
     *
     * @return Always {@code false} since {@code ValueNode} can't have child nodes
     */
    @Override
    public boolean hasNodeSpaces() {
        return false;
    }

    /**
     * Doesn't assign any nodes and throws an exception if called, because
     * {@code ValueNode} can't hold any child nodes
     *
     * @param next The {@link TreeNode} to be assigned as a child
     * @throws UnsupportedOperationException Always thrown because {@code ValueNode} cannot hold child nodes
     */
    @Override
    public void addNext(TreeNode next) {
        throw new UnsupportedOperationException("ValueNode can't have child nodes");
    }
}
