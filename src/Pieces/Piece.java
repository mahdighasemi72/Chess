package Pieces;

import java.awt.*;
import java.util.Map;

public class Piece {
    protected String name;
    protected Point position;//First Integer is Y

    public Piece(String name, Point position) {
        this.name = name;
        this.position = position;
    }
}
