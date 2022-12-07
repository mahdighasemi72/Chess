package Pieces;

import java.awt.*;

public class Elephant extends Piece{
    public Elephant(String name, Point position) {
        super(name, position);
    }

    public boolean isCorrectPath (Point start, Point destination){
        if (Math.abs(destination.x - start.x) == Math.abs(destination.y - start.y)){
            return true;
        }
        return false;
    }
}
