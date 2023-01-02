package Cribbage;

import java.util.*;

/**
 * Write a description of class Hand here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Hand {
    private Random rand;
    public Card[] hand;
    private Card start;
    private int size;
    private boolean isCrib;

    /**
     * Constructor for objects of class Hand
     */
    public Hand(int amt_cards) {
        this.rand = new Random();
        this.hand = new Card[amt_cards];
        this.size = 0;
        this.start = new Card();
        this.isCrib = false;
    }

    public Hand(int amt_cards, boolean isCrib) {
        this.rand = new Random();
        this.hand = new Card[amt_cards];
        this.size = 0;
        this.start = new Card();
        this.isCrib = isCrib;
    }

    public void setStartCard(Card start) {
        this.start = start;
    }

    public Card getStartCard() {
        return this.start;
    }

    public void DrawCard(Card[] deck) {
        int x = this.rand.nextInt(52);
        while (deck[x] == null) {
            x = this.rand.nextInt(52);
        }
        this.hand[this.size++] = new Card(deck[x].suit, deck[x].rank, deck[x].value);
        deck[x] = null;
    }

    private void sortHand() {
        for (int i = 0; i < this.hand.length; i++) {
            for (int j = 0; j < this.hand.length - 1; j++) {
                if (this.hand[j].rink > this.hand[j + 1].rink) {
                    Card temp = new Card(this.hand[j].suit, this.hand[j].rank, this.hand[j].value);
                    this.hand[j] = new Card(this.hand[j + 1].suit, this.hand[j + 1].rank, this.hand[j + 1].value);
                    this.hand[j + 1] = temp;
                }
            }
        }
    }

    public int getAndSayPoints() {
        int total = 0;
        this.sortHand();
        // Fifteens
        ArrayList<Hand> fif2 = this.getFifteensOfTwoCards();
        ArrayList<Hand> fif3 = this.getFifteensOfThreeCards();
        ArrayList<Hand> fif4 = this.getFifteensOfFourCards();
        Hand fif5 = this.getFifteenOfFiveCards();

        for (Hand h : fif2) {
            total += 2;
            System.out.println(h.toString() + " makes 15 for " + total + " points!");
        }

        for (Hand h : fif3) {
            total += 2;
            System.out.println(h.toString() + " makes 15 for " + total + " points!");
        }

        for (Hand h : fif4) {
            total += 2;
            System.out.println(h.toString() + " makes 15 for " + total + " points!");
        }

        if (fif5 != null) {
            total += 2;
            System.out.println(fif5.toString() + " makes 15 for " + total + " points!");
        }

        // n-of-a-kind
        ArrayList<Hand> pairs = this.getPairs();
        Hand threes = this.getThreeOfAKind();
        Hand four = this.getFourOfAKind();

        /*
         * Can't count a pair and a triple or quadruple together. Subsets (pairs or
         * triples) must be removed.
         */
        if (four != null) {
            total += 12;
            System.out.println(four.toString() + " makes 4 of a kind for " + total + " points!");
            pairs = null;
        } else if (threes != null) {
            total += 6;
            System.out.println(threes.toString() + " makes 3 of a kind for " + total + " points!");
            for (int i = 0; i < pairs.size(); i++) {
                if (pairs.get(i).hand[0].rank.equals(threes.hand[0].rank)) {
                    pairs.remove(i);
                    break;
                }
            }
        }
        if (pairs != null) {
            for (Hand h : pairs) {
                total += 2;
                System.out.println(h.toString() + " makes a pair for " + total + " points!");
            }
        }

        // Runs
        ArrayList<Hand> run3 = this.getRunsOfThreeCards();
        ArrayList<Hand> run4 = this.getRunsOfFourCards();
        Hand run5 = this.getRunOfFiveCards();

        /* run5 > run4 > run3 precedence */
        if (run5 != null) {
            total += 5;
            System.out.println(run5.toString() + " makes a run of 5 for " + total + " points!");
        } else if (run4.size() > 0) {
            for (Hand h : run4) {
                total += 4;
                System.out.println(h.toString() + " makes a run of 4 for " + total + " points!");
            }
        } else if (run3.size() > 0) {
            for (Hand h : run3) {
                total += 3;
                System.out.println(h.toString() + " makes a run of 3 for " + total + " points!");
            }
        }

        // Flushes
        Hand flush = this.getFlushOfFourCards();
        Hand flush5 = this.getFlushOfFiveCards();

        if (flush5 != null) {
            total += 5;
            System.out.println(flush5.toString() + " makes a flush for " + total + " points!");
        } else if (flush != null) {
            total += 4;
            System.out.println(flush.toString() + " makes a flush for " + total + " points!");
        }

        // His Nobs
        Card nobs = this.getHisNobs();

        if (nobs != null) {
            total += 1;
            String s = "His Nobs! " + nobs.toString() + " makes " + total + " point";
            if (total > 1) {
                s += "s";
            }
            s += "!";
            System.out.println(s);
        }

        return total;
    }

    public int totalPoints() {
        int total = 0;
        this.sortHand();
        // Fifteens
        ArrayList<Hand> fif2 = this.getFifteensOfTwoCards();
        ArrayList<Hand> fif3 = this.getFifteensOfThreeCards();
        ArrayList<Hand> fif4 = this.getFifteensOfFourCards();
        Hand fif5 = this.getFifteenOfFiveCards();

        /* No overlap to worry about */
        total += (fif2.size() + fif3.size() + fif4.size() + (fif5 != null ? 1 : 0)) * 2;

        // n-Of-a-Kind
        ArrayList<Hand> pairs = this.getPairs();
        Hand threes = this.getThreeOfAKind();
        Hand four = this.getFourOfAKind();

        /*
         * Can't count a pair and a triple or quadruple together. Subsets (pairs or
         * triples) must be removed.
         */
        if (four != null) {
            total += 12;
            pairs = null;
        } else if (threes != null) {
            total += 6;
            for (int i = 0; i < pairs.size(); i++) {
                if (pairs.get(i).hand[0].rank.equals(threes.hand[0].rank)) {
                    pairs.remove(i);
                    break;
                }
            }
        }
        if (pairs != null) {
            total += pairs.size() * 2;
        }

        // Runs
        ArrayList<Hand> run3 = this.getRunsOfThreeCards();
        ArrayList<Hand> run4 = this.getRunsOfFourCards();
        Hand run5 = this.getRunOfFiveCards();

        /* run5 > run4 > run3 precedence */
        if (run5 != null) {
            total += 5;
        } else if (run4.size() > 0) {
            total += 4 * run4.size();
        } else if (run3.size() > 0) {
            total += 3 * run3.size();
        }

        // Flushes
        Hand flush = this.getFlushOfFourCards();
        Hand flush5 = this.getFlushOfFiveCards();

        total += flush5 == null ? (this.isCrib ? 0 : 1) * (flush == null ? 0 : 4) : 5;

        // His Nobs
        Card nobs = this.getHisNobs();

        total += nobs == null ? 0 : 1;

        return total;
    }

    private ArrayList<Hand> getFifteensOfTwoCards() {
        ArrayList<Hand> fifteens = new ArrayList<Hand>();
        // Check within the hand
        for (int i = 0; i < this.hand.length; i++) {
            for (int j = i + 1; j < this.hand.length; j++) {
                if (this.hand[i].value + this.hand[j].value == 15) {
                    fifteens.add(new Hand(2));
                    fifteens.get(fifteens.size() - 1).hand[0] = this.hand[i];
                    fifteens.get(fifteens.size() - 1).hand[1] = this.hand[j];
                }
            }
        }
        // Check with the start card
        for (int i = 0; i < this.hand.length; i++) {
            if (this.hand[i].value + this.start.value == 15) {
                fifteens.add(new Hand(2));
                fifteens.get(fifteens.size() - 1).hand[0] = this.hand[i];
                fifteens.get(fifteens.size() - 1).hand[1] = this.start;
            }
        }
        return fifteens;
    }

    private ArrayList<Hand> getFifteensOfThreeCards() {
        ArrayList<Hand> fifteens = new ArrayList<Hand>();
        // Check within the hand
        for (int i = 0; i < this.hand.length; i++) {
            for (int j = i + 1; j < this.hand.length; j++) {
                for (int k = j + 1; k < this.hand.length; k++) {
                    if (this.hand[i].value + this.hand[j].value + this.hand[k].value == 15) {
                        fifteens.add(new Hand(3));
                        fifteens.get(fifteens.size() - 1).hand[0] = this.hand[i];
                        fifteens.get(fifteens.size() - 1).hand[1] = this.hand[j];
                        fifteens.get(fifteens.size() - 1).hand[2] = this.hand[k];
                    }
                }
            }
        }
        // Check with the start card
        for (int i = 0; i < this.hand.length; i++) {
            for (int j = i + 1; j < this.hand.length; j++) {
                if (this.hand[i].value + this.hand[j].value + this.start.value == 15) {
                    fifteens.add(new Hand(3));
                    fifteens.get(fifteens.size() - 1).hand[0] = this.hand[i];
                    fifteens.get(fifteens.size() - 1).hand[1] = this.hand[j];
                    fifteens.get(fifteens.size() - 1).hand[2] = this.start;
                }
            }
        }
        return fifteens;
    }

    private ArrayList<Hand> getFifteensOfFourCards() {
        ArrayList<Hand> fifteens = new ArrayList<Hand>();
        // Check within the hand
        for (int i = 0; i < this.hand.length; i++) {
            for (int j = i + 1; j < this.hand.length; j++) {
                for (int k = j + 1; k < this.hand.length; k++) {
                    for (int l = k + 1; l < this.hand.length; l++) {
                        if (this.hand[i].value + this.hand[j].value + this.hand[k].value + this.hand[l].value == 15) {
                            fifteens.add(new Hand(4));
                            fifteens.get(fifteens.size() - 1).hand[0] = this.hand[i];
                            fifteens.get(fifteens.size() - 1).hand[1] = this.hand[j];
                            fifteens.get(fifteens.size() - 1).hand[2] = this.hand[k];
                            fifteens.get(fifteens.size() - 1).hand[3] = this.hand[l];
                        }
                    }
                }
            }
        }
        // Check with the start card
        for (int i = 0; i < this.hand.length; i++) {
            for (int j = i + 1; j < this.hand.length; j++) {
                for (int k = j + 1; k < this.hand.length; k++) {
                    if (this.hand[i].value + this.hand[j].value + this.hand[k].value + this.start.value == 15) {
                        fifteens.add(new Hand(4));
                        fifteens.get(fifteens.size() - 1).hand[0] = this.hand[i];
                        fifteens.get(fifteens.size() - 1).hand[1] = this.hand[j];
                        fifteens.get(fifteens.size() - 1).hand[2] = this.hand[k];
                        fifteens.get(fifteens.size() - 1).hand[3] = this.start;
                    }
                }
            }
        }
        return fifteens;
    }

    private Hand getFifteenOfFiveCards() {
        if (this.hand.length == 4 && this.hand[0].value + this.hand[1].value + this.hand[2].value + this.hand[3].value
                + this.start.value == 15) {
            Hand fifteen = new Hand(5);
            fifteen.hand[0] = this.hand[0];
            fifteen.hand[1] = this.hand[1];
            fifteen.hand[2] = this.hand[2];
            fifteen.hand[3] = this.hand[3];
            fifteen.hand[4] = this.start;
            return fifteen;
        }
        return null;
    }

    private ArrayList<Hand> getPairs() {
        ArrayList<Hand> pairs = new ArrayList<Hand>();
        // Check within the hand
        for (int i = 0; i < this.hand.length; i++) {
            for (int j = i + 1; j < this.hand.length; j++) {
                if (this.hand[i].rink == this.hand[j].rink) {
                    pairs.add(new Hand(2));
                    pairs.get(pairs.size() - 1).hand[0] = this.hand[i];
                    pairs.get(pairs.size() - 1).hand[1] = this.hand[j];
                }
            }
        }
        // Check with the start card
        for (int i = 0; i < this.hand.length; i++) {
            if (this.hand[i].rink == this.start.rink) {
                pairs.add(new Hand(2));
                pairs.get(pairs.size() - 1).hand[0] = this.hand[i];
                pairs.get(pairs.size() - 1).hand[1] = this.start;
            }
        }
        return pairs;
    }

    private Hand getThreeOfAKind() {
        // Check within the hand
        for (int i = 0; i < this.hand.length; i++) {
            for (int j = i + 1; j < this.hand.length; j++) {
                for (int k = j + 1; k < this.hand.length; k++) {
                    if (this.hand[i].rink == this.hand[j].rink && this.hand[j].rink == this.hand[k].rink) {
                        Hand threeOfAKind = new Hand(3);
                        threeOfAKind.hand[0] = this.hand[i];
                        threeOfAKind.hand[1] = this.hand[j];
                        threeOfAKind.hand[2] = this.hand[k];
                        return threeOfAKind;
                    }
                }
            }
        }
        // Check with the start card
        for (int i = 0; i < this.hand.length; i++) {
            for (int j = i + 1; j < this.hand.length; j++) {
                if (this.hand[i].rink == this.hand[j].rink && this.hand[j].rink == this.start.rink) {
                    Hand threeOfAKind = new Hand(3);
                    threeOfAKind.hand[0] = this.hand[i];
                    threeOfAKind.hand[1] = this.hand[j];
                    threeOfAKind.hand[2] = this.start;
                    return threeOfAKind;
                }
            }
        }
        return null;
    }

    private Hand getFourOfAKind() {
        // Check within the hand
        if (this.hand.length == 4 && this.hand[0].rink == this.hand[1].rink && this.hand[1].rink == this.hand[2].rink
                && this.hand[2].rink == this.hand[3].rink) {
            Hand fourOfAKind = new Hand(4);
            fourOfAKind.hand[0] = this.hand[0];
            fourOfAKind.hand[1] = this.hand[1];
            fourOfAKind.hand[2] = this.hand[2];
            fourOfAKind.hand[3] = this.hand[3];
            return fourOfAKind;
        }
        // Check with the start card
        for (int i = 0; i < this.hand.length; i++) {
            for (int j = i + 1; j < this.hand.length; j++) {
                for (int k = j + 1; k < this.hand.length; k++) {
                    if (this.hand[i].rink == this.hand[j].rink && this.hand[j].rink == this.hand[k].rink
                            && this.hand[k].rink == this.start.rink) {
                        Hand fourOfAKind = new Hand(4);
                        fourOfAKind.hand[0] = this.hand[i];
                        fourOfAKind.hand[1] = this.hand[j];
                        fourOfAKind.hand[2] = this.hand[k];
                        fourOfAKind.hand[3] = this.start;
                        return fourOfAKind;
                    }
                }
            }
        }
        return null;
    }

    private ArrayList<Hand> getRunsOfThreeCards() {
        // 4,5,6
        ArrayList<Hand> runs = new ArrayList<Hand>();
        // Check within the hand
        for (int i = 0; i < this.hand.length; i++) {
            for (int j = i + 1; j < this.hand.length; j++) {
                for (int k = j + 1; k < this.hand.length; k++) {
                    // Case 0: hand, hand, hand
                    if ((this.hand[i].rink + 1 == this.hand[j].rink && this.hand[j].rink + 1 == this.hand[k].rink)) {
                        runs.add(new Hand(3));
                        runs.get(runs.size() - 1).hand[0] = this.hand[i];
                        runs.get(runs.size() - 1).hand[1] = this.hand[j];
                        runs.get(runs.size() - 1).hand[2] = this.hand[k];
                    }
                }
            }
        }
        // Check with the start card
        for (int i = 0; i < this.hand.length; i++) {
            for (int j = i + 1; j < this.hand.length; j++) {
                Hand temp = new Hand(3);
                temp.hand[0] = this.hand[i];
                temp.hand[1] = this.hand[j];
                temp.hand[2] = this.start;
                temp.sortHand();
                if (temp.hand[0].rink + 1 == temp.hand[1].rink && temp.hand[1].rink + 1 == temp.hand[2].rink) {
                    runs.add(temp);
                }
            }
        }
        return runs;
    }

    private ArrayList<Hand> getRunsOfFourCards() {
        // 4,5,6,7
        ArrayList<Hand> runs = new ArrayList<Hand>();
        // Check within the hand
        if (this.hand.length == 4) {
            // Case 0: hand, hand, hand, hand
            if (this.hand[0].rink + 1 == this.hand[1].rink && this.hand[1].rink + 1 == this.hand[2].rink
                    && this.hand[2].rink + 1 == this.hand[3].rink) {
                runs.add(new Hand(4));
                runs.get(runs.size() - 1).hand[0] = this.hand[0];
                runs.get(runs.size() - 1).hand[1] = this.hand[1];
                runs.get(runs.size() - 1).hand[2] = this.hand[2];
                runs.get(runs.size() - 1).hand[3] = this.hand[3];
            }
        }
        // Check with the start card
        for (int i = 0; i < this.hand.length; i++) {
            for (int j = i + 1; j < this.hand.length; j++) {
                for (int k = j + 1; k < this.hand.length; k++) {
                    Hand temp = new Hand(4);
                    temp.hand[0] = this.hand[i];
                    temp.hand[1] = this.hand[j];
                    temp.hand[2] = this.hand[k];
                    temp.hand[3] = this.start;
                    temp.sortHand();
                    if (temp.hand[0].rink + 1 == temp.hand[1].rink && temp.hand[1].rink + 1 == temp.hand[2].rink
                            && temp.hand[2].rink + 1 == temp.hand[3].rink) {
                        runs.add(temp);
                    }
                }
            }
        }
        return runs;
    }

    private Hand getRunOfFiveCards() {
        // 4,5,6,7,8
        // Check with the start card
        Hand run = new Hand(5);
        run.hand[0] = this.hand[0];
        run.hand[1] = this.hand[1];
        run.hand[2] = this.hand[2];
        run.hand[3] = this.hand[3];
        run.hand[4] = this.start;
        run.sortHand();
        if (run.hand[0].rink + 1 == run.hand[1].rink && run.hand[1].rink + 1 == run.hand[2].rink
                && run.hand[2].rink + 1 == run.hand[3].rink && run.hand[3].rink + 1 == run.hand[4].rink) {
            return run;
        }
        return null;
    }

    private Hand getFlushOfFourCards() {
        if (this.hand.length == 4 && this.hand[0].suit == this.hand[1].suit && this.hand[1].suit == this.hand[2].suit
                && this.hand[2].suit == this.hand[3].suit) {
            Hand flush = new Hand(4);
            flush.hand[0] = this.hand[0];
            flush.hand[1] = this.hand[1];
            flush.hand[2] = this.hand[2];
            flush.hand[3] = this.hand[3];
            return flush;
        }
        return null;
    }

    private Hand getFlushOfFiveCards() {
        if (this.hand.length == 4 && this.hand[0].suit == this.hand[1].suit && this.hand[1].suit == this.hand[2].suit
                && this.hand[2].suit == this.hand[3].suit && this.hand[3].suit == this.start.suit) {
            Hand flush = new Hand(5);
            flush.hand[0] = this.hand[0];
            flush.hand[1] = this.hand[1];
            flush.hand[2] = this.hand[2];
            flush.hand[3] = this.hand[3];
            flush.hand[4] = this.start;
            return flush;
        }
        return null;
    }

    private Card getHisNobs() {
        for (int i = 0; i < this.hand.length; i++) {
            if (this.hand[i].rank.equals("Jack") && this.hand[i].suit.equals(this.start.suit)) {
                return this.hand[i];
            }
        }
        return null;
    }

    public void addCard(Card c) {
        this.hand[this.size++] = new Card(c.suit, c.rank, c.value);
    }

    public Card append(Card c) {
        Card[] new_hand = new Card[this.hand.length + 1];
        for (int i = 0; i < this.hand.length; i++) {
            new_hand[i] = this.hand[i];
        }
        new_hand[new_hand.length - 1] = new Card(c.suit, c.rank, c.value);
        this.hand = new_hand;
        return c;
    }

    public Card remove(Card c) {
        for (int i = 0; i < this.hand.length; i++) {
            if (this.hand[i].rank.equals(c.rank) && this.hand[i].suit.equals(c.suit)) {
                this.hand[i] = null;
                return c;
            }
        }
        return null;
    }

    public void removeNulls() {
        this.hand = Arrays.stream(this.hand).filter(Objects::nonNull).toArray(Card[]::new);
        this.size = this.hand.length;
    }

    public int size() {
        if (this.hand == null) {
            return 0;
        }
        if (this.hand.length < this.size) {
            this.size = this.hand.length;
        }
        return this.size;
    }

    public Hand copy() {
        Hand copy = new Hand(this.size());
        for (int i = 0; i < this.size(); i++) {
            copy.hand[i] = new Card(this.hand[i].suit, this.hand[i].rank, this.hand[i].value);
        }
        return copy;
    }

    @Override
    public String toString() {
        String ret = this.hand[0].toString();
        for (int i = 1; i < this.size(); i++) {
            ret += ", " + this.hand[i].toString();
        }
        return ret;
    }
}
