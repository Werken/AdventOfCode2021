package days;

import java.util.ArrayList;
import java.util.List;
import models.FileReaderHelper;

public class Day1 {

  private List<Integer> sonarData = new ArrayList<>();

  public static void main(String[] args) {
    System.out.println("Part One Answer: " + new Day1().sonarSweepPartOne());
    System.out.println("Part Two Answer: " + new Day1().sonarSweepPartTwo());
  }

  private int sonarSweepPartOne() {
    int compare = 0;
    int increaseCount = 0;
    FileReaderHelper fileReaderHelper = new FileReaderHelper();
    List<String> lines = fileReaderHelper.readFileAsString("src/resources/day1/part-one-input.txt");

    for (String line : lines) {
      int currentNumber = Integer.parseInt(line);
      if (currentNumber > compare && compare != 0) {
        increaseCount++;
      }
      compare = currentNumber;
    }

    return increaseCount;
  }

  private int sonarSweepPartTwo() {
    int dataSize = 0;
    int increaseCount = 0;
    int compare = 0;
    FileReaderHelper fileReaderHelper = new FileReaderHelper();
    List<String> lines = fileReaderHelper.readFileAsString("src/resources/day1/part-two-input.txt");

    for (String line : lines) {
      sonarData.add(Integer.parseInt(line));
      dataSize++;
    }

    int count = 0;
    while (count < dataSize - 2) {
      int sum = sumDataSet(sonarData, count);
      if (sum > compare && compare != 0) {
        increaseCount++;
      }
      count++;
      compare = sum;
    }

    return increaseCount;
  }

  private int sumDataSet(List<Integer> data, int index) {
    return data.get(index) + data.get(index + 1) + data.get(index + 2);
  }
}
