package Pieces;

import java.awt.*;

public class Soldier extends Piece {
    public Soldier(String name, Point position) {
        super(name, position);
    }

    public boolean isCorrectPath(Point start, Point destination){
        if (destination.x == start.x){
            switch (start.y){
                case 2 :
                    if (destination.y == 3 || destination.y == 4)
                        return true;
                    break;
                case 3-7 :
                    if (destination.y == start.y + 1)
                        return true;
                    break;
            }
        }
        return false;
    }
}
