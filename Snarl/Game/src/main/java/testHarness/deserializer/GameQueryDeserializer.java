package testHarness.deserializer;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import model.GameManager;
import model.builders.LevelBuilder;
import model.characters.Adversary;
import model.characters.Ghost;
import model.characters.Player;
import model.level.Level;
import model.level.Location;
import testHarness.answer.GameAnswer;
import testHarness.query.GameQuery;
import testHarness.query.MockPlayer;
import testHarness.query.MockPlayerController;

public class GameQueryDeserializer implements JsonDeserializer<GameAnswer> {

  @Override
  public GameAnswer deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext context) throws JsonParseException {
    if (!jsonElement.isJsonArray()) {
      throw new JsonParseException("Expecting a JSON array, received: " + jsonElement.toString());
    }
    JsonArray jsonArray = jsonElement.getAsJsonArray();
    int natural = jsonArray.get(2).getAsInt();

    ArrayList<String> testOutput = new ArrayList<>();
    GameManager manager = parseGameManager(jsonArray, context, testOutput);

    GameQuery query = new GameQuery(natural, manager, testOutput);
    return setupGameAnswer(query, jsonArray.get(1));
  }

  /**
   * Parse the GameManager from the state element.
   * @param jsonArray the query state
   * @param context the context in which to parse the game manager
   * @return the game manager that was parsed
   */
  private static GameManager parseGameManager(JsonArray jsonArray, JsonDeserializationContext context,
                                              ArrayList<String> testOutput) {
    String[] nameArray = context.deserialize(jsonArray.get(0), String[].class);
    LevelBuilder levelBuilder = context.deserialize(jsonArray.get(1), LevelBuilder.class);
    Location[] locations = context.deserialize(jsonArray.get(3), Location[].class);
    MockPlayerController[] mockControllers = context.deserialize(jsonArray.get(4), MockPlayerController[].class);


    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Adversary> adversaries = new ArrayList<>();
    buildCharacters(nameArray, locations, mockControllers, testOutput, players, adversaries);

    Level level = levelBuilder.build();
    level.addAdversaries(adversaries);
    return new GameManager(0, new Level[]{ level }, players);
  }

  /**
   * Creates the characters for this game.
   * @param nameArray the array of player names
   * @param locations the locations at which to place characters
   * @param mockControllers the MockControllers of the players
   * @return the list of characters for this test
   */
  private static void buildCharacters(String[] nameArray, Location[] locations,
      MockPlayerController[] mockControllers, ArrayList<String> testOutput,
      ArrayList<Player> players, ArrayList<Adversary> adversaries) {
    for (int idx = 0; idx < locations.length; idx++) {
      if (idx < nameArray.length) {
        mockControllers[idx].setTestOutput(testOutput);
        players.add(new MockPlayer(locations[idx], nameArray[idx], mockControllers[idx], testOutput));
      } else {
        adversaries.add(new Ghost(locations[idx], "ghost" + adversaries.size()));
      }
    }
  }

  /**
   * Calculates and adds filler values for the StateAnswer to this StateQuery
   * @param query the query to get the answer of
   * @param jsonObject the json object that will have the room and hallway json
   * @return the answer object of this query with filled in values
   */
  private static GameAnswer setupGameAnswer(GameQuery query, JsonElement jsonObject) {
    JsonObject levelJson = jsonObject.getAsJsonObject();
    JsonElement roomsList = levelJson.get("rooms");
    JsonElement hallwaysList = levelJson.get("hallways");

    GameAnswer answer = query.getAnswer();
    answer.setRooms(roomsList.toString());
    answer.setHallways(hallwaysList.toString());

    return answer;
  }
}
