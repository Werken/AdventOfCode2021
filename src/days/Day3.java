package days;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.FileReaderHelper;

public class Day3 {

  public static void main(String[] args) {
    System.out.println("Part One Answer: " + new Day3().binaryDiagnosticPartOne());
    System.out.println("Part Two Answer: " + new Day3().binaryDiagnosticPartTwo());
  }

  private int binaryDiagnosticPartOne() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day3/part-one-input.txt");
    Map<Integer, Map<Integer, Integer>> map = createBinaryMap(new HashMap<>(), lines);

    String gammaRate = "";
    String epsilonRate = "";
    for (int i = 0; i < map.size(); i++) {
      if (map.get(i).get(0) > map.get(i).get(1)) {
        gammaRate += "0";
        epsilonRate += "1";
      } else {
        gammaRate += "1";
        epsilonRate += "0";
      }
    }

    return Integer.parseInt(gammaRate, 2) * Integer.parseInt(epsilonRate, 2);
  }

  private int binaryDiagnosticPartTwo() {
    List<String> oxygenList = new FileReaderHelper().readFileAsString("src/resources/day3/part-two-input.txt");
    List<String> co2List = new FileReaderHelper().readFileAsString("src/resources/day3/part-two-input.txt");

    return oxygenGeneratorRating(oxygenList) * co2ScrubberRating(co2List);
  }

  private int oxygenGeneratorRating(List<String> lines) {
    for (int i = 0; i < lines.get(0).length(); i++) {
      if (lines.size() != 1) {
        Map<Integer, Map<Integer, Integer>> map = createBinaryMap(new HashMap<>(), lines);
        int index = i;
        if (map.get(i).get(0) > map.get(i).get(1)) {
          lines.removeIf(l -> l.charAt(index) == '1');
        } else if (map.get(i).get(0) == map.get(i).get(1)) {
          lines.removeIf(l -> l.charAt(index) == '0');
        } else {
          lines.removeIf(l -> l.charAt(index) == '0');
        }
      }
    }

    return Integer.parseInt(lines.get(0), 2);
  }

  private int co2ScrubberRating(List<String> lines) {
    for (int i = 0; i < lines.get(0).length(); i++) {
      if (lines.size() != 1) {
        Map<Integer, Map<Integer, Integer>> map = createBinaryMap(new HashMap<>(), lines);
        int index = i;
        if (map.get(i).get(0) > map.get(i).get(1)) {
          lines.removeIf(l -> l.charAt(index) == '0');
        } else if (map.get(i).get(0) == map.get(i).get(1)) {
          lines.removeIf(l -> l.charAt(index) == '1');
        } else {
          lines.removeIf(l -> l.charAt(index) == '1');
        }
      }
    }

    return Integer.parseInt(lines.get(0), 2);
  }

  private Map<Integer, Map<Integer, Integer>> createBinaryMap(Map<Integer, Map<Integer, Integer>> map, List<String> lines) {
    for (String line : lines) {
      for (int i = 0; i < line.length(); i++) {
        if (map.get(i) == null) {
          map.put(i, new HashMap<Integer, Integer>() {{
            put(0, 0);
            put(1, 0);
          }});
        }

        if (line.charAt(i) == '0') {
          map.get(i).put(0, map.get(i).get(0) + 1);
        } else {
          map.get(i).put(1, map.get(i).get(1) + 1);
        }
      }
    }

    return map;
  }
}
