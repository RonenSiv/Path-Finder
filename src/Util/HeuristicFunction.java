package Util;

public class HeuristicFunction {

    public enum Heuristic {
        MANHATTAN,
        EUCLIDEAN,
        CHEBYSHEV,
        OCTILE,
        OCTOGONAL,
        DIAGONAL
    }

    private Heuristic heuristic;

    public HeuristicFunction(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    public String toString() {
        return heuristic.toString();
    }

    public int getDistance(Node start, Node end) {
        switch (heuristic) {
            case MANHATTAN:
                return manhattanDistance(start, end);
            case EUCLIDEAN:
                return euclideanDistance(start, end);
            case CHEBYSHEV:
                return chebyshevDistance(start, end);
            case OCTILE:
                return octileDistance(start, end);
            case OCTOGONAL:
                return octagonalDistance(start, end);
            case DIAGONAL:
                return diagonalDistance(start, end);
            default:
                return 0;
        }
    }

    public void setType(Heuristic type) {
        this.heuristic = type;
    }

    public int manhattanDistance(Node start, Node end) {
        return Math.abs(start.getCol() - end.getCol()) + Math.abs(start.getRow() - end.getRow());
    }

    public int euclideanDistance(Node start, Node end) {
        return (int) Math.sqrt(Math.pow(start.getCol() - end.getCol(), 2) + Math.pow(start.getRow() - end.getRow(), 2));
    }

    public int diagonalDistance(Node start, Node end) {
        int dx = Math.abs(start.getCol() - end.getCol());
        int dy = Math.abs(start.getRow() - end.getRow());
        return Math.min(dx, dy) + (Math.max(dx, dy) - Math.min(dx, dy)) / 2;
    }

    public int octagonalDistance(Node start, Node end) {
        int dx = Math.abs(start.getCol() - end.getCol());
        int dy = Math.abs(start.getRow() - end.getRow());
        return (dx + dy) + (Math.max(dx, dy) - Math.min(dx, dy)) / 2;
    }

    public int chebyshevDistance(Node start, Node end) {
        return Math.max(Math.abs(start.getCol() - end.getCol()), Math.abs(start.getRow() - end.getRow()));
    }

    public int octileDistance(Node start, Node end) {
        int dx = Math.abs(start.getCol() - end.getCol());
        int dy = Math.abs(start.getRow() - end.getRow());
        return (dx + dy) + (Math.max(dx, dy) - Math.min(dx, dy)) / 2;
    }

}
