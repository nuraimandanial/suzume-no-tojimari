package TicTacToe;

public class TicTacToe {
  private int[][] board;
  private int size;
  private Engine engine;

  public TicTacToe(int size, Difficulty d) {
    this.size = size;
    this.board = new int[size][size];
    this.engine = new Engine(d);
  }

  public void move(int x, int y, int player) {
    board[x][y] = player;
  }

  public int[] computer() {
    int[] move = engine.move(board);
    move(move[0], move[1], 2);
    return move;
  }

  public int checkGameOver() {
    for (int i = 0; i < size; i++) {
      if (checkLine(board[i])) {
        return board[i][0];
      }

      int column[] = new int[size];
      for (int j = 0; j < size; j++) {
        column[j] = board[j][i];
      }

      if (checkLine(column)) {
        return column[0];
      }
    }

    int diagonal1[] = new int[size];
    int diagonal2[] = new int[size];
    for (int i = 0; i < size; i++) {
      diagonal1[i] = board[i][i];
      diagonal2[i] = board[i][size - i - 1];
    }
    if (checkLine(diagonal1))
      return diagonal1[0];
    if (checkLine(diagonal2))
      return diagonal2[0];

    for (int[] row : board) {
      for (int cell : row) {
        if (cell == 0) {
          return 0;
        }
      }
    }

    return 3;
  }

  private boolean checkLine(int[] line) {
    int first = line[0];

    if (first == 0) {
      return false;
    }

    for (int i = 1; i < line.length; i++) {
      if (line[i] != first) {
        return false;
      }
    }

    return true;
  }
}
