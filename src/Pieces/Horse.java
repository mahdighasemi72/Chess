package Pieces;

import java.awt.*;

public class Horse extends Piece{
    public Horse(String name, Point position) {
        super(name, position);
    }
    public boolean isCorrectPath(Point start, Point destination){
        if (Math.sqrt(destination.y - start.y) == 2){
            if (Math.sqrt(destination.x - start.x) == 1)
                return true;
        } else if (Math.sqrt(destination.x - start.x) == 2) {
            if (Math.sqrt(destination.y - start.y) == 1)
                return true;
        }
        return false;
    }
}
