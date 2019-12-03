/**
 * @author C. N. Spencer
 *
 */

public class Player {
    private Card[] hand = new Card[13];
    private String ip;
    private String name;
    private boolean isBot;
    private int score = 0;

    Player(String ip, String name) {
        this.ip = ip;
        this.name = name;
        this.isBot = this.ip.equalsIgnoreCase("bot");
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

    public int getScore() {
        return this.score;
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

    public String toString() {
        return this.ip;
    }

    public void addScore(int points) {
        this.score =+ points;
    }
}