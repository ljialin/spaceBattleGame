package tools;

/**
 * Created by sml on 15/05/2017.
 */

import javax.swing.*;
import java.awt.*;
import java.util.Random;


/**
 * Created by sml on 15/05/2017.
 */
public class HeatMap extends JComponent {

    public static void main(String[] args) {
        Random rand = new Random();
        int len = 80;
        double[] pVec = new double[len];
        int cellsWide = 10;
        for (int i=0; i<len; i++) pVec[i] = rand.nextDouble();
        new JEasyFrame(new HeatMap(pVec, cellsWide), "HeatMap Test");
    }

    int width = 640;
    int height = 480;
    Dimension size = new Dimension(width, height);

    public Dimension getPreferredSize() {
        return size;
    }

    double[] pVec;
    int cellsWide;
    int cellsHigh;
    int gridSize;

    public HeatMap(double[] pVec, int cellsWide) {
        this.pVec = pVec;
        normalise(pVec);
        this.cellsWide = cellsWide;
        gridSize = width / cellsWide;
        cellsHigh = height / gridSize;
    }

    private void normalise(double[] v) {
        StatSummary ss = new StatSummary();
        ss.add(v);
        for (int i=0; i<v.length; i++) {
            v[i] /= ss.max();
        }
    }

    public void paintComponent(Graphics gx) {

        Graphics2D g = (Graphics2D) gx;
        g.setColor(Color.black);
        g.fillRect(0, 0, size.width, size.height);

        for (int i=0; i<cellsWide; i++) {
            for (int j=0; j<cellsHigh; j++) {
                int ix = i + j * cellsWide;
                float x = (float) pVec[ix];
                Color color = Color.getHSBColor(x, x, x);
                // color = new Color(x, x, (1-x));
                g.setColor(color);
                g.fillRect(i * gridSize, j * gridSize, gridSize, gridSize);
            }
        }
    }
}


