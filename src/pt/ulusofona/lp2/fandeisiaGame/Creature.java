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
    protected boolean isFrozen;
    protected int frozenTime;
    protected boolean isFrozen4Ever;
    protected String itSpellName;
    protected int nextX;
    protected int nextY;

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
        this.isFrozen = false;
        this.isFrozen4Ever = false;
        this.points = 0;
    }

    public String getImagePNG(){
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return id + " | " + typeName + " | " + teamId + " | " + points + " @ " + "(" + x +", " + y + ") " + orientation;
    }

    public String getTypeName() {
        return typeName;
    }

    public abstract void move();
        //return true;
     //} Talvez deixe de ser abstrato e passe a ter a implementação básica (anão) e sobrescrita nas outras classes de criatura. todo

    public void spin(){
            switch (this.orientation){
                case ("Norte"): this.orientation = "Este";
                    break;
                case ("Este"): this.orientation = "Sul";
                    break;
                case ("Sul"): this.orientation = "Oeste";
                    break;
                case ("Oeste"): this.orientation = "Norte";
                    break;
            }
    }

    public int getTeamId() {
        return teamId;
    }

    public int getCost(){ return cost; }

    public String getOrientation(){
        return orientation;
    }

    public void setOrientation(String orientation){
        this.orientation = orientation;
    }

    public int getPoints(){
        return points;
    }

    public void addPoints(int points) {
        this.points = this.points + points;
    }

    //public void lookAtTheWorld(int rows, int columns, List<Hole> holes, List<Treasure> treasures, List<Creature> creatures) {} -----> NOT YET!

    public int getRange() {
        return range;
    }

    public boolean isEnchant() {
        return isEnchant;
    }

    public void setEnchant(boolean enchant) {
        isEnchant = enchant;
    }

    public String getItSpellName() {
        return itSpellName;
    }

    public void setItSpellName(String itSpellName) {
        this.itSpellName = itSpellName;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public int getFrozenTime() {
        return frozenTime;
    }

    public void setFrozenTime(int frozenTime) {
        this.frozenTime = frozenTime;
    }

    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }

    public boolean isFrozen4Ever() {
        return isFrozen4Ever;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getNextX() {
        return nextX;
    }

    public void setNextX(int nextX) {
        this.nextX = nextX;
    }

    public int getNextY() {
        return nextY;
    }

    public void setNextY(int nextY) {
        this.nextY = nextY;
    }


    /* Feitiços! */

    public void pushNorth() {
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
    public void doubleRange(){
        this.range = range *2;
         // Custa 3 Aumenta alcance para o dobro
    }
    public void unfreezes(){
        this.isFrozen4Ever =false; // Custa 8 Inverte aplicação do Freezes4Ever.
    }

    public void freezes() {
        this.isFrozen = true;
        this.frozenTime = 0;
    }

    public void freezes4Ever() {
        isFrozen4Ever= true;
    }
}
