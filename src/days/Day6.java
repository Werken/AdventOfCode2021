package days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import models.FileReaderHelper;

public class Day6 {

  public static void main(String[] args) {
    System.out.println("Part One Answer: " + new Day6().lanternFishPartOne());
    System.out.println("Part Two Answer: " + new Day6().lanternFishPartTwo());
  }

  private long lanternFishPartOne() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day6/part-one-input.txt");
    List<Integer> fish = new ArrayList<>();

    String[] data = lines.get(0).split(",");
    for (String fishAge : data) {
      fish.add(Integer.parseInt(fishAge));
    }
    List<LanternFish> storage = setUpInitialConditions(fish);

    return simulateGrowth(storage, 80);
  }

  private long lanternFishPartTwo() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day6/part-two-input.txt");
    List<Integer> fish = new ArrayList<>();

    String[] data = lines.get(0).split(",");
    for (String fishAge : data) {
      fish.add(Integer.parseInt(fishAge));
    }
    List<LanternFish> storage = setUpInitialConditions(fish);

    return simulateGrowth(storage, 256);
  }

  private List<LanternFish> setUpInitialConditions(List<Integer> fish) {
    List<LanternFish> storage = new ArrayList<>(Arrays.asList(new LanternFish(0, (byte)0),
        new LanternFish(1, (byte)0), new LanternFish(2, (byte)0),
        new LanternFish(3, (byte)0), new LanternFish(4, (byte)0),
        new LanternFish(5, (byte)0), new LanternFish(6, (byte)0),
        new LanternFish(7, (byte)0), new LanternFish(8, (byte)0)));

    fish.stream()
        .forEach(l -> storage.get(l).setChildren(storage.get(l).getChildren() + (byte)1));

    return storage;
  }

  private long simulateGrowth(List<LanternFish> fish, int days) {
    for (int i = 0; i < days; i++) {
      increaseInternalTimer(fish);
    }
    return fish.stream().mapToLong(LanternFish::getChildren).sum();
  }

  public void increaseInternalTimer(List<LanternFish> fish) {
    long fishToAdd = 0;
    for (int i = 0; i < fish.size(); i++) {
      if (i == 0) {
        fishToAdd = fish.get(i).getChildren();
      } else {
        fish.get(i - 1).setChildren(fish.get(i).getChildren());
      }
      fish.get(i).setChildren(0);
    }
    fish.get(6).setChildren(fish.get(6).getChildren() + fishToAdd);
    fish.get(fish.size() - 1).setChildren(fishToAdd);
  }

  public class LanternFish {
    private int internalTimer;
    private long children;

    public LanternFish(int internalTimer, long children) {
      this.internalTimer = internalTimer;
      this.children = children;
    }

    public int getInternalTimer() {
      return internalTimer;
    }

    public void setInternalTimer(int internalTimer) {
      this.internalTimer = internalTimer;
    }

    public long getChildren() {
      return children;
    }

    public void setChildren(long children) {
      this.children = children;
    }
  }
}
