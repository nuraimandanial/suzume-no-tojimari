package Suzume;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import TicTacToe.*;

public class MapInterface extends JFrame {
  private static final int TILE_SIZE = 18;

  private Node[][] map;
  private File directory;

  private int currentX = 0;
  private int currentY = 0;
  private int index;

  private ArrayList<String> directions;
  private HashMap<Integer, BufferedImage> imageCache;
  private Timer timer;
  private Difficulty difficulty;
  private Integer number;
  private List<List<String>> allPaths;

  public MapInterface(Node[][] map, File directory, List<List<String>> paths) {
    this.map = map;
    this.directory = directory;
    imageCache = new HashMap<>();
    this.allPaths = paths;

    for (int i = 0; i <= 5; i++) {
      imageCache.put(i, getImageForValue(i));
    }

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Map GUI");

    JPanel panel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < map.length; row++) {
          for (int col = 0; col < map[0].length; col++) {
            int value = map[row][col].getValue();
            BufferedImage image = imageCache.get(value == -1 ? 4 : value);

            g.drawImage(image, col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
          }
        }

        // Draw the current position
        g.setColor(Color.BLACK);
        g.fillRect(currentY * TILE_SIZE, currentX * TILE_SIZE, TILE_SIZE, TILE_SIZE);
      }

      @Override
      public Dimension getPreferredSize() {
        return new Dimension(map[0].length * TILE_SIZE, map.length * TILE_SIZE);
      }
    };

    getContentPane().add(panel);
    pack();
    setLocationRelativeTo(null);
    selectDifficulties();
  }

  private BufferedImage getImageForValue(int value) {
    if (value == 4) {
      value = -1;
    }
    String imagePath = "\\Texture\\" + value + ".jpg";

    try {
      return ImageIO.read(new File(directory, imagePath));
    } catch (IOException e) {
      System.out.println("Err");
      e.printStackTrace();
      return null;
    }
  }

  public void readPath(List<List<String>> paths, int number) {
    this.directions = new ArrayList<>(paths.get(number - 1));
    index = 0;
    startMoving();
  }

  private void selectDifficulties() {
    JFrame frame = new JFrame("Select Difficulties and Path");
    frame.setLayout(new FlowLayout());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(300, 100);
    frame.setLocationRelativeTo(null);

    JComboBox<Difficulty> d = new JComboBox<>(Difficulty.values());
    d.setSelectedIndex(-1);
    frame.add(d);

    Integer[] list = new Integer[allPaths.size() + 1];
    for (int i = 1; i <= allPaths.size(); i++) list[i] = i;
    JComboBox<Integer> l= new JComboBox<>(list);
    l.setSelectedIndex(-1);
    frame.add(l);

    JButton button = new JButton("OK");
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        difficulty = (Difficulty) d.getSelectedItem();
        number = (Integer) l.getSelectedItem();
        frame.dispose();
        readPath(allPaths, (int) number);
      }
    });
    frame.add(button);

    frame.setVisible(true);
  }

  private void startMoving() {
    timer = new Timer(400, new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        String direction = directions.get(index);
        switch (direction) {
          case "Left":
            moveLeft();
            break;
          case "Right":
            moveRight();
            break;
          case "Up":
            moveUp();
            break;
          case "Down":
            moveDown();
            break;
        }
        index++;
        if (index >= directions.size() || map[currentX][currentY].getValue() == 2) {
          ((Timer) e.getSource()).stop();
        }
        repaint();
      }
    });

    timer.start();
  }

  private void moveLeft() {
    if (currentY - 1 >= 0) {
      if (map[currentX][currentY].getValue() == 2) {
        map[currentX][currentY].setValue(5);
      }
      else map[currentX][currentY].setValue(-1);
      currentY--;
      checkNodeValue();
    }
  }

  private void moveRight() {
    if (currentY + 1 < map[0].length) {
      if (map[currentX][currentY].getValue() == 2) {
        map[currentX][currentY].setValue(5);
      }
      else map[currentX][currentY].setValue(-1);
      currentY++;
      checkNodeValue();
    }
  }

  private void moveDown() {
    if (currentX + 1 < map.length) {
      if (map[currentX][currentY].getValue() == 2) {
        map[currentX][currentY].setValue(5);
      }
      else map[currentX][currentY].setValue(-1);
      currentX++;
      checkNodeValue();
    }
  }

  private void moveUp() {
    if (currentX - 1 >= 0) {
      if (map[currentX][currentY].getValue() == 2) {
        map[currentX][currentY].setValue(5);
      }
      else map[currentX][currentY].setValue(-1);
      currentX--;
      checkNodeValue();
    }
  }

  private void checkNodeValue() {
    if (map[currentX][currentY].getValue() == 2) {
      reachedStations();
    }
    if (map[currentX][currentY].getValue() == 3) {
      return;
    }
  }

  private void reachedStations() {
    GUI gui = new GUI(3, difficulty);
    gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    gui.setSize(500, 500);
    gui.setLocationRelativeTo(null);

    gui.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        startMoving();
      }
    });

    gui.setVisible(true);
  }
}
