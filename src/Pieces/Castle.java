package Pieces;

import java.awt.*;

public class Castle extends Piece {
    public Castle(String name, Point position) {
        super(name, position);
    }

    public boolean isCorrectPath(Point startPosition, Point destination){
        if (destination.x == startPosition.x){
            return true;
        } else if (destination.y == startPosition.y) {
            return true;
        }
        return false;
    }
}
