package pt.ulusofona.lp2.fandeisiaGame;

public class Giant extends Creature {

    public Giant (int id, int x, int y, int teamId, int cost, String orientation){
        super(id, x, y, teamId, cost, orientation);
        image = "Gigante.png";
        typeName = "Gigante";
        outroTypeName = "Gigante";
        range = 3;
    }

    @Override
    public void move() {

    }
}
