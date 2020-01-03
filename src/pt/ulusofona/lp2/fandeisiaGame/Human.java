package pt.ulusofona.lp2.fandeisiaGame;

public class Human extends Creature {
    public Human (int id, int x, int y, int teamId, int cost, String orientation){
        super(id, x, y, teamId, cost, orientation);
        image = "Humano.png";
        typeName = "Humano";
        outroTypeName = "Humano";
        range = 2;
    }
}
