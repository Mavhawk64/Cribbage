package Cribbage.Cribbage;
/**
 * The Card class is just a simple class to determine the type of card.
 * Properties include suit, rank, and value
 * @author Maverick Berkland
 * @version 10/26/2022
 */
public class Card
{
    private final String[] RANKS = { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };
    public String suit;
    public String rank;
    public int value;
    public int rink;
    public Card(String suit, String rank, int value)
    {
        this.suit = suit;
        this.rank = rank;
        this.value = value;
        this.rink = java.util.Arrays.asList(RANKS).indexOf(rank);
    }

    public Card() {
        this.suit = "null";
        this.rank = "null";
        this.value = -1;
        this.rink = -1;
    }
    
    @Override
    public String toString()
    {
        return this.rank + " of " + this.suit + "s";
    }
}
