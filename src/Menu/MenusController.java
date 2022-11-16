package Menu;

import Player.Player;

import java.util.ArrayList;

public class MenusController {
    private int menuNum;
    private RegisterMenu registerMenu;
    private MainMenu mainMenu;
    private GameMenu gameMenu;

    public void controlMenu(int menuNum) {
        if (menuNum == 1){
            registerMenu.Begin();
        } else if (menuNum==2) {
            String loginUsername = registerMenu.getLoginPlayerUsername();
            mainMenu.makeGame(loginUsername);
        } else if (menuNum == 3) {
            gameMenu.play();
        }
    }

    public int getMenuNum() {
        return menuNum;
    }

    public void setMenuNum(int menuNum) {
        this.menuNum = menuNum;
    }

    public MenusController() {
        this.registerMenu = new RegisterMenu();
        this.mainMenu = new MainMenu();
        this.gameMenu = new GameMenu();
    }

}
