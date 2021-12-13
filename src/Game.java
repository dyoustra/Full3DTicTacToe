// Danny Youstra
// AI
// TicTacToe Project #2
// 11/28/21

public class Game {

    public interface State<Action> {
        int evaluate();
        boolean isTerminal();
        State<Action> next(Action action);
        Iterable<Action> moves();
        int numEmptySquares();
    }
}
