package pt.ulusofona.lp2.fandeisiaGame;

public class Giant extends Creature {

    public Giant (int id, int x, int y, int teamId, int cost, String orientation){
        super(id, x, y, teamId, cost, orientation);
        image = "giant.png";
        typeName = "Gigante";
        range = 3;
    }

    @Override
    public boolean move() {
        return false;
    }

    @Override
    public boolean spin() {
        return false;
    }
}
