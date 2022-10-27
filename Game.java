package Cribbage;

public class Game
{
    private Deck deck;
    private Board board;
    private int players;
    private int round;
    private long startTime;
    public Game(int players)
    {
        this.startTime = System.nanoTime();
        this.players = players;
        this.round = 0;
        this.board = new Board(players);
        this.startGame();
    }

    private void startGame()
    {
        this.play();
        // Game is over
        System.out.println("Game over!\n\n" + this.board.getWinner() + " wins!");
        System.out.println("The game lasted " + this.round + " rounds.");
        long seconds = (System.nanoTime() - this.startTime) / 1000000000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        minutes %= 60;
        seconds %= 60;
        System.out.println("The game lasted " + hours + " hours, " + minutes + " minutes, and " + seconds + " seconds.");
    }

    /**
     * The owner of the crib is the dealer (starting with player 1, then 2 and so on)
     * 1. Deal (6/5/4) cards to each player (for 2/3/4 players)
     * 2. Discard (2/1) cards each to the crib (for 2 or 3/4 players)
     * 3. Flip the Start Card
     *  * 3a. If the start card is a Jack, the dealer gets 2 points
     * 4. The player to the left of the dealer goes first and announces the value of their card
     * 5. Each player plays a card, announcing the sum of the cards played so far
     *  * 5a. If the sum is 15, the player gets 2 points
     * * 5b. If the sum is 31, the player gets 2 points
     * * 5c. If the player can't play a card to reach a sum at or under 31, they say "Go", the previous player gets 1 point,
     * they can continue the sum to gain any extra points, and upon reaching or breaching 31, they gain their points, and the next player plays at 0.
     * * 5d. If a player reaches exactly 31, they get 2 points
     * 6. Scoring Points During Play
     * * 6a. Fifteen: For adding a card that makes the total 15, peg 2 points.
     * * 6b. Pairs: For adding a card that makes a pair, peg 2 points.
     * * 6c. Three-of-a-Kind: For adding a card that makes a three-of-a-kind, peg 6 points.
     * * 6d. Four-of-a-Kind: For adding a card that makes a four-of-a-kind, peg 12 points.
     * * 6e. Runs: For adding a card that makes a run of 3 or more cards, peg 1 point for each card in the run.
     * * * 6ei. If a run includes a pair, it counts as a double run, plus a pair.
     * * * 6eii. If a run includes a three-of-a-kind, it counts as a triple run, plus a three-of-a-kind.
     * * * 6eiii. If a run includes a four-of-a-kind, it counts as a quadruple run, plus a four-of-a-kind.
     * * * 6eiv. NOTE: A run made during the pegging phase doesn't have to be in order. For example, if there is a 3, 4, and 5,  the 5 can be played first, then the 3, then the 4.
     * * 6f. Flush: Four or more cards of the same suit in your hand.
     * * * 6fi. For a flush of 4 cards, peg 4 points. If the start card is also the same suit, peg 5 points.
     * * * 6fii. However, in the crib, only a flush of 5 cards counts. If the start card is also the same suit, peg 5 points.
     * * * 6fiii. NOTE: A flush doesn't happen during the play of cards; it occurs only when the hands and crib are counted.
     * * 6g. His Nobs: If the start card is a Jack, and it matches the suit of a card in your hand or the crib, peg 1 point.
     * * 6h. Last Card: If you play the last card in your hand, peg 1 point.
     * 7. Counting the Hands
     * * 7a. When play ends, the hands of cards are counted in order: non-dealer's hand, dealer's hand, crib.
     * * 7b. There is an etiquette to counting the hands. Follow step 6 in order.
     */
    public void play()
    {
        int crib_owner = -1;
        while(!this.board.isGameOver()) {
            this.round++;
            // Decide the dealer (crib owner), deal cards to each player, and determine the start card
            this.deck = new Deck(this.players);
            crib_owner = (crib_owner + 1) % this.players;
            // Ask each player to discard (2/1) cards to the crib
            for(int i = 0; i < this.players - 1; i++) {
                this.deck.discardPlayerCards(i);
            }
            // Randomly discard computer's cards to the crib
            this.deck.discardCpuCards();

            // DEBUG - Print the Game
            System.out.println(this.toString());
            
            // Flip the start card
            System.out.println("The start card is: " + this.deck.start.toString());
            // If the start card is a Jack, the dealer gets 2 points
            if(this.deck.start.rank.equals("Jack")) {
                this.board.addScore(crib_owner, 2);
                if(this.board.isGameOver()) {
                    return;
                }
            }

            // The player to the left of the dealer goes first and announces the value of their card,
            // and the pegging phase begins
            while(this.deck.playersStillHaveCards() && !this.board.isGameOver()) {
                boolean goEnd = true;
                for(int x = 0; x < this.players; x++) {
                    int i = (x + crib_owner + 1) % this.players;
                    // If the player has no cards, skip them
                    if(this.deck.hands[i].size() == 0) {
                        continue;
                    }

                    // Check if the player is a computer and play their card
                    boolean canPlay;
                    if(i == this.players - 1) {
                        // Computer's turn
                        canPlay = this.deck.playCpuCard();
                    } else {
                        // Player's turn
                        canPlay = this.deck.playPlayerCard(i);
                    }

                    // Check the points (15, 31, pairs, three-of-a-kind, four-of-a-kind, runs)
                    this.board.addScore(i, this.deck.checkPilePoints());
                    if(this.board.isGameOver()) {
                        return;
                    }

                    // If the player can't play a card, they say "Go", the previous player gets 1 point,
                    // they can continue the sum to gain any extra points, and upon reaching or breaching 31,
                    // they gain their points, and the next player plays at 0.
                    if(!canPlay) {
                        this.board.addScore((i - 1) % this.players, 1);
                        if(this.board.isGameOver()) {
                            return;
                        }
                    }
                    goEnd &= !canPlay;
                }
                // If no one can play a card, but there still are cards, restart the loop with a clear pile
                if(goEnd) {
                    this.deck.wipePile();
                }
            }
            // If no one can play a card, the pegging phase ends

            // Count the hands
            for(int i = 0; i < this.players; i++) {
                // Count the player's hand
                this.board.addScore(i, this.deck.countHand(i));
                if(this.board.isGameOver()) {
                    return;
                }
            }

            // Count the crib
            this.board.addScore(crib_owner, this.deck.countCrib());
            if(this.board.isGameOver()) {
                return;
            }

            // Show the board
            System.out.println("After round " + this.round + ":\n\n" + this.board.toString());
        }
    }

    @Override
    public String toString()
    {
        return this.deck.toString() + this.board.toString();
    }
}
