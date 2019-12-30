package pt.ulusofona.lp2.fandeisiaGame;

public class Treasure extends Element{

    protected String metalName;
    protected int points;

    public Treasure(int id, int x, int y, int points) {
        super(id, x, y);
        this.points = points;
    }

    @Override
    public String toString() {
        return id + " | " + points + " @ " + "(" + x +", " + y + ") ";
    }

    public int getPoints(){
        return points;
    }
}
