package main;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Main extends JFrame {
	private JButton btnPlay, btnReset;
	private JPanel board;
	private Timer ctrl, ctrl2;
	private ButtonAction bl;

	//private int i1, i2; //debug variables
	private int endIndex = 9;
	private ArrayList<Integer> i1;
	private ArrayList<Integer> i2;
	private int index = 0;
	private int[] vals;
	private int[] valsDisplay;
	//private int[] cx = {375, 175, 575,  75, 275, 475, 675,  25, 125, 225};
	private int[] cx = {425, 225, 625,  125, 325, 525, 725,  75, 175, 275};
	private int[] cy = {200, 300, 300, 400, 400, 400, 400, 500, 500, 500};
	//private int[] cxo = {375, 175, 575,  75, 275, 475, 675,  25, 125, 225};
	private int[] cxo = {425, 225, 625,  125, 325, 525, 725,  75, 175, 275};
	private int[] cyo = {200, 300, 300, 400, 400, 400, 400, 500, 500, 500};
	private Color[] cC = {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
	private int cDiam = 50;

	public Main() {
		i1 = new ArrayList<Integer>();
		i2 = new ArrayList<Integer>();
		vals = new int[10];
		valsDisplay = new int[10];
		//populate array with random values
		randomizeArray();
		sort(vals);
		btnPlay = new JButton("Play");
		btnReset = new JButton("Reset");
		bl = new ButtonAction();
		btnPlay.addActionListener(bl);
		btnReset.addActionListener(bl);
		board = new DrawCanvas();
		add(board);
		JPanel pnlSouth = new JPanel(new FlowLayout());
		pnlSouth.add(btnPlay);
		pnlSouth.add(btnReset);
		add(pnlSouth, BorderLayout.SOUTH);
		ctrl = new Timer(1, new TimerAction());
		ctrl.setInitialDelay(2500);
		ctrl2 = new Timer(1, new TimerAction2());


	}

	private class DrawCanvas extends JPanel{
		public DrawCanvas(){
			super(null);
		}
		//@paint
		public void paint(Graphics g){
			super.paint(g);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 900, 700);

			//Draw connecting lines
			g.setColor(Color.WHITE);
			for (int i = 0; i < 5; i++) {
				g.drawLine(cxo[i]+cDiam/2, cyo[i]+cDiam/2, cxo[2*i+1]+cDiam/2, cyo[2*i+1]+cDiam/2);
				if(i != 4) {
					g.drawLine(cxo[i]+cDiam/2, cyo[i]+cDiam/2, cxo[2 * i + 2]+cDiam/2, cyo[2 * i + 2]+cDiam/2);
				}
			}

			//Draw nodes
			for (int i = 0; i < cx.length; i++) {
				g.setColor(cC[i]);
				g.fillOval(cx[i], cy[i], cDiam, cDiam);
			}


			int fontSize = 15;
			g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
			//Draw numbers
			g.setColor(Color.BLUE);
			for (int i = 0; i < valsDisplay.length; i++) {
				g.drawString(Integer.toString(valsDisplay[i]), cx[i]+cDiam/4, cy[i]+cDiam/2 + 5);
			}



		}
	}

	//circle movement
	private class TimerAction implements ActionListener{
		public void actionPerformed(ActionEvent x){
				// movement of circle
				int index1 = i1.get(index);
				int index2 = i2.get(index);
				if(cxo[index1] > cxo[index2] && cx[index2] != cxo[index1]) {
					cx[index1]--;
					cx[index2]++;
				} else if (cxo[index1] < cxo[index2] && cx[index2] != cxo[index1]) {
					cx[index1]++;
					cx[index2]--;
				}
				if(cyo[index1] > cyo[index2]  && cy[index2] != cyo[index1]) {
					cy[index1]--;
					cy[index2]++;
				} else if(cyo[index1] < cyo[index2]  && cy[index2] != cyo[index1]) {
					cy[index1]++;
					cy[index2]--;
				}
				//end of animation
				if(cy[index2] == cyo[index1] && cx[index2] == cxo[index1]) {
					cx[index1] = cxo[index1];
					cx[index2] = cxo[index2];
					cy[index1] = cyo[index1];
					cy[index2] = cyo[index2];
						System.out.println(index1+"    "+index2+"   WHITE");
					cC[index1] = Color.WHITE;
					cC[index2] = Color.WHITE;

					if(index1 == 0)
						System.out.println(index1 + " ? " + index2 + " ? "+ endIndex+ "    " + (index1 == 0 && index2 == endIndex));
					if(index1 == 0 && index2 == endIndex && valsDisplay[index1] > valsDisplay[index2]) {
						System.out.println(index2 +   "   "+  "BLACK");
						cC[endIndex] = Color.BLACK;
						endIndex--;
						System.out.println(endIndex);
					}
					int temp = valsDisplay[index1];
					valsDisplay[index1] = valsDisplay[index2];
					valsDisplay[index2] = temp;
					index++;
					if(index < i1.size()) {
						ctrl.restart();
						ctrl2.restart();
					} else {
						System.out.println("end all");
						if(index == i1.size())
							cC[0] = Color.BLACK;
						ctrl.stop();
						ctrl2.stop();
					}
				}
				repaint();

		}
	}
	//circle highlight
	private class TimerAction2 implements ActionListener{
		public void actionPerformed(ActionEvent x){
			// movement of circle

			int index1 = i1.get(index);
			int index2 = i2.get(index);
			cC[index1] = Color.RED;
			cC[index2] = Color.RED;

			repaint();
		}
	}

	public void randomizeArray() {
		Random r = new Random();
		for (int i = 0; i < vals.length; i++) {
			vals[i] = r.nextInt(1000);
			valsDisplay[i] = vals[i];
			System.out.println(vals[i]);
		}
	}

	//Heap sort methods
	public int[] sort(int[]A){
		for(int i = (A.length-1)/2; i>=0; i--){
			A=build_heap(A,i,A.length-1);
		}
		for(int i=0 ; i<A.length-1; i++)
		{
			A= swap (A,0,A.length-1-i);
			A= build_heap(A,0,A.length-2-i);
		}
		return A;
	}

	public int[] build_heap(int[]A, int node, int limit){
		int child=node*2+1;
		while (child<=limit){
			if (child+1 <= limit && A[child] < A[child+1])
				child++;
			if(A[child] < A[node])
				break;
			A= swap(A, node, child);
			node = child;
			child = 2*node+1;
		}
		return A;
	}

	public int[] swap (int[]A, int in1, int in2) {
		i1.add(in1);
		i2.add(in2);
		int temp = A[in1];
		A[in1]=A[in2];
		A[in2]=temp;
		return A;
	}

	public void startSwapAnimation() {
		ctrl.start();
		ctrl2.start();
	}


	public static void createAndShowGUI() {
		Main app = new Main();
		app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		app.setSize(900, 700);
		app.setLocationRelativeTo(null);
		app.setVisible(true);

	}

	private class ButtonAction implements ActionListener{
		public void actionPerformed(ActionEvent x){
			if(x.getSource().equals(btnPlay)) {
				if(ctrl.isRunning()) {
					ctrl.stop();
					ctrl2.stop();
					btnPlay.setText("Play");
				} else {
					btnPlay.setText("Pause");
					startSwapAnimation();
				}
			} else if (x.getSource().equals(btnReset)) {
				System.out.println("Reset button pressed");
				ctrl.stop();
				ctrl2.stop();
				btnPlay.setText("Play");
				//reset node position and colors
				for (int i = 0; i < vals.length; i++) {
					cC[i] = Color.WHITE;
					cx[i] = cxo[i];
					cy[i] = cyo[i];
				}
				randomizeArray();
				// clear animation map (i1 and i2)
				//reset variables
				i1.clear();
				i2.clear();
				index = 0;
				endIndex = 9;
				System.out.println(i1.size());
				sort(vals);
				System.out.println(i1.size());
				repaint();
			}

		}
	}


	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

	}

}
