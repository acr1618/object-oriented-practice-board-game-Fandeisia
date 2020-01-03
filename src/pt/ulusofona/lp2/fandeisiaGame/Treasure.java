package pt.ulusofona.lp2.fandeisiaGame;

public class Treasure extends Element{

    protected String metalName;
    protected int value;

    public Treasure(int id, int x, int y, int value) {
        super(id, x, y);
        this.value = value;
    }

    @Override
    public String toString() {
        return id + " | " + value + " @ " + "(" + x +", " + y + ") ";
    }

    public int getValue(){
        return value;
    }

    public String getMetalName() {
        return metalName;
    }
}
