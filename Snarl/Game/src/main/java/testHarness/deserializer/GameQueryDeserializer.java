package testHarness.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import model.GameManager;
import model.characters.Character;
import model.characters.Ghost;
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
    int natural = jsonArray.get(2).getAsJsonObject().getAsInt();

    StringBuilder testOutput = new StringBuilder();
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
      StringBuilder testOutput) {
    String[] nameArray = context.deserialize(jsonArray.get(0), String[].class);
    Level level = context.deserialize(jsonArray.get(1), Level.class);
    Location[] locations = context.deserialize(jsonArray.get(3), Location.class);
    MockPlayerController[] mockControllers = context.deserialize(jsonArray.get(4), MockPlayerController[].class);

    ArrayList<Character> characters = buildCharacters(nameArray, locations, mockControllers, testOutput);
    return new GameManager(0, new Level[]{ level }, characters);
  }

  /**
   * Creates the characters for this game.
   * @param nameArray the array of player names
   * @param locations the locations at which to place characters
   * @param mockControllers the MockControllers of the players
   * @return the list of characters for this test
   */
  private static ArrayList<Character> buildCharacters(String[] nameArray, Location[] locations,
      MockPlayerController[] mockControllers, StringBuilder testOutput) {
    ArrayList<Character> characters = new ArrayList<>();
    for (int idx = 0; idx < locations.length; idx++) {
      if (idx < nameArray.length) {
        mockControllers[idx].setTestOutput(testOutput);
        characters.add(new MockPlayer(locations[idx], idx, nameArray[idx], mockControllers[idx], testOutput));
      } else {
        characters.add(new Ghost(locations[idx], "" + idx));
      }
    }

    return characters;
  }

  /**
   * Calculates and adds filler values for the StateAnswer to this StateQuery
   * @param query the query to get the answer of
   * @param jsonObject the json object that will have the room and hallway json
   * @return the answer object of this query with filled in values
   */
  private static GameAnswer setupGameAnswer(GameQuery query, JsonElement jsonObject) {
    JsonElement levelJson = jsonObject.getAsJsonObject().get("level");
    JsonElement roomsList = levelJson.getAsJsonObject().get("rooms");
    JsonElement hallwaysList = levelJson.getAsJsonObject().get("hallways");

    GameAnswer answer = query.getAnswer();
    answer.setRooms(roomsList.toString());
    answer.setHallways(hallwaysList.toString());

    return answer;
  }
}
