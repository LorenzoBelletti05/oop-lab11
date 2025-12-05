package it.unibo.oop.reactivegui02;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unibo.oop.JFrameUtil;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;

/**
 * Second example of reactive GUI.
 */
public final class ConcurrentGUI extends JFrame {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentGUI.class);
    private final JLabel display = new JLabel("0");


    public ConcurrentGUI() {
        super();
        JFrameUtil.dimensionJFrame(this);
        //creatinng a panel -- adding the display
        final JPanel panel = new JPanel();
        panel.add(display);

        //creating the necessary botton and adding to the 
        //panel (workin default with the flow layoute)
        final JButton stop = new JButton("stop");
        final JButton up = new JButton("up");
        final JButton down = new JButton("down");
        panel.add(up);
        panel.add(down);
        panel.add(stop);

        /*
         * Create the counter agent and start it. This is actually not so good:
         * thread management should be left to
         * java.util.concurrent.ExecutorService
         */
        final Agent agent = new Agent();
        new Thread(agent).start();
        /*
         * Register a listener that stops it
         */
        stop.addActionListener(e -> { 
            agent.stopCounting(); 
            up.setEnabled(false); 
            down.setEnabled(false);
            stop.setEnabled(false); 
        });
        up.addActionListener(e -> agent.setUp());
        down.addActionListener(e -> agent.setDown());

        //adding the panel with all the data and set visible the frame
        this.getContentPane().add(panel);
        this.setVisible(true);

        
    }

    /*
     * The counter agent is implemented as a nested class. This makes it
     * invisible outside and encapsulated.
     */
    private final class Agent implements Runnable {
        
        private volatile boolean stop;
        private volatile boolean up = true;        
        private int counter;
        

        @Override
        public void run() {
            while (!this.stop) {
                try {
                    // The EDT doesn't access `counter` anymore, it doesn't need to be volatile
                    final var nextText = Integer.toString(this.counter);
                    SwingUtilities.invokeAndWait(() -> ConcurrentGUI.this.display.setText(nextText));
                    if(this.up) {
                        this.counter++;
                    } else {
                        this.counter--;
                    }                    
                    Thread.sleep(100);
                } catch (InvocationTargetException | InterruptedException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }

        /**
         * External command to stop counting.
         */
        public void stopCounting() {
            this.stop = true;
        }

        public void setUp() {
            this.up = true;
        }

        public void setDown() {
            this.up = false;
        }
    }


}
