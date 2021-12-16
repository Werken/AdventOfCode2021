package days;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import models.FileReaderHelper;

public class Day9 {

  private List<Integer> lowPoints = new ArrayList<>();
  private List<Point> pointsToTraverse = new ArrayList<>();
  private List<Integer> basins = new ArrayList<>();

  public static void main(String[] args) {
    System.out.println("Part One Answer: " + new Day9().smokeBasinPartOne());
    System.out.println("Part Two Answer: " + new Day9().smokeBasinPartTwo());
  }

  private int smokeBasinPartOne() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day9/part-one-input.txt");
    List<List<Integer>> mappings = new ArrayList<>();
    List<List<Point>> pointMappings = new ArrayList<>();
    int x = 0;
    int y = 0;

    for (String line : lines) {
      List<Integer> rows = new ArrayList<>();
      List<Point> pointRows = new ArrayList<>();

      int[] toInt = Stream.of(line.split(""))
          .mapToInt(Integer::parseInt)
          .toArray();

      for (int i : toInt) {
        rows.add(i);
        pointRows.add(new Point(x, y, i, null, null, null, null));
        y++;
      }
      mappings.add(rows);
      pointMappings.add(pointRows);
      x++;
    }

    findLowPoints(mappings, pointMappings);
    return riskLevel();
  }

  private int smokeBasinPartTwo() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day9/part-two-input.txt");
    List<List<Integer>> mappings = new ArrayList<>();
    List<List<Point>> pointMappings = new ArrayList<>();
    int x = 0;
    int y = 0;

    for (String line : lines) {
      List<Integer> rows = new ArrayList<>();
      List<Point> pointRows = new ArrayList<>();
      int[] toInt = Stream.of(line.split(""))
          .mapToInt(Integer::parseInt)
          .toArray();

      for (int i : toInt) {
        rows.add(i);
        pointRows.add(new Point(x, y, i, null, null, null, null));
        y++;
      }
      mappings.add(rows);
      pointMappings.add(pointRows);
      y = 0;
      x++;
    }

    findLowPoints(mappings, pointMappings);
    findLargestBasins(pointMappings);

    return basins.get(0) * basins.get(1) * basins.get(2);

  }

  private void findLargestBasins(List<List<Point>> pointMappings) {
    for (Point point : pointsToTraverse) {
      List<Point> pointsInBasin = new ArrayList<>();
      traverseMap(point, pointMappings, pointsInBasin);
      addPointsInBasin(pointsInBasin);
    }
  }

  private void addPointsInBasin(List<Point> pointsInBasin) {
    int smallestBasin = Integer.MAX_VALUE;
    for (Integer basin : basins) {
      if (basin < smallestBasin) smallestBasin = basin;
    }
    if (basins.size() == 3 && pointsInBasin.size() > smallestBasin) {
      basins.remove(basins.indexOf(smallestBasin));
      basins.add(pointsInBasin.size());
    } else if (basins.size() < 3) {
      basins.add(pointsInBasin.size());
    }
  }

  private void traverseMap(Point point, List<List<Point>> pointMappings, List<Point> pointsInBasin) {
    if (point.isVisited()) return;

    if (point.getValue() == 9) return;

    pointMappings.get(point.getX()).get(point.getY()).setVisited(true);

    if (!pointsInBasin.contains(point)) pointsInBasin.add(point);

    if (point.getX() == 97 && point.getValue() == 0) {
      System.out.println();
    }
    // Center point
    if (point.isCenterPoint()) {
      if (point.getValue() > point.getBottomAdj() && point.getValue() > point.getLeftAdj() &&
          point.getValue() > point.getRightAdj() && point.getValue() > point.getTopAdj()) return;

      if (pointMappings.get(point.getX() + 1).get(point.getY()).getValue() != 9 &&
          !pointMappings.get(point.getX() + 1).get(point.getY()).isVisited()) {
        traverseMap(pointMappings.get(point.getX() + 1).get(point.getY()), pointMappings, pointsInBasin);
      }
      if (pointMappings.get(point.getX() - 1).get(point.getY()).getValue() != 9 &&
          !pointMappings.get(point.getX() - 1).get(point.getY()).isVisited()) {
        traverseMap(pointMappings.get(point.getX() - 1).get(point.getY()), pointMappings, pointsInBasin);
      }
      if (pointMappings.get(point.getX()).get(point.getY() - 1).getValue() != 9 &&
          !pointMappings.get(point.getX()).get(point.getY() - 1).isVisited()) {
        traverseMap(pointMappings.get(point.getX()).get(point.getY() - 1), pointMappings, pointsInBasin);
      }
      if (pointMappings.get(point.getX()).get(point.getY() + 1).getValue() != 9 &&
          !pointMappings.get(point.getX()).get(point.getY() + 1).isVisited()) {
        traverseMap(pointMappings.get(point.getX()).get(point.getY() + 1), pointMappings, pointsInBasin);
      }
    }
    // Edge point
    else if (point.isEdgePoint()) {
      if (point.getBottomAdj() == null) {
        if (point.getValue() > point.getLeftAdj() && point.getValue() > point.getRightAdj() &&
            point.getValue() > point.getTopAdj()) return;

        if (pointMappings.get(point.getX() - 1).get(point.getY()).getValue() != 9 &&
            !pointMappings.get(point.getX() - 1).get(point.getY()).isVisited()) {
          traverseMap(pointMappings.get(point.getX() - 1).get(point.getY()), pointMappings, pointsInBasin);
        }
        if (pointMappings.get(point.getX()).get(point.getY() - 1).getValue() != 9 &&
            !pointMappings.get(point.getX()).get(point.getY() - 1).isVisited()) {
          traverseMap(pointMappings.get(point.getX()).get(point.getY() - 1), pointMappings, pointsInBasin);
        }
        if (pointMappings.get(point.getX()).get(point.getY() + 1).getValue() != 9 &&
            !pointMappings.get(point.getX()).get(point.getY() + 1).isVisited()) {
          traverseMap(pointMappings.get(point.getX()).get(point.getY() + 1), pointMappings, pointsInBasin);
        }

      } else if (point.getLeftAdj() == null) {
        if (point.getValue() > point.getBottomAdj() && point.getValue() > point.getRightAdj() &&
            point.getValue() > point.getTopAdj()) return;

        if (pointMappings.get(point.getX() + 1).get(point.getY()).getValue() != 9 &&
            !pointMappings.get(point.getX() + 1).get(point.getY()).isVisited()) {
          traverseMap(pointMappings.get(point.getX() + 1).get(point.getY()), pointMappings, pointsInBasin);
        }
        if (pointMappings.get(point.getX() - 1).get(point.getY()).getValue() != 9 &&
            !pointMappings.get(point.getX() - 1).get(point.getY()).isVisited()) {
          traverseMap(pointMappings.get(point.getX() - 1).get(point.getY()), pointMappings, pointsInBasin);
        }
        if (pointMappings.get(point.getX()).get(point.getY() + 1).getValue() != 9 &&
            !pointMappings.get(point.getX()).get(point.getY() + 1).isVisited()) {
          traverseMap(pointMappings.get(point.getX()).get(point.getY() + 1), pointMappings, pointsInBasin);
        }
      } else if (point.getRightAdj() == null) {
        if (point.getValue() > point.getBottomAdj() && point.getValue() > point.getLeftAdj() &&
            point.getValue() > point.getTopAdj()) return;

        if (pointMappings.get(point.getX() + 1).get(point.getY()).getValue() != 9 &&
            !pointMappings.get(point.getX() + 1).get(point.getY()).isVisited()) {
          traverseMap(pointMappings.get(point.getX() + 1).get(point.getY()), pointMappings, pointsInBasin);
        }
        if (pointMappings.get(point.getX() - 1).get(point.getY()).getValue() != 9 &&
            !pointMappings.get(point.getX() - 1).get(point.getY()).isVisited()) {
          traverseMap(pointMappings.get(point.getX() - 1).get(point.getY()), pointMappings, pointsInBasin);
        }
        if (pointMappings.get(point.getX()).get(point.getY() - 1).getValue() != 9 &&
            !pointMappings.get(point.getX()).get(point.getY() - 1).isVisited()) {
          traverseMap(pointMappings.get(point.getX()).get(point.getY() - 1), pointMappings, pointsInBasin);
        }
      } else if (point.getTopAdj() == null) {
        if (point.getValue() > point.getBottomAdj() && point.getValue() > point.getLeftAdj() &&
            point.getValue() > point.getRightAdj()) return;

        if (pointMappings.get(point.getX() + 1).get(point.getY()).getValue() != 9 &&
            !pointMappings.get(point.getX() + 1).get(point.getY()).isVisited()) {
          traverseMap(pointMappings.get(point.getX() + 1).get(point.getY()), pointMappings, pointsInBasin);
        }
        if (pointMappings.get(point.getX()).get(point.getY() - 1).getValue() != 9 &&
            !pointMappings.get(point.getX()).get(point.getY() - 1).isVisited()) {
          traverseMap(pointMappings.get(point.getX()).get(point.getY() - 1), pointMappings, pointsInBasin);
        }
        if (pointMappings.get(point.getX()).get(point.getY() + 1).getValue() != 9 &&
            !pointMappings.get(point.getX()).get(point.getY() + 1).isVisited()) {
          traverseMap(pointMappings.get(point.getX()).get(point.getY() + 1), pointMappings, pointsInBasin);
        }
      }
    }
    // Corner point
    else {
      if (point.getTopAdj() == null && point.getLeftAdj() == null) {
        if (point.getValue() > point.getBottomAdj() && point.getValue() > point.getRightAdj()) return;

        if (pointMappings.get(point.getX() + 1).get(point.getY()).getValue() != 9 &&
            !pointMappings.get(point.getX() + 1).get(point.getY()).isVisited()) {
          traverseMap(pointMappings.get(point.getX() + 1).get(point.getY()), pointMappings, pointsInBasin);
        }
        if (pointMappings.get(point.getX()).get(point.getY() + 1).getValue() != 9 &&
            !pointMappings.get(point.getX()).get(point.getY() + 1).isVisited()) {
          traverseMap(pointMappings.get(point.getX()).get(point.getY() + 1), pointMappings, pointsInBasin);
        }
      } else if (point.getTopAdj() == null && point.getRightAdj() == null) {
        if (point.getValue() > point.getBottomAdj() && point.getValue() > point.getLeftAdj()) return;

        if (pointMappings.get(point.getX() + 1).get(point.getY()).getValue() != 9 &&
            !pointMappings.get(point.getX() + 1).get(point.getY()).isVisited()) {
          traverseMap(pointMappings.get(point.getX() + 1).get(point.getY()), pointMappings, pointsInBasin);
        }
        if (pointMappings.get(point.getX()).get(point.getY() - 1).getValue() != 9 &&
            !pointMappings.get(point.getX()).get(point.getY() - 1).isVisited()) {
          traverseMap(pointMappings.get(point.getX()).get(point.getY() - 1), pointMappings, pointsInBasin);
        }
      } else if (point.getBottomAdj() == null && point.getLeftAdj() == null) {
        if (point.getValue() > point.getRightAdj() && point.getValue() > point.getTopAdj()) return;

        if (pointMappings.get(point.getX() - 1).get(point.getY()).getValue() != 9 &&
            !pointMappings.get(point.getX() - 1).get(point.getY()).isVisited()) {
          traverseMap(pointMappings.get(point.getX() - 1).get(point.getY()), pointMappings, pointsInBasin);
        }
        if (pointMappings.get(point.getX()).get(point.getY() + 1).getValue() != 9 &&
            !pointMappings.get(point.getX()).get(point.getY() + 1).isVisited()) {
          traverseMap(pointMappings.get(point.getX()).get(point.getY() + 1), pointMappings, pointsInBasin);
        }
      } else if (point.getBottomAdj() == null && point.getRightAdj() == null) {
        if (point.getValue() > point.getLeftAdj() && point.getValue() > point.getTopAdj()) return;

        if (pointMappings.get(point.getX() - 1).get(point.getY()).getValue() != 9 &&
            !pointMappings.get(point.getX() - 1).get(point.getY()).isVisited()) {
          traverseMap(pointMappings.get(point.getX() - 1).get(point.getY()), pointMappings, pointsInBasin);
        }
        if (pointMappings.get(point.getX()).get(point.getY() - 1).getValue() != 9 &&
            !pointMappings.get(point.getX()).get(point.getY() - 1).isVisited()) {
          traverseMap(pointMappings.get(point.getX()).get(point.getY() - 1), pointMappings, pointsInBasin);
        }
      }
    }
  }

  private int riskLevel() {
    int riskLevel = 0;
    for (Integer point : lowPoints) {
      riskLevel += point + 1;
    }

    return riskLevel;
  }

  private void findLowPoints(List<List<Integer>> mappings, List<List<Point>> pointMappings) {
    for (int i = 0; i < mappings.size(); i++) {
      for (int j = 0; j < mappings.get(i).size(); j++) {
        if (isCorner(i, j, mappings.size() - 1, mappings.get(i).size() - 1)) {
          checkCornerPoint(i, j, mappings.size() - 1, mappings.get(i).size() - 1, mappings, pointMappings);
        } else if (isEdge(i, j, mappings.size() - 1, mappings.get(i).size() - 1)) {
          checkEdgePoint(i, j, mappings.size() - 1, mappings.get(i).size() - 1, mappings, pointMappings);
        } else {
          checkCenterPoint(i, j, mappings, pointMappings);
        }
      }
    }
  }

  private void checkCornerPoint(int x, int y, int height, int width, List<List<Integer>> mappings, List<List<Point>> pointMappings) {
    int point;
    int adj1;
    int adj2;
    // Top left
    if (x == 0 && y == 0) {
      point = mappings.get(x).get(y);
      adj1 = mappings.get(x).get(y + 1);
      adj2 = mappings.get(x + 1).get(y);
      pointMappings.get(x).get(y).setRightAdj(adj1);
      pointMappings.get(x).get(y).setBottomAdj(adj2);
      pointMappings.get(x).get(y).setTopAdj(null);
      pointMappings.get(x).get(y).setLeftAdj(null);
      if (point < adj1 && point < adj2) pointsToTraverse.add(new Point(x, y, point, null, null, adj1, adj2));
    // Top right
    } else if (x == 0 && y == width) {
      point = mappings.get(x).get(y);
      adj1 = mappings.get(x).get(y - 1);
      adj2 = mappings.get(x + 1).get(y);
      pointMappings.get(x).get(y).setLeftAdj(adj1);
      pointMappings.get(x).get(y).setBottomAdj(adj2);
      pointMappings.get(x).get(y).setTopAdj(null);
      pointMappings.get(x).get(y).setRightAdj(null);
      if (point < adj1 && point < adj2) pointsToTraverse.add(new Point(x, y, point, null, adj1, null, adj2));
    // Bottom left
    } else if (x == height && y == 0) {
      point = mappings.get(x).get(y);
      adj1 = mappings.get(x).get(y + 1);
      adj2 = mappings.get(x - 1).get(y);
      pointMappings.get(x).get(y).setRightAdj(adj1);
      pointMappings.get(x).get(y).setTopAdj(adj2);
      pointMappings.get(x).get(y).setLeftAdj(null);
      pointMappings.get(x).get(y).setBottomAdj(null);
      if (point < adj1 && point < adj2) pointsToTraverse.add(new Point(x, y, point, adj2, null, adj1, null));
    // Bottom right
    } else {
      point = mappings.get(x).get(y);
      adj1 = mappings.get(x).get(y - 1);
      adj2 = mappings.get(x - 1).get(y);
      pointMappings.get(x).get(y).setLeftAdj(adj1);
      pointMappings.get(x).get(y).setTopAdj(adj2);
      pointMappings.get(x).get(y).setRightAdj(null);
      pointMappings.get(x).get(y).setBottomAdj(null);
      if (point < adj1 && point < adj2) pointsToTraverse.add(new Point(x, y, point, adj2, adj1, null, null));
    }
    if (point < adj1 && point < adj2) lowPoints.add(point);
  }

  private void checkEdgePoint(int x, int y, int height, int width, List<List<Integer>> mappings, List<List<Point>> pointMappings) {
    int point;
    int adj1;
    int adj2;
    int adj3;
    // Top
    if (x == 0 && (y > 0 && y < width)) {
      point = mappings.get(x).get(y);
      adj1 = mappings.get(x).get(y - 1);
      adj2 = mappings.get(x).get(y + 1);
      adj3 = mappings.get(x + 1).get(y);
      pointMappings.get(x).get(y).setLeftAdj(adj1);
      pointMappings.get(x).get(y).setRightAdj(adj2);
      pointMappings.get(x).get(y).setBottomAdj(adj3);
      pointMappings.get(x).get(y).setTopAdj(null);
      if (point < adj1 && point < adj2 && point < adj3) pointsToTraverse.add(new Point(x, y, point, null, adj1, adj2, adj3));
      // Left
    } else if (y == 0 && (x > 0 && x < height)) {
      point = mappings.get(x).get(y);
      adj1 = mappings.get(x + 1).get(y);
      adj2 = mappings.get(x - 1).get(y);
      adj3 = mappings.get(x).get(y + 1);
      pointMappings.get(x).get(y).setBottomAdj(adj1);
      pointMappings.get(x).get(y).setTopAdj(adj2);
      pointMappings.get(x).get(y).setRightAdj(adj3);
      pointMappings.get(x).get(y).setLeftAdj(null);
      if (point < adj1 && point < adj2 && point < adj3) pointsToTraverse.add(new Point(x, y, point, adj2, null, adj3, adj1));
      // Right
    } else if (y == width && (x > 0 && x < height)) {
      point = mappings.get(x).get(y);
      adj1 = mappings.get(x + 1).get(y);
      adj2 = mappings.get(x - 1).get(y);
      adj3 = mappings.get(x).get(y - 1);
      pointMappings.get(x).get(y).setBottomAdj(adj1);
      pointMappings.get(x).get(y).setTopAdj(adj2);
      pointMappings.get(x).get(y).setLeftAdj(adj3);
      pointMappings.get(x).get(y).setRightAdj(null);
      if (point < adj1 && point < adj2 && point < adj3) pointsToTraverse.add(new Point(x, y, point, adj2, adj3, null, adj1));
      // Bottom
    } else {
      point = mappings.get(x).get(y);
      adj1 = mappings.get(x).get(y - 1);
      adj2 = mappings.get(x).get(y + 1);
      adj3 = mappings.get(x - 1).get(y);
      pointMappings.get(x).get(y).setLeftAdj(adj1);
      pointMappings.get(x).get(y).setRightAdj(adj2);
      pointMappings.get(x).get(y).setTopAdj(adj3);
      pointMappings.get(x).get(y).setBottomAdj(null);
      if (point < adj1 && point < adj2 && point < adj3) pointsToTraverse.add(new Point(x, y, point, adj3, adj1, adj2, null));
    }
    if (point < adj1 && point < adj2 && point < adj3) lowPoints.add(point);
  }

  private void checkCenterPoint(int x, int y, List<List<Integer>> mappings, List<List<Point>> pointMappings) {
    int point = mappings.get(x).get(y);
    int adj1 = mappings.get(x).get(y - 1);
    int adj2 = mappings.get(x).get(y + 1);
    int adj3 = mappings.get(x + 1).get(y);
    int adj4 = mappings.get(x - 1).get(y);

    pointMappings.get(x).get(y).setLeftAdj(adj1);
    pointMappings.get(x).get(y).setRightAdj(adj2);
    pointMappings.get(x).get(y).setBottomAdj(adj3);
    pointMappings.get(x).get(y).setTopAdj(adj4);

    if (point < adj1 && point < adj2 && point < adj3 && point < adj4) {
      lowPoints.add(point);
      pointsToTraverse.add(new Point(x, y, point, adj4, adj1, adj2, adj3));
    }
  }

  private boolean isCorner(int x, int y, int height, int width) {
    if ((x == 0 && y == 0) || (x == 0 && y == width) || (x == height && y == 0) ||
        (x == height && y == width)) return true;
    return false;
  }

  private boolean isEdge(int x, int y, int height, int width) {
    if ((x == 0 && (y > 0 && y < width)) || (y == 0 && (x > 0 && x < height)) ||
        (y == width && (x > 0 && x < height)) || (x == height && (y > 0 && y < width))) return true;
    return false;
  }

  private class Point {
    private Integer x;
    private Integer y;
    private Integer value;
    private Integer topAdj;
    private Integer leftAdj;
    private Integer rightAdj;
    private Integer bottomAdj;
    private boolean visited;

    public Point(Integer x, Integer y, Integer value, Integer topAdj, Integer leftAdj,
        Integer rightAdj, Integer bottomAdj) {
      this.x = x;
      this.y = y;
      this.value = value;
      this.topAdj = topAdj;
      this.leftAdj = leftAdj;
      this.rightAdj = rightAdj;
      this.bottomAdj = bottomAdj;
      this.visited = false;
    }

    public boolean isEdgePoint() {
      int count = 0;
      if (this.topAdj == null) count++;
      if (this.leftAdj == null) count++;
      if (this.rightAdj == null) count++;
      if (this.bottomAdj == null) count++;
      if (count == 1) return true;
      return false;
    }

    public boolean isCornerPoint() {
      int count = 0;
      if (this.topAdj == null) count++;
      if (this.leftAdj == null) count++;
      if (this.rightAdj == null) count++;
      if (this.bottomAdj == null) count++;
      if (count == 2) return true;
      return false;
    }

    public boolean isCenterPoint() {
      int count = 0;
      if (this.topAdj == null) count++;
      if (this.leftAdj == null) count++;
      if (this.rightAdj == null) count++;
      if (this.bottomAdj == null) count++;
      if (count == 0) return true;
      return false;
    }

    public boolean isVisited() {
      return visited;
    }

    public void setVisited(boolean visited) {
      this.visited = visited;
    }

    public Integer getX() {
      return x;
    }

    public void setX(Integer x) {
      this.x = x;
    }

    public Integer getY() {
      return y;
    }

    public void setY(Integer y) {
      this.y = y;
    }

    public Integer getValue() {
      return value;
    }

    public void setValue(Integer value) {
      this.value = value;
    }

    public Integer getTopAdj() {
      return topAdj;
    }

    public void setTopAdj(Integer topAdj) {
      this.topAdj = topAdj;
    }

    public Integer getLeftAdj() {
      return leftAdj;
    }

    public void setLeftAdj(Integer leftAdj) {
      this.leftAdj = leftAdj;
    }

    public Integer getRightAdj() {
      return rightAdj;
    }

    public void setRightAdj(Integer rightAdj) {
      this.rightAdj = rightAdj;
    }

    public Integer getBottomAdj() {
      return bottomAdj;
    }

    public void setBottomAdj(Integer bottomAdj) {
      this.bottomAdj = bottomAdj;
    }
  }
}
