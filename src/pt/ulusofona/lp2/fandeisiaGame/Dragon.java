package pt.ulusofona.lp2.fandeisiaGame;

public class Dragon extends Creature {

    public Dragon (int id, int x, int y, int teamId, int cost, String orientation){
        super(id, x, y, teamId, cost, orientation);
        this.image = "Dragao.png";
        this.typeName = "Dragão";
        this.outroTypeName = "Dragao";
        this.range = 3;
        this.cost =9;
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
}
