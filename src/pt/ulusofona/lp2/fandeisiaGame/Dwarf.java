package pt.ulusofona.lp2.fandeisiaGame;

public class Dwarf extends Creature {

    public Dwarf (int id, int x, int y, int teamId, int cost, String orientation){
        super(id, x, y, teamId, cost, orientation);
        image = "dwarf.png";
        typeName = "Dwarf";
        range = 1;
    }

    @Override
    public void move() {
        switch (this.orientation){
            case ("Norte"): {
                this.y = y-1;
                break;
            }
            case ("Este"): {
                this.x = x+1;
                break;
            }

            case ("Sul"): {
                this.y = y+1;
                break;
            }

            case ("Oeste"): {
                this.x = x-1;
                break;
            }


        }

    }

    @Override
    public boolean spin() {
        return false;
    }
}



