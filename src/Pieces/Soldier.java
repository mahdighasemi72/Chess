package Pieces;

import java.awt.*;
import java.util.Map;
import java.util.Set;

public class Soldier extends Piece {
    private final String symbol = "Pw";

    public Soldier(String name, Point position) {
        super(name, position);
    }

    public boolean isCorrectPath(Point startPosition, Point destination){
        if (destination.x == startPosition.x){
            switch (startPosition.y){
                case 2 :
                    if (destination.y == 3 || destination.y == 4)
                        return true;
                    break;
                case 3-7 :
                    if (destination.y == startPosition.y + 1)
                        return true;
                    break;
            }
        }
        return false;
    }
}
