package cs3500.reversi.adapters;

import cs3500.reversi.model.IPlayer;
import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.RowColCoords;
import cs3500.reversi.strategy.ReversiStrategy;

/**
 * Adapting our provider's strategies to our strategy interface.
 */
public class StrategyAdapter implements ReversiStrategy {
  private final cs3500.reversi.provider.strategy.ReversiStrategy providerStrategy;

  public StrategyAdapter(cs3500.reversi.provider.strategy.ReversiStrategy providerStrategy) {
    this.providerStrategy = providerStrategy;
  }

  @Override
  public RowColCoords chooseCell(ROReversiModel model, IPlayer player)
          throws IllegalStateException {
    AdapterUtils utils = new AdapterUtils();
    return utils.cubicToRowCol(this.providerStrategy.chooseMove(new ModelAdapter(model),
            utils.colorToState(player.getColor())), model);
  }
}
