package Logic;

public class TurnManager {

    private boolean whiteTurn = true;

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public void switchTurn() {
        whiteTurn = !whiteTurn;
    }
}