package pt.ulusofona.lp2.fandeisiaGame;

public class Silver extends Treasure {
    public Silver(int id, int x, int y, int points) {
        super(id, x, y, points);
        this.points = 2;
        metalName = "silver";
    }
}
