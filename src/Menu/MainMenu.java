package Menu;

import Player.Player;

import java.util.Scanner;
import java.util.regex.Matcher;

public class MainMenu {
    private Controller controller = Controller.getInstance() ;
    private PrintMassage printMassage = new PrintMassage();
    public int makeGame(String command,String loginUsername){
        int menuNum = 2;
        if (ConsoleCommand.NEWGAME.getStringMatcher(command).matches()){
            Matcher matcher = ConsoleCommand.NEWGAME.getStringMatcher(command);
            if (matcher.find()){
                String secondUsername = matcher.group(1);
                String limit = matcher.group(2);
                if (!controller.playerIsExist(secondUsername)){
                    System.out.println(printMassage.NO_USER_EXIST);
                } else if (secondUsername.equals(loginUsername)) {
                    System.out.println(printMassage.CHOOSE_ANOTHER_PLAYER);
                } else if (Long.parseLong(limit)<0) {
                    System.out.println(printMassage.LIMIT_PERIOD);
                } else {
                    System.out.printf(printMassage.START_NEW_GAME , loginUsername , secondUsername , limit);
                    menuNum = 3;
                }
            }
        } else if (ConsoleCommand.HELPMAINMENU.getStringMatcher(command).matches()) {
            System.out.println(printMassage.MAIN_MENU_HELP);
        }else if (ConsoleCommand.LIST_USERS.getStringMatcher(command).matches()) {
            System.out.println(controller.getPlayers());
        } else if (ConsoleCommand.LOGOUT.getStringMatcher(command).matches()) {
            System.out.println(printMassage.LOGOUT_SUCCESSFUL);
            menuNum = 1 ;
        } else if (ConsoleCommand.SCOREBOARD.getStringMatcher(command).matches()) {
            for (Player player : controller.getPlayers()) {
                System.out.println(player);
            }
        } else
            System.out.println(printMassage.INVALID_COMMAND);
        return menuNum;
    }
}
