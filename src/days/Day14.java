package days;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import models.FileReaderHelper;

public class Day14 {

  public static void main(String[] args) {
    System.out.println("Part One Answer: " + new Day14().extendedPolymerizationPartOne());
    System.out.println("Part Two Answer: " + new Day14().extendedPolymerizationPartTwo());
  }

  private int extendedPolymerizationPartOne() {
    return Integer.parseInt(polymerize(10));
  }

  private long extendedPolymerizationPartTwo() {
    return Long.parseLong(polymerize(40));
  }

  private String polymerize(int rounds) {
    List<String> lines = new FileReaderHelper().readFileAsString("src/resources/day14/test-input.txt");
    LinkedHashMap<String, BigInteger> pairs = convertPolymerToPairs(lines.get(0));
    Map<String, Character> insertionPairs = lines.stream()
        .skip(2)
        .map(i -> i.split(" -> "))
        .collect(Collectors.toUnmodifiableMap(i -> i[0], i -> i[1].charAt(0)));

    for (int i = 0; i < rounds; i++) {
      pairs = polymerize(pairs, insertionPairs);
    }

    Map<Character, BigInteger> countCharacters = countCharacters(pairs);

    return String.valueOf(countCharacters.values().stream().max(Comparator.naturalOrder()).get()
        .subtract(countCharacters.values().stream().min(Comparator.naturalOrder()).get()));
  }

  private Map<Character, BigInteger> countCharacters(LinkedHashMap<String, BigInteger> polymer) {
    Map<Character, BigInteger> characterCounts = new HashMap<>();

    for (Map.Entry<String, BigInteger> pair : polymer.entrySet()) {
      char[] chars = pair.getKey().toCharArray();

      if (characterCounts.isEmpty()) {
        characterCounts.put(chars[0], polymer.get(pair.getKey()));
      }

      if (!characterCounts.containsKey(chars[1])) {
        characterCounts.put(chars[1], polymer.get(pair.getKey()));
      } else {
        characterCounts.put(chars[1], characterCounts.get(chars[1]).add(polymer.get(pair.getKey())));
      }
    }

    return characterCounts;
  }

  private LinkedHashMap<String, BigInteger> polymerize(LinkedHashMap<String, BigInteger> pairs, Map<String, Character> insertionPairs) {
    LinkedHashMap<String, BigInteger> newPairs = new LinkedHashMap<>();

    for (String key : pairs.keySet()) {
      char c = insertionPairs.get(key);
      String firstNewPair = new String(new char[]{key.charAt(0), c});
      String secondNewPair = new String(new char[]{c, key.charAt(1)});

      if (newPairs.containsKey(firstNewPair)) {
        newPairs.put(firstNewPair, newPairs.get(firstNewPair).add(pairs.get(key)));
      } else {
        newPairs.put(firstNewPair, pairs.get(key));
      }

      if (newPairs.containsKey(secondNewPair)) {
        newPairs.put(secondNewPair, newPairs.get(secondNewPair).add(pairs.get(key)));
      } else {
        newPairs.put(secondNewPair, pairs.get(key));
      }
    }

    return newPairs;
  }

  private LinkedHashMap<String, BigInteger> convertPolymerToPairs(String polymer) {
    LinkedHashMap<String, BigInteger> pairs = new LinkedHashMap<>();

    List<Character> polymerChars = Arrays.stream(polymer.split(""))
        .map(s -> s.charAt(0))
        .collect(Collectors.toList());

    for (int i = 0; i < polymerChars.size() - 1; i++) {
      String pair = new String(new char[]{polymerChars.get(i), polymerChars.get(i + 1)});
      if (pairs.containsKey(pair)) {
        pairs.put(pair, pairs.get(pair).add(BigInteger.ONE));
      } else {
        pairs.put(pair, BigInteger.ONE);
      }
    }

    return pairs;
  }
}
