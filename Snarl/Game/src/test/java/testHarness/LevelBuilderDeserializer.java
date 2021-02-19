package testHarness;

import com.google.gson.*;
import model.LevelBuilder;
import model.Location;
import model.RoomBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class LevelBuilderDeserializer extends JsonDeserializer<LevelBuilder> {

  /**
   *
   * @param jsonElement
   * @param type
   * @param jsonDeserializationContext
   * @return
   * @throws JsonParseException
   */

  @Override
  public LevelBuilder deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    if (!jsonElement.isJsonArray()) {
      throw new JsonParseException("invalid input TODO");
    }

    JsonArray jsonArray = (JsonArray) jsonElement;
    JsonElement room = jsonArray.get(0);

    LevelBuilder levelBuilder = new LevelBuilder();
    levelBuilder.addRoom(this.parseRoom(room));
    return levelBuilder;
  }

  private RoomBuilder parseRoom(JsonElement jsonElement) throws JsonParseException {
    if (!jsonElement.isJsonObject()) {
       throw new JsonParseException("invalid input TODO");
    }
    JsonObject jsonObject = (JsonObject) jsonElement;
    Location origin = this.parseLocation(jsonObject.get("origin"));
    Location bounds = this.parseLocation(jsonObject.get("bounds"));

    RoomBuilder room = new RoomBuilder(origin.x, origin.y, bounds.x, bounds.y);
    doors.forEach(door -> room.addDoor(door));

    return room;
  }

  private ArrayList<Location> calculateDoors(JsonElement layout, Location origin) {
    // ADD ALL THE DOORS

    // ADD ALL THE WALLS (ONLY INSIDE THE ROOM)

    return roomBuilder;
  }

    /*
  [ { "type" : "room",
    "origin" : [0, 1],
    "bounds" : [3, 5],
    "layout" : [ [0, 0, 2, 0, 0],
                 [0, 1, 1, 1, 0],
                 [0, 0, 2, 0, 0], ]
    },
    [2, 2]
  ]
   */

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
}
