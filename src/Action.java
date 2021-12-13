public class Action {

    public final Player player;
    public final Coordinate move;

    public Action(Coordinate move, Player player) {
        this.move = move;
        this.player = player;
    }
}
