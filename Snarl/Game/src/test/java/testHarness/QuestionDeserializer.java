package testHarness;

import com.google.gson.*;
import model.Level;
import model.LevelBuilder;
import model.Location;
import model.RoomBuilder;
import java.lang.reflect.Type;

/**
 * A deserializer for a Question.
 */
public class QuestionDeserializer implements JsonDeserializer<Question> {

  /**
   * Deserializes a question for the Snarl test harness.
   * @param jsonElement the element to deserialize
   * @param type the type to deserialize to
   * @param jsonDeserializationContext the context in which to continue deserialization
   * @return the question that needs to be deserialized
   * @throws JsonParseException if the input is not formed as expected for this question
   */
  @Override
  public Question deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    if (!jsonElement.isJsonArray()) {
      throw new JsonParseException("the JSON element must be an array");
    }

    JsonArray jsonArray = (JsonArray) jsonElement;
    JsonElement roomJSON = jsonArray.get(0);
    Location locationOfInterest = this.parseLocation(jsonArray.get(1));


    LevelBuilder levelBuilder = new LevelBuilder();
    if (!roomJSON.isJsonObject()) {
      throw new JsonParseException("The room json must be an object type");
    }
    JsonObject roomObject = (JsonObject) roomJSON;
    Location roomOrigin = this.parseLocation(roomObject.get("origin"));
    Location roomSize = this.parseLocation(roomObject.get("bounds"));

    RoomBuilder room = this.parseRoom(roomJSON, roomOrigin, roomSize);
    levelBuilder.addRoom(room);
    Level level = levelBuilder.build();

    return new LocationQuery(level, locationOfInterest, roomOrigin);
  }

  private RoomBuilder parseRoom(JsonElement jsonElement, Location roomOrigin, Location roomSize) throws JsonParseException {
    JsonObject jsonObject = (JsonObject) jsonElement;
    // TODO: we throw an error when origin is smaller than 1,1 (because that doesn't allow for boundary tiles)
    //  do we need to reassess that?

    RoomBuilder room = new RoomBuilder(roomOrigin.xCoordinate, roomOrigin.yCoordinate, roomSize.xCoordinate, roomSize.yCoordinate);
    this.calculateLayout(jsonObject.get("layout"), room, roomOrigin);
    return room;
  }

  private void calculateLayout(JsonElement jsonElement, RoomBuilder roomBuilder, Location origin) throws JsonParseException {
    JsonArray layout = this.getArray(jsonElement);

    for (int rowIdx = 0; rowIdx < layout.size(); rowIdx++) {
      JsonArray row = this.getArray(layout.get(rowIdx));
      for (int colIdx = 0; colIdx < row.size(); colIdx++) {
        int type = row.get(colIdx).getAsInt();
        boolean isBoundary = rowIdx == 0
            || colIdx == 0
            || rowIdx == layout.size() - 1
            || colIdx == row.size() - 1;

        this.addLandmarkToRoom(type, isBoundary,
            new Location(origin.xCoordinate + colIdx,
                origin.yCoordinate + rowIdx), roomBuilder);
      }
    }
  }

  private void addLandmarkToRoom(int type, boolean isBoundary, Location location, RoomBuilder roomBuilder) {
    Tile tileType = Tile.values()[type];

    switch (tileType) {
      case WALL:
        if (!isBoundary) {
          roomBuilder.addWall(location.xCoordinate, location.yCoordinate);
        }
        return;
      case WALKABLE:
        // the type of a landmark is a tile so we don't need to  add anything heredo
        return;
      case DOOR:
        roomBuilder.addDoor(location.xCoordinate, location.yCoordinate);
        return;
      default:
        throw new JsonParseException("The landmark to be added must be a tile, wall, or door");
    }
  }

  private JsonArray getArray(JsonElement jsonElement) throws JsonParseException {
    if (!jsonElement.isJsonArray()) {
      throw new JsonParseException("The jsonElement must be an array");
    }
    return (JsonArray) jsonElement;
  }

  private Location parseLocation(JsonElement jsonElement) throws JsonParseException {
    if (!jsonElement.isJsonArray()) {
      throw new JsonParseException("The jsonElement must be an array");
    }
    JsonArray jsonArray = (JsonArray) jsonElement;

    if (jsonArray.size() != 2) {
      throw new JsonParseException("The size of the json array must be exactly 2");
    }

    int x;
    int y;

    try {
      x = jsonArray.get(0).getAsInt();
      y = jsonArray.get(1).getAsInt();
    } catch (Exception exception) {
      // TODO check the specific kind of error
      throw new JsonParseException("Cannot get integer from json array");
    }

    return new Location(x, y);
  }

  /**
   * The different kind tiles in a layout
   */
  private enum Tile {
    WALL(0),
    WALKABLE(1),
    DOOR(2);

    final int value;

    Tile(int value) {
      this.value = value;
    }
  }

}
