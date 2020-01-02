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
    public void spin(){
        switch (this.orientation){
            case ("Norte"): this.orientation = "NE";
                break;
            case ("NE"): this.orientation = "Este";
                break;
            case("Este"): this.orientation = "SE";
                break;
            case ("SE"): this.orientation = "Sul";
                break;
            case ("Sul"): this.orientation = "SO";
                break;
            case ("SO"): this.orientation = "Oeste";
                break;
            case ("Oeste"): this.orientation = "NO";
                break;
            case ("NO"): this.orientation = "Norte";
        }
    }
}
