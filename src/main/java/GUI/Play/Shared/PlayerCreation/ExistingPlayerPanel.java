package GUI.Play.Shared.PlayerCreation;

import GUI.Color;
import Helpers.ThrowableAssertions.ObjectAssertion;
import player.ActionsKeys;
import player.BasePlayer;
import player.ComputerPlayer.ComputerPlayer;
import player.HumanPlayer.HumanPlayer;
import player.PlayerType;
import player.SingleActionKey;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class ExistingPlayerPanel extends JPanel {

    PlayerTableModel playerTableModel;
    private JTable existingPlayersTable;
    private JScrollPane jScrollPane1;

    public void initUIComponents() {
        this.setLayout(new BorderLayout());

        initScrollPane();

        initExistingPlayersTable();

    }

    private void initExistingPlayersTable() {
        existingPlayersTable = new JTable();

        playerTableModel = new PlayerTableModel();
        existingPlayersTable.setModel(playerTableModel);

        existingPlayersTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        existingPlayersTable.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        existingPlayersTable.setFillsViewportHeight(true);
        existingPlayersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        existingPlayersTable.setShowGrid(false);
        existingPlayersTable.setShowHorizontalLines(false);
        existingPlayersTable.setShowVerticalLines(false);

        jScrollPane1.setViewportView(existingPlayersTable);

        this.add(jScrollPane1, BorderLayout.CENTER);
    }

    private void initScrollPane() {
        jScrollPane1 = new JScrollPane();
    }

    public void addPlayer(BasePlayer player) {
        playerTableModel.addRow(player);
    }

    public void reset() {
        playerTableModel.clear();
    }

    public boolean isTherePlayers() {
        return this.playerTableModel.getRowCount() > 0;
    }

    public boolean isPlayerWithCurrentInputsExistAlready(BasePlayer currentPlayer) {
        return this.playerTableModel.playerList.stream().anyMatch(player -> player.equals(currentPlayer));
    }

    public List<BasePlayer> getPlayers() {
        return playerTableModel.playerList;
    }

    private static class PlayerTableModel extends DefaultTableModel {
        private List<BasePlayer> playerList = new LinkedList<>();

        Class[] types = new Class[]{
                PlayerType.class,

                // Name
                String.class,

                Color.class,

                // Speed
                Integer.class,

                // Direction Keys
                SingleActionKey.class,
                SingleActionKey.class,
                SingleActionKey.class,
                SingleActionKey.class,

                // Special Keys
                SingleActionKey.class,
                SingleActionKey.class
        };

        boolean[] canEdit = new boolean[]{
                false, true, false, false, false, false, false, false, false, false
        };

        private final static String[] columnNames = {
                "Type", "Name", "Color", "Speed", "Up Key", "Right Key", "Down Key", "Left Key", "Speed Key", "Exit Key"
        };

        public PlayerTableModel() {
            super(new Object[][]{}, columnNames);
        }

        public Class getColumnClass(int columnIndex) {
            return types[columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit[columnIndex];
        }

        public void addRow(BasePlayer player) {
            ObjectAssertion.requireNonNull(player, "Player can't be null");
            playerList.add(player);
            super.addRow(convertPlayerToVector(player));
        }

        @Override
        public void removeRow(int row) {
            super.removeRow(row);
            this.playerList.remove(row);
        }

        private Vector<Object> convertPlayerToVector(BasePlayer player) {
            Vector<Object> row = new Vector<>();

            if(player instanceof HumanPlayer) {
                this.addHumanPlayerToRow(row, (HumanPlayer) player);
            } else if(player instanceof ComputerPlayer) {
                this.addComputerPlayerToRow(row, (ComputerPlayer) player);
            } else {

            }


            return row;
        }

        private void addBasePlayerDataToRow(Vector<Object> row, BasePlayer player) {
            row.add(1, player.getName());
            row.add(2, player.getColor());
        }

        private void addHumanPlayerToRow(Vector<Object> row, HumanPlayer player) {
            row.add(0, PlayerType.HUMAN);
            addBasePlayerDataToRow(row, player);
            row.add(3, player.getStepSpeed());

            ActionsKeys playerActionsKeys = player.getActionsKeys();
            row.add(4, playerActionsKeys.getUpKey());
            row.add(5, playerActionsKeys.getRightKey());
            row.add(6, playerActionsKeys.getDownKey());
            row.add(7, playerActionsKeys.getLeftKey());
            row.add(8, playerActionsKeys.getSpeedKey());
            row.add(9, playerActionsKeys.getExitKey());
        }

        private void addComputerPlayerToRow(Vector<Object> row, ComputerPlayer player) {
            row.add(0, PlayerType.COMPUTER);
            addBasePlayerDataToRow(row, player);
            row.add(3, player.getDelayMovementInMs());

        }

        private void clear() {
            dataVector.clear();
        }
    }
}
