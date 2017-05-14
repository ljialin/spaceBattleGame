package tools;

import javax.swing.JFrame;
import java.awt.Component;
import java.awt.BorderLayout;

/**
 * Created by Jialin Liu on 04/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class JEasyFrame extends JFrame {
  public Component comp;
  public JEasyFrame(Component comp, String title) {
    super(title);
    this.comp = comp;
    getContentPane().add(BorderLayout.CENTER, comp);
    pack();
    this.setVisible(true);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    repaint();
  }
}

