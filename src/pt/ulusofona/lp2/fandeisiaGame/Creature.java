package pt.ulusofona.lp2.fandeisiaGame;

import java.util.List;

public abstract class Creature extends Element {

    protected int teamId;
    protected int cost;
    protected String orientation;
    protected String image;
    protected int points;
    protected String typeName;
    protected int range;
    protected boolean isEnchant;
    protected boolean isFreezed;
    protected boolean isFreezed4Ever;


    // Variáveis para guardar o mundo:
    protected int rows;
    protected int columns;
    protected List<Hole> holes;
    protected List<Treasure> treasures;
    // ------------------------------------

    public Creature (int id, int x, int y, int teamId, int cost, String orientation){
        super(id,x,y);
        this.teamId = teamId;
        this.cost = cost;
        this.orientation = orientation;
        this.isEnchant = false;
        this.isFreezed = false;
        this.isFreezed4Ever = false;
    }

    public String getImagePNG(){
        return image;
    }

    @Override
    public String toString() {
        return id + " | " + typeName + " | " + teamId + " | " + points + " @ " + "(" + x +", " + y + ") " + orientation;
    }

    public abstract boolean move(); // Talvez deixe de ser abstrato e passe a ter a implementação básica (anão) e sobrescrita nas outras classes de criatura. todo

    public abstract boolean spin(); // Talvez deixe de ser abstrata e passe a ter implementação básica (90º) e será sobrescrita em Dragão e Elfo. todo

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

    //public void lookAtTheWorld(int rows, int columns, List<Hole> holes, List<Treasure> treasures, List<Creature> creatures) {} -----> NOT YET!

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public boolean isEnchant() {
        return isEnchant;
    }

    public void setEnchant(boolean enchant) {
        isEnchant = enchant;
    }

    public boolean isFrozen() {
        return isFreezed;
    }

    public void freezes(boolean isFrozen) { // Talvez isso é o próprio feitiço - PROVAVELMENTE!!!
        this.isFreezed = isFrozen;
    }

    public boolean isFreezed4Ever() {
        return isFreezed4Ever;
    }

    public void congela4Ever(boolean isCongelada4Ever) {
        this.isFreezed4Ever = isCongelada4Ever;
    }

    // Feitiços:

    public void pushNorth(){
        this.y = this.y -1;
         // Custa 1 Move 1 para Norte
    }
    public void pushEast(){
        this.x = this.x +1;
         // Custa 1 Move 1 para Leste
    }
    public void pushSouth(){
        this.y = this.y +1;
        // Custa 1 move para Sul
    }
    public void pushWest(){
        this.x = this.x -1;
         // Custa 1 Move 1 para Oeste
    }
    public void reducesRange(){
        this.range = 1; // Custa 2 Reduz o alcance para
    }
    public void doubleRange(int range){
        this.range = range *2;
         // Custa 3 Aumenta alcance para o dobro
    }
    public boolean freezes(){
        return true; // Custa 3 Não move neste turno
    }
    public boolean freezes4Ever(){
        return true; // Custa 10 Não move até o fim do jogo
    }
    public boolean unfreezes(){
        return true; // Custa 8 Inverte aplicação do Freezes4Ever.
    }

}
