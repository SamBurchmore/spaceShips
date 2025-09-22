import javax.swing.*;
import java.awt.*;

public class InfoPanel {

    private JLabel armourLabel;
    private JLabel attackScoreLabel;
    private JLabel attackLabel;
    private JLabel pieceLabel;
    private JLabel damagedLabel;
    private JPanel infoPanel;

    public InfoPanel() {
        armourLabel = new JLabel();
        attackScoreLabel = new JLabel();
        attackLabel = new JLabel();
        pieceLabel = new JLabel();
        damagedLabel = new JLabel();
        infoPanel = new JPanel();
        infoPanel.setBackground(new Color(200, 200, 200));
        infoPanel.add(pieceLabel);
        infoPanel.add(armourLabel);
        infoPanel.add(attackScoreLabel);
        infoPanel.add(attackLabel);
        //infoPanel.add(damagedLabel);
    }


    public void setInfoPanelText(Piece piece) {
        armourLabel.setText("Armour: " +piece.getArmour());
        armourLabel.setForeground(Color.BLACK);
        attackScoreLabel.setText("Attack: " + piece.getAttack());
        pieceLabel.setText(String.valueOf(piece.getType()));
        if (piece.getDamaged()) {
            damagedLabel.setText("!!! DAMAGED !!!");
            armourLabel.setText("Armour: " + piece.getArmour()/2 + "/" + piece.getArmour());
            armourLabel.setForeground(Color.RED);
        }
        else {
            damagedLabel.setText(" ");
        }
        attackLabel.setText(" ");
    }

    public void clearInfoPanelText() {
        armourLabel.setText(" ");
        attackScoreLabel.setText(" ");
        pieceLabel.setText(" ");
        damagedLabel.setText(" ");
    }

    public void setAttackResult(Piece attacker, Piece defender, int attackScore, boolean result, boolean damaged) {
        if (defender.getDamaged() && !damaged) {
            if (result) {
                if (defender.getType() == PieceType.BATTLESHIP) {
                    attackLabel.setText(attacker.getTeam() + " wins.");
                }
                else {
                    attackLabel.setText(attackScore + " vs " + defender.getArmour()/2 + ". Success. " + defender.getType() + " destroyed.");
                }
            }
            else {
                attackLabel.setText(attackScore + " vs " + defender.getArmour()/2 + ". Fail." + defender.getType() + " missed.");
            }
        }
        else if (damaged) {
            attackLabel.setText(attackScore + " vs " + defender.getArmour() + ". Success. " + defender.getType() + " damaged.");
        }
        else if (result) {
            if (defender.getType() == PieceType.BATTLESHIP) {
                attackLabel.setText(attacker.getTeam() + " wins.");
            }
            else {
                attackLabel.setText(attackScore + " vs " + defender.getArmour() + ". Success. " + defender.getType() + " destroyed.");
            }
        }
        else {
            attackLabel.setText(attackScore + " vs " + defender.getArmour() + ". Fail. " + defender.getType() + " missed.");
        }
    }

    public JPanel getInfoPanel() {
        return infoPanel;
    }
}
