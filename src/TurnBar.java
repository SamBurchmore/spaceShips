import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TurnBar {

    private final JPanel turnBar;

    private JLabel turnLabel;

    public TurnBar () throws IOException {
        turnLabel = new JLabel(new ImageIcon(ImageIO.read((new File("images\\noTurnBar.png").toURI()).toURL())));
        turnBar = new JPanel();
        turnBar.add(turnLabel);
    }

    public JPanel getTurnBar() {
        return turnBar;
    }
}
