package Algorithms.Create;

import Util.Node;
import Visualization.Panel;

import java.util.ArrayList;
import java.util.Random;

import static Visualization.Panel.*;

public class AldousBroder extends MazeAlgo{
    public AldousBroder(Panel panel) {
        super(panel);
    }

    @Override
    protected void generateMaze() {
        ArrayList<Node> nodesList = new ArrayList<>();
        for (int row = 0; row < numRow; row++) {
            for (int col = 0; col < numCol; col++) {
                nodesList.add(nodesGrid[row][col]);
            }
        }
        Random random = new Random();
        Node currentNode = nodesList.get(random.nextInt(nodesList.size()));
        while (!nodesList.isEmpty()) {
            currentNode.setType(Node.NodeType.VISITED);
            nodesList.remove(currentNode);
            Node neighbor = currentNode.getNeighbors().get(random.nextInt(currentNode.getNeighbors().size()));
            if(!neighbor.istVisited()) {
                removeWall(currentNode, neighbor);
            }
            currentNode.setBackgroundColor(PATH_COLOR);
            process();
            currentNode.setType(Node.NodeType.VISITED);
            currentNode = neighbor;
        }
    }
}
