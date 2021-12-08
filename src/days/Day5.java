package days;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import models.FileReaderHelper;

public class Day5 {

  public static void main(String[] args) {
    System.out.println("Part One Answer: " + new Day5().hydrothermalVenturePartOne());
    System.out.println("Part Two Answer: " + new Day5().hydrothermalVenturePartTwo());
  }

  private int hydrothermalVenturePartOne() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day5/part-one-input.txt");
    List<PositionTraversed> positions = new ArrayList<>();
    int answer = 0;
    for (String line : lines) {
      String[] startAndEnd = line.split(" -> ");
      traverseLine(positions, startAndEnd);
      answer = countGreaterThanOne(positions);
    }
    return answer;
  }

  private int hydrothermalVenturePartTwo() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day5/part-two-input.txt");
    List<PositionTraversed> positions = new ArrayList<>();
    int answer = 0;
    for (String line : lines) {
      String[] startAndEnd = line.split(" -> ");
      traverseAllLines(positions, startAndEnd);
      answer = countGreaterThanOne(positions);
    }
    return answer;
  }

  private void traverseLine(List<PositionTraversed> positions, String[] startAndEndCoordinates) {
    String startX = startAndEndCoordinates[0].split(",")[0];
    String startY = startAndEndCoordinates[0].split(",")[1];
    String endX = startAndEndCoordinates[1].split(",")[0];
    String endY = startAndEndCoordinates[1].split(",")[1];

    if (startX.equals(endX)) {
      moveVertical(positions, new String[]{startX, startY, endX, endY});
    } else if (startY.equals(endY)){
      moveHorizontal(positions, new String[]{startX, startY, endX, endY});
    }
  }

  private void traverseAllLines(List<PositionTraversed> positions, String[] startAndEndCoordinates) {
    String startX = startAndEndCoordinates[0].split(",")[0];
    String startY = startAndEndCoordinates[0].split(",")[1];
    String endX = startAndEndCoordinates[1].split(",")[0];
    String endY = startAndEndCoordinates[1].split(",")[1];

    if (startX.equals(endX)) {
      moveVertical(positions, new String[]{startX, startY, endX, endY});
    } else if (startY.equals(endY)){
      moveHorizontal(positions, new String[]{startX, startY, endX, endY});
    } else {
      moveDiagonal(positions, new String[]{startX, startY, endX, endY});
    }
  }

  private void moveHorizontal(List<PositionTraversed> positions, String[] coordinates) {
    int smallerCoord;
    int largerCoord;
    if (Integer.parseInt(coordinates[0]) > Integer.parseInt(coordinates[2])) {
      smallerCoord = Integer.parseInt(coordinates[2]);
      largerCoord = Integer.parseInt(coordinates[0]);
    } else {
      smallerCoord = Integer.parseInt(coordinates[0]);
      largerCoord = Integer.parseInt(coordinates[2]);
    }
    for (int i = smallerCoord; i < largerCoord + 1; i++) {
      int finalI = i;
      List<PositionTraversed> checkPosition = positions.parallelStream()
          .filter(p -> p.getX().equals(Integer.toString(finalI)) && p.getY().equals(coordinates[1]))
          .collect(Collectors.toList());

      if (checkPosition.isEmpty()) {
        positions.add(new PositionTraversed(Integer.toString(i), coordinates[1], 1));
      } else {
        PositionTraversed increase = positions.parallelStream()
            .filter(p -> p.getX().equals(Integer.toString(finalI)) && p.getY().equals(coordinates[1]))
            .collect(Collectors.toList())
            .get(0);

        increase.setVisitedAmount(increase.getVisitedAmount() + 1);
      }
    }
  }

  private void moveVertical(List<PositionTraversed> positions, String[] coordinates) {
    int smallerCoord;
    int largerCoord;
    if (Integer.parseInt(coordinates[1]) > Integer.parseInt(coordinates[3])) {
      smallerCoord = Integer.parseInt(coordinates[3]);
      largerCoord = Integer.parseInt(coordinates[1]);
    } else {
      smallerCoord = Integer.parseInt(coordinates[1]);
      largerCoord = Integer.parseInt(coordinates[3]);
    }
    for (int i = smallerCoord; i < largerCoord + 1; i++) {
      int finalI = i;
      List<PositionTraversed> checkPosition = positions.parallelStream()
          .filter(p -> p.getX().equals(coordinates[0]) && p.getY().equals(Integer.toString(finalI)))
          .collect(Collectors.toList());

      if (checkPosition.isEmpty()) {
        positions.add(new PositionTraversed(coordinates[0], Integer.toString(i), 1));
      } else {
        PositionTraversed increase = positions.parallelStream()
            .filter(p -> p.getX().equals(coordinates[0]) && p.getY().equals(Integer.toString(finalI)))
            .collect(Collectors.toList())
            .get(0);

        increase.setVisitedAmount(increase.getVisitedAmount() + 1);
      }
    }

  }

  private void moveDiagonal(List<PositionTraversed> positions, String[] coordinates) {
    int smallerXCoord;
    int largerXCoord;
    int yCoord;
    int direction;
    // Moving left to right
    if (Integer.parseInt(coordinates[0]) > Integer.parseInt(coordinates[2])) {
      // Moving upwards
      if (Integer.parseInt(coordinates[1]) > Integer.parseInt(coordinates[3])) {
        largerXCoord = Integer.parseInt(coordinates[0]);
        smallerXCoord = Integer.parseInt(coordinates[2]);
        yCoord = Integer.parseInt(coordinates[3]);
        direction = 1;
      // Moving downwards
      } else {
        largerXCoord = Integer.parseInt(coordinates[0]);
        smallerXCoord = Integer.parseInt(coordinates[2]);
        yCoord = Integer.parseInt(coordinates[3]);
        direction = -1;
      }
    // Moving right to left
    } else {
      // Moving downwards
      if (Integer.parseInt(coordinates[1]) > Integer.parseInt(coordinates[3])) {
        largerXCoord = Integer.parseInt(coordinates[2]);
        smallerXCoord = Integer.parseInt(coordinates[0]);
        yCoord = Integer.parseInt(coordinates[1]);
        direction = -1;
        // Moving upwards
      } else {
        largerXCoord = Integer.parseInt(coordinates[2]);
        smallerXCoord = Integer.parseInt(coordinates[0]);
        yCoord = Integer.parseInt(coordinates[1]);
        direction = 1;
      }
    }
    for (int i = smallerXCoord; i < largerXCoord + 1; i++) {
      int finalI = i;
      int finalY = yCoord;
      List<PositionTraversed> checkPosition = positions.parallelStream()
          .filter(p -> p.getX().equals(Integer.toString(finalI)) && p.getY().equals(Integer.toString(finalY)))
          .collect(Collectors.toList());

      if (checkPosition.isEmpty()) {
        positions.add(new PositionTraversed(Integer.toString(finalI), Integer.toString(finalY), 1));
      } else {
        int finalYCoord = yCoord;
        PositionTraversed increase = positions.parallelStream()
            .filter(p -> p.getX().equals(Integer.toString(finalI)) && p.getY().equals(Integer.toString(
                finalYCoord)))
            .collect(Collectors.toList())
            .get(0);

        increase.setVisitedAmount(increase.getVisitedAmount() + 1);
      }
      yCoord = yCoord + direction;

    }
  }

  private int countGreaterThanOne(List<PositionTraversed> positions) {
    return positions.parallelStream()
        .filter(p -> p.getVisitedAmount() > 1)
        .collect(Collectors.toList())
        .size();
  }

  public class PositionTraversed {
    private String x;
    private String y;
    private int visitedAmount;

    public PositionTraversed(String x, String y, int visitedAmount) {
      this.x = x;
      this.y = y;
      this.visitedAmount = visitedAmount;
    }

    public String getX() {
      return x;
    }

    public void setX(String x) {
      this.x = x;
    }

    public String getY() {
      return y;
    }

    public void setY(String y) {
      this.y = y;
    }

    public int getVisitedAmount() {
      return visitedAmount;
    }

    public void setVisitedAmount(int visitedAmount) {
      this.visitedAmount = visitedAmount;
    }
  }

}
