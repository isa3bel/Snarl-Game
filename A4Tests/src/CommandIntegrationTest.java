package src;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class CommandIntegrationTest {

  Gson gson;
  PrintStream stream;
  BufferedReader reader;

  @Before
  public void setupGson() {
    this.gson = new GsonBuilder()
        .registerTypeAdapter(Command.class, new CommandDeserialization
            ())
        .create();
  }

  public void setupReadWrite() {
    PipedOutputStream out = new PipedOutputStream();
    this.stream = new PrintStream(out);
    try {
      PipedInputStream in = new PipedInputStream(out);
      this.reader = new BufferedReader(new InputStreamReader(in));
    }
    catch (IOException ioException) {
      fail();
    }
  }

  // CommandDeserialization
  @Test
  public void invalidCommandType() {
    try {
      this.gson.fromJson("{ \"command\": \"garbage\", \"params\": { } }", Command.class);
      fail();
    }
    catch (JsonParseException parseException) {
      assertEquals("unexpected command type, received: garbage", parseException.getMessage());
    }
  }

  @Test
  public void deserializeInvalidShape() {
    try {
      this.gson.fromJson("\"string, not an object\"", Command.class);
      fail();
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
      fail();
    }
    catch (JsonParseException exception) {
      assertEquals("invalid create town request", exception.getMessage());
    }
  }

  @Test
  public void deserializeValidBasicRoadsCommand() {
    String json = "{ \"command\": \"roads\", \"params\": [ { \"from\": \"A\", \"to\": \"B\" } ] }";
    Command command = this.gson.fromJson(json, Command.class);

    TcpMock mock = new TcpMock();
    DoCommand doCommand = new DoCommand(mock);
    doCommand.accept(command);

    String expected = "{\"towns\":[\"B\",\"A\"],\"roads\":[{\"from\":\"A\",\"to\":\"B\"}]}";
    assertEquals(expected, mock.sent);
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

    TcpMock mock = new TcpMock();
    DoCommand doCommand = new DoCommand(mock);
    doCommand.accept(command);

    String expected = "{\"towns\":[\"B\",\"A\",\"C\",\"D\"]," +
        "\"roads\":[{\"from\":\"A\",\"to\":\"B\"},{\"from\":\"A\",\"to\":\"C\"}," +
        "{\"from\":\"B\",\"to\":\"A\"},{\"from\":\"C\",\"to\":\"A\"}," +
        "{\"from\":\"D\",\"to\":\"B\"},{\"from\":\"B\",\"to\":\"D\"}]}";
    assertEquals(expected, mock.sent);
  }

  @Test
  public void makeTownCanOnlyBeCalledOnce() {
    String json = "{ \"command\": \"roads\", \"params\": [ { \"from\": \"A\", \"to\": \"B\" } ] }";
    Command command = this.gson.fromJson(json, Command.class);

    TcpMock mock = new TcpMock();
    DoCommand doCommand = new DoCommand(mock);
    doCommand.accept(command);

    try {
      doCommand.accept(command);
      fail();
    }
    catch (IllegalStateException exception) {
      assertEquals("you've already created a town", exception.getMessage());
    }
  }

  // PlaceCharacter
  @Test
  public void placeCharacterDoesntSendMessage() {
    String makeTownJson = "{ \"command\": \"roads\", \"params\": [ { \"from\": \"A\", \"to\": \"B\" } ] }";
    Command makeTown = this.gson.fromJson(makeTownJson, Command.class);
    String placeCharacterJson = "{ \"command\": \"place\", \"params\": { \"character\": \"Ferd\", \"town\": \"B\" } }";
    Command placeCharacter = this.gson.fromJson(placeCharacterJson, Command.class);

    TcpMock mock = new TcpMock();
    DoCommand doCommand = new DoCommand(mock);
    doCommand.accept(makeTown);
    doCommand.accept(placeCharacter);

    String expected = "{\"towns\":[\"B\",\"A\"],\"roads\":[{\"from\":\"A\",\"to\":\"B\"}]}";
    assertEquals(expected, mock.sent);
    assertNull(mock.message);
    assertNull(mock.response);
  }

  @Test
  public void placeCharacterCannotBeCalledBeforeCreatingTown() {
    String placeCharacterJson = "{ \"command\": \"place\", \"params\": { \"character\": \"Ferd\", \"town\": \"Boston\" } }";
    Command placeCharacter = this.gson.fromJson(placeCharacterJson, Command.class);

    TcpMock mock = new TcpMock();
    DoCommand doCommand = new DoCommand(mock);

    try {
      doCommand.accept(placeCharacter);
      fail();
    }
    catch (IllegalStateException stateException) {
      assertEquals("town must be created before placing a character", stateException.getMessage());
    }
  }

  // PassageSafe
  @Test
  public void passageSafeSendsAMessage() {
    String response = "{ \"invalid\" : [],\n  \"response\" :  true }";
    TcpMock mock = new TcpMock("", response);
    DoCommand doCommand = new DoCommand(mock, this.stream);

    String makeTownJson = "{ \"command\": \"roads\", " +
        "\"params\": [ { \"from\": \"A\", \"to\": \"B\" } ] }";
    Command makeTown = this.gson.fromJson(makeTownJson, Command.class);
    doCommand.accept(makeTown);

    String placeCharacterJson = "{ \"command\": \"place\", " +
        "\"params\": { \"character\": \"Ferd\", \"town\": \"B\" } }";
    Command placeCharacter = this.gson.fromJson(placeCharacterJson, Command.class);
    doCommand.accept(placeCharacter);

    String passageSafeJson = "{ \"command\": \"passage-safe?\", " +
        "\"params\": { \"character\": \"Ferd\", \"town\": \"B\" } }";
    Command passageSafe = this.gson.fromJson(passageSafeJson, Command.class);
    doCommand.accept(passageSafe);

    String expected = "{\"characters\":[{\"name\":\"Ferd\",\"town\":\"B\"}]," +
        "\"query\":{\"character\":\"Ferd\",\"destination\":\"B\"}}";
    assertEquals(expected, mock.sent);
  }

  @Test
  public void passageSafeCombinesAllPlaceCharacterToBatchRequest() {
    String response = "{ \"invalid\" : [],\n  \"response\" :  true }";
    TcpMock mock = new TcpMock("", response);
    DoCommand doCommand = new DoCommand(mock, this.stream);

    String makeTownJson = "{ \"command\": \"roads\", " +
        "\"params\": [ { \"from\": \"A\", \"to\": \"B\" }, " +
        "{ \"from\": \"B\", \"to\": \"C\" }, " +
        "{ \"from\": \"A\", \"to\": \"D\" }, " +
        "{ \"from\": \"C\", \"to\": \"E\" } ] }";
    Command makeTown = this.gson.fromJson(makeTownJson, Command.class);
    doCommand.accept(makeTown);

    String placeCharacterJson = "{ \"command\": \"place\", " +
        "\"params\": { \"character\": \"Ferd\", \"town\": \"B\" } }";
    Command placeCharacter = this.gson.fromJson(placeCharacterJson, Command.class);
    doCommand.accept(placeCharacter);

    String placeCharacterJson2 = "{ \"command\": \"place\", " +
        "\"params\": { \"character\": \"Other\", \"town\": \"A\" } }";
    Command placeCharacter2 = this.gson.fromJson(placeCharacterJson2, Command.class);
    doCommand.accept(placeCharacter2);

    String placeCharacterJson3 = "{ \"command\": \"place\", " +
        "\"params\": { \"character\": \"Third\", \"town\": \"E\" } }";
    Command placeCharacter3 = this.gson.fromJson(placeCharacterJson3, Command.class);
    doCommand.accept(placeCharacter3);

    String passageSafeJson = "{ \"command\": \"passage-safe?\", " +
        "\"params\": { \"character\": \"Ferd\", \"town\": \"C\" } }";
    Command passageSafe = this.gson.fromJson(passageSafeJson, Command.class);
    doCommand.accept(passageSafe);

    String expected = "{\"characters\":[{\"name\":\"Ferd\",\"town\":\"B\"}," +
        "{\"name\":\"Other\",\"town\":\"A\"},{\"name\":\"Third\",\"town\":\"E\"}]," +
        "\"query\":{\"character\":\"Ferd\",\"destination\":\"C\"}}";
    assertEquals(expected, mock.sent);
  }


  @Test
  public void passageSafeResetsBatchRequest() {
    String response = "{ \"invalid\" : [],\n  \"response\" :  true }";
    TcpMock mock = new TcpMock("", response);
    DoCommand doCommand = new DoCommand(mock, this.stream);

    String makeTownJson = "{ \"command\": \"roads\", " +
        "\"params\": [ { \"from\": \"A\", \"to\": \"B\" }, " +
        "{ \"from\": \"B\", \"to\": \"C\" }, " +
        "{ \"from\": \"A\", \"to\": \"D\" }, " +
        "{ \"from\": \"C\", \"to\": \"E\" } ] }";
    Command makeTown = this.gson.fromJson(makeTownJson, Command.class);
    doCommand.accept(makeTown);

    String placeCharacterJson = "{ \"command\": \"place\", " +
        "\"params\": { \"character\": \"Ferd\", \"town\": \"B\" } }";
    Command placeCharacter = this.gson.fromJson(placeCharacterJson, Command.class);
    doCommand.accept(placeCharacter);

    String placeCharacterJson2 = "{ \"command\": \"place\", " +
        "\"params\": { \"character\": \"Other\", \"town\": \"A\" } }";
    Command placeCharacter2 = this.gson.fromJson(placeCharacterJson2, Command.class);
    doCommand.accept(placeCharacter2);

    String placeCharacterJson3 = "{ \"command\": \"place\", " +
        "\"params\": { \"character\": \"Third\", \"town\": \"E\" } }";
    Command placeCharacter3 = this.gson.fromJson(placeCharacterJson3, Command.class);
    doCommand.accept(placeCharacter3);

    String passageSafeJson = "{ \"command\": \"passage-safe?\", " +
        "\"params\": { \"character\": \"Ferd\", \"town\": \"C\" } }";
    Command passageSafe = this.gson.fromJson(passageSafeJson, Command.class);
    doCommand.accept(passageSafe);

    String expected = "{\"characters\":[{\"name\":\"Ferd\",\"town\":\"B\"}," +
        "{\"name\":\"Other\",\"town\":\"A\"},{\"name\":\"Third\",\"town\":\"E\"}]," +
        "\"query\":{\"character\":\"Ferd\",\"destination\":\"C\"}}";
    assertEquals(expected, mock.sent);

    String passageSafeJson2 = "{ \"command\": \"passage-safe?\", " +
        "\"params\": { \"character\": \"Ferd\", \"town\": \"D\" } }";
    Command passageSafe2 = this.gson.fromJson(passageSafeJson2, Command.class);
    doCommand.accept(passageSafe2);
    String emptyBatch = "{\"characters\":[],\"query\":{\"character\":\"Ferd\",\"destination\":\"D\"}}";
    assertEquals(emptyBatch, mock.sent);
  }

  @Test
  public void passageSafeCannotBeCalledBeforeCreatingTown() {
    String json = "{ \"command\": \"passage-safe?\", " +
        "\"params\": { \"character\": \"Ferd\", \"town\": \"Boston\" } }";
    Command command = this.gson.fromJson(json, Command.class);

    TcpMock mock = new TcpMock();
    DoCommand doCommand = new DoCommand(mock);

    try {
      doCommand.accept(command);
      fail();
    }
    catch (IllegalStateException exception) {
      assertEquals("town must be created before querying a passage", exception.getMessage());
    }
  }
}