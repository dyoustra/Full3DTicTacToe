// Danny Youstra
// AI
// TicTacToe Project
// 12/2/21

import javafx.concurrent.Task;

import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.StreamSupport;

public class AlphaBetaMinimax<Action> {

    private int plies;
//    private ThreadPoolExecutor executor;

    public AlphaBetaMinimax(int plies) {
        this.plies = plies;
//        this.executor = (ThreadPoolExecutor) Executors.newWorkStealingPool();
    }

    public void setPlies(int plies) {
        this.plies = plies;
    }

    public class Intermediate implements Comparable<Intermediate> {
        public Action action;
        public int value;

        public Intermediate(Action action, int value) {
            this.action = action;
            this.value = value;
        }

        @Override
        public int compareTo(Intermediate o) {
            return value - o.value;
//            return Integer.compare(value, o.value);
        }
    }

    public Action alphaBetaMinimax(Game.State<Action> state) {
        int max = Integer.MIN_VALUE;
        Action maxAction = null;
        final Intermediate winner = StreamSupport.stream(state.moves().spliterator(), true).reduce(new Intermediate(null, Integer.MIN_VALUE), (Intermediate lastResult, Action nextAction) -> {
            int value = this.minValue(state.next(nextAction), Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
            if (value > lastResult.value) {
                return new Intermediate(nextAction, value);
            } else {
                return lastResult;
            }
            // Max
        }, (a, b) -> a.compareTo(b) > 0 ? a : b);
        assert(winner.action != null);
        return winner.action;
//        for (Action action : state.moves()) {
//            int value = this.minValue(state.next(action), Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
//            if (value > max) {
//                max = value;
//                maxAction = action;
//            }
//        }
//        return maxAction;
    }

    public int maxValue(Game.State<Action> state, int alpha, int beta, int depth) {
        if (state.isTerminal() || depth == plies) {
            return state.evaluate();
        }
        int value = Integer.MIN_VALUE;
        for (Action action : state.moves()) {
            int value2 = this.minValue(state.next(action), alpha, beta, depth + 1);
            if (value2 > value) {
                value = value2;
                alpha = Math.max(alpha, value);
            }
            if (value >= beta) return value;
        }
        return value;
    }

    public int minValue(Game.State<Action> state, int alpha, int beta, int depth) {
        if (state.isTerminal() || depth == plies) {
            return state.evaluate();
        }
        int value = Integer.MAX_VALUE;
        for (Action action : state.moves()) {
            int value2 = this.maxValue(state.next(action), alpha, beta, depth + 1);
            if (value2 < value) {
                value = value2;
                beta = Math.min(beta, value);
            }
            if (value <= alpha) return value;
        }
        return value;
    }

}
