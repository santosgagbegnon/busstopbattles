package santosgagbegnon.com.busstopbattles;
import java.util.*;
public class User {
    public String username;
    public int totalWins;
    public int totalLosses;
    public ArrayList<OwnedLocation> locationsOwned;
    public User(){

    }

    public User(String username) {
        this.username = username;
        this.totalWins = 0;
        this.totalLosses = 0;
        this.locationsOwned = new ArrayList<>();
    }
    public String getUsername(){
        return this.username;
    }


}
