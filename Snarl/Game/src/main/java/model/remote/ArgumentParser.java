package model.remote;

import org.apache.commons.cli.*;

public class ArgumentParser {

  private static final String LEVELS_ARGNAME = "levels";
  private static final String CLIENT_ARGNAME = "clients";
  private static final String WAIT_ARGNAME = "wait";
  private static final String OBSERVE_ARGNAME = "observe";
  private static final String IP_ARG = "address";
  private static final String PORT_ARG = "port";

  public String levelFile = "snarl.levels";
  public int maxNumPlayers = 4;
  public int waitTime = 60;
  public String ip = "127.0.0.1";
  public boolean showObserverView = false;
  public int port = 45678;

  private static final Option LEVELS = Option.builder()
      .argName(String.valueOf(LEVELS_ARGNAME.charAt(0)))
      .longOpt(LEVELS_ARGNAME)
      .desc("The levels of the Snarl game")
      .hasArg()
      .numberOfArgs(1)
      .required(false)
      .build();

  private static final Option CLIENTS = Option.builder()
      .argName(String.valueOf(CLIENT_ARGNAME.charAt(0)))
      .longOpt(CLIENT_ARGNAME)
      .desc("The number of clients")
      .hasArg()
      .numberOfArgs(1)
      .required(false)
      .build();

  private static final Option WAIT = Option.builder()
      .argName(String.valueOf(WAIT_ARGNAME.charAt(0)))
      .longOpt(WAIT_ARGNAME)
      .desc("The number of seconds to wait for the next client to connect")
      .hasArg()
      .numberOfArgs(1)
      .required(false)
      .build();

  private static final Option OBSERVE = Option.builder()
      .argName(String.valueOf(OBSERVE_ARGNAME.charAt(0)))
      .longOpt(OBSERVE_ARGNAME)
      .desc("Whether a local observer should be created")
      .hasArg()
      .numberOfArgs(0)
      .required(false)
      .build();

  private static final Option IP_ADDRESS = Option.builder()
      .argName(String.valueOf(IP_ARG.charAt(0)))
      .longOpt(IP_ARG)
      .desc("The IP address the client should connect to")
      .hasArg()
      .numberOfArgs(1)
      .required(false)
      .build();

  private static final Option PORT_NUMBER = Option.builder()
      .argName(String.valueOf(PORT_ARG.charAt(0)))
      .longOpt(PORT_ARG)
      .desc("The IP address the client should connect to")
      .hasArg()
      .numberOfArgs(1)
      .required(false)
      .build();

  public ArgumentParser(String[] args) {
    Options options = new Options();
    options.addOption(LEVELS).addOption(CLIENTS).addOption(WAIT)
        .addOption(OBSERVE).addOption(IP_ADDRESS).addOption(PORT_NUMBER);
    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine commandLine = parser.parse(options, args);
      if (commandLine.hasOption(LEVELS_ARGNAME)) {
        levelFile = commandLine.getOptionValue(LEVELS_ARGNAME);
      }
      if (commandLine.hasOption(CLIENT_ARGNAME)) {
        maxNumPlayers = Integer.parseInt(commandLine.getOptionValue(CLIENT_ARGNAME));
      }
      if (commandLine.hasOption(WAIT_ARGNAME)) {
        waitTime = Integer.parseInt(commandLine.getOptionValue(WAIT_ARGNAME));
      }
      if (commandLine.hasOption(OBSERVE_ARGNAME)) {
        showObserverView = true;
      }
      if (commandLine.hasOption(IP_ARG)) {
        ip = commandLine.getOptionValue(IP_ARG);
      }
      if (commandLine.hasOption(PORT_ARG)) {
        port = Integer.parseInt(commandLine.getOptionValue(PORT_ARG));
      }
    } catch (ParseException exp) {
      System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
    }
  }
}
