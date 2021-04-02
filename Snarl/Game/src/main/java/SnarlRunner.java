import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.*;
import model.observer.LocalObserver;
import model.builders.GameManagerBuilder;
import model.builders.HallwayBuilder;
import model.builders.LevelBuilder;
import model.builders.RoomBuilder;
import model.level.Level;
import model.level.Location;
import model.ruleChecker.GameStatus;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import testHarness.deserializer.HallwayBuilderDeserializer;
import testHarness.deserializer.LevelBuilderDeserializer;
import testHarness.deserializer.LocationDeserializer;
import testHarness.deserializer.RoomBuilderDeserializer;

/**
 * Runs the Snarl game.
 */
public class SnarlRunner {

  private static final String LEVELS_ARGNAME = "levels";
  private static final String PLAYER_ARGNAME = "players";
  private static final String START_ARGNAME = "start";
  private static final String OBSERVE_ARGNAME = "observer";
  private static String levelFileName = "snarl.levels";
  private static int numPlayers = 1;
  private static int startingLevel = 1;
  private static boolean showObserverView = false;
  private static int numberOfLevels = 0;

  private static final Option LEVELS = Option.builder()
      .argName(String.valueOf(LEVELS_ARGNAME.charAt(0)))
      .longOpt(LEVELS_ARGNAME)
      .desc("The levels of the Snarl game")
      .hasArg()
      .numberOfArgs(1)
      .required(false)
      .build();

  private static final Option PLAYERS = Option.builder()
      .argName(String.valueOf(PLAYER_ARGNAME.charAt(0)))
      .longOpt(PLAYER_ARGNAME)
      .desc("The number of players")
      .hasArg()
      .numberOfArgs(1)
      .required(false)
      .build();

  private static final Option START = Option.builder()
      .argName(String.valueOf(START_ARGNAME.charAt(0)))
      .longOpt(START_ARGNAME)
      .desc("The level to start from")
      .hasArg()
      .numberOfArgs(1)
      .required(false)
      .build();

  private static final Option OBSERVE = Option.builder()
      .argName(String.valueOf(OBSERVE_ARGNAME.charAt(0)))
      .longOpt(OBSERVE_ARGNAME)
      .desc("Whether the observer view should be present")
      .hasArg()
      .numberOfArgs(0)
      .required(false)
      .build();

  private static final Gson gson = new GsonBuilder()
      .registerTypeAdapter(LevelBuilder.class, new LevelBuilderDeserializer())
      .registerTypeAdapter(RoomBuilder.class, new RoomBuilderDeserializer())
      .registerTypeAdapter(HallwayBuilder.class, new HallwayBuilderDeserializer())
      .registerTypeAdapter(Location.class, new LocationDeserializer())
      .create();

  public static void main(String[] args) {
    Options options = new Options();
    options.addOption(LEVELS).addOption(PLAYERS).addOption(START).addOption(OBSERVE);
    parseArguments(args, options);

    //The actual file needs to be in Game/ directory
    Scanner scanner;
    try {
      scanner = new Scanner(new File(levelFileName));
    } catch (IOException e) {
      throw new RuntimeException("Cannot find the " + levelFileName + " file.");
    }

    Level[] levels = getLevels(scanner);
    Scanner scanInput = new Scanner(System.in);
    GameManager gameManager = setupGame(scanInput, levels);
    if (showObserverView) {
      gameManager.addObserver(new LocalObserver());
    }
    gameManager.updatePlayers();

    GameStatus status;
    do {
      status = gameManager.doRound();
    } while (status.equals(GameStatus.PLAYING) || status.equals(GameStatus.ADVANCE));
  }

  private static GameManager setupGame(Scanner scanner, Level[] levels) {
    // subtract startingLevel by 1 since level array index starts at 0
    GameManagerBuilder gmBuilder = new GameManagerBuilder(startingLevel - 1, levels);

    for (int playerIdx = 0; playerIdx < numPlayers; playerIdx++) {
      System.out.print("Player " + playerIdx + " enter username: ");
      String userInput = scanner.nextLine();
      gmBuilder.addPlayer(userInput);
    }
    return gmBuilder.build();
  }

  private static Level[] getLevels(Scanner scanner) {
    StringBuilder stringBuilder = new StringBuilder();
    numberOfLevels = Integer.parseInt(scanner.nextLine());
    if (numberOfLevels == 0) {
      throw new IllegalArgumentException("There should be at least 1 level");
    }
    // the next line is guaranteed to be a empty line, so we skip it.
    scanner.nextLine();
    final List<String> levelString = new ArrayList<>();

    while (scanner.hasNext()) {
      String line = scanner.nextLine();
      if (line.equals("")) {
        levelString.add(stringBuilder.toString());
        stringBuilder = new StringBuilder();
      } else {
        stringBuilder.append(line);
      }
    }
    levelString.add(stringBuilder.toString());

    if (levelString.size() < numberOfLevels) {
      throw new IllegalArgumentException("The number of levels provided in the file should equal the number of level JSON objects");
    }
    Level[] arrLevels = new Level[numberOfLevels];
    for (int i = 0; i < numberOfLevels; i++) {
      arrLevels[i] = gson.fromJson(levelString.get(i), LevelBuilder.class).build();
    }

    return arrLevels;
  }

  private static void parseArguments(String[] args, Options options) {
    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine commandLine = parser.parse(options, args);
      if (commandLine.hasOption(LEVELS_ARGNAME)) {
        levelFileName = commandLine.getOptionValue(LEVELS_ARGNAME);
      }
      if (commandLine.hasOption(PLAYER_ARGNAME)) {
        numPlayers = Integer.parseInt(commandLine.getOptionValue(PLAYER_ARGNAME));
      }
      if (commandLine.hasOption(START_ARGNAME)) {
        startingLevel = Integer.parseInt(commandLine.getOptionValue(START_ARGNAME));
      }
      if (commandLine.hasOption(OBSERVE_ARGNAME)) {
        showObserverView = true;
        numPlayers = 1;
      }
    }
    catch(ParseException exp ) {
      System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
    }
  }

}
