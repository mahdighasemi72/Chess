package Menu;

import java.util.Scanner;
import java.util.regex.Matcher;
public class RegisterMenu {
    private String loginPlayerUsername;
    private PrintMassage printMassage;
    private Controller controller = Controller.getInstance() ;
    public String getLoginPlayerUsername() {
        return loginPlayerUsername;
    }
    public RegisterMenu(PrintMassage printMassage) {
        this.printMassage = printMassage;
    }

    public int Begin(String command){
        int menuNum = 1 ;
        if (ConsoleCommand.EXIT.getStringMatcher(command).matches()){
            System.out.println(printMassage.BYE_BYE);
            menuNum = 0;
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
                    menuNum = 2;
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
        return menuNum;
    }
}
