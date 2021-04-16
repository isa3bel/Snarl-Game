package model.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.builders.HallwayBuilder;
import model.builders.LevelBuilder;
import model.builders.RoomBuilder;
import model.level.Level;
import model.level.Location;
import testHarness.deserializer.HallwayBuilderDeserializer;
import testHarness.deserializer.LevelBuilderDeserializer;
import testHarness.deserializer.LocationDeserializer;
import testHarness.deserializer.RoomBuilderDeserializer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Reads an array of levels from a file.
 */
public class LevelFileReader {

  private final Scanner scanner;
  private static final Gson gson = new GsonBuilder()
      .registerTypeAdapter(LevelBuilder.class, new LevelBuilderDeserializer())
      .registerTypeAdapter(RoomBuilder.class, new RoomBuilderDeserializer())
      .registerTypeAdapter(HallwayBuilder.class, new HallwayBuilderDeserializer())
      .registerTypeAdapter(Location.class, new LocationDeserializer())
      .create();

  public LevelFileReader(String filename) throws RuntimeException {
    try {
      this.scanner = new Scanner(new File(filename));
    } catch (IOException e) {
      throw new RuntimeException("Cannot find the " + filename + " file.");
    }
  }

  /**
   * Reads the number of levels in this level file.
   * @return the number of levels in this file
   */
  private int readNumberOfLevels() {
    int numberOfLevels = Integer.parseInt(scanner.nextLine());
    if (numberOfLevels == 0) {
      throw new IllegalArgumentException("There should be at least 1 level");
    }
    // the next line is guaranteed to be a empty line, so we skip it.
    scanner.nextLine();
    return numberOfLevels;
  }

  /**
   * Reads the strings representing a level from the scanner file.
   * @return an array list of all the strings in this level file
   */
  private String[] readLevelStrings() {
    StringBuilder stringBuilder = new StringBuilder();
    while (scanner.hasNext()) {
      stringBuilder.append(scanner.nextLine()).append("\n");
    }
    return stringBuilder.toString().split("\n\n");
  }

  /**
   * Reads and creates an array of Levels from the file provided in program arguments.
   * @return the array of levels that will be used in the game
   */
  public Level[] getLevels() {
    int numLevels = this.readNumberOfLevels();
    String[] levelStrings = this.readLevelStrings();

    if (levelStrings.length < numLevels) {
      throw new IllegalArgumentException("The number of levels provided in the file should equal the number of level JSON objects");
    }

    return Arrays.stream(levelStrings)
        .map(levelString -> gson.fromJson(levelString, LevelBuilder.class).build())
        .toArray(Level[]::new);
  }
}
