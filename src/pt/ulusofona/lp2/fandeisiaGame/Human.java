package pt.ulusofona.lp2.fandeisiaGame;

public class Human extends Creature {

    public Human (int id, int x, int y, int teamId, int cost, String orientation){
        super(id, x, y, teamId, cost, orientation);
        this.image = "Humano.png";
        this.typeName = "Humano";
        this.outroTypeName = "Humano";
        this.range = 2;
        this.cost =3;
        this.teamId = teamId;
        this.cost = cost;
        this.orientation = orientation;
        this.isEnchant = false;
        this.isFrozen = false;
        this.isFrozen4Ever = false;
        this.points = 0;
        this.frozenTime =0;
        this.gold =0;
        this.silver =0;
        this.bronze=0;
        this.nextX=0;
        this.nextY=0;
    }
}
