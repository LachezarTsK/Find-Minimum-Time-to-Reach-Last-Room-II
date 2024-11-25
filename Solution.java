
import java.util.Arrays;
import java.util.PriorityQueue;

public class Solution {

    private record Step(int row, int column, int timeToMoveBetweenTwoPoints, int timeFromStart) {}

    private static final int[] UP = {-1, 0};
    private static final int[] DOWN = {1, 0};
    private static final int[] LEFT = {0, -1};
    private static final int[] RIGHT = {0, 1};
    private static final int[][] MOVES = {UP, DOWN, LEFT, RIGHT};

    private int rows;
    private int columns;

    private int startRow;
    private int startColumn;

    private int targetRow;
    private int targetColumn;

    public int minTimeToReach(int[][] moveTime) {
        rows = moveTime.length;
        columns = moveTime[0].length;

        startRow = 0;
        startColumn = 0;

        targetRow = rows - 1;
        targetColumn = columns - 1;

        return dijkstraSearchForPathWithMinTime(moveTime);
    }

    private int dijkstraSearchForPathWithMinTime(int[][] moveTime) {
        PriorityQueue<Step> minHeapForTime = new PriorityQueue<>((x, y) -> x.timeFromStart - y.timeFromStart);
        minHeapForTime.add(new Step(startRow, startColumn, 0, 0));

        int[][] minTimeMatrix = new int[rows][columns];
        for (int row = 0; row < rows; ++row) {
            Arrays.fill(minTimeMatrix[row], Integer.MAX_VALUE);
        }
        minTimeMatrix[startRow][startColumn] = 0;

        while (!minHeapForTime.isEmpty()) {
            Step current = minHeapForTime.poll();
            if (current.row == targetRow && current.column == targetColumn) {
                break;
            }

            for (int[] move : MOVES) {
                int nextRow = current.row + move[0];
                int nextColumn = current.column + move[1];
                if (!isInMatrix(nextRow, nextColumn)) {
                    continue;
                }

                int timeToMoveToNextPoint = getNewTimeToMoveBetweenTwoPoints(current.timeToMoveBetweenTwoPoints);
                int nextValueForTime = Math.max(timeToMoveToNextPoint + current.timeFromStart,
                                                timeToMoveToNextPoint + moveTime[nextRow][nextColumn]);

                if (minTimeMatrix[nextRow][nextColumn] > nextValueForTime) {
                    minTimeMatrix[nextRow][nextColumn] = nextValueForTime;
                    minHeapForTime.add(new Step(nextRow, nextColumn, timeToMoveToNextPoint, nextValueForTime));
                }
            }
        }

        return minTimeMatrix[targetRow][targetColumn];
    }

    private boolean isInMatrix(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    private int getNewTimeToMoveBetweenTwoPoints(int prviousTimeToMoveBetweenTwoPoints) {
        return 1 + (prviousTimeToMoveBetweenTwoPoints % 2);
    }
}
