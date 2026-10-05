# Tic-Tac-Toe with Minimax and Alpha-Beta Pruning

This document explains the code in the Tic-Tac-Toe project in a simple beginner-friendly way.

## What this program does

This program creates a Tic-Tac-Toe game where:

- You play as X
- The computer plays as O
- The computer uses the Minimax algorithm to choose a smart move
- Alpha-Beta pruning helps the computer search faster

---

## 1. The first lines

```python
#!/usr/bin/env python3
"""Beginner-friendly Tic-Tac-Toe game with Minimax and Alpha-Beta Pruning."""
```

- The first line tells the system to use Python when the file is run directly.
- The second line is a short description of the program.
- These lines are useful for understanding the file quickly.

---

## 2. Importing math

```python
import math
```

- This imports Python's math module.
- We need it because the AI uses very large values such as infinity during the search.

---

## 3. The print_board function

```python
def print_board(board):
    """Display the board in a simple 3x3 format."""
    for i in range(0, 9, 3):
        print(f"{board[i]} | {board[i + 1]} | {board[i + 2]}")
        if i < 6:
            print("--+---+--")
    print()
```

### What this function does

- It shows the current board to the player.
- It prints the board in 3 rows and 3 columns.

### Why it is important

- Without this function, the game would not be visible to the player.
- It makes the game easier to read.

### Line-by-line explanation

- `def print_board(board):` creates a function that accepts a board list.
- `for i in range(0, 9, 3):` loops through row positions.
- `print(f"{board[i]} | {board[i + 1]} | {board[i + 2]}")` prints one row.
- `if i < 6:` adds a separator line between rows.
- `print()` adds a blank line after the board.

---

## 4. The check_winner function

```python
def check_winner(board, player):
    """Return True if the given player has a winning line."""
    winning_combinations = [
        [0, 1, 2], [3, 4, 5], [6, 7, 8],
        [0, 3, 6], [1, 4, 7], [2, 5, 8],
        [0, 4, 8], [2, 4, 6]
    ]
    return any(all(board[pos] == player for pos in combo) for combo in winning_combinations)
```

### What this function does

- It checks whether a player has three matching symbols in a row.
- It checks rows, columns, and diagonals.

### Why it is important

- This tells the game when someone has won.

### Important idea

A player wins if one of these patterns is complete:

- Top row: 0, 1, 2
- Middle row: 3, 4, 5
- Bottom row: 6, 7, 8
- Left column: 0, 3, 6
- Middle column: 1, 4, 7
- Right column: 2, 5, 8
- Main diagonal: 0, 4, 8
- Other diagonal: 2, 4, 6

---

## 5. The get_available_moves function

```python
def get_available_moves(board):
    """Return all empty positions on the board."""
    return [i for i, cell in enumerate(board) if cell == " "]
```

### What this function does

- It finds all empty positions on the board.

### Why it is important

- The player and the computer must know where they are allowed to move.

### Simple explanation

- The loop checks every cell.
- If the cell is empty, its index is added to the list.

---

## 6. The is_board_full function

```python
def is_board_full(board):
    """Return True if there is no empty space left."""
    return " " not in board
```

### What this function does

- It checks whether the board has no empty cells left.

### Why it is important

- If the board is full and nobody has won, the game is a draw.

---

## 7. The evaluate_board function

```python
def evaluate_board(board):
    """Evaluate the board state.

    Positive values mean the AI (O) is winning.
    Negative values mean the human (X) is winning.
    Zero means the game is still even.
    """
    if check_winner(board, "O"):
        return 10
    if check_winner(board, "X"):
        return -10
    return 0
```

### What this function does

- It gives the board a score.
- A high positive score means the computer is winning.
- A negative score means the human is winning.
- Zero means the board is balanced.

### Why it is important

- The AI needs a way to judge whether a position is good or bad.

---

## 8. The minimax function

```python
def minimax(board, depth, is_maximizing, alpha, beta):
    """Return the best score for the current board state."""
    score = evaluate_board(board)
    if score != 0:
        return score - depth if score > 0 else score + depth

    if is_board_full(board):
        return 0

    if is_maximizing:
        best_score = -math.inf
        for move in get_available_moves(board):
            board[move] = "O"
            score = minimax(board, depth + 1, False, alpha, beta)
            board[move] = " "
            best_score = max(best_score, score)
            alpha = max(alpha, best_score)
            if beta <= alpha:
                break
        return best_score

    best_score = math.inf
    for move in get_available_moves(board):
        board[move] = "X"
        score = minimax(board, depth + 1, True, alpha, beta)
        board[move] = " "
        best_score = min(best_score, score)
        beta = min(beta, best_score)
        if beta <= alpha:
            break
    return best_score
```

### What this function does

- It is the heart of the AI.
- It looks ahead into the future and tries to choose the best move.

### Why it is important

- This is how the computer becomes smart.

### Simple explanation

- If it is the AI turn, the AI tries to maximize the score.
- If it is the human turn, the human tries to minimize the score.
- The function looks several moves ahead before deciding.

### Alpha-Beta pruning

- Alpha-Beta pruning avoids checking moves that are obviously bad.
- This makes the search faster.

---

## 9. The get_best_move function

```python
def get_best_move(board):
    """Choose the best move for the AI using Minimax."""
    best_score = -math.inf
    best_move = None

    for move in get_available_moves(board):
        board[move] = "O"
        score = minimax(board, 0, False, -math.inf, math.inf)
        board[move] = " "

        if score > best_score:
            best_score = score
            best_move = move

    return best_move
```

### What this function does

- It picks the actual move that the computer should play.

### Why it is important

- The Minimax function gives scores, but this function turns those scores into a real move choice.

---

## 10. The play_game function

```python
def play_game():
    """Run the full Tic-Tac-Toe game."""
    board = [" "] * 9
    current_player = "X"
```

### What this function does

- It starts and runs the full game.

### Why it is important

- This is the main part of the program.

### Important lines

- `board = [" "] * 9` creates the 3x3 board.
- `current_player = "X"` means the human starts first.

---

## 11. Printing the game instructions

```python
print("Welcome to Tic-Tac-Toe")
print("You are X. The computer is O.")
print("Choose a number from 1 to 9 based on the board positions below:")
print("1 | 2 | 3")
print("--+---+--")
print("4 | 5 | 6")
print("--+---+--")
print("7 | 8 | 9")
print()
```

### What this does

- It shows the game title and instructions.
- It tells the user how to choose a move.

---

## 12. The main game loop

```python
while True:
    print_board(board)
```

### What this does

- This loop keeps the game running until someone wins or the board is full.

### Why it is important

- The program needs to repeat the game steps many times.

---

## 13. Human turn logic

```python
if current_player == "X":
    try:
        move = int(input("Your turn. Enter a position (1-9): ")) - 1
    except ValueError:
        print("Please enter a valid number.")
        continue

    if move not in range(9) or board[move] != " ":
        print("That position is not available. Try again.")
        continue

    board[move] = "X"
```

### What this section does

- It asks the user to enter a move.
- It checks whether the move is valid.
- If valid, it places X on the board.

### Why it is important

- The human must be able to play correctly.

---

## 14. Computer turn logic

```python
else:
    print("Computer is thinking...")
    move = get_best_move(board)
    board[move] = "O"
    print(f"Computer chose position {move + 1}")
```

### What this section does

- It lets the computer choose its move.
- It uses the AI function to decide the best move.

---

## 15. Checking the result

```python
if check_winner(board, "X"):
    print_board(board)
    print("You win!")
    break

if check_winner(board, "O"):
    print_board(board)
    print("Computer wins!")
    break

if is_board_full(board):
    print_board(board)
    print("It is a draw!")
    break
```

### What this does

- It checks whether the human won, the computer won, or the game ended in a draw.

### Why it is important

- The game must stop at the correct time.

---

## 16. Switching turns

```python
current_player = "O" if current_player == "X" else "X"
```

### What this does

- It changes the turn from X to O or from O to X.

### Why it is important

- This makes the game alternate between players.

---

## 17. Starting the program

```python
if __name__ == "__main__":
    play_game()
```

### What this does

- It starts the game when the program is run.

### Why it is important

- Without this, the game would not begin automatically.

---

## Final summary

This Tic-Tac-Toe project teaches several important ideas:

- How to build a simple game in Python
- How to use functions to organize code
- How to detect wins and draws
- How the Minimax algorithm works
- How Alpha-Beta pruning improves performance

If you want, I can also turn this into a more polished classroom-style note with headings such as:

- Objective
- Concepts used
- Program flow
- Output explanation
- Viva questions
