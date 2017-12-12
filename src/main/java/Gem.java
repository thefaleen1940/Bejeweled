import java.awt.Color;
import java.awt.Graphics;
/**
 * Represents a gem of one of 7 colors or a placeholder gem.
 */
public class Gem {

    public enum Type {
        /**
         * Color and corresponding number of each gem.
         */
        RED (0), ORANGE (1), YELLOW (2), GREEN (3), BLUE (4), PURPLE (5), WHITE (6), BLANK (7);
        public int num;
        /**
         * Basic enum constructor to implement numbered gems.
         * @param i Number corresponding to gem color.
         */
        Type(int i) {
            num = i;
        }
        /**
         * @return A random Gem Type that isn't a placeholder.
         */
        public static Type getRandomGem() {
            int random = (int)(Math.random() * 7);
            return values()[random];
        }
    }
    /**
     * What type of gem it is.
     */
    private Type gemType;
    /**
     * Whether the gem is marked for removal.
     */
    private boolean matched;
    /**
     * Generate a random typed gem at given coords and dimension.
     * @param newX x coords
     * @param newY y coords
     * @param newDim dimensions of gem
     */
    public Gem(int x, int y, int dim) {
        this(Type.getRandomGem(), x, y, dim);
    }
    /**
     * Generate a new gem of Type t at specified location an with specified dim.
     * @param t type of gem
     * @param newX x coords
     * @param newY y coords
     * @param newDim dimensions of gem
     */
    public Gem(Type t, int x, int y, int dim) {
        gemType = t;
        matched = false;
        this.x = x;
        this.y = y;
        this.dim = dim;
    }
    /**
     * Copy constructor.
     * @param g gem to copy
     */
    public Gem(Gem g) {
        this.gemType = g.gemType;
        this.x = g.x;
        this.y = g.y;
        this.dim = g.dim;
        this.matched = g.matched;
    }
    /**
     * Sets gem type to BLANK (placeholder)
     */
    public void toBlank() {
        gemType = Type.BLANK;
    }
    /**
     * Change gem type.
     * @param t type to change to
     */
    public void setType(Type t) {
        gemType = t;
    }
    /**
     * @return The type of gem
     */
    public Type getGem() {
        return gemType;
    }
    private int x;
    private int y;
    private int dim;
    public void setX(int xNew){
        x = xNew;
    }

    public int getX(){
        return x;
    }

    public void setY(int yNew){
        y = yNew;
    }

    public int getY(){
        return y;
    }

    public int getDim() {
        return dim;
    }

    public void setMatched(boolean b){
        matched = b;
    }

    public boolean isMatched() {
        return matched;
    }

    public void moveGem(int xNew, int yNew) {
        x = xNew;
        y = yNew;
    }

}
