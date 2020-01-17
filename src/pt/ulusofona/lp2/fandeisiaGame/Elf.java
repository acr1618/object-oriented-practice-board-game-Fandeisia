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
            case ("Norte"):
                this.orientation = "Nordeste";
                this.image = this.outroTypeName+"-"+this.orientation+".png";
                break;
            case ("Nordeste"):
                this.orientation = "Este";
                this.image = this.outroTypeName+"-"+this.orientation+".png";
                break;
            case("Este"):
                this.orientation = "Sudeste";
                this.image = this.outroTypeName+"-"+this.orientation+".png";
                break;
            case ("Sudeste"):
                this.orientation = "Sul";
                this.image = this.outroTypeName+"-"+this.orientation+".png";
                break;
            case ("Sul"):
                this.orientation = "Sudoeste";
                this.image = this.outroTypeName+"-"+this.orientation+".png";
                break;
            case ("Sudoeste"):
                this.orientation = "Oeste";
                this.image = this.outroTypeName+"-"+this.orientation+".png";
                break;
            case ("Oeste"):
                this.orientation = "Noroeste";
                this.image = this.outroTypeName+"-"+this.orientation+".png";
                break;
            case ("Noroeste"):
                this.orientation = "Norte";
                this.image = this.outroTypeName+"-"+this.orientation+".png";
                break;
        }
    }
    public void move(){
        switch (this.orientation){
            case ("Norte"): {
                this.y = y-this.range;
                break;
            }
            case ("Este"): {
                this.x = x+this.range;
                break;
            }
            case ("Sul"): {
                this.y = y+this.range;
                break;
            }
            case ("Oeste"): {
                this.x = x-this.range;
                break;
            }
            case ("Nordeste"): {
                this.y = y-this.range;
                this.x = x+this.range;
                break;
            }
            case ("Sudeste"): {
                this.y = y+this.range;
                this.x = x+this.range;
                break;
            }
            case ("Sudoeste"): {
                this.y = y+this.range;
                this.x = x-this.range;
                break;
            }
            case ("Noroeste"): {
                this.y = y-this.range;
                this.x = x-this.range;
                break;
            }
        }
    }
}
