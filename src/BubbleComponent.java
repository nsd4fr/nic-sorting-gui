//Nicolas Dalton - nsd4fr - 11/10/17

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.JComponent;

public class BubbleComponent extends JComponent {
	
ArrayList<RankedRectangle> currentList;
	
	boolean done = true;
	boolean start = false;
	int marked;
	int lastEdited;
	
	public BubbleComponent(ArrayList<RankedRectangle> list) {
		currentList = list;
		//sortedStateLock = new ReentrantLock();
	}
	
	public boolean isDone() {
		return done;
	}
	public boolean canStart() {
		return start;
	}
	
	public void sort() throws InterruptedException{
		start = false;
		for(int i = 0; i<currentList.size() - 1; i++) {
			for(int j = 0; j<currentList.size()-i-1;j++) {
				marked = currentList.get(j).rank;
				try {
					//sortedStateLock.lock();
					if(currentList.get(j).rank > currentList.get(j+1).rank) {
						RankedRectangle temp = currentList.get(j);
						currentList.set(j, currentList.get(j+1));
						currentList.set(j+1, temp);
						//currentList.get(j+1).setSorted(true);
					}
				} finally {
					//sortedStateLock.unlock();
					refreshPos();
				}
				repaint();
				Thread.sleep(50);
				lastEdited = j;
			}
			currentList.get(lastEdited + 1).setSorted(true);
			
		}
		done = true;
		refreshPos();
		
	}
	
	public void refreshPos() {
		//sortedStateLock.lock();
		
		currentList.get(0).setLocation(20, 5);
		for(int i = 1; i<currentList.size();i++) {
			currentList.get(i).setLocation(20, currentList.get(i-1).y + 6);
		}
		if(done) {
			currentList.get(1).setSorted(true);
			repaint();
		}
		//sortedStateLock.unlock();
		
	}
	
	public void scramble(long seed) {
		//sortedStateLock.lock();
		done = false;
		start = true;
		Collections.shuffle(currentList, new Random(seed));
		for(RankedRectangle each: currentList) {
			each.setSorted(false);
		}
		refreshPos();
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		//sortedStateLock.lock();
		try {
			for(RankedRectangle each:currentList) {
				if(each.isSorted()) {
					g2.setColor(Color.BLUE);
				} else {
					g2.setColor(Color.RED);
				}
				if(each.rank == marked) {
					g2.setColor(Color.green);
				}
				g2.fill(each);
				g2.draw(each);
			}
		} finally {
			//sortedStateLock.unlock();
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
