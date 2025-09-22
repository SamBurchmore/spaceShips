import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Random;

public class Board {

    private Tile[] tiles = new Tile[]{};
    
    private final int size = 12;


    public Board() throws IOException {
        Color currentColor = Color.LIGHT_GRAY;
        this.tiles = new Tile[size * size];
        int x = 0;
        int y = 0;
        for (int i = 0; i < size * size; i++) {
            this.tiles[i] = new Tile(x, y);
            this.tiles[i].setColor(currentColor);
            if (currentColor == Color.LIGHT_GRAY) {
                currentColor = Color.DARK_GRAY;
            }
            else {
                currentColor = Color.LIGHT_GRAY;
            }
            x++;
            if (x == size) {
                x = 0;
                y++;
                if (currentColor == Color.LIGHT_GRAY) {
                    currentColor = Color.DARK_GRAY;
                }
                else {
                    currentColor = Color.LIGHT_GRAY;
                }
            }
        }
    }

    public Tile getTile(int x, int y) {
        return tiles[y * size + x];
    }

    public void setTile(int x, int y, Piece piece) {
        tiles[y * size + x].setPiece(piece);
    }

    public int getSize() {
        return size;
    }
}
