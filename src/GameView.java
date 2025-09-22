import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameView {

    private JFrame gameFrame;

    private JPanel boardPanel;

    private BufferedImage boardImage;

    private final int size = 600;

    private final SidePanel directionButtons;




    public GameView() throws IOException {
        gameFrame = new JFrame();
        directionButtons = new SidePanel();
        gameFrame.setName("FlatLand 1.0a");
        gameFrame.getContentPane().setLayout(new FlowLayout(FlowLayout.LEADING));
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setIconImage(ImageIO.read((new File("images\\battleship.png").toURI()).toURL()));
        gameFrame.setLayout(new GridLayout());
        gameFrame.setResizable(true);
        gameFrame.setLocationRelativeTo(null);

        boardPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                int x = (size - boardImage.getWidth(null)) / 2;
                int y = (size - boardImage.getHeight(null)) / 2;
                g.drawImage(boardImage, x, y, this);
            }
        };
        gameFrame.add(boardPanel);
        gameFrame.add(directionButtons.getSidePanel());
        boardPanel.setPreferredSize(new Dimension(size, size));
        gameFrame.pack();

    }

    public JFrame getGameFrame() {
        return gameFrame;
    }

    public void setGameFrame(JFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public void updateBoardImage(BufferedImage boardImage) {
        this.boardImage = boardImage;
    }

    public void updateBoard(BufferedImage worldImage) {
        updateBoardImage(worldImage);
        boardPanel.repaint();
    }

    public void setVisible(boolean visible) {
        gameFrame.setVisible(visible);
    }

    public SidePanel getSidePanel() {
        return directionButtons;
    }


}
