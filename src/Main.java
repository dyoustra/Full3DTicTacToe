import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean first = true;
        boolean export = false;
        boolean statistics = false;
        // default board is empty
        Board board = new Board("");
        AlphaBetaMinimax<Action> minimax = new AlphaBetaMinimax<>(5);

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-X": // default
                    break;

                case "-O":
                    Player.ME = Player.O;
                    Player.YOU = Player.X;
                    break;

                case "-first": // default
                    break;

                case "-second":
                    first = false;
                    break;

                case "-plies":
                    minimax.setPlies(Integer.parseInt(args[++i]));
                    break;

                case "-statistics":
                    statistics = true;
                    break;

                case "-export":
                    export = true;
                    break;

                case "-singlethread":
                    minimax.setMultithread(false);

                default:
                    board = new Board(args[i]);
                    break;
            }
        }

        Scanner scanner = new Scanner(System.in);
        State current;
        if (first) current = new State(board, Player.ME);
        else current = new State(board, Player.YOU);
        current.print();
        if (statistics) System.out.println("Current Board Evaluation: " + current.evaluate());
        while (!current.isTerminal()) {
            Statistics.reset();
            System.out.println("Turn: " + current.turn());
            if (current.turn() == Player.ME) {
                final long time = System.nanoTime();
                current = current.next(minimax.alphaBetaMinimax(current));
                final long finish = System.nanoTime() - time;
                if (statistics) {
                    System.out.println("Seconds: " + finish / 1000000000.0);
                    System.out.println("States: " + Statistics.states);
                }
            }
            else {
                current = playerInput(scanner, current);
            }
            current.print();
            if (export) System.out.println(current.board());
            if (statistics) System.out.println("Current Board Evaluation: " + current.evaluate());
        }

        switch (current.evaluate()) {
            case Board.win:
                System.out.println("I won!!");
                break;
            case Board.loss:
                System.out.println("You won!!");
        }
    }

    private static State playerInput(Scanner scanner, State current) {
        Coordinate coordinate = null;
        while (coordinate == null) {
            System.out.print("X: ");
            int x = scanner.nextInt();
            System.out.print("Y: ");
            int y = scanner.nextInt();
            System.out.print("Z: ");
            int z = scanner.nextInt();
            if (Coordinate.isValid(x, y, z)) {
                if (current.board().isEmpty(x, y, z)) {
                    coordinate = Coordinate.valueOf(x, y, z);
                    current = current.next(x, y, z, Player.YOU);
                }
                else {
                    System.out.println("(" + x + ", " + y + ", " + z + ") is already occupied.");
                }
            }
            else {
                System.out.println("(" + x + ", " + y + ", " + z + ") is an invalid coordinate");
            }
        }
        return current;
    }
}