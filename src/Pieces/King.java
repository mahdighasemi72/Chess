package Pieces;

import java.awt.*;

public class King extends Piece{
    public King(String name, Point position) {
        super(name, position);
    }
    public boolean isCorrectPath(Point start, Point destination){
        if (Math.sqrt(destination.x - start.x) <= 1 && Math.sqrt(destination.y - start.y) <= 1)
            return true;
        return false;
    }
}
