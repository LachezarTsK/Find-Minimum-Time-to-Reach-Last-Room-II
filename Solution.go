
package main

import (
    "container/heap"
    "fmt"
    "math"
)

var UP = [2]int{-1, 0}
var DOWN = [2]int{1, 0}
var LEFT = [2]int{0, -1}
var RIGHT = [2]int{0, 1}
var MOVES = [4][2]int{UP, DOWN, LEFT, RIGHT}

var rows int
var columns int

var startRow int
var startColumn int

var targetRow int
var targetColumn int

func minTimeToReach(moveTime [][]int) int {
    rows = len(moveTime)
    columns = len(moveTime[0])

    startRow = 0
    startColumn = 0

    targetRow = rows - 1
    targetColumn = columns - 1

    return dijkstraSearchForPathWithMinTime(moveTime)
}

func dijkstraSearchForPathWithMinTime(moveTime [][]int) int {
    minHeapForTime := make(PriorityQueue, 0)
    step := NewStep(startRow, startColumn, 0, 0)
    heap.Push(&minHeapForTime, step)

    minTimeMatrix := make([][]int, rows)
    for row := 0; row < rows; row++ {
        minTimeMatrix[row] = make([]int, columns)
        for column := 0; column < columns; column++ {
            minTimeMatrix[row][column] = math.MaxInt
        }
    }
    minTimeMatrix[startRow][startColumn] = 0

    for len(minHeapForTime) > 0 {
        current := heap.Pop(&minHeapForTime).(*Step)
        if current.row == targetRow && current.column == targetColumn {
            break
        }

        for _, move := range MOVES {
            nextRow := current.row + move[0]
            nextColumn := current.column + move[1]
            if !isInMatrix(nextRow, nextColumn) {
                continue
            }

            timeToMoveToNextPoint := getNewTimeToMoveBetweenTwoPoints(current.timeToMoveBetweenTwoPoints)
            nextValueForTime := max(timeToMoveToNextPoint+current.timeFromStart,
                                    timeToMoveToNextPoint+moveTime[nextRow][nextColumn])

            if minTimeMatrix[nextRow][nextColumn] > nextValueForTime {
                minTimeMatrix[nextRow][nextColumn] = nextValueForTime
                step := NewStep(nextRow, nextColumn, timeToMoveToNextPoint, nextValueForTime)
                heap.Push(&minHeapForTime, step)
            }
        }
    }

    return minTimeMatrix[targetRow][targetColumn]
}

func isInMatrix(row int, column int) bool {
    return row >= 0 && row < rows && column >= 0 && column < columns
}

func getNewTimeToMoveBetweenTwoPoints(prviousTimeToMoveBetweenTwoPoints int) int {
    return 1 + (prviousTimeToMoveBetweenTwoPoints % 2)
}

type Step struct {
    row                        int
    column                     int
    timeToMoveBetweenTwoPoints int
    timeFromStart              int
}

func NewStep(row int, column int, timeToMoveBetweenTwoPoints int, timeFromStart int) *Step {
    step := &Step{
        row:                        row,
        column:                     column,
        timeToMoveBetweenTwoPoints: timeToMoveBetweenTwoPoints,
        timeFromStart:              timeFromStart,
    }
    return step
}

type PriorityQueue []*Step

func (pq PriorityQueue) Len() int {
    return len(pq)
}

func (pq PriorityQueue) Less(first int, second int) bool {
    return pq[first].timeFromStart < pq[second].timeFromStart
}

func (pq PriorityQueue) Swap(first int, second int) {
    pq[first], pq[second] = pq[second], pq[first]
}

func (pq *PriorityQueue) Push(object any) {
    step := object.(*Step)
    *pq = append(*pq, step)
}

func (pq *PriorityQueue) Pop() any {
    step := (*pq)[len(*pq)-1]
    (*pq)[len(*pq)-1] = nil
    *pq = (*pq)[0 : len(*pq)-1]
    return step
}
