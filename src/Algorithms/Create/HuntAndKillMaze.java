package Algorithms.Create;

import Util.Node;
import Visualization.Panel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static Visualization.Panel.*;

public class HuntAndKillMaze extends MazeAlgo{

    public HuntAndKillMaze(Panel panel) {
        super(panel);
    }

    @Override
    protected void generateMaze() {
        Random random = new Random();
        Node startNode = nodesGrid[0][0];
        Node currentNode = nodesGrid[random.nextInt(numRow - 1)][random.nextInt(numCol - 1)];
        while (startNode != null) {
            currentNode.setType(Node.NodeType.VISITED);
            Node nextNode = getUnvisitedNeighbor(currentNode);
            if(nextNode != null) {
                removeWall(currentNode, nextNode);
                currentNode = nextNode;
//                currentNode.setType(Node.NodeType.CHECKED);
                currentNode.setBackgroundColor(CHECKED_COLOR);
                process();
            } else {
                startNode = searchForUnvisitedNode(startNode);
                currentNode = startNode;
            }
        }
    }

    public Node searchForUnvisitedNode(Node start) {
        int row = start.getRow();
        int col = start.getCol();

        for (int i = row; i < numRow; i++) {
            for (int j = i == row ? col : 0; j < numCol; j++) {
                Color color = nodesGrid[i][j].getColor();
                nodesGrid[i][j].setBackgroundColor(PATH_COLOR);
                process();
                nodesGrid[i][j].setBackgroundColor(color);
                Node neighbor = getUnvisitedNeighbor(nodesGrid[i][j]);
                if (neighbor != null) {
                    removeWall(nodesGrid[i][j], neighbor);
                    return nodesGrid[i][j];
                }
            }
        }
        return null;
    }

    public Node getUnvisitedNeighbor(Node node) {
        ArrayList<Node> neighbors = new ArrayList<>();
        for (Node neighbor : node.getNeighbors()) {
            if (!neighbor.istVisited()) {
                neighbors.add(neighbor);
            }
        }
        if(neighbors.size() > 0) {
            return neighbors.get(new Random().nextInt(neighbors.size()));
        }
        return null;
    }
}
