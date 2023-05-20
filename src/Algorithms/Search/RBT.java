package Algorithms.Search;

import Util.Node;
import Visualization.Panel;

import java.util.Stack;

import static Visualization.Panel.*;

public class RBT extends SearchAlgo{
    private Stack<Node> stack = new Stack<>();
    private Node currentNode;
    public RBT(Panel panel) {
        super(panel);
    }

    @Override
    protected void find() {
        stack.push(START_NODE);
        startTime = System.currentTimeMillis();
        while (!stack.isEmpty()) {
            currentNode.setAsChecked();
            if (currentNode.equals(END_NODE)) {
//                tracePath();
                break;
            }
            if (currentNode.hasUnvisitedNeighbors()) {
                Node nextCurrent = currentNode.getRandomUnvisitedNeighbor();
                stack.push(currentNode);
                nextCurrent.setParent(currentNode);
                currentNode = nextCurrent;
                process();
            } else if (stack.size() > 0) {
                currentNode = stack.pop();
            } else {
                break;
            }
            numNodesExpanded++;
        }
    }

    @Override
    protected void initializeNodes() {
        for (Node[] nodes : Panel.nodesGrid) {
            for (Node node : nodes) {
                node.setDistance(Integer.MAX_VALUE);
                node.addNeighbors(false);
                node.setParent(null);
            }
        }
        currentNode = START_NODE;
    }
}
