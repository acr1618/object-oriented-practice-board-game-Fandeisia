package pt.ulusofona.lp2.fandeisiaGame;

public class Hole extends Element {

    public Hole (int id, int x, int y){
        super(id, x, y);
    }

    @Override
    public String toString() {
        return id + " | " + " @ " + "(" + x +", " + y + ") " + "Hole";
    }
}
