package Pieces;

import java.awt.*;

public class Castle extends Piece {
    public Castle(String name, Point position) {
        super(name, position);
    }

    public boolean isCorrectPath(Point start, Point destination){
        if (destination.x == start.x){
            return true;
        } else if (destination.y == start.y) {
            return true;
        }
        return false;
    }
}
