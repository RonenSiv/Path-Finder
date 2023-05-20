package Algorithms.Create;

import Util.Node;
import Visualization.Panel;

import java.util.Random;

import static Visualization.Panel.*;

public class RecursiveDivision extends MazeAlgo {
    private static final int HORIZONTAL = 1;
    private static final int VERTICAL = 2;

    private static final int S = 1;
    private static final int E = 2;

    private final Random rand = new Random();

    public RecursiveDivision(Panel panel) {
        super(panel);
    }

    @Override
    protected void generateMaze() {
        for (int row = 0; row < numRow; row++) {
            for (int col = 0; col < numCol; col++) {
                Node node = nodesGrid[row][col];
                node.addNeighbors(true);
                if (col > 0) {
                    node.getWall().setLeftWall(false);
                }
                if (col < numCol - 1) {
                    node.getWall().setRightWall(false);
                }
                if (row > 0) {
                    node.getWall().setTopWall(false);
                }
                if (row < numRow - 1) {
                    node.getWall().setBottomWall(false);
                }
            }
        }
        divide(nodesGrid, 0, 0, numCol, numRow, chooseOrientation(numCol, numRow));
    }

    private void divide(Node[][] grid, int currentCol, int currentRow, int numCols, int numRows, int orientation) {
        if (numCols < 2 || numRows < 2) {
            return;
        }
        boolean horizontal = orientation == HORIZONTAL;

        int randomCol = currentCol + (horizontal ? 0 : rand.nextInt(numCols - 1));
        int randomRow = currentRow + (horizontal ? rand.nextInt(numRows - 1) : 0);

        int colHole = randomCol + (horizontal ? rand.nextInt(numCols) : 0);
        int rowHole = randomRow + (horizontal ? 0 : rand.nextInt(numRows));

        int colDrawDir = horizontal ? 1 : 0;
        int rowDrawDir = horizontal ? 0 : 1;

        int length = horizontal ? numCols : numRows;

        int dir = horizontal ? S : E;

        for (int i = 0; i < length; i++) {
            if (randomCol != colHole || randomRow != rowHole) {
                Node node = grid[randomRow][randomCol];
                if (dir == S) {
                    node.getWall().setBottomWall(true);
                    if (node.getBottomNeighbor() != null) {
                        node.getBottomNeighbor().getWall().setTopWall(true);
                    }
                } else {
                    node.getWall().setRightWall(true);
                    if (node.getRightNeighbor() != null) {
                        node.getRightNeighbor().getWall().setLeftWall(true);
                    }
                }
                process();
            }
            randomCol += colDrawDir;
            randomRow += rowDrawDir;
        }

        int nCol = currentCol;
        int nRow = currentRow;
        int w = horizontal ? numCols : randomCol - currentCol + 1;
        int h = horizontal ? randomRow - currentRow + 1 : numRows;
        divide(grid, nCol, nRow, w, h, chooseOrientation(w, h));

        nCol = horizontal ? currentCol : randomCol + 1;
        nRow = horizontal ? randomRow + 1 : currentRow;
        w = horizontal ? numCols : currentCol + numCols - randomCol - 1;
        h = horizontal ? currentRow + numRows - randomRow - 1 : numRows;
        divide(grid, nCol, nRow, w, h, chooseOrientation(w, h));
    }

    private int chooseOrientation(int w, int h) {
        if (w < h) {
            return HORIZONTAL;
        } else if (h < w) {
            return VERTICAL;
        } else {
            return rand.nextInt(2) + 1;
        }
    }
}
