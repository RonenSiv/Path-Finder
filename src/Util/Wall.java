package Util;

import java.awt.*;

import static Visualization.Panel.WALL_COLOR;
import static Visualization.Panel.nodeSize;

public class Wall {
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean[] walls; // top, right, bottom, left
    public Wall(int x, int y, int width, int height, boolean[] walls) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.walls = walls;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean[] getWalls() {
        return walls;
    }

    public void setTopWall(boolean topWall) {
        walls[0] = topWall;
    }

    public void setRightWall(boolean rightWall) {
        walls[1] = rightWall;
    }


    public void setBottomWall(boolean bottomWall) {
        walls[2] = bottomWall;
    }

    public void setLeftWall(boolean leftWall) {
        walls[3] = leftWall;
    }

    public boolean hasTopWall() {
        return walls[0];
    }

    public boolean hasRightWall() {
        return walls[1];
    }

    public boolean hasBottomWall() {
        return walls[2];
    }

    public boolean hasLeftWall() {
        return walls[3];
    }

    public void draw(Graphics2D g) {
        g.setColor(WALL_COLOR);
        if (hasTopWall()) {
            g.drawLine(x, y, x + nodeSize, y); // top
        }
        if (hasRightWall()) {
            g.drawLine(x + nodeSize, y, x + nodeSize, y + nodeSize); // right
        }
        if (hasBottomWall()) {
            g.drawLine(x + nodeSize, y + nodeSize, x, y + nodeSize); // bottom
        }
        if (hasLeftWall()) {
            g.drawLine(x, y + nodeSize, x, y); // left
        }
    }
}
