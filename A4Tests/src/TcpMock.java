package src;

class TcpMock implements Tcp {

  String sent;
  String message;
  String response;

  TcpMock() {}

  TcpMock(String message, String response) {
    this.message = message;
    this.response = response;
  }

  @Override
  public void sendMessage(String message) {
    this.sent = message;
  }

  @Override
  public String readMessage() {
    return this.message;
  }

  @Override
  public String readResponse() {
    return this.response;
  }

  @Override
  public void closeConnection() {
    // don't need to do anything
  }
}