package days;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import models.FileReaderHelper;

public class Day16 {

  private static final Map<String, String> hexToBin = new HashMap<>(){{
    put("0", "0000");
    put("1", "0001");
    put("2", "0010");
    put("3", "0011");
    put("4", "0100");
    put("5", "0101");
    put("6", "0110");
    put("7", "0111");
    put("8", "1000");
    put("9", "1001");
    put("A", "1010");
    put("B", "1011");
    put("C", "1100");
    put("D", "1101");
    put("E", "1110");
    put("F", "1111");
  }};

  private static final Map<String, String> threeDigitBinary = new HashMap<>(){{
    put("000", "0");
    put("001", "1");
    put("010", "2");
    put("011", "3");
    put("100", "4");
    put("101", "5");
    put("110", "6");
    put("111", "7");
  }};

  private Stack<String> values = new Stack<>();

  private int bitCount = 0;

  private int stopParsing = 0;

  private int versionCount = 0;

  public static void main(String[] args) {
    System.out.println("Part One Answer: " + new Day16().packetDecoderPartOne());
    System.out.println("Part Two Answer: " + new Day16().packetDecoderPartTwo());
  }

  private int packetDecoderPartOne() {
    String line = new FileReaderHelper().readFileAsString("src/resources/day16/test-input.txt").get(0);
    line = convertToBinary(line);

    readPackets(line, true);

    while (bitCount < stopParsing) {
      readPackets(line, false);
    }
    return versionCount;
  }

  private String packetDecoderPartTwo() {
    String line = new FileReaderHelper().readFileAsString("src/resources/day16/part-two-input.txt").get(0);
    line = convertToBinary(line);

    readPackets(line, true);

    while (bitCount < stopParsing) {
      readPackets(line, false);
    }
    values.add(")");

    return evaluateOperations().toString();
  }

  private BigInteger evaluateOperations() {
    String equation = values.toString().replaceAll(",", "");
    String compute = equation;
    while (compute.contains(")")) {
      Pattern pattern = Pattern.compile("(\\((\\+|\\*|min|max|<|>|=)\\s([0-9]\\s?)*\\))");
      Matcher matcher = pattern.matcher(compute);

      if (matcher.find()) {
        String solve = matcher.group();
        String result = solve;
        result = result.substring(1, result.length() - 1);
        String[] data = result.split(" ");
        if (data[0].equals("+")) {
          BigInteger sum = BigInteger.ZERO;
          for (String num : data) {
            if (!num.equals("+")) {
              sum = sum.add(new BigInteger(num));
            }
          }
          compute = compute.replace(solve, sum.toString());
        } else if (data[0].equals("*")) {
            BigInteger product = BigInteger.ONE;
            for (String num : data) {
              if (!num.equals("*")) {
                product = product.multiply(new BigInteger(num));
              }
            }
            compute = compute.replace(solve, product.toString());
        } else if (data[0].equals("min")) {
          BigInteger min = new BigInteger(String.valueOf(Integer.MAX_VALUE));
          for (String num : data) {
              if (!num.equals("min") && min.compareTo(new BigInteger(num)) > 0) {
                min = new BigInteger(num);
              }
            }
          compute = compute.replace(solve, min.toString());
        } else if (data[0].equals("max")) {
          BigInteger max = BigInteger.ZERO;
          for (String num : data) {
              if (!num.equals("max") && max.compareTo(new BigInteger(num)) < 0) {
                max = new BigInteger(num);
              }
            }
          compute = compute.replace(solve, max.toString());
        } else if (data[0].equals(">")) {
          BigInteger gt = new BigInteger(data[1]).compareTo(new BigInteger(data[2])) > 0 ? BigInteger.ONE : BigInteger.ZERO;
          compute = compute.replace(solve, gt.toString());
        } else if (data[0].equals("<")) {
          BigInteger lt = new BigInteger(data[1]).compareTo(new BigInteger(data[2])) < 0 ? BigInteger.ONE : BigInteger.ZERO;
          compute = compute.replace(solve, lt.toString());
        } else if (data[0].equals("=")) {
          BigInteger et = new BigInteger(data[1]).compareTo(new BigInteger(data[2])) == 0 ? BigInteger.ONE : BigInteger.ZERO;
          compute = compute.replace(solve, et.toString());
        }
      }
    }

    return new BigInteger(compute.substring(1, compute.length() - 1));
  }

  private void readPackets(String line, boolean isFirstPacket) {
    int typeID;
    versionCount += getBitVersionOrType(line);
    typeID = getBitVersionOrType(line);
    switch (typeID) {
      case 0:
        values.add("(+");
        readOperatorPackets(line, isFirstPacket);
        break;
      case 1:
        values.add("(*");
        readOperatorPackets(line, isFirstPacket);
        break;
      case 2:
        values.add("(min");
        readOperatorPackets(line, isFirstPacket);
        break;
      case 3:
        values.add("(max");
        readOperatorPackets(line, isFirstPacket);
        break;
      case 4:
        values.add(new BigInteger(getLiteralValue(line), 2).toString());
        if (isFirstPacket) stopParsing = bitCount;
        break;
      case 5:
        values.add("(>");
        readOperatorPackets(line, isFirstPacket);
        break;
      case 6:
        values.add("(<");
        readOperatorPackets(line, isFirstPacket);
        break;
      case 7:
        values.add("(=");
        readOperatorPackets(line, isFirstPacket);
        break;
    }
  }

  private void readOperatorPackets(String line, boolean isFirstPacket) {
    String lengthTypeID = line.charAt(bitCount) + "";
    bitCount++;
    if (lengthTypeID.equals("0") && bitCount < line.length() - 1) {
      int subPacketLength = getSubPacketLength(line);
      int startingBitCount = bitCount;
      while (startingBitCount + subPacketLength > bitCount) {
        readPackets(line, false);
      }
      if (isFirstPacket) stopParsing = subPacketLength + 7 + 15;
      else values.add(")");

    } else if (lengthTypeID.equals("1") && bitCount < line.length() - 1) {
      int subPacketNumber = getSubPacketNumber(line);
      while (subPacketNumber > 0) {
        readPackets(line, false);
        subPacketNumber--;
      }
      if (isFirstPacket) stopParsing = bitCount;
      else values.add(")");
    }
  }

  private int getSubPacketNumber(String line) {
    String length = line.substring(bitCount, bitCount + 11);
    bitCount += 11;
    return Integer.parseInt(length, 2);
  }

  private int getSubPacketLength(String line) {
    String length = line.substring(bitCount, bitCount + 15);
    bitCount += 15;
    return Integer.parseInt(length, 2);
  }

  private String getLiteralValue(String line) {
    String prefixBit = line.charAt(bitCount) + "";
    String literalValue = "";
    bitCount++;

    if (prefixBit.equals("1")) {
      do {
        literalValue += getBitLiteralValue(line);
        prefixBit = line.charAt(bitCount) + "";
        bitCount++;
      } while (!prefixBit.equals("0"));
    }
    literalValue += getBitLiteralValue(line);

    return literalValue;
  }

  private String getBitLiteralValue(String line) {
    String literalValue = line.substring(bitCount, bitCount + 4);
    bitCount += 4;
    return literalValue;
  }

  private int getBitVersionOrType(String line) {
    String versionString = line.substring(bitCount, bitCount + 3);
    bitCount += 3;
    return Integer.parseInt(threeDigitBinary.get(versionString));
  }

  private String convertToBinary(String line) {
    String binary = "";
    for (String hex : line.split("")) {
      binary += hexToBin.get(hex);
    }

    return binary;
  }

}
