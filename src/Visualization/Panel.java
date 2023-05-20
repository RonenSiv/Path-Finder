package Visualization;

import Util.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Panel extends JPanel {
    public static final Color BACKGROUND_COLOR = Color.WHITE;
    public static final Color WALKABLE_COLOR = Color.WHITE;
    public static final Color WALL_COLOR = new Color(0, 0, 0, 200);
    public static final Color START_COLOR = new Color(255, 165, 0, 100);
    public static final Color END_COLOR = new Color(121, 231, 90, 100);
    public static final Color CHECKED_COLOR = new Color(0, 255, 255, 100); // new Color(0, 255, 255, 100);
    public static final Color VISITED_COLOR = new Color(255, 236, 166, 100);
    public static final Color PATH_COLOR = new Color(113, 39, 173, 255);
    public final SettingsPanel settingsPanel = new SettingsPanel(this);

    public static int WIDTH = 1500;
    public static int HEIGHT = 900;
    public static int SETTINGS_WIDTH = 400;
    public static int SETTINGS_HEIGHT = HEIGHT;
    public static int GRID_WIDTH = WIDTH - SETTINGS_WIDTH;
    public static int GRID_HEIGHT = HEIGHT;
    public static int nodeSize = 20;
    public static int numRow = GRID_HEIGHT / nodeSize;
    public static int numCol = GRID_WIDTH / nodeSize;
    public static int solvingSpeed = 50;
    public static int pathLength = 0;
    public static int numNodesExpanded = 0;
    public static long searchTime = 0;
    public static boolean drawPath = true;

    public static Node START_NODE = Node.start;
    public static Node END_NODE = Node.end;
    public static Node[][] nodesGrid = new Node[numRow][numCol];
    public static ArrayList<Node> pathNodes = new ArrayList<>();

    public Panel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(BACKGROUND_COLOR);
        this.setLayout(null);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.add(settingsPanel);
        createNodes();

        this.addMouseListener(new MouseAdapter() {// provides empty implementation of all
            // MouseListener`s methods, allowing us to
            // override only those which interests us
            @Override //I override only one method for presentation
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    int x = e.getX();
                    int y = e.getY();
                    int row = y / nodeSize;
                    int col = x / nodeSize;
                    if (row < numRow && col < numCol) {
                        Panel.this.mouseClicked(row, col);
                    }
                }
            }
        });
    }

    public void createNodes() {
        for (int row = 0; row < numRow; row++) {
            for (int col = 0; col < numCol; col++) {
                nodesGrid[row][col] = new Node(row, col);
            }
        }
        for (int row = 0; row < numRow; row++) {
            for (int col = 0; col < numCol; col++) {
                nodesGrid[row][col].addNeighbors(true);
            }
        }
    }

    public void drawNodes(Graphics2D g2d) {
        if (drawPath && nodeSize >= 7) {
            for (int row = 0; row < numRow; row++) {
                for (int col = 0; col < numCol; col++) {
                    nodesGrid[row][col].draw(g2d);
                }
            }
            if (!pathNodes.isEmpty()) {
                ArrayList<Node> copyPathNodes = new ArrayList<>(pathNodes);
                int i = 0;
                for (Node node : copyPathNodes) {
//                    float hue = (float) (i + 1) / (float) pathNodes.size() + 1;
//                    Color color = Color.getHSBColor(0.8f, 0.5f, hue);
                    g2d.setColor(PATH_COLOR);
                    drawPath(node.getRect().getCenterX(), node.getRect().getCenterY(),
                            node.getParent().getRect().getCenterX(), node.getParent().getRect().getCenterY(), g2d);
                    i++;
                }
            }
        } else {
            ArrayList<Node> copyPathNodes = new ArrayList<>(pathNodes);
            for (Node node : copyPathNodes) {
                node.setBackgroundColor(PATH_COLOR);
            }
            for (int row = 0; row < numRow; row++) {
                for (int col = 0; col < numCol; col++) {
                    nodesGrid[row][col].draw(g2d);
                }
            }
        }
//        int i = 0;
//        for (int row = 0; row < numRow; row++) {
//            for (int col = 0; col < numCol; col++) {
//                if (nodesGrid[row][col].isPath()) {
//                    Node node = nodesGrid[row][col];
//                    g2d.setColor(new Color(255, 255, 255));
//                    g2d.drawLine(node.getRect().getCenterX(), node.getRect().getCenterY(),
//                            node.getParent().getRect().getCenterX(), node.getParent().getRect().getCenterY());
//                    g2d.setColor(colorArr[i]);
//                    drawPath(node.getRect().getCenterX(), node.getRect().getCenterY(),
//                            node.getParent().getRect().getCenterX(), node.getParent().getRect().getCenterY(), g2d);
//                    i++;
//                }
//            }
//        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawNodes(g2d);
        updateInfo();
    }

    public void resetPanel() {
        numCol = GRID_WIDTH / nodeSize;
        numRow = GRID_HEIGHT / nodeSize;
        nodesGrid = new Node[numRow][numCol];
        pathNodes.clear();
        Node.start = null;
        Node.end = null;
        START_NODE = Node.start;
        END_NODE = Node.end;
        createNodes();
        repaint();
    }

    private void mouseClicked(int row, int col) {
        nodesGrid[row][col].changeType();
        drawNodes(this.getGraphics2D());
        repaint();
    }

    public Graphics2D getGraphics2D() {
        return (Graphics2D) this.getGraphics();
    }

    public void updateInfo() {
        String info = "<html>";
        info += "Grid size: " + numRow + "x" + numCol + "<br>";
        if (START_NODE != null) {
            info += "Start: " + START_NODE + "<br>";
        } else {
            info += "Start: none<br>";
        }
        if (END_NODE != null) {
            info += "End: " + END_NODE + "<br>";
        } else {
            info += "End: none<br>";
        }
        info += "Solving speed: " + solvingSpeed + " fps<br>";
        info += "Path length: " + pathLength + "<br>";
        info += "Nodes expanded: " + numNodesExpanded + "<br>";
        info += "Search time: " + searchTime + " ms<br>";
        settingsPanel.setInfo(info + "</html>");
        repaint();
    }

    private void drawPath(int x, int y, int px, int py, Graphics2D g2d) {
        int width = Math.abs(x - px);
        int height = Math.abs(y - py);
        int rectX = 0;
        int rectY = 0;
        rectX = Math.min(x, px);
        rectY = Math.min(y, py);

        width = width == 0 ? nodeSize / 3 : nodeSize + nodeSize / 3;
        height = height == 0 ? nodeSize / 3 : nodeSize + nodeSize / 3;
        g2d.fillRect(rectX, rectY, width, height);
    }
}
