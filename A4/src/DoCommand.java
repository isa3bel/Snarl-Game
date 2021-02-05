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

  /**
   * Visit the given command.
   * @param command visit this command
   */
  public void accept(Command command) {
    if (command != null) command.accept(this);
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
    System.out.println("visiting place character");
    this.batchRequest.add(placeCharacter);
  }

  @Override
  public void visitPassageSafe(PassageSafe passageSafe) throws IllegalStateException {
    if (!this.townHasBeenCreated) {
      throw new IllegalStateException("town must be created before querying a passage");
    }

    System.out.println("visiting passage safe");
    this.sendBatchRequest(passageSafe);
    Response response = this.receiveResponse();
    response.print(passageSafe);
    this.batchRequest.clear();
  }

  /**
   * Assemble the batch request and send it to the server.
   * @param passageSafe the passage safe command to query
   */
  private void sendBatchRequest(PassageSafe passageSafe) {
    BatchRequest batchRequest = new BatchRequest(this.batchRequest, passageSafe);
    String jsonRequest = this.gson.toJson(batchRequest);
    this.tcpConnection.sendMessage(jsonRequest);
  }

  /**
   * Read the response from the server.
   */
  private Response receiveResponse() {
    String serversMessage = "";

    try {
      serversMessage = this.tcpConnection.readResponse();
    } catch (IOException ioException) {
      this.tcpConnection.closeConnection();
      System.out.println("server closed connection - invalid request sent");
      System.exit(-1);
    }

    Response response = this.gson.fromJson(serversMessage, Response.class);
    return response;
  }

  /**
   * Represents a batch request to the server.
   */
  private static class BatchRequest {

    private PlaceCharacter[] characters;
    private PassageSafe query;

    /**
     * Creates the batch request with the given characters and a query.
     * @param characters the arraylist of PlaceCharacter commands
     * @param query the passage safe query to make
     */
    BatchRequest(ArrayList<PlaceCharacter> characters, PassageSafe query) {
      this.characters = characters.toArray(new PlaceCharacter[0]);
      this.query = query;
    }
  }

  /**
   * The response from the server to the batch request.
   */
  private static class Response {

    private PlaceCharacter[] invalid;
    private boolean response;

    /**
     * Format and print the response for the user.
     */
    public void print(PassageSafe query) {
      Gson gson = new Gson();
      this.printInvalid(gson);
      this.printResponse(gson, query);
    }

    private void printInvalid(Gson gson) {
      for (PlaceCharacter placeCharacter : this.invalid) {
        System.out.println("[\"invalid placement\", " + gson.toJson(placeCharacter) + " } ]");
      }
    }

    private void printResponse(Gson gson, PassageSafe query) {
      System.out.println("[\"the response for\", " + gson.toJson(query) + ", \"is\", " + this.response + "]");
    }
  }
}
