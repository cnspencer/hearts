import javafx.scene.image.Image;

/**
 * @author C. N. Spencer
 *
 */

public class Card {
    private Numbers cardNum;
    private Suits cardType;
    private int value = 0;
    private Image image;
    private Image back = new Image("/GameData/back.png");
    Card(Numbers cardNum, Suits cardType) {
        this.cardNum = cardNum;
        this.cardType = cardType;
        switch (cardNum) {
            case ACE:
                this.value = 14;
            case TWO:
                this.value = 2;
            case THREE:
                this.value = 3;
            case FOUR:
                this.value = 4;
            case FIVE:
                this.value = 5;
            case SIX:
                this.value = 6;
            case SEVEN:
                this.value = 7;
            case EIGHT:
                this.value = 8;
            case NINE:
                this.value = 9;
            case TEN:
                this.value = 10;
            case JACK:
                this.value = 11;
            case QUEEN:
                this.value = 12;
            case KING:
                this.value = 13;
        }
    }

    public Image showBack() {
        return this.back;
    }

    public Image showFront() {
        return this.image;
    }

    public int getValue() {
        return this.value;
    }

    public Suits getSuit() {
        return this.cardType;
    }

    public Numbers getNumber() {
        return this.cardNum;
    }

    @Override
    public String toString() {
        return this.cardNum.toString() + " of " + this.cardType.toString();
    }
}
