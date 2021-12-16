package days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import models.FileReaderHelper;

public class Day13 {

  private List<Map<Integer, Integer>> coords = new ArrayList<>();
  private int maxX = 0;
  private int maxY = 0;

  public static void main(String[] args) {
    System.out.println("Part One Answer: " + new Day13().transparentOrigamiPartOne());
    System.out.println("Part Two Answer:");
    new Day13().transparentOrigamiPartTwo();
  }

  private int transparentOrigamiPartOne() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day13/part-one-input.txt");
    List<String> folds = new ArrayList<>();
    for (String line : lines) {
      if (line.contains(",")) {
        coords.add(new HashMap<Integer, Integer>(){{put(Integer.parseInt(line.split(",")[0]),
            Integer.parseInt(line.split(",")[1]));}});
      } else if (line.contains("=")) {
        folds.add(line.split("along ")[1]);
      }
    }

    foldPaper(folds.get(0));

    return coords.size();
  }

  private void transparentOrigamiPartTwo() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day13/part-two-input.txt");
    List<String> folds = new ArrayList<>();
    for (String line : lines) {
      if (line.contains(",")) {
        coords.add(new HashMap<Integer, Integer>(){{put(Integer.parseInt(line.split(",")[0]),
            Integer.parseInt(line.split(",")[1]));}});
      } else if (line.contains("=")) {
        folds.add(line.split("along ")[1]);
      }
    }

    for (String fold : folds) foldPaper(fold);

    printCode();
  }

  private void printCode() {
    for (Map<Integer, Integer> coord : coords) {
      if (coord.entrySet().iterator().next().getKey() > maxX)
        maxX = coord.entrySet().iterator().next().getKey() + 1;
      if (coord.entrySet().iterator().next().getValue() > maxY)
        maxY = coord.entrySet().iterator().next().getValue() + 1;
    }

    for (int i = 0; i < maxY; i++) {
      for (int j = 0; j < maxX; j++) {
        int finalJ = j;
        int finalI = i;
        if (coords.parallelStream()
            .filter(coord -> coord.entrySet().iterator().next().getKey().equals(finalJ) &&
                coord.entrySet().iterator().next().getValue().equals(finalI))
            .collect(Collectors.toList()).size() == 0) System.out.print(".");
        else System.out.print("#");
      }
      System.out.println();
    }
  }

  private void foldPaper(String instructions) {
    List<Map<Integer, Integer>> toRemove = new ArrayList<>();
    List<Map<Integer, Integer>> toAdd = new ArrayList<>();
    int fold = Integer.parseInt(instructions.split("=")[1]);
    for (Map<Integer, Integer> coord : coords) {
      final Integer x = coord.entrySet().iterator().next().getKey();
      final Integer y = coord.entrySet().iterator().next().getValue();
      if (instructions.contains("y")) {
        if (y == fold)
          toRemove.add(coord);
        if (y > fold) {
          int diff = y - fold;
          if (coords.stream()
              .filter(c -> c.entrySet().iterator().next().getValue().equals(fold - diff) &&
                  c.entrySet().iterator().next().getKey().equals(x))
              .collect(Collectors.toList()).size() == 0) {
            toAdd.add(new HashMap<Integer, Integer>() {{
              put(x, fold - diff);
            }});
          }
          toRemove.add(coord);
        }
      } else {
        if (x == fold)
          toRemove.add(coord);
        if (x > fold) {
          int diff = x - fold;
          if (coords.stream()
              .filter(c -> c.entrySet().iterator().next().getKey().equals(fold - diff) &&
                  c.entrySet().iterator().next().getValue().equals(y))
              .collect(Collectors.toList()).size() == 0) {
            toAdd.add(new HashMap<Integer, Integer>() {{
              put(fold - diff, y);
            }});
          }
          toRemove.add(coord);
        }
      }
    }
    for (Map<Integer, Integer> remove : toRemove) {
      coords.remove(remove);
    }
    for (Map<Integer, Integer> add : toAdd) {
      coords.add(add);
    }
  }
}
