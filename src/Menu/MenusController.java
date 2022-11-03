package Menu;

import Player.Player;

import java.util.ArrayList;

public class MenusController {
    private static int menuNum;

    public static void controlMenu(int menuNum) {
        if (menuNum == 1){
            RegisterMenu.Begin();
        } else if (menuNum==2) {
            MainMenu.makeGame();
        } else if (menuNum == 3) {
            GameMenu.play();
        }
    }

    public int getMenuNum() {
        return menuNum;
    }

    public void setMenuNum(int menuNum) {
        this.menuNum = menuNum;
    }

    public MenusController(int menuNum) {
        this.menuNum = menuNum;
    }

}
