import javax.swing.*;
import java.awt.*;

public class SidePanel {

    private JPanel sidePanel;

    private final JButton upButton;
    private final JButton downButton;
    private final JButton leftButton;
    private final JButton rightButton;

    private JPanel turnPanel;
    private JLabel turnLabel;

    private final InfoPanel infoPanel;


    public SidePanel() {
        turnPanel = new JPanel();
        infoPanel = new InfoPanel();
        JPanel blankPanel0 = new JPanel();
        JPanel blankPanel1 = new JPanel();
        JPanel blankPanel2 = new JPanel();
        JPanel blankPanel3 = new JPanel();
        Color color = new Color(10, 10, 60);
        blankPanel0.setBackground(color);
        blankPanel1.setBackground(color);
        blankPanel2.setBackground(color);
        blankPanel3.setBackground(color);
        this.upButton = new JButton("North");
        this.downButton = new JButton("South");
        this.leftButton = new JButton("West");
        this.rightButton = new JButton("East");
        this.sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(2, 1));
        sidePanel.add(infoPanel.getInfoPanel());
        sidePanel.add(turnPanel);

        sidePanel.setPreferredSize(new Dimension(300, 300));
    }

    public JPanel getSidePanel() {
        return sidePanel;
    }

    public JButton getUpButton() {
        return upButton;
    }

    public JButton getDownButton() {
        return downButton;
    }

    public JButton getLeftButton() {
        return leftButton;
    }

    public JButton getRightButton() {
        return rightButton;
    }

    public void changeTurnDisplay(Team team) {
        if (team == Team.BLACK) {
            turnPanel.setBackground(Color.BLACK);
        }
        else {
            turnPanel.setBackground(Color.WHITE);
        }
    }

    public InfoPanel getInfoPanel() {
        return infoPanel;
    }
}
