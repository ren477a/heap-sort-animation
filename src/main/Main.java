package main;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame {
    //GUI components
    private JButton btnPlay, btnReset;
    private JLabel[] sorted;
    private JPanel board;
    private JLabel lblCurrLog;
    private Timer ctrl, ctrl2, ctrl3;
    private ButtonAction bl;

    //animation map
    private int endIndex = 9;
    private ArrayList<Integer> i1;
    private ArrayList<Integer> i2;
    private ArrayList<Boolean> willSwap;
    private ArrayList<Boolean> isSibling;

    //animation variables
    private int index = 0;
    private int[] vals;
    private int[] valsDisplay;
    //private int[] cx = {375, 175, 575,  75, 275, 475, 675,  25, 125, 225};
    private int[] cx = {425, 225, 625, 125, 325, 525, 725, 75, 175, 275};
    private int[] cy = {100, 200, 200, 300, 300, 300, 300, 400, 400, 400};
    //private int[] cxo = {375, 175, 575,  75, 275, 475, 675,  25, 125, 225};
    private int[] cxo = {425, 225, 625, 125, 325, 525, 725, 75, 175, 275};
    private int[] cyo = {100, 200, 200, 300, 300, 300, 300, 400, 400, 400};
    private Color[] cC = {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
    private Color[] valsColor = {Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE};
    private int cDiam = 50;

    private String logMsg = "";
    private String fullLogMsg = "";

    public Main() {
        i1 = new ArrayList<Integer>();
        i2 = new ArrayList<Integer>();
        willSwap = new ArrayList<Boolean>();
        isSibling = new ArrayList<Boolean>();
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
        //GUI
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        board = new DrawCanvas();
        add(board);

        JPanel pnlNorth = new JPanel(new GridLayout(2, 11, 0, 5));
        pnlNorth.add(new JLabel("Index:"));
        for(int i = 0; i < 10; i++) {
            pnlNorth.add(new JLabel(""+i, SwingConstants.CENTER));
        }
        pnlNorth.add(new JLabel("Sorted:"));
        sorted = new JLabel[10];
        for (int i = 0; i < sorted.length; i++) {
            sorted[i] = new JLabel("", SwingConstants.CENTER);
            sorted[i].setBorder(border);
            pnlNorth.add(sorted[i]);
        }
        lblCurrLog = new JLabel(" ", SwingConstants.CENTER);
        lblCurrLog.setBorder(border);
        JPanel pnl = new JPanel(new GridLayout(2, 1, 0, 20));
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnl.add(pnlNorth);
        pnl.add(lblCurrLog);
        JPanel pnlSouth = new JPanel(new FlowLayout());
        pnlSouth.add(btnPlay);
        pnlSouth.add(btnReset);
        add(pnlSouth, BorderLayout.SOUTH);
        add(pnl, BorderLayout.NORTH);
        ctrl = new Timer(10, new TimerAction());
        ctrl.setInitialDelay(5000);
        ctrl2 = new Timer(1, new TimerAction2());
        ctrl3 = new Timer(1, new TimerAction3());
        ctrl3.setInitialDelay(5000);


    }

    private class DrawCanvas extends JPanel {
        public DrawCanvas() {
            super(null);
        }

        //@paint
        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, 900, 700);

            //Draw connecting lines
            g.setColor(Color.WHITE);
            for (int i = 0; i < 5; i++) {
                if (cC[2 * i + 1] != Color.CYAN)
                    g.drawLine(cxo[i] + cDiam / 2, cyo[i] + cDiam / 2, cxo[2 * i + 1] + cDiam / 2, cyo[2 * i + 1] + cDiam / 2);

                if (i != 4)
                    if (cC[2 * i + 2] != Color.CYAN)
                        g.drawLine(cxo[i] + cDiam / 2, cyo[i] + cDiam / 2, cxo[2 * i + 2] + cDiam / 2, cyo[2 * i + 2] + cDiam / 2);

            }

            //Draw nodes
            for (int i = cx.length - 1; i >= 0; i--) {
                g.setColor(cC[i]);
                g.fillOval(cx[i], cy[i], cDiam, cDiam);
            }


            int fontSize = 15;
            g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
            //Draw numbers
            for (int i = 0; i < valsDisplay.length; i++) {
                g.setColor(valsColor[i]);
                g.drawString(Integer.toString(valsDisplay[i]), cx[i] + cDiam / 4, cy[i] + cDiam / 2 + 5);
            }


        }
    }

    //circle movement
    private class TimerAction implements ActionListener {
        public void actionPerformed(ActionEvent x) {
            // movement of circle
            int index1 = i1.get(index);
            int index2 = i2.get(index);


            if (!(index1 == 0 && index2 == endIndex && valsDisplay[index1] > valsDisplay[index2])) {
                if (!logMsg.equals("Swap: " + valsDisplay[index1] + " and " + valsDisplay[index2])) {
                    logMsg = "Swap: " + valsDisplay[index1] + " and " + valsDisplay[index2];
                    System.out.println(logMsg);
                    lblCurrLog.setText(logMsg);

                }
            }
            if (cxo[index1] > cxo[index2] && cx[index2] != cxo[index1]) {
                cx[index1]--;
                cx[index2]++;
            } else if (cxo[index1] < cxo[index2] && cx[index2] != cxo[index1]) {
                cx[index1]++;
                cx[index2]--;
            }
            if (cyo[index1] > cyo[index2] && cy[index2] != cyo[index1]) {
                cy[index1]--;
                cy[index2]++;
            } else if (cyo[index1] < cyo[index2] && cy[index2] != cyo[index1]) {
                cy[index1]++;
                cy[index2]--;
            }
            //end of animation
            if (cy[index2] == cyo[index1] && cx[index2] == cxo[index1]) {
                cx[index1] = cxo[index1];
                cx[index2] = cxo[index2];
                cy[index1] = cyo[index1];
                cy[index2] = cyo[index2];
                //System.out.println(index1+"    "+index2+"   WHITE");
                cC[index1] = Color.WHITE;
                cC[index2] = Color.WHITE;

                if (index1 == 0)
                    //System.out.println(index1 + " ? " + index2 + " ? "+ endIndex+ "    " + (index1 == 0 && index2 == endIndex));
                    if (index1 == 0 && index2 == endIndex && valsDisplay[index1] > valsDisplay[index2]) {
                        //System.out.println(index2 +   "   "+  "BLACK");
                        cC[endIndex] = Color.CYAN;
                        valsColor[endIndex] = Color.CYAN;
                        sorted[endIndex].setText(Integer.toString(valsDisplay[0]));
                        endIndex--;
                        //System.out.println(endIndex);
                    }
                int temp = valsDisplay[index1];
                valsDisplay[index1] = valsDisplay[index2];
                valsDisplay[index2] = temp;
                repaint();

                index++;
                if (index < i1.size()) {
                    ctrl.stop();
                    ctrl2.stop();
                    startSwapAnimation();
                } else {
                    System.out.println("end all");
                    if (index == i1.size()) {
                        cC[0] = Color.CYAN;
                        valsColor[0] = Color.CYAN;
                        sorted[endIndex].setText(Integer.toString(valsDisplay[0]));
                    }
                    ctrl.stop();
                    ctrl2.stop();
                }
            }

        }
    }

    //circle highlight
    private class TimerAction2 implements ActionListener {
        public void actionPerformed(ActionEvent x) {
            int index1 = i1.get(index);
            int index2 = i2.get(index);
            cC[index1] = Color.RED;
            cC[index2] = Color.RED;
            repaint();

            if (index1 == 0 && index2 == endIndex && valsDisplay[index1] > valsDisplay[index2]) {
                if(!logMsg.equals("MAX HEAP! Swap " + valsDisplay[index1] +" and " + valsDisplay[index2])) {
                    logMsg = "MAX HEAP! Swap " + valsDisplay[index1] +" and " + valsDisplay[index2];
                    lblCurrLog.setText(logMsg);
                }
            }
        }
    }

    //change back to normal color
    private class TimerAction3 implements ActionListener {
        public void actionPerformed(ActionEvent x) {
             
            int index1 = i1.get(index);
            int index2 = i2.get(index);
//            if(!logMsg.equals("Swap: " + valsDisplay[index1] + " and " + valsDisplay[index2])) {
//                logMsg = "Swap: " + valsDisplay[index1] + " and " + valsDisplay[index2];
//                System.out.println(logMsg);
//                lblCurrLog.setText(logMsg);
//                pushLog(logMsg);
//            }
            cC[index1] = Color.WHITE;
            cC[index2] = Color.WHITE;
            repaint();
            ctrl2.stop();
            ctrl3.stop();
            index++;
            if (index < i1.size()) {
                startSwapAnimation();
            }

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
    public int[] sort(int[] A) {
        for (int i = (A.length - 1) / 2; i >= 0; i--) {
            A = build_heap(A, i, A.length - 1);
        }
        for (int i = 0; i < A.length - 1; i++) {
            i1.add(0);
            i2.add(A.length - 1 - i);
            willSwap.add(true);
            isSibling.add(false);
            A = swap(A, 0, A.length - 1 - i);
            A = build_heap(A, 0, A.length - 2 - i);
        }
        return A;
    }

    public int[] build_heap(int[] A, int node, int limit) {
        int child = node * 2 + 1;
        while (child <= limit) {
            if (child + 1 <= limit) {
                //System.out.println("child " + A[child] + " " + A[child+1] + " " + (A[child] < A[child+1]));
                i1.add(child);
                i2.add(child + 1);
                willSwap.add(false);
                isSibling.add(true);
            }
            if (child + 1 <= limit && A[child] < A[child + 1])
                child++;
            i1.add(child);
            i2.add(node);
            willSwap.add(!(A[child] < A[node]));
            isSibling.add(false);
            System.out.println(A[child] + " " + A[node] + " willSwap: " + !(A[child] < A[node]));
            if (A[child] < A[node])
                break;
            A = swap(A, node, child);
            node = child;
            child = 2 * node + 1;
        }
        return A;
    }

    public int[] swap(int[] A, int in1, int in2) {
//		i1.add(in1);
//		i2.add(in2);
        //	System.out.println(A[in1] + " " + A[in2]);
        int temp = A[in1];
        A[in1] = A[in2];
        A[in2] = temp;
        return A;
    }

    public void startSwapAnimation() {
        if(isSibling.get(index)) {
            logMsg = "Compare siblings: (" + valsDisplay[i1.get(index)] + ", " + valsDisplay[i2.get(index)] + ")   ";
            if(valsDisplay[i1.get(index)] > valsDisplay[i2.get(index)]) {
                logMsg += ("Choose " + valsDisplay[i1.get(index)]);
            } else {
                logMsg += ("Choose " + valsDisplay[i2.get(index)]);
            }
        } else
            logMsg = "Compare: " + valsDisplay[i1.get(index)] + " > " + valsDisplay[i2.get(index)] + " ? " + willSwap.get(index);
        System.out.println(logMsg);
        lblCurrLog.setText(logMsg);
        ctrl2.start();
        if (willSwap.get(index)) {
            ctrl.start();
        } else
            ctrl3.start();

        //enable delay after unpausing
        ctrl.setInitialDelay(5000);
        ctrl3.setInitialDelay(5000);
    }


    public static void createAndShowGUI() {
        Main app = new Main();
        app.setTitle("Heap Sort Algorithm");
        app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        app.setSize(900, 750);
        app.setLocationRelativeTo(null);
        app.setVisible(true);

    }

    private class ButtonAction implements ActionListener {
        public void actionPerformed(ActionEvent x) {
            if (x.getSource().equals(btnPlay)) {
                if (ctrl.isRunning()) {
                    //pause here
                    ctrl.stop();
                    ctrl2.stop();
                    ctrl3.stop();
                    //remove delay for instant unpause
                    ctrl.setInitialDelay(0);
                    ctrl3.setInitialDelay(0);
                    btnPlay.setText("Play");
                } else {
                    //unpause here
                    btnPlay.setText("Pause");
                    startSwapAnimation();
                }
            } else if (x.getSource().equals(btnReset)) {
                System.out.println("Reset button pressed");
                logMsg = " ";
                lblCurrLog.setText(logMsg);
                ctrl.stop();
                ctrl2.stop();
                ctrl3.stop();
                btnPlay.setText("Play");
                //reset node position and colors
                for (int i = 0; i < vals.length; i++) {
                    cC[i] = Color.WHITE;
                    valsColor[i] = Color.BLUE;
                    sorted[i].setText("");
                    cx[i] = cxo[i];
                    cy[i] = cyo[i];
                }
                randomizeArray();
                // clear animation map (i1 and i2)
                //reset variables
                i1.clear();
                i2.clear();
                willSwap.clear();
                isSibling.clear();
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
