package Util;

import java.awt.*;
import java.util.*;
import java.util.List;

import static Visualization.Panel.*;

public class Node implements Comparable<Node> {
    public static Node start = null;
    public static Node end = null;
    public static HeuristicFunction heuristicFunction = new HeuristicFunction(HeuristicFunction.Heuristic.EUCLIDEAN);

    private final int row;
    private final int col;
    private int fCost, gCost, hCost;

    private List<Node> neighbors = new ArrayList<>();
    private Node parent;
    private Rectangle rect;
    private final Wall wall;

    private boolean checked = false;
    private boolean open = false;
    private boolean visited = false;
    private boolean path = false;

    public enum NodeType {
        START,
        END,
        WALKABLE,
        CHECKED,
        VISITED,
        PATH,
        SOLUTION,
    }

    private NodeType type = NodeType.WALKABLE;

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
        int y = row * nodeSize;
        int x = col * nodeSize;
        this.fCost = Integer.MAX_VALUE;
        this.gCost = Integer.MAX_VALUE;
        this.hCost = Integer.MAX_VALUE;
        rect = new Rectangle(x, y, nodeSize, nodeSize, WALKABLE_COLOR);
        wall = new Wall(x, y, nodeSize, nodeSize, new boolean[]{true, true, true, true});
    }

    public void draw(Graphics2D g) {
        rect.draw(g);
        wall.draw(g);
//        if(isPath()) {
//            g.setColor(new Color(255, 255, 255));
//            g.drawLine(this.rect.getCenterX(), this.rect.getCenterY(), this.parent.getRect().getCenterX(),
//                    this.parent.getRect().getCenterY());
//        }
    }

    public Rectangle getRect() {
        return this.rect;
    }

    public Wall getWall() {
        return  this.wall;
    }

    public void addNeighbors(boolean ignoreWalls) {
        neighbors = new ArrayList<>();
        if (ignoreWalls) {
            if (row > 0) {
                neighbors.add(nodesGrid[row - 1][col]); // top
            }
            if (row < numRow - 1) {
                neighbors.add(nodesGrid[row + 1][col]); // bottom
            }
            if (col > 0) {
                neighbors.add(nodesGrid[row][col - 1]); // left
            }
            if (col < numCol - 1) {
                neighbors.add(nodesGrid[row][col + 1]); // right
            }
        } else {
            if (!wall.hasTopWall()) {
                neighbors.add(nodesGrid[row - 1][col]); // top
            }
            if (!wall.hasBottomWall()) {
                neighbors.add(nodesGrid[row + 1][col]); // bottom
            }
            if (!wall.hasLeftWall()) {
                neighbors.add(nodesGrid[row][col - 1]); // left
            }
            if (!wall.hasRightWall()) {
                neighbors.add(nodesGrid[row][col + 1]); // right
            }
        }
    }
    public void setAsVisited() {
        if(this != start && this != end) {
            setBackgroundColor(VISITED_COLOR);
        }
        this.visited = true;
    }

    public boolean istVisited() {
        return this.visited;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean hasUnvisitedNeighbors() {
        for (Node neighbor : getNeighbors()) {
            if (neighbor != null && !neighbor.istVisited()) {
                return true;
            }
        }
        return false;
    }

    public Node getRandomUnvisitedNeighbor() {
        Random random = new Random();
        ArrayList<Node> unvisitedNeighbors = new ArrayList<>();
        for(Node neighbor : getNeighbors()) {
            if(!neighbor.istVisited()) {
                unvisitedNeighbors.add(neighbor);
            }
        }
        if(unvisitedNeighbors.isEmpty()) {
            return null;
        }
        Node neighbor = unvisitedNeighbors.get((int) Math.floor(random.nextInt(0, unvisitedNeighbors.size())));
        neighbor.setAsVisited();
        return neighbor;
    }

    public void setBackgroundColor(Color color) {
        this.rect.setColor(color);
    }

    public void resetPath() {
        if(this != start && this != end) {
            setType(NodeType.WALKABLE);
        }

        this.open = false;
        this.checked = false;
        this.visited = false;
        this.path = false;
        this.parent = null;
    }

    public void setG() {
        if (start != null) {
            this.gCost = heuristicFunction.getDistance(this, start);
        }
    }

    public void setH() {
        if (end != null) {
            this.hCost = heuristicFunction.getDistance(this, end);
        }
    }

    public void setF() {
        this.fCost = this.gCost + this.hCost;
    }

    @Override
    public int compareTo(Node o) {
        if (this.fCost < o.fCost) {
            return -1;
        } else if (this.fCost > o.fCost) {
            return 1;
        } else {
            return Integer.compare(this.hCost, o.hCost);
        }
    }

    public void setParent(Node currentNode) {
        this.parent = currentNode;
    }

    public List<Node> getNeighbors() {
        return neighbors;
    }

    public void changeType() {
        if (start == null && this != end) {
            start = this;
            this.rect.setColor(START_COLOR);
        } else if (end == null && this != start) {
            end = this;
            this.rect.setColor(END_COLOR);
        } else {
            if (start == this) {
                start = null;
                this.rect.setColor(WALKABLE_COLOR);
            } else if (end == this) {
                end = null;
                this.rect.setColor(WALKABLE_COLOR);
            }
        }
        START_NODE = start;
        END_NODE = end;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }

    public Node getParent() {
        return parent;
    }

    public void setCost() {
        if (start != null) {
            setG();
        }
        if (end != null) {
            setH();
        }
        this.fCost = this.gCost + this.hCost;
    }

    public int getCost() {
        return this.fCost;
    }

    public void setDistance(int maxValue) {
        this.fCost = maxValue;
    }

    public void setAsChecked() {
        if(this != start && this != end) {
            setBackgroundColor(CHECKED_COLOR);
        }
        this.checked = true;
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setAsOpen() {
        this.open = true;
    }

    public void setAsPath() {
//        if(this != start && this != end) {
//            setBackgroundColor(PATH_COLOR);
//        }
        path = true;
    }

    public boolean isPath() {
        return path;
    }

    public void setType(NodeType type) {
        boolean changeType = true;
        if(type == NodeType.START) {
            if(start != null){
                if(this == end) {
                    end = null;
                    END_NODE = end;
                }
                if(this != start) {
                    start.setType(NodeType.WALKABLE);
                }
            }
            start = this;
            START_NODE = start;
        } else if(type == NodeType.END) {
            if(end != null){
                if(this == start) {
                    start = null;
                    START_NODE = start;
                }
                if(this != end) {
                    end.setType(NodeType.WALKABLE);
                }
            }
            end = this;
            END_NODE = end;
        } else if(type == NodeType.WALKABLE) {
            if(this == start) {
                start = null;
                START_NODE = start;
            } else if(this == end) {
                end = null;
                END_NODE = end;
            }
        } else if(type == NodeType.PATH) {
            if(this == start || this == end) {
                changeType = false;
            }
            setAsPath();
        } else if(type == NodeType.CHECKED) {
            if(this == start || this == end) {
                changeType = false;
            }
            setAsChecked();
        } else if(type == NodeType.VISITED) {
            if(this == start || this == end) {
                changeType = false;
            }
            setAsVisited();
        }
        if(changeType) {
            this.type = type;
        }
        setTypeColor();
    }
    public void setTypeColor() {
        if(type == NodeType.START) {
            this.rect.setColor(START_COLOR);
        } else if(type == NodeType.END) {
            this.rect.setColor(END_COLOR);
        } else if (type == NodeType.WALKABLE) {
            this.rect.setColor(WALKABLE_COLOR);
        } else if (type == NodeType.PATH) {
            this.rect.setColor(PATH_COLOR);
        } else if (type == NodeType.CHECKED) {
            this.rect.setColor(CHECKED_COLOR);
        }
    }

    public Color getColor() {
        return this.rect.getColor();
    }

    public boolean checkIfHasWallWith(Node other) {
        if(this.row == other.row) {
            if(this.col - other.col == - 1) {
                return this.wall.hasRightWall() && other.getWall().hasLeftWall();
            } else if(this.col - other.col == 1) {
                return this.wall.hasLeftWall() && other.getWall().hasRightWall();
            }
            return false;
        } else if(this.col == other.col) {
            if(this.row - other.row == - 1) {
                return this.wall.hasBottomWall() && other.getWall().hasTopWall();
            } else if(this.row - other.row == 1) {
                return this.wall.hasTopWall() && other.getWall().hasBottomWall();
            }
            return false;
        }
        return false;
    }
    public Node getTopNeighbor() {
        for(Node node: neighbors) {
            if(node.getRow() - this.getRow() == -1 && node.getCol() == this.getCol()) {
                return node;
            }
        }
        return null;
    }
    public Node getRightNeighbor() {
        for(Node node: neighbors) {
            if(node.getRow() == this.getRow() && node.getCol() - this.getCol() == 1) {
                return node;
            }
        }
        return null;
    }
    public Node getBottomNeighbor() {
        for(Node node: neighbors) {
            if(node.getRow() - this.getRow() == 1 && node.getCol() == this.getCol()) {
                return node;
            }
        }
        return null;
    }
    public Node getLeftNeighbor() {
        for(Node node: neighbors) {
            if(node.getRow() == this.getRow() && node.getCol() - this.getCol() == -1) {
                return node;
            }
        }
        return null;
    }
}
