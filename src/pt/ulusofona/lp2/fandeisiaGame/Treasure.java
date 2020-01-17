package pt.ulusofona.lp2.fandeisiaGame;

public class Treasure extends Element{

    private int value;

    public Treasure(int id, int x, int y, int value) {
        super(id, x, y);
        this.value = value;
    }

    public int getValue(){
        return value;
    }
    @Override
    public String toString() {
        return id + " | " + value + " @ " + "(" + x +", " + y + ") ";
    }
}
