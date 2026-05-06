package AI;

import Model.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChessAi {
    public Move getBestMove(Board board) {
        List<Move> allLegalMoves = new ArrayList<>();

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Tile tile = board.getTile(r, c);
                if (tile.isOccupied() && !tile.getPiece().isWhite()) {
                    // Note: In a full game, filter these for self-check too
                    allLegalMoves.addAll(tile.getPiece().getValidMoves(board, r, c));
                }
            }
        }

        if (allLegalMoves.isEmpty()) return null;

        // POINT 4 FIX: Shuffle moves so AI isn't predictable
        Collections.shuffle(allLegalMoves);
        return allLegalMoves.get(0);
    }
}