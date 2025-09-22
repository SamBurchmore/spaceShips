import java.awt.*;

public class Tile {

    private Piece piece = null;

    private int x;

    private int y;

    private Color color;

    public Tile(int x, int y) {
        setLocation(x, y);
    }

    public Tile(Piece piece) {
        setLocation(x, y);
        setPiece(piece);
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int[] getLocation() {
        return new int[]{x,y};
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
