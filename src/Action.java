public class Action {

    public final Player player;
    public final Coordinate move;

    public Action(Coordinate move, Player player) {
        this.move = move;
        this.player = player;
    }

//    public Action(Coordinate move, boolean xMove) {
//        this.move = move;
//        this.player = (xMove) ? Player.X : Player.O;
//    }
}
