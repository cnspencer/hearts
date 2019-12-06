import java.util.ArrayList;

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
        this.isBot = this.ip.contains("bot");
    }

    protected Card botTurnPlay(Card[] played) {
        Numbers num = Numbers.THREE;
        Suits suit = Suits.SPADES;
        if (played.length == 0) {
            if (this.getCard(Numbers.TWO, Suits.CLUBS) != null) {   // if Two of Clubs in hand, play it
                this.removeCard(this.getCard(Numbers.TWO, Suits.CLUBS));
                return this.getCard(Numbers.TWO, Suits.CLUBS);
            }
            for (Card i : this.hand) {
                if (i.getSuit() != Suits.HEARTS) {
                    while (this.getCard(num, suit) == null) {
                        num = this.getRandNum(12, 0);
                        suit = this.getRandSuit(3);
                    }
                } else {
                    while (this.getCard(num, Suits.HEARTS) == null) {
                        num = this.getRandNum(12, 0);
                    }
                    suit = Suits.HEARTS;
                    break;
                }
            }
        } else {
            for (Card i : this.hand) {
                if (i.getSuit() == played[0].getSuit()) {
                    while (this.getCard(num, played[0].getSuit()) == null) {
                        num = this.getRandNum(12, 0);
                    }
                    break;
                } else {
                    while (this.getCard(num, suit) == null) {
                        num = this.getRandNum(12, 0);
                        suit = this.getRandSuit(4);
                    }
                }
            }
        }
        this.removeCard(this.getCard(num, suit));
        return new Card(num, suit);
    }

    protected Card[] botTurnPass() {
        Card[] pass = new Card[3];
        for (Card i : this.hand) {
            if (i.getNumber().ordinal() < Numbers.QUEEN.ordinal() || i.getNumber() == Numbers.ACE) {
                if (i.getSuit() == Suits.SPADES) {
                    for (int j = 0; j < pass.length; j++) {
                        if (pass[j] == null) {
                            pass[j] = i;
                        }
                    }
                } else if (i.getSuit() == Suits.HEARTS) {
                    for (int j = 0; j < pass.length; j++) {
                        if (pass[j] == null) {
                            pass[j] = i;
                        }
                    }
                } else {
                    for (int j = 0; j < pass.length; j++) {
                        if (pass[j] == null) {
                            pass[j] = i;
                        }
                    }
                }
            } else {
                for (int j = 0; j < pass.length; j++) {
                    if (pass[j] == null) {
                        Numbers num = Numbers.JACK;
                        Suits suit = Suits.HEARTS;
                        while (this.getCard(num, suit) == null) {
                            num = this.getRandNum(12, 0);
                            suit = this.getRandSuit(4);
                        }
                        pass[j] = getCard(num, suit);
                    }
                }
            }
        }
        return pass;
    }

    private Numbers getRandNum(int numPossibilities, int lowerLimit) {
        int randNum = Math.floorMod((int)(Math.random() * 10), numPossibilities + 1);
        while (randNum < lowerLimit) {
            randNum = Math.floorMod((int)(Math.random() * 10), numPossibilities + 1);
        }
        switch (randNum) {
            case (0): return Numbers.ACE;
            case (1): return Numbers.TWO;
            case (2): return Numbers.THREE;
            case (3): return Numbers.FOUR;
            case (4): return Numbers.FIVE;
            case (5): return Numbers.SIX;
            case (6): return Numbers.SEVEN;
            case (7): return Numbers.EIGHT;
            case (8): return Numbers.NINE;
            case (9): return Numbers.TEN;
            case (10): return Numbers.JACK;
            case (11): return Numbers.QUEEN;
            case (12): return Numbers.KING;
        }
        return null;
    }

    private Suits getRandSuit(int numPossibilities) {
        int randSuit = Math.floorMod((int)(Math.random() * 10), numPossibilities + 1);
        switch (randSuit) {
            case (0): return Suits.DIAMONDS;
            case (1): return Suits.SPADES;
            case (2): return Suits.CLUBS;
            case (3): return Suits.HEARTS;
        }
        return null;
    }

    public void dealCard(Card card, int deal) {
        /*Adds a card to the designated index*/
        this.hand[deal] = card;
    }

    public void addCard(Card card) {
        /*Adds the card to the next available space*/
        for (int i = 0; i < this.hand.length; i++) {
            if (this.hand[i] == null) {
                this.hand[i] = card;
            }
        }
    }

    public boolean isBot() {
        return this.isBot;
    }

    public String getIP() {
        return this.ip;
    }

    public String getName() {
        return this.name;
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

    public Card getCard(Numbers num, Suits suit) {
        for (Card i : this.hand) {
            if (i.getNumber() == num) {
                if (i.getSuit() == suit) {
                    return i;
                }
            }
        }
        return null;
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

    public boolean hasTwoOfClubs() {
        for (Card i:this.hand) {
            if (i.getNumber().equals(Numbers.TWO)) {
                if (i.getSuit().equals(Suits.CLUBS)) {
                    return true;
                }
            }
        }
        return false;
    }
}