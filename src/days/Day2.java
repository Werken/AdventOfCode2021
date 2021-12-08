package days;

import java.util.List;
import models.FileReaderHelper;

public class Day2 {

  public static void main(String[] args) {
    System.out.println("Part One Answer: " + new Day2().divePartOne());
    System.out.println("Part Two Answer: " + new Day2().divePartTwo());
  }

  private int divePartOne() {
    int depth = 0;
    int horizontal = 0;
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day2/part-one-input.txt");

    for (String line : lines) {
      int positionChange = Integer.parseInt(line.split(" ")[1]);
      if (line.contains("forward")) {
        horizontal += positionChange;
      } else if (line.contains("up")) {
        depth -= positionChange;
      } else {
        depth += positionChange;
      }
    }

    return depth * horizontal;
  }

  private int divePartTwo() {
    int depth = 0;
    int horizontal = 0;
    int aim = 0;
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day2/part-two-input.txt");

    for (String line : lines) {
      int positionChange = Integer.parseInt(line.split(" ")[1]);
      if (line.contains("forward")) {
        horizontal += positionChange;
        depth += aim * positionChange;
      } else if (line.contains("up")) {
        aim -= positionChange;
      } else {
        aim += positionChange;
      }
    }

    return depth * horizontal;
  }
}
