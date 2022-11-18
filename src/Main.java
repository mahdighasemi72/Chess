import Menu.GameMenu;
import Menu.MainMenu;
import Menu.PrintMassage;
import Menu.RegisterMenu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PrintMassage printMassage = new PrintMassage();
        Scanner scanner = new Scanner(System.in);
        int menuNum = 1;
        String input;
        String loginUsername = "";
        String secondUsername = "";
        RegisterMenu registerMenu = new RegisterMenu(printMassage);
        GameMenu gameMenu = new GameMenu(printMassage);
        while (menuNum!=0){
            input = scanner.nextLine().trim();
            switch (menuNum) {
                case 1 :
                    menuNum = registerMenu.Begin(input);
                    break;
                case 2 :
                    loginUsername = registerMenu.getLoginPlayerUsername();
                    MainMenu mainMenu = new MainMenu(loginUsername,printMassage);
                    menuNum = mainMenu.makeGame(input);
                    secondUsername = mainMenu.getSecondUsername();
                    break;
                case 3 :
                    menuNum = gameMenu.play(input, loginUsername, secondUsername);
                    break;
            }
        }
    }
}