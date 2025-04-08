package com.shpp.p2p.cs.bhnatiuk.assignment11;

/**
 * <p>This class is made for easier calculation of operations on numbers</p>
 * <p>It contains nested classes that implement IAction interface for every operation</p>
 * <p>Theis class is used for making object of nested class for operation,
 * so that gives us the opportunity to call one method action(),
 * when calculating the action between 2 numbers and right operation will be executed</p>
 */
public class Actions {
    public static class Sin implements IAction {
        @Override
        public double action(double a) {
            return Math.sin(a);
        }
    }

    public static class Cos implements IAction {
        @Override
        public double action(double a) {
            return Math.cos(a);
        }
    }

    public static class Tan implements IAction {
        @Override
        public double action(double a) {
            return Math.tan(a);
        }
    }

    public static class ATan implements IAction {
        @Override
        public double action(double a) {
            return Math.atan(a);
        }
    }

    public static class Log10 implements IAction {
        @Override
        public double action(double a) {
            return Math.log10(a);
        }
    }

    public static class Log2 implements IAction {
        @Override
        public double action(double a) {
            return Math.log(a) / Math.log(2);
        }
    }

    public static class Sqrt implements IAction {
        @Override
        public double action(double a) {
            return Math.sqrt(a);
        }
    }
}
