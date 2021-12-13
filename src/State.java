import java.util.Iterator;

public class State implements Game.State<Action> {

    private final Board board;
    private final Player turn;

    public State(Board board, Player turn) {
        this.board = board;
        this.turn = turn;
        Statistics.states++;
    }

    public Player turn() {
        return this.turn;
    }

    public void print() {
        this.board.print();
        System.out.println("----------------------------");
    }

    public Board board() {
        return this.board;
    }

    @Override
    public int hashCode() {
        return this.board.hashCode();
    }

    @Override
    public int evaluate() {
        return this.board.evaluate();
    }

    @Override
    public boolean isTerminal() {
        return this.board.isTerminal();
    }

    @Override
    public State next(Action action) {
        return new State(this.board.next(action.move, action.player), this.turn.other());
    }

    public State next(int row, int col, int plane, Player player) {
        return this.next(new Action(Coordinate.valueOf(row, col, plane), player));
    }

    @Override
    public Iterable<Action> moves() {
        return MoveIterator::new;
    }

    @Override
    public int numEmptySquares() {
        return this.board.numberEmptySquares();
    }

    private class MoveIterator implements Iterator<Action> {

        private final Iterator<Coordinate> iterator;

        public MoveIterator() {
            this.iterator = State.this.board.emptySquareIterator();
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public Action next() {
            return new Action(this.iterator.next(), State.this.turn);
        }
    }

}
