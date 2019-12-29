package pt.ulusofona.lp2.fandeisiaGame;

public class Bronze extends Treasure {
    public Bronze(int id, int x, int y, int points) {
        super(id, x, y, points);
        this.points = 1;
        metalName = "bronze";
    }
}
