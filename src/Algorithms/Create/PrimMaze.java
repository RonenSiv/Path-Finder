package Algorithms.Create;

import Util.Node;
import Visualization.Panel;
import java.util.*;
import static Visualization.Panel.*;

public class PrimMaze extends MazeAlgo {
    public PrimMaze(Panel panel) {
        super(panel);
    }

    public void generateMaze() {
        for (int row = 0; row < numRow; row++) {
            for (int col = 0; col < numCol; col++) {
                nodesGrid[row][col].addNeighbors(true);
            }
        }
        List<NodePairs> nodePairs = new ArrayList<>();
        Random random = new Random();
        Node startNode = nodesGrid[random.nextInt(numRow)][random.nextInt(numCol)];
        startNode.setAsVisited();
        for (Node node : startNode.getNeighbors()) {
            nodePairs.add(new NodePairs(startNode, node));
        }
        NodePairs currentPair;
        while (!nodePairs.isEmpty()) {
            currentPair = nodePairs.get(random.nextInt(nodePairs.size()));
            currentPair.getNode1().setBackgroundColor(CHECKED_COLOR);
            currentPair.getNode2().setBackgroundColor(CHECKED_COLOR);
            if ((currentPair.getNode1().istVisited() && currentPair.getNode2().istVisited())
                    || (!currentPair.getNode1().istVisited() && !currentPair.getNode2().istVisited())) {
                nodePairs.remove(currentPair);
                continue;
            }
            currentPair.getNode1().setType(Node.NodeType.VISITED);
            currentPair.getNode2().setType(Node.NodeType.VISITED);
            removeWall(currentPair.getNode1(), currentPair.node2);
            nodePairs.remove(currentPair);
            for(Node node : currentPair.getNode1().getNeighbors()) {
                    nodePairs.add(new NodePairs(currentPair.getNode1(), node));
            }
            for(Node node : currentPair.getNode2().getNeighbors()) {
                    nodePairs.add(new NodePairs(currentPair.getNode2(), node));
            }
            process();
        }
    }

    static class NodePairs {
        private final Node node1;
        private final Node node2;

        public NodePairs(Node node1, Node node2) {
            this.node1 = node1;
            this.node2 = node2;
        }

        public Node getNode1() {
            return node1;
        }

        public Node getNode2() {
            return node2;
        }
    }
}
