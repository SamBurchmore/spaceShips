import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BoardController {

    private Board board;

    private int scale = 50;

    private final int size = 12;


    public BoardController() throws IOException {
        board = new Board();
        setUpBoard();
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Piece pieceFactory(PieceType pieceType, Team team, int x, int y) {
        switch (pieceType) {
            case CORVETTE -> {
                return new Corvette(team, new int[]{x, y});
            }
            case FRIGATE -> {
                return new Frigate(team, new int[]{x, y});
            }
            case DESTROYER -> {
                return new Destroyer(team, new int[]{x, y});
            }
            case CRUISER -> {
                return new Cruiser(team, new int[]{x, y});
            }
            case BATTLESHIP -> {
                return new Battleship(team, new int[]{x, y});
            }
            case CARRIER -> {
                return new Carrier(team, new int[]{x, y});
            }
        }
        return null;
    }

    public void setUpBoard() {
        board.setTile(2, 1, pieceFactory(PieceType.FRIGATE, Team.BLACK, 2, 1));
        board.setTile(3, 1, pieceFactory(PieceType.FRIGATE, Team.BLACK, 3, 1));
        board.setTile(4, 1, pieceFactory(PieceType.CORVETTE, Team.BLACK, 4, 1));
        board.setTile(5, 1, pieceFactory(PieceType.CORVETTE, Team.BLACK, 5, 1));
        board.setTile(6, 1, pieceFactory(PieceType.CORVETTE, Team.BLACK, 6, 1));
        board.setTile(7, 1, pieceFactory(PieceType.CORVETTE, Team.BLACK, 7, 1));
        board.setTile(8, 1, pieceFactory(PieceType.FRIGATE, Team.BLACK, 8, 1));
        board.setTile(9, 1, pieceFactory(PieceType.FRIGATE, Team.BLACK, 9, 1));
        board.setTile(3, 0, pieceFactory(PieceType.CRUISER, Team.BLACK, 3, 0));
        board.setTile(4, 0, pieceFactory(PieceType.DESTROYER, Team.BLACK, 4, 0));
        board.setTile(5, 0, pieceFactory(PieceType.BATTLESHIP, Team.BLACK, 5, 0));
        board.setTile(6, 0, pieceFactory(PieceType.CARRIER, Team.BLACK, 6, 0));
        board.setTile(7, 0, pieceFactory(PieceType.DESTROYER, Team.BLACK, 7, 0));
        board.setTile(8, 0, pieceFactory(PieceType.CRUISER, Team.BLACK, 8, 0));


        board.setTile(2, 10, pieceFactory(PieceType.FRIGATE, Team.WHITE, 2, 10));
        board.setTile(3, 10, pieceFactory(PieceType.FRIGATE, Team.WHITE, 3, 10));
        board.setTile(4, 10, pieceFactory(PieceType.CORVETTE, Team.WHITE, 4, 10));
        board.setTile(5, 10, pieceFactory(PieceType.CORVETTE, Team.WHITE, 5, 10));
        board.setTile(6, 10, pieceFactory(PieceType.CORVETTE, Team.WHITE, 6, 10));
        board.setTile(7, 10, pieceFactory(PieceType.CORVETTE, Team.WHITE, 7, 10));
        board.setTile(8, 10, pieceFactory(PieceType.FRIGATE, Team.WHITE, 8, 10));
        board.setTile(9, 10, pieceFactory(PieceType.FRIGATE, Team.WHITE, 9, 10));
        board.setTile(3, 11, pieceFactory(PieceType.CRUISER, Team.WHITE, 3, 11));
        board.setTile(4, 11, pieceFactory(PieceType.DESTROYER, Team.WHITE, 4, 11));
        board.setTile(5, 11, pieceFactory(PieceType.BATTLESHIP, Team.WHITE, 5, 11));
        board.setTile(6, 11, pieceFactory(PieceType.CARRIER, Team.WHITE, 6, 11));
        board.setTile(7, 11, pieceFactory(PieceType.DESTROYER, Team.WHITE, 7, 11));
        board.setTile(8, 11, pieceFactory(PieceType.CRUISER, Team.WHITE, 8, 11));
    }



    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getSize() {
        return size;
    }

    public void move(Piece piece, int x, int y) {
        int[] oldLocation = piece.getLocation();
        board.setTile(oldLocation[0], oldLocation[1], null);
        board.setTile(x, y, piece);
        piece.setLocation(new int[]{x, y});
    }

    public ArrayList<Object> attack(Piece attacker, Piece defender) {
        if (attacker.getDirection() != null) {
            int x = defender.getLocation()[0];
            int y = defender.getLocation()[1];
            int x1 = attacker.getLocation()[0];
            int y1 = attacker.getLocation()[1];
            if(x > x1) {
                attacker.setDirection(Direction.EAST);
            }
            if (x < x1) {
                attacker.setDirection(Direction.WEST);
            }
            if(y > y1) {
                attacker.setDirection(Direction.SOUTH);
            }
            if (y < y1) {
                attacker.setDirection(Direction.NORTH);
            }
        }
        Random random = new Random();
        int attackScore = 1 + random.nextInt(attacker.getAttack());
        if (attacker.getType() == PieceType.FRIGATE) {
            attackScore = 1 + random.nextInt(Math.max(defender.getArmour(), 4));
        }
        ArrayList<Object> result = new ArrayList<>();
        int armour = defender.getArmour();
        if (defender.getDamaged()) {
            armour = armour/2;
        }
        System.out.println("attack score: " + attackScore + " armour: " + armour);
        if (attackScore >= armour) {
            board.setTile(defender.getLocation()[0], defender.getLocation()[1], null);
            result.add(defender);
            result.add(attackScore);
            result.add(true);
            result.add(false);
            return result;
        }
        if (!defender.getDamaged()) {
            if (attacker.getType() == PieceType.FRIGATE || attacker.getType() == PieceType.CRUISER) {
                if (attackScore >= defender.getArmour() / 2) {
                    defender.setDamaged(true);
                    result.add(defender);
                    result.add(attackScore);
                    result.add(true);
                    result.add(true);
                    return result;
                }
            }
        }
        result.add(defender);
        result.add(attackScore);
        result.add(false);
        result.add(false);
        return result;
    }
    

}
