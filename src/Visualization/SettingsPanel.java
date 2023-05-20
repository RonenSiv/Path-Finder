package Visualization;

import Algorithms.Create.*;
import Algorithms.Search.*;
import Util.HeuristicFunction;
import Util.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.Objects;

import static Visualization.Panel.*;

public class SettingsPanel extends JPanel {
    private final JButton startButton = new JButton("Start");
    private final JButton stop = new JButton("Stop");
    private final JButton resetButton = new JButton("Generate");

    private final JLabel speedLabel = new JLabel("Speed");
    private final JScrollBar speed = new JScrollBar(JScrollBar.HORIZONTAL, 1, 1, 0, 1000);
    private final JLabel speedValue = new JLabel(solvingSpeed + " fps");

    private final JLabel sizeLabel = new JLabel("Size");
    private final JScrollBar size = new JScrollBar(JScrollBar.HORIZONTAL, 2, 1, 2, 100);
    private final JLabel sizeValue = new JLabel(nodeSize + " px");

    private final JLabel info = new JLabel("");

    JLabel heuristicLabel = new JLabel("Heuristic Function");
    private final String[] heuristics = {"Manhattan", "Euclidean", "Chebyshev", "Octile", "Octagonal", "Diagonal"};
    private final JComboBox<String> heuristicBox = new JComboBox<>(heuristics);

    JLabel algorithmLabel = new JLabel("Algorithm");
    private final String[] algorithms = {"Dijkstra", "A*", "DFS", "BFS", "Recursive Backtracking"};
    private final JComboBox<String> algorithmBox = new JComboBox<>(algorithms);

    JLabel mazeTypeLabel = new JLabel("Maze Type");
    private final String[] mazeTypes = {"Recursive Backtracking", "Prim's", "Hunt & Kill", "Binary Tree", "Wilson's",
    "Kruskal's", "Aldous-Broder", "Recursive Division"};
    private final JComboBox<String> mazeTypeBox = new JComboBox<>(mazeTypes);

    private final Panel panel;

    public SettingsPanel(Panel panel) {
        this.panel = panel;
        this.setPreferredSize(new Dimension(SETTINGS_WIDTH, SETTINGS_HEIGHT));
        this.setLayout(null);
        this.setBounds(Panel.WIDTH - SETTINGS_WIDTH, 0, SETTINGS_WIDTH, SETTINGS_HEIGHT);
        addButtonsToPanel();
        setButtonsBounds();
        setButtonsAction();
    }

    private void addButtonsToPanel() {
        // add speed to panel
        this.add(speedLabel);
        this.add(speed);
        this.add(speedValue);
        // add size to panel
        this.add(sizeLabel);
        this.add(size);
        this.add(sizeValue);
        // add maze type to panel
        this.add(mazeTypeLabel);
        this.add(mazeTypeBox);
        // add algorithm to panel
        this.add(algorithmLabel);
        this.add(algorithmBox);
        // add heuristic to panel
        if(Objects.equals(algorithmBox.getSelectedItem(), "A*")){
            this.add(heuristicLabel);
            this.add(heuristicBox);
        }
        // add function buttons to panel
        this.add(startButton);
        this.add(resetButton);
        this.add(stop);
        // add info to panel
        this.add(info);
    }

    private void setButtonsBounds() {
        int downShift = 20;
        int labelWidth = 50;
        int scrollWidth = 150;
        int buttonWidth = 100;
        int fontSize = 15;
        // speed bounds
        speedLabel.setBounds((SETTINGS_WIDTH - labelWidth) / 2 - scrollWidth, downShift, labelWidth, downShift);
        speed.setBounds((SETTINGS_WIDTH - scrollWidth) / 2, downShift, scrollWidth, 20);
        speedValue.setBounds((SETTINGS_WIDTH - labelWidth) / 2 + scrollWidth, downShift, labelWidth, downShift);
        // size bounds
        sizeLabel.setBounds((SETTINGS_WIDTH - labelWidth) / 2 - scrollWidth, downShift * 3, labelWidth, downShift);
        size.setBounds((SETTINGS_WIDTH - scrollWidth) / 2, downShift * 3, scrollWidth, downShift);
        sizeValue.setBounds((SETTINGS_WIDTH - labelWidth) / 2 + scrollWidth, downShift * 3, labelWidth, downShift);
        // maze algorithm bounds
        mazeTypeLabel.setBounds((SETTINGS_WIDTH - labelWidth) / 2 - scrollWidth, downShift * 5, labelWidth, downShift);
        mazeTypeBox.setBounds((SETTINGS_WIDTH - scrollWidth) / 2, downShift * 5, scrollWidth, downShift);
        // algorithm bounds
        algorithmLabel.setBounds((SETTINGS_WIDTH - labelWidth) / 2 - scrollWidth, downShift * 7,
                labelWidth * 2, downShift);
        algorithmBox.setBounds((SETTINGS_WIDTH - scrollWidth) / 2, downShift * 7, scrollWidth, downShift);

        // heuristic bounds
        heuristicLabel.setBounds((SETTINGS_WIDTH - labelWidth) / 2 - scrollWidth,
                downShift * 9, labelWidth * 3, downShift);
        heuristicBox.setBounds((SETTINGS_WIDTH - scrollWidth) / 2 + labelWidth,
                downShift * 9, scrollWidth, downShift);
        if(Objects.equals(algorithmBox.getSelectedItem(), "A*")) {
            heuristicBox.setEnabled(true);
        } else {
            heuristicBox.setEnabled(false);
        }
        // function button bounds
        startButton.setBounds((SETTINGS_WIDTH - buttonWidth) / 2, downShift * 11, buttonWidth, downShift);
        resetButton.setBounds((SETTINGS_WIDTH - buttonWidth) / 2, downShift * 13, buttonWidth, downShift);
        stop.setBounds((SETTINGS_WIDTH - buttonWidth) / 2, downShift * 15, buttonWidth, downShift);
        // info bounds (for displaying algorithm info)
        info.setBounds(0, downShift * 17, SETTINGS_WIDTH, fontSize * 10);
        info.setFont(new Font("Arial", Font.BOLD, fontSize));
        info.setForeground(Color.RED);
        info.setHorizontalAlignment(JLabel.CENTER);
        info.setVerticalAlignment(JLabel.CENTER);
    }

    private void setButtonsAction() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetPath();
                SearchAlgo pathFinding = switch (algorithmBox.getSelectedItem().toString()) {
                    case "Dijkstra" -> new Dijkstra(panel);
                    case "A*" -> new aStar(panel);
                    case "DFS" -> new DFS(panel);
                    case "BFS" -> new BFS(panel);
                    case "Recursive Backtracking" -> new RBT(panel);
                    default -> null;
                };
                assert pathFinding != null;
                pathFinding.execute();
            }
        });

        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (drawPath) {
                    drawPath = false;
                    stop.setText("Start");
                } else {
                    drawPath = true;
                    stop.setText("Stop");
                }
                panel.repaint();
//                stop();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.resetPanel();
                panel.repaint();
                MazeAlgo maze = switch (mazeTypeBox.getSelectedItem().toString()) {
                    case "Recursive Backtracking" -> new RBTMaze(panel);
                    case "Prim's" -> new PrimMaze(panel);
                    case "Hunt & Kill" -> new HuntAndKillMaze(panel);
                    case "Binary Tree" -> new BinaryTreeMaze(panel);
                    case "Wilson's" -> new WilsonMaze(panel);
                    case "Kruskal's" -> new KruskalMaze(panel);
                    case "Aldous-Broder" -> new AldousBroder(panel);
                    case "Recursive Division" -> new RecursiveDivision(panel);
                    default -> null;
                };
                assert maze != null;
                maze.execute();
            }
        });

        speed.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                speedValue.setText(String.valueOf(speed.getValue()) + " fps");
                solvingSpeed = speed.getValue();
                speedValue.repaint();
                speed.repaint();
            }
        });

        size.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                sizeValue.setText(String.valueOf(size.getValue()) + " px");
                sizeValue.repaint();
                size.repaint();
                nodeSize = size.getValue();
                panel.resetPanel();
                panel.repaint();
            }
        });

        heuristicBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = heuristicBox.getSelectedIndex();
                Node.heuristicFunction.setType(HeuristicFunction.Heuristic.values()[index]);
                for (int row = 0; row < numRow; row++) {
                    for (int col = 0; col < numCol; col++) {
                        nodesGrid[row][col].setCost();
                    }
                }
            }
        });

        algorithmBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = algorithmBox.getSelectedIndex();
                if (index == 1) {
                    heuristicBox.setEnabled(true);
                    SettingsPanel.this.add(heuristicLabel);
                    SettingsPanel.this.add(heuristicBox);
                } else {
                    heuristicBox.setSelectedIndex(0);
                    heuristicBox.setEnabled(false);
                    SettingsPanel.this.remove(heuristicLabel);
                    SettingsPanel.this.remove(heuristicBox);
                }
            }
        });
    }

    private void resetPath() {
        for (Util.Node[] nodes : nodesGrid) {
            for (int j = 0; j < nodes.length; j++) {
                nodes[j].resetPath();
            }
        }
        pathLength = 0;
        numNodesExpanded = 0;
        searchTime = 0;
        pathNodes = new ArrayList<>();
        panel.repaint();
    }

    public void setInfo(String info) {
        this.info.setText(info);
    }

    public void enableButtons(boolean b) {
        // size bounds
        size.setEnabled(b);
        // function buttons
        startButton.setEnabled(b);
        resetButton.setEnabled(b);
        stop.setEnabled(b);
    }
}
