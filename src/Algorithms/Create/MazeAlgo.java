package Algorithms.Create;

import Util.Node;
import Visualization.Panel;

import javax.swing.*;
import static Visualization.Frame.frameRate;
import static Visualization.Panel.*;
import static Visualization.Panel.nodesGrid;

public abstract class MazeAlgo extends SwingWorker<Void, Void> {
    protected final Panel panel;

    protected MazeAlgo(Panel panel) {
        this.panel = panel;
    }

    protected abstract void generateMaze();

    @Override
    protected Void doInBackground() throws Exception {
        panel.settingsPanel.enableButtons(false);
        generateMaze();
        mazeFinished();
        panel.settingsPanel.enableButtons(true);
        return null;
    }

    protected void process() {
        if (solvingSpeed > 0) {
            frameRate(solvingSpeed);
        }
        panel.repaint();
    }

    protected void removeWall(Node currentNode, Node nextNode) {
        if (currentNode.getRow() == nextNode.getRow()) {
            if (currentNode.getCol() < nextNode.getCol()) {
                currentNode.getWall().setRightWall(false);
                nextNode.getWall().setLeftWall(false);
            } else {
                currentNode.getWall().setLeftWall(false);
                nextNode.getWall().setRightWall(false);
            }
        } else {
            if (currentNode.getRow() < nextNode.getRow()) {
                currentNode.getWall().setBottomWall(false);
                nextNode.getWall().setTopWall(false);
            } else {
                currentNode.getWall().setTopWall(false);
                nextNode.getWall().setBottomWall(false);
            }
        }
    }

    protected void mazeFinished() {
        int count = 0;
        for (int row = 0; row < numRow; row++, count++) {
            for (int col = 0; col < numCol; col++) {
                nodesGrid[row][col].resetPath();
                if(count >= (numRow) / 10) {
                    process();
                }
            }
            if(count >= (numRow) / 10) {
                count = 0;
            }
        }
        panel.repaint();
    }
}
