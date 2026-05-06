package Pieces;

import Model.Board;
import Model.Move;
import Model.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    protected ImageIcon loadIcon() {
        String path = isWhite ?
                "/Assets/Pieces/white_pawn.png" :
                "/Assets/Pieces/black_pawn.png";

        ImageIcon original = new ImageIcon(getClass().getResource(path));
        Image img = original.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    @Override
    public List<Move> getValidMoves(Board board, int row, int col) {
        List<Move> moves = new ArrayList<>();
        int dir = isWhite ? -1 : 1;
        int startRow = isWhite ? 6 : 1;

        // One square forward
        int nextRow = row + dir;
        if (nextRow >= 0 && nextRow < 8 && !board.getTile(nextRow, col).isOccupied()) {
            moves.add(new Move(row, col, nextRow, col));

            // POINT 3 FIX: Two squares forward from start
            int doubleNextRow = row + (2 * dir);
            if (row == startRow && !board.getTile(doubleNextRow, col).isOccupied()) {
                moves.add(new Move(row, col, doubleNextRow, col));
            }
        }

        // Diagonal Captures
        int[] cols = {col - 1, col + 1};
        for (int c : cols) {
            if (c >= 0 && c < 8 && nextRow >= 0 && nextRow < 8) {
                Tile target = board.getTile(nextRow, c);
                if (target.isOccupied() && target.getPiece().isWhite() != isWhite) {
                    moves.add(new Move(row, col, nextRow, c));
                }
            }
        }
        return moves;
    }

    @Override
    public String getSymbol() {
        return "P";
    }
}