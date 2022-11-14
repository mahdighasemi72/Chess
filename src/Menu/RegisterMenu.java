package Menu;

import java.util.Scanner;
import java.util.regex.Matcher;
public class RegisterMenu {

    private static String loginPlayerUsername;
    private static Controller controller = new Controller();
    public static Controller getController() {
        return controller;
    }
    public static String getLoginPlayerUsername() {
        return loginPlayerUsername;
    }
    private static Scanner scanner = new Scanner(System.in);
    public static Scanner getScanner() {
        return scanner;
    }

    public static void Begin(){
        while (true){
            String command = scanner.nextLine().trim();
            if (ConsoleCommand.EXIT.getStringMatcher(command).matches()){
                System.out.println(PrintMassage.BYE_BYE);
                break;
            } else if (ConsoleCommand.REGISTER.getStringMatcher(command).matches()) {
                Matcher matcher = ConsoleCommand.REGISTER.getStringMatcher(command);
                if (matcher.find()){
                    if (!(matcher.group(1).matches("\\w+"))){
                        System.out.println(PrintMassage.INVALID_USERNAME_FORMAT);
                    }if (!(matcher.group(2).matches("\\w+"))){
                        System.out.println(PrintMassage.INVALID_PASSWORD_FORMAT);
                    }
                else {
                    String username = matcher.group(1);
                    String password = matcher.group(2);
                    if (controller.playerIsExist(username)) {
                        System.out.println(PrintMassage.USER_EXIST);
                    }else {
                        controller.addPlayer(username,password);
                        System.out.println(PrintMassage.REGISTER_SUCCESSFUL);
                    }
                }
                }
            } else if (ConsoleCommand.LOGIN.getStringMatcher(command).matches()){
                Matcher matcher = ConsoleCommand.LOGIN.getStringMatcher(command);
                if (matcher.find()){
                    String username = matcher.group(1);
                    String password = matcher.group(2);
                    if (!controller.playerIsExist(username)){
                        System.out.println(PrintMassage.NO_USER_EXIST);
                    }else if (!controller.passwordCheck(username,password)){
                        System.out.println(PrintMassage.INCORRECT_PASSWORD);
                    }else {
                        loginPlayerUsername = username;
                        System.out.println(PrintMassage.LOGIN_SUCCESSFUL);
                        MenusController.controlMenu(2);
                        break;
                    }
                }
            } else if (ConsoleCommand.LIST_USERS.getStringMatcher(command).matches()) {
                System.out.println(controller.getPlayers());
            }else if (ConsoleCommand.HELPREGISTERMENU.getStringMatcher(command).matches()) {
                    System.out.println(PrintMassage.REGISTER_MENU_HELP);
            }else if (ConsoleCommand.REMOVE.getStringMatcher(command).matches()) {
                Matcher matcher = ConsoleCommand.REMOVE.getStringMatcher(command);
                if (matcher.find()) {
                    String username = matcher.group(1);
                    String password = matcher.group(2);
                    if (!controller.playerIsExist(username)) {
                        System.out.println(PrintMassage.NO_USER_EXIST);
                    } else if (!controller.passwordCheck(username,password)) {
                        System.out.println(PrintMassage.INCORRECT_PASSWORD);
                    } else {
                        controller.removePlayer(username,password);
                        System.out.println("remove " + username + " successfully");
                    }
                }
                }
            else {
                System.out.println(PrintMassage.INVALID_COMMAND);
            }
        }
    }
}
