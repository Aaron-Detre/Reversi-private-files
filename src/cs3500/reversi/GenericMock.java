package cs3500.reversi;

/**
 * The new mocks MockController, MockPanel, and MockView all have the same structure, so
 * this generic mock contains their shared functionality.
 */
public class GenericMock {
  private final StringBuilder log;

  /**
   * Creates a new GenericMock object.
   * @param log the string output that gets built up by the different methods of the mock
   */
  public GenericMock(StringBuilder log) {
    this.log = log;
  }

  /**
   * Appends a string to the log and then adds a line break.
   * @param s the string to be appended
   */
  public void appendToLog(String s) {
    log.append(s).append("\n");
  }

  /**
   * Returns the mock's log.
   * @return the mock's log
   */
  public String getLog() {
    return this.log.toString();
  }
}
