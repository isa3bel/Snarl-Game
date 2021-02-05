package src;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Mutable function for commands that runs a command and updates the townNetwork.
 */
class DoCommand implements Consumer<Command>, Command.Visitor {

  private Gson gson;
  private Tcp tcpConnection;
  private boolean townHasBeenCreated;
  private ArrayList<PlaceCharacter> batchRequest;
  private PrintStream out;

  DoCommand(Tcp tcpConnection) {
    this.gson = new Gson();
    this.tcpConnection = tcpConnection;
    this.townHasBeenCreated = false;
    this.batchRequest = new ArrayList<>();
    this.out = System.out;
  }

  // TODO: setup mockito?
  DoCommand(Tcp tcpConnection, PrintStream out) {
    this.gson = new Gson();
    this.tcpConnection = tcpConnection;
    this.townHasBeenCreated = false;
    this.batchRequest = new ArrayList<>();
    this.out = out;
  }

  public void accept(Command command) {
    command.accept(this);
  }

  @Override
  public void visitMakeTown(MakeTown makeTown) throws IllegalStateException {
    if (this.townHasBeenCreated) {
      throw new IllegalStateException("you've already created a town");
    }

    String jsonRequest = this.gson.toJson(makeTown);
    this.tcpConnection.sendMessage(jsonRequest);
    this.townHasBeenCreated = true;
  }

  @Override
  public void visitPlaceCharacter(PlaceCharacter placeCharacter) throws IllegalStateException {
    if (!this.townHasBeenCreated) {
      throw new IllegalStateException("town must be created before placing a character");
    }

    this.batchRequest.add(placeCharacter);
  }

  @Override
  public void visitPassageSafe(PassageSafe passageSafe) throws IllegalStateException {
    if (!this.townHasBeenCreated) {
      throw new IllegalStateException("town must be created before querying a passage");
    }

    this.sendBatchRequest(passageSafe);
    this.receiveResponse();
    this.batchRequest.clear();
  }

  private void sendBatchRequest(PassageSafe passageSafe) {
    BatchRequest batchRequest = new BatchRequest(this.batchRequest, passageSafe);
    String jsonRequest = this.gson.toJson(batchRequest);
    this.tcpConnection.sendMessage(jsonRequest);
  }

  private void receiveResponse() {
    String serversMessage = "";
    try {
      // TODO: need to actually handle this
      serversMessage = this.tcpConnection.readResponse();
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
    Response response = this.gson.fromJson(serversMessage, Response.class);
    response.printResponse();
  }

  private static class BatchRequest {

    private PlaceCharacter[] characters;
    private PassageSafe query;

    BatchRequest(ArrayList<PlaceCharacter> characters, PassageSafe query) {
      this.characters = characters.toArray(new PlaceCharacter[0]);
      this.query = query;
    }
  }

  private static class Response {

    private PlaceCharacter[] invalid;
    private boolean response;

    public void printResponse() {
      // TODO: this
    }
  }
}
