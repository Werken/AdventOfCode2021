package days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.FileReaderHelper;

public class Day8 {

  private int sum = 0;
  private Display display = new Display();

  public static void main(String[] args) {
    System.out.println("Part One Answer: " + new Day8().sevenSegmentSearchPartOne());
    System.out.println("Part Two Answer: " + new Day8().sevenSegmentSearchPartTwo());
  }

  private int sevenSegmentSearchPartOne() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day8/part-one-input.txt");

    for (String line : lines) {
      countUniqueDigits(line.split(" \\| ")[1]);
    }
    return sum;
  }

  private int sevenSegmentSearchPartTwo() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day8/part-two-input.txt");
    Map<Integer, List<String>> map = new HashMap<>();
    for (int i = 2; i < 8; i++) {
      map.put(i, new ArrayList<>());
    }

    for (String line : lines) {
      mapDigits(line.split(" \\| ")[0], map);
      sum += unscrambleDigits(map, line.split(" \\| ")[1]);
      resetMapping(map);
    }
    return sum;
  }

  private int unscrambleDigits(Map<Integer, List<String>> map, String values) {
    do {
      for (Map.Entry<Integer, List<String>> entry : map.entrySet()) {
        if (entry.getKey().equals(2) && display.getOne().getCode().isEmpty()) {
          display.getOne().setCode(entry.getValue().get(0));
        }
        if (entry.getKey().equals(3) && display.getSeven().getCode().isEmpty()) {
          display.getSeven().setCode(entry.getValue().get(0));
        }
        if (entry.getKey().equals(4) && display.getFour().getCode().isEmpty()) {
          display.getFour().setCode(entry.getValue().get(0));
        }
        if (entry.getKey().equals(7) && display.getEight().getCode().isEmpty()) {
          display.getEight().setCode(entry.getValue().get(0));
        }
        if (entry.getKey().equals(5) && (display.getTwo().getCode().isEmpty() ||
            display.getThree().getCode().isEmpty() || display.getFive().getCode().isEmpty())) {
          getFiveDigitNumber(map.get(5));
        }
        if (entry.getKey().equals(6) && (display.getSix().getCode().isEmpty() ||
            display.getNine().getCode().isEmpty())) {
          getSixDigitNumber(map.get(6));
        }
      }
    } while(display.determinedNumbers() < mapSize(map));
    Map<Integer, List<String>> newMap = sortDisplayValues(map);

    return createDisplayNumber(newMap, values);
  }

  private Map<Integer, List<String>> sortDisplayValues(Map<Integer, List<String>> map) {
    char sort[];
    String sorted;
    Map<Integer, List<String>> newMap = new HashMap<>();
    for (Map.Entry<Integer, List<String>> entry : map.entrySet()) {
      List<String> sortedList = new ArrayList<>();
      for (String value : entry.getValue()) {
        sort = value.toCharArray();
        Arrays.sort(sort);
        sorted = new String(sort);
        sortedList.add(sorted);
      }
      newMap.put(entry.getKey(), sortedList);
    }

    return newMap;
  }

  private int createDisplayNumber(Map<Integer, List<String>> map, String values) {
    String number = "";
    String[] valuesArray = values.split(" ");
    for (String value : valuesArray) {
      char sort[] = value.toCharArray();
      Arrays.sort(sort);
      String sorted = new String(sort);

      if (sorted.equals(display.getZero().getCode())) number += "0";
      if (sorted.equals(display.getOne().getCode())) number += "1";
      if (sorted.equals(display.getTwo().getCode())) number += "2";
      if (sorted.equals(display.getThree().getCode())) number += "3";
      if (sorted.equals(display.getFour().getCode())) number += "4";
      if (sorted.equals(display.getFive().getCode())) number += "5";
      if (sorted.equals(display.getSix().getCode())) number += "6";
      if (sorted.equals(display.getSeven().getCode())) number += "7";
      if (sorted.equals(display.getEight().getCode())) number += "8";
      if (sorted.equals(display.getNine().getCode())) number += "9";
    }

    return Integer.parseInt(number);
  }

  private int mapSize(Map<Integer, List<String>> map) {
    int total = 0;
    for (Map.Entry<Integer, List<String>> entry : map.entrySet()) {
      total += entry.getValue().size();
    }
    return total;
  }

  private void getFiveDigitNumber(List<String> fives) {
    for (String fiveDigitNumber : fives) {
      if (!isAssignedFiveDigit(fiveDigitNumber)) {
        if (display.getThree().getCode().isEmpty()) {
          isThree(fiveDigitNumber);
        } else if (display.getFive().getCode().isEmpty()) {
          isFive(fiveDigitNumber);
        } else if (display.getTwo().getCode().isEmpty()) {
          isTwo(fiveDigitNumber);
        }
      }
    }
  }

  private boolean isAssignedFiveDigit(String number) {
    char sort[] = number.toCharArray();
    Arrays.sort(sort);
    number = new String(sort);
    return display.getThree().getCode().equals(number) || display.getFive().getCode().equals(number) ||
        display.getTwo().getCode().equals(number);
  }

  private void isTwo(String number) {
    if (!display.getThree().getCode().isEmpty() && !display.getFive().getCode().isEmpty()) {
      display.getTwo().setCode(number);
      return;
    }
    return;
  }

  private void isThree(String number) {
    if (!display.getTwo().getCode().isEmpty() && !display.getFive().getCode().isEmpty()) {
      display.getThree().setCode(number);
      return;
    }
    if (!display.getSeven().getCode().isEmpty()) {
      for (int i = 0; i < display.getSeven().getCode().length(); i++) {
        if (!number.contains(display.getSeven().getCode().charAt(i) + "")) return;
      }
      display.getThree().setCode(number);
      return;
    }
  }

  private void isFive(String number) {
    if (!display.getTwo().getCode().isEmpty() && !display.getThree().getCode().isEmpty()) {
      display.getFive().setCode(number);
      return;
    }
    if (!display.getSix().getCode().isEmpty()) {
      for (int i = 0; i < number.length(); i++) {
        if (!display.getSix().getCode().contains(number.charAt(i) + "")) return;
      }
      display.getFive().setCode(number);
      return;
    }
    if (!display.getNine().getCode().isEmpty()) {
      for (int i = 0; i < number.length(); i++) {
        if (!display.getNine().getCode().contains(number.charAt(i) + "")) return;
      }
      display.getFive().setCode(number);
      return;
    }

    return;
  }

  private void getSixDigitNumber(List<String> sixes) {
    for (String sixDigitNumber : sixes) {
      if (!isAssignedSixDigit(sixDigitNumber)) {
        if (display.getNine().getCode().isEmpty()) {
          isNine(sixDigitNumber);
        } else if (display.getZero().getCode().isEmpty()) {
          isZero(sixDigitNumber);
        } else if (display.getSix().getCode().isEmpty()) {
          isSix(sixDigitNumber);
        }
      }
    }
  }

  private boolean isAssignedSixDigit(String number) {
    char sort[] = number.toCharArray();
    Arrays.sort(sort);
    number = new String(sort);
    return display.getNine().getCode().equals(number) || display.getZero().getCode().equals(number) ||
        display.getSix().getCode().equals(number);
  }

  private void isZero(String number) {
    if (!display.getSix().getCode().isEmpty() && !display.getNine().getCode().isEmpty()) {
      display.getZero().setCode(number);
      return;
    }
    if (!display.getSeven().getCode().isEmpty()) {
      for (int i = 0; i < display.getSeven().getCode().length(); i++) {
        if (!number.contains(display.getSeven().getCode().charAt(i) + "")) return;
      }
    }
    if (!display.getOne().getCode().isEmpty()) {
      for (int i = 0; i < display.getOne().getCode().length(); i++) {
        if (!number.contains(display.getOne().getCode().charAt(i) + "")) return;
      }
      display.getZero().setCode(number);
      return;
    }

    return;
  }

  private void isSix(String number) {
    if (!display.getNine().getCode().isEmpty() && !display.getZero().getCode().isEmpty()) {
      display.getSix().setCode(number);
      return;
    }

    return;
  }

  private void isNine(String number) {
    if (!display.getFour().getCode().isEmpty()) {
      for (int i = 0; i < display.getFour().getCode().length(); i++) {
        if (!number.contains(display.getFour().getCode().charAt(i) + "")) return;
      }
      display.getNine().setCode(number);
      return;
    }

    return;
  }

  private void resetMapping(Map<Integer, List<String>> map) {
    for (Map.Entry<Integer, List<String>> entry : map.entrySet()) {
      entry.getValue().clear();
    }
    display = new Display();
  }

  private void mapDigits(String output, Map<Integer, List<String>> map) {
    String[] values = output.split(" ");
    for (String value : values) {
      map.get(value.length()).add(value);
    }
  }

  private void countUniqueDigits(String output) {
    String[] values = output.split(" ");
    for (String value : values) {
      if (value.length() == 2 || value.length() == 3 || value.length() == 4 || value.length() == 7) {
        sum++;
      }
    }
  }

  public class Display {
    private Number zero;
    private Number one;
    private Number two;
    private Number three;
    private Number four;
    private Number five;
    private Number six;
    private Number seven;
    private Number eight;
    private Number nine;

    public Display() {
      this.zero = new Number("0", "");
      this.one = new Number("1", "");
      this.two = new Number("2", "");
      this.three = new Number("3", "");
      this.four = new Number("4", "");
      this.five = new Number("5", "");
      this.six = new Number("6", "");
      this.seven = new Number("7", "");
      this.eight = new Number("8", "");
      this.nine = new Number("9", "");
    }

    public int determinedNumbers() {
      int total = 0;
      if (!this.zero.getCode().isEmpty()) total++;
      if (!this.one.getCode().isEmpty()) total++;
      if (!this.two.getCode().isEmpty()) total++;
      if (!this.three.getCode().isEmpty()) total++;
      if (!this.four.getCode().isEmpty()) total++;
      if (!this.five.getCode().isEmpty()) total++;
      if (!this.six.getCode().isEmpty()) total++;
      if (!this.seven.getCode().isEmpty()) total++;
      if (!this.eight.getCode().isEmpty()) total++;
      if (!this.nine.getCode().isEmpty()) total++;
      return total;
    }

    public Number getZero() {
      return zero;
    }

    public void setZero(Number zero) {
      this.zero = zero;
    }

    public Number getOne() {
      return one;
    }

    public void setOne(Number one) {
      this.one = one;
    }

    public Number getTwo() {
      return two;
    }

    public void setTwo(Number two) {
      this.two = two;
    }

    public Number getThree() {
      return three;
    }

    public void setThree(Number three) {
      this.three = three;
    }

    public Number getFour() {
      return four;
    }

    public void setFour(Number four) {
      this.four = four;
    }

    public Number getFive() {
      return five;
    }

    public void setFive(Number five) {
      this.five = five;
    }

    public Number getSix() {
      return six;
    }

    public void setSix(Number six) {
      this.six = six;
    }

    public Number getSeven() {
      return seven;
    }

    public void setSeven(Number seven) {
      this.seven = seven;
    }

    public Number getEight() {
      return eight;
    }

    public void setEight(Number eight) {
      this.eight = eight;
    }

    public Number getNine() {
      return nine;
    }

    public void setNine(Number nine) {
      this.nine = nine;
    }
  }

  public class Number {
    private String number;
    private String code;

    public Number(String number, String code) {
      this.code = code;
      this.number = number;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      char sort[] = code.toCharArray();
      Arrays.sort(sort);
      String sorted = new String(sort);
      this.code = sorted;
    }

    public String getNumber() {
      return number;
    }

    public void setNumber(String number) {
      this.number = number;
    }
  }

}
