import javax.swing.*;
import java.awt.*;

public class DirectionPanel {

    private JFrame directionFrame;

    private final JButton upButton;
    private final JButton downButton;
    private final JButton leftButton;
    private final JButton rightButton;

    public DirectionPanel() {
        this.upButton = new JButton("North");
        this.downButton = new JButton("South");
        this.leftButton = new JButton("West");
        this.rightButton = new JButton("East");
        this.directionFrame = new JFrame();
        directionFrame.setLayout(new GridLayout(3, 3));
        directionFrame.add(upButton, 1);
        directionFrame.add(downButton, 7);
        directionFrame.add(leftButton, 3);
        directionFrame.add(rightButton, 5);

    }
}
