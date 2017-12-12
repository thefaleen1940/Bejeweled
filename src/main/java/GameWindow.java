import edu.illinois.cs.cs125.lib.zen.Zen;
/**
 *Responsible for making the window the game is played in.
 */
public class GameWindow {
    /**
     * Main game instance.
     */
    GameLogic game = new GameLogic();
    /**
     * Dimensions of the game board in number of gems.
     */
    int dim = game.getGemNumber();
    /**
     * Setup code and main game loop.
     * @param unused N/A
     */
    public static void main(final String[] unused) {
        GameLogic game = new GameLogic();
        Zen.create(900, 800);
        redrawScreen(game);
        int xPosOld = -1;
        int yPosOld = -1;
        int xPos = 0;
        int yPos = 0;
        game.printBoard();
        while (Zen.isRunning()) {
            Zen.waitForClick();
            xPos = Zen.getMouseClickX() / game.pixelsPerGem();
            yPos = Zen.getMouseClickY() / game.pixelsPerGem();
            xPos = clipNumber(xPos, 0, game.getGemNumber() - 1);
            yPos = clipNumber(yPos, 0, game.getGemNumber() - 1);
            if (((xPosOld != xPos) || (yPosOld != yPos)) && xPosOld != -1) {
                boolean isSwapped = game.swapAttemptGame(xPos, yPos, xPosOld, yPosOld);
                xPosOld = -1;
                if (isSwapped) {
                    System.out.println("swapped");
                    int fullScreenDim = game.getGemNumber() * game.pixelsPerGem();
                    Zen.setColor("black");
                    Zen.fillRect(0, 0, fullScreenDim, fullScreenDim);
                    int moves = 0;
                    do {
                        redrawScreen(game);
                        moves = game.afterTurn();
                    } while (moves > 0);
                }
            } else {
                xPosOld = xPos;
                yPosOld = yPos;
            }
        }
    }
    /**
     * Clips a number between a max and min value.
     * @param val Number to clip.
     * @param min Minimum value (inclusive)
     * @param max Maximum value (inclusive)
     * @return Clipped number.
     */
    private static int clipNumber(int val, int min, int max) {
        return Math.min(Math.max(val, min), max);
    }
    /**
     * Fills in a gem's background with a color.
     * @param gem Gem to draw background on.
     * @param color Color of background.
     */
    public static void drawHighlight(Gem gem, String color) {
        Zen.setColor(color);
        Zen.fillRect(gem.getX(), gem.getY(), gem.getDim(), gem.getDim());
        drawGem(gem);
    }
    /**
     * Redraws entire screen from scratch.
     * @param game Game to redraw screen of.
     */
    public static void redrawScreen(GameLogic game) {
        for (int x = 0; x < game.getGemNumber(); x++) {
            for (int y = 0; y < game.getGemNumber(); y++) {
                drawGem(game.getGemAt(x, y));
            }
        }
        Zen.setColor("black");
        Zen.fillOval(800, 50, 100, 100);
        Zen.setColor("white");
        Zen.drawText("Score: " + Integer.toString(game.score), 810, 100);

    }
    /**
     * Draws a gem at its x, y with the right color.
     * @param gem Gem to draw
     */
    public static void drawGem(Gem gem) {
        switch (gem.getGem()) {
            case RED:
                Zen.setColor("red");
                break;
            case ORANGE:
                Zen.setColor("orange");
                break;
            case YELLOW:
                Zen.setColor("yellow");
                break;
            case GREEN:
                Zen.setColor("green");
                break;
            case BLUE:
                Zen.setColor("blue");
                break;
            case PURPLE:
                Zen.setColor("purple");
                break;
            case WHITE:
                Zen.setColor("white");
                break;
            case BLANK:
                Zen.setColor("black");
                break;
            default: throw new RuntimeException("unreachable");
        }
        Zen.fillOval(gem.getX(), gem.getY(), gem.getDim(), gem.getDim());
    }

}