import java.awt.*;

public class Cruiser implements Piece {

    private int[] location;

    private final Team team;

    private final int armour;

    private final int attack;

    private boolean damaged;

    private final int movementRange;

    private final int attackRange;
    private Direction direction;



    public Cruiser(Team team, int[] location) {
        this.team = team;
        this.location = location;
        armour = 6;
        attack = 10;
        damaged = false;
        attackRange = 8;
        movementRange = 8;
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
        return PieceType.CRUISER;
    }

    @Override
    public Color getColor() {
        return Color.MAGENTA;
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
