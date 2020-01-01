package pt.ulusofona.lp2.fandeisiaGame;

public class Human extends Creature {
    public Human (int id, int x, int y, int teamId, int cost, String orientation){
        super(id, x, y, teamId, cost, orientation);
        image = "human.png";
        typeName = "human";
        range = 2;
    }

    @Override
    public void move() {
    }

    @Override
    public boolean spin() {
        return false;
    }

}
