import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MainController {
    GameView gameView = new GameView();
    BoardController boardController = new BoardController();
    Tile lastClickedTile;
    Piece activePiece;
    Team activeTeam;
    HashMap<int[], int[]> pointMap = new HashMap<>();
    private HashMap<SpriteTags, BufferedImage> sprites = new HashMap<>();
    private final BufferedImage blankBoardImage;
    private BufferedImage lastBoardImage;
    private BoardView boardView;
    private GameLogic gameLogic;

    private ArrayList<Tile> moveTiles;
    private ArrayList<Tile> attackTiles;

    public GameState gameState;

    public int[] attackResult;

    private BoardListener boardListener;

    private Color lightMoveColor = new Color(122, 122, 222);
    private Color darkMoveColor = new Color(14, 14, 94);
    private Color lightAttackColor = new Color(150, 0, 0);
    private Color darkAttackColor = new Color(200, 0, 0);
    private Color lightDamagedColor = new Color(200, 200, 0);
    private Color darkDamagedColor = new Color(150, 150, 0);



    public MainController() throws IOException {
        boardView = new BoardView();
        gameLogic = new GameLogic();
        boardListener = new BoardListener();
        moveTiles = new ArrayList<>();
        attackTiles = new ArrayList<>();
        boardView.loadSprites();
        blankBoardImage = boardView.scaledImage();
        lastBoardImage = BoardView.deepCopy(blankBoardImage);
        gameView.updateBoard(boardView.getBoardImage());
        gameView.setVisible(true);
        gameView.getGameFrame().addMouseListener(boardListener);
        gameView.getSidePanel().getUpButton().addActionListener(e -> gameLogic.setNorth());
        gameView.getSidePanel().getDownButton().addActionListener(e -> gameLogic.setSouth());
        gameView.getSidePanel().getRightButton().addActionListener(e -> gameLogic.setEast());
        gameView.getSidePanel().getLeftButton().addActionListener(e -> gameLogic.setWest());
        activeTeam = Team.BLACK;
        gameView.getSidePanel().changeTurnDisplay(activeTeam);
    }

    public void setLastClickedTile(int x, int y) {
        lastClickedTile = boardController.getBoard().getTile((x-7) / boardController.getScale(), (y-30) / boardController.getScale());
        //System.out.println((x-7) / boardController.getScale() + " " + (y-30) / boardController.getScale());
    }

    public boolean setActivePiece() {
        if (lastClickedTile.getPiece() != null) {
            if (lastClickedTile.getPiece().getTeam() == activeTeam) {
                activePiece = lastClickedTile.getPiece();
                //System.out.println(activePiece.getType());
                //System.out.println(Arrays.toString(activePiece.getLocation()));
                gameView.getSidePanel().getInfoPanel().setInfoPanelText(activePiece);
                return true;
            }
        }
        return false;
    }

    public void newTurn() {
        if (activeTeam == Team.BLACK) {
            activeTeam = Team.WHITE;
        }
        else {
            activeTeam = Team.BLACK;
        }
        activePiece = null;
        gameState = GameState.WAITING_INPUT;
        gameView.getSidePanel().getInfoPanel().clearInfoPanelText();
        boardView.updateBoard();
    }

    public void resetTurn() {
        activePiece = null;
        gameState = GameState.WAITING_INPUT;
        gameView.getSidePanel().getInfoPanel().clearInfoPanelText();
        boardView.updateBoard();
    }

    public void startRotate() {
        gameState = GameState.PIECE_TURNING;
        gameLogic.getActivePieceActions(); // Retrieve the active pieces potential actions
        boardView.displayActions(); // Display the active pieces potential actions
    }

    public void runGame() {
        gameView.getSidePanel().getInfoPanel().clearInfoPanelText();
        System.out.println(gameState);
        boardListener.getPointer(); // Get the mouse location
        if (gameState == GameState.PIECE_TURNING) {
            boolean actionPerformed = gameLogic.performAction();
            if (actionPerformed) {
                gameState = GameState.WAITING_INPUT;
                newTurn();
            }
        }
        else {
            if (gameState == GameState.WAITING_INPUT) {
                boolean pieceSelected = setActivePiece(); // Try to set the active piece
                if (pieceSelected) {
                    gameState = GameState.PIECE_SELECTED;
                    gameLogic.getActivePieceActions(); // Retrieve the active pieces potential actions
                    boardView.displayActions(); // Display the active pieces potential actions
                }
            } else if (lastClickedTile.getPiece() != null) {
                if (activePiece == lastClickedTile.getPiece()) {
                    resetTurn();
                } else if (lastClickedTile.getPiece().getTeam() == activeTeam) {
                    resetTurn();
                }
            }
            if (gameState == GameState.PIECE_SELECTED) {
                boolean actionPerformed = gameLogic.performAction();
                if (actionPerformed) {
                    System.out.println("action performed");
                    newTurn();
                    gameView.getSidePanel().changeTurnDisplay(activeTeam);
                }
            }
            System.out.println(gameState);
        }
    }

    public class BoardListener implements java.awt.event.MouseListener {


        public BoardListener() {
            gameState = GameState.WAITING_INPUT;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            runGame();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        public void getPointer() {
            Point point = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(point, gameView.getGameFrame());
            setLastClickedTile(point.x, point.y);
        }

    }

    public class BoardView {

        public void displayActions() {
            lastBoardImage = BoardView.deepCopy(blankBoardImage);
            gameView.updateBoard(boardView.getBoardImage());
            boardView.displayMovementRange(moveTiles);
            boardView.displayAttackRange(attackTiles);
            gameView.updateBoard(boardView.getBoardImage());
        }

        public void updateBoard() {
            lastBoardImage = BoardView.deepCopy(blankBoardImage);
            gameView.updateBoard(boardView.getBoardImage());
        }
        public BufferedImage getBoardImage() {
            Random random = new Random();
            int size = boardController.getSize();
            int scale = boardController.getScale();
            for (int x = 0; x <= scale * size; x += scale) {
                for (int y = 0; y <= scale * size; y += scale) {
                    if (((x < scale * size) && (y < scale * size)) && ((x >= 0) && (y >= 0))) {
                        if (boardController.getBoard().getTile(x / scale, y / scale).getPiece() != null) {
                            for (int i = 0; i < scale; i++) {
                                for (int j = 0; j < scale; j++) {
                                    if (((x + i < scale * size) && (y + j < scale * size)) && ((x + i >= 0) && (y + j >= 0))) {
                                        Tile tile = boardController.getBoard().getTile(x / scale, y / scale);
                                        Color rgba = new Color(sprites.get(getPieceSpriteTag(tile.getPiece())).getRGB(i, j), true);
                                        if (rgba.getAlpha() == 255) {
                                            if (tile.getPiece().getDamaged()) {
                                                if (i % 3 == 0 || j % 2 == 0) {
                                                    lastBoardImage.setRGB(x + i, y + j, rgba.getRGB());
                                                }
                                            }
                                            else {
                                                lastBoardImage.setRGB(x + i, y + j, rgba.getRGB());

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return lastBoardImage;
        }

        public BufferedImage displayMovementRange(ArrayList<Tile> tiles) {
            int size = boardController.getSize();
            int scale = boardController.getScale();
            for (Tile tile : tiles) {
                int x = tile.getLocation()[0];
                int y = tile.getLocation()[1];
//                if (tile.getPiece() != null) {
//                    for (int i = 0; i < scale; i++) {
//                        for (int j = 0; j < scale; j++) {
//                            if (((x + i < scale * size) && (y + j < scale * size)) && ((x + i >= 0) && (y + j >= 0))) {
//                                Color rgba = new Color(sprites.get(getPieceSpriteTag(tile.getPiece())).getRGB(i, j), true);
//                                if (rgba.getAlpha() != 255) {
//                                    if (tile.getColor() == Color.darkGray) {
//                                        lastBoardImage.setRGB((x * scale) + i, (y * scale) + j, darkMoveColor.getRGB());
//                                    }
//                                    else {
//                                        lastBoardImage.setRGB((x * scale) + i, (y * scale) + j, lightMoveColor.getRGB());
//                                    }
//                                }
//
//                            }
//                        }
//                    }
//                }
                    for (int i = 0; i < scale; i++) {
                        for (int j = 0; j < scale; j++) {
                            if (((x + i < scale * size) && (y + j < scale * size)) && ((x + i >= 0) && (y + j >= 0))) {
                                if (tile.getColor() == Color.darkGray) {
                                    lastBoardImage.setRGB((x * scale) + i, (y * scale) + j, darkMoveColor.getRGB());
                                }
                                else {
                                    lastBoardImage.setRGB((x * scale) + i, (y * scale) + j, lightMoveColor.getRGB());
                                }
                            }
                        }
                    }
                }
            return lastBoardImage;
        }

        public BufferedImage displayAttackRange(ArrayList<Tile> tiles) {
            int size = boardController.getSize();
            int scale = boardController.getScale();
            for (Tile tile : tiles) {
                int x = tile.getLocation()[0];
                int y = tile.getLocation()[1];
                if (tile.getPiece() != null) {
                    for (int i = 0; i < scale; i++) {
                        for (int j = 0; j < scale; j++) {
                            if (((x + i < scale * size) && (y + j < scale * size)) && ((x + i >= 0) && (y + j >= 0))) {
                                Color rgba = new Color(sprites.get(getPieceSpriteTag(tile.getPiece())).getRGB(i, j), true);
                                if (rgba.getAlpha() != 255) {
                                    //lastBoardImage.setRGB((x * scale) + i, (y * scale) + j, rgba.getRGB());
                                    if (tile.getColor() == Color.darkGray) {
                                        lastBoardImage.setRGB((x * scale) + i, (y * scale) + j, darkAttackColor.getRGB());
                                    }
                                    else {
                                        lastBoardImage.setRGB((x * scale) + i, (y * scale) + j, lightAttackColor.getRGB());
                                    }
                                }

                            }
                        }
                    }
                }
            }
            return lastBoardImage;
        }

        public BufferedImage scaledImage() {
            int size = boardController.getSize();
            int scale = boardController.getScale();
            BufferedImage boardImage = new BufferedImage(size * scale, size * scale, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x <= scale * size; x += scale) {
                for (int y = 0; y <= scale * size; y += scale) {
                    for (int i = 0; i < scale; i++) {
                        for (int j = 0; j < scale; j++) {
                            if (((x + i < scale * size) && (y + j < scale * size)) && ((x + i >= 0) && (y + j >= 0))) {
                                boardImage.setRGB(x + i, y + j, boardController.getBoard().getTile(x / scale, y / scale).getColor().getRGB());
                            }
                        }
                    }
                }
            }
            return boardImage;
        }

        public void loadSprites() throws IOException {
            for (SpriteTags spriteTag: SpriteTags.values()) {
                sprites.put(spriteTag, ImageIO.read((new File(spritePathMap(spriteTag)).toURI()).toURL()));
            }

        }

        public String getSpritePath(SpriteTags spriteTag, Piece piece) {
            if (piece.getDirection() != null) {
                return getDirectionSpritePath(spritePathMap(spriteTag), piece.getDirection());
            }
            return spritePathMap(spriteTag);
        }

        public String getDirectionSpritePath(String path, Direction direction) {
            String fileName = path.substring(0, path.length() - 4);
            System.out.println(fileName);
            switch (direction) {
                case NORTH -> {
                    return fileName + "_north.png";
                }
                case EAST -> {
                    return fileName + "_east.png";
                }
                case SOUTH -> {
                    return fileName + "_south.png";
                }
                case WEST -> {
                    return fileName + "_west.png";
                }
            }
            return path;
        }

        public String spritePathMap(SpriteTags spriteTag) {
            switch (spriteTag) {
                case WHITECORVETTE -> {
                    return "images\\corvette_white.png";
                }
                case WHITEFRIGATENORTH -> {
                    return "images\\frigate_white_north.png";
                }
                case WHITEFRIGATEEAST -> {
                    return "images\\frigate_white_east.png";
                }
                case WHITEFRIGATESOUTH -> {
                    return "images\\frigate_white_south.png";
                }
                case WHITEFRIGATEWEST -> {
                    return "images\\frigate_white_west.png";
                }
                case WHITEDESTROYER -> {
                    return "images\\destroyer_white.png";
                }
                case WHITECRUISERNORTH -> {
                    return "images\\cruiser_white_north.png";
                }
                case WHITECRUISEREAST -> {
                    return "images\\cruiser_white_east.png";
                }
                case WHITECRUISERSOUTH -> {
                    return "images\\cruiser_white_south.png";
                }
                case WHITECRUISERWEST -> {
                    return "images\\cruiser_white_west.png";
                }
                case WHITEBATTLESHIP -> {
                    return "images\\battleship_white.png";
                }
                case WHITECARRIER -> {
                    return "images\\carrier_white.png";
                }
                case BLACKCORVETTE -> {
                    return "images\\corvette_black.png";
                }
                case BLACKFRIGATENORTH -> {
                    return "images\\frigate_black_north.png";
                }
                case BLACKFRIGATEEAST -> {
                    return "images\\frigate_black_east.png";
                }
                case BLACKFRIGATESOUTH -> {
                    return "images\\frigate_black_south.png";
                }
                case BLACKFRIGATEWEST -> {
                    return "images\\frigate_black_west.png";
                }
                case BLACKDESTROYER -> {
                    return "images\\destroyer_black.png";
                }
                case BLACKCRUISERNORTH -> {
                    return "images\\cruiser_black_north.png";
                }
                case BLACKCRUISEREAST -> {
                    return "images\\cruiser_black_east.png";
                }
                case BLACKCRUISERSOUTH -> {
                    return "images\\cruiser_black_south.png";
                }
                case BLACKCRUISERWEST -> {
                    return "images\\cruiser_black_west.png";
                }
                case BLACKBATTLESHIP -> {
                    return "images\\battleship_black.png";
                }
                case BLACKCARRIER -> {
                    return "images\\carrier_black.png";
                }
            }
            return null;
        }

        public SpriteTags getPieceSpriteTag(Piece piece) {
            if (piece.getTeam() == Team.BLACK) {
                switch (piece.getType()) {
                    case CORVETTE -> {
                        return SpriteTags.BLACKCORVETTE;
                    }
                    case FRIGATE -> {
                        switch (piece.getDirection()) {
                            case NORTH -> {
                                return SpriteTags.BLACKFRIGATENORTH;

                            }
                            case EAST -> {
                                return SpriteTags.BLACKFRIGATEEAST;

                            }
                            case SOUTH -> {
                                return SpriteTags.BLACKFRIGATESOUTH;

                            }
                            case WEST -> {
                                return SpriteTags.BLACKFRIGATEWEST;
                            }
                        }
                    }
                    case DESTROYER -> {
                        return SpriteTags.BLACKDESTROYER;
                    }
                    case CRUISER -> {
                        switch (piece.getDirection()) {
                            case NORTH -> {
                                return SpriteTags.BLACKCRUISERNORTH;

                            }
                            case EAST -> {
                                return SpriteTags.BLACKCRUISEREAST;

                            }
                            case SOUTH -> {
                                return SpriteTags.BLACKCRUISERSOUTH;

                            }
                            case WEST -> {
                                return SpriteTags.BLACKCRUISERWEST;
                            }
                        }
                    }
                    case BATTLESHIP -> {
                        return SpriteTags.BLACKBATTLESHIP;
                    }
                    case CARRIER -> {
                        return SpriteTags.BLACKCARRIER;
                    }
                }
            }
            else {
                switch (piece.getType()) {
                    case CORVETTE -> {
                        return SpriteTags.WHITECORVETTE;
                    }
                    case FRIGATE -> {
                        switch (piece.getDirection()) {
                            case NORTH -> {
                                return SpriteTags.WHITEFRIGATENORTH;

                            }
                            case EAST -> {
                                return SpriteTags.WHITEFRIGATEEAST;

                            }
                            case SOUTH -> {
                                return SpriteTags.WHITEFRIGATESOUTH;

                            }
                            case WEST -> {
                                return SpriteTags.WHITEFRIGATEWEST;
                            }
                        }
                    }
                    case DESTROYER -> {
                        return SpriteTags.WHITEDESTROYER;
                    }
                    case CRUISER -> {
                        switch (piece.getDirection()) {
                            case NORTH -> {
                                return SpriteTags.WHITECRUISERNORTH;

                            }
                            case EAST -> {
                                return SpriteTags.WHITECRUISEREAST;

                            }
                            case SOUTH -> {
                                return SpriteTags.WHITECRUISERSOUTH;

                            }
                            case WEST -> {
                                return SpriteTags.WHITECRUISERWEST;
                            }
                        }
                    }
                    case BATTLESHIP -> {
                        return SpriteTags.WHITEBATTLESHIP;
                    }
                    case CARRIER -> {
                        return SpriteTags.WHITECARRIER;
                    }
                }
            }
            return null;
        }

        static BufferedImage deepCopy(BufferedImage bufferedImage) {
            ColorModel cm = bufferedImage.getColorModel();
            boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
            WritableRaster raster = bufferedImage.copyData(null);
            return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        }
    }

    public class GameLogic {

        public void move(Piece piece, int x, int y) {
            if (piece.getDirection() != null) {
                int x1 = piece.getLocation()[0];
                int y1 = piece.getLocation()[1];
                if(x > x1) {
                    piece.setDirection(Direction.EAST);
                }
                if (x < x1) {
                    piece.setDirection(Direction.WEST);
                }
                if(y > y1) {
                    piece.setDirection(Direction.SOUTH);
                }
                if (y < y1) {
                    piece.setDirection(Direction.NORTH);
                }
            }
            boardController.move(piece, x, y);
        }

        public boolean performAction() {
            for (Tile tile : moveTiles) {
                if (lastClickedTile.getLocation()[0] == tile.getLocation()[0] && lastClickedTile.getLocation()[1] == tile.getLocation()[1]) {
                    move(activePiece, lastClickedTile.getLocation()[0], lastClickedTile.getLocation()[1]);
                    return true;
                }
            }
            for (Tile tile : attackTiles) {
                if (lastClickedTile.getLocation()[0] == tile.getLocation()[0] && lastClickedTile.getLocation()[1] == tile.getLocation()[1]) {
                    if (tile.getPiece().getTeam() != activePiece.getTeam()) {
                            ArrayList<Object> result = boardController.attack(activePiece, tile.getPiece());
                        gameView.getSidePanel().getInfoPanel().setAttackResult(activePiece, (Piece) result.get(0), (int) result.get(1), (boolean) result.get(2), (boolean) result.get(3));

                    }
                    else {
                        move(activePiece, lastClickedTile.getLocation()[0], lastClickedTile.getLocation()[1]);
                    }
                    return true;
                }
            }
            return false;
        }

        public void getActivePieceActions() {
            moveTiles = gameLogic.getMoveTiles(activePiece.getLocation(), activePiece.getMovementRange(), activePiece.getDirection());
            //attackTiles = gameLogic.getAttackTilesInSquareRange(activePiece.getLocation(), activePiece.getAttackRange(), activePiece.getTeam());
            attackTiles = gameLogic.getAttackTiles(activePiece.getLocation(), activePiece.getAttackRange(), activePiece.getDirection(), activePiece.getTeam());
        }

        public void setNorth() {
            if (gameState == GameState.PIECE_SELECTED) {
                activePiece.setDirection(Direction.NORTH);
                startRotate();
            }
        }
        public void setSouth() {
            if (gameState == GameState.PIECE_SELECTED) {
                activePiece.setDirection(Direction.SOUTH);
                startRotate();
            }
        }
        public void setEast() {
            if (gameState == GameState.PIECE_SELECTED) {
                activePiece.setDirection(Direction.EAST);
                startRotate();
            }
        }
        public void setWest() {
            if (gameState == GameState.PIECE_SELECTED) {
                activePiece.setDirection(Direction.WEST);
                startRotate();
            }
        }



        public ArrayList<Tile> getMoveTiles(int[] location, int range, Direction direction) {
            if (direction != null) {
                //return getTilesInLineRange(location, range, direction);
                return getTilesInLineRangeAlpha(location, range, direction);
            }
            else {
                return getTilesInSquareRange(location, range);
            }
        }

        public ArrayList<Tile> getAttackTiles(int[] location, int range, Direction direction, Team team) {
            if (direction != null) {
                return getAttackTilesInLineRangeAlpha(location, range, direction);
            }
            else {
                return getAttackTilesInSquareRange(location, range, team);
            }
        }

        public ArrayList<Tile> getTilesInLineRange(int[] location, int range, Direction direction) {
            int x = location[0];
            int y = location[1];
            ArrayList<Tile> tiles = new ArrayList<>();
            switch (direction) {
                case NORTH -> {
                    for (int i = 0; i > -range; i--) {
                        y = y - 1;
                        if (((x < boardController.getSize())
                                && (y < boardController.getSize()))
                                && ((x >= 0) && (y >= 0))) {
                            if (boardController.getBoard().getTile(x, y).getPiece() == null) {
                                tiles.add(boardController.getBoard().getTile(x, y));
                            }
                            else if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                                return tiles;
                            }
                        }
                    }
                }
                case EAST -> {
                    for (int i = 0; i < range; i++) {
                        x = x + 1;
                        if (((x < boardController.getSize())
                                && (y < boardController.getSize()))
                                && ((x >= 0) && (y >= 0))) {
                            if (boardController.getBoard().getTile(x, y).getPiece() == null) {
                                tiles.add(boardController.getBoard().getTile(x, y));
                            }
                            else if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                                return tiles;
                            }
                        }
                    }
                }
                case SOUTH -> {
                    for (int i = 0; i < range; i++) {
                        y = y + 1;
                        if (((x < boardController.getSize())
                                && (y < boardController.getSize()))
                                && ((x >= 0) && (y >= 0))) {
                            if (boardController.getBoard().getTile(x, y).getPiece() == null) {
                                tiles.add(boardController.getBoard().getTile(x, y));
                            }
                            else if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                                return tiles;
                            }
                        }
                    }
                }
                case WEST -> {
                    for (int i = 0; i > -range; i--) {
                        x = x - 1;
                        if (((x < boardController.getSize())
                                && (y < boardController.getSize()))
                                && ((x >= 0) && (y >= 0))) {
                            if (boardController.getBoard().getTile(x, y).getPiece() == null) {
                                tiles.add(boardController.getBoard().getTile(x, y));
                            }
                            else if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                                return tiles;
                            }
                        }
                    }
                }
            }
            return tiles;
        }

        public ArrayList<Tile> getTilesInLineRangeAlpha(int[] location, int range, Direction direction) {
            int x = location[0];
            int y = location[1];
            ArrayList<Tile> tiles = new ArrayList<>();
            Direction lineDirection = Direction.NORTH;
            for (int i = 0; i > -range; i--) {//north
                y = y - 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece() == null) {
                        tiles.add(boardController.getBoard().getTile(x, y));
                    }
                    else if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                        break;
                    }
                }
            }
            x = location[0];
            y = location[1];
            lineDirection = Direction.EAST;
            for (int i = 0; i < range; i++) {//east
                x = x + 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece() == null) {
                        tiles.add(boardController.getBoard().getTile(x, y));
                    }
                    else if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                        break;
                    }
                }
            }
            x = location[0];
            y = location[1];
            lineDirection = Direction.SOUTH;
            for (int i = 0; i < range; i++) {//south
                y = y + 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece() == null) {
                        tiles.add(boardController.getBoard().getTile(x, y));
                    }
                    else if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                        break;
                    }
                }
            }
            x = location[0];
            y = location[1];
            lineDirection = Direction.WEST;
            for (int i = 0; i > -range; i--) {//west
                x = x - 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece() == null) {
                        tiles.add(boardController.getBoard().getTile(x, y));
                    }
                    else if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                        break;
                    }
                }
            }
            return tiles;
        }

        public ArrayList<Tile> getAttackTilesInLineRangeAlpha(int[] location, int range, Direction direction) {
            int x = location[0];
            int y = location[1];
            ArrayList<Tile> tiles = new ArrayList<>();
            Direction lineDirection = Direction.NORTH;
            for (int i = 0; i > -range; i--) {//north
                y = y - 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece()!= null) {
                        if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                            tiles.add(boardController.getBoard().getTile(x, y));
                            break;
                        }
                    }
                }
            }
            x = location[0];
            y = location[1];
            lineDirection = Direction.EAST;
            for (int i = 0; i < range; i++) {//east
                x = x + 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece()!= null) {
                        if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                            tiles.add(boardController.getBoard().getTile(x, y));
                            break;
                        }
                    }
                }
            }
            x = location[0];
            y = location[1];
            lineDirection = Direction.SOUTH;
            for (int i = 0; i < range; i++) {//south
                y = y + 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece()!= null) {
                        if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                            tiles.add(boardController.getBoard().getTile(x, y));
                            break;
                        }
                    }
                }
            }
            x = location[0];
            y = location[1];
            lineDirection = Direction.WEST;
            for (int i = 0; i > -range; i--) {//west
                x = x - 1;
                if (((x < boardController.getSize())
                        && (y < boardController.getSize()))
                        && ((x >= 0) && (y >= 0))) {
                    if (boardController.getBoard().getTile(x, y).getPiece()!= null) {
                        if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                            tiles.add(boardController.getBoard().getTile(x, y));
                            break;
                        }
                    }
                }
            }
            return tiles;
        }

        public ArrayList<Tile> getAttackTilesInLineRange(int[] location, int range, Direction direction, Team team) {
            int x = location[0];
            int y = location[1];
            ArrayList<Tile> tiles = new ArrayList<>();
            switch (direction) {
                case NORTH -> {
                    for (int i = 0; i > -range; i--) {
                        y = y - 1;
                        if (((x < boardController.getSize())
                                && (y < boardController.getSize()))
                                && ((x >= 0) && (y >= 0))) {
                            if (boardController.getBoard().getTile(x, y).getPiece()!= null) {
                                if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                                    tiles.add(boardController.getBoard().getTile(x, y));
                                    return tiles;
                                }
                            }
                        }
                    }
                }
                case EAST -> {
                    for (int i = 0; i < range; i++) {
                        x = x + 1;
                        if (((x < boardController.getSize())
                                && (y < boardController.getSize()))
                                && ((x >= 0) && (y >= 0))) {
                            if (boardController.getBoard().getTile(x, y).getPiece()!= null) {
                                if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                                    tiles.add(boardController.getBoard().getTile(x, y));
                                    return tiles;
                                }
                            }
                        }
                    }
                }
                case SOUTH -> {
                    for (int i = 0; i < range; i++) {
                        y = y + 1;
                        if (((x < boardController.getSize())
                                && (y < boardController.getSize()))
                                && ((x >= 0) && (y >= 0))) {
                            if (boardController.getBoard().getTile(x, y).getPiece()!= null) {
                                if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                                    tiles.add(boardController.getBoard().getTile(x, y));
                                    return tiles;
                                }
                            }
                        }
                    }
                }
                case WEST -> {
                    for (int i = 0; i > -range; i--) {
                        x = x - 1;
                        if (((x < boardController.getSize())
                                && (y < boardController.getSize()))
                                && ((x >= 0) && (y >= 0))) {
                            if (boardController.getBoard().getTile(x, y).getPiece()!= null) {
                                if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                                    tiles.add(boardController.getBoard().getTile(x, y));
                                    return tiles;
                                }
                            }
                        }
                    }
                }
            }
            return tiles;
        }

        public ArrayList<Tile> getTilesInSquareRange(int[] location, int range) {
            int X = location[0];
            int Y = location[1];
            ArrayList<Tile> tiles = new ArrayList<>();
            for (int i = -range; i <= range; i++) {
                for (int j = -range; j <= range; j++) {
                    int x = X + i;
                    int y = Y + j;
                    // Checks the agent isn't looking outside the grid, at its current tile or at terrain.
                    if (((x < boardController.getSize())
                            && (y < boardController.getSize()))
                            && ((x >= 0) && (y >= 0))
                            && !(i == 0 && j == 0)) {
                        if (boardController.getBoard().getTile(x, y).getPiece() == null) {
                            tiles.add(boardController.getBoard().getTile(x, y));
                        }
                    }
                }
            }
            return tiles;
        }

        public ArrayList<Tile> getAttackTilesInSquareRange(int[] location, int range, Team team) {
            int X = location[0];
            int Y = location[1];
            ArrayList<Tile> tiles = new ArrayList<>();
            for (int i = -range; i <= range; i++) {
                for (int j = -range; j <= range; j++) {
                    int x = X + i;
                    int y = Y + j;
                    // Checks the agent isn't looking outside the grid, at its current tile or at terrain.
                    if (((x < boardController.getSize())
                            && (y < boardController.getSize()))
                            && ((x >= 0) && (y >= 0))
                            && !(i == 0 && j == 0)) {
                        if (boardController.getBoard().getTile(x, y).getPiece()!= null) {
                            if (boardController.getBoard().getTile(x, y).getPiece().getTeam() != activeTeam) {
                                tiles.add(boardController.getBoard().getTile(x, y));
                            }
                        }
                    }
                }
            }
            return tiles;
        }
    }



}
