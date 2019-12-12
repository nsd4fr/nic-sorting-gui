import java.awt.Color;
import java.awt.Rectangle;

//Nicolas Dalton - nsd4fr - 11/10/17

public class RankedRectangle extends Rectangle {
	
	public int rank;
	private boolean sorted = true;
	public Color color;
	
	public RankedRectangle(int rank, Rectangle r) {
		super(r);
		this.rank = rank;
	}
	
	public RankedRectangle(int rank, int xPos, int yPos, int width, int height) {
		super(xPos, yPos, width, height);
		this.rank = rank;
	}
	
	public RankedRectangle(RankedRectangle rr) {
		super(rr.x, rr.y, rr.width, rr.height);
		rank = rr.rank;
	}
	
	public boolean isSorted() {
		return sorted;
	}
	public void setSorted(boolean boo) {
		sorted = boo;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	@Override
	public String toString() {
		return "" + rank;
	}

}
