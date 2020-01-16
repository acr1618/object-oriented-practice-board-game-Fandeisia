package pt.ulusofona.lp2.fandeisiaGame;

public class InsufficientCoinsException extends Exception{

    public boolean teamRequiresMoreCoins (int teamId){
        return true;
    }

    public int getRequiredCoinsForTeam (int teamId){
        return 0;
    }






}
