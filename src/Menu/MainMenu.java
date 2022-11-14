package Menu;

import Player.Player;

import java.util.Scanner;
import java.util.regex.Matcher;

public class MainMenu {
    private static Scanner scanner = RegisterMenu.getScanner();
    private static Controller controller = RegisterMenu.getController();
    public static void makeGame(){
        while (true){
            String command = scanner.nextLine().trim();
            if (ConsoleCommand.NEWGAME.getStringMatcher(command).matches()){
                Matcher matcher = ConsoleCommand.NEWGAME.getStringMatcher(command);
                if (matcher.find()){
                    String secondUsername = matcher.group(1);
                    String limit = matcher.group(2);
                    String loginUsername = RegisterMenu.getLoginPlayerUsername();
                    if (!controller.playerIsExist(secondUsername)){
                        System.out.println(PrintMassage.NO_USER_EXIST);
                    } else if (secondUsername.equals(loginUsername)) {
                        System.out.println(PrintMassage.CHOOSE_ANOTHER_PLAYER);
                    } else if (Long.parseLong(limit)<0) {
                        System.out.println(PrintMassage.LIMIT_PERIOD);
                    } else {
                        System.out.printf(PrintMassage.START_NEW_GAME , loginUsername , secondUsername , limit);
                        MenusController.controlMenu(3);
                        break;
                    }
                }
            } else if (ConsoleCommand.HELPMAINMENU.getStringMatcher(command).matches()) {
                System.out.println(PrintMassage.MAIN_MENU_HELP);
            }else if (ConsoleCommand.LIST_USERS.getStringMatcher(command).matches()) {
                System.out.println(controller.getPlayers());
            } else if (ConsoleCommand.LOGOUT.getStringMatcher(command).matches()) {
                System.out.println(PrintMassage.LOGOUT_SUCCESSFUL);
                MenusController.controlMenu(1);
                break;
            } else if (ConsoleCommand.SCOREBOARD.getStringMatcher(command).matches()) {
                for (Player player : controller.getPlayers()) {
                    System.out.println(player);
                }
            } else
                System.out.println(PrintMassage.INVALID_COMMAND);
        }
    }
}
