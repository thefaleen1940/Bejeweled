import java.awt.Dimension;

import edu.illinois.cs.cs125.lib.zen.Zen;

/*import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;

public class GameWindow {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI() {
		JFrame mainFrame = new JFrame("Bejeweled");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.add(new MyPanel());
		mainFrame.setSize(800, 800);
		mainFrame.setVisible(true);
	}

}

class MyPanel extends JPanel {

	GameLogic game = new GameLogic();
	Gem gem;
	public MyPanel() {

		setBorder(BorderFactory.createLineBorder(Color.black));

		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				gem = game.getGemAt(e.getX(),e.getY());
			}
		});

		addMouseMotionListener(new MouseAdapter(){
			public void mouseDragged(MouseEvent e){
				moveGem(e.getX(),e.getY());
			}
		});

		for (int x = 0; x < game.getGemNumber(); x++) {
			for (int y = 0; y < game.getGemNumber(); y++) {
				Gem gg = game.getGemAt(x, y);
				System.out.println(gg.getX() + " " + gg.getY());
				int pix = game.pixelsPerGem();
				repaint(gg.getX() * pix, gg.getY() * pix,
						gg.getDim() * pix + 1,
						gg.getDim() * pix + 1);
			}
		}

	}

	private void moveGem(int x, int y){
		final int CURRENT_X = gem.getX();
		final int CURRENT_Y = gem.getY();

		if ((CURRENT_X!=x) || (CURRENT_Y!=y)) {

			repaint(CURRENT_X, CURRENT_Y, gem.getDim() + 1, gem.getDim() + 1);

			gem.setX(x);
			gem.setY(y);

			repaint(gem.getX(), gem.getY(),
					gem.getDim() + 1,
					gem.getDim() + 1);
		}
	}

	public Dimension getPreferredSize() {
		return new Dimension(800,800);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

}
 */
/**
 * Example program written using Zen Graphics.
 *
 * @see <a href="https://github.com/cs125-illinois/Zen">Zen on GitHub</a>
 * @see <a href="https://cs125-illinois.github.io/Zen/">Zen Documentation</a>
 * @see <a href="https://cs125.cs.illinois.edu/lab/1/#zen">Lab 1 Writeup</a>
 */
public class GameWindow {
    /**
     * This example shows how to use buffer swapping.
     *
     * Zen maintains two image buffers. The foreground buffer is currently displayed; the background
     * buffer is not. A complete image can be assembled unseen in the background buffer. The buffers
     * are then swapped and the image is then visible to the user.
     *
     * This is a very common technique in graphics stacks. First, it allows us to restart rendering
     * from a blank canvas without worrying about what might have been drawn previously.
     *
     * In addition, assembling an entire window can require a lot of computation and compositing of
     * multiple layers. For example, on Android the smartphone platform is responsible for drawing
     * the top and bottom of the display, while the application draws the middle. These two parts
     * have to be combined before the image is displayed to the user. Buffer swapping ensures that
     * users never see the screen in an incomplete state. All assembly takes place on the unseen
     * buffer.
     *
     * @see https://en.wikipedia.org/wiki/Multiple_buffering
     * @param unused unused input parameters
     */
    GameLogic game = new GameLogic();
    int dim = game.getGemNumber();
    public static void main(final String[] unused) {
        GameLogic game = new GameLogic();
        Zen.create(800, 800);
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

    private static int clipNumber(int val, int min, int max) {
        return Math.min(Math.max(val, min), max);
    }

    public static void drawHighlight(Gem gem, String color) {
        Zen.setColor(color);
        Zen.fillRect(gem.getX(), gem.getY(), gem.getDim(), gem.getDim());
        drawGem(gem);
    }

    public static void redrawScreen(GameLogic game) {
        for (int x = 0; x < game.getGemNumber(); x++) {
            for (int y = 0; y < game.getGemNumber(); y++) {
                drawGem(game.getGemAt(x, y));
            }
        }
    }

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
        System.out.println("draw" + "(" + gem.getX() + "," + gem.getY() + gem.getGem());
        Zen.fillOval(gem.getX(), gem.getY(), gem.getDim(), gem.getDim());
    }

}