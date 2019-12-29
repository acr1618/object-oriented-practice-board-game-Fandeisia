package pt.ulusofona.lp2.fandeisiaGame;

public class Gold extends Treasure {
    public Gold(int id, int x, int y, int points) {
        super(id, x, y, points);
        this.points = 3;
        metalName = "gold";
    }
}
