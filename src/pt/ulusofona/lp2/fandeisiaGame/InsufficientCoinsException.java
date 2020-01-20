package pt.ulusofona.lp2.fandeisiaGame;

public class InsufficientCoinsException extends Exception{
    Team teamLdr;
    Team teamRes;

    public InsufficientCoinsException(String message, Team ldr, Team res){
        super(message);
        this.teamLdr = ldr;
        this.teamRes = res;
    }

    public boolean teamRequiresMoreCoins (int teamId){
        if(teamId == teamLdr.getId()){
            if(teamLdr.getCoins() < 0){
                return true;
            }
        }else{
            if(teamRes.getCoins() < 0){
                return true;
            }
        }
        return false;
    }

    public int getRequiredCoinsForTeam (int teamID){
        if(teamID == teamLdr.getId()){
            return 50 - teamLdr.getCoins();
        } else {
            return 50 - teamRes.getCoins();
        }
    }
}
