//Nicolas Dalton - nsd4fr - 11/10/17

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;


//Used ideas from textbook page 972 for general structure
public class AlgoDisp extends JFrame {
	JPanel selectionP, insertionP, bubbleP, dispP, buttonP, titleP;
	JTextArea selectionTA, insertionTA, bubbleTA;
	JButton scramble, go;
	JLabel selectionL, insertionL, bubbleL, colorL;
	
	InsertionComponent insertionAnime;
	SelectionComponent selectionAnime;
	BubbleComponent bubbleAnime;
	
	ArrayList<RankedRectangle> selectionList, insertionList, bubbleList;
	
	boolean sortIsDone;
	
	
	public AlgoDisp() {
		
		this.setTitle("Algorithm Comparison");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(900, 420);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		
		//Create the ArrayList of RankedRectangles
		selectionList = new ArrayList<RankedRectangle>();
		insertionList = new ArrayList<RankedRectangle>();
		bubbleList = new ArrayList<RankedRectangle>();
		
		Rectangle startingR = new Rectangle(20, 5, 255, 3);
		selectionList.add(new RankedRectangle(0, startingR));
		insertionList.add(new RankedRectangle(0, startingR));
		bubbleList.add(new RankedRectangle(0, startingR));
		
		for(int i = 1; i < 50; i++) {
			RankedRectangle newRec = new RankedRectangle(i, 20, selectionList.get(i-1).y+6, selectionList.get(i-1).width-5, 3);
			selectionList.add(new RankedRectangle(newRec));
			insertionList.add(new RankedRectangle(newRec));
			bubbleList.add(new RankedRectangle(newRec));
		}
		
		//Declare the main display Panel
		dispP = new JPanel();
		dispP.setLayout(new GridLayout(1, 3, 10, 10));
		this.add(dispP, BorderLayout.CENTER);
		
		//Button Panel
		buttonP = new JPanel();
		scramble = new JButton("Scramble All");
		go = new JButton("GO!");
		go.addActionListener(new Go());
		buttonP.add(scramble);
		buttonP.add(go);
		colorL = new JLabel("Blue is sorted, Red is unsorted, Green is the current window, Yellow is a potential swap");
		buttonP.add(colorL);
		
		scramble.addActionListener(new Scrambler());
		
		this.add(buttonP, BorderLayout.SOUTH);
		
		//Title Panel
		titleP = new JPanel();
		titleP.setLayout(new GridLayout(1, 3, 10, 10));
		
		selectionL = new JLabel("Selection Sort");
		insertionL = new JLabel("Insertion Sort");
		bubbleL = new JLabel("Bubble Sort");
		
		ArrayList<JLabel> labelArrList = new ArrayList<JLabel>();
		labelArrList.add(selectionL);
		labelArrList.add(insertionL);
		labelArrList.add(bubbleL);
		
		for(JLabel each: labelArrList) {
			each.setForeground(Color.BLUE);
			titleP.add(each);
			
		}
		
		this.add(titleP, BorderLayout.NORTH);
		
		//Panel 1: Selection Sort
		
		//Declare the panel
		selectionP = new JPanel();
		//set a layout
		selectionP.setLayout(new BoxLayout(selectionP, BoxLayout.Y_AXIS));
		selectionP.setBorder(new LineBorder(Color.BLACK, 1));
		selectionAnime = new SelectionComponent(selectionList);
		selectionP.add(selectionAnime);
		//add the panel to the display Panel
		dispP.add(selectionP);
		
		
		//Panel 2: Insertion Sort
		
		//Declare the panel
		insertionP = new JPanel();
		//set a layout
		insertionP.setLayout(new BoxLayout(insertionP, BoxLayout.Y_AXIS));
		insertionP.setBorder(new LineBorder(Color.BLACK, 1));
		insertionAnime = new InsertionComponent(insertionList);
		insertionP.add(insertionAnime);
		//add the panel to the display panel
		dispP.add(insertionP);
		
		
		
		//Panel 3: Bubble Sort
		bubbleP = new JPanel();
		bubbleP.setBorder(new LineBorder(Color.BLACK, 1));
		bubbleP.setLayout(new BoxLayout(bubbleP, BoxLayout.Y_AXIS));
		bubbleAnime = new BubbleComponent(bubbleList);
		bubbleP.add(bubbleAnime);
		
		dispP.add(bubbleP);
		
		//Final Edits		
		this.setVisible(true);
		
	}
	
	private class Scrambler implements ActionListener { 
		//Alternatively, if I dont want to store the ArrayList of RankedRectangles here, I can implement a scramble method in
		//a lower subclass then just call it as a part of this method.
		@Override
		public void actionPerformed(ActionEvent e) {
			// Got this idea from https://stackoverflow.com/questions/10312442/how-to-shuffle-two-list-in-the-same-fashion-in-java?noredirect=1&lq=1
			long seed = System.nanoTime(); 
			if(insertionAnime.isDone()) {
				insertionAnime.scramble(seed);
			}
			if(selectionAnime.isDone()) {
				selectionAnime.scramble(seed);
			}
			if(bubbleAnime.isDone()) {
				bubbleAnime.scramble(seed);
			}
		}
	}
	
	private class Go implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(insertionAnime.canStart()) {
				insertionAnime.startAnimation();
				//insertionAnime.repaint();
			}
			if(selectionAnime.canStart()) {
				selectionAnime.startAnimation();
			}
			if(bubbleAnime.canStart()) {
				bubbleAnime.startAnimation();
			}
		}
	}

	public static void main(String[] args) {
		new AlgoDisp();
	}
}