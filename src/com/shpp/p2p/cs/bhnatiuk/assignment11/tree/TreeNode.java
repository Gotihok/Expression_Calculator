package com.shpp.p2p.cs.bhnatiuk.assignment11.tree;

import com.shpp.p2p.cs.bhnatiuk.assignment11.IAction;
import com.shpp.p2p.cs.bhnatiuk.assignment11.Actions;
import com.shpp.p2p.cs.bhnatiuk.assignment11.IOperation;
import com.shpp.p2p.cs.bhnatiuk.assignment11.Operations;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a generic tree node for unifying different types of tree nodes
 * and treating them as a common {@code TreeNode} without specifying their individual types
 */
public interface TreeNode {
    /** A mapping of function names to their corresponding {@link IAction} implementations. */
    Map<String, IAction> actions = Map.of(
            "sin", new Actions.Sin(),
            "cos", new Actions.Cos(),
            "tan", new Actions.Tan(),
            "atan", new Actions.ATan(),
            "log10", new Actions.Log10(),
            "log2", new Actions.Log2(),
            "sqrt", new Actions.Sqrt()
    );

    /** A mapping of operator symbols to their corresponding {@link IOperation} implementations */
    Map<Character, IOperation> operations = Map.of(
            '+', new Operations.Plus(),
            '-', new Operations.Minus(),
            '*', new Operations.Multiply(),
            '/', new Operations.Divide(),
            '^', new Operations.ToPower()
    );

    /**
     * Evaluates the tree node by performing the necessary calculations
     * or returning the numeric value of a {@link ValueNode}
     *
     * @param variables HashMap of name-value pairs for variables
     * @return The computed result or the numeric value of the node {@link ValueNode}
     */
    double calculate(HashMap<String, Double> variables);

    /**
     * Checks if the node has available spaces for adding child nodes
     *
     * @return {@code true} if additional nodes can be added; otherwise, returns {@code false}
     */
    boolean hasNodeSpaces();

    /**
     * Assigns a child node to the next available child node field in a predefined order
     *
     * @param next The {@link TreeNode} to be assigned as a child
     */
    void addNext(TreeNode next);
}
