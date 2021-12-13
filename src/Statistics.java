public class Statistics {

    public static int states;
    public static int branchingNum;

    public static void reset() {
        states = 0;
        branchingNum = 0;
    }

    public static void print() {
        System.out.println("States: " + states);
        System.out.println("Effective Branching Factor: " + (double) states / (double) branchingNum);
    }
}
