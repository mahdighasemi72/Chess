package Pieces;

import java.awt.*;

public class King extends Piece{
    public King(String name, Point position) {
        super(name, position);
    }
    public boolean isCorrectPath(Point start, Point destination){
        if (Math.abs(destination.x - start.x) <= 1 && Math.abs(destination.y - start.y) <= 1)
            return true;
        return false;
    }
}
