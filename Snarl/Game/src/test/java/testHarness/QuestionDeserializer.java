package testHarness;

import com.google.gson.*;
import model.Level;
import model.LevelBuilder;
import model.Location;
import model.RoomBuilder;

import java.lang.reflect.Type;

/**
 *
 */
public class QuestionDeserializer implements JsonDeserializer<Question> {

  /**
   *
   * @param jsonElement
   * @param type
   * @param jsonDeserializationContext
   * @return
   * @throws JsonParseException
   */
  @Override
  public Question deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    if (!jsonElement.isJsonArray()) {
      throw new JsonParseException("invalid input TODO");
    }

    JsonArray jsonArray = (JsonArray) jsonElement;
    JsonElement room = jsonArray.get(0);
    Location locationOfInterest = this.parseLocation(jsonArray.get(1));


    LevelBuilder levelBuilder = new LevelBuilder();
    // check type of object here before parsing room
    levelBuilder.addRoom(this.parseRoom(room));
    Level level = levelBuilder.build();

    return new LocationQuery(level, locationOfInterest);
  }

  private RoomBuilder parseRoom(JsonElement jsonElement) throws JsonParseException {
    if (!jsonElement.isJsonObject()) {
       throw new JsonParseException("invalid input TODO");
    }
    JsonObject jsonObject = (JsonObject) jsonElement;
    // TODO: we throw an error when origin is smaller than 1,1 (because that doesn't allow for boundary tiles)
    //  do we need to reassess that?
    Location origin = this.parseLocation(jsonObject.get("origin"));
    Location bounds = this.parseLocation(jsonObject.get("bounds"));

    RoomBuilder room = new RoomBuilder(origin.xCoordinate, origin.yCoordinate, bounds.xCoordinate, bounds.yCoordinate);
    this.calculateLayout(jsonObject.get("layout"), room, origin);
    return room;
  }

  private void calculateLayout(JsonElement jsonElement, RoomBuilder roomBuilder, Location origin) throws JsonParseException {
    JsonArray layout = this.getArray(jsonElement);

    for (int rowIdx = 0; rowIdx < layout.size(); rowIdx++) {
      JsonArray row = this.getArray(layout.get(rowIdx));
      for (int colIdx = 0; colIdx < row.size(); colIdx++) {
        int type = row.get(colIdx).getAsInt();
        boolean isBoundary = rowIdx == 0 || colIdx == 0 || rowIdx == layout.size() - 1 || colIdx == row.size() - 1;

        this.addLandmarkToRoom(type, isBoundary,
            new Location(origin.xCoordinate + colIdx, origin.yCoordinate + rowIdx), roomBuilder);
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
        throw new JsonParseException("TODO invalid layout thing");
    }
  }

  private JsonArray getArray(JsonElement jsonElement) throws JsonParseException {
    if (!jsonElement.isJsonArray()) {
      throw new JsonParseException("TODO");
    }
    return (JsonArray) jsonElement;
  }

  private Location parseLocation(JsonElement jsonElement) throws JsonParseException {
    if (!jsonElement.isJsonArray()) {
      throw new JsonParseException("invalid input TODO");
    }
    JsonArray jsonArray = (JsonArray) jsonElement;

    if (jsonArray.size() != 2) {
      throw new JsonParseException("invalid input TODO");
    }

    int x;
    int y;

    try {
      x = jsonArray.get(0).getAsInt();
      y = jsonArray.get(1).getAsInt();
    } catch (Exception TODO) {
      throw new JsonParseException("invalid input TODO");
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
