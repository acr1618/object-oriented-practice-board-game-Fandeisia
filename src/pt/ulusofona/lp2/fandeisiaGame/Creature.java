package pt.ulusofona.lp2.fandeisiaGame;

import java.util.List;
public abstract class Creature extends Element {



     int teamId;
     int cost;
     String orientation;
     String image;
     int points;
     String typeName;
     int range;
     boolean isEnchant;
     boolean isFrozen, isFrozen4Ever;
     int frozenTime;
     String itSpellName;
     int nextX, nextY;
     int gold =0, silver =0, bronze =0;
     String outroTypeName; // para usar com a atualização de imagens
     int treasureCounter = 0;
     int spellTargetCounter = 0;
     int distanceTraveled = 0;

    /*// Variáveis para guardar o mundo:
    protected int rows;
    protected int columns;
    protected List<Hole> holes;
    protected List<Treasure> treasures;
    // ------------------------------------*/

    public Creature (int id, int x, int y, int teamId, int cost, String orientation){
        super(id,x,y);
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


    public String getOutroTypeName() {
        return outroTypeName;
    }

    public void setOutroTypeName(String outroTypeName) {
        this.outroTypeName = outroTypeName;
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
        }
    }

    public void spin(){
            switch (this.orientation){
                case ("Norte"):
                    this.orientation = "Este";
                    this.image = this.outroTypeName+"-"+this.orientation+".png";
                    break;
                case ("Este"):
                    this.orientation = "Sul";
                    this.image = this.outroTypeName+"-"+this.orientation+".png";
                    break;
                case ("Sul"):
                    this.orientation = "Oeste";
                    this.image = this.outroTypeName+"-"+this.orientation+".png";
                    break;
                case ("Oeste"):
                    this.orientation = "Norte";
                    this.image = this.outroTypeName+"-"+this.orientation+".png";
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
    public void descongela(){
        this.isFrozen4Ever =false; // Custa 8 Inverte aplicação do Freezes4Ever.
    }
    public void congela() {
        this.isFrozen = true;
        this.frozenTime = 0;
    }

    public int getFrozenTime() {
        return frozenTime;
    }
    public void congela4Ever() {
        isFrozen4Ever= true;
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

    /*public void setTypeName(String typeName) {
        this.typeName = typeName;
    }*/

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


    /* Feitiços */

    public void empurraParaNorte() {
        this.y = this.y -1;
        // Custa 1 Move 1 para Norte
    }
    public void empurraParaEste(){
        this.x = this.x +1;
         // Custa 1 Move 1 para Leste
    }
    public void empurraParaSul(){
        this.y = this.y +1;
        // Custa 1 move para Sul
    }
    public void empurraParaOeste(){
        this.x = this.x -1;
         // Custa 1 Move 1 para Oeste
    }
    public void reduzAlcance(){
        this.range = 1; // Custa 2 Reduz o alcance para
    }
    public void duplicaAlcance(){
        this.range = range *2;
         // Custa 3 Aumenta alcance para o dobro
    }

    public void addGold() {
        gold = gold +1;
    }
    public void addSilver() {
        silver = silver +1;
    }
    public void addBronze() {
        bronze = bronze +1;
    }
    public int getGold() {
        return gold;
    }
    public int getSilver() {
        return silver;
    }
    public int getBronze() {
        return bronze;
    }
}
