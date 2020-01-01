package pt.ulusofona.lp2.fandeisiaGame;

public class Dragon extends Creature {

    public Dragon (int id, int x, int y, int teamId, int cost, String orientation){
        super(id, x, y, teamId, cost, orientation);
        image = "dragon.png";
        typeName = "dragon";
        range = 3;
    }

    @Override
    public void move() {
            }

    @Override
    public boolean spin() {
        return false;
    }
}
