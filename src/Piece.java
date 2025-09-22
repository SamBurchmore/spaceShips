import java.awt.*;
import java.security.DigestInputStream;

public interface Piece {

    Movement movement = null;

    Attack attack = null;

    Direction getValidDirectionChanges();

    PieceType getType();

    Color getColor();

    Team getTeam();

    int[] getLocation();

    void setLocation(int[] location);

    int getArmour();

    void setDamaged(boolean damaged);

    boolean getDamaged();

    int getAttack();

    int getMovementRange();

    int getAttackRange();

    Direction getDirection();

    void setDirection(Direction direction);

}
