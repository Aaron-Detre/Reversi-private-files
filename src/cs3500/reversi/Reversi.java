package cs3500.reversi;

import cs3500.reversi.adapters.StrategyAdapter;
import cs3500.reversi.adapters.ViewAdapter;
import cs3500.reversi.adapters.ViewEventListenerController;
import cs3500.reversi.controller.IReversiController;
import cs3500.reversi.controller.ReversiController;
import cs3500.reversi.model.AIPlayer;
import cs3500.reversi.model.DiscColor;
import cs3500.reversi.model.HumanPlayer;
import cs3500.reversi.model.IPlayer;
import cs3500.reversi.model.MutableReversiModel;
import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.provider.strategy.AvoidSecondRing;
import cs3500.reversi.provider.strategy.CaptureMostPieces;
import cs3500.reversi.provider.strategy.PrioritizeCorners;
import cs3500.reversi.provider.strategy.TwoStrategy;
import cs3500.reversi.strategy.AIStrategy;
import cs3500.reversi.strategy.AvoidNextToCorners;
import cs3500.reversi.strategy.MaxCapture;
import cs3500.reversi.strategy.MinimizeOpponentMaxCapture;
import cs3500.reversi.strategy.PlayToCorners;
import cs3500.reversi.strategy.ReversiStrategy;
import cs3500.reversi.view.IReversiView;
import cs3500.reversi.view.ReversiView;

/**
 * Runs the game of Reversi and allows it to be played.
 */
public final class Reversi {
  /**
   * The main method for the game of Reversi.
   * @param args For the first string argument, it can be one of the following:
   *             human - creates a human player
   *             strategy1 - creates an AI player that always plays the upper-leftmost available
   *             move
   *             strategy2 - creates an AI player that always plays the move that captures the
   *             most pieces and then follows strategy1
   *             strategy3 - creates an AI player that first tries to play to corners and avoid
   *             playing next to corners and then follows strategy2
   *             strategy4 - creates an AI player that first tries to minimize the maximum
   *             number of pieces that its opponent could capture on their turn and then follows
   *             strategy3
   *             For the second string argument, it can be one of the following:
   *             providerStrategy1 - creates an AI player that uses our provider's
   *             CaptureMostPieces strategy
   *             providerStrategy2 - creates an AI player that uses our provider's
   *             PrioritizeCorners and AvoidSecondRing strategies
   *             providerStrategy3 - creates an AI player that uses our provider's
   *             PrioritizeCorners, AvoidSecondRing, and CaptureMostPieces strategies
   *             (If less than 2 arguments are given, human players will be created by default)
   */
  public static void main(String[] args) {
    String input1 = getCommandLineInput(args, 0);
    String input2 = getCommandLineInput(args, 1);

    // Model
    MutableReversiModel model = ReversiModel.create(4);

    // View
    IReversiView viewPlayer1 = new ReversiView(model);
    ViewAdapter viewPlayer2 = new ViewAdapter(model);
    ViewEventListenerController controller2Decorator = new ViewEventListenerController();
    // Set the empty decorator as the view's event listener
    viewPlayer2.addProviderViewEventListener(controller2Decorator);

    // Players
    IPlayer player1 = makePlayer(input1, 1, model);
    IPlayer player2 = makePlayer(input2, 2, model);

    // Controller
    IReversiController controller1 = new ReversiController(model, viewPlayer1, player1);
    IReversiController controller2 = new ReversiController(model, viewPlayer2, player2);
    // Set controller2 as the delegate controller for the decorator listening to the view
    controller2Decorator.setController(controller2);

    model.startGame();
  }

  /**
   * Gets one of the strings from the command line arguments.
   * @param args the command line arguments
   * @param argIndex the index of the desired argument
   * @return the desired string or "human" if there was no argument at the specified index
   */
  private static String getCommandLineInput(String[] args, int argIndex) {
    String input;
    try {
      input = args[argIndex];
    } catch (ArrayIndexOutOfBoundsException e) {
      input = "human";
    }
    return input;
  }

  /**
   * Creates a new player in the game depending on the command line input.
   * @param input one of the strings from the command line input
   * @param playerNum 1 or 2 depending on if this is the 1st or 2nd player
   * @param model the model for the game that the player being created is playing
   * @return the new player
   * @throws IllegalArgumentException if the input or playerNum is invalid
   */
  private static IPlayer makePlayer(String input, int playerNum, ROReversiModel model) 
          throws IllegalArgumentException {
    if (input.equals("human")) {
      return HumanPlayer.create(getPlayerColor(playerNum), model);
    } else {
      ReversiStrategy strategy;
      if (playerNum == 1) {
        strategy = getStrategy(input);
      } else if (playerNum == 2) {
        strategy = getProviderStrategy(input);
      } else {
        throw new IllegalArgumentException("Invalid player number");
      }
      return AIPlayer.create(getPlayerColor(playerNum), model, strategy);
    }
  }

  /**
   * Determines the player's disc color depending on whether they are the 1st or 2nd player.
   * @param playerNum 1 or 2 depending on if this is the 1st or 2nd player
   * @return the player's color
   * @throws IllegalArgumentException if the playerNum is invalid
   */
  private static DiscColor getPlayerColor(int playerNum) throws IllegalArgumentException {
    switch (playerNum) {
      case 1:
        return DiscColor.BLACK;
      case 2:
        return DiscColor.WHITE;
      default:
        throw new IllegalArgumentException("Invalid Player Number");
    }
  }

  /**
   * Determines an AI player's strategy depending on the command line input.
   * @param input one of the strings from the command line input
   * @return the strategy the AI player will use to choose moves
   * @throws IllegalArgumentException if the input is invalid
   */
  private static AIStrategy getStrategy(String input) throws IllegalArgumentException {
    switch (input) {
      case "strategy1":
        return new AIStrategy();
      case "strategy2":
        return new AIStrategy(new MaxCapture());
      case "strategy3":
        return new AIStrategy(new PlayToCorners(), new AvoidNextToCorners(), new MaxCapture());
      case "strategy4":
        return new AIStrategy(new MinimizeOpponentMaxCapture(), new PlayToCorners(),
                new AvoidNextToCorners(), new MaxCapture());
      default:
        throw new IllegalArgumentException("Invalid input");
    }
  }

  private static ReversiStrategy getProviderStrategy(String input) {
    switch (input) {
      case "providerStrategy1":
        return new StrategyAdapter(new CaptureMostPieces());
      case "providerStrategy2":
        return new StrategyAdapter(
                new TwoStrategy(new PrioritizeCorners(), new AvoidSecondRing()));
      case "providerStrategy3":
        return new StrategyAdapter(
                new TwoStrategy(new PrioritizeCorners(),
                        new TwoStrategy(new AvoidSecondRing(), new CaptureMostPieces())));
      default:
        throw new IllegalArgumentException("Invalid input");
    }
  }
}