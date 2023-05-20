package Algorithms.Create;

import Util.Node;
import Visualization.Panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Visualization.Panel.*;

public class BinaryTreeMaze extends MazeAlgo {
    public BinaryTreeMaze(Panel panel) {
        super(panel);
    }

    @Override
    protected void generateMaze() {
        for (int row = numRow - 1; row >= 0; row--) {
            for (int col = 0; col < numCol; col++) {
                Node currentNode = nodesGrid[row][col];
                currentNode.setType(Node.NodeType.VISITED);
                Node neighborNode = getNeighbor(currentNode);
                if (neighborNode != null) {
                    removeWall(currentNode, neighborNode);
                }
                process();
            }
        }
    }

    public Node getNeighbor(Node node) {
        int row = node.getRow();
        int col = node.getCol();
        List<Node> neighbors = new ArrayList<>();
        if (row > 0) {
            if (col < numCol - 1) {
                neighbors.add(nodesGrid[row][col + 1]);
                neighbors.add(nodesGrid[row - 1][col]);
            } else {
                neighbors.add(nodesGrid[row - 1][col]);
            }
        } else {
            if (col < numCol - 1) {
                neighbors.add(nodesGrid[row][col + 1]);
            } else {
                return null;
            }
        }
        return neighbors.get(new Random().nextInt(neighbors.size()));
    }
}
