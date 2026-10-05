# Project 3: N-Queens and 8-Puzzle

## What This Program Does

This Python program demonstrates two search problems:

1. **N-Queens:** place one queen in each row of an `n x n` chessboard so that no two queens share a column or diagonal.
2. **8-Puzzle:** move numbered tiles around a `3 x 3` board until the board reaches the goal arrangement.

For both problems, the program demonstrates:

- **Greedy Best-First Search:** chooses the waiting state with the smallest heuristic value `h`.
- **A* Search:** chooses the waiting state with the smallest total value `f = g + h`, where `g` is the cost already spent and `h` estimates the remaining cost.

The file name says `N-Queens`, but the source file also contains the complete 8-Puzzle implementation.

## How To Run It

Run:

```bash
python3 Project-3_N-Queens.py
```

The program first shows a menu. Enter:

- `1` for N-Queens, then enter the number of queens.
- `2` for the fixed 8-Puzzle experiment.
- `3` to exit.

The script must be run from the folder containing the Python file, or the path must be supplied.

## Important Representation Ideas

### N-Queens state

A state is a list. The list index is a row, and the value stored at that index is the queen's column.

Example:

```python
[0, 1, 2, 3]
```

means:

- row `0` has a queen in column `0`;
- row `1` has a queen in column `1`;
- row `2` has a queen in column `2`;
- row `3` has a queen in column `3`.

### 8-Puzzle state

A puzzle state is a tuple of nine values. The values are read from left to right and top to bottom. The value `0` represents the blank space.

```python
(1, 2, 3,
 4, 0, 6,
 7, 5, 8)
```

### `Node`

A `Node` packages a state and search information:

- `state`: the board configuration;
- `parent`: the earlier node, used to rebuild a solution path;
- `g`: cost from the starting state to this node;
- `h`: estimated cost from this node to a goal;
- `f`: `g + h`, the value used by A*.

---

## Complete Line-by-Line Explanation

The sections below follow the source file from top to bottom. Blank lines do not execute an operation; they visually separate related statements. Comment and separator lines also do not execute, but they label the next block and make the source easier to read.

## 1. Module Description and Imports

### Lines 1-11: opening documentation string

```python
"""
Project-03
N-Queens and 8-Puzzle using Heuristic Search

Algorithms:
1. Greedy Best First Search
2. A* Search

Level:
Undergraduate Pattern Recognition Lab

Run:
python Project_03_Heuristic_Search.py
"""
```

- `"""` on the first line starts a multi-line string. Because it appears at the beginning of the file, Python treats it as the module documentation string.
- `Project-03` identifies the assignment.
- `N-Queens and 8-Puzzle using Heuristic Search` states the two problems and the general technique.
- `Algorithms:` introduces the list of algorithms demonstrated.
- `1. Greedy Best First Search` records the first algorithm.
- `2. A* Search` records the second algorithm.
- `Level:` labels the course-level information.
- `Undergraduate Pattern Recognition Lab` describes the academic context.
- `Run:` labels the run command written by the author.
- `python Project_03_Heuristic_Search.py` is an old example command. It does not match the current file name, so use `python3 Project-3_N-Queens.py` when running this file.
- The final `"""` ends the documentation string. These lines are not part of the search algorithm; they document the file for a human reader.

### Lines 13-15: imports

```python
import heapq
import time
import copy
```

- `import heapq` loads Python's heap/priority-queue library. The program uses `heappush` to add nodes and `heappop` to remove the node with the smallest priority.
- `import time` loads time-related functions. The program uses `time.time()` to measure approximately how long each search takes.
- `import copy` loads copying utilities, but this program never uses it. Removing it would not change the current behavior.
- The blank lines after these imports separate the imports from the first class.

## 2. The General `Node` Class

The separator comments introduce a class shared by both N-Queens and 8-Puzzle.

### Class declaration and constructor

```python
class Node:
```

- `class Node:` creates a new class named `Node`.
- A class is a blueprint for objects. Here, each object represents one possible search position.
- The colon starts the indented class body.

```python
    def __init__(self, state, parent=None, g=0, h=0):
```

- `def` defines a function; inside a class, this function is a method.
- `__init__` is the constructor. Python calls it automatically when code creates `Node(...)`.
- `self` refers to the new Node object.
- `state` is the board configuration being stored.
- `parent=None` optionally stores the node from which this node was generated. `None` means no parent was supplied, usually because this is the starting node.
- `g=0` gives a default path cost of zero.
- `h=0` gives a default heuristic value of zero.

```python
        self.state = state
```

- Stores the received board in the object's `state` attribute.
- Search code later reads `current.state` to inspect the board.

```python
        self.parent = parent
```

- Stores the previous Node in `parent`.
- This is essential for the 8-Puzzle because the final node can follow its parents backward to reconstruct every move.

```python
        self.g = g
```

- Stores the path cost already paid.
- In this program, each move has cost `1`, so A* increases `g` by one for every child.

```python
        self.h = h
```

- Stores the heuristic estimate supplied by the caller.
- The heuristic tells the search which states appear closer to a goal.

```python
        self.f = g + h
```

- Calculates the total priority used by A*.
- `f` combines the known cost `g` and estimated remaining cost `h`.
- Greedy search also stores this value, but when it creates nodes it leaves `g` at its default `0`, so its effective priority is just `h`.

### Priority comparison

```python
    def __lt__(self, other):
        return self.f < other.f
```

- `__lt__` means “less than.” `heapq` needs to know how two Node objects should be ordered.
- `other` is the other Node being compared.
- `return self.f < other.f` says that a node is smaller when its `f` value is smaller.
- Therefore, `heapq.heappop(queue)` normally returns the waiting node with the smallest `f`.
- When two nodes have the same `f`, this method does not define a special tie-breaker; the exact choice can depend on heap ordering.

## 3. N-Queens Board Display

### `print_board`

```python
def print_board(board):
```

- Defines a function named `print_board`.
- `board` is expected to be a list representing an N-Queens state.

```python
    n = len(board)
```

- `len(board)` counts the rows.
- The same count is also the board width because an N-Queens board is `n x n`.

```python
    print()
```

- Prints an empty line so the board is visually separated from earlier output.

```python
    for row in range(n):
```

- Repeats once for each row number: `0` through `n - 1`.
- `range(n)` produces those integer indices.

```python
        line = ""
```

- Starts an empty text string for the current row.
- Each cell will be appended to this string before the complete row is printed.

```python
        for col in range(n):
```

- Repeats once for every column in the current row.

```python
            if board[row] == col:
```

- Checks whether the queen recorded for this row is in the current column.
- `board[row]` is the queen's column; `col` is the column currently being displayed.

```python
                line += " Q "
```

- Adds a `Q` cell to the row text when the queen belongs in this column.
- `+=` appends text to the existing string.

```python
            else:
                line += " . "
```

- `else` handles a column without the queen.
- It appends a dot cell to represent an empty square.

```python
        print(line)
```

- Prints the completed row after all of its columns have been examined.

```python
    print()
```

- Prints a final blank line after the board.

### `queen_conflicts`

This function is the N-Queens heuristic: it counts pairs of queens that attack each other.

```python
def queen_conflicts(state):
    n = len(state)
    conflicts = 0
```

- Defines `queen_conflicts` with a board state argument.
- `n` records how many queens/rows exist.
- `conflicts` starts at zero because no pairs have been checked yet.

```python
    for i in range(n):
        for j in range(i + 1, n):
```

- The outer loop chooses the first queen index `i`.
- The inner loop chooses only later indices, starting at `i + 1`.
- This checks each pair once. For example, it checks `(0, 1)` but does not later check `(1, 0)`.

```python
            if state[i] == state[j]:
                conflicts += 1
```

- Compares the two queens' columns.
- Equal columns mean the queens attack vertically.
- The counter increases by one for that attacking pair.

```python
            if abs(state[i] - state[j]) == abs(i - j):
                conflicts += 1
```

- `state[i] - state[j]` measures the column difference; `abs` removes its sign.
- `i - j` measures the row difference; `abs` also removes its sign.
- Equal absolute differences mean the queens lie on the same diagonal.
- The counter increases for that attacking pair.

```python
    return conflicts
```

- Sends the total number of attacking pairs back to the caller.
- A lower value is better; zero means no pair attacks another queen.

### `queen_goal`

```python
def queen_goal(state):
    return queen_conflicts(state) == 0
```

- Defines the goal test for N-Queens.
- Calls `queen_conflicts(state)` and compares the result with zero.
- Returns `True` only when the state has no conflicts; otherwise it returns `False`.

### `queen_neighbors`

This function creates all states reachable by moving one queen to another column in the same row.

```python
def queen_neighbors(state):
    neighbors = []
    n = len(state)
```

- Defines the function.
- Creates an empty list for generated child states.
- Records the number of rows/columns.

```python
    for row in range(n):
        current_col = state[row]
```

- Selects each row.
- Saves the queen's current column in that row.

```python
        for col in range(n):
            if col != current_col:
```

- Tries every column.
- Skips the current column because leaving a queen where it is would not create a new state.

```python
                new_state = state.copy()
                new_state[row] = col
```

- `state.copy()` creates a separate list, preventing this function from changing the original state.
- The copied state's selected row is changed to the candidate column.

```python
                neighbors.append(new_state)
```

- Adds the newly generated board to the list.
- For an `n x n` board, this produces `n * (n - 1)` neighbors: each of `n` queens can move to `n - 1` other columns.

```python
    return neighbors
```

- Returns all one-move candidate states.

## 4. Greedy Best-First Search for N-Queens

```python
def greedy_nqueen(start):
    start_time = time.time()
```

- Defines the Greedy search function.
- Saves the current clock value before searching so elapsed time can be calculated later.

```python
    queue = []
    visited = set()
```

- `queue` is the priority queue of discovered but not yet processed Nodes.
- `visited` is a set of already processed states. Sets provide fast membership checks.

```python
    h = queen_conflicts(start)
    heapq.heappush(queue, Node(start, h=h))
```

- Calculates the start state's conflict count.
- Creates a starting Node. Since `g` is omitted, it defaults to `0`; its `f` becomes `0 + h`, effectively `h`.
- `heappush` inserts that Node while preserving heap order.

```python
    expanded = 0
```

- Counts how many unique states the algorithm removes from the queue and examines.

```python
    while queue:
        current = heapq.heappop(queue)
```

- Continues while at least one candidate is waiting.
- Removes the candidate with the smallest Node priority and stores it in `current`.

```python
        key = tuple(current.state)
```

- Converts the mutable list state into an immutable tuple.
- The tuple can safely be placed in a set and used as the state's identity.

```python
        if key in visited:
            continue
```

- If this state was already processed through another route, skips it.
- `continue` jumps to the next loop iteration.

```python
        visited.add(key)
        expanded += 1
```

- Marks the state as processed.
- Increases the expansion count.

```python
        if queen_goal(current.state):
```

- Tests whether the current board has zero conflicts.

```python
            end_time = time.time()
            return (
                current.state,
                expanded,
                end_time-start_time
            )
```

- Records the clock at the moment a solution is found.
- Immediately returns a three-item tuple: the solution state, number of expanded states, and elapsed seconds.
- `end_time-start_time` is the measured duration. Spaces around the minus operator would improve style but do not change behavior.

```python
        for child in queen_neighbors(current.state):
            if tuple(child) not in visited:
```

- Generates every board one queen move away.
- Converts each child list to a tuple and ignores children already processed.

```python
                heapq.heappush(
                    queue,
                    Node(
                        child,
                        h=queen_conflicts(child)
                    )
                )
```

- Creates a Node for the child.
- Calculates its conflict count as its heuristic.
- Uses the default `g=0`, so Greedy prioritizes the smallest conflict count.
- Pushes the child onto the heap.

```python
    return None
```

- Runs only if the queue becomes empty without finding a solution.
- `None` communicates that no solution was found.

## 5. A* Search for N-Queens

```python
def astar_nqueen(start):
    start_time = time.time()
    queue = []
    visited = set()
```

- Defines A* for N-Queens.
- Records the starting time.
- Creates an empty priority queue and visited-state set.

```python
    heapq.heappush(
        queue,
        Node(
            start,
            g=0,
            h=queen_conflicts(start)
        )
    )
```

- Creates the start node with path cost `g=0`.
- Computes the start heuristic.
- Since `Node` calculates `f=g+h`, A* starts with `f=0+h`.
- Pushes the start node into the heap.

```python
    expanded = 0
```

- Starts the expansion counter.

```python
    while queue:
        current = heapq.heappop(queue)
        key = tuple(current.state)
```

- Repeats while candidates exist.
- Removes the smallest `f` node.
- Converts its list state to a tuple for set membership.

```python
        if key in visited:
            continue
        visited.add(key)
        expanded += 1
```

- Skips duplicate processed states.
- Records this state as visited and counts its expansion.

```python
        if queen_goal(current.state):
            end_time=time.time()
            return(
                current.state,
                expanded,
                end_time-start_time
            )
```

- Tests for a conflict-free board.
- Records the finish time and returns the solution, expansion count, and duration.
- The missing spaces around `=` and `-` are formatting choices, not different operations.

```python
        for child in queen_neighbors(current.state):
            if tuple(child) not in visited:
                heapq.heappush(
                    queue,
                    Node(
                        child,
                        g=current.g+1,
                        h=queen_conflicts(child)
                    )
                )
```

- Generates each one-move child and skips already visited states.
- Gives the child `g=current.g+1`, because generating it costs one queen move.
- Calculates its conflict heuristic.
- The Node constructor calculates `f=(current.g+1)+h`; this is the A* priority.
- Pushes the child into the priority queue.

```python
    return None
```

- Reports failure if the queue is exhausted.

## 6. Running the N-Queens Experiment

```python
def run_nqueen():
```

- Defines the function called when the user chooses menu option `1`.

```python
    n = int(input("\nEnter number of queens: "))
```

- `input` displays a prompt and receives text from the keyboard.
- `\n` begins the prompt on a new line.
- `int(...)` converts the text to an integer, such as `"4"` to `4`.
- The integer controls the board size.
- Non-numeric input raises `ValueError`; the program does not catch that error.

```python
    start=[]
```

- Creates an empty list for the initial state.

```python
    for i in range(n):
        start.append(i)
```

- Loops over row indices from `0` to `n-1`.
- Places queen `i` in column `i`.
- For `n=4`, the starting state becomes `[0, 1, 2, 3]`, which places all queens on the main diagonal and creates conflicts.

```python
    print("\nInitial State:")
    print_board(start)
```

- Prints a heading on a new line.
- Displays the initial list as a board of `Q` and `.` characters.

```python
    print("Running Greedy Best First Search...")
    result1 = greedy_nqueen(start)
```

- Announces the first algorithm.
- Runs Greedy search using the initial state.
- Stores either the three-item result tuple or `None` in `result1`.

```python
    if result1:
```

- Checks whether a result was returned.
- A non-`None` tuple is truthy, so the output block runs after success.

```python
        print("\nGreedy Solution:")
        print_board(result1[0])
```

- Prints a solution heading.
- `result1[0]` retrieves the solution state from the tuple and displays it.

```python
        print("Expanded Nodes:", result1[1])
        print("Time:", result1[2], "seconds")
```

- `result1[1]` is the number of expanded states.
- `result1[2]` is the elapsed time in seconds.
- `print` accepts several arguments and places spaces between them.

```python
    print("\nRunning A* Search...")
    result2 = astar_nqueen(start)
```

- Announces and runs A* using the same initial state.
- Stores its result in `result2`.

```python
    if result2:
        print("\nA* Solution:")
        print_board(result2[0])
        print("Expanded Nodes:", result2[1])
        print("Time:", result2[2], "seconds")
```

- Checks for A* success.
- Displays the A* board, expansion count, and elapsed time using the same tuple positions.
- The blank lines and extra indentation in this block only improve visual separation.

## 7. 8-Puzzle Goal and Display Functions

```python
GOAL_STATE = (
    1,2,3,
    4,5,6,
    7,8,0
)
```

- Creates a module-level constant named `GOAL_STATE`.
- Parentheses and commas create a tuple containing nine values.
- `0` represents the blank tile.
- This is the arrangement every puzzle search tries to reach.

```python
def print_puzzle(state):
    print()
```

- Defines the puzzle-display function.
- Prints a blank line before the board.

```python
    for i in range(0,9,3):
        row = state[i:i+3]
```

- Loops through starting indexes `0`, `3`, and `6`, one for each puzzle row.
- `state[i:i+3]` is slicing: it extracts three consecutive values.

```python
        print(
            row[0],
            row[1],
            row[2]
        )
```

- Prints the three values in the extracted row.
- The multi-line formatting is still one `print` function call.

```python
    print()
```

- Adds a blank line after the puzzle.

## 8. 8-Puzzle State and Neighbor Generation

### Finding the blank

```python
def blank_position(state):
    return state.index(0)
```

- Defines a function for finding the blank tile.
- `state.index(0)` searches the tuple and returns the index where `0` occurs.
- For `(1,2,3,4,0,6,7,5,8)`, it returns `4`, the center position.
- It raises `ValueError` if a malformed state has no zero.

### Generating legal moves

```python
def puzzle_neighbors(state):
    neighbors=[]
    index = blank_position(state)
    row=index//3
    col=index%3
```

- Defines the neighbor-generation function.
- Starts an empty output list.
- Finds the blank's one-dimensional index.
- `// 3` gives the blank's grid row using integer division.
- `% 3` gives its grid column using the remainder.

```python
    moves=[]
```

- Creates a list of indexes that the blank is allowed to swap with.

```python
    if row>0:
        moves.append(index-3)
```

- If the blank is not on the top row, the tile three positions above can move into it.
- `index-3` is the index above in a 3-column board.

```python
    if row<2:
        moves.append(index+3)
```

- If the blank is not on the bottom row, the tile three positions below is legal.

```python
    if col>0:
        moves.append(index-1)
```

- If the blank is not in the leftmost column, the tile to its left is legal.

```python
    if col<2:
        moves.append(index+1)
```

- If the blank is not in the rightmost column, the tile to its right is legal.

```python
    for move in moves:
        new_state=list(state)
```

- Processes every legal swap target.
- Converts the immutable tuple to a mutable list so two positions can be changed.

```python
        new_state[index],new_state[move]=(
            new_state[move],
            new_state[index]
        )
```

- Uses Python multiple assignment to swap the blank and neighboring tile in one operation.
- The right side is evaluated before either left-side position is changed, so no temporary variable is needed.

```python
        neighbors.append(tuple(new_state))
```

- Converts the changed list back to a tuple.
- Adds the new immutable puzzle state to the output list.

```python
    return neighbors
```

- Returns every legal state reachable in exactly one move.

## 9. 8-Puzzle Heuristics

### Misplaced tiles

```python
def misplaced_tiles(state):
    count=0
```

- Defines the first puzzle heuristic.
- Starts a counter for tiles that are not in their goal positions.

```python
    for i in range(9):
        if (
            state[i]!=0
            and
            state[i]!=GOAL_STATE[i]
        ):
            count+=1
```

- Checks each of the nine positions.
- `state[i] != 0` excludes the blank from the count.
- `state[i] != GOAL_STATE[i]` detects a numbered tile in the wrong position.
- `and` requires both conditions to be true.
- Adds one for every misplaced numbered tile.

```python
    return count
```

- Returns the heuristic estimate.
- A value of zero means all numbered tiles are in their goal positions.

### Manhattan distance

```python
def manhattan_distance(state):
    distance=0
```

- Defines the second heuristic.
- Starts a total distance at zero.

```python
    for index,value in enumerate(state):
        if value!=0:
```

- `enumerate` supplies both each tile's current index and its value.
- Skips the blank because it is not counted in Manhattan distance.

```python
            goal_index=GOAL_STATE.index(value)
            current_row=index//3
            current_col=index%3
```

- Finds where the tile belongs in the goal tuple.
- Converts the current one-dimensional index into row and column coordinates.

```python
            goal_row=goal_index//3
            goal_col=goal_index%3
```

- Converts the tile's goal index into goal row and column coordinates.

```python
            distance += (
                abs(current_row-goal_row)
                +
                abs(current_col-goal_col)
            )
```

- Calculates vertical distance plus horizontal distance for this tile.
- `abs` ensures distance is positive regardless of direction.
- Adds the tile's distance to the total.
- This is usually more informative than simply counting misplaced tiles because it measures how far tiles must travel.

```python
    return distance
```

- Returns the total Manhattan-distance estimate.

## 10. Reconstructing an 8-Puzzle Solution

```python
def solution_path(node):
    path=[]
```

- Defines a function receiving the goal Node.
- Starts an empty list for states in the solution path.

```python
    while node:
        path.append(node.state)
        node=node.parent
```

- Follows parent links until `node` becomes `None`.
- Adds each state, beginning with the goal, to `path`.
- Replaces `node` with its parent, moving backward toward the start.

```python
    return path[::-1]
```

- `[::-1]` creates a reversed copy of the list.
- The collected order is goal-to-start, so reversing it produces start-to-goal order.

## 11. Generic Greedy Search for the 8-Puzzle

```python
def greedy_puzzle(start, heuristic):
    start_time=time.time()
    queue=[]
    visited=set()
```

- Defines one Greedy function that can use different heuristics.
- Records the start time.
- Creates the priority queue and visited-state set.
- `heuristic` is a function parameter, so callers can pass `misplaced_tiles` or `manhattan_distance`.

```python
    start_node=Node(
        start,
        h=heuristic(start)
    )
    heapq.heappush(queue,start_node)
```

- Builds a starting Node with the chosen heuristic value.
- Leaves `g` at zero, making the effective priority equal to `h`.
- Pushes the start node into the heap.

```python
    expanded=0
```

- Starts counting processed states.

```python
    while queue:
        current=heapq.heappop(queue)
```

- Repeats while candidates exist.
- Selects the node with the smallest priority.

```python
        if current.state in visited:
            continue
        visited.add(current.state)
        expanded+=1
```

- Skips duplicate puzzle states.
- Marks the current tuple as visited and increments the counter.

```python
        if current.state==GOAL_STATE:
            return(
                solution_path(current),
                expanded,
                time.time()-start_time
            )
```

- Tests whether the current tuple exactly matches the goal tuple.
- If so, rebuilds the path, records expanded states, and calculates elapsed time.
- Returns those three results together.

```python
        for child in puzzle_neighbors(current.state):
            if child not in visited:
                heapq.heappush(
                    queue,
                    Node(
                        child,
                        parent=current,
                        h=heuristic(child)
                    )
                )
```

- Generates all legal one-move states.
- Ignores states already visited.
- Creates each child Node and stores `parent=current`, which is required later by `solution_path`.
- Calculates the selected heuristic for each child and pushes it into the heap.

```python
    return None
```

- Reports failure if no route reaches the goal.

## 12. Generic A* Search for the 8-Puzzle

```python
def astar_puzzle(start, heuristic):
    start_time=time.time()
    queue=[]
    visited=set()
```

- Defines A* with a start state and selectable heuristic.
- Records start time and creates the queue and visited set.

```python
    heapq.heappush(
        queue,
        Node(
            start,
            g=0,
            h=heuristic(start)
        )
    )
```

- Creates the start Node with cost zero and the selected heuristic.
- The Node calculates `f=g+h` and the heap uses that value.

```python
    expanded=0
```

- Starts the expansion counter.

```python
    while queue:
        current=heapq.heappop(queue)
        if current.state in visited:
            continue
        visited.add(current.state)
        expanded+=1
```

- Removes the smallest-`f` node while candidates remain.
- Skips duplicates, marks new states, and counts them.

```python
        if current.state==GOAL_STATE:
            return(
                solution_path(current),
                expanded,
                time.time()-start_time
            )
```

- Checks for the exact goal arrangement.
- On success, returns the reconstructed path, expansion count, and duration.

```python
        for child in puzzle_neighbors(current.state):
            if child not in visited:
                heapq.heappush(
                    queue,
                    Node(
                        child,
                        parent=current,
                        g=current.g+1,
                        h=heuristic(child)
                    )
                )
```

- Generates legal children and skips visited ones.
- Stores the current node as the child's parent.
- Increases the known path cost by one because one puzzle move was made.
- Calculates the heuristic and lets `Node` calculate `f=g+h`.
- Pushes the child into the heap.

```python
    return None
```

- Reports failure if the search cannot reach the goal.

## 13. Showing a Puzzle Solution

```python
def show_solution(path):
    print("\nTotal Moves:", len(path)-1)
```

- Defines a display function for a complete path.
- The number of moves is one less than the number of states because the first state is the starting position, not a move.

```python
    for step,state in enumerate(path):
        print("\nStep", step)
        print_puzzle(state)
```

- `enumerate(path)` supplies both a step number and its state.
- Prints the step number.
- Displays the corresponding puzzle board.

## 14. Running the 8-Puzzle Experiment

```python
def run_puzzle():
    start=(1,2,3,
           4,0,6,
           7,5,8)
```

- Defines the function called by menu option `2`.
- Creates the fixed starting tuple. The blank is in the center, tile `5` is below it, and tile `8` is in the bottom-right position.

```python
    print("\nInitial Puzzle:")
    print_puzzle(start)
```

- Prints a heading and displays the initial board.

```python
    print(
        "\nGreedy Best First Search"
        " using Misplaced Tiles"
    )
```

- Announces Greedy search using the misplaced-tile heuristic.
- Python automatically joins adjacent string literals, so the displayed text becomes one sentence.

```python
    result1=greedy_puzzle(
        start,
        misplaced_tiles
    )
```

- Runs the generic Greedy function.
- Passes the function object `misplaced_tiles`, not the result of calling it; `greedy_puzzle` calls it for each state.
- Stores the returned path, expansion count, and duration.

```python
    if result1:
        show_solution(result1[0])
        print("Expanded Nodes:", result1[1])
        print("Time:", result1[2], "seconds")
```

- Displays the result only if a solution was returned.
- Index `0` is the path, index `1` is expanded states, and index `2` is elapsed time.

```python
    print(
        "\nGreedy Best First Search"
        " using Manhattan Distance"
    )
```

- Announces a second Greedy run with a different heuristic.
- The adjacent strings again become one printed message.

```python
    result2=greedy_puzzle(
        start,
        manhattan_distance
    )
```

- Runs Greedy using Manhattan distance and stores its result.

```python
    if result2:
        show_solution(result2[0])
        print("Expanded Nodes:", result2[1])
        print("Time:", result2[2], "seconds")
```

- Displays the second Greedy result when it exists.

```python
    print("\nA* Search using Manhattan Distance")
```

- Announces the A* experiment.

```python
    result3=astar_puzzle(
        start,
        manhattan_distance
    )
```

- Runs A* with the Manhattan heuristic.
- A* includes the cost already spent, unlike the Greedy calls.

```python
    if result3:
        show_solution(result3[0])
        print("Expanded Nodes:", result3[1])
        print("Time:", result3[2], "seconds")
```

- Shows the A* path and performance values when successful.

## 15. Main Menu

```python
def main():
    while True:
```

- Defines the program's main controller function.
- `while True` creates a loop that continues until `break` is executed.

```python
        print(
            """
========================================
 Project-03 Heuristic Search
 N-Queens and 8-Puzzle
========================================

1. Solve N-Queens
2. Solve 8-Puzzle
3. Exit

"""
        )
```

- Prints a multi-line menu string.
- Triple quotes allow the menu to span multiple lines naturally.
- The three choices tell the user what input is expected.

```python
        choice=input(
            "Enter your choice: "
        )
```

- Displays the prompt and waits for keyboard input.
- The result is always text, such as the string `"1"`, not the integer `1`.

```python
        if choice=="1":
            run_nqueen()
```

- Compares the text with the string `"1"`.
- If equal, calls the N-Queens experiment.
- After that function returns, the `while` loop displays the menu again.

```python
        elif choice=="2":
            run_puzzle()
```

- `elif` means “otherwise, if.”
- Choice `"2"` calls the 8-Puzzle experiment.

```python
        elif choice=="3":
            print("Program terminated.")
            break
```

- Choice `"3"` prints a termination message.
- `break` immediately exits the infinite menu loop, ending `main`.

```python
        else:
            print("Invalid choice!")
```

- Handles every other input, such as `"4"` or `"x"`.
- Prints an error message and then loops back to show the menu again.

## 16. Starting the Program

```python
if __name__=="__main__":
    main()
```

- Python sets `__name__` to `"__main__"` when this file is run directly.
- The condition prevents `main()` from running automatically if another Python file imports this module.
- When run directly, `main()` starts the menu.

---

## Observed Execution: N-Queens With Four Queens

I ran the program with menu input `1`, number of queens `4`, and then menu input `3` to exit. The exact timing changes from run to run because it depends on the machine and current workload, but the meaningful output was:

```text
Enter number of queens:

Initial State:

 Q  .  .  .
 .  Q  .  .
 .  .  Q  .
 .  .  .  Q

Running Greedy Best First Search...

Greedy Solution:

 .  Q  .  .
 .  .  .  Q
 Q  .  .  .
 .  .  Q  .

Expanded Nodes: 7
Time: approximately 0.00008 seconds

Running A* Search...

A* Solution:

 .  Q  .  .
 .  .  .  Q
 Q  .  .  .
 .  .  Q  .

Expanded Nodes: 15
Time: approximately 0.00015 seconds

Program terminated.
```

### What the observed values mean

- Entering `4` created the initial state `[0, 1, 2, 3]`.
- The initial queens all lie on one diagonal, so many pairs conflict.
- The solution state is `[1, 3, 0, 2]`.
- Its rows use columns `1`, `3`, `0`, and `2`, and `queen_conflicts` returns `0`.
- Greedy expanded `7` states in this run.
- A* expanded `15` states in this run. A* is not guaranteed to expand fewer nodes here because this N-Queens formulation uses a conflict heuristic and treats every queen relocation as a move; the measured result is an experiment, not a universal ranking.
- Timing values are measurements, not fixed constants. Re-running can produce different decimal values.

### An input mistake to avoid

Entering `4` at the first menu prompt does **not** mean “four queens.” The menu accepts only `1`, `2`, or `3`. To solve four queens, enter `1` first and then enter `4` when the program asks for the number of queens.

## Short Concept Summary

- A **state** is one possible board arrangement.
- A **neighbor** is a state reachable by one allowed move.
- A **heuristic** is an estimate of how undesirable or far from the goal a state is.
- **Greedy** chooses the lowest `h` and ignores the path cost already used.
- **A*** chooses the lowest `f = g + h`.
- A **visited set** prevents repeated processing of the same state.
- A **parent link** lets the program reconstruct the 8-Puzzle route after reaching the goal.
