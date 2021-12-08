package days;

import java.util.List;
import models.FileReaderHelper;

public class Day7 {

  public static void main(String[] args) {
    System.out.println("Part One Answer: " + new Day7().whalesPartOne());
    System.out.println("Part Two Answer: " + new Day7().whalesPartTwo());
  }

  private int whalesPartOne() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day7/part-one-input.txt");
    String[] crabs = lines.get(0).split(",");
    int mean = findMean(crabs);
    int sum = findSumOfSquareOfDistance(crabs, mean);
    int standDev = (int)Math.sqrt(sum / crabs.length);

    return findShortestLevel(crabs, mean, standDev, 1);
  }

  private int whalesPartTwo() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day7/part-two-input.txt");
    String[] crabs = lines.get(0).split(",");
    int mean = findMean(crabs);
    int sum = findSumOfSquareOfDistance(crabs, mean);
    int standDev = (int)Math.sqrt(sum / crabs.length);

    return findShortestLevel(crabs, mean, standDev, 2);
  }

  private int findShortestLevel(String[] crabs, int mean, int sd, int part) {
    int start = mean - sd;
    int lowestFuel = Integer.MAX_VALUE;
    for (int i = start; i < mean + sd; i++) {
      int fuel = getFuelCost(crabs, i, part);
      if (lowestFuel > fuel) {
        lowestFuel = fuel;
      }
    }
    return lowestFuel;
  }

  private int getFuelCost(String[] crabs, int level, int part) {
    int fuel = 0;
    int distance;
    for (String crab : crabs) {
      distance = Math.abs(Integer.parseInt(crab) - level);
      if (part == 1) {
        fuel += distance;
      } else {
        fuel += (distance * (distance + 1)) / 2;
      }
    }

    return fuel;
  }

  private int findMean(String[] crabs) {
    int mean = 0;

    for (String crab : crabs) {
      mean += Integer.parseInt(crab);
    }
    return mean / crabs.length;
  }

  private int findSumOfSquareOfDistance(String[] crabs, int mean) {
    int sum = 0;
    for (String crab : crabs) {
      sum += Math.pow(Integer.parseInt(crab) - mean, 2);
    }
    return sum;
  }

}
