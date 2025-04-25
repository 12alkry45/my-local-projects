import javax.swing.*;
import java.awt.*;
public class CanvasPanel extends JPanel {
    private int count = 10;
    private int low = 0;
    private int up = 750;
    private Polygon trap[] = new Polygon[count];
    public final int size = 750;
    private boolean isFill = false;
    CanvasPanel(){
        createNewTrap();
    }
    public void createNewTrap(){
        int numx = low, numy = size, mod = (up - low) / count;
        trap = new Polygon[count];
        for(int i = 0; i < count; i++) {
            trap[i] = new Polygon(new int[]{numx, numx, numx + mod, numx + mod, numx}, new int[]{numy, numy - numx, numy - numx - mod, numy, numy}, 4);
            numx += mod;
        }
    }
    public void updateParam(int low, int up, int num){
        this.low = low;
        this.up = up;
        this.count = num;
        createNewTrap();
        repaint();
    }
    public void updateFill(boolean fill){
        this.isFill = fill;
        repaint();
    }
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for(int i = 0; i < count; i++) {
            if (i % 2 == 0 && isFill) g2d.fillPolygon(trap[i]);
            else g2d.drawPolygon(trap[i]);
        }
    }
}
