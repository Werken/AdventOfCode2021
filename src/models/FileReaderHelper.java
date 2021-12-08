package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReaderHelper {

  public FileReaderHelper() {
  }

  public List<String> readFileAsString(String path) {
    List<String> lines = new ArrayList<>();
    try(BufferedReader reader = new BufferedReader(new FileReader(new File(path)))) {
      for (String line; (line = reader.readLine()) != null; ) {
        lines.add(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return lines;
  }

}
