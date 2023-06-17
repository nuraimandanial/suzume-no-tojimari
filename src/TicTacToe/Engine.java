package TicTacToe;

import java.util.Random;

public class Engine {
  private Random random;
  private Difficulty difficulty;

  public Engine(Difficulty difficulty) {
    this.random = new Random();
    this.difficulty = difficulty;
  }

  public int[] move(int[][] board) {
    switch (difficulty) {
      case EASY:
        return easyMove(board);
      case MEDIUM:
        return mediumMove(board);
      case HARD:
        return hardMove(board);
      default:
        throw new IllegalStateException("Unexpected value: " + difficulty);
    }
  }

  private int[] easyMove(int[][] board) {
    int x, y;
    int size = board.length;

    do {
      x = random.nextInt(size);
      y = random.nextInt(size);
    } while (board[x][y] != 0);

    return new int[] { x, y };
  }

  private int[] mediumMove(int[][] board) {
    int size = board.length;

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (board[i][j] == 0) {
          board[i][j] = 2;
          if (gameWin(board, 2)) {
            board[i][j] = 0;
            return new int[] { i, j };
          }
          board[i][j] = 1;
          if (gameWin(board, 1)) {
            board[i][j] = 0;
            return new int[] { i, j };
          }
          board[i][j] = 0;
        }
      }
    }

    return easyMove(board);
  }

  private int[] hardMove(int[][] board) {
    int bestScore = Integer.MIN_VALUE;
    int[] move = new int[2];
    int size = board.length;

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (board[i][j] == 0) {
          board[i][j] = 2;
          int score = minimax(board, false);
          board[i][j] = 0;
          if (score > bestScore) {
            bestScore = score;
            move[0] = i;
            move[1] = j;
          }
        }
      }
    }

    return move;
  }

  private int minimax(int[][] board, boolean maximum) {
    int result = gameWin(board);

    if (result != 0) {
      if (result == 1) return -1;
      if (result == 2) return 1;
      if (result == 3) return 0;
    }

    if (maximum) {
      int bestScore = Integer.MIN_VALUE;
      int size = board.length;

      for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
          if (board[i][j] == 0) {
            board[i][j] = 2;
            int score = minimax(board, false);
            board[i][j] = 0;
            bestScore = Math.max(score, bestScore);
          }
        }
      }
      return bestScore;
    }
    else {
      int bestScore = Integer.MAX_VALUE;
      int size = board.length;

      for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
          if (board[i][j] == 0) {
            board[i][j] = 1;
            int score = minimax(board, true);
            board[i][j] = 0;
            bestScore = Math.min(score, bestScore);
          }
        }
      }
      return bestScore;
    }
  }

  private boolean gameWin(int[][] board, int player) {
    int size = board.length;

    for (int i = 0; i < size; i++) {
      int row = 0;
      int column = 0;

      for (int j = 0; j < size; j++) {
        row += board[i][j] == player ? 1 : 0;
        column += board[j][i] == player ? 1 : 0;
      }
      if (row == size || column == size) {
        return true;
      }
    }

    int diagonal1 = 0;
    int diagonal2 = 0;
    for (int i = 0; i < size; i++) {
      diagonal1 += board[i][i] == player ? 1 : 0;
      diagonal2 += board[i][size - i - 1] == player ? 1 : 0;
    }
    if (diagonal1 == size || diagonal2 == size) {
      return true;
    }

    return false;
  }

  private int gameWin(int[][] board) {
    int size = board.length;

    for (int i = 0; i < size; i++) {
      int row = 0;
      int column = 0;

      for (int j = 0; j < size; j++) {
        row += board[i][j];
        column += board[j][i];
      }
      if (row == size || column == size) {
        return 1;
      } else if (row == size * 2 || column == size * 2) {
        return 2;
      }
    }

    int diagonal1 = 0;
    int diagonal2 = 0;
    for (int i = 0; i < size; i++) {
      diagonal1 += board[i][i];
      diagonal2 += board[i][size - i - 1];
    }
    if (diagonal1 == size || diagonal2 == size) {
      return 1;
    } else if (diagonal1 == size * 2 || diagonal2 == size * 2) {
      return 2;
    }
    for (int[] rows : board) {
      for (int cell : rows) {
        if (cell == 0) {
          return 0;
        }
      }
    }

    return 3;
  }
}

enum Difficulty {
  EASY, MEDIUM, HARD
}
