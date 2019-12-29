package pt.ulusofona.lp2.fandeisiaGame;

public abstract class Creature extends Element {

    protected int teamId;
    protected int cost;
    protected String orientation;
    protected String image;
    protected int points;
    protected String typeName;

    public Creature (int id, int x, int y, int teamId, int cost, String orientation){
        super(id,x,y);
        this.teamId = teamId;
        this.cost = cost;
        this.orientation = orientation;
    }

    public String getImagePNG(){
        return image;
    }

    @Override
    public String toString() {
        return id + " | " + typeName + " | " + teamId + " | " + points + " @ " + "(" + x +", " + y + ") " + orientation;
    }

    public int getTeamId() {
        return teamId;
    }

    public int getCost(){ return cost; }

    public String getOrientation(){
        return orientation;
    }

    public int getPoints(){
        return points;
    }

    public abstract boolean move();

    public abstract boolean spin();


}
