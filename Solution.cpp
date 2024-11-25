
#include <span>
#include <limits>
#include <vector>
#include <algorithm>
using namespace std;

class Solution {

    struct Step {
            size_t row{};
            size_t column{};
            int timeToMoveBetweenTwoPoints{};
            int timeFromStart{};

            Step(size_t row, size_t column, int timeToMoveBetweenTwoPoints, int timeFromStart) :
                    row{ row }, column{ column }, timeToMoveBetweenTwoPoints{ timeToMoveBetweenTwoPoints }, timeFromStart{ timeFromStart } {
            };
    };

    struct ComparatorStep {
            bool operator()(const Step& first, const Step& second) const {
                    return first.timeFromStart > second.timeFromStart;
            }
    };

    static constexpr array<int, 2> UP = { -1, 0 };
    static constexpr array<int, 2> DOWN = { 1, 0 };
    static constexpr array<int, 2> LEFT = { 0, -1 };
    static constexpr array<int, 2> RIGHT = { 0, 1 };
    static constexpr array< array<int, 2>, 4> MOVES = { UP, DOWN, LEFT, RIGHT };

    size_t rows{};
    size_t columns{};

    size_t startRow{};
    size_t startColumn{};

    size_t targetRow{};
    size_t targetColumn{};

public:
    int minTimeToReach(const vector<vector<int>>& moveTime) {
            rows = moveTime.size();
            columns = moveTime[0].size();

            startRow = 0;
            startColumn = 0;

            targetRow = rows - 1;
            targetColumn = columns - 1;

            return dijkstraSearchForPathWithMinTime(moveTime);
    }

private:
    int dijkstraSearchForPathWithMinTime(span<const vector<int>> moveTime) const {
        priority_queue<Step, vector<Step>, ComparatorStep> minHeapForTime;
        minHeapForTime.emplace(startRow, startColumn, 0, 0);

        vector<vector<int>> minTimeMatrix(rows, vector<int>(columns, numeric_limits<int>::max()));
        minTimeMatrix[startRow][startColumn] = 0;

        while (!minHeapForTime.empty()) {
            Step current = minHeapForTime.top();
            minHeapForTime.pop();
            if (current.row == targetRow && current.column == targetColumn) {
                    break;
            }

            for (const auto& move : MOVES) {
                size_t nextRow = current.row + move[0];
                size_t nextColumn = current.column + move[1];
                if (!isInMatrix(nextRow, nextColumn)) {
                    continue;
                }

                int timeToMoveToNextPoint = getNewTimeToMoveBetweenTwoPoints(current.timeToMoveBetweenTwoPoints);
                int nextValueForTime = max(timeToMoveToNextPoint + current.timeFromStart,
                                           timeToMoveToNextPoint + moveTime[nextRow][nextColumn]);

                if (minTimeMatrix[nextRow][nextColumn] > nextValueForTime) {
                    minTimeMatrix[nextRow][nextColumn] = nextValueForTime;
                    minHeapForTime.emplace(nextRow, nextColumn, timeToMoveToNextPoint, nextValueForTime);
                }
            }
        }

        return minTimeMatrix[targetRow][targetColumn];
    }

    bool isInMatrix(size_t row, size_t column) const {
        return  row < rows && column < columns;
    }

    int getNewTimeToMoveBetweenTwoPoints(int prviousTimeToMoveBetweenTwoPoints) const {
        return 1 + (prviousTimeToMoveBetweenTwoPoints % 2);
    }
};
