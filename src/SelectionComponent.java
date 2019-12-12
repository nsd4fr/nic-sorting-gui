//Nicolas Dalton - nsd4fr - 11/10/17


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.JComponent;

public class SelectionComponent extends JComponent{
	
	ArrayList<RankedRectangle> currentList;
		
	boolean done = true;
	boolean start = false;
	int targetPos, targetPosRank;
	int marked;
	
	public SelectionComponent(ArrayList<RankedRectangle> list) {
		currentList = list;
	}
	
	public boolean isDone() {
		return done;
	}
	public boolean canStart() {
		return start;
	}
	
	public void sort() throws InterruptedException {
		start = false;
		for(int i = 0; i<currentList.size()-1;i++) {
			targetPos = i;
			for(int j = i+1;j<currentList.size();j++) {
				try {
					if(currentList.get(j).rank < currentList.get(targetPos).rank) {
						targetPos = j;
					}
					marked = currentList.get(j).rank;
				} finally {
					refreshPos();
				}
				Thread.sleep(50);
				repaint();
			}
			try {
				RankedRectangle temp = currentList.get(targetPos);
				currentList.set(targetPos, currentList.get(i));
				currentList.set(i, temp);
				currentList.get(i).setSorted(true);
			} finally {
				refreshPos();
			}
			repaint();
			Thread.sleep(50);
		}
		done = true;
	}
	
	public void refreshPos() {
		//do something, might be unnecessary
		currentList.get(0).setLocation(20, 5);
		for(int i = 1;i<currentList.size();i++) {
			currentList.get(i).setLocation(20,  currentList.get(i-1).y + 6);
		}
	}
	
	public void scramble(long seed) {
		done = false;
		start = true;
		marked = currentList.get(0).rank;
		Collections.shuffle(currentList, new Random(seed));
		for(RankedRectangle each: currentList) {
			each.setSorted(false);
		}
		refreshPos();
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		try {
			for(RankedRectangle each:currentList) {
				if(each.isSorted()) {
					g2.setColor(Color.BLUE);
				} else {
					g2.setColor(Color.red);
				}
				if(each.rank == marked) {
					g2.setColor(Color.green);
				}
				if(currentList.indexOf(each) == targetPos) {
					g2.setColor(Color.YELLOW);
				}
				g2.fill(each);
				g2.draw(each);
			}
		} finally {
		}
	}
	
	public void startAnimation() {
		
		class AnimationRunnable implements Runnable{
			public void run() {
				try {
					sort();
				} catch (InterruptedException e) {
					System.out.println("Error bro");
				}
			}
		}
		
		Runnable r = new AnimationRunnable();
		Thread t = new Thread(r);
		t.start();
		
	}
}
