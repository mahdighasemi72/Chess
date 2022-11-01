package Player;

import java.util.HashMap;

public class Player {
    private String userName , passWord;
    private long score ;
    private int wins , draws , losses;

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public long getScore() {
        return score;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public Player(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        this.score = 0;
        this.draws = 0;
        this.losses = 0;
        this.wins = 0;
    }

    @Override
    public String toString() {
            return this.userName+" "+this.score+" "+this.wins+" "+this.draws+" "+this.losses;
        }

}
