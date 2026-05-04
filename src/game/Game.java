package game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Base canvas/window class for simple Java desktop games.
 *
 * This class handles window creation and double-buffered drawing so subclasses
 * can focus on game logic and rendering.
 */
abstract class Game extends Canvas {
    protected final int width;
    protected final int height;
    private Image buffer;

    public Game(String name, int width, int height) {
        this.width = width;
        this.height = height;

        Frame frame = new Frame(name);
        frame.add(this);
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        frame.pack();
        frame.setVisible(true);
        requestFocus();
    }

    @Override
    public abstract void paint(Graphics brush);

    @Override
    public void update(Graphics brush) {
        if (buffer == null) {
            buffer = createImage(width, height);
        }

        Graphics bufferGraphics = buffer.getGraphics();
        paint(bufferGraphics);
        brush.drawImage(buffer, 0, 0, this);
        bufferGraphics.dispose();
    }
}
