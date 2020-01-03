package pt.ulusofona.lp2.fandeisiaGame;

public class Elf extends Creature {

    public Elf (int id, int x, int y, int teamId, int cost, String orientation){
        super(id, x, y, teamId, cost, orientation);
        this.image = "Elfo.png";
        this.typeName = "Elfo";
        this.outroTypeName = "Elfo";
        this.range = 2;
        this.cost = 5;
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

    @Override
    public void spin(){
        switch (this.orientation){
            case ("Norte"): this.orientation = "Nordeste";
                break;
            case ("Nordeste"): this.orientation = "Este";
                break;
            case("Este"): this.orientation = "Sudeste";
                break;
            case ("Sudeste"): this.orientation = "Sul";
                break;
            case ("Sul"): this.orientation = "Sudoeste";
                break;
            case ("Sudoeste"): this.orientation = "Oeste";
                break;
            case ("Oeste"): this.orientation = "Noroeste";
                break;
            case ("Noroeste"): this.orientation = "Norte";
        }
    }
}
