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
        //this.itSpellName = null;
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

    public boolean spin(){
        return true;
    } // Talvez deixe de ser abstrata e passe a ter implementação básica (90º) e será sobrescrita em Dragão e Elfo. todo

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

    public void setRange(int range) {
        this.range = range;
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

    public void setFrozen4Ever(boolean frozen4Ever) {
        isFrozen4Ever = frozen4Ever;
    }

    public void congela4Ever(boolean isCongelada4Ever) {
        this.isFrozen4Ever = isCongelada4Ever;
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
    public boolean unfreezes(){
        return true; // Custa 8 Inverte aplicação do Freezes4Ever.
    }

}
