package Cribbage;

public class Board {
    private final int SCORE_LIMIT = 121;
    private int[] players;

    public Board(int players) {
        this.players = new int[players];
    }

    public void addScore(int player, int score) {
        this.players[player] += score;
    }

    public boolean isGameOver() {
        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i] >= this.SCORE_LIMIT) {
                return true;
            }
        }
        return false;
    }

    public String getWinner() {
        int winner = 0;
        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i] > this.players[winner]) {
                winner = i;
            }
        }
        return "Player " + (winner + 1);
    }

    @Override
    public String toString() {
        String ret = "***********\n   Board   \n***********\n\n" + "Player 1: " + this.players[0];
        for (int i = 1; i < this.players.length; i++) {
            ret += "\n\nPlayer " + (i + 1) + ": " + this.players[i];
        }
        return ret + "\n\n";
    }
}
