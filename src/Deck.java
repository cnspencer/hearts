/**
 * @author C. N. Spencer
 *
 */

public class Deck {
    private Card[] cards = new Card[52];
    private int deckSize = cards.length;
    Deck() {
        for (Suits i : Suits.values()) {
            for (Numbers j : Numbers.values()) {
                this.cards[i.ordinal() * j.ordinal()] = new Card(j, i);
            }
        }
    }

    public void shuffle() {
        for (int i = 0; i < this.cards.length; i++) {
            int randIdx = (int)(Math.random() * 52);
            Card cardSwap = this.cards[i];
            this.cards[i] = this.cards[randIdx];
            this.cards[randIdx] = cardSwap;
        }
    }

    public Card dealCard() {
        this.deckSize--;
        return this.cards[this.deckSize];
    }

    public int getDeckSize() {
        return this.deckSize;
    }

    public int getCards() {
        return this.cards.length;
    }
}

