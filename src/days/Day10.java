package days;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import models.FileReaderHelper;

public class Day10 {

  private List<SyntaxCharacter> illegalCharacters = new ArrayList<>();
  private List<String> incompleteLines = new ArrayList<>();

  public static void main(String[] args) {
    System.out.println("Part One Answer: " + new Day10().syntaxScoringPartOne());
    System.out.println("Part Two Answer: " + new Day10().syntaxScoringPartTwo());
  }

  private void setUpList() {
    illegalCharacters.add(new SyntaxCharacter(")", 0));
    illegalCharacters.add(new SyntaxCharacter("}", 0));
    illegalCharacters.add(new SyntaxCharacter("]", 0));
    illegalCharacters.add(new SyntaxCharacter(">", 0));
  }

  private int syntaxScoringPartOne() {
    setUpList();
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day10/part-one-input.txt");
    incompleteLines = new ArrayList<>(lines);

    for (String line : lines) {
      findIllegalCharacter(line.split(""), line);
    }

    return totalSyntaxErrorScore();
  }

  private long syntaxScoringPartTwo() {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day10/part-two-input.txt");
    List<Long> allScores = new ArrayList<>();
    incompleteLines = new ArrayList<>(lines);

    for (String line : lines) {
      findIllegalCharacter(line.split(""), line);
    }

    completeIncompleteLines(allScores);

    Collections.sort(allScores);
    return allScores.get(allScores.size() / 2);
  }

  private void completeIncompleteLines(List<Long> allScores) {
    Stack<String> stack = new Stack<>();
    for (String line : incompleteLines) {
      for (String character : line.split("")) {
        if (isOpenChunk(character)) {
          stack.add(character);
        } else if (closesChunk(stack.peek(), character)) {
          stack.pop();
        }
      }
      if (stack.size() > 0) {
        allScores.add(completionScore(stack));
      }
    }
  }

  private long completionScore(Stack<String> stack) {
    long totalScore = 0;

    do {
     totalScore *= 5;
     if (stack.peek().equals("(")) {
       totalScore += 1;
       stack.pop();
     } else if (stack.peek().equals("[")) {
       totalScore += 2;
       stack.pop();
     } else if (stack.peek().equals("{")) {
       totalScore += 3;
       stack.pop();
     } else if (stack.peek().equals("<")) {
       totalScore += 4;
       stack.pop();
     }
    } while (stack.size() != 0);

    return totalScore;
  }

  private int totalSyntaxErrorScore() {
    int total = 0;
    for (SyntaxCharacter sc : illegalCharacters) {
      if (sc.getCharacter().equals(")")) {
        total += 3 * sc.getCount();
      } else if (sc.getCharacter().equals("]")) {
        total += 57 * sc.getCount();
      } else if (sc.getCharacter().equals("}")) {
        total += 1197 * sc.getCount();
      } else if (sc.getCharacter().equals(">")) {
        total += 25137 * sc.getCount();
      }
    }

    return total;
  }

  private void findIllegalCharacter(String[] characters, String object) {
    Stack<String> stack = new Stack<>();
    for (String character : characters) {
      if (isOpenChunk(character)) {
        stack.add(character);
      } else if (closesChunk(stack.peek(), character)) {
        stack.pop();
      } else {
        incompleteLines.remove(object);
        increaseIllegalCharacter(character);
        return;
      }
    }
  }

  private void increaseIllegalCharacter(String character) {
    for (SyntaxCharacter sc : illegalCharacters) {
      if (sc.getCharacter().equals(character)) sc.setCount(sc.getCount() + 1);
    }
  }

  private boolean isOpenChunk(String character) {
    if (character.equals("(") || character.equals("[") || character.equals("{") ||
        character.equals("<")) {
      return true;
    }
    return false;
  }

  private boolean closesChunk(String top, String character) {
    if ((top.equals("(") && character.equals(")")) || (top.equals("[") && character.equals("]")) ||
        (top.equals("{") && character.equals("}")) || (top.equals("<") && character.equals(">"))) {
      return true;
    }
    return false;
  }

  public class SyntaxCharacter {
    private String character;
    private int count;

    public SyntaxCharacter(String character, int count) {
      this.character = character;
      this.count = count;
    }

    public String getCharacter() {
      return character;
    }

    public void setCharacter(String character) {
      this.character = character;
    }

    public int getCount() {
      return count;
    }

    public void setCount(int count) {
      this.count = count;
    }
  }
}
