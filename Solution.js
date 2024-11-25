
// const {PriorityQueue} = require('@datastructures-js/priority-queue');
/*
 PriorityQueue is internally included in the solution file on leetcode.
 So, when running the code on leetcode it should stay commented out. 
 It is mentioned here as a comment, just for information about 
 which external library is applied for this data structure.
 */

/**
 * @param {number[][]} moveTime
 * @return {number}
 */
var minTimeToReach = function (moveTime) {
    const UP = [-1, 0];
    const DOWN = [1, 0];
    const LEFT = [0, -1];
    const RIGHT = [0, 1];

    this.MOVES = [UP, DOWN, LEFT, RIGHT];

    this.rows = moveTime.length;
    this.columns = moveTime[0].length;

    this.startRow = 0;
    this.startColumn = 0;

    this.targetRow = this.rows - 1;
    this.targetColumn = this.columns - 1;

    return dijkstraSearchForPathWithMinTime(moveTime);
};

/**
 * @param {number} row
 * @param {number} column
 * @param {number} timeToMoveBetweenTwoPoints 
 * @param {number} timeFromStart
 */
function Step(row, column, timeToMoveBetweenTwoPoints, timeFromStart) {
    this.row = row;
    this.column = column;
    this.timeToMoveBetweenTwoPoints = timeToMoveBetweenTwoPoints;
    this.timeFromStart = timeFromStart;
}

/**
 * @param {number[][]} moveTime
 * @return {number}
 */
function dijkstraSearchForPathWithMinTime(moveTime) {
    const minHeapForTime = new MinPriorityQueue({compare: (x, y) => x.timeFromStart - y.timeFromStart});
    minHeapForTime.enqueue(new Step(this.startRow, this.startColumn, 0, 0));

    const minTimeMatrix = Array.from(new Array(this.rows), () => new Array(this.columns).fill(Number.MAX_SAFE_INTEGER));
    minTimeMatrix[this.startRow][this.startColumn] = 0;

    while (!minHeapForTime.isEmpty()) {
        const current = minHeapForTime.dequeue();
        if (current.row === this.targetRow && current.column === this.targetColumn) {
            break;
        }

        for (let move of this.MOVES) {
            const nextRow = current.row + move[0];
            const nextColumn = current.column + move[1];
            if (!isInMatrix(nextRow, nextColumn)) {
                continue;
            }

            const timeToMoveToNextPoint = getNewTimeToMoveBetweenTwoPoints(current.timeToMoveBetweenTwoPoints);
            const nextValueForTime = Math.max(timeToMoveToNextPoint + current.timeFromStart,
                                              timeToMoveToNextPoint + moveTime[nextRow][nextColumn]);

            if (minTimeMatrix[nextRow][nextColumn] > nextValueForTime) {
                minTimeMatrix[nextRow][nextColumn] = nextValueForTime;
                minHeapForTime.enqueue(new Step(nextRow, nextColumn, timeToMoveToNextPoint, nextValueForTime));
            }
        }
    }

    return minTimeMatrix[this.targetRow][this.targetColumn];
}

/**
 * @param {number} row
 * @param {number} column
 * @return {boolean}
 */
function isInMatrix(row, column) {
    return row >= 0 && row < this.rows && column >= 0 && column < this.columns;
}

/**
 * @param {number} prviousTimeToMoveBetweenTwoPoints
 * @return {number}
 */
function getNewTimeToMoveBetweenTwoPoints(prviousTimeToMoveBetweenTwoPoints) {
    return 1 + (prviousTimeToMoveBetweenTwoPoints % 2);
}
