// Danny Youstra
// AI
// TicTacToe Project
// 12/2/21

import java.util.*;
import java.util.stream.StreamSupport;

public class AlphaBetaMinimax<Action> {

    private int plies;
    private boolean multithread;

    public AlphaBetaMinimax(int plies) {
        this.plies = plies;
        this.multithread = true;
    }

    public void setMultithread(boolean multithread) {
        this.multithread = multithread;
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
        }

        @Override
        public String toString() {
            return "Intermediate{" +
                    "action=" + action +
                    ", value=" + value +
                    '}';
        }
    }

    public Action alphaBetaMinimax(Game.State<Action> state) {
        if (this.multithread) {
            final Optional<Intermediate> winnerMaybe = StreamSupport.stream(Spliterators.spliterator(state.moves().iterator(), state.numEmptySquares(), 0), true)
                    .map(a -> new Intermediate(a, this.minValue(state.next(a), Integer.MIN_VALUE, Integer.MAX_VALUE, 0)))
                    .max(Intermediate::compareTo);
            // for some reason, this needs to be optional. I end up just unwrapping the value in the line below.
            final Intermediate winner = winnerMaybe.get();
            return winner.action;
        } else {
            int max = Integer.MIN_VALUE;
            Action maxAction = null;
            for (Action action : state.moves()) {
                int value = this.minValue(state.next(action), Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
                if (value > max) {
                    max = value;
                    maxAction = action;
                }
            }
            return maxAction;
        }
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
