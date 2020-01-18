package pt.ulusofona.lp2.fandeisiaGame;

public class InsufficientCoinsException extends Exception{

    public boolean teamRequiresMoreCoins (int teamId){
        return false;
    }

    public int getRequiredCoinsForTeam (int teamID){
        return 20;
    }
}
