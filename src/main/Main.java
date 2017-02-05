package main;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class Main extends JFrame {
	private JButton btnPlay;
	private JPanel board;
	private Timer ctrl, ctrl2;
	private ButtonAction bl;

	private int[] vals;
	private int[] cx = {375, 175, 575,  75, 275, 475, 675,  25, 125, 225};
	private int[] cy = {100, 200, 200, 300, 300, 300, 300, 400, 400, 400};
	private int[] cxo = {375, 175, 575,  75, 275, 475, 675,  25, 125, 225};
	private int[] cyo = {100, 200, 200, 300, 300, 300, 300, 400, 400, 400};
	private Color[] cC = {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
	private int cDiam = 50;

	public Main() {
		vals = new int[10];
		randomizeArray();
		btnPlay = new JButton("Play");
		bl = new ButtonAction();
		btnPlay.addActionListener(bl);
		board = new DrawCanvas();
		add(board);
		add(btnPlay, BorderLayout.SOUTH);
		ctrl = new Timer(0, new TimerAction());
		ctrl.setInitialDelay(2000);
		ctrl2 = new Timer(0, new TimerAction2());
	}

	private class DrawCanvas extends JPanel{
		public DrawCanvas(){
			super(null);
		}

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

	private class TimerAction implements ActionListener{
		public void actionPerformed(ActionEvent x){
			// movement of circle
			int index1 = 3;
			int index2 = 7;

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
				ctrl.stop();
			}
			repaint();
		}
	}

	private class TimerAction2 implements ActionListener{
		public void actionPerformed(ActionEvent x){
			// movement of circle
			int index1 = 3;
			int index2 = 7;
			cC[index1] = Color.RED;
			cC[index2] = Color.RED;
			if(!ctrl.isRunning()) {
				cC[index1] = Color.WHITE;
				cC[index2] = Color.WHITE;
				ctrl2.stop();
			}
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
				ctrl.stop();
			} else {
				ctrl.start();
				ctrl2.start();
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
