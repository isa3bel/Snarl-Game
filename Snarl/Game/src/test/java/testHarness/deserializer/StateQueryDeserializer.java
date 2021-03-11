package testHarness.deserializer;

import com.google.gson.*;
import model.*;
import model.Character;
import testHarness.answer.StateAnswer;
import testHarness.query.MockPlayer;
import testHarness.query.StateQuery;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A deserializer for a Question.
 */
public class StateQueryDeserializer implements JsonDeserializer<StateAnswer> {

  HashMap<String, MockPlayer> players;

  /**
   * Deserializes a question for the Snarl test harness.
   * @param jsonElement the element to deserialize
   * @param type the type to deserialize to
   * @param context the context in which to continue deserialization
   * @return the question that needs to be deserialized
   * @throws JsonParseException if the input is not formed as expected for this question
   */
  @Override
  public StateAnswer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    if (!jsonElement.isJsonArray()) {
      throw new JsonParseException("Expecting a JSON array, received: " + jsonElement.toString());
    }
    JsonArray jsonArray = (JsonArray) jsonElement;
    GameManager gameManager = this.parseGameManager(jsonArray.get(0), context);
    String playerName = jsonArray.get(1).getAsString();
    Location location = context.deserialize(jsonArray.get(2), Location.class);

    MockPlayer player = this.players.get(playerName);
    player.setNextLocation(location);

    StateQuery query = new StateQuery(gameManager, player);
    return setupStateAnswer(query, jsonArray.get(0), playerName, location);
  }

  /**
   * Parse the GameManager from the state element.
   * @param jsonElement the state to parse the GameManager from
   * @param context the context in which to continue deserialization
   * @return the game manager that was parsed
   */
  private GameManager parseGameManager(JsonElement jsonElement, JsonDeserializationContext context) {
    if (!jsonElement.isJsonObject()) {
      throw new IllegalArgumentException("expected state to be a json object");
    }
    JsonObject state = jsonElement.getAsJsonObject();

    LevelBuilder levelBuilder = context.deserialize(state.get("level"), LevelBuilder.class);
    levelBuilder.setExitLocked(state.get("exit-locked").getAsBoolean());
    Level[] levels = new Level[]{ levelBuilder.build() };

    this.players = context.deserialize(state.get("players"), HashMap.class);
    ArrayList<Character> characters = new ArrayList<>(this.players.values());
    Adversary[] adversaries = context.deserialize(state.get("adversaries"), Adversary[].class);
    characters.addAll(Arrays.asList(adversaries));

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
    String roomsList = levelJson.getAsJsonObject().get("rooms").getAsString();
    String hallwaysList = levelJson.getAsJsonObject().get("hallways").getAsString();
    StateAnswer answer = query.getAnswer();
    return answer
        .setPlayerName(playerName)
        .setRooms(roomsList)
        .setHallways(hallwaysList)
        .setQueryLocation(location);
  }
}
