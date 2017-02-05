package main;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Main extends JFrame {
	private JButton btnPlay;
	private JPanel board;
	private Timer ctrl, ctrl2;
	private ButtonAction bl;

	//private int i1, i2; //debug variables
	private ArrayList<Integer> i1;
	private ArrayList<Integer> i2;
	private int index = 0;
	private int[] vals;
	private int[] cx = {375, 175, 575,  75, 275, 475, 675,  25, 125, 225};
	private int[] cy = {100, 200, 200, 300, 300, 300, 300, 400, 400, 400};
	private int[] cxo = {375, 175, 575,  75, 275, 475, 675,  25, 125, 225};
	private int[] cyo = {100, 200, 200, 300, 300, 300, 300, 400, 400, 400};
	private Color[] cC = {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
	private int cDiam = 50;

	public Main() {
		i1 = new ArrayList<Integer>();
		i2 = new ArrayList<Integer>();
		i1.add(0);
		i2.add(1);
		i1.add(0);
		i2.add(2);
		i1.add(1);
		i2.add(3);
		i1.add(1);
		i2.add(4);
		vals = new int[10];
		randomizeArray();
		btnPlay = new JButton("Play");
		bl = new ButtonAction();
		btnPlay.addActionListener(bl);
		board = new DrawCanvas();
		add(board);
		add(btnPlay, BorderLayout.SOUTH);
		ctrl = new Timer(0, new TimerAction());
		ctrl.setInitialDelay(1000);
		ctrl2 = new Timer(0, new TimerAction2());
	}

	private class DrawCanvas extends JPanel{
		public DrawCanvas(){
			super(null);
		}
		//@paint
		public void paint(Graphics g){
			super.paint(g);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 800, 800);

			g.setColor(Color.WHITE);
			g.fillOval(cx[0], cy[0], cDiam, cDiam);
			g.fillOval(cx[1], cy[1], cDiam, cDiam);
			g.fillOval(cx[2], cy[2], cDiam, cDiam);

			for (int i = 0; i < cx.length; i++) {
				g.setColor(cC[i]);
				g.fillOval(cx[i], cy[i], cDiam, cDiam);
			}

			int fontSize = 15;
			g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));

			g.setColor(Color.RED);
			g.drawString("0", cx[0]+cDiam/2, cy[0]+cDiam/2);

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
				if(cy[index2] == cyo[index1] && cx[index2] == cxo[index1]) {
					cx[index1] = cxo[index1];
					cx[index2] = cxo[index2];
					cy[index1] = cyo[index1];
					cy[index2] = cyo[index2];
					cC[index1] = Color.WHITE;
					cC[index2] = Color.WHITE;
					ctrl2.stop();
					index++;
					System.out.println(index);
					System.out.println(index < i1.size());
					if(index < i1.size()) {
						ctrl2.start();
						ctrl.restart();
					} else {
						ctrl.stop();

					}
				}
				repaint();


		}
	}
	//circle highlight
	private class TimerAction2 implements ActionListener{
		public void actionPerformed(ActionEvent x){
			// movement of circle
		//	if(index >= i1.size()) { ctrl2.stop(); }
			int index1 = i1.get(index);
			int index2 = i2.get(index);
			cC[index1] = Color.RED;
			cC[index2] = Color.RED;
//			if(!ctrl.isRunning()) {
//				cC[index1] = Color.WHITE;
//				cC[index2] = Color.WHITE;
//				ctrl2.stop();
//			}
			repaint();
		}
	}

	public void randomizeArray() {
		Random r = new Random();
		for (int i = 0; i < vals.length; i++) {
			vals[i] = r.nextInt(1000);
			System.out.println(vals[i]);
		}
	}

	//Heap sort methods
	public static int[] sort(int[]A){
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

	public static int[] build_heap(int[]A, int node, int limit){
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

	public static int[] swap (int[]A,int in1, int in2)
	{
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
		app.setSize(800, 800);
		app.setLocationRelativeTo(null);
		app.setVisible(true);

	}

	private class ButtonAction implements ActionListener{
		public void actionPerformed(ActionEvent x){
			if(ctrl.isRunning()) {

			} else {
				startSwapAnimation();
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
