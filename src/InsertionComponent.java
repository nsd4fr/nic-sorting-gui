//Nicolas Dalton - nsd4fr - 11/10/17


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.JComponent;


public class InsertionComponent extends JComponent {
	
	ArrayList<RankedRectangle> currentList;
		
	boolean done = true;
	boolean start = false;
	int marked; 
	
	public InsertionComponent(ArrayList<RankedRectangle> list) {
		currentList = list;
		//sortedStateLock = new ReentrantLock();
	}
	
	public boolean isDone() {
		return done;
	}
	public boolean canStart() {
		return start;
	}
	
	public void sort() throws InterruptedException {
		for(int i = 1; i<currentList.size();i++) {
			boolean cont = true;
			start = false;
			for(int j = i;j>0 && cont;j--) {
				marked = currentList.get(j).rank;
				try {
					////sortedStateLock.lock();
					if(currentList.get(j).rank < currentList.get(j-1).rank) {
						//start swap action
						RankedRectangle temp = currentList.get(j);
						currentList.set(j, currentList.get(j-1));
						currentList.set(j-1, temp);
						//end swap action 
					} else {
						cont = false;
						currentList.get(j).setSorted(true);

					}
				} finally {
					//sortedStateLock.unlock();
					refreshPos();
				}
				repaint();
				Thread.sleep(75);
			}
		}
		done = true;
		start = false;
	}
	
	public void refreshPos() {
		//////sortedStateLock.lock();
		currentList.get(0).setSorted(true);
		currentList.get(0).setLocation(20, 5);
		for(int i = 1; i<currentList.size(); i++) {
			currentList.get(i).setLocation(20, currentList.get(i-1).y+6);
		}
		////sortedStateLock.unlock();
	}
	
	public void scramble(long seed) {
		////sortedStateLock.lock();
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
					g2.setColor(Color.red);
				}
				if(each.rank == marked ) {
					g2.setColor(Color.GREEN);
				}
				g2.fill(each);
				g2.draw(each);
			}
		} finally {
			//sortedStateLock.unlock();
		}
		
	}
	
	public void startAnimation() {
		
		class AnimationRunnable implements Runnable {
			public void run() {
				try {
					sort();
				} catch (InterruptedException e) {	
					System.out.println("Error Bro");
				}
			}
		}
		
		Runnable r = new AnimationRunnable();
		Thread t = new Thread(r);
		t.start();
	}
}
