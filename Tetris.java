package tetris;



import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;


public class Tetris extends JPanel {

    public static final int REC_WIDTH = 26;
    public static final int REC_LENGTH = 26;
    public static final int WINDOW_WIDTH = 18;
    public static final int WINDOW_LENGTH = 36;

    //Game Setting
    private int fps;
    private double speed;
    private String seq;
    private Boolean pause = false;

    private Point pieceOrigin;
    private int currentPiece;
    private int rotation;
    private ArrayList<Integer> nextPieces = new ArrayList<>();

    private long score;
    private Color[][] bgColor;
    private Timer timer;
    private Boolean ifMouseSelected = false;
    private final Point[][][] pieceLayout = {
            // I-Piece 0
            {
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1)},
                    {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3)},
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1)},
                    {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3)}
            },
            // L-Piece 1
            {
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2)},
                    {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2)},
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0)},
                    {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0)}
            },
            // J-Piece 2
            {
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0)},
                    {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2)},
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2)},
                    {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0)}
            },
            // T-Piece 3
            {
                    {new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)},
                    {new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2)},
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2)},
                    {new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2)}
            },
            // O-Piece 4
            {
                    {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)},
                    {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)},
                    {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)},
                    {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)}
            },

            // S-Piece 5
            {
                    {new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1)},
                    {new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2)},
                    {new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1)},
                    {new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2)}
            },


            // Z-Piece 6
            {
                    {new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1)},
                    {new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2)},
                    {new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1)},
                    {new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2)}
            }
    };

    private final Color[] piecesColors = {
            Color.MAGENTA,//I-Piece
            Color.BLUE,// J-Piece
            Color.YELLOW,// L-Piece
            Color.GREEN, // O-Piece
            Color.WHITE,// S-Piece
            Color.ORANGE, // T-Piece
            Color.RED// Z-Piece
    };


    public Tetris(int fps, double speed, String sequence) {
        this.fps = fps;
        this.speed = speed;
        this.seq = sequence;
        timer = new Timer(1000 / this.fps, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dropDown(0);
            }
        });
        initBoard();

    }

    private void initBoard() {
        bgColor = new Color[WINDOW_WIDTH][WINDOW_LENGTH];
        for (int i = 0; i < WINDOW_WIDTH; i++) {
            for (int j = 0; j < WINDOW_LENGTH; j++) {
                if (i == 0 || i == WINDOW_WIDTH - 1 || j == WINDOW_LENGTH - 1) {
                    bgColor[i][j] = Color.GRAY;
                } else {
                    bgColor[i][j] = Color.BLACK;
                }
            }
        }
        initNextPieces();
        fireNew();
        timer.start();
    }

    private void initNextPieces() {
        for (int i = 0; i < seq.length(); i++) {
            char c = seq.charAt(i);
            if (c == 'I') {
                nextPieces.add(0);
            } else if (c == 'L') {
                nextPieces.add(1);
            } else if (c == 'J') {
                nextPieces.add(2);
            } else if (c == 'T') {
                nextPieces.add(3);
            } else if (c == 'O') {
                nextPieces.add(4);
            } else if (c == 'S') {
                nextPieces.add(5);
            } else if (c == 'Z') {
                nextPieces.add(6);
            }
        }
    }

    public void fireNew() {
        pieceOrigin = new Point(5, 1);
        rotation = 0;
        if (nextPieces.isEmpty()) {
            initNextPieces();
        }
        currentPiece = nextPieces.get(0);
        nextPieces.remove(0);
    }


    private boolean ifThereIsSomething(int x, int y, int rotation) {
        for (Point p : pieceLayout[currentPiece][rotation]) {
            if (bgColor[p.x + x][p.y + y] != Color.BLACK) {
                return true;
            }
        }
        return false;
    }

    public void rotate(int i) {
        int newRotation = (rotation + i) % 4;
        if (newRotation < 0) {
            newRotation = 3;
        }
        if (!ifThereIsSomething(pieceOrigin.x, pieceOrigin.y, newRotation)) {
            rotation = newRotation;
        }
        repaint();
    }


    public void move(int i) {
        if (!ifThereIsSomething(pieceOrigin.x + i, pieceOrigin.y, rotation)) {
            pieceOrigin.x += i;
        }
        repaint();
    }


    public void dropDown(int flag) {//0 means normal, 1 means dropnow
        if (!pause) {
            if (flag == 0) {
                if (!ifThereIsSomething(pieceOrigin.x, pieceOrigin.y + 1, rotation)) {
                    pieceOrigin.y += 1;
                } else {
                    fixToBgColor();
                }
            } else if (flag == 1) {
                while (!ifThereIsSomething(pieceOrigin.x, pieceOrigin.y + 1, rotation)) {
                    pieceOrigin.y += 1;
                }
                fixToBgColor();
            }
            repaint();
        }
    }


    public void fixToBgColor() {
        for (Point p : pieceLayout[currentPiece][rotation]) {
            bgColor[pieceOrigin.x + p.x][pieceOrigin.y + p.y] = piecesColors[currentPiece];
        }
        clearRows();
        ifMouseSelected = false;
        if (isGameOver()) {
            setVisible(false);

        } else {
            fireNew();
        }

    }

    private boolean isGameOver() {
        for (int i = 1; i < WINDOW_WIDTH - 1; i++) {
            if (bgColor[i][1] != Color.BLACK) {
                return true;
            }
        }
        return false;
    }

    public void deleteRow(int row) {
        for (int j = row - 1; j > 0; j--) {
            for (int i = 1; i < WINDOW_WIDTH - 1; i++) {
                bgColor[i][j + 1] = bgColor[i][j];
            }
        }
    }

    // Clear completed rows from the field and award score according to
    // the number of simultaneously cleared rows.
    public void clearRows() {
        boolean gap;
        int numClears = 0;

        for (int j = WINDOW_LENGTH - 2; j > 0; j--) {
            gap = false;
            for (int i = 1; i < WINDOW_WIDTH - 1; i++) {
                // found rows that are not filled
                if (bgColor[i][j] == Color.BLACK) {
                    gap = true;
                    break;
                }
            }

            if (!gap) {
                deleteRow(j);
                j += 1;//reset j, so it deletes multiple rows
                numClears += 1;
            }
        }
        if (numClears == 0) {
            //do nothing
        } else if (numClears == 1) {
            score += 10;
        } else if (numClears == 2) {
            score += 40;
        } else if (numClears == 3) {
            score += 90;
        } else if (numClears == 4) {
            score += 160;
        } else {
            score += 200;
        }
    }

    // Draw the falling block
    private void drawPiece(Graphics g) {
        g.setColor(piecesColors[currentPiece]);
        for (Point p : pieceLayout[currentPiece][rotation]) {
            g.fillRect((p.x + pieceOrigin.x) * REC_WIDTH,
                    (p.y + pieceOrigin.y) * REC_WIDTH,
                    REC_WIDTH, REC_LENGTH);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.fillRect(0, 0, REC_WIDTH * WINDOW_WIDTH, REC_WIDTH * WINDOW_LENGTH);
        for (int i = 0; i < WINDOW_WIDTH; i++) {
            for (int j = 0; j < WINDOW_LENGTH; j++) {
                g.setColor(bgColor[i][j]);
                if (i == 0 || i == WINDOW_WIDTH - 1 || j == WINDOW_LENGTH - 1) {
                    //boundary
                } else {
                    g.fillRect(REC_WIDTH * i, REC_WIDTH * j, REC_WIDTH, REC_LENGTH);
                }


            }
        }
        // Display the score
        g.setColor(Color.PINK);
        g.drawString("" + score, WINDOW_WIDTH / 2 * 40, REC_WIDTH);

        drawPiece(g);
    }


    public void setPause(Boolean pause) {
        this.pause = pause;
        if (pause == true) {
            timer.stop();
        } else {
            timer.start();
        }

    }

    public Boolean getPause() {
        return pause;
    }


    public void handleMousePressed(Point point) {
        if (!ifMouseSelected) {
            ifMouseSelected = true;
            return;
        }
        if (ifMouseSelected) {
            dropDown(1);
        }
    }

    public void handleMouseMoved(Point point) {
//        if (ifMouseSelected) {
//            move((int) point.getX() - pieceOrigin.x);
//        }
    }

    public void handleMouseWheelMoved(Point point) {
        if (ifMouseSelected) {
            rotate(-1);
        }

    }
}