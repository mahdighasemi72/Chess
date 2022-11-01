package Menu;

import Player.Player;

import java.util.ArrayList;

public class MenusController {
    private static int menuNum;
    public static void controlMenu(int menuNum) {
        switch (menuNum){
            case (1) :
                RegisterMenu.Begin();
            case (2) :
                MainMenu.makeGame();
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
