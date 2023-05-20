package Algorithms.Search;

import Util.Node;
import Visualization.Panel;

import java.util.Stack;

import static Visualization.Frame.frameRate;
import static Visualization.Panel.*;
import static Visualization.Panel.solvingSpeed;

public class DFS extends SearchAlgo {
    private long startTime;
    private final Stack<Node> stack = new Stack<>();

    public DFS(Panel panel) {
        super(panel);
    }

    public void find() {
        stack.push(START_NODE);
        startTime = System.currentTimeMillis();
        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();
            currentNode.setAsChecked();
            if (currentNode.equals(END_NODE)) {
//                tracePath();
                break;
            }
            for (Node neighbor : currentNode.getNeighbors()) {
                if (!stack.contains(neighbor) && neighbor.isChecked() || neighbor.isOpen()) {
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

    protected void process() {
        searchTime = System.currentTimeMillis() - startTime;
        panel.updateInfo();
        if (solvingSpeed > 0) {
            frameRate(solvingSpeed);
        }
    }
}
