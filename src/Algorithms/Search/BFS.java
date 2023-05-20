package Algorithms.Search;

import Util.Node;
import Visualization.Panel;

import java.util.Stack;

import static Visualization.Panel.*;

public class BFS extends SearchAlgo {
    private final Stack<Node> stack = new Stack<>();

    public BFS(Panel panel) {
        super(panel);
    }

    @Override
    protected void find() {
        stack.push(START_NODE);
        startTime = System.currentTimeMillis();
        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();
            currentNode.setAsChecked();
            if (currentNode.equals(END_NODE)) {
//                tracePath();
                break;
            }
            for (int i = currentNode.getNeighbors().size() - 1; i >= 0; i--) {
                Node neighbor = currentNode.getNeighbors().get(i);
                if (neighbor.isChecked() || neighbor.isOpen()) {
                    continue;
                }
                openNode(neighbor, currentNode);
                stack.push(neighbor);
            }
            process();
        }
    }

    private void openNode(Node neighbor, Node currentNode) {
        neighbor.setAsOpen();
        neighbor.setParent(currentNode);
        numNodesExpanded++;
        process();
    }

    protected void initializeNodes() {
        for (Node[] nodes : panel.nodesGrid) {
            for (Node node : nodes) {
                node.setDistance(Integer.MAX_VALUE);
                node.addNeighbors(false);
                node.setParent(null);
            }
        }
    }
}
