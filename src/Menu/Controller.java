package Menu;

import Player.Player;

import java.util.ArrayList;

public class Controller {
    private static ArrayList<Player> players = new ArrayList<>();
    public Controller() {
        this.players = players;
    }
    public ArrayList<Player> getPlayers() {
        return players;
    }
    public static void addPlayer(String userName, String passWord){
        Player player = new Player(userName, passWord);
        players.add(player);
    }
    public static void removePlayer(String userName, String passWord){
        Player player = new Player(userName, passWord);
        players.remove(player);
    }
    public static boolean playerIsExist(String userName){
        for (Player player : players) {
            if (userName.equals(player.getUserName())){
                return true;
            }
        }
        return false;
    }
    public static Player getPlayerByUsername(String username) {
        for (Player player : players) {
            if(player.getUserName().equals(username))
                return player;
        }
        return null;
    }
    public static boolean passwordCheck(String userName, String passWord){
        Player player = getPlayerByUsername(userName);
        if (passWord.equals(player.getPassWord())){
            return true;
        } else {
            return false;
        }
    }

}
