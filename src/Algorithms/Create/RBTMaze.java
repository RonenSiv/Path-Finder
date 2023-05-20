package Algorithms.Create;

import Util.Node;
import Visualization.Panel;
import java.util.Random;
import java.util.Stack;
import static Visualization.Panel.*;

public class RBTMaze extends MazeAlgo{
    private boolean mazeFinished = false;
    private final Stack<Node> stack = new Stack<>();

    public RBTMaze(Panel panel) {
        super(panel);
    }

    public void generateMaze() {
        Random random = new Random();
        Node currentNode = nodesGrid[random.nextInt(numRow - 1)][random.nextInt(numCol - 1)];
        stack.push(currentNode);
        currentNode.setAsVisited();
        while (!mazeFinished) {
            currentNode.setType(Node.NodeType.CHECKED);
            if (currentNode.hasUnvisitedNeighbors()) {
                Node nextCurrent =  currentNode.getRandomUnvisitedNeighbor();
                stack.push(currentNode);
                removeWall(currentNode, nextCurrent);
                currentNode = nextCurrent;
                currentNode.setBackgroundColor(PATH_COLOR);
                process();
            } else if (stack.size() > 0) {
                currentNode = stack.pop();
                currentNode.setType(Node.NodeType.VISITED);
                process();
            } else {
                mazeFinished = true;
            }
        }
    }
}
