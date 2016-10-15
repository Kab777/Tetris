package tetris;

import javax.swing.*;
import java.awt.event.*;

import static tetris.Tetris.*;

/**
 * Created by bwbecker on 2016-09-19.
 */
public class TetrisMain {

    public static void main(String[] args) {
        System.out.println("Hello, Tetris!");
        try {
            ProgramArgs a = ProgramArgs.parseArgs(args);
            Tetris tetris = new Tetris(a.getFPS(), a.getSpeed(), a.getSequence());

            //final Tetris tetris = new Tetris(1, 2, "ILJSZOT");
            JFrame f = new JFrame("Tetris");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize((WINDOW_WIDTH + 1) * REC_WIDTH, (WINDOW_LENGTH + 2) * REC_LENGTH);
            f.setVisible(true);
            f.setContentPane(tetris);


            f.addKeyListener(new KeyListener() {
                public void keyTyped(KeyEvent e) {
                }

                public void keyPressed(KeyEvent e) {
                    int code = e.getKeyCode();
                    if (code == KeyEvent.VK_UP || code == KeyEvent.VK_X || code == KeyEvent.VK_NUMPAD1
                            || code == KeyEvent.VK_NUMPAD5 || code == KeyEvent.VK_NUMPAD9) {
                        tetris.rotate(-1);
                    } else if (code == KeyEvent.VK_CONTROL || code == KeyEvent.VK_Z) {
                        tetris.rotate(+1);
                    } else if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_NUMPAD4) {
                        tetris.move(-1);
                    } else if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_NUMPAD6) {
                        tetris.move(+1);
                    } else if (code == KeyEvent.VK_P) {
                        if (tetris.getPause()) {
                            tetris.setPause(false);
                        } else {
                            tetris.setPause(true);
                        }
                    } else if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_NUMPAD8) {
                        tetris.dropDown(1);
                    }

                }

                public void keyReleased(KeyEvent e) {
                }
            });

            f.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    super.mouseMoved(e);
                    tetris.handleMouseMoved(e.getPoint());
                }
            });
            f.addMouseWheelListener(new MouseWheelListener() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    tetris.handleMouseWheelMoved(e.getPoint());
                }
            });
            f.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    tetris.handleMousePressed(e.getPoint());
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });



        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }

    }
}


