package pt.ulusofona.lp2.fandeisiaGame;

public abstract class Treasure extends Element{

    protected String metalName;
    protected int points;

    public Treasure(int id, int x, int y, int points) {
        super(id, x, y);
        this.points = points;
    }

    @Override
    public String toString() {
        return id + " | " + points + " @ " + "(" + x +", " + y + ") " + metalName;
    }

    public int getPoints(){
        return points;
    }
}
