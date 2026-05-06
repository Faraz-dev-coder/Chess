package UI.Panels;

import AI.ChessAi;
import Game.GameMode;
import Logic.GameController;
import Model.Board;
import Model.Move;
import Model.Tile;
import Pieces.Piece;
import UI.GameFrame;
import Util.SoundPlayer;
import UI.TilePanel;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private GameFrame frame;
    private GameController controller;
    private ChessAi ai = new ChessAi();

    public GamePanel(GameFrame frame) {
        this.frame = frame;
        setLayout(new GridLayout(8, 8));
    }

    public void startGame(GameMode mode) {

        controller = new GameController();

        removeAll();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

//                JPanel tilePanel = new JPanel(new BorderLayout());

                int r = row;
                int c = col;
                // Use your custom TilePanel class instead of a generic JPanel
                TilePanel tilePanel = new TilePanel(row, col);
                tilePanel.setLayout(new BorderLayout());

                tilePanel.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        if (frame.getGameMode() == GameMode.SINGLE_PLAYER && !controller.isWhiteTurn()) {
                            return;
                        }
                        controller.handleClick(r, c);
                        refreshBoard();

                        // 🔥 AI DELAYED MOVE
                        if (frame.getGameMode() == GameMode.SINGLE_PLAYER
                                && !controller.isWhiteTurn()) {

                            Timer timer = new Timer(3000, e -> {

                                Move aiMove = ai.getBestMove(controller.getBoard());

                                if (aiMove != null) {
                                    controller.handleClick(aiMove.getFromRow(), aiMove.getFromCol());
                                    controller.handleClick(aiMove.getToRow(), aiMove.getToCol());
                                    refreshBoard();
                                }

                            });

                            timer.setRepeats(false);
                            timer.start();
                        }
                    }
                });

                add(tilePanel);
            }
        }

        refreshBoard();
        revalidate();
        repaint();
    }

    private void refreshBoard() {
        Board board = controller.getBoard();
        Component[] components = getComponents(); // These are your TilePanels

        for (int i = 0; i < components.length; i++) {
            TilePanel tilePanel = (TilePanel) components[i];
            int row = i / 8;
            int col = i % 8;

            // POINT 5 FIX: Update background instead of rebuilding
            boolean isHighlight = controller.getCurrentValidMoves().stream()
                    .anyMatch(m -> m.getToRow() == row && m.getToCol() == col);

            tilePanel.setBackground(isHighlight ? new Color(144, 238, 144) :
                    (row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);

            // Update Icons
            tilePanel.removeAll();
            Tile tile = board.getTile(row, col);
            if (tile.isOccupied()) {
                JLabel label = new JLabel(tile.getPiece().getIcon());
                tilePanel.add(label);
            }
            tilePanel.revalidate();
            tilePanel.repaint();
        }
    }
}