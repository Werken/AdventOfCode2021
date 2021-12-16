package days;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import models.FileReaderHelper;

public class Day15 {

  public static void main(String[] args) {
    System.out.println("Part One Answer: " + new Day15().chitonPartOne());
    System.out.println("Part Two Answer: " + new Day15().chitonPartTwo());
  }

  private String chitonPartOne() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day15/part-one-input.txt");
    List<List<Chiton>> caveSystem = generateCave(lines.size(), lines.get(0).length(), lines);

    parseLocations(lines, caveSystem);

    calculateShortestPathFromSource(caveSystem.get(0).get(0));
    return caveSystem.get(lines.size() - 1).get(lines.get(0).length() - 1).getDistance().toString();
  }

  private String chitonPartTwo() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day15/part-two-input.txt");
    List<List<Chiton>> caveSystem = generateCave(lines.size(), lines.get(0).length(), lines);
    List<List<Chiton>> largerCaveSystem = generateLargerCave(caveSystem);

    parseLocationsForLargerCave(largerCaveSystem);

    calculateShortestPathFromSource(largerCaveSystem.get(0).get(0));
    return largerCaveSystem.get(largerCaveSystem.size() - 1).get(largerCaveSystem.get(0).size() - 1).getDistance().toString();
  }

  private void parseLocationsForLargerCave(List<List<Chiton>> caveSystem) {
    for (int i = 0; i < caveSystem.size(); i++) {
      for (int j = 0; j < caveSystem.get(i).size(); j++) {
        if (j < caveSystem.get(i).size() - 1 &&
            !caveSystem.get(i).get(j).getAdjacentNodes()
                .containsKey(caveSystem.get(i).get(j + 1))) {
          caveSystem.get(i).get(j).addDestination(caveSystem.get(i).get(j + 1),
              caveSystem.get(i).get(j + 1).getInitialValue());
        }
        if (i < caveSystem.size() - 1 &&
            !caveSystem.get(i).get(j).getAdjacentNodes()
                .containsKey(caveSystem.get(i + 1).get(j))) {
          caveSystem.get(i).get(j).addDestination(caveSystem.get(i + 1).get(j),
              caveSystem.get(i + 1).get(j).getInitialValue());

        }
        if (i != 0 &&
            !caveSystem.get(i).get(j).getAdjacentNodes()
                .containsKey(caveSystem.get(i - 1).get(j))) {
          caveSystem.get(i).get(j).addDestination(caveSystem.get(i - 1).get(j),
              caveSystem.get(i - 1).get(j).getInitialValue());

        }
        if (j != 0 &&
            !caveSystem.get(i).get(j).getAdjacentNodes()
                .containsKey(caveSystem.get(i).get(j - 1))) {
          caveSystem.get(i).get(j).addDestination(caveSystem.get(i).get(j - 1),
              caveSystem.get(i).get(j - 1).getInitialValue());
        }
      }
    }
  }

  private void parseLocations(List<String> lines, List<List<Chiton>> caveSystem) {
    for (int i = 0; i < caveSystem.size(); i++) {
      for (int j = 0; j < caveSystem.get(i).size(); j++) {
        if (j < caveSystem.get(i).size() - 1 &&
            !caveSystem.get(i).get(j).getAdjacentNodes()
                .containsKey(caveSystem.get(i).get(j + 1))) {
          caveSystem.get(i).get(j).addDestination(caveSystem.get(i).get(j + 1),
              BigInteger.valueOf(Integer.valueOf(lines.get(i).split("")[j + 1])));
        }
        if (i < caveSystem.size() - 1 &&
            !caveSystem.get(i).get(j).getAdjacentNodes()
                .containsKey(caveSystem.get(i + 1).get(j))) {
          caveSystem.get(i).get(j).addDestination(caveSystem.get(i + 1).get(j),
              BigInteger.valueOf(Integer.valueOf(lines.get(i + 1).split("")[j])));

        }
        if (i != 0 &&
            !caveSystem.get(i).get(j).getAdjacentNodes()
                .containsKey(caveSystem.get(i - 1).get(j))) {
          caveSystem.get(i).get(j).addDestination(caveSystem.get(i - 1).get(j),
              BigInteger.valueOf(Integer.valueOf(lines.get(i - 1).split("")[j])));

        }
        if (j != 0 &&
            !caveSystem.get(i).get(j).getAdjacentNodes()
                .containsKey(caveSystem.get(i).get(j - 1))) {
          caveSystem.get(i).get(j).addDestination(caveSystem.get(i).get(j - 1),
              BigInteger.valueOf(Integer.valueOf(lines.get(i).split("")[j - 1])));
        }
      }
    }
  }

  private List<List<Chiton>> generateCave(int y, int x, List<String> lines) {
    List<List<Chiton>> cave = new ArrayList<>();

    for (int i = 0; i < y; i++) {
      cave.add(new ArrayList<>());

      for (int j = 0; j < x; j++) {
        cave.get(i).add(new Chiton(BigInteger.valueOf(Integer.valueOf(lines.get(i).split("")[j]))));
      }
    }

    return cave;
  }

  private List<List<Chiton>> generateLargerCave(List<List<Chiton>> caveSystem) {
    int maxMultiplier = 5;
    BigInteger maxCost = BigInteger.valueOf(9);
    int baseLineLength = caveSystem.get(0).size();
    List<List<Chiton>> cave = initializeListArray(caveSystem.size() * maxMultiplier, baseLineLength * maxMultiplier);

    for (int y = 0; y < caveSystem.size(); y++) {
      for (int xMultiplier = 0; xMultiplier < maxMultiplier; xMultiplier++) {
        for (int x = 0; x < caveSystem.get(y).size(); x++) {
          BigInteger cost = (caveSystem.get(y % caveSystem.size()).get(x).getInitialValue().add(BigInteger.valueOf(xMultiplier)));

          if (cost.compareTo(maxCost) > 0) {
            cost = cost.subtract(maxCost);
          }

          cave.get(y).get(x + baseLineLength * xMultiplier).setInitialValue(cost);
        }
      }
    }

    for (int y = caveSystem.size(); y < cave.size(); y++) {
      for (int x = 0; x < cave.get(0).size(); x++) {
        BigInteger cost = cave.get(y - caveSystem.size()).get(x).getInitialValue().add(BigInteger.ONE);

        if (cost.compareTo(BigInteger.TEN) >= 0) {
          cost = cost.subtract(BigInteger.valueOf(9));
        }

        cave.get(y).get(x).setInitialValue(cost);
      }
    }

    return cave;
  }

  private List<List<Chiton>> initializeListArray(int y, int x) {
    List<List<Chiton>> initalizedArray = new ArrayList<>();

    for (int i = 0; i < y; i++) {
      initalizedArray.add(new ArrayList<>());

      for (int j = 0; j < x; j++) {
        initalizedArray.get(i).add(new Chiton(BigInteger.valueOf(-1)));
      }
    }
    return initalizedArray;
  }

  private void calculateShortestPathFromSource(Chiton source) {
    source.setDistance(BigInteger.ZERO);

    Set<Chiton> settledNodes = new HashSet<>();
    Set<Chiton> unsettledNodes = new HashSet<>();

    unsettledNodes.add(source);

    while (unsettledNodes.size() != 0) {
      Chiton currentNode = getLowestDistanceChiton(unsettledNodes);
      unsettledNodes.remove(currentNode);

      for (Entry<Chiton, BigInteger> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
        Chiton adjacentNode = adjacencyPair.getKey();
        BigInteger edgeWeight = adjacencyPair.getValue();

        if (!settledNodes.contains(adjacentNode)) {
          calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
          unsettledNodes.add(adjacentNode);
        }
      }

      settledNodes.add(currentNode);
    }
  }

  private void calculateMinimumDistance(Chiton evaluationNode, BigInteger edgeWeight, Chiton sourceNode) {
    BigInteger sourceDistance = sourceNode.getDistance();

    if (sourceDistance.add(edgeWeight).compareTo(evaluationNode.getDistance()) < 0) {
      evaluationNode.setDistance(sourceDistance.add(edgeWeight));
      LinkedList<Chiton> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
      shortestPath.add(sourceNode);
      evaluationNode.setShortestPath(shortestPath);
    }
  }

  private Chiton getLowestDistanceChiton(Set<Chiton> unsettledNodes) {
    Chiton lowestDistanceNode = null;
    BigInteger lowestDistance = BigInteger.valueOf(Integer.MAX_VALUE);

    for (Chiton chiton : unsettledNodes) {
      BigInteger nodeDistance = chiton.getDistance();
      if (nodeDistance.compareTo(lowestDistance) < 0) {
        lowestDistance = nodeDistance;
        lowestDistanceNode = chiton;
      }
    }

    return lowestDistanceNode;
  }

  public class Chiton {

    private BigInteger initialValue;

    private List<Chiton> shortestPath = new LinkedList<>();

    private BigInteger distance = BigInteger.valueOf(Integer.MAX_VALUE);

    Map<Chiton, BigInteger> adjacentNodes = new HashMap<>();

    public Chiton(BigInteger initialValue) {
      this.initialValue = initialValue;
    }

    public BigInteger getInitialValue() {
      return initialValue;
    }

    public void setInitialValue(BigInteger initialValue) {
      this.initialValue = initialValue;
    }

    private void addDestination(Chiton destination, BigInteger distance) {
      adjacentNodes.put(destination, distance);
    }

    public List<Chiton> getShortestPath() {
      return shortestPath;
    }

    public void setShortestPath(List<Chiton> shortestPath) {
      this.shortestPath = shortestPath;
    }

    public BigInteger getDistance() {
      return distance;
    }

    public void setDistance(BigInteger distance) {
      this.distance = distance;
    }

    public Map<Chiton, BigInteger> getAdjacentNodes() {
      return adjacentNodes;
    }

    public void setAdjacentNodes(Map<Chiton, BigInteger> adjacentNodes) {
      this.adjacentNodes = adjacentNodes;
    }
  }

}
