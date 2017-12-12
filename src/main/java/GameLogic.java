import java.util.ArrayList;
import edu.illinois.cs.cs125.lib.zen.Zen;
/**
 * The main control scheme of the game. Is a bejeweled clone with 6 gem types that
 * can be swapped 1 tile horizontally or vertically. If a group of 3+ is formed, the
 * tiles are removed and new tiles fall in to take their place.
 */
public class GameLogic {
    /**
     *  Board width and height dimensions for playing field in pixels.
     */
    private int boardDimensions;
    /**
     * Current array of gems in the board.
     */
    private Gem[][] gameState;
    /**
     * Number of gems per row/column.
     */
    private int gemNumber;
    /**
     * Current score, 1 per gem removed.
     */
    int score;
    /**
     * Number of left rotations used in checking for matches and board generation,
     * used to re-orient board.
     */
    private int leftRotations;

    /**
     * Makes a new game board with default values.
     */
    public GameLogic() {
        this(800, 8);
    }

    public GameLogic(int dim, int gemNum) {
        leftRotations = 0;
        boardDimensions = dim;
        score = 0;
        gemNumber = gemNum;
        gameState = new Gem[gemNum][gemNum];
        populateBoard();
        turnCheck();
        fixCoords();
    }

    public int getGemNumber() {
        return gemNumber;
    }

    private void populateBoard() {
        for (int x = 0; x < gemNumber; x++) {
            for (int y = 0; y < gemNumber; y++) {
                gameState[x][y] = new Gem(Gem.Type.BLANK, x * pixelsPerGem(), y * pixelsPerGem(), pixelsPerGem());
            }
        }
        do {
            for (int x = 0; x < gemNumber; x++) {
                for (int y = 0; y < gemNumber; y++) {
                    if(gameState[x][y].getGem() == Gem.Type.BLANK || gameState[x][y].isMatched()) {
                        gameState[x][y] = new Gem(x * pixelsPerGem(), y * pixelsPerGem(), pixelsPerGem());
                    }
                }
            }
        } while (checkForMatches() > 0);
        score = 0;
    }

    private void turnCheck() {
        leftRotations = 0;
        rotateLeft();
        do {
            populateBoard();
            rotateLeft();
        } while (checkForMatches() > 0);
        while (leftRotations % 4 != 0) {
            rotateLeft();
        }
    }

    public boolean swapAttemptGame(int x1, int y1, int x2, int y2) {
        if(Math.abs(x1 - x2) + Math.abs(y1 - y2) == 1) {
            return swapAttempt(x1, gemNumber - 1 - y1, x2, gemNumber - 1 - y2);
        }
        return false;
    }

    private boolean swapAttempt(int x1, int y1, int x2, int y2) {
        swap(x1, y1, x2, y2);
        int matches = clearAllMatches();
        if (matches > 0) {
            fallToggle();
            return true;
        } else {
            swap(x1, y1, x2, y2);
            return false;
        }
    }

    public int afterTurn() {
        int matches = clearAllMatches();
        fallToggle();
        return matches;
    }

    private int clearAllMatches() {
        int matches = checkForMatches();
        rotateLeft();
        matches += checkForMatches();
        for(int i = 0; i < 3; i++) {
            rotateLeft();
        }
        return matches;
    }

    private void swap(int x1, int y1, int x2, int y2) {
        Gem copy = new Gem(gameState[x1][y1]);
        gameState[x1][y1] = new Gem(gameState[x2][y2]);
        gameState[x2][y2] = new Gem(copy);
        gameState[x1][y1].moveGem(x1 * pixelsPerGem(), (gemNumber - y1 - 1) * pixelsPerGem());
        gameState[x2][y2].moveGem(x2 * pixelsPerGem(), (gemNumber - y2 - 1) * pixelsPerGem());
    }

    public int pixelsPerGem() {
        return boardDimensions / gemNumber;
    }

    private int checkForMatches() {
        int removals = 0;
        for (int x = 0; x < gemNumber; x++) {
            Gem.Type currentCheck = gameState[x][0].getGem();

            int gemCount = 1;
            for (int y = 1; y < gemNumber; y++) {
                if (gameState[x][y].getGem() == currentCheck && currentCheck != Gem.Type.BLANK) {
                    gemCount++;
                } else if (gemCount >= 3 && gameState[x][y].getGem() != currentCheck) {
                    removals += gemCount;
                    remove(x, y, gemCount);
                    gemCount = 1;
                    currentCheck = gameState[x][y].getGem();
                } else {
                    gemCount = 1;
                    currentCheck = gameState[x][y].getGem();
                }
                if (gemCount >= 3 && y == gemNumber - 1) {
                    removals += gemCount;
                    remove(x, y + 1, gemCount);
                }
            }

        }

        return removals;
    }

    private void remove(int x, int y, int gemCount) {
        for (int clearPosition = 0; clearPosition < gemCount; clearPosition++) {
            gameState[x][y - gemCount + clearPosition].setMatched(true);
            score++;

            System.out.println("score is " + score);
        }
    }

    public void fallToggle() {
        for (int y = gemNumber - 1; y >= 0; y--) {
            for (int x = 0; x < gemNumber; x++) {
                Gem gem = gameState[x][y];
                if (gem.isMatched()) {
                    gem.toBlank();
                }
            }
        }
        boolean repeat = false;
        do {
            Zen.sleep(100);
            repeat = fallLoop();
            GameWindow.redrawScreen(this);
        } while (repeat);
    }

    private boolean fallLoop() {
        boolean repeat = false;
        for (int y = gemNumber - 1; y >= 0; y--) {
            for (int x = 0; x < gemNumber; x++) {
                Gem gem = gameState[x][y];
                if (gem.isMatched()) {
                    repeat = true;
                    if (y != gemNumber - 1) {
                        swap(x, y, x, y + 1);
                    } else {
                        Gem change = gameState[x][gemNumber - 1];
                        change.setType(Gem.Type.getRandomGem());
                        change.setMatched(false);
                    }
                }
            }
        }
        return repeat;
    }

    private void fixCoords() {
        for(int x = 0; x < gemNumber; x++) {
            for (int y = 0; y < gemNumber; y++) {
                gameState[x][y].moveGem(x * pixelsPerGem(), (gemNumber - 1 - y) * pixelsPerGem());
            }
        }
    }

    private void rotateLeft() {
        leftRotations++;
        Gem[][] rotatedGame = new Gem[gemNumber][gemNumber];
        for (int x = 0; x < gemNumber; x++) {
            for (int y = 0; y < gemNumber; y++) {
                rotatedGame[x][y] = gameState[y][gemNumber - x - 1];
            }
        }
        for (int x = 0; x < gemNumber; x++) {
            for (int y = 0; y < gemNumber; y++) {
                gameState[x][y] = rotatedGame[x][y];
            }
        }
    }

    public void printBoard() {
        for (int y = gemNumber - 1; y >= 0; y--) {
            for (int x = 0; x < gemNumber; x++) {
                Gem gem = gameState[x][y];
                System.out.print(gem.getGem().num + "(" + gem.getX() + "," + gem.getY() + ") ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public Gem getGemAt(int x, int y) {
        return gameState[x][gemNumber - 1 - y];
    }

}