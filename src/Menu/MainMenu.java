package Menu;

import Player.Player;

import java.util.Scanner;
import java.util.regex.Matcher;

public class MainMenu {
    private static Scanner scanner = new Scanner(System.in);
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
                        System.out.println("no user exists with this username");
                    } else if (secondUsername.equals(loginUsername)) {
                        System.out.println("you must choose another player to start a game");
                    } else if (Long.parseLong(limit)<0) {
                        System.out.println("number should be positive to have a limit or 0 for no limit");
                    } else {
                        //TODO
                        System.out.println("new game started successfully between " + loginUsername +
                                " and " + secondUsername + " with limit " + limit);
                    }
                }
            } else if (ConsoleCommand.HELPMAINMENU.getStringMatcher(command).matches()) {
                System.out.println("new_game [username] [limit]\n" +
                        "scoreboard\n" +
                        "list_users\n" +
                        "help\n" +
                        "logout");
            }else if (ConsoleCommand.LIST_USERS.getStringMatcher(command).matches()) {
                System.out.println(controller.getPlayers());
            } else if (ConsoleCommand.LOGOUT.getStringMatcher(command).matches()) {
                System.out.println("logout successful");
                MenusController.controlMenu(2);
            } else
                System.out.println("Invalid Command");
        }
    }
}
