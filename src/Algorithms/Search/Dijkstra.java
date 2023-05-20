package Algorithms.Search;

import Util.Node;
import Visualization.Panel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static Visualization.Panel.*;
import static Visualization.Panel.solvingSpeed;

public class Dijkstra extends SearchAlgo {
    private ArrayList<Node> openSet;
    private final ArrayList<Node> closedSet = new ArrayList<>();

    public Dijkstra(Panel panel) {
        super(panel);
    }

    protected void initializeNodes() {
        openSet = Arrays.stream(nodesGrid).flatMap(Arrays::stream).collect(Collectors.toCollection(ArrayList::new));
        openSet.add(START_NODE);
        for (Node[] nodes : nodesGrid) {
            for (Node node : nodes) {
                node.setDistance(Integer.MAX_VALUE);
                node.setParent(null);
                node.addNeighbors(false);
            }
        }
    }

    public void find() {
        Node currentNode = START_NODE;
        currentNode.setDistance(0);
        searchTime = 0;
        startTime = System.currentTimeMillis();
        while (!openSet.isEmpty() && currentNode != END_NODE) {
            currentNode = openSet.get(0);
            for (Node node : openSet) {
                if (node.getCost() < currentNode.getCost()) {
                    currentNode = node;
                }
            }
            openSet.remove(currentNode);
            closedSet.add(currentNode);
            currentNode.setAsChecked();
            for (Node neighbor : currentNode.getNeighbors()) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                if (neighbor.getCost() > currentNode.getCost() + 1) {
                    neighbor.setDistance(currentNode.getCost() + 1);
                    neighbor.setParent(currentNode);
                }
            }
            numNodesExpanded++;
            if (solvingSpeed > 0) {
                process();
            }
        }
//        System.out.println("Path found");
//        tracePath();
    }
}
