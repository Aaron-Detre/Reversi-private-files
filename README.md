(This was submitted for part 1 of Reversi and is now somewhat outdated. The updates
for parts 2, 3, and 4 are further down)  
# Overview:  
> The codebase is tyring to model the game of Reversi. We assume that the grid is a regular
hexagon made up of hexagonal cells, the grid has an edge length of two or more cells, and
there are two players. It uses the standard rules of the game Reversi. In our code, the grid
is able to have a variable size. The game could be played with more than two players or the
grid could be a different shape, but our model doesn't currently allow those variations.  


# Quick start:
> <ul>
	<li>MutableReversiModel m = ReversiModel.create(3);</li>
	<li>Player p1 = Player.create(1, m);</li>
	<li>Player p2 = Player.create(2, m);</li>
	<li>RowColCoords posn = new RowColCoords(1, 3);</li>
	<li>m.selectCell(posn);</li>
	<li>m.placeDiscInSelectedCell();</li>
	<li>m.pass();</li>
	<li>p1.makeMove("pass");</li>
	<li>Assert.assertTrue(m.isGameOver());</li>
	<li>Assert.assertEquals(Color.BLACK, m.getWinner());</li>
 </ul>


# Key components:  
>The model holds the information about the game board, state, and rules. It enforces those rules,
allows moves to be made, and outputs information about the game. The model would be driven by the
controller.  
The view currently just outputs a simple text view of the model. In future versions it will be
more complex. The view is not in charge of the system's control flow.  
The controller hasn't been implemented yet. The controller will be the component that handles
inputs and drives the control-flow of the system.  


# Key subcomponents of the model:  
>Color represents the black or white color of a disc or the lack of color in an empty cell. The
cells of the grid are represented as colors.  
GameState represents the state of the game, specifying which player's turn it is or whether the
game is over.  
Player represents one of the two players playing the game. Players can make moves to affect the
board.  
RowColCoords represents a row/column coordinate system used to find a cell in the board.  


(The view and controller don't have any subcomponents currently)  


# Source organization:  
>src/cs3500/reversi/model:  
	Model interface - ROReversiModel and MutableReversiModel  
	Model implementation - ReversiModel  
	Player interface - IPlayer  
	Player implementation - Player  
	Color enum - Color  
	Game state enum - GameState  
	Row column coordinates - RowColCoords  
	Player move interface - PlayerMove  
	Player move implementations - Pass, SelectCell, and PlaceDisc  
src/cs3500/reversi/view  
	View interface - IReversiTextView  
	View implementation - ReversiTextView  



#Updates 11/14/23:  
>New interfaces and classes:  
In src/cs3500/reversi  
    -Reversi  
        -> Class containing the main method for the game.  
In src/cs3500/reversi/model  
    -CoordinateTranscriptMock  
        -> Mock model that appends the coordinates of the grid when getGrid is called and the
           coordinates of any cell that is called with validMove.  
    -ForcedMoveMock  
        -> Mock model that sets all possible moves to be invalid except for one given cell.  
In src/cs3500/reversi/view  
    -IReversiView  
        -> The interface for ReversiView.  
    -ReversiView  
        -> The JFrame that contains the panel with the game board.  
    -ReversiPanel  
        -> The JPanel that contains the game board and allows the user to select/deselect cells and
           "place discs" or "pass" though you can't actually do that yet.  
    -Hexagon  
        -> A Path2D.Double that draws a regular hexagon shape.  
In src/cs3500/reversi/strategy  
    -StrategyUtils  
        -> Utility methods used in strategies.  
    -ReversiStrategy  
        -> The interface for high-level Reversi strategies.  
    -AIStrategy  
        -> The strategy the computer can use to choose a move, combining zero, one, or more
           FilteringReversiStrategies in a specified order.  
    -FilteringReversiStrategy  
        -> The interface for sub-strategies that narrow down a player's available options.  
    -MaxCapture  
        -> A FilteringReversiStrategy that prioritizes capturing the most opponent discs
           as possible.  

    Extra Credit:  
    -AvoidNextToCorners  
        -> A FilteringReversiStrategy that prioritizes avoiding cells adjacent to corner cells.  
    -PlayToCorners  
        -> A FilteringReversiStrategy that prioritizes playing to corner cells.  
    -MinimizeOpponentMaxCapture  
        -> A FilteringReversiStrategy that prioritizes minimizing the maximum number of cells that
           the opponent can capture.  


User actions:  
-Select a cell  
    -This is done by clicking on a cell in the grid. Only one cell can be selected
     at a time.  
-Deselect a cell  
    -This is done either by clicking on the selected cell again or by clicking
     on no cells (the background).  
-"Place a disc"  
    -This is done by pressing the 'Enter' key on the keyboard, but it only outputs
     the message "place disc" currently instead of actually placing a disc.  
-"Pass"  
    -This is done by pressing the spacebar on the keyboard, but it only outputs
     the message "pass" currently instead of actually passing the player's turn.  


Changes for Part 2:  
-Added the public methods getEdgeLength and getPassCounter to the read only interface so that
 a copy constructor could be made.  
-Added the public method anyLegalMoves to the read only interface as specified by the assignment.  
-In our original implementation of the model, the only way to place a disc was to select a cell
 and then place the disc into that selected cell, but now it's changed to just place the disc
 in a given cell.  
-Previously there was nothing stopping a player from placing on a cell with a disc already on
  it, so now to determine whether something is a valid move, we first check that the cell is
 actually empty.  
-The method validMove is now public and returns the boolean specifying whether the move is valid,
 but now that is mapped to the number of discs that would be captured if the move was played.  
-Fixed some slightly wrong logic in finding the next cell in a line that was causing some legal
 moves to be found to be illegal and vice-versa.  
-Added basic tests for anyLegalMoves and validMove.  



Updates 11/29/23:  
New interfaces and classes:  
In src/cs3500/reversi/model  
    -IPlayer (changed significantly)  
        -> The interface for players.  
    -AIPlayer  
        -> An implementation of IPlayer that allows AIPlayers to choose moves
           depending on the strategy they are using.  
    -HumanPlayer  
        -> An implementation of IPlayer that doesn't do much since human players
           choose moves by interacting with the view.  
    -ModelBroadcaster  
        -> The interface for an object that has a ModelCallback object in it
           (MutableReversiModel now also extends this).  
    -ModelCallback  
        -> The interface for an object that can signal to its listeners when a
           model is mutated.  
    -ModelMutatedBroadcaster  
        -> An implementation of a ModelCallback.  
In src/cs3500/reversi/view  
    -PlayerActions  
        -> The features interface describing all four actions a player is able to
           make in the game.  
    -HumanPlayerActions  
        -> An implementation of PlayerActions that allows the player to make all
           four of those actions.  
    -AIPlayerActions  
        -> An implementation of PlayerActions that only allows that player to pass
           or place their disc.  
    -NoActions  
        -> An implementation of PlayerActions that doesn't allow the player to make
           any actions.  
    -IReversiPanel  
        -> The interface for the panel that display the board.  
    -IReversiView (changed significantly)  
        -> The interface for the frame that displays the panel.  
In src/cs3500/reversi/controller  
    -ModelCallbackListener  
        -> The interface for an object that listens to the broadcast from
           ModelCallback objects when the model is mutated.  
    -IReversiController  
        -> The interface for the controller for Reversi.  
    -ReversiController  
        -> An implementation of an IReversiController.  


Changes for Part 3:  
Model changes:  
-Removed nulls from the model and replaced with either empty lists or optionals  
-Moved the oppositeColor method from the model to DiscColor  
-Created the ModelCallback interface which lets its listeners know when the model has
 been mutated and when the game is over  
-Gave ReversiModel a ModelCallback object which is used to broadcast model updates to the
 controllers  
-Created the ModelBroadcaster interface which can set and remove broadcast listeners  
-Made MutableReversiModel extend ModelBroadcaster and implemented its methods in ReversiModel  
-In nextPlayerTurn, now calling modelMutated() on the broadcaster  
-Fixed slightly wrong logic in finding the next cell in upper left pointing lines  
-Added a startGame method and an UNSTARTED GameState  
-Throw exceptions for pass, placeDiscInCell, getActivePlayerColor, anyLegalMoves, and validMove
 if the game hasn't started yet  
-Changed the isGameOver method to not just return true if the pass counter = 2, but to update the
 game state if the pass counter has reached 2 and then return the game state  
-Also removed the private method updateStateIfGameOver since isGameOver now updates the game
 state directly  
-In pass(), changed it so that nextPlayerTurn is only called if the game is not over  
-In getGrid(), we now make sure the inner lists of the 2D list are also unmodifiable  
-Added tests for the new interfaces in NewReversiModelTests  

View changes:  
-Created a features interface, PlayerActions, containing the 4 actions a player can take  
-Created the class HumanPlayerActions which implements PlayerActions and delegates to the view
 and controller based on the input to the mouse and key listener in ReversiPanel  
-Created the class AIPlayerActions which implements PlayerActions and delegates to the controller
 based on the move chosen by the AI player  
-Created the class NoActions which implements PlayerActions and does nothing, meaning the player
 is unable to select any cells or make any moves  
-Added 8 methods to IReversiView: setPanelActions, setSelectedCell, deselectCell, rerender,
 endGame, notActivePlayerMessage, illegalMoveMessage, and yourTurnMessage  
-Implemented those methods in ReversiView  
-Added the private method gameOverMessage to ReversiView which constructs the message to be
 displayed when the game has ended  
-Created a panel interface: IReversiPanel with 3 methods: setActionListeners, setSelectedCell,
 and deselectSelectedCell  
-Made ReversiPanel implement IReversiPanel and implemented those methods  
-Made ReversiPanel not implement KeyListener, and moved the KeyListener and MouseInputAdapter
 methods into the setActionListeners method  
-Gave ReversiPanel 3 new fields: discRadius, the radius of each disc depending on the width of
 the panel, and mouseListener and keyListener, the two active action listeners for the panel  
-Added 3 private helper methods to ReversiPanel: logicalToCell which finds the physical hexagon
 that corresponds to some RowColCoords, pointToLogical which finds the RowColCoords that
 correspond to some physical point, and pathToLogical which finds the RowColCoords that correspond
 to the center point of some Path2D.Double  
-Removed the ROReversiModel from the signature of the render() method because it was never used  
-Created GUITests in the view test directory which tests the methods of IReversiView,
 IReversiPanel, and PlayerActions  

Player changes:
-Removed the makeMove method and replaced with chooseMove which does nothing for human players
 and chooses a move and then plays it using a PlayerActions object for AI players  
-Renamed Player to HumanPlayer and created the class AIPlayer  
-Added the public method isHuman() to IPlayer  
-Added tests for the changes to the IPlayer interface in NewReversiModelTests  




Updates 12/6/23  
How to run the jar file:  
java -jar Reversi.java argument1 argument2  
Either argument can be "human" which creates a human player.  
For the first argument, the available strategies are:  
   -> strategy1 - creates an AI player that always plays the upper-leftmost available
                  move  
   -> strategy2 - creates an AI player that always plays the move that captures the
                  most pieces and then follows strategy1  
   -> strategy3 - creates an AI player that first tries to play to corners and avoid
                  playing next to corners and then follows strategy2  
   -> strategy4 - creates an AI player that first tries to minimize the maximum
                  number of pieces that its opponent could capture on their turn and then follows
                  strategy3  
For the second argument, the available strategies are:  
   -> providerStrategy1 - creates an AI player that uses our provider's CaptureMostPieces strategy  
   -> providerStrategy2 - creates an AI player that uses our provider's PrioritizeCorners and
                          AvoidSecondRing strategies (this strategy isn't very smart due to a bug
                          in the provided AvoidSecondRing strategy)  
   -> providerStrategy3 - creates an AI player that uses our provider's PrioritizeCorners,
                          AvoidSecondRing, and CaptureMostPieces strategies (this strategy isn't
                          very smart due to a bug in the provided AvoidSecondRing strategy)  
If less than 2 arguments are given, human players will be created by default.  


Which features work and which don't:  
-The provided view displays properly except that the score and the player label that they have
 on their view start hidden, but if the window is resized at all, they become visible.  
-The provided view is playable.  
-The strategies CaptureMostPieces and PrioritizeCorners work correctly.  
-The strategy AvoidSecondRing doesn't work correctly due to a bug in their code that removes
 all the possible moves if the only options are next to corners. To fix this, if there are no
 options that aren't next to a corner, it could just add back all the options that were removed.  
-We deleted the strategy Minimax because it constructed a concrete implementation of their model
 which we weren't given.  


New interfaces and classes:  
In src/cs3500/reversi/adapters  
    -AdapterUtils  
        -> Class containing conversions between our coordinate systems and disc color enums  
    -ModelAdapter  
        -> Class adapting our model to our provider's read only model interface.  
    -StrategyAdapter  
        -> Class adapting our provider's strategies to our strategy interface.  
    -ViewAdapter  
        -> Class adapting our provider's view to our view interface.  
    -ViewEventListenerController  
        -> Decorator class for our controller that gives it the capabilities of our provider's
           ViewEventListener  


Changes for part 4:  
-No longer setting the panel actions to NoActions in the constructor since our code customers did
 not have the NoActions class and the line turned out to be unnecessary  
-Removed PlayerMove interface and PlaceDisc and Pass classes since they were something left over
 from Reversi Part 1, and we just forgot to remove them  
-MinimizeOpponentMaxCapture now gets all the possible moves from the copyModel, not the old
 model on line 52  
-Exposed the private method discsToCapture in ReversiModel to the ROReversiModel interface and
 changed the return type to List<List<DiscColor>> to match the structure of our provider's
 findAllSeams method. They do basically the same thing but ours was private, so to implement
 their model interface to get their strategies to run, it had to be made public  
-In the chooseMove method in AIPlayer, now also catching NoSuchElementExceptions due to a bug
 in our provider's AvoidSecondRing strategy  
