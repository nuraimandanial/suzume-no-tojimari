package TicTacToe;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GUI extends JFrame {
  private TicTacToe game;
  private JButton[][] buttons;
  private int size;

  public GUI(int sizes) {
    this.size = sizes;
    this.game = new TicTacToe(size);
    this.buttons = new JButton[size][size];

    setLayout(new GridLayout(size, size));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle(size + "x" + size + "Tic-Tac-Toe");
    setSize(600, 600);

    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        JButton button = new JButton();

        int finalRow = row;
        int finalCol = col;

        button.addActionListener(e -> {
          game.move(finalRow, finalCol, 1);
          button.setText("X");
          button.setEnabled(false);

          if (game.checkGameOver() == 0) {
            int[] computer = game.computer();
            buttons[computer[0]][computer[1]].setText("O");
            buttons[computer[0]][computer[1]].setEnabled(false);
          }

          int result = game.checkGameOver();
          if (result != 0) {
            endGame(result);
          }
        });

        buttons[row][col] = button;
        add(button);
      }
    }
  }

  private void endGame(int result) {
    for (JButton[] jButtons : buttons) {
      for (JButton jButton : jButtons) {
        jButton.setEnabled(false);
      }
    }

    String message = result == 1 ? "You win!" : result == 2 ? "You lose!" : "Draw!";
    JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
  }
}
