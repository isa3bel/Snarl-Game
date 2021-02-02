import java.io.IOException;
import java.util.ArrayList;

/**
 * Runs the traveller client by reading input from System.in and passing commands to server.
 */
public class TravellerClient {

  /**
   * Runs this program
   * @param args args to program
   * @throws IOException is System.in is closed
   */
  public static void main(String[] args) throws IOException {
    InputReader<Command> reader = new InputReader<>(System.in, Command.class, new CommandDeserialization());
    ArrayList<Command> commands = reader.parseInput();

    ClientTownNetworkGarbagePlaceholder townNetwork = null;
    for (Command command : commands) {
      townNetwork = command.doCommand(townNetwork);
    }
  }

}
