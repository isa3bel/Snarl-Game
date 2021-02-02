package travellerClient;

public interface Command {

  /**
   * Execute the command on the given town network
   * @param townNetwork the town network on which to do the command
   * @return the resulting town network after executing the command
   */
  ClientTownNetworkGarbagePlaceholder doCommand(ClientTownNetworkGarbagePlaceholder townNetwork);

}
