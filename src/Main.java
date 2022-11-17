import Menu.GameMenu;
import Menu.MainMenu;
import Menu.RegisterMenu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int menuNum = 1;
        Scanner scanner = new Scanner(System.in);
        String input;
        RegisterMenu registerMenu = new RegisterMenu();
        MainMenu mainMenu = new MainMenu();
        GameMenu gameMenu = new GameMenu();
        String loginUsername;
        while (menuNum!=0){
            input = scanner.nextLine().trim();
            if (menuNum == 1) {
                menuNum = registerMenu.Begin(input);
            } else if (menuNum == 2) {
                loginUsername = registerMenu.getLoginPlayerUsername();
                menuNum = mainMenu.makeGame(input, loginUsername);
            } else if (menuNum == 3) {
                menuNum = gameMenu.play(input);
            }
        }
    }
}