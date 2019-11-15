/**
 * @author C. N. Spencer
 *
 */

public class Player {
    private Card[] hand = new Card[13];
    private String ip;
    private boolean isBot;
    Player(String ip) {
        this.ip = ip;
        if (this.ip.equalsIgnoreCase("") || this.ip.equalsIgnoreCase("bot")) {
            this.isBot = true;
        } else {
            this.isBot = false;
        }
    }

    public void addCard(Card card, int deal) {
        this.hand[deal] = card;
    }

    public boolean isBot() {
        return this.isBot;
    }

    public String getIP() {
        return this.ip;
    }

    public Card[] getHand() {
        return this.hand;
    }

    public Card getCard(int index) {
        return this.hand[index];
    }

    public void removeCard(Card card) {
        for (int i = 0; i < this.hand.length; i++) {
            if (this.hand[i].equals(card)) {
                this.hand[i] = null;
            }
        }
    }
}
