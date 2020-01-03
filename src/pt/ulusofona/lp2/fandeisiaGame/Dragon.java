package pt.ulusofona.lp2.fandeisiaGame;

public class Dragon extends Creature {

    public Dragon (int id, int x, int y, int teamId, int cost, String orientation){
        super(id, x, y, teamId, cost, orientation);
        image = "Dragao.png";
        typeName = "Dragao";
        outroTypeName = "Dragão";
        range = 3;
    }

    @Override
    public void move() {
    }

    @Override
    public void spin(){
        switch (this.orientation){
            case ("Norte"): this.orientation = "Nordeste";
                break;
            case ("NE"): this.orientation = "Este";
                break;
            case("Este"): this.orientation = "Sudeste";
                break;
            case ("SE"): this.orientation = "Sul";
                break;
            case ("Sul"): this.orientation = "Sudoeste";
                break;
            case ("SO"): this.orientation = "Oeste";
                break;
            case ("Oeste"): this.orientation = "Noroeste";
                break;
            case ("NO"): this.orientation = "Norte";
        }
    }
}
