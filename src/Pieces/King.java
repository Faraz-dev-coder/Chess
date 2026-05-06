package Pieces;

import Model.Board;
import Model.Move;
import Model.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    protected ImageIcon loadIcon() {
        String path = isWhite ?
                "/Assets/Pieces/white_king.png" :
                "/Assets/Pieces/black_king.png";

        ImageIcon original = new ImageIcon(getClass().getResource(path));
        Image img = original.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public List<Move> getValidMoves(Board board, int row, int col) {

        List<Move> moves = new ArrayList<>();

        int[][] dirs = {
                {1,0}, {-1,0}, {0,1}, {0,-1},
                {1,1}, {1,-1}, {-1,1}, {-1,-1}
        };

        for (int[] d : dirs) {

            int r = row + d[0];
            int c = col + d[1];

            if (r >= 0 && r < 8 && c >= 0 && c < 8) {

                Tile tile = board.getTile(r, c);

                if (!tile.isOccupied() || tile.getPiece().isWhite() != isWhite) {
                    moves.add(new Move(row, col, r, c));
                }
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return "K";
    }
}