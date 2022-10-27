package Cribbage.Cribbage;


public class Pile
{
    private Card[] pile;
    public Pile() {
        this.pile = new Card[0];
    }

    /**
     * @return the size of the pile
     */
    public int size() {
        return this.pile.length;
    }

    public Card get(int index) {
        return this.pile[index];
    }

    public Card append(Card c) {
        Card[] temp = new Card[this.pile.length + 1];
        for(int i = 0; i < this.pile.length; i++) {
            temp[i] = this.pile[i];
        }
        temp[this.pile.length] = c;
        this.pile = temp;
        return c;
    }

    public int totalValue() {
        int ret = 0;
        for(int i = 0; i < this.pile.length; i++) {
            ret += this.pile[i].value;
        }
        return ret;
    }

    public int getAndSayPoints() {
        int fifteen = this.is15() ? 2 : 0;
        int thirtyone = this.is31() ? 2 : 0;
        int pairs = this.isPair() && !this.isTriple() && !this.isQuad()  ? 2 : 0;
        int triples = this.isTriple() && !this.isQuad() ? 6 : 0;
        int quads = this.isQuad() ? 12 : 0;
        int run = this.getRun();
        String buffer = "";
        if(fifteen > 0)
            buffer += "15 for 2 points\n";
        if(thirtyone > 0)
            buffer += "31 for 2 points\n";
        if(pairs > 0)
            buffer += "Pair for 2 points\n";
        if(triples > 0)
            buffer += "Triple for 6 points\n";
        if(quads > 0)
            buffer += "Quad for 12 points\n";
        if(run > 0)
            buffer += "Run of " + run + " for " + run + " points\n";
        if(buffer.length() > 0)
            System.out.println(buffer);
        return fifteen + thirtyone + pairs + triples + quads + run;
    }

    private boolean is15() {
        return this.totalValue() == 15;
    }

    private boolean is31() {
        return this.totalValue() == 31;
    }

    private boolean isPair() {
        if(this.pile.length < 2) {
            return false;
        }
        return this.pile[this.pile.length - 1].rink == this.pile[this.pile.length - 2].rink;
    }

    private boolean isTriple() {
        if(this.pile.length < 3) {
            return false;
        }
        return this.pile[this.pile.length - 1].rink == this.pile[this.pile.length - 2].rink && this.pile[this.pile.length - 2].rink == this.pile[this.pile.length - 3].rink;
    }

    private boolean isQuad() {
        if(this.pile.length < 4) {
            return false;
        }
        return this.pile[this.pile.length - 1].rink == this.pile[this.pile.length - 2].rink && this.pile[this.pile.length - 2].rink == this.pile[this.pile.length - 3].rink && this.pile[this.pile.length - 3].rink == this.pile[this.pile.length - 4].rink;
    }

    private int getRun() {
        int ret = 0;
        if(this.pile.length < 3) {
            return ret;
        }
        for(int i = this.pile.length - 1; i >= 0; i--) {
            if(this.pile[i].rink != this.pile[i - 1].rink + 1) {
                return ret;
            }
            ret++;
        }
        return ret;
    }
    
    @Override
    public String toString() {
        String ret = this.pile[0].toString();
        for (int i = 1; i < this.size(); i++) {
            ret += ", " + this.pile[i].toString();
        }
        return ret;
    }
}
