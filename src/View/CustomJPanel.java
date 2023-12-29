
package src.View;
import javax.swing.*;
import java.awt.*;

class CustomJPanel extends JPanel {
    public CustomJPanel() {
        super();
        this.setBackground(Color.WHITE);
    }
    public CustomJPanel(BorderLayout layout) {
        super(layout);
        this.setBackground(Color.WHITE);
    }
    public CustomJPanel(FlowLayout layout) {
        super(layout);
        this.setBackground(Color.WHITE);
    }
    public CustomJPanel(GridLayout layout, int hgap, int vgap) {
        super(layout);
        this.setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}