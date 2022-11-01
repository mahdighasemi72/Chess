package Menu;

import Player.Player;

import java.util.Scanner;
import java.util.regex.Matcher;

public class RegisterMenu {
    private static String loginPlayerUsername;
    private static Controller controller = new Controller();

    public static String getLoginPlayerUsername() {
        return loginPlayerUsername;
    }

    private static Scanner scanner = new Scanner(System.in);

    public static void Begin(){
        while (true){
            String command = scanner.nextLine().trim();
            if (ConsoleCommand.EXIT.getStringMatcher(command).matches()){
                System.out.println("ByeBye");
                break;
            } else if (ConsoleCommand.REGISTER.getStringMatcher(command).matches()) {
                Matcher matcher = ConsoleCommand.REGISTER.getStringMatcher(command);
                if (matcher.find()){
                    if (!(matcher.group(1).matches("\\w+"))){
                        System.out.println("username format is invalid");
                    }if (!(matcher.group(2).matches("\\w+"))){
                        System.out.println("password format is invalid");
                    }
                else {
                    String username = matcher.group(1);
                    String password = matcher.group(2);
                    if (controller.playerIsExist(username)) {
                        System.out.println("a user exists with this username");
                    }else {
                        controller.addPlayer(username,password);
                        System.out.println("register successful");
                    }
                }
                }
            } else if (ConsoleCommand.LOGIN.getStringMatcher(command).matches()){
                Matcher matcher = ConsoleCommand.LOGIN.getStringMatcher(command);
                if (matcher.find()){
                    String username = matcher.group(1);
                    String password = matcher.group(2);
                    if (!controller.playerIsExist(username)){
                        System.out.println("no user exists with this username");
                    }else if (!controller.passwordCheck(username,password)){
                        System.out.println("incorrect password");
                    }else {
                        loginPlayerUsername = username;
                        System.out.println("login successful");
                        MenusController.controlMenu(2);
                    }
                }
            } else if (ConsoleCommand.LIST_USERS.getStringMatcher(command).matches()) {
                System.out.println(controller.getPlayers());
            }else if (ConsoleCommand.HELPREGISTERMENU.getStringMatcher(command).matches()) {
                    System.out.println("register [username] [password]\n" +
                            "login [username] [password]\n" +
                            "remove [username] [password]\n" +
                            "list_users\n" +
                            "help\n" +
                            "exit");
            }else if (ConsoleCommand.REMOVE.getStringMatcher(command).matches()) {
                Matcher matcher = ConsoleCommand.REMOVE.getStringMatcher(command);
                if (matcher.find()) {
                    String username = matcher.group(1);
                    String password = matcher.group(2);
                    if (!controller.playerIsExist(username)) {
                        System.out.println("no user exists with this username");
                    } else if (!controller.passwordCheck(username,password)) {
                        System.out.println("incorrect password");
                    } else {
                        System.out.println("remove " + username + " successfully");

                    }
                }
                }
            else {
                System.out.println("Invalid Command");
            }
        }
    }
}
