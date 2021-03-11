package testHarness.deserializer;

import com.google.gson.*;
import model.GameManager;
import model.builders.LevelBuilder;
import model.characters.Adversary;
import model.characters.Character;
import model.level.Level;
import model.level.Location;
import testHarness.answer.StateAnswer;
import testHarness.query.MockPlayer;
import testHarness.query.StateQuery;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A deserializer for a Question.
 */
public class StateQueryDeserializer implements JsonDeserializer<StateAnswer> {

  MockPlayer player;

  /**
   * Deserializes a question for the Snarl test harness.
   * @param wrapper the wrapper around the query to deserialize (e.g. { "query": queryJson })
   * @param type the type to deserialize to
   * @param context the context in which to continue deserialization
   * @return the question that needs to be deserialized
   * @throws JsonParseException if the input is not formed as expected for this question
   */
  @Override
  public StateAnswer deserialize(JsonElement wrapper, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    JsonElement jsonElement = wrapper.getAsJsonObject().get("query");
    if (!jsonElement.isJsonArray()) {
      throw new JsonParseException("Expecting a JSON array, received: " + jsonElement.toString());
    }
    JsonArray jsonArray = jsonElement.getAsJsonArray();
    String playerName = jsonArray.get(1).getAsString();
    GameManager gameManager = this.parseGameManager(jsonArray.get(0), playerName, context);
    Location location = context.deserialize(jsonArray.get(2), Location.class);
    if (this.player != null) this.player.setNextLocation(location);

    StateQuery query = new StateQuery(gameManager, player);
    return setupStateAnswer(query, jsonArray.get(0), playerName, location);
  }

  /**
   * Parse the GameManager from the state element.
   * @param jsonElement the state to parse the GameManager from
   * @param context the context in which to continue deserialization
   * @return the game manager that was parsed
   */
  private GameManager parseGameManager(JsonElement jsonElement, String playerName, JsonDeserializationContext context) {
    if (!jsonElement.isJsonObject()) {
      throw new IllegalArgumentException("expected state to be a json object");
    }
    JsonObject state = jsonElement.getAsJsonObject();

    LevelBuilder levelBuilder = context.deserialize(state.get("level"), LevelBuilder.class);
    levelBuilder.setExitLocked(state.get("exit-locked").getAsBoolean());
    Level[] levels = new Level[]{ levelBuilder.build() };

    ArrayList<Character> characters = new ArrayList<>();
    MockPlayer[] players = context.deserialize(state.get("players"), MockPlayer[].class);
    characters.addAll(Arrays.asList(players));
    Adversary[] adversaries = context.deserialize(state.get("adversaries"), Adversary[].class);
    characters.addAll(Arrays.asList(adversaries));

    this.player = Arrays.stream(players).filter(p -> p.getName().equals(playerName)).findFirst().orElse(null);

    return new GameManager(0, levels, characters);
  }

  /**
   * Calculates and adds filler values for the StateAnswer to this StateQuery
   * @param query the query to get the answer of
   * @param jsonObject the json object that will have the room and hallway json
   * @param playerName the name of the player for this query
   * @param location the location of this query
   * @return the answer object of this query with filled in values
   */
  private static StateAnswer setupStateAnswer(StateQuery query, JsonElement jsonObject,
                                              String playerName, Location location) {
    JsonElement levelJson = jsonObject.getAsJsonObject().get("level");
    JsonElement roomsList = levelJson.getAsJsonObject().get("rooms");
    JsonElement hallwaysList = levelJson.getAsJsonObject().get("hallways");
    StateAnswer answer = query.getAnswer();
    return answer
        .setPlayerName(playerName)
        .setRooms(roomsList.toString())
        .setHallways(hallwaysList.toString())
        .setQueryLocation(location);
  }
}
