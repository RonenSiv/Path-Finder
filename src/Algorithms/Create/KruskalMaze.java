package Algorithms.Create;

import Util.Node;
import Visualization.Panel;

import java.util.*;

import static Visualization.Panel.*;

public class KruskalMaze extends MazeAlgo {
    private final ArrayList<Node> nodesList = new ArrayList<>();
    private final ArrayList<Set> nodesSet = new ArrayList<>();

    public KruskalMaze(Panel panel) {
        super(panel);
    }

    @Override
    protected void generateMaze() {
        createSets();

    }

    public void createSets() {
        for (int row = 0; row < numRow; row++) {
            nodesList.addAll(Arrays.asList(nodesGrid[row]).subList(0, numCol));
        }

        while (!nodesList.isEmpty()) {
            Random random = new Random();
            Node randomNode = nodesList.get(random.nextInt(nodesList.size()));
            Node neighbor = randomNode.getRandomUnvisitedNeighbor();
            if (neighbor == null) {
                nodesList.remove(randomNode);
                randomNode.setAsVisited();
                Set set = new Set();
                set.addNode(randomNode);
                nodesSet.add(set);
                continue;
            }
            removeWall(randomNode, neighbor);
            randomNode.setAsVisited();
            neighbor.setAsVisited();
            Set set = new Set();
            set.addNode(randomNode);
            set.addNode(neighbor);
            nodesSet.add(set);
            nodesList.remove(randomNode);
            nodesList.remove(neighbor);
            process();
        }

        while (nodesSet.size() > 1) {
            Random random = new Random();
            Set randomSet = nodesSet.get(random.nextInt(nodesSet.size()));
            Set neighborSet = randomSet.getRandomNeighborSet(nodesSet);
            if(neighborSet == null) {
                nodesSet.remove(randomSet);
            }
            randomSet.removeRandomCommonNode(neighborSet);
            for(Node n : neighborSet.getNodes()) {
                randomSet.addNode(n);
                n.setAsChecked();
            }
            nodesSet.remove(neighborSet);
            process();
        }
    }

    public class Set {
        private final List<Node> nodes;

        public Set() {
            nodes = new ArrayList<>();
        }

        public void addNode(Node node) {
            nodes.add(node);
        }

        public List<Node> getNodes() {
            return nodes;
        }

        public Set getRandomNeighborSet(ArrayList<Set> set) {
            Collections.shuffle(set);
            for (Set s : set) {
                if (s == this) {
                    continue;
                }
                for (Node node : nodes) {
                    for (Node other : s.getNodes()) {
                        if (node.checkIfHasWallWith(other)) {
                            return s;
                        }
                    }
                }
            }
            return null;
        }

        public void removeRandomCommonNode(Set other) {
            List<Set> commonNodes = new ArrayList<>();
            for (Node node : nodes) {
                for (Node otherNode : other.getNodes()) {
                    if (node.checkIfHasWallWith(otherNode)) {
                        Set set = new Set();
                        set.addNode(node);
                        set.addNode(otherNode);
                        commonNodes.add(set);
                    }
                }
            }
            if(commonNodes.isEmpty()) {
                return;
            }
            Random random = new Random();
            int randomIndex = random.nextInt(commonNodes.size());
            Node node1 = commonNodes.get(randomIndex).getNodes().get(0);
            Node node2 = commonNodes.get(randomIndex).getNodes().get(1);
            removeWall(node1, node2);
        }
    }
}
