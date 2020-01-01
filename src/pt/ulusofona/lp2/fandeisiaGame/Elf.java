package pt.ulusofona.lp2.fandeisiaGame;

public class Elf extends Creature {

    public Elf (int id, int x, int y, int teamId, int cost, String orientation){
        super(id, x, y, teamId, cost, orientation);
        image = "elf.png";
        typeName = "elf";
        range = 2;

    }

    @Override
    public void move(){
    }

    @Override
    public boolean spin() {
        return false;
    }
}
