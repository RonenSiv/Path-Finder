package Visualization;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    private static Frame instance;

    private Frame() {
    }

    public static Frame getInstance() {
        if(instance == null) {
            instance = new Frame();
        }
        return instance;
    }

    public void init() {
        if(instance != null) {
            Panel panel = new Panel();
            this.setTitle("Maze Solver");
            this.getContentPane().add(panel);
            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
//            this.setResizable(false);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    public static void frameRate(int fps) {
        try {
            Thread.sleep(1000 / fps);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
