package travellerClient;

import travellerClient.ClientTownNetworkGarbagePlaceholder;
import travellerClient.Command;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Runs the traveller client by reading input from System.in and passing commands to server.
 */
public class TravellerClient {

  /** TODO: document design decisions about error throwing
   * (e.g. that the client will handle parsing errors, the server
   * should be able to handle the logic errors - like the town is not
   * in the network or that the character is not in a town) */

  /**
   * Runs this program.
   * @param args args to program
   * @throws IOException is System.in is closed
   */
  public static void main(String[] args) throws IOException {
    InputReader<Command> reader = new InputReader<>(System.in, Command.class, new CommandDeserialization());
    ArrayList<Command> commands = reader.parseInput();

    /** TODO: confirm that the input will actually close
     * (otherwise we need to make "doCommand" happen at the
     * sametime as the parsing) */
    ClientTownNetworkGarbagePlaceholder townNetwork = null;
    for (Command command : commands) {
      townNetwork = command.doCommand(townNetwork);
    }
  }

}
