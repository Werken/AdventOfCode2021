package days;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import models.FileReaderHelper;

public class Day11 {

  private int flashes = 0;
  private int flashesPerStep = 0;
  private List<List<Octopus>> octopuses = new ArrayList<>();

  public static void main(String[] args) {
    System.out.println("Part One Answer: " + new Day11().dumboOctopusPartOne());
    System.out.println("Part Two Answer: " + new Day11().dumboOctopusPartTwo());
  }

  private int dumboOctopusPartOne() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day11/part-one-input.txt");
    List<List<Integer>> map = new ArrayList<>();
    for (String line : lines) {
      int[] entries = Stream.of(line.split("")).mapToInt(Integer::parseInt).toArray();
      List<Integer> mapRow = new ArrayList<>();
      for (int entry : entries) {
        mapRow.add(entry);
      }
      map.add(mapRow);
    }
    setupGrid(lines.size(), lines.get(0).length(), map);
    mapNeighbors();

    generateFlashes(100);

    return flashes;
  }

  private int dumboOctopusPartTwo() {
    flashes = 0;
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day11/part-two-input.txt");
    List<List<Integer>> map = new ArrayList<>();
    for (String line : lines) {
      int[] entries = Stream.of(line.split("")).mapToInt(Integer::parseInt).toArray();
      List<Integer> mapRow = new ArrayList<>();
      for (int entry : entries) {
        mapRow.add(entry);
      }
      map.add(mapRow);
    }
    setupGrid(lines.size(), lines.get(0).length(), map);
    mapNeighbors();

    return simulateSteps();
  }

  private int simulateSteps() {
    int step = 0;
    boolean sync;
    do {
      for (int i = 0; i < octopuses.size(); i++) {
        for (int j = 0; j < octopuses.get(i).size(); j++) {
          flash(octopuses.get(i).get(j));
        }
      }
      sync = flashesPerStep < octopuses.size() * octopuses.get(0).size();
      resetOctopuses();
      step++;
    } while (sync);

    return step;
  }

  private void generateFlashes(int steps) {
    for (int i = 0; i < steps; i++) {
      for (int j = 0; j < octopuses.size(); j++) {
        for (int k = 0; k < octopuses.get(j).size(); k++) {
          flash(octopuses.get(j).get(k));
        }
      }
      resetOctopuses();
    }
  }

  private void resetOctopuses() {
    for (int i = 0; i < octopuses.size(); i++) {
      for (int j = 0; j < octopuses.get(i).size(); j++) {
        if (octopuses.get(i).get(j).isFlashed()) {
          flashesPerStep = 0;
          octopuses.get(i).get(j).setEnergyLevel(0);
          octopuses.get(i).get(j).setFlashed(false);
        }
      }
    }
  }

  private void flash(Octopus octopus) {
    octopus.setEnergyLevel(octopus.getEnergyLevel() + 1);
    if (octopus.getEnergyLevel() > 9 && !octopus.isFlashed()) {
      octopus.setFlashed(true);
      flashes++;
      flashesPerStep++;
      octopus.getNeighbors()
          .stream()
          .filter(f -> !f.isFlashed())
          .forEach(o -> flash(o));
    }
  }

  private void mapNeighbors() {
    for (int i = 0; i < octopuses.size(); i++) {
      for (int j = 0; j < octopuses.get(i).size(); j++) {
        if (i == 0 && j == 0) {
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i).get(j + 1));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i + 1).get(j));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i + 1).get(j + 1));
        } else if (i == 0 && (j > 0 && j < octopuses.get(i).size() - 1)) {
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i).get(j - 1));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i).get(j + 1));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i + 1).get(j));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i + 1).get(j - 1));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i + 1).get(j + 1));
        } else if (i == 0 && j == octopuses.get(i).size() - 1) {
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i).get(j - 1));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i + 1).get(j));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i + 1).get(j - 1));
        } else if ((i > 0 && i < octopuses.size() - 1) && j == 0) {
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i - 1).get(j));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i + 1).get(j));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i).get(j + 1));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i - 1).get(j + 1));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i + 1).get(j + 1));
        } else if ((i > 0 && i < octopuses.size() - 1) && j == octopuses.get(i).size() - 1) {
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i - 1).get(j));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i + 1).get(j));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i).get(j - 1));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i + 1).get(j - 1));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i - 1).get(j - 1));
        } else if (i == octopuses.get(i).size() - 1 && j == 0) {
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i - 1).get(j));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i).get(j + 1));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i - 1).get(j + 1));
        } else if (i == octopuses.get(i).size() - 1 && j == octopuses.get(i).size() - 1) {
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i - 1).get(j));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i).get(j - 1));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i - 1).get(j - 1));
        } else if (i == octopuses.size() - 1 && (j > 0 && j < octopuses.get(i).size() - 1)) {
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i - 1).get(j));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i).get(j + 1));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i).get(j - 1));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i - 1).get(j - 1));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i - 1).get(j + 1));
        } else {
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i - 1).get(j));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i + 1).get(j));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i).get(j - 1));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i).get(j + 1));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i + 1).get(j - 1));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i + 1).get(j + 1));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i - 1).get(j - 1));
          octopuses.get(i).get(j).addNeighbor(octopuses.get(i - 1).get(j + 1));
        }
      }
    }
  }

  private void setupGrid(int x, int y, List<List<Integer>> map) {
    for (int i = 0; i < x; i++) {
      List<Octopus> octopusRow = new ArrayList<>();
      for (int j = 0; j < y; j++) {
        octopusRow.add(new Octopus(map.get(i).get(j), false, new ArrayList<>()));
      }
      octopuses.add(octopusRow);
    }
  }

  public class Octopus {
    private int energyLevel;
    private boolean flashed;
    private List<Octopus> neighbors;

    public Octopus(int energyLevel, boolean flashed, List<Octopus> neighbors) {
      this.energyLevel = energyLevel;
      this.flashed = flashed;
      this.neighbors = neighbors;
    }

    public int getEnergyLevel() {
      return energyLevel;
    }

    public void setEnergyLevel(int energyLevel) {
      this.energyLevel = energyLevel;
    }

    public boolean isFlashed() {
      return flashed;
    }

    public void setFlashed(boolean flashed) {
      this.flashed = flashed;
    }

    public List<Octopus> getNeighbors() {
      return neighbors;
    }

    public void setNeighbors(List<Octopus> neighbors) {
      this.neighbors = neighbors;
    }

    public void addNeighbor(Octopus neighbor) {
      this.neighbors.add(neighbor);
    }
  }

}
