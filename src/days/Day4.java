package days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.FileReaderHelper;

public class Day4 {

  private Map<Integer, List<List<BingoEntry>>> boards = new HashMap<>();
  private List<Integer> boardsToIgnore = new ArrayList<>();
  private int sumOfBoard;

  public static void main(String[] args) {
    System.out.println("Part One Answer: " + new Day4().giantSquidPartOne());
    System.out.println("Part Two Answer: " + new Day4().giantSquidPartTwo());
  }

  private int giantSquidPartOne() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day4/part-one-input.txt");
    List<String> bingoNumbers = Arrays.asList(lines.get(0).split(","));
    lines.remove(0);
    lines.remove(0);
    generateBoards(lines);

    for (int i = 0; i < bingoNumbers.size(); i++) {
      checkIfNumberIsOnBoard(bingoNumbers.get(i));
      if (i > 3 && checkForWinningBoard()) {
        return Integer.parseInt(bingoNumbers.get(i)) * sumOfBoard;
      }
    }

    return -1;
  }

  private void generateBoards(List<String> lines) {
    int boardCount = 0;
    int rowCount = 0;
    List<List<BingoEntry>> boardRows = new ArrayList<>();
    for (String line : lines) {
      if (rowCount == 5) {
        boards.put(boardCount, boardRows);
        boardCount++;
        rowCount = 0;
        boardRows = new ArrayList<>();
      } else {
        List<BingoEntry> boardRow = new ArrayList<>();
        for (String entry : Arrays.asList(line.trim().replaceAll("  ", " ").split(" "))) {
          boardRow.add(new BingoEntry(entry, 0));
        }
        boardRows.add(boardRow);
        rowCount++;
      }
    }
  }

  private void checkIfNumberIsOnBoard(String number) {
    // Loops through each board
    for (int i = 0; i < boards.size(); i++) {
      // List of rows in board
      List<List<BingoEntry>> rows = boards.get(i);
      // Loops through each row
      for (int j = 0; j < rows.size(); j++) {
        // List of numbers in each row
        List<BingoEntry> entries = rows.get(j);
        // Loops through each number
        for (int k = 0; k < entries.size(); k++) {
          if (entries.get(k).getNumber().equals(number)) {
            entries.get(k).setMarked(1);
          }
        }
      }
    }
  }

  private boolean checkForWinningBoard() {
    // Loops through each board
    for (int i = 0; i < boards.size(); i++) {
      // List of rows in board
      if (checkHorizontal(boards.get(i)) || checkVertical(boards.get(i))) {
        sumBoard(boards.get(i));
        return true;
      }
    }
    return false;
  }

  private void removeWinningBoards() {
    // Loops through each board
    for (int i = 0; i < boards.size(); i++) {
      // List of rows in board
      if ((checkHorizontal(boards.get(i)) || checkVertical(boards.get(i))) &&
          !boardsToIgnore.contains(i)) {
        boardsToIgnore.add(i);
      }
    }
  }

  private void sumBoard (List<List<BingoEntry>> board) {
    // Loops through each row
    for (int i = 0; i < board.size(); i++) {
      // List of numbers in each row
      List<BingoEntry> entries = board.get(i);
      // Loops through each number
      for (int j = 0; j < entries.size(); j++) {
        if (entries.get(j).getMarked() == 0) {
          sumOfBoard += Integer.parseInt(entries.get(j).getNumber());
        }
      }
    }
  }

  private boolean checkHorizontal(List<List<BingoEntry>> board) {
    int winCount = 0;
    // Loops through each row
    for (int i = 0; i < board.size(); i++) {
      // List of numbers in each row
      List<BingoEntry> entries = board.get(i);
      // Loops through each number
      for (int j = 0; j < entries.size(); j++) {
        if (entries.get(j).getMarked() == 1) winCount++;
      }

      if (winCount == 5) {
        return true;
      }
      winCount = 0;
    }

    return false;
  }

  private boolean checkVertical(List<List<BingoEntry>> board) {
    int[] winCount = new int[]{0, 0, 0, 0, 0};
    // Loops through each row
    for (int i = 0; i < board.size(); i++) {
      // List of numbers in each row
      List<BingoEntry> entries = board.get(i);
      // Loops through each number
      for (int j = 0; j < entries.size(); j++) {
        if (entries.get(j).getMarked() == 1) winCount[j]++;
      }
    }

    for (int i = 0; i < winCount.length; i++) {
      if (winCount[i] == 5) {
        return true;
      }
    }

    return false;
  }

  public void printBoard (List<List<BingoEntry>> board) {
    for (int i = 0; i <  board.size(); i++) {
      List<BingoEntry> entries = board.get(i);
      for (int j = 0; j < entries.size(); j++) {
        System.out.print(board.get(i).get(j).getNumber() + ":" + board.get(i).get(j).getMarked() + " ");
      }
      System.out.println();
    }
    System.out.println();
  }

  private int giantSquidPartTwo() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day4/part-two-input.txt");
    List<String> bingoNumbers = Arrays.asList(lines.get(0).split(","));
    lines.remove(0);
    lines.remove(0);
    generateBoards(lines);
    int indexOfLastBoard = 0;
    for (int i = 0; i < bingoNumbers.size(); i++) {
      checkIfNumberIsOnBoard(bingoNumbers.get(i));
      removeWinningBoards();
      if (boardsToIgnore.size() == boards.size() - 1) {
        for (int j = 0; j < boards.size(); j++) {
          if (!boardsToIgnore.contains(j)) {
            indexOfLastBoard = j;
          }
        }
      } else if (boardsToIgnore.size() == boards.size()) {
        sumBoard(boards.get(indexOfLastBoard));
        return Integer.parseInt(bingoNumbers.get(i)) * sumOfBoard;
      }
    }

    return -1;
  }

  public class BingoEntry {
    private String number;
    private int marked;

    public BingoEntry(String number, int marked) {
      this.number = number;
      this.marked = marked;
    }

    private String getNumber() {
      return number;
    }

    private void setNumber(String number) {
      this.number = number;
    }

    private int getMarked() {
      return marked;
    }

    private void setMarked(int marked) {
      this.marked = marked;
    }
  }
}
