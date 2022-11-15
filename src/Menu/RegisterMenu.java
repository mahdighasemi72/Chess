package Menu;

import java.util.Scanner;
import java.util.regex.Matcher;
public class RegisterMenu {
    private String loginPlayerUsername;
    public static Controller controller ;
    private static Scanner scanner;
    private PrintMassage printMassage;
    private MenusController menusController;
    public static Controller getController() {
        return controller;
    }
    public String getLoginPlayerUsername() {
        return loginPlayerUsername;
    }
    public static Scanner getScanner() {
        return scanner;
    }

    public RegisterMenu() {
        this.controller = new Controller();
        scanner = new Scanner(System.in);
    }

    public void Begin(){
        while (true){
            String command = scanner.nextLine().trim();
            if (ConsoleCommand.EXIT.getStringMatcher(command).matches()){
                System.out.println(printMassage.MAIN_MENU_HELP);
                break;
            } else if (ConsoleCommand.REGISTER.getStringMatcher(command).matches()) {
                Matcher matcher = ConsoleCommand.REGISTER.getStringMatcher(command);
                if (matcher.find()){
                    if (!(matcher.group(1).matches("\\w+"))){
                        System.out.println(printMassage.INVALID_USERNAME_FORMAT);
                    }if (!(matcher.group(2).matches("\\w+"))){
                        System.out.println(printMassage.INVALID_PASSWORD_FORMAT);
                    }
                else {
                    String username = matcher.group(1);
                    String password = matcher.group(2);
                    if (controller.playerIsExist(username)) {
                        System.out.println(printMassage.USER_EXIST);
                    }else {
                        controller.addPlayer(username,password);
                        System.out.println(printMassage.REGISTER_SUCCESSFUL);
                    }
                }
                }
            } else if (ConsoleCommand.LOGIN.getStringMatcher(command).matches()){
                Matcher matcher = ConsoleCommand.LOGIN.getStringMatcher(command);
                if (matcher.find()){
                    String username = matcher.group(1);
                    String password = matcher.group(2);
                    if (!controller.playerIsExist(username)){
                        System.out.println(printMassage.NO_USER_EXIST);
                    }else if (!controller.passwordCheck(username,password)){
                        System.out.println(printMassage.INCORRECT_PASSWORD);
                    }else {
                        loginPlayerUsername = username;
                        System.out.println(printMassage.LOGIN_SUCCESSFUL);
                        menusController.controlMenu(2);
                        break;
                    }
                }
            } else if (ConsoleCommand.LIST_USERS.getStringMatcher(command).matches()) {
                System.out.println(controller.getPlayers());
            }else if (ConsoleCommand.HELPREGISTERMENU.getStringMatcher(command).matches()) {
                    System.out.println(printMassage.REGISTER_MENU_HELP);
            }else if (ConsoleCommand.REMOVE.getStringMatcher(command).matches()) {
                Matcher matcher = ConsoleCommand.REMOVE.getStringMatcher(command);
                if (matcher.find()) {
                    String username = matcher.group(1);
                    String password = matcher.group(2);
                    if (!controller.playerIsExist(username)) {
                        System.out.println(printMassage.NO_USER_EXIST);
                    } else if (!controller.passwordCheck(username,password)) {
                        System.out.println(printMassage.INCORRECT_PASSWORD);
                    } else {
                        controller.removePlayer(username,password);
                        System.out.printf(printMassage.REMOVE,username);
                    }
                }
                }
            else {
                System.out.println(printMassage.INVALID_COMMAND);
            }
        }
    }
}
