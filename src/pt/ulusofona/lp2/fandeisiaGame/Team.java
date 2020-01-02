package pt.ulusofona.lp2.fandeisiaGame;

import java.util.List;

public class Team{
    protected int id;
    protected String name;
    protected int points;
    protected int coins;


    public Team(int id, String name){
        this.id = id;
        this.name = name;
        this.coins = 50;
        this.points = 0;
    }
    public boolean isActive(){
        return true;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public int getCoins(){
        return this.coins;
    }

    public void addCoins(int coins){
        this.coins = this.coins + coins;
    }

    public void removeCoins(int cost){
        coins = coins - cost;
    }

    public int getPoints(){
        return points;
    }

    public void addPoints(int points){
        this.points = this.points + points;
    }

    public void removePoints(int points){
        this.points = points;
    }

    @Override
    public String toString() {
        return null;
    }

    public boolean checkBalanceToSpell(int spellCost) {
        return this.coins >= spellCost;
    }
}
