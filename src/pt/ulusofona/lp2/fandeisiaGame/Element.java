package pt.ulusofona.lp2.fandeisiaGame;

public abstract class Element {

    protected int id;
    protected int x;
    protected int y;

    public Element(int id, int x, int y){
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId(){
        return id;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }


}
