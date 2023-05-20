package Algorithms.Create;

import Util.Node;
import Visualization.Panel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Visualization.Panel.*;

public class WilsonMaze extends MazeAlgo {
    private final List<Node> nodesList = new ArrayList<>();
    private Node start;
    private List<Node> currentPath = new ArrayList<>();
    public WilsonMaze(Panel panel) {
        super(panel);
    }

    public void generateMaze() {
        for (int row = 0; row < numRow; row++) {
            for (int col = 0; col < numCol; col++) {
                nodesList.add(nodesGrid[row][col]);
            }
        }
        Random random = new Random();
        Node currentNode = nodesList.get(random.nextInt(nodesList.size() - 1));
        currentNode.setType(Node.NodeType.VISITED);
        currentNode.setBackgroundColor(new Color(243, 209, 81, 150));
        start = currentNode;
        Node endNode = nodesList.get(random.nextInt(nodesList.size() - 1));
        endNode.setType(Node.NodeType.CHECKED);

        while (!nodesList.isEmpty()) {
            if(nodesList.size() == 1) {
                currentNode = nodesList.get(0);
                removeWall(currentNode, getRandomNeighbor(currentNode));
                nodesList.remove(currentNode);
                break;
            }
            Node neighborNode = getRandomNeighbor(currentNode);
            if(neighborNode.isChecked()) {
                neighborNode.setAsChecked();
                neighborNode.setParent(currentNode);
                setPath(neighborNode);
                currentNode = nodesList.get(random.nextInt(nodesList.size()));
                start = currentNode;
                for (Node node : nodesList) {
                    node.setType(Node.NodeType.WALKABLE);
                    node.setParent(null);
                }
                currentNode.setType(Node.NodeType.VISITED);
                continue;
            }
            else if(neighborNode.getParent() == null) {
                neighborNode.setParent(currentNode);
                currentNode = neighborNode;
            } else {
                currentNode = neighborNode;
            }
            drawCurrentPath(currentNode);
            process();
            for (Node node : currentPath) {
                node.setType(Node.NodeType.WALKABLE);
            }
        }
    }
    private void drawCurrentPath(Node node) {
        List<Node> path = new ArrayList<>();
        while (node.getParent() != null && !path.contains(node)) {
            if(node != start) {
                    node.setType(Node.NodeType.VISITED);
            } else {
                node.setBackgroundColor(new Color(243, 209, 81, 150));
            }
            path.add(node);
            node = node.getParent();
        }
        currentPath = path;
    }

    private void setPath(Node node) {
        List<Node> path = new ArrayList<>();
        while (!path.contains(node)) {
            node.setType(Node.NodeType.CHECKED);
            path.add(node);
            nodesList.remove(node);
            if(node.getParent() != null) {
                removeWall(node.getParent(), node);
                node = node.getParent();
            } else {
                break;
            }
        }
    }

    private Node getRandomNeighbor(Node currentNode) {
        List<Node> neighbors = currentNode.getNeighbors();
        Random random = new Random();
        return neighbors.get(random.nextInt(neighbors.size()));
    }

}
