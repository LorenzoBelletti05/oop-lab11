package it.unibo.oop.reactivegui02;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.unibo.oop.JFrameUtil;

import java.io.Serial;

/**
 * Second example of reactive GUI.
 */
public final class ConcurrentGUI extends JFrame {

    @Serial
    private static final long serialVersionUID = 1L;
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

        //adding the panel with all the data and set visible the frame
        this.getContentPane().add(panel);
        this.setVisible(true);

        
    }
}
