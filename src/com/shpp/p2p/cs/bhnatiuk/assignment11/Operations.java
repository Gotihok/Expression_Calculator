package com.shpp.p2p.cs.bhnatiuk.assignment11;

/**
 * <p>This class is made for easier calculation of operations between numbers</p>
 * <p>It contains nested class that implements IOperation interface for every operation</p>
 * <p>Theis class is used for making object of nested class for operation,
 * so that gives us the opportunity to call one method operation(),
 * when calculating the operation between 2 numbers and right operation will be executed</p>
 */
public class Operations {
    public static class Plus implements IOperation {
        @Override
        public double operation(double a, double b) {
            return a + b;
        }
    }

    public static class Minus implements IOperation {
        @Override
        public double operation(double a, double b) {
            return a - b;
        }
    }

    public static class Multiply implements IOperation {
        @Override
        public double operation(double a, double b) {
            return a * b;
        }
    }

    public static class Divide implements IOperation {
        @Override
        public double operation(double a, double b) {
            return a / b;
        }
    }

    public static class ToPower implements IOperation {
        @Override
        public double operation(double a, double b) {
            return Math.pow(a, b);
        }
    }
}
