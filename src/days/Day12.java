package days;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import models.FileReaderHelper;

public class Day12 {

  private static final String START = "start";
  private static final String END = "end";

  public static void main(String[] args) {
    System.out.println("Part One Answer: " + new Day12().passagePathingPartOne());
    System.out.println("Part Two Answer: " + new Day12().passagePathingPartTwo());
  }

  private int passagePathingPartOne() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day12/part-one-input.txt");
    Map<String, List<String>> map = new HashMap<>();

    lines.stream()
        .map(line -> line.split("-"))
        .forEach(path -> {
          map.computeIfAbsent(path[0], key -> new ArrayList<>()).add(path[1]);
          map.computeIfAbsent(path[1], key -> new ArrayList<>()).add(path[0]);
        });

    List<List<String>> paths = findPaths(START, END, map, Collections.emptyList(), false);

    return paths.size();
  }

  private int passagePathingPartTwo() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day12/part-two-input.txt");
    Map<String, List<String>> map = new HashMap<>();

    lines.stream()
        .map(line -> line.split("-"))
        .forEach(path -> {
          map.computeIfAbsent(path[0], key -> new ArrayList<>()).add(path[1]);
          map.computeIfAbsent(path[1], key -> new ArrayList<>()).add(path[0]);
        });

    List<List<String>> paths = findPaths(START, END, map, Collections.emptyList(), true);

    return paths.size();
  }

  private List<List<String>> findPaths(String start, String end, Map<String, List<String>> map,
      List<String> visited, boolean canDoubleVisitSmallCave) {
    if (start.equals(end)) {
      return Collections.singletonList(Collections.singletonList(start));
    }

    List<String> currentPath = new ArrayList<>(visited);
    currentPath.add(start);

    boolean isSecondSmallCaveVisit = start.toLowerCase().equals(start) && visited.contains(start);

    List<String> visitable = map.get(start).stream()
        .filter(cave -> !START.equals(cave))
        .filter(cave -> cave.toUpperCase().equals(cave) || !visited.contains(cave) ||
            (!isSecondSmallCaveVisit && canDoubleVisitSmallCave))
        .collect(Collectors.toList());

    List<List<String>> paths = new ArrayList<>();

    visitable.forEach(cave -> paths.addAll(findPaths(cave, end, map, currentPath,
        !isSecondSmallCaveVisit && canDoubleVisitSmallCave)));

    return paths;
  }

}
