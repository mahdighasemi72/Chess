package Pieces;

import java.awt.*;
import java.util.Map;

public class Piece {
    protected String name;
    protected Point position;//First Integer is Y

    public String getName() {
        return name;
    }

    public Point getPosition() {
        return position;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Piece(String name, Point position) {
        this.name = name;
        this.position = position;
    }
}
