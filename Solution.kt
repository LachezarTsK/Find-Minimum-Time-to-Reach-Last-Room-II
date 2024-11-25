
import java.util.PriorityQueue
import kotlin.math.max

class Solution {

    private data class Step(
        val row: Int,
        val column: Int,
        val timeToMoveBetweenTwoPoints: Int,
        val timeFromStart: Int
    ) {}

    private companion object {
        val UP = intArrayOf(-1, 0)
        val DOWN = intArrayOf(1, 0)
        val LEFT = intArrayOf(0, -1)
        val RIGHT = intArrayOf(0, 1)
        val MOVES = arrayOf(UP, DOWN, LEFT, RIGHT)
    }

    private var rows = 0
    private var columns = 0

    private var startRow = 0
    private var startColumn = 0

    private var targetRow = 0
    private var targetColumn = 0

    fun minTimeToReach(moveTime: Array<IntArray>): Int {
        rows = moveTime.size
        columns = moveTime[0].size

        startRow = 0
        startColumn = 0

        targetRow = rows - 1
        targetColumn = columns - 1

        return dijkstraSearchForPathWithMinTime(moveTime)
    }

    private fun dijkstraSearchForPathWithMinTime(moveTime: Array<IntArray>): Int {
        val minHeapForTime = PriorityQueue<Step>() { x, y -> x.timeFromStart - y.timeFromStart }
        minHeapForTime.add(Step(startRow, startColumn, 0, 0))

        val minTimeMatrix = Array<IntArray>(rows) { IntArray(columns) }
        for (row in 0..<rows) {
            minTimeMatrix[row].fill(Int.MAX_VALUE, 0, columns)
        }
        minTimeMatrix[startRow][startColumn] = 0

        while (!minHeapForTime.isEmpty()) {
            val current = minHeapForTime.poll()
            if (current.row == targetRow && current.column == targetColumn) {
                break
            }

            for (move in MOVES) {
                val nextRow = current.row + move[0]
                val nextColumn = current.column + move[1]
                if (!isInMatrix(nextRow, nextColumn)) {
                    continue
                }

                val timeToMoveToNextPoint = getNewTimeToMoveBetweenTwoPoints(current.timeToMoveBetweenTwoPoints)
                val nextValueForTime = max(timeToMoveToNextPoint + current.timeFromStart,
                                           timeToMoveToNextPoint + moveTime[nextRow][nextColumn])

                if (minTimeMatrix[nextRow][nextColumn] > nextValueForTime) {
                    minTimeMatrix[nextRow][nextColumn] = nextValueForTime
                    minHeapForTime.add(Step(nextRow, nextColumn, timeToMoveToNextPoint, nextValueForTime))
                }
            }
        }

        return minTimeMatrix[targetRow][targetColumn]
    }

    private fun isInMatrix(row: Int, column: Int): Boolean {
        return row in 0..<rows && column in 0..<columns
    }

    private fun getNewTimeToMoveBetweenTwoPoints(prviousTimeToMoveBetweenTwoPoints: Int): Int {
        return 1 + (prviousTimeToMoveBetweenTwoPoints % 2)
    }
}
