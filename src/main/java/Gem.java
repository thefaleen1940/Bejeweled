import java.awt.Color;
import java.awt.Graphics;

public class Gem {
	public enum Type {
		/* RED is a square
		 * ORANGE is a hexagon
		 * YELLOW is a diamond
		 * GREEN is a truncated square
		 * BLUE is a truncated, upside down, triangle
		 * PURPLE is a triangle
		 * WHITE is a circle
		 */
		RED (0), ORANGE (1), YELLOW (2), GREEN (3), BLUE (4), PURPLE (5), WHITE (6), BLANK (7);

		public int num;
		Type(int i) {
			num = i;
		}

		public static Type getRandomGem() {
            int random = (int)(Math.random() * 7);
            return values()[random];
        }
	}

	private Type gemType;
	private boolean matched;

	public Gem(int x, int y, int dim) {
		this(Type.getRandomGem(), x, y, dim);
	}

	public Gem(Type t, int x, int y, int dim) {
		gemType = t;
		matched = false;
		this.x = x;
		this.y = y;
		this.dim = dim;
	}

	public Gem(Gem g) {
		this.gemType = g.gemType;
		this.x = g.x;
		this.y = g.y;
		this.dim = g.dim;
		this.matched = g.matched;
	}

	public void toBlank() {
		gemType = Type.BLANK;
	}

	public void setType(Type t) {
	    gemType = t;
	}

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
