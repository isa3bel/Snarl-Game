import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static org.junit.Assert.*;

import com.google.gson.JsonParseException;
import org.junit.Before;
import org.junit.Test;

public class CommandIntegrationTest {

  Gson gson;

  @Before
  public void setupGson() {
    this.gson = new GsonBuilder()
        .registerTypeAdapter(Command.class, new CommandDeserialization())
        .create();
  }

  // CommandDeserialization
  @Test
  public void invalidCommandType() {
    try {
      this.gson.fromJson("{ \"command\": \"garbage\", \"params\": { } }", Command.class);
      assertTrue(false);
    }
    catch (JsonParseException parseException) {
      assertEquals("unexpected command type, received: garbage", parseException.getMessage());
    }
  }

  @Test
  public void deserializeInvalidShape() {
    try {
      this.gson.fromJson("\"string, not an object\"", Command.class);
      assertTrue(false);
    } catch (JsonParseException parseException) {
      assertEquals("expected json object", parseException.getMessage());
    }
  }

  // MakeTown
  @Test
  public void deserializeInvalidRoadsCommand() {
    try {
      String json = "{ \"command\": \"roads\", \"params\": [ { \"notFrom\": \"A\", \"to\": \"B\" } ] }";
      this.gson.fromJson(json, Command.class);
      assertTrue(false);
    }
    catch (IllegalArgumentException illegalArgumentException) {
      assertEquals("to make a town, all roads must have a non-null to and from field", illegalArgumentException.getMessage());
    }
  }

  @Test
  public void deserializeValidBasicRoadsCommand() {
    String json = "{ \"command\": \"roads\", \"params\": [ { \"from\": \"A\", \"to\": \"B\" } ] }";
    Command command = this.gson.fromJson(json, Command.class);

    ClientTownNetworkGarbagePlaceholder mock = command.doCommand(null);
    assertEquals("A: B", mock.constructor);
    assertNull(mock.placeCharacter);
    assertNull(mock.canReachTownAlone);
  }

  @Test
  public void deserializeValidLongRoadsCommand() {
    String json = "{ \"command\": \"roads\", " +
        "\"params\": [ { \"from\": \"A\", \"to\": \"B\" }, " +
        "{ \"from\": \"A\", \"to\": \"C\" }, " +
        "{ \"from\": \"B\", \"to\": \"A\" }, " +
        "{ \"from\": \"C\", \"to\": \"A\" }, " +
        "{ \"from\": \"D\", \"to\": \"B\" }, " +
        "{ \"from\": \"B\", \"to\": \"D\" } ] }";
    Command command = this.gson.fromJson(json, Command.class);

    ClientTownNetworkGarbagePlaceholder mock = command.doCommand(null);
    assertEquals("A: B, C; B: A, D; C: A; D: B", mock.constructor);
    assertNull(mock.placeCharacter);
    assertNull(mock.canReachTownAlone);
  }

  @Test
  public void makeTownDoCommandMustBeCalledWithNull() {
    String json = "{ \"command\": \"roads\", \"params\": [ { \"from\": \"A\", \"to\": \"B\" } ] }";
    Command command = this.gson.fromJson(json, Command.class);

    try {
      ClientTownNetworkGarbagePlaceholder nonNullTown = new ClientTownNetworkGarbagePlaceholder("");
      command.doCommand(nonNullTown);
      assertTrue(false);
    }
    catch (IllegalArgumentException illegalArgumentException) {
      assertEquals("creating the town must be the first command called", illegalArgumentException.getMessage());
    }
  }

  // PlaceCharacter
  @Test
  public void deserializeInvalidPlaceCharacterDoesntError() {
    String json = "{ \"command\": \"place\", \"params\": { \"notCharacter\": \"Ferd\", \"town\": \"Boston\" } }";
    Command command = this.gson.fromJson(json, Command.class);

    ClientTownNetworkGarbagePlaceholder nonNullTown = new ClientTownNetworkGarbagePlaceholder("");
    ClientTownNetworkGarbagePlaceholder result = command.doCommand(nonNullTown);
    assertEquals(result, nonNullTown);
    assertNull(result.placeCharacter);
    assertNull(result.canReachTownAlone);
  }

  @Test
  public void placeCharacterIsCalledWithNames() {
    String json = "{ \"command\": \"place\", \"params\": { \"character\": \"Ferd\", \"town\": \"Boston\" } }";
    Command command = this.gson.fromJson(json, Command.class);

    ClientTownNetworkGarbagePlaceholder nonNullTown = new ClientTownNetworkGarbagePlaceholder("");
    ClientTownNetworkGarbagePlaceholder result = command.doCommand(nonNullTown);
    assertEquals(result, nonNullTown);
    assertEquals("Ferd Boston", result.placeCharacter);
    assertNull(result.canReachTownAlone);
  }

  @Test
  public void placeCharacterCannotBeCalledWithNull() {
    String json = "{ \"command\": \"place\", \"params\": { \"character\": \"Ferd\", \"town\": \"Boston\" } }";
    Command command = this.gson.fromJson(json, Command.class);

    try {
      command.doCommand(null);
      assertTrue(false);
    }
    catch (IllegalArgumentException illegalArgumentException) {
      assertEquals("must create a town network before executing commands on the network", illegalArgumentException.getMessage());
    }
  }

  // PassageSafe
  @Test
  public void deserializeInvalidPassageSageDoesntError() {
    String json = "{ \"command\": \"passage-safe?\", \"params\": { \"notCharacter\": \"Ferd\", \"town\": \"Boston\" } }";
    Command command = this.gson.fromJson(json, Command.class);

    ClientTownNetworkGarbagePlaceholder nonNullTown = new ClientTownNetworkGarbagePlaceholder("");
    ClientTownNetworkGarbagePlaceholder result = command.doCommand(nonNullTown);
    assertEquals(result, nonNullTown);
    assertNull(result.placeCharacter);
    assertNull(result.canReachTownAlone);
  }

  @Test
  public void passageSafeIsCalledWithNames() {
    String json = "{ \"command\": \"passage-safe?\", \"params\": { \"character\": \"Ferd\", \"town\": \"Boston\" } }";
    Command command = this.gson.fromJson(json, Command.class);

    ClientTownNetworkGarbagePlaceholder nonNullTown = new ClientTownNetworkGarbagePlaceholder("");
    ClientTownNetworkGarbagePlaceholder result = command.doCommand(nonNullTown);
    assertEquals(result, nonNullTown);
    assertEquals("Ferd Boston", result.canReachTownAlone);
    assertNull(result.placeCharacter);
  }

  @Test
  public void passageSafeCannotBeCalledWithNull() {
    String json = "{ \"command\": \"passage-safe?\", \"params\": { \"character\": \"Ferd\", \"town\": \"Boston\" } }";
    Command command = this.gson.fromJson(json, Command.class);

    try {
      command.doCommand(null);
      assertTrue(false);
    }
    catch (IllegalArgumentException illegalArgumentException) {
      assertEquals("must create a town network before executing commands on the network", illegalArgumentException.getMessage());
    }
  }
}