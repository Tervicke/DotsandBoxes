import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import static java.lang.Math.*;

public class Main {
    public static void main(String[] args) {
        JFrame Mainframe = new JFrame("Dots and boxes");
        mycanvas mc = new mycanvas();
        MLabel player1score = new MLabel("0");
        player1score.setBackground(new Color(220,20,60));
        MLabel player2score = new MLabel("0");
        player2score.setBackground(new Color(30,144,255));
        mc.setScorelistener(new ScoreListener() {
            @Override
            public void onScoreUpdate(int[] newScore) {
                player1score.setText(String.valueOf(newScore[1]));
                player2score.setText(String.valueOf(newScore[2]));
            }
        });
        JPanel scoreboard = new JPanel();
        MButton reset_button = new MButton("Reset" , mc::Reset);
        scoreboard.add(player1score,BorderLayout.EAST);
        scoreboard.add(reset_button, BorderLayout.CENTER);
        scoreboard.add(player2score,BorderLayout.WEST);
        Mainframe.add(scoreboard,BorderLayout.NORTH);
        Mainframe.add(mc,BorderLayout.SOUTH);
        mc.setPreferredSize(new Dimension(410,410));
        Mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Mainframe.pack();
        Mainframe.setVisible(true);
    }
}
class mycanvas extends Canvas {
    private int first_selection = -1; //this will take the circle number
    private int second_selection = -1;
    private final int spacing = 60;
    private final int circleWidth = 30;
    private final int circleHeight = 30;
    private final int rows = 7;
    private final int cols = 7;
    private final int startX = 10;
    private final int startY = 10;
    private final int[][] Nodes = new int[rows * rows + 1][cols * cols + 1];
    private ScoreListener scorelistener;

    {
        for (int[] row : Nodes) {
            Arrays.fill(row, -1);
        }
    }

    private int current_color = 1;
    private final int[] scores = {0, 0, 0};

    public mycanvas() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int circleNo = detectClickedCircle(e.getX(), e.getY());
                if (circleNo != -1) {
                    if (first_selection == -1) { //first not selected , select it as the first
                        System.out.println("Accepted " + first_selection + " " + circleNo);
                        first_selection = circleNo;
                        repaint();
                    } else {
                        System.out.println("Tried " + first_selection + " " + circleNo);
                        if(first_selection == circleNo){
                            first_selection = -1;
                            repaint();
                            return;
                        }
                        if (abs(first_selection - circleNo) == 1 || abs(first_selection - circleNo) == 7) {
                            second_selection = circleNo;
                            if (Nodes[first_selection][second_selection] != -1) {
                                System.out.println("taken already");
                                return;
                            }
                            Nodes[first_selection][second_selection] = 0;
                            Nodes[second_selection][first_selection] = 0;
                            CheckSquare(first_selection, second_selection);
                            //clear the first selection
                            first_selection = -1;
                            //toggle the current color
                            if (current_color == 1) {
                                current_color = 2;
                            } else {
                                current_color = 1;
                            }
                            repaint();
                        } else {
                            System.out.println("Error not a valid line");
                            first_selection = circleNo;
                            repaint();
                        }
                    }
                }
            }
        });
    }

    private void CheckSquare(int first_selection, int second_selection) {
        //check for the horizontal line or vertical line
        int mx = max(first_selection, second_selection);
        int mn = min(first_selection, second_selection);
        if (mx - mn == 1) { //horizontal line
            //bruteforce and check for the both the squares
            if (first_selection <= 42 && second_selection <= 42 && first_selection >= 8 && second_selection >= 8) {
                CheckBottomSquare(mx, mn);
                CheckAboveSquare(mx, mn);
                repaint();
            }else if(first_selection <= 7 && second_selection <= 7){
                CheckBottomSquare(mx , mn);
            }else{
                CheckAboveSquare(mx,mn);
            }
        } else if (mx - mn == 7) {
            CheckLeftSquare(mx, mn);
            CheckRightSquare(mx,mn);
        }
    }

    private void CheckRightSquare(int mx, int mn) {
        int bottom_line = Nodes[mx][mx+1];
        int above_line = Nodes[mn][mn+1];
        int right_line = Nodes[mx+1][mn+1];
        int current_line = Nodes[mn][mx];
        if(bottom_line != -1 && current_line != -1 && right_line != -1 && above_line != -1){
            updateScore();
            Nodes[mx][mx+1] = current_color;
            Nodes[mx+1][mx] = current_color;

            Nodes[mn][mn+1] = current_color;
            Nodes[mn+1][mn] = current_color;

            Nodes[mx+1][mn+1] = current_color;
            Nodes[mn+1][mx+1] = current_color;

            Nodes[mn][mx] = current_color;
            Nodes[mx][mn] = current_color;
            repaint();
        }
    }

    private void CheckLeftSquare(int mx, int mn) {
        int bottom_line = Nodes[mx][mx-1];
        int above_line = Nodes[mn][mn-1];
        int left_line = Nodes[mx-1][mn-1];
        int current_line = Nodes[mx][mn];
        if(bottom_line != -1 && current_line != -1 && left_line != -1 && above_line != -1){
            updateScore();
            Nodes[mx][mx-1] = current_color;
            Nodes[mx-1][mx] = current_color;

            Nodes[mn][mn-1] = current_color;
            Nodes[mn-1][mn] = current_color;

            Nodes[mx-1][mn-1] = current_color;
            Nodes[mn-1][mx-1] = current_color;

            Nodes[mx][mn] = current_color;
            Nodes[mn][mx] = current_color;

            repaint();
        }
    }

    private void CheckBottomSquare(int mx, int mn) {
        //get bottom line , left line , right line , current_line
        int bottom_line = Nodes[mn + 7][mx + 7];
        int left_line = Nodes[mn][mn + 7];
        int right_line = Nodes[mx][mx + 7];
        int current_line = Nodes[mx][mn];
        System.out.println(bottom_line);
        System.out.println(left_line);
        System.out.println(right_line);
        System.out.println(mx + " " + mn);
        System.out.println(current_line);
        if (bottom_line != -1 && current_line != -1 && left_line != -1 && right_line != -1) { //check if the square exists
            //update the score of the current user and make all lines the others equal to that indicating its his square
            updateScore();
            Nodes[mn + 7][mx + 7] = current_color;
            Nodes[mx + 7][mn + 7] = current_color;

            Nodes[mn][mn + 7] = current_color;
            Nodes[mn + 7][mn] = current_color;

            Nodes[mx][mx + 7] = current_color;
            Nodes[mx + 7][mx] = current_color;

            Nodes[mx][mn] = current_color;
            Nodes[mn][mx] = current_color;
            repaint();
        }
    }

    private void CheckAboveSquare(int mx, int mn) {
        int above_line = Nodes[mx - 7][mn - 7];
        int left_line = Nodes[mn][mn - 7];
        int right_line = Nodes[mx][mx - 7];
        int current_line = Nodes[mx][mn];

        System.out.println(current_line);
        System.out.println(left_line);
        System.out.println(right_line);
        System.out.println(mx + " " + mn);
        System.out.println(current_line);
        if (above_line != -1 && current_line != -1 && left_line != -1 && right_line != -1) { //check if the square exists
            //update the score of the current user and make all lines the others equal to that indicating its his square
            updateScore();

            Nodes[mx - 7][mn - 7] = current_color;
            Nodes[mn - 7][mx - 7] = current_color;

            Nodes[mn][mn - 7] = current_color;
            Nodes[mn - 7][mn] = current_color;

            Nodes[mx][mx - 7] = current_color;
            Nodes[mx - 7][mx] = current_color;

            Nodes[mx][mn] = current_color;
            Nodes[mn][mx] = current_color;

            repaint();

        }

    }

    private void updateScore() {
        scores[current_color] += 1;
        if(scorelistener != null){
            scorelistener.onScoreUpdate(scores);
        }
    }

    private int detectClickedCircle(int mouseX, int mouseY) {
        int circleNumber = 1;
        for (int j = 0; j < rows; j++) {  // ⬅ Same fix here: Columns first
            for (int i = 0; i < cols; i++) {
                int x = startX + spacing * i;
                int y = startY + spacing * j;

                if (mouseX >= x && mouseX <= x + circleWidth &&
                        mouseY >= y && mouseY <= y + circleHeight) {
                    return circleNumber;
                }
                circleNumber++;
            }
        }
        return -1; // No circle clicked
    }

        public void paint(Graphics g){
        DesignUI(g);
    }

    public void DesignUI(Graphics g) {
        int circleNumber = 1;
        Map<Integer,int[]> CirclePositions = new HashMap<>(); //Records the center of the Circle not its actual position
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = startX + spacing * j;
                int y = startY + spacing * i;
                if(first_selection != -1 && circleNumber == first_selection){
                    if(current_color == 1){
                        g.setColor(Color.RED);
                    }else{
                        g.setColor(Color.BLUE);
                    }
                }else{
                    g.setColor(Color.BLACK);
                }
                g.drawOval(x, y, circleWidth, circleHeight);
                CirclePositions.put(circleNumber,new int[]{x + circleWidth/2,y + circleHeight/2});
                circleNumber++;
            }
        }


        for(int i = 0 ; i < Nodes.length ; i++){
            for(int j = 0 ; j < Nodes[0].length ; j++){
                if(Nodes[i][j] != -1){
                    if(Nodes[i][j] == 1){
                        g.setColor(Color.RED);
                    }else if(Nodes[i][j] == 2){
                        g.setColor(Color.BLUE);
                    }else if(Nodes[i][j] == 0){
                        g.setColor(Color.BLACK);
                    }
                    g.drawLine(CirclePositions.get(i)[0] , CirclePositions.get(i)[1] , CirclePositions.get(j)[0] , CirclePositions.get(j)[1] );
                }
            }
        }
    }
    public void Reset(){
        for (int[] row : Nodes) {
            Arrays.fill(row, -1);
        }
        Arrays.fill(scores,0);
        updateScore();
        repaint();
    }

    public void setScorelistener(ScoreListener scorelistener) {
        this.scorelistener = scorelistener;
    }
}