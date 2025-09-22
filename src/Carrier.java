import java.awt.*;

public class Carrier implements Piece {

    private final Team team;

    int[] location;

    private final int armour;

    private final int attack;

    private boolean damaged;
    private final int movementRange;

    private final int attackRange;
    private Direction direction;




    public Carrier(Team team, int[] location) {
        this.team = team;
        this.location = location;
        armour = 6;
        attack = 10;
        damaged = false;
        attackRange = 6;
        movementRange = 1;
        if (team == Team.BLACK) {
            direction = Direction.SOUTH;
        }
        else {
            direction = Direction.NORTH;
        }
    }

    @Override
    public Direction getValidDirectionChanges() {
        return null;
    }

    @Override
    public PieceType getType() {
        return PieceType.CARRIER;
    }

    @Override
    public Color getColor() {
        return Color.CYAN;
    }

    @Override
    public Team getTeam() {
        return team;
    }

    @Override
    public int[] getLocation() {
        return location;
    }

    @Override
    public void setLocation(int[] location) {
        this.location = location;
    }

    @Override
    public int getArmour() {
        return armour;
    }

    @Override
    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    @Override
    public boolean getDamaged() {
        return damaged;
    }

    @Override
    public int getAttack() {
        return attack;
    }

    @Override
    public int getMovementRange() {
        return movementRange;
    }

    @Override
    public int getAttackRange() {
        return attackRange;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
