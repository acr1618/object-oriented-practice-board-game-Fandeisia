package pt.ulusofona.lp2.fandeisiaGame;

public class InsufficientCoinsException extends Exception{
    Team teamLdr;
    Team teamRes;

    public InsufficientCoinsException(String message, Team ldr, Team res){
        super(message);
        teamLdr = ldr;
        teamRes = res;
    }

    public boolean teamRequiresMoreCoins (int teamId){

        return false;
    }

    public int getRequiredCoinsForTeam (int teamID){
        return 20;
    }
}
