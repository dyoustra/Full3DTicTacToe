// Danny Youstra
// AI
// TicTacToe Project
// 12/2/21

import java.util.Iterator;

public class Board {

    public static final int N = 4;
    public static final int NCubed = N * N * N;

    public static final int loss = Integer.MAX_VALUE;
    public static final int win = Integer.MIN_VALUE;

    // higher value = more separation between 1 in a row - 2 in a row - 3 in a row
    // change individually to debug
    public static final int evaluationBaseX = 8;
    public static final int evaluationBaseO = 8;

    private long x; // Boolean vector of positions containing X's
    private long o; // Boolean vector of positions containing O's


    // Constructors.

    public Board() {
        this.x = 0;
        this.o = 0;
    }

    public Board(Board board) {
        this.x = board.x;
        this.o = board.o;
    }

    public Board(String s) {
        int position = 0;
        this.x = 0;
        this.o = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case 'x':
                case 'X':
                    this.set(position++, Player.X);
                    break;

                case 'o':
                case 'O':
                    this.set(position++, Player.O);
                    break;

                case '.':
                    position++;
                    break;

                case ' ':
                case '|':
                    break;

                default:
                    throw new IllegalArgumentException("Invalid player: " + c);
            }
        }
    }


    // Empty squares.

    public boolean isEmpty(int position) {
        assert Coordinate.isValid(position);
        return !Bit.isSet(this.x | this.o, position);
    }

    public boolean isEmpty(Coordinate coordinate) {
        return this.isEmpty(coordinate.position());
    }

    public boolean isEmpty(int x, int y, int z) {
        return this.isEmpty(Coordinate.position(x, y, z));
    }

    public int numberEmptySquares() {
        return Bit.countOnes(~(this.x | this.o));
    }


    // Get value of a square on the board.

    public long get(Player player) {
        switch (player) {
            case X:
                return this.x;
            case O:
                return this.o;
            default:
                return 0;
        }
    }

    public Player get(int position) {
        assert Coordinate.isValid(position);
        if (Bit.isSet(this.x, position)) return Player.X;
        if (Bit.isSet(this.o, position)) return Player.O;
        return null;
    }

    public Player get(Coordinate coordinate) {
        return this.get(coordinate.position());
    }

    public Player get(int x, int y, int z) {
        return this.get(Coordinate.position(x, y, z));
    }

    public boolean isTerminal() {
        if (this.numberEmptySquares() == 0) return true;
        if (Line.isWinning(x)) return true;
        if (Line.isWinning(o)) return true;
        return false;
    }

    // Evaluate.

//    public int evaluate() {
//        return 1;
//    }

    public int evaluate() {
        int evaluation = 0;
        for (Line line : Line.lines) {
            boolean intersectsX = line.intersects(this.x);
            boolean intersectsO = line.intersects(this.o);
            if (intersectsX && !intersectsO) {
                int xOnLine = Bit.countOnes(line.positions() & this.x); // how many x are on the open line
                if (xOnLine == 4) return (Player.ME == Player.X) ? win : loss;
                int value = evaluationBaseX << xOnLine; // shift the binary over to the left by the num of x on the line
                evaluation += (Player.ME == Player.X) ? value : -value;
            } else if (intersectsO && !intersectsX) {
                int oOnLine = Bit.countOnes(line.positions() & this.o); // how many o are on the open line
                if (oOnLine == 4) return (Player.ME == Player.O) ? win : loss;
                int value = evaluationBaseO << oOnLine; // shift the binary over to the left by the num of o on the line
                evaluation += (Player.ME == Player.O) ? value : -value;
            }
        }
        return evaluation;
    }

//    public int evaluate() {
//        int evaluation = 0;
//        for (Line line : Line.lines) {
//            boolean intersectsX = line.intersects(this.x);
//            boolean intersectsO = line.intersects(this.o);
//            if (intersectsX && !intersectsO) {
//                int xOnLine = Bit.countOnes(line.positions() & this.x); // how many x are on the open line
//                if (xOnLine == 4) return (Player.ME == Player.X) ? win : loss;
//                int value = (int) Math.pow(evaluationBaseME, xOnLine);
//                evaluation += (Player.ME == Player.X) ? value : -value;
//            } else if (intersectsO && !intersectsX) {
//                int oOnLine = Bit.countOnes(line.positions() & this.o); // how many o are on the open line
//                if (oOnLine == 4) return (Player.ME == Player.O) ? win : loss;
//                int value = (int) Math.pow(evaluationBaseYOU, oOnLine);
//                evaluation += (Player.ME == Player.O) ? value : -value;
//            }
//        }
//        return evaluation;
//    }

    // Set value of a square on the board.

    public void set(int position, Player player) {
        assert (isEmpty(position));
        switch (player) {
            case X:
                this.x = Bit.set(this.x, position);
                break;

            case O:
                this.o = Bit.set(this.o, position);
                break;

            default:
                break;
        }
    }

    public void set(Coordinate coordinate, Player player) {
        set(coordinate.position(), player);
    }

    public void set(int x, int y, int z, Player player) {
        set(Coordinate.position(x, y, z), player);
    }

    public void clear(int position) {
        this.x = Bit.clear(this.x, position);
        this.o = Bit.clear(this.o, position);
    }

    public void clear(Coordinate coordinate) {
        clear(coordinate.position());
    }

    public void clear(int x, int y, int z) {
        clear(Coordinate.valueOf(x, y, z));
    }


    // Equality.

    public boolean equals(Board other) {
        return this.o == other.o && this.x == other.x;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Board && this.equals((Board) other);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.x) * Long.hashCode(this.o);
    }


    // Image & printing functions.

    @Override
    public String toString() {
        String result = "";
        String separator;
        for (int position = 0; position < 64; position++) {
            if (position % 16 == 0) {
                separator = " | ";
            } else if (position % 4 == 0) {
                separator = " ";
            } else {
                separator = "";
            }
            result += separator;
            Player player = this.get(position);
            result += (player != null) ? player : ".";
        }
        return result;
    }


    public static Board valueOf(String s) {
        return new Board(s);
    }


    public void print() {
        for (int y = N - 1; y >= 0; y--) {
            for (int z = 0; z < N; z++) {
                for (int x = 0; x < N; x++) {
                    Player player = this.get(x, y, z);
                    if (player != null) {
                        System.out.print(player);
                    } else {
                        System.out.print(".");
                    }
                }
                System.out.print("    ");
            }
            System.out.println();
        }
    }

    // Generate new board for a given move.

    public Board next(Coordinate move, Player player) {
        assert this.isEmpty(move);
        Board result = new Board(this);
        result.set(move, player);
        return result;
    }

    public Board next(int position, Player player) {
        return next(Coordinate.valueOf(position), player);
        // return next(new Coordinate(position), player);
    }

    public Board next(int x, int y, int z, Player player) {
        return next(Coordinate.valueOf(x, y, z), player);
        // return next (new Coordinate(x, y, z), player);
    }


    // Iterators.

    private class EmptySquareIterator implements Iterator<Coordinate> {

        private final Iterator<Integer> iterator;

        public EmptySquareIterator() {
            this.iterator = Bit.iterator(~(Board.this.x | Board.this.o));
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public Coordinate next() {
            return Coordinate.valueOf(this.iterator.next());
            // return new Coordinate(this.iterator.next());
        }
    }

    public Iterator<Coordinate> emptySquareIterator() {
        return new EmptySquareIterator();
    }

    public Iterable<Coordinate> emptySquares() {
        return EmptySquareIterator::new;
    }

}