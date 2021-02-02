package travellerClient;

import travellerClient.ClientTownNetworkGarbagePlaceholder;
import travellerClient.Command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

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
    InputReader<Command> reader = new InputReader<>(System.in, Command.class, new CommandDeserialization(), new DoCommand());

    // DESIGN DECISION: STDIN does not have to close before the code is actually run
    // this means that "passage-safe?" will receive an immediate answer
    reader.parseInput();
  }

  /**
   * Mutable function for commands that runs a command and updates the townNetwork.
   */
  private static class DoCommand implements Consumer<Command> {

    private ClientTownNetworkGarbagePlaceholder townNetwork;

    public void accept(Command command) {
      this.townNetwork = command.doCommand(townNetwork);
    }
  }

}
