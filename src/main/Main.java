package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JFrame {

	private JButton btnPlay;

	public Main() {
		btnPlay = new JButton("Play");

	}

	public static void createAndShowGUI() {
		Main app = new Main();
		app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		app.setSize(500, 500);
		app.setLocationRelativeTo(null);
		app.setVisible(true);
	}


	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

}
