package Menu;

import Player.Player;

import java.util.ArrayList;

public class Controller {
    private ArrayList<Player> players = new ArrayList<>();
    public Controller() {
        this.players = players;
    }
    public ArrayList<Player> getPlayers() {
        return players;
    }
    public void addPlayer(String userName, String passWord){
        Player player = new Player(userName, passWord);
        players.add(player);
    }
    public void removePlayer(String userName, String passWord){
        Player player = new Player(userName, passWord);
        players.remove(player);
    }
    public boolean playerIsExist(String userName){
        for (Player player : players) {
            if (userName.equals(player.getUserName())){
                return true;
            }
        }
        return false;
    }
    public Player getPlayerByUsername(String username) {
        for (Player player : players) {
            if(player.getUserName().equals(username))
                return player;
        }
        return null;
    }
    public boolean passwordCheck(String userName, String passWord){
        Player player = getPlayerByUsername(userName);
        if (passWord.equals(player.getPassWord())){
            return true;
        } else {
            return false;
        }
    }

}
