package Algorithms.Search;

import Util.Node;
import Visualization.Panel;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static Visualization.Frame.frameRate;
import static Visualization.Panel.*;

public abstract class SearchAlgo extends SwingWorker<Void, Void> {
    protected final Panel panel;
    protected long startTime;
    private final List<Node> pathList = new ArrayList<>();

    public SearchAlgo(Panel panel) {
        this.panel = panel;
    }

    protected abstract void find();

    protected abstract void initializeNodes();

    @Override
    protected Void doInBackground() throws Exception {
        if (START_NODE == null || END_NODE == null) {
            System.out.println("Start or end node is null");
            return null;
        }
        panel.settingsPanel.enableButtons(false);
        initializeNodes();
        find();
        tracePath();//
        panel.settingsPanel.enableButtons(true);
        return null;
    }

    protected void process() {
        searchTime = System.currentTimeMillis() - startTime;
        panel.updateInfo();
        if (solvingSpeed > 0) {
            frameRate(solvingSpeed);
        }
        panel.repaint();
    }

    protected void tracePath() {
        Node currentNode = END_NODE;
        while (currentNode.getParent() != null && !currentNode.equals(START_NODE)) {
//            currentNode.setAsPath();
            pathList.add(currentNode);
            currentNode = currentNode.getParent();
            pathLength++;
//            process();
        }
        Collections.reverse(pathList);
        for(Node node : pathList) {
            node.setAsPath();
            pathNodes.add(node);
            process();
        }
//        panel.repaint();
    }
}
