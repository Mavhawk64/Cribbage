package Cribbage;
import java.util.*;

/**
 * Write a description of class Deck here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Deck
{
    private final Card[] FULL_DECK = {new Card("Club","Ace",1),new Card("Club","2",2),new Card("Club","3",3),new Card("Club","4",4),new Card("Club","5",5),new Card("Club","6",6),new Card("Club","7",7),new Card("Club","8",8),new Card("Club","9",9),new Card("Club","10",10),new Card("Club","Jack",10),new Card("Club","Queen",10),new Card("Club","King",10),new Card("Diamond","Ace",1),new Card("Diamond","2",2),new Card("Diamond","3",3),new Card("Diamond","4",4),new Card("Diamond","5",5),new Card("Diamond","6",6),new Card("Diamond","7",7),new Card("Diamond","8",8),new Card("Diamond","9",9),new Card("Diamond","10",10),new Card("Diamond","Jack",10),new Card("Diamond","Queen",10),new Card("Diamond","King",10),new Card("Spade","Ace",1),new Card("Spade","2",2),new Card("Spade","3",3),new Card("Spade","4",4),new Card("Spade","5",5),new Card("Spade","6",6),new Card("Spade","7",7),new Card("Spade","8",8),new Card("Spade","9",9),new Card("Spade","10",10),new Card("Spade","Jack",10),new Card("Spade","Queen",10),new Card("Spade","King",10),new Card("Heart","Ace",1),new Card("Heart","2",2),new Card("Heart","3",3),new Card("Heart","4",4),new Card("Heart","5",5),new Card("Heart","6",6),new Card("Heart","7",7),new Card("Heart","8",8),new Card("Heart","9",9),new Card("Heart","10",10),new Card("Heart","Jack",10),new Card("Heart","Queen",10),new Card("Heart","King",10)};;
    private Hand[] ORIGINAL_HANDS;
    public Hand[] hands;
    public Hand crib;
    public Pile pile;
    public Card start;
    /**
     * Constructor for objects of class Deck
     */
    public Deck(int players)
    {
        if(players < 5 && players > 1) {
            this.hands = new Hand[players];
            this.ORIGINAL_HANDS = new Hand[players];
            this.InitializeHands();
            this.DealHands();
        }
        else this.exit();
    }

    private void InitializeHands() {
        this.crib = new Hand(4 - this.hands.length % 2, true);
        for(int i = 0; i < this.hands.length; i++) {
            this.hands[i] = new Hand(8 - this.hands.length);
            this.ORIGINAL_HANDS[i] = new Hand(8 - this.hands.length);
        }
        this.pile = new Pile();
    }

    private void DealHands() {
        int amt_cards = 8 - this.hands.length;
        Card[] rem_cards = FULL_DECK;
        for(int j = 0; j < amt_cards; j++) {
            for(int i = 0; i < this.hands.length; i++) {
                this.hands[i].DrawCard(rem_cards);
                this.ORIGINAL_HANDS[i] = this.hands[i].copy();
            }
        }
        this.DrawStartCard(rem_cards);
    }

    public void DrawStartCard(Card[] deck) {
        int x = (int)(Math.random() * 52);
        while(deck[x] == null) {
            x = (int)(Math.random() * 52);
        }
        this.start = new Card(deck[x].suit, deck[x].rank, deck[x].value);
        deck[x] = null;
    }

    public boolean playersStillHaveCards() {
        for(int i = 0; i < this.hands.length; i++) {
            if(this.hands[i].size() > 0) return true;
        }
        return false;
    }

    /**
     * @param playerid
     */
    public void discardPlayerCards(int playerid) {
        System.out.println("Player " + (playerid + 1) + ": " + this.hands[playerid].toString());
        int[] discard = new int[this.hands.length == 2 ? 2 : 1];
        System.out.println("The total number of cards you must discard is " + discard.length + ".");
        Scanner sc = new Scanner(System.in);
        for(int i = 0; i < discard.length; i++) {
            System.out.println("Enter the card you want to discard (1-" + this.hands[playerid].size() + "): ");
            
            int input = sc.nextInt();
            while(input < 1 || input > this.hands[playerid].size() || discard[0] == input || discard[discard.length-1] == input) {
                System.out.println("Invalid input. Enter the card you want to discard (1-" + this.hands[playerid].size() + "): ");
                input = sc.nextInt();
            }
            discard[i] = input;
        }
        sc.close();
        Arrays.sort(discard);
        for(int i = 0; i < discard.length; i++) {
            this.crib.addCard(this.hands[playerid].hand[discard[i]-1]);
            this.hands[playerid].hand[discard[i]-1] = null;
        }
        this.hands[playerid].removeNulls();
    }

    public void discardCpuCards() {
        int cpu = this.hands.length - 1;
        int[] discard = new int[cpu == 1 ? 2 : 1];
        for(int i = 0; i < discard.length; i++) {
            int input = (int)(Math.random() * this.hands[cpu].size()) + 1;
            while(discard[0] == input || discard[discard.length-1] == input) {
                input = (int)(Math.random() * this.hands[cpu].size()) + 1;
            }
            discard[i] = input;
        }
        Arrays.sort(discard);
        for(int i = discard.length - 1; i >= 0; i--) {
            this.crib.addCard(this.hands[cpu].hand[discard[i] - 1]);
            this.hands[cpu].hand[discard[i] - 1] = null;
        }
        this.hands[cpu].removeNulls();
    }

    public boolean playCpuCard() {
        System.out.println("The total value of the pile is " + this.pile.totalValue() + ".");
        int cpu = this.hands.length - 1;
        Hand validCards = new Hand(0);
        for(int i = 0; i < this.hands[cpu].size(); i++) {
            if(this.hands[cpu].hand[i].value + this.pile.totalValue() <= 31) {
                validCards.append(this.hands[cpu].hand[i]);
            }
        }
        if(validCards.size() == 0) {
            System.out.println("Player " + (cpu + 1) + " has no valid cards to play. That's a Go!");
            return false;
        }
        int input = (int)(Math.random() * validCards.size()) + 1;
        System.out.println("Player " + (cpu + 1) + " played the " + validCards.hand[input - 1].toString());
        this.pile.append(validCards.hand[input - 1]);
        this.hands[cpu].remove(validCards.hand[input - 1]);
        this.hands[cpu].removeNulls();
        return true;
    }

    public boolean playPlayerCard(int id) {
        System.out.println("Player " + (id + 1) + ": " + this.hands[id].toString());
        System.out.println("The total value of the pile is " + this.pile.totalValue() + ".");
        Hand validCards = new Hand(0);
        for(int i = 0; i < this.hands[id].size(); i++) {
            if(this.hands[id].hand[i].value + this.pile.totalValue() <= 31) {
                validCards.append(this.hands[id].hand[i]);
            }
        }

        if(validCards.size() == 0) {
            System.out.println("Player " + (id + 1) + " has no valid cards to play. That's a Go!");
            return false;
        }

        System.out.println("Player " + (id + 1) + "'s valid cards: " + validCards.toString());

        System.out.println("Enter the card you want to play (1-" + validCards.size() + "): ");
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();
        while(input < 1 || input > validCards.size()) {
            System.out.println("Invalid input. Enter the card you want to play (1-" + validCards.size() + "): ");
            input = sc.nextInt();
        }
        sc.close();
        System.out.println("Player " + (id + 1) + " played the " + validCards.hand[input - 1].toString());
        this.pile.append(validCards.hand[input - 1]);
        this.hands[id].remove(validCards.hand[input - 1]);
        this.hands[id].removeNulls();
        return true;
    }

    public int countHand(int id) {
        return this.hands[id].getAndSayPoints();
    }

    public int countCrib() {
        return this.crib.getAndSayPoints();
    }

    public int checkPilePoints() {
        return this.pile.getAndSayPoints();
    }

    public void wipePile() {
        this.pile = new Pile();
    }

    @Override
    public String toString() {
        String ret = "**********\n   Deck   \n**********\n\n*****\nHands\n*****\n\n";
        if(this.hands != null) {
            for(int i = 0; i < this.hands.length; i++) {
                int p = i+1;
                ret += "Player " + p + ": " + this.hands[i].toString() + "\n\n";
            }
        }

        if(this.crib != null && this.crib.size() > 0) {
            ret += "****\nCrib\n****\n\n" + this.crib.toString() + "\n\n";
        }
        return ret;
    }

    private void exit() {
        System.err.println("Error: Invalid amount of players");
    }
}
