import java.awt.*;

public class Corvette implements Piece {

    private int[] location;
    private final Team team;

    private final int armour;

    private final int attack;

    private boolean damaged;

    private final int movementRange;

    private final int attackRange;
    private Direction direction;



    public Corvette(Team team, int[] location) {
        this.team = team;
        this.location = location;
        armour = 2;
        attack = 4;
        damaged = false;
        attackRange = 1;
        movementRange = 3;
        direction = null;
    }

    @Override
    public Direction getValidDirectionChanges() {
        return null;
    }

    @Override
    public PieceType getType() {
        return PieceType.CORVETTE;
    }

    @Override
    public Color getColor() {
        if (team == Team.BLACK) {
            return new Color(30, 30, 30);
        }
        return new Color(225, 225, 225);
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
