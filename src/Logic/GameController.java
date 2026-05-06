package Logic;

import Model.*;
import Pieces.Piece;
import Util.SoundPlayer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import Logic.CheckDetector;

import javax.swing.*;

public class GameController {
    private Board board;
    private boolean whiteTurn = true;
    private int selectedRow = -1, selectedCol = -1;
    private List<Move> currentValidMoves = new ArrayList<>();

    public GameController() { board = new Board(); }

    public void handleClick(int row, int col) {
        Tile tile = board.getTile(row, col);

        if (selectedRow == -1) {
            if (tile.isOccupied() && tile.getPiece().isWhite() == whiteTurn) {
                selectedRow = row;
                selectedCol = col;
                // POINT 6 FIX: Filter moves that would leave the king in check
                currentValidMoves = filterIllegalMoves(tile.getPiece().getValidMoves(board, row, col));
            }
        } else {
            Move selectedMove = currentValidMoves.stream()
                    .filter(m -> m.getToRow() == row && m.getToCol() == col)
                    .findFirst().orElse(null);

            if (selectedMove != null) {
                makeMove(selectedMove);
                whiteTurn = !whiteTurn;
                checkGameOver();
            }
            selectedRow = -1; selectedCol = -1;
            currentValidMoves.clear();
        }
    }

    private void checkGameOver() {
        if (CheckDetector.isCheckmate(board, whiteTurn, this)) {
            String winner = whiteTurn ? "Black" : "White";
            JOptionPane.showMessageDialog(null, "Checkmate! " + winner + " wins.");
        } else if (CheckDetector.isStalemate(board, whiteTurn, this)) {
            JOptionPane.showMessageDialog(null, "Draw! Stalemate.");
        }
    }

    // POINT 6 FIX: Simulates a move to see if it's legal
    public List<Move> filterIllegalMoves(List<Move> moves) {
        return moves.stream().filter(move -> {
            Tile from = board.getTile(move.getFromRow(), move.getFromCol());
            Tile to = board.getTile(move.getToRow(), move.getToCol());
            Piece movingPiece = from.getPiece();
            Piece capturedPiece = to.getPiece();

            // Simulate
            to.setPiece(movingPiece);
            from.setPiece(null);
            boolean inCheck = CheckDetector.isKingInCheck(board, whiteTurn);

            // Undo
            from.setPiece(movingPiece);
            to.setPiece(capturedPiece);

            return !inCheck;
        }).collect(Collectors.toList());
    }

    private void makeMove(Move move) {
        Tile from = board.getTile(move.getFromRow(), move.getFromCol());
        Tile to = board.getTile(move.getToRow(), move.getToCol());

        if (to.isOccupied()) SoundPlayer.play("capture.wav");
        else SoundPlayer.play("move.wav");

        to.setPiece(from.getPiece());
        from.setPiece(null);
    }

    // Getters...
    public Board getBoard() { return board; }
    public boolean isWhiteTurn() { return whiteTurn; }
    public List<Move> getCurrentValidMoves() { return currentValidMoves; }
}