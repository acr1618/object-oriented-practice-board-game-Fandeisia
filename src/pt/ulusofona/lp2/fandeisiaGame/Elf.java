package pt.ulusofona.lp2.fandeisiaGame;

public class Elf extends Creature {

    public Elf (int id, int x, int y, int teamId, int cost, String orientation){
        super(id, x, y, teamId, cost, orientation);
        image = "elf.png";
        typeName = "Elfo";

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
